package com.domreporter.login;

import java.sql.SQLException;

import com.domreporter.admin.ErrorView;
import com.domreporter.admin.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;


@Title("Administration area")
@Theme("reindeer")
public class AdminUI extends UI {
	private static final long serialVersionUID = 336221542383210458L;

	private SQLContainer createUrlContainer() {
		try {
			final JDBCConnectionPool pool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/db", "domreporter", "domreporter");
			//TableQuery query = new TableQuery("document_dom", pool);
			FreeformQuery query = new FreeformQuery("SELECT DISTINCT url FROM click", 
					pool);//, "url");
			SQLContainer container = new SQLContainer(query);
			
			return container;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
    protected void init(VaadinRequest request) {
		getSession().getService().setSystemMessagesProvider(new SystemMessagesProvider() {
			private static final long serialVersionUID = 1L;

			@Override
			public SystemMessages getSystemMessages(
					
			    SystemMessagesInfo systemMessagesInfo) {
	        CustomizedSystemMessages messages =
	                new CustomizedSystemMessages();
	        messages.setInternalErrorNotificationEnabled(false);
	        return messages;
			}
		});
        //
        // Create a new instance of the navigator. The navigator will attach
        // itself automatically to this view.
        //
        new Navigator(this, this);

        SQLContainer container= createUrlContainer();
             

        
        //
        // Add views to the application
        //
        getNavigator().addView(LoginView.NAME, LoginView.class);
        
        getNavigator().addView(MainView.NAME, new MainView(container));
        
        getNavigator().addView(ErrorView.NAME, ErrorView.class);
        
        
        getNavigator().setErrorView(new ErrorView());

        getNavigator().addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                
                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }
            
            @Override
            public void afterViewChange(ViewChangeEvent event) {
                
            }
        });
        
        
     // Configure the error handler for the UI
        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
 			private static final long serialVersionUID = 1L;
			@Override
            public void error(com.vaadin.server.ErrorEvent event) {
                // Find the final cause
                String cause = "";// = "Oops, something went wrong :-(";
                for (Throwable t = event.getThrowable(); t != null;
                     t = t.getCause())
                    if (t.getCause() == null) // We're at final cause
                        cause += t.getClass().getName() + " ";
                
                // Display the error message in a custom fashion
                Notification.show("Something went wrong:", cause,
                        Type.ERROR_MESSAGE);
             } 
        });
    }	
	
	
}
