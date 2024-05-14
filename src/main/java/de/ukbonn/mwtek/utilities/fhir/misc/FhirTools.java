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

import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.IDENTIFIER_VN_TYPE_CODE;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.IDENTIFIER_VN_TYPE_SYSTEM;

import de.ukbonn.mwtek.utilities.Compare;
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbEncounter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.BaseDateTimeType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.MedicationAdministration.MedicationAdministrationDosageComponent;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Timing;
import org.hl7.fhir.r4.model.Type;

@Slf4j
public class FhirTools {

  public static void addIdentifierValue(List<String> values, String system, Identifier identifier) {
    if (identifier == null) {
      // nothing to do
      return;
    } // if

    // retrieve and add the value
    String value = identifier.getValue();
    if ((value != null) && ((system == null) || (Compare.isEqual(system, identifier.getSystem())))) {
      values.add(value);
    } // if
  }

  public static void addIdentifierValues(List<String> values, String system, Collection<Identifier> identifiers) {
    if ((identifiers == null) || (identifiers.isEmpty())) {
      // nothing to do
      return;
    } // if

    // handle all identifiers
    for (Identifier currentIdentifier : identifiers) {
      addIdentifierValue(values, system, currentIdentifier);
    } // for
  }

  public static void addReferenceIdentifier(List<Identifier> identifiers, Reference reference) {
    if (reference == null) {
      // nothing to do
      return;
    } // if

    // retrieve and add the identifier
    Identifier identifier = reference.getIdentifier();
    if (identifier != null) {
      identifiers.add(identifier);
    } // if
  }

  public static void addReferenceIdentifierValue(List<String> values, String system, Reference reference) {
    if (reference == null) {
      // nothing to do
      return;
    } // if

    // retrieve and add the identifier
    Identifier identifier = reference.getIdentifier();
    addIdentifierValue(values, system, identifier);
  }

  public static BigDecimal getEffectiveReference(Type effective) throws IllegalArgumentException {
    // validate argument
    ExceptionTools.checkNull("effective", effective);

    // check all allowed types
    if ((effective instanceof DateTimeType) || (effective instanceof InstantType)) {
      // subclass of BaseDateTimeDt (like DateTimeDt or InstantDt) can be used directly
      return FhirTools.getReference((BaseDateTimeType) effective);
    } // if

    if (effective instanceof Period) {
      DateTimeType start = ((Period) effective).getStartElement();
      if (start == null) {
        throw new IllegalArgumentException("start");
      } // if
      return FhirTools.getReference(start);
    } // if

    if (effective instanceof Timing) {
      List<DateTimeType> events = ((Timing) effective).getEvent();
      if ((events == null) || (events.isEmpty()) || (events.get(0) == null)) {
        throw new IllegalArgumentException("event");
      } // if
      return FhirTools.getReference(events.get(0));
    } // if

    // not allowed
    throw new IllegalArgumentException("data");
  }

  public static long getEffectiveReferenceAsMicros(Type effective)
    throws IllegalArgumentException, ArithmeticException {
    // validate argument
    ExceptionTools.checkNull("effective", effective);

    // check all allowed types
    if ((effective instanceof DateTimeType) || (effective instanceof InstantType)) {
      // subclass of BaseDateTimeDt (like DateTimeDt or InstantDt) can be used directly
      return FhirTools.getReferenceAsMicros((BaseDateTimeType) effective);
    } // if

    if (effective instanceof Period) {
      DateTimeType start = ((Period) effective).getStartElement();
      if (start == null) {
        throw new IllegalArgumentException("start");
      } // if
      return FhirTools.getReferenceAsMicros(start);
    } // if

    if (effective instanceof Timing) {
      List<DateTimeType> events = ((Timing) effective).getEvent();
      if ((events == null) || (events.isEmpty()) || (events.get(0) == null)) {
        throw new IllegalArgumentException("event");
      } // if
      return FhirTools.getReferenceAsMicros(events.get(0));
    } // if

    // not allowed
    throw new IllegalArgumentException("data");
  }

  public static Identifier getIdentifierBySystem(String system, List<Identifier> identifierList)
    throws IllegalArgumentException {
    ExceptionTools.checkNullOrEmpty("identifierList", identifierList);
    // if system is provided then return identifier for this system
    if (system != null) {
      for (Identifier identifier : identifierList) {
        if (identifier != null && Compare.isEqual(system, identifier.getSystem())) {
          return identifier;
        } // if
      } // for
    } // if
    // if system is not provided then return identifier for default system
    else {
      system = StaticValueProvider.SYSTEM;
      for (Identifier identifier : identifierList) {
        if (identifier != null && Compare.isEqual(system, identifier.getSystem())) {
          return identifier;
        } // if
      } // for
    } // else

    return null;
  }

