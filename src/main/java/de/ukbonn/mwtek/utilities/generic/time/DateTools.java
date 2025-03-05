/*
 * Copyright (C) 2021 University Hospital Bonn - All Rights Reserved You may use, distribute and
 * modify this code under the GPL 3 license. THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT
 * PERMITTED BY APPLICABLE LAW. EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR
 * OTHER PARTIES PROVIDE THE PROGRAM “AS IS” WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH
 * YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR
 * OR CORRECTION. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL ANY
 * COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS THE PROGRAM AS PERMITTED ABOVE,
 * BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF DATA
 * OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE
 * PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGES. You should have received a copy of the GPL 3 license with *
 * this file. If not, visit http://www.gnu.de/documents/gpl-3.0.en.html
 */

package de.ukbonn.mwtek.utilities.generic.time;

import de.ukbonn.mwtek.utilities.ExceptionTools;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * Auxiliary class, with various tools to convert / create date fields.
 *
 * @author <a href="mailto:david.meyers@ukbonn.de">David Meyers</a>
 */
@Slf4j
public class DateTools {

  public static final ZoneId timeZoneEuropeBerlin = ZoneId.of("Europe/Berlin");
  public static final String GMT = "GMT";

  /**
   * Merging a date and a time (necessary because of weird database design where date and time are )
   *
   * <p>Care with using java.util.date to merge dates because its kinda bugged!
   *
   * @param date {@link Date} with date information
   * @param time {@link Date} with time information
   * @param nullifyMilliseconds Should the millisecond field be explicitly set to 0?
   * @return A {@link Date} object, initialized with the passed (separated) date and the passed
   *     time.
   */
  public static Date mergeToDateTime(Date date, Date time, Boolean nullifyMilliseconds) {
    // Set DefaultValues
    ExceptionTools.checkNull("date", date);
    ExceptionTools.checkNull("time", time);

    Calendar aDate = Calendar.getInstance();
    aDate.setTime(date);

    Calendar aTime = Calendar.getInstance();
    aTime.setTime(time);

    Calendar aDateTime = Calendar.getInstance();
    aDateTime.set(Calendar.DAY_OF_MONTH, aDate.get(Calendar.DAY_OF_MONTH));
    aDateTime.set(Calendar.MONTH, aDate.get(Calendar.MONTH));
    aDateTime.set(Calendar.YEAR, aDate.get(Calendar.YEAR));
    aDateTime.set(Calendar.HOUR_OF_DAY, aTime.get(Calendar.HOUR_OF_DAY));
    aDateTime.set(Calendar.MINUTE, aTime.get(Calendar.MINUTE));
    aDateTime.set(Calendar.SECOND, aTime.get(Calendar.SECOND));
    // Unused orbis milliseconds can interfere with the matching of episodes, for example by
    // overlaying them in the margin -> very fuzzy debugging (e.g. tiss-score 1325498)
    if (nullifyMilliseconds) {
      aDateTime.set(Calendar.MILLISECOND, 0);
    }

    return aDateTime.getTime();
  }

  /**
   * Transformation a unixtime (micros) in a date with truncating of the time data
   *
   * @param unixTimeInMicros UnixTime in microseconds
   * @param timeZone Optional: {@link TimeZone} of the date (default: GMT)
   * @return A {@link Date} object without time information
   */
  public static Date unixTimeMicrosToDateExtinguishedTime(
      long unixTimeInMicros, TimeZone timeZone) {
    if (timeZone == null) {
      timeZone = TimeZone.getTimeZone("GMT");
    }

    Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTimeInMillis((long) unixTimeInMicros / 1000);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    return calendar.getTime();
  }

  /**
   * Transformation an unix time (seconds) in a date with truncating of the time data
   *
   * @param unixTimeInSeconds UnixTime in seconds
   * @param timeZone Optional: {@link TimeZone} of the date (default: GMT)
   * @return A {@link Date} object without time information
   */
  public static Date unixTimeSecondsToDateExtinguishedTime(
      long unixTimeInSeconds, TimeZone timeZone) {
    if (timeZone == null) {
      timeZone = TimeZone.getTimeZone("GMT");
    }
    return unixTimeMicrosToDateExtinguishedTime(unixTimeInSeconds * 1000, timeZone);
  }

