package net.chinawuyue.mls;

public class Constant {

	public static final int MAX_SCROLL_DISTANCE = 1;
	
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
	}
}
