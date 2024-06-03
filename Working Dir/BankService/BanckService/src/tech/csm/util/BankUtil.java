package tech.csm.util;

public class BankUtil {
	
	private static int ac=1000;
	
	public static String generateAccountNo() {
		return "BA"+(ac++);
	}

}
