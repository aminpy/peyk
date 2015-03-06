package org.oruji.peyk;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class TextFormatter {
	private static Map<String, String> treeMap = new TreeMap<String, String>(
			new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					if (s1.length() > s2.length()) {
						return -1;
					} else if (s1.length() < s2.length()) {
						return 1;
					} else {
						return s1.compareTo(s2);
					}
				}
			});

	public static String emoticons(String text) {

		treeMap.put("&gt;:D&lt;", "baghal");
		treeMap.put(":))", "laugh");
		treeMap.put(":((", "cry");
		treeMap.put(":)", "1");
		treeMap.put(":(", "2");
		treeMap.put(":D", "3");
		treeMap.put(":-h", "bye");
		treeMap.put("(:|", "khamyaze");
		treeMap.put("8-&gt;", "rolleyes");
		treeMap.put("B-)", "shades");
		treeMap.put(":\"&gt;", "shy");
		treeMap.put(":-\"", "soot");
		treeMap.put(":x", "love");
		treeMap.put(":o", "taajjob");
		treeMap.put(":-/", "confused");
		treeMap.put("x(", "angry");
		treeMap.put("I-)", "sleep");
		treeMap.put(":-?", "think");
		treeMap.put(":-p", "tongue");
		treeMap.put(":*", "kiss");

		Iterator<Entry<String, String>> it = treeMap.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String myPath = Main.class.getClassLoader()
					.getResource("emoticons/" + pairs.getValue() + ".gif")
					.toString();
			text = text.replaceAll(Pattern.quote(pairs.getKey()), "<img src=\""
					+ myPath + "\" />");
		}

		return text;
	}

	public static String buildStr(String text) {
		text = text.replaceAll(">", "&gt;").replace("<", "&lt;");
		return text;
	}
}
