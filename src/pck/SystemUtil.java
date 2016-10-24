package pck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SystemUtil {

	/**
	 * The index of standard output in the returned array from
	 * {@link #exec(String[])}
	 */
	public static final int STD = 0;
	/**
	 * The index of error output in the returned array from
	 * {@link #exec(String[])}
	 */
	public static final int ERR = 1;

	/**
	 * mode flag for for {@link #getCmdArray(String)}
	 */
	private static final int SKIP_WHITESPACE = 0;
	/**
	 * mode flag for for {@link #getCmdArray(String)}
	 */
	private static final int CAPTURE_CHARS = 1;

	/**
	 * Parses a cmd array and returns the equivalent single-string cmd.
	 * @param cmd a cmd array
	 * @return a single-string cmd
	 */
	public static String getCmdLine(String[] cmd) {
		StringBuilder sb = new StringBuilder();
		boolean space = false;
		for (String c : cmd) {
			sb.append(space ? " " : "");
			space = true;
			if (c.contains(" ")) {
				sb.append("\"");
				sb.append(c);
				sb.append("\"");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Parses a cmd string and returns the equivalent cmd array.
	 * @param cmd a cmd string
	 * @return the cmd array
	 */
	public static String[] getCmdArray(String cmd) {
		int len = cmd.length();
		char c;
		int mode = SKIP_WHITESPACE;
		ArrayList<String> parts = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean quoted = false;
		for (int i = 0; i < len; i++) {
			c = cmd.charAt(i);
			if (mode == SKIP_WHITESPACE) {
				if (Character.isWhitespace(c)) {
					continue;
				} else {
					mode = CAPTURE_CHARS;
					quoted = '"' == c;
					// System.out.println("quoted = " + quoted);
					if (!quoted) {
						sb.append(c);
					}
				}
			} else { // CAPTURE_CHARS
			// System.out.println("quoted = " + quoted + " char = '" + c + "'");
				if (quoted && '"' == c) {
					int j = i - 1;
					int count = 0;
					while (j >= 0 && cmd.charAt(j) == '\\') {
						count++;
						j--;
					}
					if (count % 2 == 1) {
						// odd, so this " is escaped, ignore
						sb.append(c);
					} else { // even
						// even, this is a real quote
						mode = SKIP_WHITESPACE;
					}
				} else if (!quoted && Character.isWhitespace(c)) {
					mode = SKIP_WHITESPACE;
				} else if (!quoted && i == (len - 1)) {
					sb.append(c);
					mode = SKIP_WHITESPACE;
				} else {
					sb.append(c);
				}
				if (mode == SKIP_WHITESPACE) {
					parts.add(sb.toString());
					sb.setLength(0);
				}
			}
		}
		return parts.toArray(new String[parts.size()]);
	}

	/**
	 * Executes a command, specified as a cmd string. This method is equivalent
	 * to calling {@link #getCmdArray(String)} followed by {@link #exec(String[])}.
	 * @param cmd The command to exec
	 * @return a two element array containing the standard output from the command at
	 *         index 0 ({@link #STD}), and the error output at index 1 ({@link #ERR})
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String[] exec(String cmd) throws IOException,	InterruptedException {
		return exec(getCmdArray(cmd));
	}

	/**
	 * Executes a command, specified as a cmd array.
	 * @param cmd the command to exec, the first element should be the path of
	 *            the executable. Subsequent elements are for command
	 *            parameters.
	 * @return a two element array containing the standard output from the command at
	 *         index 0 ({@link #STD}), and the error output at index 1 ({@link #ERR})
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String[] exec(String[] cmd) throws IOException, InterruptedException {
		String std = null, err = null;
		
		Process p = Runtime.getRuntime().exec(cmd);
		try (
			InputStream in = p.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		) {
			FileUtil.copy(in, out);
			std = out.toString();
		}
		try (
			InputStream in = p.getErrorStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		) {
			FileUtil.copy(in, out);
			err = out.toString();
		}
		p.waitFor();
		return new String[] { std, err };
	}

	/**
	 * Convenience method to &quot;append&quot; a String element to an array.
	 * Internally creates a new array of length +1, copies across the existing
	 * Strings and fills the last index supplied String. It then returns the new
	 * array.
	 * @param cmd the existing array
	 * @param elem the String to &quot;append&quot;
	 * @return a new array, based on copying the existing array
	 */
	public static String[] appendToCmdArray(String[] cmd, String elem) {
		String[] expanded = new String[cmd.length + 1];
		expanded[cmd.length] = elem;
		System.arraycopy(cmd, 0, expanded, 0, cmd.length);
		return expanded;
	}
}
