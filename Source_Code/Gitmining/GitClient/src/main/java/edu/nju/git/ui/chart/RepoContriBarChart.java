package edu.nju.git.ui.chart;

import edu.nju.git.ui.chart.common.MyBarChart;

public class RepoContriBarChart extends MyBarChart{

	@Override
	public String chartName() {
		return "Contributor Statistic";
	}

	@Override
	public double[] updown() {
		return new double[]{0,1500,300};
	}

}
