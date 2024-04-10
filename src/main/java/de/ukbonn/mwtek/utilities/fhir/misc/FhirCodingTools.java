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

import java.util.Collection;
import java.util.List;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

public class FhirCodingTools {

  /**
   * Retrieval of the fhir {@link Coding}s in a list that uses the given
   * {@link Coding#getSystem()}.
   */
  public static Coding getCodingBySystem(List<Coding> codings, String system) {
    return codings.stream()
        .filter(x -> x.hasSystem() && x.getSystem().equals(system)).findFirst()
        .orElse(null);
  }

  /**
   * Does the given list of fhir {@link Coding}s contain a value that uses the given
   * {@link Coding#getSystem()}.
   */
  public static String getCodeBySystem(List<Coding> codings, String system) {
    Coding coding = getCodingBySystem(codings, system);
    return coding != null && coding.hasCode() ? coding.getCode() : null;
  }

  /**
   * Does the given list of fhir {@link Coding}s contain a value that is part of the given code
   * system.
   */
  public static boolean isCodeInCodesystem(List<Coding> codings,
      Collection<String> codeSystemEntries,
      String system) {
    return hasCodeBySystem(codings, system) && (codeSystemEntries.contains(
        getCodeBySystem(codings, system)));
  }


  /**
   * Does the given fhir {@link Coding} contain a value that is part of the given code system.
   *
   * @return <code>True</code> is the code is not <code>null</code> and also part of the given code
   * system.
   */
  public static boolean isCodeInCodesystem(String code, Collection<String> codeSystemEntries) {
    return code != null && codeSystemEntries.contains(code);
  }

  /**
   * Checks whether the given fhir {@link Coding}  got a code with an additional non-null check.
   */
  public static boolean hasCode(Coding coding) {
    return coding != null && coding.hasCode();
  }

  /**
   * Checking if there is a fhir {@link Coding} in the list that got the given system.
   */
  public static boolean hasCodeBySystem(List<Coding> codings, String system) {
    return getCodeBySystem(codings, system) != null;
  }

  /**
   * This method will return <code>null</code> instead of creating an empty object like in the
   * official hapi fhir method {@link CodeableConcept#getCodingFirstRep()}.
   */
  public static Coding getFirstCoding(List<Coding> codings) {
    return codings.stream().findFirst().orElse(null);
  }

  /**
   * This method will return <code>null</code> instead of creating an empty object like in the
   * official hapi fhir method {@link CodeableConcept#getCodingFirstRep()}.
   */
  public static String getCodeOfFirstCoding(List<Coding> codings) {
    Coding firstCoding = getFirstCoding(codings);
    return firstCoding.hasCode() ? firstCoding.getCode() : null;
  }

  /**
   * This method retrieves the first {@link Coding#getCode()} of the first {@link Coding} and checks
   * if it matches the passed reference code.
   */
  public static boolean isCodeOfFirstCodingEquals(List<Coding> codings, String referenceCode) {
    String code = getCodeOfFirstCoding(codings);
    return code != null && code.equals(referenceCode);
  }

  /**
   * This method will retrieve the first code found of the first {@link CodeableConcept} found in a
   * given list.
   *
   * @return This method will return <code>null</code> instead of creating an empty object as in the
   * official hapi fhir method {@link CodeableConcept#getCodingFirstRep()}.
   */
  public static String getCodeOfFirstCodeableConcept(List<CodeableConcept> codeableConcepts) {
    if (codeableConcepts != null && codeableConcepts.size() > 0) {
      return getCodeOfFirstCoding(codeableConcepts.get(0).getCoding());
    }
    return null;
  }

  /**
   * This method retrieves the first {@link Coding#getCode()} of the first {@link Coding} and checks
   * if it matches the passed reference code.
   */
  public static boolean isCodeOfFirstCodeableConceptEquals(List<CodeableConcept> codeableConcepts,
      String referenceCode) {
    String code = getCodeOfFirstCodeableConcept(codeableConcepts);
    return code != null && code.equals(referenceCode);
  }

  /**
   * This method checks whether a given set of {@link CodeableConcept} contains any of the
   * {@link Coding#getCode() codes} in the given code system, to the given system.
   */
  public static boolean isCodeInCodeableConcepts(List<CodeableConcept> codeableConcepts,
      String system, List<String> referenceCodes) {
    if (codeableConcepts != null) {
      for (CodeableConcept concept : codeableConcepts) {
        if (isCodeInCodesystem(concept.getCoding(), referenceCodes, system)) {
          return true;
        }
      }
    }
    return false;
  }
}
