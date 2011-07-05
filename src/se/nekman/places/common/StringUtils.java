package se.nekman.places.common;

public class StringUtils {
	
	public final static String EMPTY = "";
	
	public static boolean isEmpty(final String s) {
		return s == null || s.length() == 0;
	}
}
