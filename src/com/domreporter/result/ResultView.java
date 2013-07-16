package com.domreporter.result;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.domreporter.admin.MainView;
import com.domreporter.login.LoginView;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.Compare.Greater;
import com.vaadin.data.util.filter.Compare.Less;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class ResultView extends AbsoluteLayout implements View{

	private static final long serialVersionUID = -7747249458262282381L;

	public static final String NAME = "result";
	
	private Table resultTable;
	private String url;
	
	private SQLContainer createResultContainer() {
		try {
			final JDBCConnectionPool pool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/db", "domreporter", "domreporter");
			TableQuery query = new TableQuery("click", pool);
			SQLContainer container = new SQLContainer(query);
			return container;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public ResultView(String url, Date startDate, Date endDate){
		this.url = url;
		VerticalLayout layout = new VerticalLayout();
		setSizeFull();
		layout.setSizeUndefined();
		layout.setWidth("100%");
		setWidth("100%");
		setHeight("890px");
		
		
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
		
		Label label = new Label("Statistics report");
		label.setStyleName(Reindeer.LABEL_H1);
		layout.addComponent(label);
		
		SQLContainer resultContainer = createResultContainer();
		if (resultContainer.size() == 0) { getUI().getNavigator().navigateTo(MainView.NAME); Notification.show("No data", Type.HUMANIZED_MESSAGE); }
		resultContainer.addContainerFilter(new Greater("click_timestamp", startDate));
		resultContainer.addContainerFilter(new Less("click_timestamp", endDate));
		resultContainer.addContainerFilter(new Equal("url", url));
		
		resultTable = new ResultTable(resultContainer, url);
		layout.addComponent(resultTable);
		layout.setExpandRatio(resultTable, 1);
		Button exportButton = new Button("Export XLS");
		exportButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -73954695086117200L;
	        private ExcelExport excelExport;
			public void buttonClick(ClickEvent event) {
				 excelExport = new ExcelExport(resultTable);
	             excelExport.excludeCollapsedColumns();
	             excelExport.setReportTitle("XLS Report");
	             excelExport.setDisplayTotals(false);
	             excelExport.setExportFileName("Table.xls");
	             excelExport.export();

			}
		});
		exportButton.setIcon(new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/WEB-INF/xls_icon.png")));
		layout.addComponent(exportButton);
		ResultChart chartDays = getResultChart("Days chart", ResultChart.COLUMN_TIME, "Day");
		ResultChart chartElements = getResultChart("Elements chart", ResultChart.COLUMN_ELEMENT, "Element");
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.addComponent(chartDays);
		hl.addComponent(chartElements);
		layout.addComponent(hl);
		
		Button backButton = new Button( "Back");
		backButton.setStyleName(BaseTheme.BUTTON_LINK);
		backButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
	            getUI().getNavigator().navigateTo(MainView.NAME);
			}
		});		
		
		layout.setStyleName(Reindeer.LAYOUT_BLUE);
		addComponent(layout,"left: 0%; right: 0%;" +
                			"top: 0%; bottom: 0%;");
		
		addComponent(logoutButton, "right: 0px; top: 0px;");
		addComponent(backButton, "right: 48px; top: 0px;");
		System.out.println(layout.getHeight());
		

	}
	public ResultChart getResultChart(String caption, String column, String seriesLabel){
		// Get items from the table and put them into a hash map
		HashMap<String, Integer> occMap = new HashMap<String, Integer>();
		Object[] ids = resultTable.getItemIds().toArray();
		ArrayList<String> elementNames = new ArrayList<String>();

		for (int x = 0; x < ids.length; x++) {
			if (column.equals(ResultChart.COLUMN_TIME)) {
				Date day = (Date)resultTable.getItem(ids[x]).getItemProperty(column).getValue();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				elementNames.add(df.format(day)); 
			}else{
				elementNames.add((String) resultTable.getItem(ids[x]).getItemProperty(column).getValue());
			}
						
		}
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(elementNames);

		for (String s: hs) {
			int count = Collections.frequency(elementNames, s);
			occMap.put(s, count);
		}	
				
		
		
		ResultChart resultChart = new ResultChart(caption, occMap, seriesLabel);
		resultChart.setSizeFull();
		return resultChart;		
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
		//	TODO
		
	}

}
