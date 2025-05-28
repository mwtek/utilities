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

import static de.ukbonn.mwtek.utilities.enums.TerminologySystems.ICD;
import static de.ukbonn.mwtek.utilities.enums.TerminologySystems.OPS;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdsdiagnosis.valuesets.KdsDiagnosisFixedValues.EXTENSION_DIAGNOSIS_RELIABILITY;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdsdiagnosis.valuesets.KdsDiagnosisFixedValues.EXTENSION_DIAGNOSIS_RELIABILITY_SYSTEM;

import de.ukbonn.mwtek.utilities.fhir.resources.UkbCondition;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbProcedure;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Extension;

public class FhirConditionTools {

  public static Set<String> getEncounterIdsByIcdCodes(
      Collection<UkbCondition> ukbConditions, String icdCode) {
    Set<String> caseIds = new HashSet<>();
    if (ukbConditions != null) {
      ukbConditions.forEach(
          condition -> {
            if (condition.hasCode() && condition.getCode().hasCoding(ICD, icdCode)) {
              // Check each code and break if at least 1 got found
              caseIds.add(condition.getCaseId());
            }
          });
    }
    return caseIds;
  }

  public static Set<UkbCondition> getConditionsByIcdCodes(
      final Collection<UkbCondition> ukbConditions, final Collection<String> icdCodes) {
    // Return an empty set if the input collections are null or if icdCodes is empty
    if (ukbConditions == null || icdCodes == null || icdCodes.isEmpty()) {
      return Collections.emptySet();
    }

    // Checking matches
    return ukbConditions.parallelStream()
        .filter(
            condition ->
                condition.hasCode()
                    && condition.getCode().getCoding().stream()
                        .anyMatch(
                            coding ->
                                ICD.equals(coding.getSystem())
                                    && icdCodes.contains(coding.getCode())))
        .collect(Collectors.toSet());
  }

  public static boolean isIcdCodeInCondition(
      UkbCondition condition, final Collection<String> icdCodes) {
    // Return an empty set if the input collections are null or if icdCodes is empty
    if (condition == null) {
      return false;
    }
    // Checking matches
    return condition.hasCode()
        && condition.getCode().getCoding().stream()
            .anyMatch(
                coding -> ICD.equals(coding.getSystem()) && icdCodes.contains(coding.getCode()));
  }

  public static boolean isOpsCodeInProcedure(
      UkbProcedure procedure, final Collection<String> opsCodes) {
    // Return an empty set if the input collections are null or if opsCodes is empty
    if (procedure == null) {
      return false;
    }
    // Checking matches
    return procedure.hasCode()
        && procedure.getCode().getCoding().stream()
            .anyMatch(
                coding -> OPS.equals(coding.getSystem()) && opsCodes.contains(coding.getCode()));
  }

  /**
   * Checks if the given {@link UkbCondition} contains any ICD code that is subsumed by one of the
   * provided ICD codes via prefix matching (e.g., {@code I48} matches {@code I48.2}).
   *
   * <p>This enables broader matching based on ICD code categories.
   *
   * <p>Example: If {@code icdCodes} contains {@code "I48"}, it matches condition codes such as
   * {@code "I48.0"}, {@code "I48.1"}, {@code "I48.2"} etc.
   *
   * @param condition the condition to check
   * @param icdCodes the collection of ICD code prefixes to match against
   * @return {@code true} if any coding in the condition starts with any of the provided ICD codes,
   *     {@code false} otherwise or if the condition is null
   */
  public static boolean isIcdCodeInConditionWithPrefixWildcardCheck(
      UkbCondition condition, Collection<String> icdCodes) {
    return hasMatchingPrefixCode(condition, ICD, icdCodes, UkbCondition::getCode);
  }

  /**
   * Checks if the given {@link UkbProcedure} contains any OPS code that is subsumed by one of the
   * provided OPS codes via prefix matching (e.g., {@code 5-480} matches {@code 5-480.2}).
   *
   * <p>This enables broader matching based on OPS code categories.
   *
   * <p>Example: If {@code icdCodes} contains {@code "5-480"}, it matches procedure codes such as
   * {@code "5-480.0"}, {@code "5-480.1"}, {@code "5-480.2"} etc.
   *
   * @param procedure the procedure to check
   * @param icdCodes the collection of OPS code prefixes to match against
   * @return {@code true} if any coding in the procedure starts with any of the provided OPS codes,
   *     {@code false} otherwise or if the procedure is null
   */
  public static boolean isOpsCodeInProcedureWithPrefixWildcardCheck(
      UkbProcedure procedure, Collection<String> icdCodes) {
    return hasMatchingPrefixCode(procedure, OPS, icdCodes, UkbProcedure::getCode);
  }

