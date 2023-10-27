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

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.ukbonn.mwtek.utilities.ExceptionTools;
import lombok.extern.slf4j.Slf4j;

/**
 * Auxiliary class, with various tools to convert / create date fields.
 *
 * @author <a href="mailto:david.meyers@ukbonn.de">David Meyers</a>
 */
@Slf4j public class DateTools {

  public static final ZoneId timeZoneEuropeBerlin = ZoneId.of("Europe/Berlin");

  /**
   * Merging a date and a time (necessary because of weird database design where date and time are )
   * <p>
   * Care with using java.util.date to merge dates because its kinda bugged!
   *
   * @param date                {@link Date} with date information
   * @param time                {@link Date} with time information
   * @param nullifyMilliseconds Should the millisecond field be explicitly set to 0?
   * @return A {@link Date} object, initialized with the passed (separated) date and the passed
   * time.
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
    if (nullifyMilliseconds)
      aDateTime.set(Calendar.MILLISECOND, 0);

    return aDateTime.getTime();
  }

  /**
   * Transformation a unixtime (micros) in a date with truncating of the time data
   *
   * @param unixTimeInMicros UnixTime in microseconds
   * @param timeZone         Optional: {@link TimeZone} of the date (default: GMT)
   * @return A {@link Date} object without time information
   */
  public static Date unixTimeMicrosToDateExtinguishedTime(long unixTimeInMicros,
          TimeZone timeZone) {

    if (timeZone == null)
      timeZone = TimeZone.getTimeZone("GMT");

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
   * @param timeZone          Optional: {@link TimeZone} of the date (default: GMT)
   * @return A {@link Date} object without time information
   */
  public static Date unixTimeSecondsToDateExtinguishedTime(long unixTimeInSeconds,
          TimeZone timeZone) {
    if (timeZone == null)
      timeZone = TimeZone.getTimeZone("GMT");
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
    date.setTime((long) unixTimeInMicros / 1000);
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
    date.setTime((long) unixTimeInMillis);
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
    date.setTime((long) unixTimeInSeconds * 1000);
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
    if (date != null)
      return date.getTime() / 1000; // convert millis in sec
    else
      return null;
  }

  /**
   * Calculation of time differences between two given dates
   *
   * @param lowerDate lower date
   * @param upperDate upper date
   * @param timeUnit  desired output format ({@link TimeUnit} )of the time specification (e.g.
   *                  seconds) (default: minutes)
   * @return difference of the dates in the given timeUnit
   */
  public static double calcDiffBetweenDates(Date lowerDate, Date upperDate, TimeUnit timeUnit) {

    if (timeUnit == null)
      timeUnit = TimeUnit.MINUTES;

    // take the current time if the upperDate is null
    if (upperDate == null)
      upperDate = DateTools.getCurrentDateTime();

    long diffInSeconds = 0;
    if (upperDate != null && lowerDate != null) {
      if (dateToUnixTime(upperDate) >= dateToUnixTime(lowerDate))
        diffInSeconds = dateToUnixTime(upperDate) - dateToUnixTime(lowerDate);
      else
        diffInSeconds = dateToUnixTime(lowerDate) - dateToUnixTime(upperDate);
    } else {
      log.debug("Upper or lower date is empty! ");
    }

    return timeUnit.convert(diffInSeconds, TimeUnit.SECONDS);

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
    Date date = new Date();
    return date;
  }

  /**
   * Calculate the current age of a person
   *
   * @param birthDate birth date of a person
   * @return Age at the current time
   */
  public static int getAge(Date birthDate) {
    long birthDateInSec = dateToUnixTime(birthDate);
    LocalDate birthDateLocal =
            Instant.ofEpochSecond(birthDateInSec).atZone(ZoneId.systemDefault()).toLocalDate();
    return Period.between(birthDateLocal, LocalDate.now()).getYears();
  }


  /**
   * Calculating the sum of all intersected calendar days between two dates
   *
   * @param lowerDate lower {@link Date}
   * @param upperDate upper {@link Date}
   * @return Time difference (in whole days) between two date objects
   */
  public static Long calcWholeDaysBetweenDates(Date lowerDate, Date upperDate) {

    // take the current time if the upperDate is null
    if (upperDate == null)
      upperDate = DateTools.getCurrentDateTime();
    long daysBetween = 0;
    try {
      // timezone operations (take the system zone -> case 1530302 (26.03. -> 27.03. 00:40 (GMT))
      // should be
      // count as 2 days not 1 day (26.03. -> 26.04. 23:40 in UTC)
      Instant lowerLocalDate =
              Instant.ofEpochMilli(lowerDate.getTime()).atZone(ZoneOffset.systemDefault())
                      .toInstant();
      Instant upperLocalDate = Instant.ofEpochMilli(upperDate.getTime());

      ZonedDateTime lowerZonedDateTime =
              ZonedDateTime.ofInstant(lowerLocalDate, timeZoneEuropeBerlin);
      ZonedDateTime upperZonedDateTime =
              ZonedDateTime.ofInstant(upperLocalDate, timeZoneEuropeBerlin);

      // truncate to days cause we need to sum the calendar days
      daysBetween = ChronoUnit.DAYS.between(lowerZonedDateTime.truncatedTo(ChronoUnit.DAYS),
              upperZonedDateTime.truncatedTo(ChronoUnit.DAYS)) + 1;
    } catch (Exception ex) {
      log.debug(
              "unable to calculate the differences between dates (lowerDate maybe null) " + ex.getMessage());
    }
    return daysBetween;
  }

  public static double secondsToDay(long seconds, Boolean decimalPlaces) {
    if (decimalPlaces == null)
      decimalPlaces = false;
    // return TimeUnit.SECONDS.toDays(seconds);
    if (decimalPlaces)
      return (double) seconds / 60 / 60 / 24;
    else
      return TimeUnit.SECONDS.toDays(seconds);
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
   * @return <code>true</code> if the difference between the two dates is more than one day, <code>false</code> otherwise
   */
  public static boolean isMoreThanOneDayOlder(Date youngerDate, Date olderDate) {
    return isMoreThanXDaysOlder(youngerDate,olderDate,1);
  }

  /**
   * Determines whether the difference between two dates is more than a given amount of day(s).
   *
   * @param youngerDate The date that is younger than the other date
   * @param olderDate The date that is older than the other date
   * @param daysDifference The number of days that should be at least between the two dates
   * @return <code>true</code> if the difference between the two dates is more than one day, <code>false</code> otherwise
   */
  public static boolean isMoreThanXDaysOlder(Date youngerDate, Date olderDate, int daysDifference) {
    long timeDifference = youngerDate.getTime() - olderDate.getTime();
    return timeDifference > TimeUnit.DAYS.toMillis(daysDifference);
  }
}
