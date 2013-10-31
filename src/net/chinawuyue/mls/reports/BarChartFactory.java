package net.chinawuyue.mls.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport.ReportType;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
 * 柱状图工厂
 * 
 */
public class BarChartFactory extends AbstractChart {

	private Context context;
	private String balance;
	private String count;
	private String sum;
	private String ratio;

	public BarChartFactory(Context context) {
		this.context = context;
		balance = context.getResources().getString(R.string.balance);
		count = context.getResources().getString(R.string.count);
		sum = context.getResources().getString(R.string.sum);
		ratio = context.getResources().getString(R.string.ratio);
	}

	/**
	 * 返回柱状图视图
	 * 
	 * @param map
	 *            单行数据集合
	 * @param reportType
	 *            报表类型
	 * @param content
	 *            报表内容(笔数、金额、净增、同比)
	 * @return
	 */
	public View getBarChartView(Map<String, Object> map, ReportType reportType,
			String content) throws Exception {
		double[] values = null; // 数据值
		String orgName = null; // 报表标题
		String[] xLabels = null; // 项目标题
		double minValue = 0; // 最大值
		double maxValue = 0; // 最小值
		int barColor = Color.CYAN; // 柱颜色
		// 求各项最大值
		double maxCount = 0, maxBalance = 0, maxSum = 0,maxRatio=0;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().startsWith(BaseReport.COUNT)) {
				double value = Double.parseDouble(entry.getValue().toString());
				if (value > maxCount)
					maxCount = value;
			}
			if (entry.getKey().startsWith(BaseReport.BALANCE)) {
				double value = Double.parseDouble(entry.getValue().toString());
				if (value > maxBalance)
					maxBalance = value;
			}
			if (entry.getKey().startsWith(BaseReport.SUM)) {
				double value = Double.parseDouble(entry.getValue().toString());
				if (value > maxSum)
					maxSum = value;
			}
			if (entry.getKey().startsWith(BaseReport.RATIO)) {
				double value = Double.parseDouble(entry.getValue().toString());
				if (value > maxRatio)
					maxRatio = value;
			}
		}
		// 判断报表类型
		if (reportType == ReportType.LoanBalance) {
			values = new double[12];
			orgName = map.get(BaseReport.PROJECTNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_balance);
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			}
		} else if (reportType == ReportType.BusinessSurvey) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_business_survey);
			if (content.equals(balance)) {
				values = new double[8];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				values = new double[8];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			} else if (content.equals(ratio)) {
				values = new double[3];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 6)).toString());
				}
				xLabels = new String[] { xLabels[5], xLabels[6], xLabels[7] };
				maxValue = maxRatio;
			}
		} else if (reportType == ReportType.SubjectBalance) {
			orgName = map.get(BaseReport.SUBJECTNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_subject_balance);
			values = new double[6];
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			}
		} else if (reportType == ReportType.LoanAnalysis1) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_analysis1);
			values = new double[17];
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			} else if (content.equals(sum)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.SUM + (i + 1)).toString());
				}
				maxValue = maxSum;
			} else if (content.equals(ratio)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
				maxValue = maxRatio;
			}
		} else if (reportType == ReportType.LoanAnalysis2) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_analysis2);
			values = new double[16];
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				values = new double[13];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			} else if (content.equals(sum)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.SUM + (i + 1)).toString());
				}
				maxValue = maxSum;
			} else if (content.equals(ratio)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
				maxValue = maxRatio;
			}
		} else if (reportType == ReportType.LoanAnalysis3) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_analysis3);
			values = new double[10];
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			} else if (content.equals(sum)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.SUM + (i + 1)).toString());
				}
				maxValue = maxSum;
			} else if (content.equals(ratio)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
				maxValue = maxRatio;
			}
		} else if (reportType == ReportType.LoanRate1) {
			orgName = map.get(BaseReport.SUPERORGNAME).toString() +" 排名" +
					  map.get(BaseReport.COUNT+7).toString();
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_rate1);
			if (content.equals(balance)) {
				values = new double[6];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				values = new double[6];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			} else if (content.equals(ratio)) {
				values = new double[3];
				xLabels = context.getResources().getStringArray(
						R.array.report_loan_rate1_ratio);
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.RATIO + (i + 1)).toString());
				}
				maxValue = maxRatio;
			}
		} else if (reportType == ReportType.LoanRate2||
				reportType == ReportType.LoanRate3||reportType == ReportType.LoanRate4) {
			if(reportType==ReportType.LoanRate2){
				orgName = map.get(BaseReport.BUSSINESSNAME).toString();
			}else if(reportType==ReportType.LoanRate3){
				orgName = map.get(BaseReport.VOUCHNAME).toString();
			}else if(reportType==ReportType.LoanRate4){
				orgName = map.get(BaseReport.INDUSTRYNAME).toString();
			}
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_rate2);
			values = new double[3];
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance;
			} else if (content.equals(count)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.COUNT + (i + 1)).toString());
				}
				maxValue = maxCount ;
			} 
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
			if(reportType == ReportType.LoanRate1||reportType == ReportType.LoanRate2||
				reportType == ReportType.LoanRate3||reportType == ReportType.LoanRate4)
			content = "回收率\n(百分比)";
		}
		//保留2位小数
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0;i < values.length;i++){
			values[i]=Double.parseDouble(df.format(values[i]));
		}
		return getChartView(values, orgName, new String[] { content }, xLabels,
				minValue, maxValue*1.3, barColor);
	}

	// 设置表视图的数据和渲染
	private View getChartView(double[] values, String orgName, String[] titles,
			String[] xLabels, double minValue, double maxValue, int barColor) {
		int[] colors = new int[] { barColor }; // 图形颜色
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);// 获得渲染器
		List<double[]> valueList = new ArrayList<double[]>();// 图表数据
		valueList.add(values);
		
		StringBuilder sb = new StringBuilder(orgName);
		for(int i = 0;i < sb.length();i++){
			if(i>0&&i%15==0)
				sb.insert(i, "\n");
		}
		setChartSettings(renderer, sb.toString(), "", "", 0, 9, minValue, maxValue,
				Color.GRAY, Color.LTGRAY); // 设置图表
		for (int i = 0; i < xLabels.length; i++) { // 添加项目标签
			renderer.addXTextLabel(i + 1, xLabels[i]);
		}
		renderer.setShowGrid(true); // 显示网格
		SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);// 获得其中一个渲染器
		r.setGradientEnabled(true); // 设置颜色渐变
		r.setGradientStart(maxValue * 0.1, context.getResources().getColor(R.color.green));
		r.setGradientStop(maxValue * 0.8, Color.BLUE);
		r.setHighlighted(true); // 设置高亮
		View view = ChartFactory.getBarChartView(context, // 创建柱状图
				buildBarDataset(titles, valueList), renderer, Type.DEFAULT);
		view.setBackgroundColor(Color.BLACK); // 设置背景色
		return view;
	}
}
