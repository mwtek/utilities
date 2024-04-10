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
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdsdiagnosis.valuesets.KdsDiagnosisFixedValues.EXTENSION_DIAGNOSIS_RELIABILITY;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdsdiagnosis.valuesets.KdsDiagnosisFixedValues.EXTENSION_DIAGNOSIS_RELIABILITY_SYSTEM;

import de.ukbonn.mwtek.utilities.fhir.resources.UkbCondition;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;

public class FhirConditionTools {

  public static Set<String> getEncounterIdsByIcdCodes(Collection<UkbCondition> ukbConditions,
      String icdCode) {
    Set<String> caseIds = new HashSet<>();
    if (ukbConditions != null) {
      ukbConditions.forEach(condition -> {
        if (condition.hasCode() && condition.getCode().hasCoding(ICD, icdCode)) {
          // Check each code and break if at least 1 got found
          caseIds.add(condition.getCaseId());
        }
      });
    }
    return caseIds;
  }

  /**
   * Finds case IDs from a collection of UkbConditions that contain at least one ICD code from the
   * provided set.
   *
   * @param ukbConditions A collection of {@link UkbCondition} objects.
   * @param icdCodes      A collection of ICD (International Classification of Diseases) codes as
   *                      strings.
   * @return A Set of unique case IDs found within the UkbConditions that have matching ICD codes.
   * @throws NullPointerException if either ukbConditions or icdCodes is null.
   */
  public static Set<String> getEncounterIdsByIcdCodes(Collection<UkbCondition> ukbConditions,
      Collection<String> icdCodes) {
    Set<String> caseIds = new HashSet<>();
    if (ukbConditions != null && !icdCodes.isEmpty()) {
      for (UkbCondition condition : ukbConditions) {
        if (condition.hasCode()) {
          for (Coding coding : condition.getCode().getCoding()) {
            // Check each code and break if at least 1 got found
            if (coding.hasSystem() && coding.getSystem().equals(ICD) && icdCodes.contains(
                coding.getCode())) {
              caseIds.add(condition.getCaseId());
              break;
            }
          } // for
        } // if
      } // for
    }
    return caseIds;
  }

  public static Set<String> getCaseIdsWithIcdCodeReliability(Collection<UkbCondition> ukbConditions,
      Collection<String> icdCodes, String reliability) {
    Set<String> caseIds = new HashSet<>();
    if (ukbConditions != null && !icdCodes.isEmpty()) {
      for (UkbCondition condition : ukbConditions) {
        if (condition.hasCode()) {
          for (Coding coding : condition.getCode().getCoding()) {
            // Check each code and break if at least 1 got found
            if (coding.hasSystem() && coding.getSystem().equals(ICD) && icdCodes.contains(
                coding.getCode())) {
              // Detect the diagnosis reliability which is part of an extension
              if (coding.hasExtension(EXTENSION_DIAGNOSIS_RELIABILITY)) {
                Extension extDiagReliability = coding.getExtensionByUrl(
                    EXTENSION_DIAGNOSIS_RELIABILITY);
                if (extDiagReliability.getValue() instanceof Coding codingExtDiagReliability) {
                  if (codingExtDiagReliability.getSystem()
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
}
