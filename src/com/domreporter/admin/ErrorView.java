package com.domreporter.admin;

import com.domreporter.login.LoginView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class ErrorView extends AbsoluteLayout implements View{
	public static final String NAME = "error";
	
	public ErrorView(){
		HorizontalLayout hLayout = new HorizontalLayout();
		setSizeFull();
		hLayout.setSizeFull();
		
		Label headerLabel = new Label("Requested view is not available, please continue ");
		Button redirectButton = new Button( "here");
		redirectButton.setStyleName(BaseTheme.BUTTON_LINK);
		redirectButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				  getUI().getNavigator().navigateTo(MainView.NAME);
			}
		});
		hLayout.setWidth(545, Unit.PIXELS);
		hLayout.addComponent(headerLabel);
		hLayout.addComponent(redirectButton);
		
		addComponent(hLayout,"left: 0%; right: 0%;" +
    			"top: 0%; bottom: 0%;");		
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
