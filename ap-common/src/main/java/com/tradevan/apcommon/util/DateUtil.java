package com.tradevan.apcommon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.tradevan.apcommon.bean.CommonMsgCode;
import com.tradevan.apcommon.exception.ApRuntimeException;

/**
 * Title: DateUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.1
 */
public class DateUtil {

	public static final String FMT_YYYY = "yyyy";
	public static final String FMT_YYMM = "yyMM";
	public static final String FMT_YYYYMM = "yyyyMM";
	public static final String FMT_YYYYMMDD = "yyyyMMdd";
	public static final String FMT_YYYY_MM = "yyyy/MM";
	public static final String FMT_YYYY_MM_DD = "yyyy/MM/dd";
	public static final String FMT_YYYY_MM_DD_HH_MM = "yyyy/MM/dd-HH:mm";
	public static final String FMT_YYYY_MM_DD_S_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String FMT_YYYY_MM_DD_S_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FMT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd-HH:mm:ss";
	public static final String FMT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String FMT_YYYYMMDDHHMMSS_SSS = "yyyyMMddHHmmssSSS";
	public static final String FMT_HH_MM = "HH:mm";
	public static final String FMT_HH_MM_SS = "HH:mm:ss";
	
	public static Date parseDate(String dateStr, String fmt) {
		if (StringUtils.isNotBlank(dateStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			sdf.setLenient(false);
			try {
				return sdf.parse(dateStr);
			} catch (ParseException e) {
				throw new ApRuntimeException(CommonMsgCode.CODE_W_DATE_FORMAT_ERR, 
						dateStr + " is not a valid date format of " + fmt);
			}
		}
		return null;
	}
	
	public static String formatDate(Date date, String fmt) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			return sdf.format(date);
		}
		return "";
	}
	
	public static Date getSysDate() {
		return parseDate(formatDate(new Date(), FMT_YYYY_MM_DD), FMT_YYYY_MM_DD);
	}
	
	public static Date getYearFirstDate() {
		return parseDate(formatDate(new Date(), FMT_YYYY) + "/01/01", FMT_YYYY_MM_DD);
	}
	
	public static Date getYearLastDate() {
		return parseDate(formatDate(new Date(), FMT_YYYY) + "/12/31", FMT_YYYY_MM_DD);
	}
	
	public static String getSysDateStr(String fmt) {
		return formatDate(new Date(), fmt);
	}
	
	public static String getDateStrByAddMonth(int months, String fmt) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, months); 
		return formatDate(cal.getTime(), fmt);
	}
	
	public static String getDateStrByAddDay(int days, String fmt) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days); 
		return formatDate(cal.getTime(), fmt);
	}
	
	public static Date addYears(Date date, int years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
	
	public static Date addMonths(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months); 
		return cal.getTime();
	}
	
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); 
		return cal.getTime();
	}
	
	public static Date addHours(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hours); 
		return cal.getTime();
	}
	
	public static Date addMinutes(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes); 
		return cal.getTime();
	}
	
	public static boolean isToday(Date date) {
	    return DateUtils.isSameDay(Calendar.getInstance().getTime(), date);
	}
	
	public static Date getSDate() { //get nowYear + 01/01
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 0, 1);
		return cal.getTime();
	}
	
	public static Date getEDate() { //get nowYear + 12/31
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 11, 31);
		return cal.getTime();
	}
	
	public static Date strToDate(String strDate, String strTime) {
		//欲轉換的日期字串
		String dateString = strDate.replace("/", "-") + " " + strTime +":00";
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat(FMT_YYYY_MM_DD_S_HH_MM);
		//進行轉換
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToString(Date date, Boolean hasTime) {
		if(hasTime) {
			return new SimpleDateFormat(FMT_YYYY_MM_DD_S_HH_MM).format(date);
		}
		else {
			return new SimpleDateFormat(FMT_YYYY_MM_DD).format(date);
		}		
	}
}
