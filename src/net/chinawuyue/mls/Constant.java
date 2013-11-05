package net.chinawuyue.mls;

/**
 * 常量
 * @author Administrator
 *
 */
public class Constant {

	public static final double MAX_SCROLL_DISTANCE = 1.5;
	
	public static class AfterLoanConstan{
		public static final int AFTER_LOAN = 1000;
		
		public static final int KIND_FIRST = AFTER_LOAN + 100;
		public static final int KIND_FIRST_UNFINISH = KIND_FIRST + 1;
		public static final int KIND_FIRST_FINISH = KIND_FIRST + 2;
		public static final int KIND_FIRST_PAST = KIND_FIRST + 3;
		
		public static final int KIND_COMMON = AFTER_LOAN + 200;
		public static final int KIND_COMMON_UNFINISH = KIND_COMMON + 1;
		public static final int KIND_COMMON_FINISH = KIND_COMMON + 2;
		public static final int KIND_COMMON_PAST = KIND_COMMON + 3;
	}
	
	public static class BeforeLoanConstan{
		public static final int BERFORE_LOAN = 2000;
		
		public static final int KIND = BERFORE_LOAN + 100;
		public static final int KIND_UNFINISH = KIND + 1;
		public static final int KIND_FINISH = KIND + 2;
		public static final int KIND_REJECT = KIND + 3;
		
		public static final int REQUEST_CODE = BERFORE_LOAN + 200;
		public static final int RESULT_CODE_TRUE = REQUEST_CODE + 1;
		public static final int RESULT_CODE_FALSE = REQUEST_CODE + 2;
	}
	
	public static class ReportConstants{
		
		public static final int ITEM = 500; // 选项菜单项id
		public static final int ITEM1 = ITEM+1; 
		public static final int ITEM2 = ITEM+2;
		public static final int ITEM3 = ITEM+3;
		public static final int ITEM4 = ITEM+4;
		public static final int ITEM5 = ITEM+5;
		public static final int ITEM6 = ITEM+6;
		public static final int ITEM7 = ITEM+7;
		public static final int ITEM8 = ITEM+8;
		public static final int ITEM9 = ITEM+9;
		public static final int ITEM10 = ITEM+10;
		public static final int ITEM_SETTING = 11;
	}
}
