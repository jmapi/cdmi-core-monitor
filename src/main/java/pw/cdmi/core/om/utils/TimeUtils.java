package pw.cdmi.core.om.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间计算工具类
 * @author shechaojun
 * @date 2014-9-28 下午4:25:31
 */
public class TimeUtils {

	/**
	 * 计算时间与天数的加减(days为正数则进行相加，为负数则进行相减)
	 * @param date
	 * @param days
	 * @return Date
	 * @author shechaojun
	 */
	public static Date calTime(Date date,int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,+days);
		return cal.getTime();
	}
	
	/**
	 * 将时间格式化为"yyyy-MM-dd HH:mm:ss"的字符串
	 * @param date
	 * @return String
	 * @author shechaojun
	 */
	public static String formatTime(Date date){
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fmt.format(date);
	}
	
	/**
	 * 获取当前时间
	 * @param date
	 * @return Date
	 * @author ninglz
	 */
	public static  Date toDayTime(){
		return new Date();
	}
	
	/**
	 * String类型转Date类型
	 * @param dateString 字符串类型的时间 
	 * @param format 转换格式
	 * @return Date
	 * @author ninglz
	 */
	public static Date stringToDate(String dateString,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
	    try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	    return date;
	}
}
