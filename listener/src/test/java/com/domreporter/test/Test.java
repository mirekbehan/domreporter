package com.domreporter.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.domreporter.db.HtmlSnapshotSaver;
import com.domreporter.model.Click;
import com.domreporter.model.HtmlSnapshot;
import com.domreporter.util.HibernateUtil;



/*
 * A class that tests various database scenarios.
 */
public class Test {
	
	public void testConnection() {
		System.out.println("Hibernate + MySQL");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Click click = new Click();
		click.setElementId(1);
		click.setElementName("A");
		click.setClientSessionId("25463");
		click.setX(50);
		click.setY(100);
		click.setElementX(4);
		click.setElementY(15);
		click.setUrl("http://www.seznam.cz");
		session.save(click);
		session.getTransaction().commit();
	}
	
	public void saveDOM() {
		System.out.println("Hibernate + MySQL");
		HtmlSnapshot dom = new HtmlSnapshot();
		dom.setDom("<html></html>");
		dom.setPageSessionId("123456789");
		dom.setUrl("www.seznam.cz");
		HtmlSnapshotSaver dao = new HtmlSnapshotSaver();
		dao.saveDocumentSnapshot(dom);
		List<HtmlSnapshot> list = dao.loadAllSnapshots();
		for ( HtmlSnapshot ddom : list) {
			System.out.println(ddom.getDom());
		}
		HibernateUtil.rollbackTransaction();
	}
	
	public void testCriteria() {
		System.out.println("Hibernate + MySQL");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse("2013/3/28");
			endDate = format.parse("2013/4/17");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = hibernateSession.createCriteria(HtmlSnapshot.class);
		if(startDate!=null){
			criteria.add(Restrictions.ge("snapshotCreationTime",startDate));
		}
		if(endDate!=null){
			criteria.add(Restrictions.le("snapshotCreationTime",endDate));
		}
		criteria.setMaxResults(1);
		HtmlSnapshot snapshot = new HtmlSnapshot();
		snapshot = (HtmlSnapshot) criteria.uniqueResult();
		System.out.println(snapshot.getId());
	}
	
	@SuppressWarnings("unchecked")
	public void testCriteria2(){
		System.out.println("Hibernate + MySQL");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse("2013/3/28");
			endDate = format.parse("2013/4/17");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Criteria criteria = HibernateUtil.getSessionFactory().openSession().createCriteria(Click.class);
		if(startDate!=null){
			criteria.add(Restrictions.ge("timeOfClick",startDate));
		}
		if(endDate!=null){
			criteria.add(Restrictions.le("timeOfClick",endDate));
		}
		List<Click> clicks =  criteria.list();
		System.out.println(clicks.get(0).getUrl());
		System.out.println(clicks.get(1).getUrl());
	}
	
	@org.junit.Test
	@SuppressWarnings("rawtypes")
	public void testOrderBy(){
		Criteria criteria = HibernateUtil.getSessionFactory().openSession().createCriteria(Click.class);
		criteria.add(Restrictions.eq("url","http://www.freecsstemplates.org/previews/opentools/#"));
		ProjectionList pl = Projections.projectionList();  
		pl.add(Projections.groupProperty("elementId"));
		pl.add(Projections.property("elementName"));
		pl.add(Projections.count("elementId"));
		criteria.setProjection(pl);
		List clicks =  criteria.list();
		
		for (Iterator it = clicks.iterator(); it.hasNext(); ) {
            Object[] myResult = (Object[]) it.next();
            Integer firstName = (Integer) myResult[0];
            String lastName = (String) myResult[1];
            Long count = (Long) myResult[2];
            int haf = count.intValue();
            System.out.println( "Found " + firstName + " " + lastName + " " + haf );
         }
		
	}
}