  public static Identifier getOfficialIdentifier(
    List<Identifier> identifierList,
    boolean useFirstIdentifierFoundIfOfficialIsMissing
  ) throws IllegalArgumentException {
    ExceptionTools.checkNullOrEmpty("identifierList", identifierList);
    // if the identifier use is 'official' return this identifier
    for (Identifier identifier : identifierList) {
      if (identifier != null && identifier.hasUse() && identifier.getUse() == IdentifierUse.OFFICIAL) {
        return identifier;
      } // if
    } // for
    // Experimental but may be useful in the future: return the first identifier found if there
    // was no official identifier found in the fhir data
    if (useFirstIdentifierFoundIfOfficialIsMissing) {
      // if the 'use' attribute is not set return the first one by default
      for (Identifier identifier : identifierList) {
        if (identifier != null && !identifier.hasUse()) {
          return identifier;
        } // if
      } // for
    }
    return null;
  }

  /**
   * Retrieves the visit number identifier from the given list of identifiers.
   *
   * @param identifierList                                The list of identifiers to search.
   * @param useFirstIdentifierFoundIfVisitNumberIsMissing If true, returns the first identifier from
   *                                                      the list if no visit number slice was
   *                                                      found.
   * @return The visit number identifier if found, otherwise null.
   * @throws IllegalArgumentException If the identifierList is null or empty.
   */
  public static Identifier getVisitNumberIdentifier(
    List<Identifier> identifierList,
    boolean useFirstIdentifierFoundIfVisitNumberIsMissing
  ) throws IllegalArgumentException {
    // Check for null or empty identifier list
    ExceptionTools.checkNullOrEmpty("identifierList", identifierList);

    // Check if the identifier type is 'official'
    for (Identifier identifier : identifierList) {
      if (identifier != null && identifier.hasType()) {
        CodeableConcept identifierType = identifier.getType();
        if (identifierType.hasCoding(IDENTIFIER_VN_TYPE_SYSTEM, IDENTIFIER_VN_TYPE_CODE)) {
          return identifier;
        }
      } // if
    } // for

    // Experimental but may be useful in the future: return the first identifier found if there
    // is no visit number slice found in the fhir data
    if (useFirstIdentifierFoundIfVisitNumberIsMissing) {
      for (Identifier identifier : identifierList) {
        if (identifier != null && !identifier.hasUse()) {
          return identifier;
        }
      } // for
    } // if
    return null;
  }

  public static Set<String> getOfficialIdentifiers(Set<String> positiveEncounterIds, List<UkbEncounter> ukbEncounters) {
    Set<UkbEncounter> encountersWithIdentifier = ukbEncounters
      .parallelStream()
      .filter(Encounter::hasIdentifier)
      .collect(Collectors.toSet());
    if (encountersWithIdentifier.size() != ukbEncounters.size()) {
      log.warn(
        "Found: " +
        ukbEncounters.size() +
        " encounter resources but only " +
        encountersWithIdentifier.size() +
        " got an identifier!"
      );
    }
    if (!encountersWithIdentifier.isEmpty()) {
      Set<String> ukbEncounterWithOfficialIdentifier = new HashSet<>(
        ukbEncounters
          .stream()
          .filter(x -> positiveEncounterIds.contains(x.getId()))
          .map(UkbEncounter::getOfficialIdentifierValue)
          .toList()
      );
      if (ukbEncounterWithOfficialIdentifier.isEmpty()) {
        log.error(
          "Not a single encounter with an identifier of type 'official' was found. No " +
          "hierarchical determination from supply contact -> facility contact is " +
          "possible!"
        );
      }
      return ukbEncounterWithOfficialIdentifier;
    } else {
      return null;
    }
  }

  public static Set<String> getVisitNumberIdentifiers(
    Set<String> positiveEncounterIds,
    List<UkbEncounter> ukbEncounters
  ) {
    Set<UkbEncounter> encountersWithIdentifier = ukbEncounters
      .parallelStream()
      .filter(Encounter::hasIdentifier)
      .collect(Collectors.toSet());
    if (encountersWithIdentifier.size() != ukbEncounters.size()) {
      log.warn(
        "Found: " +
        ukbEncounters.size() +
        " encounter resources but only " +
        encountersWithIdentifier.size() +
        " are using the 'Aufnahmenummer' slice."
      );
    }
    if (!encountersWithIdentifier.isEmpty()) {
      Set<String> encountersVisitNumbers = new HashSet<>(
        ukbEncounters
          .stream()
          .filter(x -> positiveEncounterIds.contains(x.getId()))
          .map(UkbEncounter::getVisitNumberIdentifierValue)
          .toList()
      );
      if (encountersVisitNumbers.isEmpty()) {
        log.error(
          "Not a single encounter with an identifier of slice type 'Aufnahmenummer' was found. " +
          "No hierarchical determination from supply contact -> facility contact is " +
          "possible!"
        );
      }
      return encountersVisitNumbers;
    } else {
      return null;
    }
  }

