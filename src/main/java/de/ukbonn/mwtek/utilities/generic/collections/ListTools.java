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
import java.util.List;

/**
 * Class with auxiliary methods for list operations.
 *
 * @author <a href="mailto:david.meyers@ukbonn.de">David Meyers</a>
 */
public class ListTools {

  /**
   * Splitting a list of any type into different batches.
   *
   * @param listInput List to be split into several lists.
   * @param maxPartSize Maximum size of a batch.
   * @param <E> Any datatype allowed.
   * @return Sublists of the given list.
   */
  public static <E> List<List<E>> splitList(List<E> listInput, int maxPartSize) {

    List<List<E>> listOutput = new ArrayList<>();

    // Divide the size of the list by the maximum size of a list and round up.
    int numSubLists = (int) Math.ceil((double) listInput.size() / maxPartSize);

    for (int i = 0; i < numSubLists; i++) {
      List<E> tempSubList =
          listInput.subList(
              i * maxPartSize, Math.min((i * maxPartSize) + maxPartSize, listInput.size()));
      listOutput.add(tempSubList);
    }
    return listOutput;
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
}
