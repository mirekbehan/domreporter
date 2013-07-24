package com.domreporter.rest;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/download")
public class ExtensionDownloadService {
	@GET
	@Produces("application/x-chrome-extension")
	public Response download(){
		InputStream in = ExtensionDownloadService.class.getResourceAsStream("../ClickSender.crx");
		ResponseBuilder response = Response.ok((Object) in);
		response.header("Content-Disposition",
			"attachment; filename=\"ClickSender.crx\"");
		return response.build();
	}

}
