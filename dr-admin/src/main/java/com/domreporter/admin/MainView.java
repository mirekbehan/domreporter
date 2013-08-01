package com.domreporter.admin;

import java.util.Date;
import java.util.Locale;

import com.domreporter.result.FrameView;
import com.domreporter.result.ResultView;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class MainView extends AbsoluteLayout implements View {

	private static final long serialVersionUID = 5062318498513788508L;

	public static final String NAME = "";
	
	private PopupDateField startDateField;
	private PopupDateField endDateField;
	private final CheckBox overlayChkBox;
	private Table urlTable;
	SQLContainer container;

	public MainView(SQLContainer urlContainer){
		this.container = urlContainer;
		setWidth("100%");
		setHeight("100%");
		VerticalLayout layout = new VerticalLayout();
		Label headerLabel = new Label("Admin area");
		headerLabel.setStyleName(Reindeer.LABEL_H1);
		layout.addComponent(headerLabel);
		
		Button logoutButton = new Button( "Logout");
		logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
		logoutButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				  Notification.show("You have been logged out",
	                        Type.HUMANIZED_MESSAGE);
				  	// "Logout" the user
		            getSession().setAttribute("user", null);
		            // Refresh this view, should redirect to login view
		            getUI().getNavigator().navigateTo(NAME);
			}
		});
		
							
		FormLayout form = new FormLayout();
		
		urlTable = new UrlSelectionTable(urlContainer);
		layout.addComponent(urlTable);
	
		startDateField = new PopupDateField("Start date");
		startDateField.setValue(new Date());
		startDateField.setImmediate(true);
		startDateField.setLocale(Locale.US);
		startDateField.setResolution(Resolution.MINUTE);
		
		form.addComponent(startDateField);
		
		endDateField = new PopupDateField("End date");
		endDateField.setValue(new Date());
		endDateField.setImmediate(true);
		endDateField.setLocale(Locale.US);
		endDateField.setResolution(Resolution.MINUTE);
		
		form.addComponent(endDateField);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		
		
		Button reportButton = new Button("Report");
		
		reportButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String urlString = String.valueOf(container.getContainerProperty(urlTable.getValue(), "url"));
				if (urlString == "null"){
					Notification.show("Select a URL!",
	                        Type.HUMANIZED_MESSAGE);					
				}else if(startDateField.getValue().compareTo(endDateField.getValue())>0){
					Notification.show("Start date should be before end date",
	                        Type.HUMANIZED_MESSAGE);
				}
				else {				
				getSession().setAttribute("testdate", startDateField.getValue());
				getUI().getNavigator().addView(ResultView.NAME, new ResultView(String.valueOf(container.getContainerProperty(urlTable.getValue(), "url")), startDateField.getValue(), endDateField.getValue()));				
				getUI().getNavigator().navigateTo(ResultView.NAME);
				}
			}
		});
		
		hlayout.addComponent(reportButton);
		overlayChkBox = new CheckBox("overlay");
		Button visualizeButton = new Button("Visualisation");
		visualizeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String urlString = String.valueOf(container.getContainerProperty(urlTable.getValue(), "url"));
				
				
				if (urlString == "null"){
					Notification.show("Select a URL!",
	                        Type.HUMANIZED_MESSAGE);					
				}else if(startDateField.getValue().compareTo(endDateField.getValue())>0){
					Notification.show("Start date should be before end date",
	                        Type.HUMANIZED_MESSAGE);
				}
				else {
					getUI().getNavigator().addView(FrameView.NAME, new FrameView(urlString, startDateField.getValue(), endDateField.getValue(), overlayChkBox.getValue()));				
					getUI().getNavigator().navigateTo(FrameView.NAME);
				}
				
			}
		});
		

		hlayout.addComponent(visualizeButton);
		
		hlayout.addComponent(overlayChkBox);
		form.addComponent(hlayout);
		
		layout.addComponent(form);
		
		
		addComponent(layout,"left: 0%; right: 0%;" +
                			"top: 0%; bottom: 0%;");
		
		addComponent(logoutButton, "right: 0px; top: 0px;");
			
	}


	public void enter(ViewChangeEvent event) {
		Notification.show("Welcome to the administration area");
		
	}
}
