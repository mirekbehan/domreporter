package com.domreporter.db;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.domreporter.model.HtmlSnapshot;
import com.domreporter.util.HibernateUtil;


public class HtmlSnapshotSaver {
	protected Session getSession() {
		return HibernateUtil.getSession();
	}
	
	/**
	 * Saves a snapshot to a database.
	 * @param dom accepts any HtmlSnapshot object
	 */
	public void saveDocumentSnapshot(HtmlSnapshot dom){
	       try {
	            HibernateUtil.beginTransaction();
	            Session hibernateSession = this.getSession();
	    		hibernateSession.save(dom);
	            HibernateUtil.commitTransaction();
	        } catch (HibernateException ex) {
	            System.out.println("Couldn't save the snapshot.");	            
	        }
		
	}
	
	/**
	 * Loads all snapshots from a database.
	 * @test this method is used only in junit db testing
	 * @return a list of all snapshots
	 */
	@SuppressWarnings("unchecked")
	public List<HtmlSnapshot> loadAllSnapshots() {
	      try {	    	  
	            HibernateUtil.beginTransaction();
	            Session hibernateSession = this.getSession();
		        Query query = hibernateSession.createQuery("from DocumentDOM");
		        List<HtmlSnapshot> allSnapshots = query.list();
	            HibernateUtil.commitTransaction();
	            return allSnapshots;
	        } catch (HibernateException ex) {
	            System.out.println("Couldn't retrieve the snapshot list");
	            return null;
	        }		
	        
	}
	
	
}
