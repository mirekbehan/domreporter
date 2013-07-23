package com.domreporter.result;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.domreporter.admin.MainView;
import com.domreporter.login.LoginView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class FrameView extends AbsoluteLayout implements View {
	private static final long serialVersionUID = 1L;

	public static final String NAME = "html";
	
	private BrowserFrame browser;
	

	private FileDownloader fd;
	Button exportButton;
	
	public FrameView(String url, Date startDate, Date endDate){
		VerticalLayout layout = new VerticalLayout();
		setSizeFull();
		layout.setSizeFull();
		
		Button logoutButton = new Button( "Logout");
		logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
		logoutButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				  Notification.show("You have been logged out",
	                        Type.HUMANIZED_MESSAGE);
				  	// "Logout" the user
		            getSession().setAttribute("user", null);
    	            // Redirect to login view
		            getUI().getNavigator().navigateTo(LoginView.NAME);
			}
		});		
		
		Label headerLabel = new Label("Visualization");
		headerLabel.setStyleName(Reindeer.LABEL_H1);
		layout.addComponent(headerLabel);
		
		exportButton = new Button("Export HTML");
		exportButton.setIcon(new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/WEB-INF/html_icon.png")));
		
		try {
			fd = new FileDownloader(new StreamResource(getHtmlReportSource(url,startDate,endDate),"report.html"));
			fd.extend(exportButton);
		} catch (Exception e1) {
			e1.printStackTrace();			
		}

		try {			
			browser = new BrowserFrame("HTML page generated from the server:");//,
			browser.setSizeFull();
			StreamResource sr = new StreamResource(getHtmlReportSource(url,startDate,endDate),"report.html");
			sr.setCacheTime(0);
			browser.setSource(sr);
			layout.addComponent(browser);
			layout.setExpandRatio(browser, 1);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		Button backButton = new Button( "Back");
		backButton.setStyleName(BaseTheme.BUTTON_LINK);
		backButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
	            getUI().getNavigator().navigateTo(MainView.NAME);
			}
		});		
		
		layout.addComponent(exportButton);
		
		layout.setStyleName(Reindeer.LAYOUT_BLUE);
		addComponent(layout,"left: 0%; right: 0%;" +
                			"top: 0%; bottom: 0%;");
		
		addComponent(logoutButton, "right: 0px; top: 0px;");
		addComponent(backButton, "right: 48px; top: 0px;");
	}
	
	public StreamSource getHtmlReportSource(String url, Date startDate, Date endDate) throws Exception{
		String serviceUrl = "http://localhost:8080/dr-listener/report/html";
		URL obj = new URL(serviceUrl);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		String sDate = formatter.format(startDate);
		String eDate = formatter.format(endDate);
		String urlParameters = "page_url="+URLEncoder.encode(url,"UTF-8")+"&start_date="+sDate+"&end_date="+eDate;
		
		connection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		
		int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + serviceUrl);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close(); 
			
	       final byte[] htmlData = response.toString().getBytes(); 
           StreamResource.StreamSource ss = new StreamResource.StreamSource() {
               private static final long serialVersionUID = 8337866232702077402L;

               InputStream is = new BufferedInputStream(new ByteArrayInputStream(htmlData));
               public InputStream getStream() {
                   return is;
               }
           };
		
		return ss;

	}
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
