package fr.paris.lutece.plugins.statistics.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateHelper {

	private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private static final long MONTH_IN_MILLIS = DAY_IN_MILLIS * 31;

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String DDMMYYYY_SLASH = "dd/MM/yyyy";
	public static final String DDMM_SLASH = "dd/MM";
	public static final String YYYYMMDD_DASH = "yyyy-MM-dd";
	public static final String DDMMYYYY_DASH = "dd-MM-yyyy";
	public static final String YYYYMMDDHHMMSS_DASH = "yyyy-MM-ddHHmmss";
	public static final String YYYYMMDDTHHMMSS_DASH_COLONS = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String YYYYMMDDHHMMSS_DASH_COLONS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDHHMM_DASH_COLONS = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMDDHHMMSS_SLASH_COLONS = "yyyy/MM/dd HH:mm:ss";
	public static final String YYYYDDMMHHMMSS_COLONS = "yyyyddMM HH:mm:ss";
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static final String HHHMM = "HH'h'mm";
	public static final String HHHMMMNSSS = "HH'h'mm'mn'ss's'";
	public static final String DDMMYYYYHHMM_SLASH = "dd/MM/yyyy HH:mm";
	public static final String DDMMYYYYHHMM_SLASH_BIS = "dd/MM/yyyy '&nbsp;&nbsp;&nbsp;' HH:mm";
	public static final String DDMMHHMM_SLASH = "dd/MM HH:mm";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DDMMYYYYHHMMSSSSS = "ddMMyyyyHHmmssSSS";
	public static final String MMDDYYYYHHMMSS_SLASH_COLONS = "MM/dd/yyyy HH:mm:ss";
	public static final String MMDDYYYY_SLASH = "MM/dd/yyyy";
	public static final String YYYYMMDDHHMMSS_POINT_COLONS = "yyyy.MM.dd HH:mm:ss";
	public static final String YYYYMMDDHHMMSSSSS_DASH_COLONS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String YYYYMMDDTHHMMSS_DASH_COLONS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	// JSON Format
	public static final String YYYYMMDDHHMMSSZ_DASH = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	private static final FormattersThreadCache FMT_CACHE = new FormattersThreadCache();

	static class FormattersThreadCache extends ThreadLocal<Map<String, SimpleDateFormat>> {
		@Override
		protected Map<String, SimpleDateFormat> initialValue() {
			return new HashMap<String, SimpleDateFormat>();
		}

		public SimpleDateFormat get(final String pattern) {
			final Map<String, SimpleDateFormat> map = get();
			SimpleDateFormat sdf = map.get(pattern);
			if (sdf == null) {
				sdf = new SimpleDateFormat(pattern);
				map.put(pattern, sdf);
			}
			return sdf;
		}
	};

	/**
	 * Create a Date object according to the given format and date.
	 * 
	 * @param pattern
	 *            A String that match the given date format
	 * @param strDate
	 *            The date to parse
	 * @return a Date object that represent the given date
	 */
	public static Date parse(final String pattern, final String strDate) {
		try {
			if (pattern != null && strDate != null) {
				return FMT_CACHE.get(pattern).parse(strDate);
			} else {
				return null;
			}
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param pattern
	 *            A String that represent the output date format
	 * @param date
	 *            The date that will be formated
	 * @return A String version of the Date according to the given format
	 */
	public static String format(final String pattern, final Date date) {
		String strDate = "";
		if (date != null) {
			strDate = FMT_CACHE.get(pattern).format(date);
		}
		return strDate;
	}

	/**
	 * Get an independently initialized copy of the SimpleDateFormat according to
	 * the given pattern
	 * 
	 * @param pattern
	 *            Pattern of the returned SimpleDateFormat
	 * @return a SimpleDateFormat
	 */
	public static SimpleDateFormat getFormat(final String pattern) {
		return FMT_CACHE.get(pattern);
	}

	/**
	 * Compute a date X day far from a reference date
	 * 
	 * @param date
	 *            reference date
	 * @param xDay
	 *            number of day to add (or decrease if negative)
	 * @return the computed date
	 */
	public static Date getDateXDayAfter(final Date date, final int xDay) {

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, xDay);
		return cal.getTime();
	}

	/**
	 * Compute a date X month far from a reference date
	 * 
	 * @param date
	 *            reference date
	 * @param xMonth
	 *            number of month to add (or decrease if negative)
	 * @return the computed date
	 */
	public static Date getDateXMonthAfter(final Date date, final int xMonth) {

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, xMonth);
		return cal.getTime();
	}

	private static final int TIME_STAMP_REFERENCE_YEAR = 1970;
	private static final int MONTHS_IN_YEAR = 12;
	private static final int DAYS_IN_HALF_MONTH = 15;

	/**
	 * Get the number of months between two dates
	 * 
	 * @param date1
	 * @param date2
	 * @return diffrence in months bettween two dates
	 */
	public static int nbOfMonthsBetweenTwoDates(final Date date1, final Date date2) {
		final Date date = new Date(Math.abs(date2.getTime() - date1.getTime()));
		final Calendar gcal = Calendar.getInstance();
		gcal.setTime(date);

		final int years = gcal.get(Calendar.YEAR);
		int months = gcal.get(Calendar.MONTH);

		months = months + (years - TIME_STAMP_REFERENCE_YEAR) * MONTHS_IN_YEAR;
		final int days = gcal.get(Calendar.DAY_OF_MONTH);
		if (days >= DAYS_IN_HALF_MONTH) {
			months++;
		}
		return months;
	}

	/**
	 * Get the number of months between two dates without the absolute value
	 * 
	 * @param date1
	 * @param date2
	 * @return diffrence in months bettween two dates
	 */
	public static int nbOfMonthsBetweenTwoDatesNotAbs(final Date date1, final Date date2) {
		long diff = date2.getTime() - date1.getTime();

		long months = diff / MONTH_IN_MILLIS;

		return (int) months;
	}

	/**
	 * Set the date with the year, month and day, if the day is greater than the max
	 * day for the month, the date will be the first of next month
	 * 
	 * @param year
	 * @param month
	 *            Attention: month begins by 0, that is, 0 for January, ..., 11 for
	 *            December
	 * @param day
	 * @return
	 */
	public static Date getDate(final int year, final int month, final int day) {
		final Calendar calendar = Calendar.getInstance();
		// set the first of the month
		calendar.set(year, month, 1);

		// get the max day of the month
		final int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (day > maxDay) {
			// pass to the next month
			calendar.add(Calendar.MONTH, 1);
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, day);
		}
		return calendar.getTime();
	}

	/**
	 * Methode to know if the given dates is in the same day
	 * 
	 * @param date1
	 *            date to check
	 * @param date2
	 *            date to check
	 * @return true if the given date is today
	 */
	public static boolean isSameDay(final Date date1, final Date date2) {
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * Methode to know if the given date is after today
	 * 
	 * @param date
	 *            date to check
	 * @return true if the given date is after today
	 */
	public static boolean isDayAfterToday(final Date date) {
		final Calendar today = Calendar.getInstance();
		today.setTime(new Date());

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return (cal.get(Calendar.ERA) > today.get(Calendar.ERA) || cal.get(Calendar.YEAR) > today.get(Calendar.YEAR)
				|| cal.get(Calendar.DAY_OF_YEAR) > today.get(Calendar.DAY_OF_YEAR));
	}

}
