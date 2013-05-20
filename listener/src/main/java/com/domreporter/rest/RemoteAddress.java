package com.domreporter.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("/ip")
public class RemoteAddress {
	/**
	 * Resource providing IP information of given client. Useful for bypassing same origin policy.
	 * @param request HTTP Request
	 * @return IP address of the remote host
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAddress(@Context HttpServletRequest request) {
		return request.getRemoteAddr();	
	}
}