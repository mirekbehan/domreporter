package com.domreporter.rest;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/download")
public class ExtensionDownload {
	// download service
	@GET
	@Produces("application/x-chrome-extension")
	public Response download(){
		InputStream in = ExtensionDownload.class.getResourceAsStream("../DOM_Reporter.crx");
		ResponseBuilder response = Response.ok((Object) in);
		response.header("Content-Disposition",
			"attachment; filename=\"domreporter.crx\"");
		return response.build();
	}

}
