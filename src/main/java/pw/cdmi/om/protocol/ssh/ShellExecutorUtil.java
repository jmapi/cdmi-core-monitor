package pw.cdmi.om.protocol.ssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.EofMatch;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import expect4j.matches.TimeoutMatch;

public class ShellExecutorUtil {

	final static Logger logger = LoggerFactory.getLogger(ShellExecutorUtil.class);

	private Session session;

	private ChannelShell channel;

	private Expect4j expect = null;

	private static final long defaultTimeOut = 1000;

	private StringBuffer buffer = new StringBuffer();

	public static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;

	public static final String BACKSLASH_R = "\r";

	public static final String BACKSLASH_N = "\n";

	public static final String COLON_CHAR = ":";

	public static String ENTER_CHARACTER = BACKSLASH_R;

	// public static final int SSH_PORT = 22;

	// 正则匹配，用于处理服务器返回的结果s
	public static String[] linuxPromptRegEx = new String[] { "~]#", "~#", "#", ":~#", "/$", ">" };

	public static String[] errorMsg = new String[] { "could not acquire the config lock " };

	// // ssh服务器的ip地址
	// private String ip;
	//
	// // ssh服务器的登入端口
	// private int port;
	//
	// // ssh服务器的登入用户名
	// private String user;
	//
	// // ssh服务器的登入密码
	// private String password;

	public ShellExecutorUtil(String ip, int port, String user, String password) throws JSchException, IOException {
		// this.ip = ip;
		// this.port = port;
		// this.user = user;
		// this.password = password;
		JSch jsch = new JSch();
		session = jsch.getSession(user, ip, port);
		session.setPassword(password);
		Hashtable<String, String> config = new Hashtable<String, String>();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		localUserInfo ui = new localUserInfo();
		session.setUserInfo(ui);
		session.connect();
		channel = (ChannelShell) session.openChannel("shell");
		this.expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
		channel.connect();
	}

	/**
	 * 关闭SSH远程连接
	 */
	public void disconnect() {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
		if (expect != null) {
			expect.close();
		}
	}

	/**
	 * 获取服务器返回的信息
	 * @return 服务端的执行结果
	 */
	public String getResponse() {
		return buffer.toString();
	}

	// // 获得Expect4j对象，该对用可以往SSH发送命令请求
	// private Expect4j getExpect() {
	//
	// }

