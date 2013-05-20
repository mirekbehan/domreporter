package com.domreporter.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.domreporter.db.ClickSaver;
import com.domreporter.model.Click;






@Path("/click")
public class ClickService {
	/**
	 * Using Jersey implementation of JAX-RS for consuming requests. This method saves posted clicks.
	 * @param click json object that contains all needed information about a click
	 * @return confirmation message
	 */
	@POST
	@Path("/post")
	@Consumes("application/json")
	public String postClick(Click click){
		ClickSaver dao = new ClickSaver();
		dao.saveClick(click);
		return "Click received.";
	}
}