  /**
   * Checks whether the coding of a FHIR resource contains any code starting with one of the given
   * prefixes and belonging to the expected coding system.
   *
   * @param resource the resource (e.g., condition or procedure) to check
   * @param expectedSystem the expected coding system (e.g., {@code ICD} or {@code OPS})
   * @param icdCodes the collection of code prefixes to match against
   * @param codeExtractor function to extract the {@link CodeableConcept} from the resource
   * @return {@code true} if any code in the resource starts with a prefix in {@code icdCodes} and
   *     belongs to {@code expectedSystem}; {@code false} otherwise
   */
  private static <T> boolean hasMatchingPrefixCode(
      T resource,
      String expectedSystem,
      Collection<String> icdCodes,
      Function<T, CodeableConcept> codeExtractor) {

    if (resource == null || icdCodes == null || icdCodes.isEmpty()) {
      return false;
    }

    CodeableConcept codeableConcept = codeExtractor.apply(resource);
    return codeableConcept != null
        && codeableConcept.getCoding().stream()
            .anyMatch(
                coding ->
                    expectedSystem.equals(coding.getSystem())
                        && coding.getCode() != null
                        && icdCodes.stream().anyMatch(code -> coding.getCode().startsWith(code)));
  }

  /**
   * Finds case IDs from a collection of UkbConditions that contain at least one ICD code from the
   * provided set.
   *
   * @param ukbConditions A collection of {@link UkbCondition} objects.
   * @param icdCodes A collection of ICD (International Classification of Diseases) codes as
   *     strings.
   * @return A Set of unique case IDs found within the UkbConditions that have matching ICD codes.
   * @throws NullPointerException if either ukbConditions or icdCodes is null.
   */
  public static Set<String> getEncounterIdsByIcdCodes(
      Collection<UkbCondition> ukbConditions, Collection<String> icdCodes) {
    Set<String> caseIds = new HashSet<>();
    if (ukbConditions != null && !icdCodes.isEmpty()) {
      for (UkbCondition condition : ukbConditions) {
        if (condition.hasCode()) {
          for (Coding coding : condition.getCode().getCoding()) {
            // Check each code and break if at least 1 got found
            if (coding.hasSystem()
                && coding.getSystem().equals(ICD)
                && icdCodes.contains(coding.getCode())) {
              caseIds.add(condition.getCaseId());
              break;
            }
          } // for
        } // if
      } // for
    }
    return caseIds;
  }

  public static Set<String> getPatientIdsByIcdCodes(
      Collection<UkbCondition> ukbConditions, Collection<String> icdCodes) {
    Set<String> patientIds = new HashSet<>();
    if (ukbConditions != null && !icdCodes.isEmpty()) {
      for (UkbCondition condition : ukbConditions) {
        if (condition.hasCode()) {
          for (Coding coding : condition.getCode().getCoding()) {
            // Check each code and break if at least 1 got found
            if (coding.hasSystem()
                && coding.getSystem().equals(ICD)
                && icdCodes.contains(coding.getCode())) {
              patientIds.add(condition.getPatientId());
              break;
            }
          } // for
        } // if
      } // for
    }
    return patientIds;
  }

  public static Set<String> getCaseIdsWithIcdCodeReliability(
      Collection<UkbCondition> ukbConditions, Collection<String> icdCodes, String reliability) {
    Set<String> caseIds = new HashSet<>();
    if (ukbConditions != null && !icdCodes.isEmpty()) {
      for (UkbCondition condition : ukbConditions) {
        if (condition.hasCode()) {
          for (Coding coding : condition.getCode().getCoding()) {
            // Check each code and break if at least 1 got found
            if (coding.hasSystem()
                && coding.getSystem().equals(ICD)
                && icdCodes.contains(coding.getCode())) {
              // Detect the diagnosis reliability which is part of an extension
              if (coding.hasExtension(EXTENSION_DIAGNOSIS_RELIABILITY)) {
                Extension extDiagReliability =
                    coding.getExtensionByUrl(EXTENSION_DIAGNOSIS_RELIABILITY);
                if (extDiagReliability.getValue() instanceof Coding codingExtDiagReliability) {
                  if (codingExtDiagReliability
                          .getSystem()
                          .equals(EXTENSION_DIAGNOSIS_RELIABILITY_SYSTEM)
                      && codingExtDiagReliability.hasCode()) {
                    // check if ICD diagnosis reliability code (usually a letter) is available
                    if (reliability.equals(codingExtDiagReliability.getCode())) {
                      caseIds.add(condition.getCaseId());
                      break;
                    }
                  } // if codingExtDiagReliability.getSystem()
                } // if extDiagReliability.getValue()
              }
            } // for
          } // if
        } // for
      }
    }
    return caseIds;
  }

  /**
   * Filters a list of UKB conditions to include only those that have a recorded date after the
   * specified reference date.
   *
   * @param ukbConditions A list of {@link UkbCondition} objects to be filtered.
   * @param referenceDate The date to compare against; only conditions recorded after this date will
   *     be included.
   * @return A list of {@link UkbCondition} objects that have a recorded date after the specified
   *     reference date. If no conditions meet the criteria, an empty list is returned.
   */
  public static List<UkbCondition> filterConditionsByRecordDate(
      List<UkbCondition> ukbConditions, Date referenceDate) {
    return ukbConditions.parallelStream()
        .filter(Condition::hasRecordedDate)
        .filter(x -> x.getRecordedDate().after(referenceDate))
        .toList();
  }
}
