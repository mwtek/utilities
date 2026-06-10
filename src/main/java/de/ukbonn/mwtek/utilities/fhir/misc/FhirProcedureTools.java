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
package de.ukbonn.mwtek.utilities.fhir.misc;

import static de.ukbonn.mwtek.utilities.enums.TerminologySystems.OPS;
import static de.ukbonn.mwtek.utilities.enums.TerminologySystems.SNOMED;

import de.ukbonn.mwtek.utilities.fhir.resources.MiiProcedure;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Type;

public class FhirProcedureTools {

  /**
   * Returns the set of encounter (case) IDs for which at least one {@link MiiProcedure} carries a
   * coding with system {@code SNOMED} and the given SNOMED CT code.
   *
   * @param ukbProcedures the procedures to inspect; may be {@code null}
   * @param snomedCodes the SNOMED CT code to match (single code); must not be {@code null} for
   *     reliable results
   * @return a {@link Set} of encounter IDs (case IDs) that have at least one matching procedure;
   *     never {@code null}
   */
  public static Set<String> getEncounterIdsBySnomedCodes(
      Collection<MiiProcedure> ukbProcedures, String snomedCodes) {
    Set<String> caseIds = new HashSet<>();
    if (ukbProcedures != null) {
      ukbProcedures.forEach(
          procedure -> {
            if (procedure.hasCode() && procedure.getCode().hasCoding(SNOMED, snomedCodes)) {
              // Check each code and break if at least 1 got found
              caseIds.add(procedure.getCaseId());
            }
          });
    }
    return caseIds;
  }

  /**
   * Checks whether a given {@link MiiProcedure} contains <em>any</em> coding whose {@code system}
   * equals {@code SNOMED} and whose {@code code} is contained in the provided collection of SNOMED
   * CT codes.
   *
   * @param procedure the procedure to inspect; may be {@code null}
   * @param snomedCodes collection of SNOMED CT codes to look for; may be {@code null} or empty
   * @return {@code true} if a SNOMED coding with a code in {@code snomedCodes} is present;
   *     otherwise {@code false}
   */
  public static boolean isSnomedCodeInProcedure(
      MiiProcedure procedure, final Collection<String> snomedCodes) {
    // Return an empty set if the input collections are null or if snomedCodes is empty
    if (procedure == null) {
      return false;
    }
    // Checking matches
    return procedure.hasCode()
        && procedure.getCode().getCoding().stream()
            .anyMatch(
                coding ->
                    SNOMED.equals(coding.getSystem()) && snomedCodes.contains(coding.getCode()));
  }

  /**
   * Checks whether a given {@link MiiProcedure} contains <em>any</em> coding whose {@code system}
   * equals {@code OPS} and whose {@code code} is contained in the provided collection of SNOMED CT
   * codes.
   *
   * @param procedure the procedure to inspect; may be {@code null}
   * @param opsCodes collection of OPS codes to look for; may be {@code null} or empty
   * @return {@code true} if a OPS coding with a code in {@code snomedCodes} is present; otherwise
   *     {@code false}
   */
  public static boolean isOpsCodeInProcedure(
      MiiProcedure procedure, final Collection<String> opsCodes) {
    // Return an empty set if the input collections are null or if snomedCodes is empty
    if (procedure == null || opsCodes == null) {
      return false;
    }
    // Checking matches
    return procedure.hasCode()
        && procedure.getCode().getCoding().stream()
            .anyMatch(
                coding -> OPS.equals(coding.getSystem()) && opsCodes.contains(coding.getCode()));
  }

  /**
   * Checks whether the given {@link MiiProcedure} contains at least one OPS coding whose code
   * starts with the specified OPS code prefix.
   *
   * @param procedure the {@link MiiProcedure} to check; may be {@code null}
   * @param opsCodePrefix the OPS code prefix to look for; may be {@code null} or empty
   * @return {@code true} if the procedure contains an OPS coding whose code starts with the
   *     specified prefix; {@code false} otherwise (including if inputs are null or empty)
   */
  public static boolean isOpsCodePrefixInProcedure(
      MiiProcedure procedure, final String opsCodePrefix) {

    // Return false if inputs are null or empty
    if (procedure == null || opsCodePrefix == null || opsCodePrefix.isEmpty()) {
      return false;
    }

    // Check if any coding code starts with the given opsCode prefix
    return procedure.hasCode()
        && procedure.getCode().getCoding().stream()
            .anyMatch(
                coding ->
                    OPS.equals(coding.getSystem())
                        && coding.getCode() != null
                        && coding.getCode().startsWith(opsCodePrefix));
  }