  /**
   * Conversion of a Unix time in microseconds into a Java {@link java.util.Date#getDate() date}
   * object
   *
   * @param unixTimeInMicros unix time in microseconds
   * @return A {@link Date} object initialized with the given unix time
   */
  public static Date unixTimeMicrosToDate(long unixTimeInMicros) {
    Date date = new Date();
    date.setTime(unixTimeInMicros / 1000);
    return date;
  }

  /**
   * Conversion of a Unix time in milliseconds into a Java {@link java.util.Date#getDate() date}
   * object
   *
   * @param unixTimeInMillis unix time in milliseconds
   * @return A {@link Date} object initialized with the given unix time
   */
  public static Date unixTimeMillisToDate(long unixTimeInMillis) {
    Date date = new Date();
    date.setTime(unixTimeInMillis);
    return date;
  }

  /**
   * Conversion of a Unix time in seconds into a Java {@link java.util.Date#getDate() date} object
   *
   * @param unixTimeInSeconds unix time in seconds
   * @return A {@link Date} object initialised with the given Unixtime
   */
  public static Date unixTimeSecondsToDate(long unixTimeInSeconds) {
    Date date = new Date();
    date.setTime(unixTimeInSeconds * 1000);
    return date;
  }

  /**
   * Conversion of a Unix time in microseconds into a Java {@link Instant} object
   *
   * @param unixTimeInMicros unix time in microseconds
   * @return A {@link Instant} object initialized with the given unixtime
   */
  public static Instant unixTimeToInstant(long unixTimeInMicros) {
    return Instant.ofEpochSecond(unixTimeInMicros);
  }

  /**
   * Converting a {@link Date} object into a numeric Unix time value
   *
   * @param date A {@link Date} object
   * @return Unixtime in seconds
   */
  public static Long dateToUnixTime(Date date) {
    if (date != null) {
      return date.getTime() / 1000; // convert millis in sec
    } else {
      return null;
    }
  }

  /**
   * Calculation of time differences between two given dates
   *
   * @param lowerDate lower date
   * @param upperDate upper date
   * @param timeUnit desired output format ({@link TimeUnit} )of the time specification (e.g.
   *     seconds) (default: minutes)
   * @return difference of the dates in the given timeUnit
   */
  public static double calcDiffBetweenDates(Date lowerDate, Date upperDate, TimeUnit timeUnit) {
    if (timeUnit == null) {
      timeUnit = TimeUnit.MINUTES;
    }

    // take the current time if the upperDate is null
    if (upperDate == null) {
      upperDate = DateTools.getCurrentDateTime();
    }

    long diffInSeconds = 0;
    if (lowerDate != null) {
      if (dateToUnixTime(upperDate) >= dateToUnixTime(lowerDate)) {
        diffInSeconds = dateToUnixTime(upperDate) - dateToUnixTime(lowerDate);
      } else {
        diffInSeconds = dateToUnixTime(lowerDate) - dateToUnixTime(upperDate);
      }
    } else {
      log.debug("Upper or lower date is empty! ");
    }

    return timeUnit.convert(diffInSeconds, TimeUnit.SECONDS);
  }

  public static int calcYearsBetweenDates(Date lowerDate, Date upperDate) {
    if (lowerDate == null) {
      throw new IllegalArgumentException("Date parameters cannot be null");
    }

    // take the current time if the upperDate is null
    if (upperDate == null) {
      upperDate = DateTools.getCurrentDateTime();
    }

    // Ensure lowerDate is before upperDate
    if (lowerDate.after(upperDate)) {
      Date temp = lowerDate;
      lowerDate = upperDate;
      upperDate = temp;
    }

    Calendar startCal = Calendar.getInstance();
    startCal.setTime(lowerDate);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(upperDate);

    int startYear = startCal.get(Calendar.YEAR);
    int endYear = endCal.get(Calendar.YEAR);

    int startDayOfYear = startCal.get(Calendar.DAY_OF_YEAR);
    int endDayOfYear = endCal.get(Calendar.DAY_OF_YEAR);

    int yearDiff = endYear - startYear;

    // If the end date is before the start date in the same year, subtract one year
    if (endDayOfYear < startDayOfYear) {
      yearDiff--;
    }

    return yearDiff;
  }

