package edu.nju.git.ui.chart;

public class RepoLanguageBarChart extends MyBarChart {

	@Override
	public String chartName() {
		return "Language Statistic";
	}

	@Override
	public double[] updown() {
		return new double[]{0,1000,200};
	}

}