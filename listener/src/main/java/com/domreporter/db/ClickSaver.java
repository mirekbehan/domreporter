package com.domreporter.db;


import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.domreporter.model.Click;
import com.domreporter.util.HibernateUtil;



public class ClickSaver {
	/**
	 * Saves a click to a database.
	 * @param click any Click object that should be stored in a database 
	 */
	public void saveClick(Click click) {
		try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = this.getSession();
    		hibernateSession.save(click);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            System.out.println("Couldn't save the click.");	            
        }
	}
	
	protected Session getSession() {
		return HibernateUtil.getSession();
	}
}
