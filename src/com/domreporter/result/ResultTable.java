package com.domreporter.result;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Table;

public class ResultTable extends Table{

	private static final long serialVersionUID = 5687791730379339776L;

	public ResultTable(SQLContainer container, String url){
		super.setCaption("Displaying clicks for "+url);
		setContainerDataSource(container);
		setSizeFull();
		setHeight("500px");
		setWidth("100%");
		setVisibleColumns(new String[] { "x", "y", "element_x", "element_y", "element_id", "element_name", "page_session_id", "click_timestamp"});
	}
}
