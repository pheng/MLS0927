package net.chinawuyue.mls.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport.ReportType;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * 线状图工厂
 * 
 */
public class LineChartFactory extends AbstractChart {

	private Context context;
	private String balance; // 字符串 金额
	private String count; // 字符串 笔数
	private String sum; // 字符串 净增
	private String ratio; // 字符串 同比

	public LineChartFactory(Context context) {
		this.context = context;
		// 读取字符串资源
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
	public View getLineChartView(Map<String, Object> map,
			ReportType reportType, String content)throws Exception {
		double[] values = null; // 数据值
		String orgName = null; // 报表标题
		String[] xLabels = null; // 项目标题
		double minValue = 0; // 最大值
		double maxValue = 0; // 最小值
		int barColor = Color.GREEN; // 柱颜色
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
			// 读取报表项目字符串数组
			xLabels = context.getResources().getStringArray(
					R.array.report_loan_balance);
			if (content.equals(balance)) {
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(map.get(
							BaseReport.BALANCE + (i + 1)).toString());
				}
				maxValue = maxBalance ;
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
				maxValue = maxBalance ;
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
				maxValue = maxBalance ;
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
				maxValue = maxBalance ;
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
				maxValue = maxSum ;
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
				maxValue = maxBalance ;
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
				maxValue = maxSum ;
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
				maxValue = maxBalance ;
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
				maxValue = maxSum ;
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
				orgName = map.get(BaseReport.BUSSINESSNAME).toString() +" 回收率" +
					  map.get(BaseReport.RATIO + 4).toString();
			}else if(reportType==ReportType.LoanRate3){
				orgName = map.get(BaseReport.VOUCHNAME).toString() +" 回收率" +
						  map.get(BaseReport.RATIO + 4).toString();
			}else if(reportType==ReportType.LoanRate4){
				orgName = map.get(BaseReport.INDUSTRYNAME).toString() +" 回收率" +
						  map.get(BaseReport.RATIO + 4).toString();
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
		List<double[]> xValues = new ArrayList<double[]>();
		double[] x = new double[values.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		xValues.add(x);
		List<double[]> yValues = new ArrayList<double[]>();
		yValues.add(values);
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
		return getChartView(xValues, yValues, orgName,
				new String[] { content }, xLabels, minValue, maxValue*1.3,
				new int[] { barColor });
	}

	// 设置表视图的数据和渲染
	private View getChartView(List<double[]> xValues, List<double[]> yValues,
			String orgName, String[] titles, String[] xLabels, double minValue,
			double maxValue, int[] colors) {

		XYMultipleSeriesRenderer renderer = buildRenderer(colors,
				new PointStyle[] { PointStyle.DIAMOND });
		
		StringBuilder sb = new StringBuilder(orgName);
		for(int i = 0;i < sb.length();i++){
			if(i>0&&i%15==0)
				sb.insert(i, "\n");
		}
		setChartSettings(renderer, sb.toString(), "", "", 0.5, 10, minValue,
				maxValue, Color.GRAY, Color.LTGRAY);
		renderer.setXLabels(0);// 设置x轴不显示数字
		renderer.setYLabels(10);// 设置y轴显示10个点,根据setChartSettings的最大值和最小值自动计算点的间隔
		for (int i = 0; i < xLabels.length; i++) {
			renderer.addXTextLabel(i + 1, xLabels[i]);
		}
		renderer.getSeriesRendererAt(0).setColor(Color.GREEN);
		renderer.setShowGrid(true);// 是否显示网格
		renderer.setXLabelsAlign(Align.LEFT);// 刻度线与刻度标注之间的相对位置关系
		View view = ChartFactory.getLineChartView(context,
				buildDataset(titles, xValues, yValues), renderer);
		view.setBackgroundColor(Color.BLACK);
		return view;
	}
}
