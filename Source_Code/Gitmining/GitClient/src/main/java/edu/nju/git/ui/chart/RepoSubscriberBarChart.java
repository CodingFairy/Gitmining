package edu.nju.git.ui.chart;

public class RepoSubscriberBarChart extends MyBarChart{

	@Override
	public String chartName() {
		return "Subscriber Statistic";
	}

	@Override
	public double[] updown() {
		return new double[]{0,1200,300};
	}

}