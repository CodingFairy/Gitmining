package edu.nju.git.ui.chart;

import edu.nju.git.ui.chart.common.MyBarChart;

public class CompanyUserBarChart extends MyBarChart {

	@Override
	public String chartName() {
		return "User Num of Different Company";
	}

	@Override
	public double[] updown() {
		
		return new double[]{0,350,35};
	}

	

}
