package net.chinawuyue.mls.reports;

import java.util.Map;
import java.util.Random;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport.ReportType;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
 * 饼状图工厂
 * 
 */
public class PieChartFactory extends AbstractChart {

	private Context context;
	private String balance;
	private String count;
	private String sum;
	private String ratio;

	public PieChartFactory(Context context) {
		this.context = context;
		balance = context.getResources().getString(R.string.balance);
		count = context.getResources().getString(R.string.count);
		sum = context.getResources().getString(R.string.sum);
		ratio = context.getResources().getString(R.string.ratio);
	}

	/**
	 * 返回饼状图视图
	 * 
	 * @param map
	 *            单行数据集合
	 * @param reportType
	 *            报表类型
	 * @param content
	 *            报表内容(笔数、金额、净增、同比)
	 * @return
	 */
	public View getPieChartView(Map<String, Object> map, ReportType reportType,
			String content) throws Exception{
		double[] values = null; // 数据值
		String orgName = null; // 报表标题
		String[] xLabels = null; // 项目标题
		// 判断报表类型
		if (reportType == ReportType.LoanBalance) {
			values = new double[12];
			orgName = map.get(BaseReport.PROJECTNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_balance);
			if (content == balance) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
			} else if (content == count) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
			}
		} else if (reportType == ReportType.BusinessSurvey) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_business_survey);
			if (content == balance) {
				values = new double[8];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
			} else if (content == count) {
				values = new double[8];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
			} else if (content == ratio) {
				values = new double[3];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 6)).toString());
				}
				xLabels = new String[] { xLabels[5], xLabels[6], xLabels[7] };
			}
		} else if (reportType == ReportType.SubjectBalance) {
			orgName = map.get(BaseReport.SUBJECTNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_subject_balance);
			values = new double[6];
			if (content == balance) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
			} else if (content == count) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
			}
		} else if (reportType == ReportType.LoanAnalysis1) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_analysis1);
			values = new double[17];
			if (content == balance) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
			} else if (content == count) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
			} else if (content == sum) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.SUM + (i + 1)).toString());
				}
			} else if (content == ratio) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
			}
		} else if (reportType == ReportType.LoanAnalysis2) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_analysis2);
			values = new double[16];
			if (content == balance) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
			} else if (content == count) {
				values = new double[13];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
			} else if (content == sum) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.SUM + (i + 1)).toString());
				}
			} else if (content == ratio) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
			}
		} else if (reportType == ReportType.LoanAnalysis3) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_analysis3);
			values = new double[10];
			if (content == balance) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
			} else if (content == count) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
			} else if (content == sum) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.SUM + (i + 1)).toString());
				}
			} else if (content == ratio) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
			}
		}
		int[] colors = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			colors[i] = (Color.argb(
					250,
					new Random().nextInt(200)+20, 
					new Random().nextInt(200)+20,
					new Random().nextInt(200)+20));
		}
		if (content.equals(balance)) {
			content = balance + "\n(" + ChartActivity.unitName + ")";
		}
		if (content.equals(count)) {
			content = count + "\n(笔)";
		}
		if (content.equals(sum)) {
			content = sum + "\n(" + ChartActivity.unitName + ")";
		}
		if (content.equals(ratio)) {
			content = ratio + "\n(百分比)";
		}
		return getChartView(values, orgName, content, xLabels,
				 colors);
	}

	// 设置表视图的数据和渲染
	private View getChartView(double[] values, String orgName, String title,
			String[] xLabels,  int[] colors) {
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		renderer.setChartTitleTextSize(20);
		renderer.setDisplayValues(true);
		renderer.setShowLabels(true);
		SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		r.setGradientEnabled(true);
		r.setGradientStart(0, Color.BLUE);
		r.setGradientStop(0, Color.GREEN);
		r.setHighlighted(true);
		View view = ChartFactory.getPieChartView(context,
				buildCategoryDataset(title, values,xLabels), renderer);
		view.setBackgroundColor(Color.BLACK);
		return view;
	}
}
