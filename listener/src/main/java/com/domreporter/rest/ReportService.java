package com.domreporter.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.domreporter.db.OutputCSV;
import com.domreporter.db.Report;




@Path("/report")
public class ReportService {
	@POST
	@Path("/html")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response makeReport(@FormParam("page_url") String url,
			@FormParam("start_date") String startDate,
			@FormParam("end_date") String endDate) {
		Date start = new Date();
		Date end = new Date();
		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		try {
			start = formatter.parse(startDate);
			end = formatter.parse(endDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		Report report = new Report();
		String htmlOutput = report.getReport(start, end, url);
		InputStream is = new ByteArrayInputStream(htmlOutput.getBytes());
		ResponseBuilder response = Response.ok((Object) is);
		response.header("Content-Disposition", "attachment; filename=report.html");
		return response.build();
				
	}

	@POST
	@Path("/csv")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response makeCVS(@FormParam("page_url") String url,
			@FormParam("start_date") String startDate,
			@FormParam("end_date") String endDate) {
		Date start = new Date();
		Date end = new Date();
		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		try {
			start = formatter.parse(startDate);
			end = formatter.parse(endDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}		
		OutputCSV csv = new OutputCSV();
		String csvOutput = csv.getCSV(start, end, url);
		InputStream is = new ByteArrayInputStream(csvOutput.getBytes());
		
		ResponseBuilder response = Response.ok((Object) is);
		response.header("Content-Disposition",
			"attachment; filename=report.csv");
		return response.build();
		
	}

}
