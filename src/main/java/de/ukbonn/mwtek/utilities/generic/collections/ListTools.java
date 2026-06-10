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

package de.ukbonn.mwtek.utilities.generic.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class with auxiliary methods for list operations.
 *
 * @author <a href="mailto:david.meyers@ukbonn.de">David Meyers</a>
 */
public class ListTools {

  /**
   * Splits a given list into sublists of a specified maximum size.
   *
   * <p>If the list cannot be evenly divided, the last sublist will contain the remaining elements.
   * This method is safe against {@code IndexOutOfBoundsException} and does not use {@code
   * Math.ceil} or hardcoded sublist counts.
   *
   * @param listInput the list to split; must not be {@code null}
   * @param maxPartSize the maximum number of elements in each sublist; must be greater than 0
   * @param <E> the type of elements in the list
   * @return a list of sublists, each containing up to {@code maxPartSize} elements
   * @throws IllegalArgumentException if {@code listInput} is {@code null} or {@code maxPartSize <=
   *     0}
   */
  public static <E> List<List<E>> splitList(List<E> listInput, int maxPartSize) {
    if (listInput == null) {
      throw new IllegalArgumentException("listInput must not be null");
    }
    if (maxPartSize <= 0) {
      throw new IllegalArgumentException("maxPartSize must be greater than 0");
    }

    List<List<E>> listOutput = new ArrayList<>();
    for (int start = 0; start < listInput.size(); start += maxPartSize) {
      int end = Math.min(start + maxPartSize, listInput.size());
      listOutput.add(listInput.subList(start, end));
    }
    return listOutput;
  }

  /**
   * Safely splits a concurrent set into smaller sublists by creating a defensive copy first.
   *
   * <p>This method avoids concurrency-related issues such as {@link ArrayIndexOutOfBoundsException}
   * by copying the input {@code Set} into a regular {@link HashSet} before converting it to a
   * {@link List}.
   *
   * @param <T> the type of elements in the set
   * @param concurrentSet the concurrent set to be split; typically from {@code
   *     ConcurrentHashMap.newKeySet()}
   * @param batchSize the maximum size of each sublist; must be greater than 0
   * @return a list of sublists, each of size at most {@code batchSize}
   * @throws IllegalArgumentException if {@code concurrentSet} is {@code null} or {@code batchSize}
   *     is less than or equal to 0
   */
  public static <T> List<List<T>> safeSplit(Set<T> concurrentSet, int batchSize) {
    return splitList(new ArrayList<>(new HashSet<>(concurrentSet)), batchSize);
  }

  /**
   * Transformation of a comma separated string (e.g. "U07.1, U07.2") into a list with an entry for
   * each item
   *
   * @param inputString Comma separated string (e.g. "U07.1, U07.2") like in the standard syntax in
   *     the .yaml
   * @return List with an entry for each item.
   */
  public static List<String> commaSeparatedStringIntoList(String inputString) {
    return Arrays.asList(inputString.split(",", -1));
  }

  public static List<String> commaSeparatedStringsIntoList(List<String> inputStrings) {
    List<String> result = new ArrayList<>();
    inputStrings.forEach(
        x -> {
          result.addAll(commaSeparatedStringIntoList(x));
        });
    return result;
  }

  /**
   * Converts a primitive long array into a List of Long values.
   *
   * @param values Primitive long array
   * @return List containing all array values
   */
  public static List<Long> toList(long[] values) {
    List<Long> result = new ArrayList<>(values.length);
    for (long value : values) {
      result.add(value);
    }
    return result;
  }
}
