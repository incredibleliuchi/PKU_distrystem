package util;

import java.util.ArrayList;
import java.util.List;


public class CmdLineUtils {
	private CmdLineUtils() {}
	
	public static String[] parse(String line) throws Exception {
		line = line.trim();
		if (line.length() == 0) return new String[0];
		List<String> args = new ArrayList<>();
		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ') i ++;
		args.add("-" + line.substring(0, i));
		while (i < line.length() && line.charAt(i) == ' ') i ++;
		while (i < line.length()) {
			StringBuffer sb = new StringBuffer();
			if (line.charAt(i) == '"') {
				i ++;
				while (i < line.length()) {
					if (line.charAt(i) == '\\') {
						if (i + 1 >= line.length()) throw new IllegalArgumentException("Nothing after '\\'");
						sb.append(line.charAt(i + 1));
						i += 2;
					} else if (line.charAt(i) == '"') break;
					else {
						sb.append(line.charAt(i));
						i ++;
					}
				}
				if (i == line.length()) {
					throw new IllegalArgumentException("'\"' does not match");
				} else {
					i ++;
				}
			} else {
				while (i < line.length()) {
					if (line.charAt(i) == '\\') {
						if (i + 1 >= line.length()) throw new IllegalArgumentException("Nothing after '\\'");
						sb.append(line.charAt(i + 1));
						i += 2;
					} else if (line.charAt(i) == ' ' || line.charAt(i) == '"') break; 
					else {
						sb.append(line.charAt(i));
						i ++;
					}
				}
			}
			args.add(sb.toString());
			while (i < line.length() && line.charAt(i) == ' ') i ++;
		}
		return args.toArray(new String[args.size()]);
	}
}
