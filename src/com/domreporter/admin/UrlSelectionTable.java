package com.domreporter.admin;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Table;

public class UrlSelectionTable extends Table {

	public UrlSelectionTable(SQLContainer container){
		super.setCaption("Select a URL address from the list below:");
		setContainerDataSource(container);
		setSizeFull();
		setHeight("280px");
		setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		setSelectable(true);
		setVisibleColumns(new String[]{"url"});
	}

}
