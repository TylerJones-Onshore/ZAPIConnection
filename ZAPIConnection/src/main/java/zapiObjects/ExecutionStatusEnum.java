package zapiObjects;

public class ExecutionStatusEnum {
	public enum StatusCode {
		UNEXECUTED(-1), PASS(1), FAIL(2), WIP(3), BLOCKED(4), INTEST(5), RETEST(6);

		private final int statusCode;

		public int getStatusCode() {
			return statusCode;
		}

		private StatusCode(int code) {
			statusCode = code;
		}
	};

	public static int getStatusCode(String status) {
		int statusCode;
		if (status.equalsIgnoreCase("UNEXECUTED")) {
			statusCode = StatusCode.UNEXECUTED.getStatusCode();
		} else if (status.equalsIgnoreCase("PASS")) {
			statusCode = StatusCode.PASS.getStatusCode();
		} else if (status.equalsIgnoreCase("FAIL")) {
			statusCode = StatusCode.FAIL.getStatusCode();
		} else if (status.equalsIgnoreCase("WIP")) {
			statusCode = StatusCode.WIP.getStatusCode();
		} else if (status.equalsIgnoreCase("BLOCKED")) {
			statusCode = StatusCode.BLOCKED.getStatusCode();
		} else if (status.equalsIgnoreCase("INTEST")) {
			statusCode = StatusCode.INTEST.getStatusCode();
		} else if (status.equalsIgnoreCase("RETEST")) {
			statusCode = StatusCode.RETEST.getStatusCode();
		} else {
			statusCode = 9999999;
		}
		
		return statusCode;
	}

}
