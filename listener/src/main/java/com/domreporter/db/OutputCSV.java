package com.domreporter.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.domreporter.model.Click;
import com.domreporter.util.HibernateUtil;


public class OutputCSV {
	protected Session getSession() {
		return HibernateUtil.getSession();
	}
	
	/**
	 * Retrieves clicks from a database and formats them into a CSV.
	 * @param startDate 
	 * @param endDate
	 * @param url url of a page for which CSV file will be generated
	 * @return a comma separated values (CSV) String retrieved from a database
	 */
	@SuppressWarnings("unchecked")
	public String getCSV(Date startDate, Date endDate, String url){
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
		criteria.addOrder(Order.asc("elementName"));
		List<Click> clicks =  criteria.list();

		StringBuffer writer = new StringBuffer();

		writer.append("Report for URL: ");
		writer.append(clicks.get(0).getUrl());
		writer.append('\n');
		
		writer.append("Tag name");
		writer.append(',');
		writer.append("Element ID");
		writer.append(',');
		writer.append("Absolute x");
		writer.append(',');
		writer.append("Absolute y");
		writer.append(',');
		writer.append("Session ID");
		writer.append(',');
		writer.append("Time");
		writer.append('\n');

		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		for (int i = 0; i < clicks.size(); i++) {
			writer.append(clicks.get(i).getElementName());
			writer.append(',');
			writer.append(Integer.toString(clicks.get(i).getElementId()));
			writer.append(',');
			writer.append(Integer.toString(clicks.get(i).getX()));
			writer.append(',');
			writer.append(Integer.toString(clicks.get(i).getY()));
			writer.append(',');
			writer.append(clicks.get(i).getClientSessionId());
			writer.append(',');
			writer.append(formatter.format((clicks.get(i).getTimeOfClick())));
			writer.append('\n');
		}		
		
		return writer.toString();
	}
}
