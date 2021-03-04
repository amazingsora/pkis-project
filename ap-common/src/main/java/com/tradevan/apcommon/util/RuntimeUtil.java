package com.tradevan.apcommon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tradevan.apcommon.exception.ApRuntimeException;

/**
 * Title: RuntimeUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class RuntimeUtil {

	private static Logger logger = LoggerFactory.getLogger(RuntimeUtil.class);

	public static void exec(String cmd) {
		exec(cmd);
	}
	
	public static String exec(String cmd, String compareKey) {
		String compareResult = "";
		InputStream is = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			is = process.getErrorStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = in.readLine()) != null) {
				if (compareKey != null && line.contains(compareKey)) {
					compareResult = line;
					break;
				}

			}
			process.waitFor();
		}
		catch (Exception e) {
			throw new ApRuntimeException(e);
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return compareResult;
	}

}
