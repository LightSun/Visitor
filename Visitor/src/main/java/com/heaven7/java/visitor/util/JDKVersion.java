package com.heaven7.java.visitor.util;

/**
 * help we handle version info
 * @author heaven7
 * @since 1.1.2
 */
public final class JDKVersion {

	private static final String VERSION;
	static {
		VERSION = System.getProperty("java.version");
	}

	public static boolean isJdK18() {
		return VERSION != null && VERSION.startsWith("1.8");
	}

	/*public static void main(String[] args) {
		System.out.println(isJdK18());
	}*/

}