  /**
   * Filters a list of UkbEncounter objects based on their official identifier values and adds a
   * given extension to each filtered encounter.
   *
   * @param visitNumberIdentifierValues A set of official identifier values to filter the
   *                                    UkbEncounter objects independent fr
   * @param ukbEncounters               A list of UkbEncounter objects to be filtered.
   * @param flag                        The extension to be added to the filtered UkbEncounter
   *                                    objects.
   * @return A set of UkbEncounter objects filtered by the official identifier values with the
   * specified extension added.
   */
  public static Set<UkbEncounter> flagEncountersByIdentifierValue(
    Set<String> visitNumberIdentifierValues,
    List<UkbEncounter> ukbEncounters,
    Extension flag
  ) {
    return ukbEncounters
      .stream()
      .filter(x -> visitNumberIdentifierValues.contains(x.getVisitNumberIdentifierValue()))
      .peek(x -> x.addExtension(flag))
      .collect(Collectors.toSet());
  }

  public static MedicationAdministrationDosageComponent getMedicationAdministrationDosageComponent(Dosage dosage) {
    MedicationAdministrationDosageComponent medicationAdministrationDosageComponent =
      new MedicationAdministrationDosageComponent();
    medicationAdministrationDosageComponent.setText(dosage.getText());
    medicationAdministrationDosageComponent.setSite(dosage.getSite());
    medicationAdministrationDosageComponent.setRoute(dosage.getRoute());
    medicationAdministrationDosageComponent.setMethod(dosage.getMethod());
    // Dosage has multiple doseAndRate. On the other hand MedicationAdministrationDosageComponent
    // has just one dose
    return medicationAdministrationDosageComponent;
  }

  /**
   * Filter "given consent" from "all consent" Bundle.
   *
   * @param bundle - input Consent bundle
   * @return - Bundle with permit status
   */
  public static Bundle getPermittedConsentBundle(Bundle bundle) {
    Bundle response = new Bundle();
    if (bundle != null) {
      List<BundleEntryComponent> entry = bundle.getEntry();
      for (BundleEntryComponent e : entry) {
        Consent c = (Consent) e.getResource();
        if (c.getProvision().getCode().size() > 0) {
          response.addEntry(e);
        }
      }
    }
    return response;
  }

  /**
   * Extract PIDs from Consent Bundle.
   *
   * @param bundle - input Consent bundle
   * @return - list of unique PIDs from bundle.
   */
  public static List<String> getPIDsFromConsentBundle(Bundle bundle) {
    Set<String> hs = new HashSet<>();
    if (bundle != null) {
      List<BundleEntryComponent> entry = bundle.getEntry();
      for (BundleEntryComponent e : entry) {
        Consent consent = (Consent) e.getResource();
        hs.add(consent.getPatient().getIdentifier().getValue());
      }
    }
    return new ArrayList<>(hs);
  }

  public static BigDecimal getReference(BaseDateTimeType dateTime) throws IllegalArgumentException {
    // validate argument
    ExceptionTools.checkNull("dateTime", dateTime);

    // convert the value to
    Date value = dateTime.getValue();
    if (value == null) {
      throw new IllegalArgumentException("value");
    } // if
    return millisToSeconds(value.getTime());
  }

  public static long getReferenceAsMicros(BaseDateTimeType dateTime)
    throws IllegalArgumentException, ArithmeticException {
    // validate argument
    ExceptionTools.checkNull("dateTime", dateTime);

    // convert the value to
    Date value = dateTime.getValue();
    if (value == null) {
      throw new IllegalArgumentException("value");
    } // if
    return millisToMicros(value.getTime());
  }

  public static Object getValue(Type value) {
    if (value instanceof PrimitiveType<?>) {
      // get the value from the primitiv
      return ((PrimitiveType<?>) value).getValue();
    } // if
    if (value instanceof Quantity) {
      // get the value from the primitiv
      return ((Quantity) value).getValue();
    } // if

    // not a primitive type: return the whole value
    return value;
  }

  /**
   * Convert the given Unix-time in milliseconds to microseconds by multiplying the value with
   * 10^3.
   *
   * @param millis the milliseconds value to convert
   * @return milliseconds to macroseconds
   * @throws ArithmeticException thrown if the converted value exceeds the range of
   *                             <code>long</code>
   */
  public static long millisToMicros(long millis) throws ArithmeticException {
    // multiply with 10^3
    return Math.multiplyExact(millis, 1_000L);
  }

  /**
   * Convert the given Unix-time in milliseconds to seconds by dividing the value by 10^3.
   *
   * @param millis the milliseconds value to convert
   * @return milliseconds to seconds
   */
  public static BigDecimal millisToSeconds(long millis) {
    // divide by 10^3
    return BigDecimal.valueOf(millis).divide(BigDecimal.valueOf(1_000L), 3, RoundingMode.UNNECESSARY);
  }
}
