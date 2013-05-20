package com.domreporter.rest;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import com.sun.jersey.api.view.Viewable;
 

@Path("/admin")
public class JspController {
	/**
	 * Index.jsp resource for administrators.
	 * @param request HTTP Request
	 * @return index.jsp
	 */
    @GET
    @Path("/index")
    public Viewable index(@Context HttpServletRequest request) {
        request.setAttribute("obj", new String("IT Works"));
        System.out.println("/INDEX called");
        return new Viewable("/index.jsp", null);
    }
    
    @GET
    @Path("/datetimepicker")
    public Viewable dpStylesheet(@Context HttpServletRequest request) {
        return new Viewable("/datetimepicker_css.js", null);
    }

}