  /**
   * Returns all procedures whose OPS code starts with the given prefix.
   *
   * @param ukbProcedures the procedures to filter; may be {@code null}
   * @param opsCodePrefix the OPS code prefix; may be {@code null} or empty
   * @return a set of procedures whose OPS coding starts with the prefix; empty if inputs invalid
   */
  public static Set<MiiProcedure> getProceduresByOpsCodePrefix(
      final Collection<MiiProcedure> ukbProcedures, final String opsCodePrefix) {

    // Return an empty set if inputs are null/empty
    if (ukbProcedures == null || opsCodePrefix == null || opsCodePrefix.isEmpty()) {
      return Collections.emptySet();
    }

    // Filter by prefix using the existing helper
    return ukbProcedures.parallelStream()
        .filter(proc -> isOpsCodePrefixInProcedure(proc, opsCodePrefix))
        .collect(Collectors.toSet());
  }

  /**
   * Returns all procedures that match ANY of the provided OPS code prefixes.
   *
   * @param ukbProcedures the procedures to filter; may be {@code null}
   * @param opsCodePrefixes the collection of OPS code prefixes; may be {@code null} or empty
   * @return a set of procedures with at least one OPS coding starting with any given prefix
   */
  public static Set<MiiProcedure> getProceduresByAnyOpsCodePrefix(
      final Collection<MiiProcedure> ukbProcedures, final Collection<String> opsCodePrefixes) {

    // Return an empty set if inputs are null/empty
    if (ukbProcedures == null || opsCodePrefixes == null || opsCodePrefixes.isEmpty()) {
      return Collections.emptySet();
    }

    // Pre-filter prefixes (null/empty guards)
    final List<String> cleanPrefixes =
        opsCodePrefixes.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();

    if (cleanPrefixes.isEmpty()) {
      return Collections.emptySet();
    }

    // Match if any given prefix matches via helper
    return ukbProcedures.parallelStream()
        .filter(proc -> cleanPrefixes.stream().anyMatch(p -> isOpsCodePrefixInProcedure(proc, p)))
        .collect(Collectors.toSet());
  }

  /**
   * Filters procedures to include only those that were performed after the provided reference date.
   *
   * <p>Uses {@link #isAfterReference(Procedure, Date)} for comparison logic.
   *
   * @param ukbProcedures the list of procedures; may be {@code null}
   * @param referenceDate the cutoff date; must not be {@code null}
   * @return list of procedures performed after the reference date; never {@code null}
   */
  public static List<MiiProcedure> filterProceduresByRecordDate(
      List<MiiProcedure> ukbProcedures, Date referenceDate) {

    // Defensive checks
    if (ukbProcedures == null || referenceDate == null) {
      return Collections.emptyList();
    }

    return ukbProcedures.parallelStream()
        .filter(Procedure::hasPerformed)
        .filter(p -> isAfterReference(p, referenceDate))
        .toList();
  }

  /**
   * Returns true if the procedure happened after the given reference date. - For performedDateTime:
   * value > referenceDate - For performedPeriod: end > referenceDate, or (no end && start >
   * referenceDate)
   *
   * @param p the {@link Procedure} to check; may be {@code null}
   * @param referenceDate the date to compare against; must not be {@code null}
   * @return {@code true} if the procedure took place after referenceDate, otherwise {@code false}
   */
  private static boolean isAfterReference(Procedure p, Date referenceDate) {
    Type performed = p.getPerformed();

    // Case 1: performedDateTime
    if (performed instanceof DateTimeType dt) {
      Date when = dt.getValue();
      return when != null && when.after(referenceDate);
    }

    // Case 2: performedPeriod
    if (performed instanceof Period period) {
      Date start = period.getStart();
      Date end = period.getEnd();

      if (end != null) {
        return end.after(referenceDate);
      }
      return start != null && start.after(referenceDate);
    }

    // Other performed[x] types (Age, Range, String) are not handled here
    return false;
  }
}
