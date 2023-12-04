/*
 *  Copyright (C) 2021 University Hospital Bonn - All Rights Reserved You may use, distribute and
 *  modify this code under the GPL 3 license. THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT
 *  PERMITTED BY APPLICABLE LAW. EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR
 *  OTHER PARTIES PROVIDE THE PROGRAM “AS IS” WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 *  IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH
 *  YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR
 *  OR CORRECTION. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL ANY
 *  COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS THE PROGRAM AS PERMITTED ABOVE,
 *  BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES
 *  ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF DATA
 *  OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE
 *  PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED
 *  OF THE POSSIBILITY OF SUCH DAMAGES. You should have received a copy of the GPL 3 license with
 *  this file. If not, visit http://www.gnu.de/documents/gpl-3.0.en.html
 */

package de.ukbonn.mwtek.utilities.generic.time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for functions concerning time measurements
 *
 * @author <a href="mailto:david.meyers@ukbonn.de">David Meyers</a>
 */
@Slf4j
public class TimerTools {

  /**
   * Calculation of the time interval between a specified time and the current time
   *
   * @param startTime start time (in UnixTime EPOCHSECOND) of the time to be compared
   * @return elapsed time in s
   */
  public static double calcTimeDifference(long startTime) {
    return ((Instant.now().getEpochSecond() * 1000 - startTime) / 1000.0);
  }

  /**
   * Calculates the time interval between a specified time and the current time.
   *
   * @param startTimeInEpochMillis Start time (in UnixTime EPOCH MILLISECONDS) of the time to be
   *                               compared.
   * @param scale                  Scale of the {@code BigDecimal} value to be returned. Specifies
   *                               the number of digits to the right of the decimal point.
   * @return Elapsed time in seconds as a {@code BigDecimal} value. The result is rounded according
   * to the specified scale using {@code RoundingMode.HALF_UP}.
   * @throws IllegalArgumentException If the input parameters are invalid (negative scale or start
   *                                  time).
   */
  public static BigDecimal calcTimeDifference(long startTimeInEpochMillis, int scale) {
    if (startTimeInEpochMillis < 0 || scale < 0) {
      throw new IllegalArgumentException(
          "Invalid input parameters. Start time and scale must be non-negative.");
    }

    Instant startTime = Instant.ofEpochMilli(startTimeInEpochMillis);
    Duration duration = Duration.between(startTime, Instant.now());

    return new BigDecimal(duration.getSeconds()).setScale(scale,
        RoundingMode.HALF_UP);
  }

  /**
   * A utility method for measuring the execution time of a provided {@code Consumer} block.
   *
   * @param <T>   The type of input accepted by the {@code Consumer}.
   * @param block The {@code Consumer} block whose execution time is to be measured.
   * @return A new {@code Consumer} that wraps the provided block, measuring and logging the elapsed
   * time. The measured time is logged using the debug level in nanoseconds.
   */
  public static <T> Consumer<T> measureTime(Consumer<T> block) {
    // Executes the provided Consumer block, measures the elapsed time, and logs it.
    return t -> {
      Instant start = Instant.now();
      block.accept(t);
      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);
      log.debug("Elapsed time: " + duration.toNanos() + " ns");
    };
  }


  /**
   * Starting a timer
   *
   * @return current time in ms
   */
  public static Instant startTimer() {
    return Instant.now();
  }

  /**
   * Calculates the difference of a start time and the current time
   *
   * @param startTime starting time in ms
   * @return the time that has passed in ms
   */
  public static long stopTimer(Instant startTime) {
    Instant endTime = Instant.now();
    Duration timeElapsed = Duration.between(startTime, endTime);
    log.debug("Time elapsed: " + timeElapsed.toMillis() + " milliseconds");
    return timeElapsed.toMillis();
  }

  /**
   * Calculates the difference of a start time and the current time
   *
   * @param startTime starting time in ms
   * @param taskDesc  puts the task description to the output
   * @return the time that has passed in ms
   */
  public static long stopTimerAndLog(Instant startTime, String taskDesc) {
    Instant endTime = Instant.now();
    Duration timeElapsed = Duration.between(startTime, endTime);
    log.debug(taskDesc + " [took " + timeElapsed.toMillis() + " ms]");
    return timeElapsed.toMillis();
  }
}