  /**
   * Calculates the number of full months between two dates. If the day of the end date is earlier
   * than the day of the start date in the same month, one month is subtracted from the total
   * difference.
   *
   * <p>If the provided dates are in the same month and day, the result will be 0. If the end date
   * is null, the current date will be used as the end date. The method ensures that lowerDate is
   * always before upperDate by swapping them if needed.
   *
   * @param lowerDate the earlier date. Must not be null.
   * @param upperDate the later date. If null, the current date is used.
   * @return the number of full months between the two dates.
   * @throws IllegalArgumentException if the lowerDate is null.
   */
  public static int calcMonthsBetweenDates(Date lowerDate, Date upperDate) {
    if (lowerDate == null) {
      throw new IllegalArgumentException("Date parameters cannot be null");
    }

    // Use the current date if upperDate is null
    if (upperDate == null) {
      upperDate = new Date(); // You can replace this with DateTools.getCurrentDateTime() if needed
    }

    // Ensure lowerDate is before upperDate
    if (lowerDate.after(upperDate)) {
      Date temp = lowerDate;
      lowerDate = upperDate;
      upperDate = temp;
    }

    // Set up Calendar instances for both dates
    Calendar startCal = Calendar.getInstance();
    startCal.setTime(lowerDate);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(upperDate);

    // Extract year, month, and day information from both dates
    int startYear = startCal.get(Calendar.YEAR);
    int endYear = endCal.get(Calendar.YEAR);

    int startMonth = startCal.get(Calendar.MONTH);
    int endMonth = endCal.get(Calendar.MONTH);

    int startDay = startCal.get(Calendar.DAY_OF_MONTH);
    int endDay = endCal.get(Calendar.DAY_OF_MONTH);

    // Calculate the total month difference considering the year difference
    int monthDiff = (endYear - startYear) * 12 + (endMonth - startMonth);

    // Subtract one month if the end day is before the start day
    if (endDay < startDay) {
      monthDiff--;
    }

    return monthDiff;
  }

  /**
   * Determining the current epoch second
   *
   * @return unix time in seconds
   */
  public static long getCurrentUnixTime() {
    return System.currentTimeMillis() / 1000;
  }

  /**
   * Getting the current {@link Date}
   *
   * @return current date
   */
  public static Date getCurrentDateTime() {
    // empty constructor = current date/time
    return new Date();
  }

  /**
   * Calculate the current age of a person
   *
   * @param birthDate The birthdate of a person
   * @return Age at the current time
   */
  public static int getAge(Date birthDate) {
    long birthDateInSec = dateToUnixTime(birthDate);
    LocalDate birthDateLocal =
        Instant.ofEpochSecond(birthDateInSec).atZone(ZoneId.systemDefault()).toLocalDate();
    return Period.between(birthDateLocal, LocalDate.now()).getYears();
  }

  /**
   * Calculates the whole number of calendar days between two dates.
   *
   * @param lowerDate the start date (inclusive) must not be null.
   * @param upperDate the end date (inclusive). If null, the current date is used.
   * @return the number of whole calendar days between the two dates, or null if an error occurs.
   * @throws IllegalArgumentException if the lowerDate is null.
   */
  public static Long calcWholeDaysBetweenDates(Date lowerDate, Date upperDate) {
    // Validate input
    if (lowerDate == null) {
      throw new IllegalArgumentException("lowerDate must not be null");
    }

    // Take the current date if the upperDate is null
    if (upperDate == null) {
      upperDate = getCurrentDateTime();
    }

    long daysBetween;
    try {
      // Convert Dates to LocalDate
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDate lowerLocalDate =
          Instant.ofEpochMilli(lowerDate.getTime()).atZone(zoneId).toLocalDate();
      LocalDate upperLocalDate =
          Instant.ofEpochMilli(upperDate.getTime()).atZone(zoneId).toLocalDate();

      // Calculate the number of days between the two dates
      daysBetween = ChronoUnit.DAYS.between(lowerLocalDate, upperLocalDate);
    } catch (Exception ex) {
      log.error(
          "Unable to calculate the difference between dates. lowerDate: {}, upperDate: {}. Error:"
              + " {}",
          lowerDate,
          upperDate,
          ex.getMessage());
      return null;
    }
    return daysBetween;
  }

