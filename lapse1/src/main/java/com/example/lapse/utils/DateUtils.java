package com.example.lapse.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	  private DateUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	  //convert Date format to Calendar format
	  public static  Calendar dateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
	  }

	  //Check if start date is <= end date
	  public static boolean startDateBeforeEndDate(Calendar start, Calendar end) {
		  if(start.getTimeInMillis() <= end.getTimeInMillis()) {
			  return true;
		  }
		  else return false;
	  };
	  
	  //Trim hour, min, sec, millisecond of a Date away
	  public static Date trim(Date date) {
	      Calendar cal = Calendar.getInstance();
	      cal.clear();
	      cal.setTime( date );
	      cal.set(Calendar.HOUR_OF_DAY, 0);
	      cal.set(Calendar.MINUTE, 0);
	      cal.set(Calendar.SECOND, 0);
	      cal.set(Calendar.MILLISECOND, 0);
	      return cal.getTime();
	 }

	  //Remove weekends when days applied  <=14
	  public static float removeWeekends(Calendar start, Calendar end) {
		  float daysWithoutWeekends = 0;
		  do {
			  if (start.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
					  start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {;
					  daysWithoutWeekends++; }
			  start.add(Calendar.DAY_OF_MONTH, 1);
		  } 
		  while (start.getTimeInMillis() <= end.getTimeInMillis());
		  return daysWithoutWeekends;

	  }
	  //Count the weekdays that is a public Holiday
	  public static float countWeekDayPH(Calendar start, Calendar end) {
		  float WeekdayPH = 0f;

			if (start.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				;
				WeekdayPH++;
			}
			if(!(end.equals(start))) {
				if (end.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
						&& end.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					;
					WeekdayPH++;
				}
			}
				return WeekdayPH;  
	  }

}