	public boolean testOracleCommand(String[] commands) {
		if (expect == null) {
			return false;
		}
		Closure closure = new Closure() {
			public void run(ExpectState expectState) throws Exception {
				buffer.append(expectState.getBuffer());
				expectState.exp_continue();
			}
		};
		List<Match> lstPattern = new ArrayList<Match>();
		String[] regEx = linuxPromptRegEx;
		if (regEx != null && regEx.length > 0) {
			for (String regexElement : regEx) {
				try {
					RegExpMatch mat = new RegExpMatch(regexElement, closure);
					lstPattern.add(mat);
				} catch (MalformedPatternException e) {
					logger.error(e.getMessage());
					return false;
				} catch (Exception e) {
					logger.error(e.getMessage());
					return false;
				}
			}
			lstPattern.add(new EofMatch(new Closure() {
				public void run(ExpectState state) {
				}
			}));
			lstPattern.add(new TimeoutMatch(defaultTimeOut, new Closure() {
				public void run(ExpectState state) {
				}
			}));
		}
		try {
			boolean isSuccess = true;
			for (int i = 0; i < commands.length; i++) {
				if (i == (commands.length - 1)) {
					isSuccess = isSuccess(lstPattern, commands[i], i, Integer.parseInt(defaultTimeOut + ""));
				} else {
					isSuccess = isSuccess(lstPattern, commands[i], 0, Integer.parseInt(defaultTimeOut + ""));
				}

			}
			// 防止最后一个命令执行不了
			isSuccess = !checkResult(expect.expect(lstPattern));

			// 找不到错误信息标示成功
			String response = buffer.toString().toLowerCase();
			for (String msg : errorMsg) {
				if (response.indexOf(msg) > -1) {
					return false;
				}
			}
			if ((response.contains("sql>") || response.contains("SQL>")) && (isSuccess == true)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			logger.error("执行expect ssh时候出现异常，引起原因：" + ex.getMessage());
			return false;
		} finally {
			try {

				expect.close();

				channel.disconnect();
				session.disconnect();
			} catch (Throwable e) {
				// 不处理
				logger.debug("关闭expect的Session和channel时候出现异常，原引起因：" + e.getMessage());
			}
		}
	}

	/**
	 * 执行Oracle配置命令
	 * @param commands 要执行的命令，为字符数组
	 * @return 执行是否成功
	 */
	public List<String> executeOracleCommands(String[] commands, Integer outTime) {
		// 如果expect返回为0，说明登入没有成功
		List<String> ls = new ArrayList<String>();
		if (expect == null) {
			return null;
		}
		Closure closure = new Closure() {
			public void run(ExpectState expectState) throws Exception {
				buffer.append(expectState.getBuffer());
				expectState.exp_continue();
			}
		};
		List<Match> lstPattern = new ArrayList<Match>();
		String[] regEx = linuxPromptRegEx;
		if (regEx != null && regEx.length > 0) {
			for (String regexElement : regEx) {
				try {
					RegExpMatch mat = new RegExpMatch(regexElement, closure);
					lstPattern.add(mat);
				} catch (MalformedPatternException e) {
					return null;
				} catch (Throwable e) {
					return null;
				}
			}
			lstPattern.add(new EofMatch(new Closure() {
				public void run(ExpectState state) {
				}
			}));
		}
		try {
			boolean isSuccess = true;
			String command = null;
			for (int i = 0; i < commands.length; i++) {
				if (i == (commands.length - 1)) {
					command = commands[i];
					isSuccess = isSuccess(lstPattern, commands[i], i, outTime);
					if (isSuccess) {
						continue;
					} else {
						return null;
					}
				} else {
					isSuccess = isSuccess(lstPattern, commands[i], 0, outTime);
					if (isSuccess) {
						continue;
					} else {
						return null;
					}
				}

			}
			// 防止最后一个命令执行不了
			isSuccess = !checkResult(expect.expect(lstPattern));

			// 找不到错误信息标示成功
			String response = buffer.toString().toLowerCase();
			for (String msg : errorMsg) {
				if (response.indexOf(msg) > -1) {
					return null;
				}
			}
			if (isSuccess == true) {
				if (response.contains("no rows selected")) {
					ls.add("no rows selected");
				} else {
					ls = SplitStringBuffer(response, command);
				}
				return ls;
			} else {
				return null;
			}
		} catch (Exception ex) {
			logger.error("使用expect包执行SSH时候出现错误，原因为：" + ex.toString());
			return null;
		} finally {
			try {
				channel.disconnect();
				session.disconnect();
			} catch (Throwable ex) {
				// 不处理
				logger.debug("关闭channel或session时出现异常，原因为：" + ex.toString());
			}
		}
	}

	private List<String> SplitStringBuffer(String response, String command) throws IOException {
		List<String> statuses = new ArrayList<String>();
		if (response.length() > 0) {
			response = response.trim().substring(command.length(), response.trim().length());

			String[] statusesArray = response.split("\n");
			int changeLine = 3;
			String header = null;
			for (int i = 0; i < statusesArray.length; i++) {
				if (i > 1 && i < (statusesArray.length - 2)) {
					if (i != changeLine) {

						if (null != statusesArray[i] && !statusesArray[i].equals("\r")
								&& !statusesArray[i].contains("rows selected")) {
							if (statusesArray[i].contains(" ---")) {
								continue;
							}
							statusesArray[i] = statusesArray[i].replace("\t", "");
							// 获取第一个-的位置
							Integer line = statusesArray[i].indexOf("-");
							if (line > 0) {
								if (header != null) {
									if (header.length() > line && header.substring(0, line).split("-").length > 1
											&& header.indexOf("-") > line) {

										statusesArray[i] = "- " + statusesArray[i];
									}
								}
							}
							if (i == 2) {
								header = statusesArray[i];
							}
							statusesArray[i] = statusesArray[i].trim();
							statuses.add(statusesArray[i]);
						} else if (statusesArray[i].equals("\r")) {
							changeLine = i + 2;
							i = i + 2;
						}
					}
				}
			}
		}
		return statuses;
	}

	// public static void write(String path, String content, String encoding)
	// throws IOException {
	// File file = new File(path);
	// file.delete();
	// file.createNewFile();
	// BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
	// new FileOutputStream(file), encoding));
	// writer.write(content);
	// writer.close();
	// }
	//
	// public static String read(String path, String encoding) throws IOException {
	// String content = "";
	// File file = new File(path);
	// BufferedReader reader = new BufferedReader(new InputStreamReader(
	// new FileInputStream(file), encoding));
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// content += line + "\n";
	// }
	// reader.close();
	// return content;
	// }

	// 检查执行是否成功
	private boolean isSuccess(List<Match> objPattern, String strCommandPattern, int strCommandIndex, Integer outTime) {
		try {
			long time = 1000;
			if (strCommandPattern.toLowerCase().contains("select") && outTime != null) {
				time = outTime.intValue() * 1000;
			}
			objPattern.add(new TimeoutMatch(time, new Closure() {
				public void run(ExpectState state) {
				}
			}));

			boolean isFailed = checkResult(expect.expect(objPattern));
			if (!isFailed) {
				expect.send(strCommandPattern);
				expect.send("\r");
				String response = buffer.toString().toLowerCase();
				if (!response.equals("") && !response.contains("#>")) {
					if (!(response.contains("sql>") || response.contains("SQL>"))) {
						return false;
					}
				}
				if (strCommandIndex > 0) {
					buffer = new StringBuffer();
				}
				return true;
			}
			return false;
		} catch (MalformedPatternException ex) {
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	// 检查执行返回的状态
	private boolean checkResult(int intRetVal) {
		if (intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE) {
			return true;
		}
		return false;
	}

	// 登入SSH时的控制信息
	// 设置不提示输入密码、不显示登入信息等
	public static class localUserInfo implements UserInfo {
		String passwd;

		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {

		}
	}

}