  /**
   * Calculates the length of stay ("Verweildauer") in the hospital between two dates. This method
   * includes the start and end dates in the calculation, thus adding one more day to the result.
   * This adjustment handles the "fencepost" error ("Zaunpfahlfehler") in time intervals.
   *
   * @param lowerDate the start date of the hospital stay
   * @param upperDate the end date of the hospital stay
   * @return the length of stay in days
   */
  public static Long calcLengthOfStayBetweenDates(Date lowerDate, Date upperDate) {
    // Calculate the number of whole days between the two dates
    long daysBetween = calcWholeDaysBetweenDates(lowerDate, upperDate);

    // If the start and end dates are the same, return 1 (stay of one day)
    if (daysBetween == 0) {
      daysBetween = 1;
    } else {
      // Include both the start and end dates in the length of stay
      daysBetween += 1;
    }

    return daysBetween;
  }

  public static double secondsToDay(long seconds, Boolean decimalPlaces) {
    if (decimalPlaces == null) {
      decimalPlaces = false;
    }
    // return TimeUnit.SECONDS.toDays(seconds);
    if (decimalPlaces) {
      return (double) seconds / 60 / 60 / 24;
    } else {
      return TimeUnit.SECONDS.toDays(seconds);
    }
  }

  public static boolean isSameCalendarDay(Date date1, Date date2) {
    LocalDate localDate1 = date1.toInstant().atZone(timeZoneEuropeBerlin).toLocalDate();
    LocalDate localDate2 = date2.toInstant().atZone(timeZoneEuropeBerlin).toLocalDate();
    return localDate1.isEqual(localDate2);
  }

  /**
   * Determines whether the difference between two dates is more than one day.
   *
   * @param youngerDate The date that is younger than the other date
   * @param olderDate The date that is older than the other date
   * @return <code>true</code> if the difference between the two dates is more than one day, <code>
   *     false</code> otherwise
   */
  public static boolean isMoreThanOneDayOlder(Date youngerDate, Date olderDate) {
    return isMoreThanXDaysOlder(youngerDate, olderDate, 1);
  }

  /**
   * Determines whether the difference between two dates is more than a given amount of day(s).
   *
   * @param youngerDate The date that is younger than the other date
   * @param olderDate The date that is older than the other date
   * @param daysDifference The number of days that should be at least between the two dates
   * @return <code>true</code> if the difference between the two dates is more than one day, <code>
   *     false</code> otherwise
   */
  public static boolean isMoreThanXDaysOlder(Date youngerDate, Date olderDate, int daysDifference) {
    long timeDifference = youngerDate.getTime() - olderDate.getTime();
    return timeDifference > TimeUnit.DAYS.toMillis(daysDifference);
  }

  /** Converts year, month, and day into a Date object with GMT time zone. */
  public static Date toDate(int year, int month, int day) {
    return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.of(GMT)).toInstant());
  }

  /**
   * Checks whether a given {@link Date} is within the specified {@link Period}, including the start
   * and end dates.
   *
   * <p>This method ensures that the target date is:
   *
   * <ul>
   *   <li>On or after the start date
   *   <li>On or before the end date
   * </ul>
   *
   * <p>If the period or any required values are missing, the method returns {@code false}.
   *
   * @param target The {@link Date} to check.
   * @param period The {@link Period} within which the target date should fall.
   * @return {@code true} if the target date is within the period (inclusive), otherwise {@code
   *     false}.
   */
  public static boolean isWithinPeriod(Date target, org.hl7.fhir.r4.model.Period period) {
    if (target == null || period == null || !period.hasStart() || !period.hasEnd()) {
      return false; // Return false if any required values are missing
    }
    Date start = period.getStart();
    Date end = period.getEnd();
    return start.compareTo(target) <= 0 && end.compareTo(target) >= 0;
  }
}
