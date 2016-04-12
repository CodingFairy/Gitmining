package edu.nju.git.ui.chart;

import edu.nju.git.ui.chart.common.MyPieChart;

public class UserTypePieChart extends MyPieChart{

	@Override
	public String chartName() {
		return "User Type Between User and Orgnization";
	}

	@Override
	protected double getHeight() {
		return 580;
	}

	@Override
	protected double getWidth() {
		return 800;
	}


}
