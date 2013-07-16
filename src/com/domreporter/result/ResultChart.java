package com.domreporter.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.Series;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ResultChart extends VerticalLayout{

	private static final long serialVersionUID = -4103272020223675934L;
	
	public static final String COLUMN_TIME = "click_timestamp";
	
	public static final String COLUMN_ELEMENT = "element_name";

	public ResultChart(String caption, HashMap<String, Integer> inputData, String seriesLabel) {
		Label label = new Label(caption);
		addComponent(label);
		
		
		List<Integer> data = new ArrayList<Integer>(inputData.values());
		List<String> ticks = new ArrayList<String>(inputData.keySet());
		
		Collections.reverse(data);
		Collections.reverse(ticks);
		
		Ticks t = new Ticks();
		for (int i = 0; i < ticks.size(); i++) {
			t.add(ticks.get(i));
		}
				
		DataSeries dataSeries = new DataSeries();
		Object[] objects = new Object[data.size()];
			
		for (int j = 0; j < objects.length; j++) {
			objects[j] = data.get(j);
		}
		
		dataSeries.add(objects);
		
		Series series = new Series()
			.addSeries(
					new XYseries()
					.setLabel(seriesLabel));
					
		SeriesDefaults seriesDefaults = new SeriesDefaults()
		.setRenderer(SeriesRenderers.BAR);

		Axes axes = new Axes()
		.addAxis(
				new XYaxis()
				.setRenderer(AxisRenderers.CATEGORY)
				.setTicks(t));
								
		Highlighter highlighter = new Highlighter()
		.setShow(true)
		.setShowTooltip(true)
		.setTooltipAlwaysVisible(true)
		.setKeepTooltipInsideChart(true)
		.setTooltipLocation(TooltipLocations.NORTH)
		.setUseAxesFormatters(true)
		.setTooltipAxes(TooltipAxes.XY_BAR);
		

		Options options = new Options()
		.setSeriesDefaults(seriesDefaults)
		.setSeries(series)
		.setAxes(axes)
		.setHighlighter(highlighter);

		DCharts chart = new DCharts()
		.setDataSeries(dataSeries)
		.setOptions(options)
		.show();
		
		
		setSizeFull();
		chart.setWidth("100%");
		addComponent(chart);
	}

}
