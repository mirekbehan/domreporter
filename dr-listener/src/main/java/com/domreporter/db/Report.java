package com.domreporter.db;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.domreporter.model.Click;
import com.domreporter.model.GroupedClick;
import com.domreporter.model.HtmlSnapshot;
import com.domreporter.util.HibernateUtil;

@SuppressWarnings("unchecked")
public class Report {
	private HtmlSnapshot snapshot;
	private List<Click> clicks;
	private List<GroupedClick> groupedClicks;
	
	public final static float GRC = 0.618033988749895f;
	private float h = 0;
	
	protected Session getSession() {
		return HibernateUtil.getSession();
	}
	
	/**
	 * Creates an html report that will be send to an administrator via admin jsp page.
	 * This method connects to a database and transforms plain html to a DOM.
	 * It also connects to a database and retrieves all Click objects within a given time range.
	 * Finally it takes advantage of html DOM representation and adds CSS styles and
	 * html tags that will construct the click map and element heat map. 
	 * @param startDate start date and time
	 * @param endDate end date and time
	 * @param url a url of the page for which a user wants the report
	 * @return a html page String that contains additional styles which highlight spots where users clicked
	 */
	public String getReport(Date startDate, Date endDate, String url, Boolean overlay){
		try {
			HibernateUtil.beginTransaction();
			snapshot = getSnapshot(startDate, endDate, url);
			clicks = getClicks(startDate, endDate, url);
			HibernateUtil.commitTransaction();
		
		Document doc = Jsoup.parse(snapshot.getDom());
		Element body = doc.body();
		Element currentElement = null;
		String color = getRandomColor();
		for (int i = 0; i < clicks.size(); i++) {
			if (i > 0) {
				if (!(clicks.get(i).getClientSessionId().equals(clicks.get(
						i - 1).getClientSessionId()))) {
					color = getRandomColor();					
				}
			}
			int left = clicks.get(i).getElementX() - 5; // -5 is adjusting value
			int top = clicks.get(i).getElementY() - 5;  // to center the circle
			
			if (clicks.get(i).getElementId() == -5) {
				currentElement = body;
			}
			if (clicks.get(i).getElementId() == -10) {
				currentElement = body.parent();
			}
			if (clicks.get(i).getElementId() != -5 && clicks.get(i).getElementId() != -10) {
				currentElement = body.select("[data-id=" + clicks.get(i).getElementId() + "]").first();
			}
			if(overlay){
				currentElement.append("<span style='background-color: "
						+ color
						+ "; width: 10px; height: 10px; border-top-left-radius: 20px; border-top-right-radius: 20px; border-bottom-right-radius: 20px; border-bottom-left-radius: 20px; position: absolute; left:"
						+ left + "px; top: " + top + "px; display:block; z-index: 9999;'></span>");
			}else{			
				currentElement.append("<span style='background-color: "
						+ color
						+ "; width: 9px; height: 9px; border-top-left-radius: 20px; border-top-right-radius: 20px; border-bottom-right-radius: 20px; border-bottom-left-radius: 20px; position: absolute; left:"
						+ left + "px; top: " + top + "px; display: block;border: 1px solid black;'></span>");
			}			
		}
		
		
		 // Each object has three attributes: elementId, corresponding
		 // elementName and a count of all clicks within a given element.
		 // Click count is very important for another method which computes
		 // the element heat color.
		 
		groupedClicks = groupByElementIds(startDate, endDate, url);
		for (int i = 0; i < groupedClicks.size(); i++) {
			String styles = "position: relative;";
			  if(!overlay){
					styles= "position: relative; background-color: "					
					+ getHeatColor(clicks.size(), groupedClicks.get(i)
							.getElementIdCount())+";";
					}
			if (groupedClicks.get(i).getElementId() != -5
					&& groupedClicks.get(i).getElementId() != -10) {
				currentElement = body
						.select("[data-id="
								+ groupedClicks.get(i).getElementId() + "]")
						.first();
				
				if (groupedClicks.get(i).getElementName()
						.equalsIgnoreCase("IMG")
						|| groupedClicks.get(i).getElementName()
								.equalsIgnoreCase("HR")
						|| groupedClicks.get(i).getElementName()
								.equalsIgnoreCase("INPUT")) {
					currentElement.wrap("<div style='" + styles + "'></div>");
				} else {
					currentElement.attr("style", styles);
				}

				
			} else {
				currentElement = body;
				String temp1 = body.attr("style");
				body.attr("style",styles + " " + temp1);
			}
		
				
				
		}
		// set z-index to auto for all elements - is needed for overlay
		if(overlay) {
			doc.head().append("<style>*:not(span){ z-index: auto !important;} .dr_overlay {position:fixed; width:100%;	height:100%; top:0; left:0; right:0; bottom:0;"
					+ "background-color:rgba(100, 100, 100, 0.7);"
					+ "background: url(data:;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAABl0RVh0U29mdHdhcmUAUGFpbnQuTkVUIHYzLjUuNUmK/OAAAAATSURBVBhXY2RgYNgHxGAAYuwDAA78AjwwRoQYAAAAAElFTkSuQmCC) repeat scroll transparent\\9;"
					+"z-index:9990; color:white;}</style>");
			doc.body().append("<div class='dr_overlay'>&nbsp;</div>");
		}


		// disable overflow:hidden if any
		String temp2 = "overflow: visible !important;" + body.attr("style");
		body.attr("style",temp2);
		
		
		return doc.html();
		} catch (Exception e) {
			System.out.println("Couldn't get the report.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates a list of GroupedClick objects that represent aggregated elements that has been clicked.
	 * For example if an element has been clicked 3 times, only one GroupedClick object will be created.
	 * This object will also hold a count value so we safely say that 3 clicks hit the element. 
	 * @param startDate start date
	 * @param endDate end date
	 * @param url a url of the page for which a user wants the report
	 * @return a list of GroupedClick objects
	 */
	@SuppressWarnings("rawtypes")
	public List<GroupedClick> groupByElementIds(Date startDate, Date endDate, String url){
		Criteria criteria = HibernateUtil.getSessionFactory().openSession().createCriteria(Click.class);
		if(startDate!=null){
			criteria.add(Restrictions.ge("timeOfClick",startDate));
		}
		if(endDate!=null){
			criteria.add(Restrictions.le("timeOfClick",endDate));
		}
		if(url!=null){
			criteria.add(Restrictions.eq("url",url));
		}
		ProjectionList pl = Projections.projectionList();  
		pl.add(Projections.groupProperty("elementId"));
		pl.add(Projections.property("elementName"));
		pl.add(Projections.count("elementId"));
		criteria.setProjection(pl);
		List clicks =  criteria.list();
		List<GroupedClick> gcList = new ArrayList<GroupedClick>();
		for (Iterator it = clicks.iterator(); it.hasNext(); ) {
            Object[] myResult = (Object[]) it.next();
            Integer elemId = (Integer) myResult[0];
            String elemName = (String) myResult[1];
            Long count = (Long) myResult[2];
            gcList.add(new GroupedClick(elemId, elemName, count.intValue()));            
         }
		return gcList;
		
	}
	
	/**
	 * Gets one snapshot of the desired page.
	 * @param startDate start date
	 * @param endDate end date
	 * @param url a url of the page for which a user wants the report
	 * @return html snapshot
	 */
	public HtmlSnapshot getSnapshot(Date startDate, Date endDate, String url){
		Criteria criteria = this.getSession().createCriteria(HtmlSnapshot.class);
		if(startDate!=null && endDate!=null && url!=null){
			criteria.add(Restrictions.ge("snapshotCreationTime",startDate));
		
			criteria.add(Restrictions.le("snapshotCreationTime",endDate));
		
			criteria.add(Restrictions.eq("url",url));
		}else{
			System.out.println("No HTML snapshot: null argument(s)");
			return null;
			
		}
		criteria.setMaxResults(1);
		HtmlSnapshot htmlSnapshot = new HtmlSnapshot();
		htmlSnapshot = (HtmlSnapshot) criteria.uniqueResult();
		return htmlSnapshot;
	}
	
	/**
	 * Gets all clicks within a time range. It also orders clicks by session id.
	 * That way we can easily distinguish clicks depending on from which user a click was received.
	 * @param startDate start date
	 * @param endDate end date
	 * @param url a url of the page for which a user wants the report
	 * @return ordered list of clicks
	 */
	public List<Click> getClicks(Date startDate, Date endDate, String url){
		Criteria criteria = this.getSession().createCriteria(Click.class);
		if(startDate!=null){
			criteria.add(Restrictions.ge("timeOfClick",startDate));
		}
		if(endDate!=null){
			criteria.add(Restrictions.le("timeOfClick",endDate));
		}
		if(url!=null){
			criteria.add(Restrictions.eq("url",url));
		}
		criteria.addOrder(Order.asc("clientSessionId"));
		return criteria.list();
	}
	
	/**
	 * Gets heat color.
	 * @param clicksTotal max value
	 * @param value number of clicks of an element 
	 * @return color string in rgb(r,g,b) format
	 */
	public String getHeatColor(int clicksTotal, int value){
		int oldMax = clicksTotal/2;
		int oldMin = 0;
		int range = oldMax - oldMin;
		int colorRange = 100;
		int res = Math.round(((value - oldMin) * colorRange) / range);
		
		float hue = 0.125f; //hue
		float lumination= 1.0f; //lumination
		String heatColor;
			
		Color col = Color.getHSBColor(hue, res*0.01f, lumination);
		heatColor = "rgb(" +
				col.getRed() + "," + col.getGreen() + "," +
				col.getBlue()+		")";
		return heatColor;
	}
	
	/**
	 * Gets random color.
	 * @return a string in html color format, for example rgb(252,50,118)
	 */
	public String getRandomColor(){
		String rColor;
		h += GRC;
		h %= 1;
		Color col = Color.getHSBColor(h, 0.8f, 0.99f);
		rColor = "rgb(" +
				col.getRed() + "," + col.getGreen() + "," +
				col.getBlue()+		")";
		return rColor;
	}	
	
}
