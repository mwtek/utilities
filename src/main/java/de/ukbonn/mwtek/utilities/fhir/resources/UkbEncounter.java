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
package de.ukbonn.mwtek.utilities.fhir.resources;

import static de.ukbonn.mwtek.utilities.enums.EncounterContactLevel.DEPARTMENT_CONTACT;
import static de.ukbonn.mwtek.utilities.enums.EncounterContactLevel.FACILITY_CONTACT;
import static de.ukbonn.mwtek.utilities.enums.EncounterContactLevel.SUPPLY_CONTACT;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.CASETYPE_CONTACT_ART_SYSTEM;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.CASETYPE_INTENSIVESTATIONARY;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.CASETYPE_PARTSTATIONARY;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.CASETYPE_POSTSTATIONARY;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.CASETYPE_PRESTATIONARY;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.DEATH_CODE;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.DISCHARGE_DISPOSITION_EXT_URL;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.DISCHARGE_DISPOSITION_FIRST_AND_SECOND_POS_EXT_URL;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.DISCHARGE_DISPOSITION_FIRST_AND_SECOND_POS_SYSTEM;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.ENCOUNTER_CLASS_INPATIENT_CODES;
import static de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets.KdsEncounterFixedValues.ENCOUNTER_CLASS_OUTPATIENT_CODES;
import static de.ukbonn.mwtek.utilities.fhir.misc.FhirCodingTools.isCodeInCodesystem;
import static de.ukbonn.mwtek.utilities.fhir.misc.ResourceConverter.extractReferenceId;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import de.ukbonn.mwtek.utilities.Compare;
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.enums.EncounterContactLevel;
import de.ukbonn.mwtek.utilities.fhir.interfaces.CaseIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.PatientIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbPatientProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FhirTools;
import de.ukbonn.mwtek.utilities.fhir.misc.FieldAlreadyInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.MandatoryFieldNotInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;

@ResourceDef(name = "Encounter")
@Slf4j
public class UkbEncounter extends Encounter
    implements UkbPatientProvider, PatientIdentifierValueProvider, CaseIdentifierValueProvider {

  protected UkbPatient patient;
  @Setter protected String patientId;
  private String visitNumberIdentifierValue;

  @Setter protected String facilityContactId;

  /**
   * @deprecated This constructor is only used for Fhir resource validation purpose. Use other
   *     constructors for creating an instance of this resource.
   */
  @Deprecated
  public UkbEncounter() {
    super();
  }

  /**
   * Creates a new UkbEncounter object without defined {@link UkbPatient}. This object may be
   * assigned later using {@link #initializeUkbPatient(UkbPatient)}. The patient is mandatory,
   * therefore the <code>patientId</code> must be specified
   *
   * @param patientId the default system id of the patient
   * @param encounterStatus {@link EncounterStatus} e.g. {@link EncounterStatus#INPROGRESS}
   * @param encounterClass The EncounterClass e.g. "pre-stationary"
   */
  public UkbEncounter(
      String patientId, Enumeration<EncounterStatus> encounterStatus, Coding encounterClass) {
    super(encounterStatus, encounterClass);
    // validate arguments
    ExceptionTools.checkNullOrEmpty("patientId", patientId);

    // set local variables
    this.patientId = patientId;
  }

  @Deprecated
  public UkbEncounter(
      UkbPatient patient, Enumeration<EncounterStatus> status, Coding encounterClass)
      throws IllegalArgumentException {
    super(status, encounterClass);
    // INFO: this constructor seems deprecated and isnt handling the parameters
    // u could just replace the body with initializePatient at the moment for the same output

    // validate arguments
    ExceptionTools.checkNull("patient", patient);
    ExceptionTools.checkNullOrEmpty("patient.Identifier", patient.getIdentifier());

    // set local variables
    this.patient = patient;
    this.patientId = patient.getPatientId();

    // set fhir content
    this.setSubject(
        new Reference()
            .setIdentifier(
                FhirTools.getIdentifierBySystem(
                    StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT, patient.getIdentifier())));

    // set external id
    Identifier identifier = new Identifier();
    identifier.setSystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT);
    identifier.setValue(patient.getId());
    this.setSubject(new Reference().setIdentifier(identifier));
  }

  @Override
  public UkbPatient getUkbPatient() throws MandatoryFieldNotInitializedException {
    // the patient field is mandatory!
    if (this.patient == null) {
      throw new MandatoryFieldNotInitializedException();
    } // if
    return this.patient;
  }

  @Override
  public void initializeUkbPatient(UkbPatient patient)
      throws IllegalArgumentException, FieldAlreadyInitializedException {
    // validate arguments
    ExceptionTools.checkNull("patient", patient);
    ExceptionTools.checkNullOrEmpty("patient.Identifier", patient.getIdentifier());

    // must not be initialized more than once!
    if (this.patient != null) {
      throw new FieldAlreadyInitializedException();
    } // if

    // assign the patient to the local fields
    this.patient = patient;
    this.patientId = patient.getPatientId();

    // assign the patient to the fhir object
    this.setSubject(
        new Reference()
            .setIdentifier(
                FhirTools.getIdentifierBySystem(
                    StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT, patient.getIdentifier())));

    Identifier identifier = new Identifier();
    identifier.setSystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT);
    identifier.setValue(patient.getId());
    this.setSubject(new Reference().setIdentifier(identifier));
  }

  @Override
  public boolean isUkbPatientInitialized() {
    return (this.patient != null);
  }

  @Override
  public String getPatientId() {
    return this.patientId;
  }

  @Override
  public String getPatientIdentifierValue(String system)
      throws MandatoryFieldNotInitializedException {
    if (Compare.isEqual(system, StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT)) {
      return this.patientId;
    } // if

    return this.getUkbPatient().getPatientIdentifierValue(system);
  }

  @Override
  public String getCaseIdentifierValue(String system) {
    if (system == null) {
      system = StaticValueProvider.SYSTEM_WITH_IDENTIFIER_ENCOUNTER;
    }
    Identifier identifier = FhirTools.getIdentifierBySystem(system, this.getIdentifier());
    if (identifier != null) {
      return identifier.getValue();
    }
    return null;
  }

  public String getOfficialIdentifierValue() {
    if (this.hasIdentifier()) {
      Identifier officialIdentifier = FhirTools.getOfficialIdentifier(this.getIdentifier(), false);
      if (officialIdentifier != null && officialIdentifier.hasValue()) {
        return officialIdentifier.getValue();
      }
    }
    return null;
  }

  /** Auxiliary function to return the textual value of the visit number. */
  public String getVisitNumberIdentifierValue() {
    if (visitNumberIdentifierValue == null) {
      if (this.hasIdentifier()) {
        Identifier visitNumberIdentifier =
            FhirTools.getVisitNumberIdentifier(this.getIdentifier(), false);
        if (visitNumberIdentifier != null && visitNumberIdentifier.hasValue()) {
          visitNumberIdentifierValue = visitNumberIdentifier.getValue();
        }
      }
    }
    return visitNumberIdentifierValue;
  }

  public boolean hasVisitNumberIdentifierValue() {
    return this.getVisitNumberIdentifierValue() != null;
  }

  public boolean isIcuCase(Collection<String> icuLocationIds, boolean checkActiveOnly) {
    // Check for matches in ICU location IDs based on whether we want to include only active
    // periods.
    return this.getLocation().stream()
        .filter(
            location ->
                !checkActiveOnly || (location.hasPeriod() && !location.getPeriod().hasEnd()))
        .anyMatch(location -> icuLocationIds.contains(location.getLocation().getIdBase()));
  }

  public boolean isCurrentlyOnIcuWard(Collection<String> icuLocationIds) {
    // Look for active icu locations
    return isIcuCase(icuLocationIds, true);
  }

  /**
   * Encounter.period.start is a mandatory field in the kds profile.
   *
   * @return True if {@link UkbEncounter#getPeriod() Encounter.period.start} is not null
   */
  public boolean isPeriodStartExistent() {
    return this.getPeriod() != null && this.getPeriod().getStart() != null;
  }

  public String getFacilityContactId() {
    if (facilityContactId != null) {
      return facilityContactId;
    } else {
      if (this.isFacilityContact()) {
        return getId();
      }
    }
    return null;
  }

  /**
   * Determines whether the passed encounter type is a facility contact ("Einrichtungskontakt"). To
   * ensure backwards compatibility, any missing type is considered as a facility contact as well.
   *
   * @return True if the encounter is a facility contact, otherwise false.
   */
  public boolean isFacilityContact() {
    return isContactType(this, FACILITY_CONTACT.getCode());
  }

  /** Determines whether the passed encounter instance is in-progress. */
  public boolean isActive() {
    return this.hasStatus() && this.getStatus() == EncounterStatus.INPROGRESS;
  }

  /**
   * Determines whether the passed encounter type is a supply contact ("Versorgungsstellenkontakt").
   *
   * @return True if the encounter is a supply contact, otherwise false.
   */
  public boolean isSupplyContact() {
    return isContactType(this, SUPPLY_CONTACT.getCode());
  }

  /**
   * Determines whether the passed encounter type is a department contact ("Abteilungskontakt").
   *
   * @return True if the encounter is a department contact, otherwise false.
   */
  public boolean isDepartmentContact() {
    return isContactType(this, DEPARTMENT_CONTACT.getCode());
  }

  /**
   * Internal method to determine whether the passed encounter type matches the specified contact
   * level code.
   *
   * @param encounter The encounter to be checked.
   * @param contactLevelCode The contact level code to match.
   * @return True if the encounter matches the specified contact level code, otherwise false.
   */
  public static boolean isContactType(UkbEncounter encounter, String contactLevelCode) {
    // If there is no type specified in the encounter, it's considered as the specified contact
    // type.
    if (!encounter.hasType()) {
      return true;
    }

    // Check if any type in the encounter matches the specified contact level code.
    return encounter.getType().stream()
        .flatMap(x -> x.getCoding().stream())
        .filter(Coding::hasSystem)
        .filter(x -> x.getSystem().equals(EncounterContactLevel.SYSTEM))
        .anyMatch(contactLevelType -> contactLevelType.getCode().equals(contactLevelCode));
  }

  /**
   * A simple check if the given contact type got the value "prestationary".
   *
   * @return <code>True</code> if the case type equals "prestationary".
   */
  public boolean isCaseTypePreStationary() {
    String contactType = getContactType(this.getType());
    return contactType != null && contactType.equals(CASETYPE_PRESTATIONARY);
  }

  /**
   * A simple check if the given contact type got the value "intensivstationaer".
   *
   * @return <code>True</code> if the case type equals "intensivstationaer".
   */
  public boolean isCaseTypeIntensiveStationary() {
    String contactType = getContactType(this.getType());
    return contactType != null && contactType.equals(CASETYPE_INTENSIVESTATIONARY);
  }

  /**
   * A simple check if the given contact type got the value "prestationary".
   *
   * @return <code>True</code> if the case type equals "prestationary".
   */
  public boolean isCaseTypePostStationary() {
    String contactType = getContactType(this.getType());
    return contactType != null && contactType.equals(CASETYPE_POSTSTATIONARY);
  }

  /**
   * Is the case class counted as "inpatient" regarding the json data specification (without
   * pre-stationary and post-stationary cases)?
   *
   * @return <code>True</code>, if the case class of the encounter is "inpatient"
   */
  public boolean isCaseClassInpatient() {
    return this.hasClass_()
        && isCodeInCodesystem(this.getClass_().getCode(), ENCOUNTER_CLASS_INPATIENT_CODES);
  }

  /**
   * is the case class counted as "outpatient" regarding the json data specification (plus
   * pre-stationary + post-stationary cases that are counted as "outpatient" logic-wise in the
   * workflow aswell)
   *
   * @return <code>True</code>, if the case class of the encounter is "outpatient".
   */
  public boolean isCaseClassOutpatient() {
    return this.hasClass_()
        && isCodeInCodesystem(this.getClass_().getCode(), ENCOUNTER_CLASS_OUTPATIENT_CODES);
  }

  /**
   * Is the case type semi stationary?
   *
   * @return <code>True</code>, if the encounter.type holds at least one entry with type =
   *     "teilstationär"
   */
  public boolean isSemiStationary() {
    return !this.getType().stream()
        .filter(x -> x.hasCoding(CASETYPE_CONTACT_ART_SYSTEM, CASETYPE_PARTSTATIONARY))
        .toList()
        .isEmpty();
  }

  /**
   * Retrieval of the value that is part of a slice in {@link Encounter#getType() Encounter.type}.
   *
   * @param listType List of {@link Encounter#getType() Encounter.types}.
   * @return The contact-type of a german value set (e.g. "vorstationär").
   */
  public static String getContactType(List<CodeableConcept> listType) {
    StringBuilder contactType = new StringBuilder();
    listType.forEach(
        ccType ->
            ccType
                .getCoding()
                .forEach(
                    codingType -> {
                      if (codingType.hasSystem()
                          && codingType.getSystem().equals(CASETYPE_CONTACT_ART_SYSTEM)) {
                        contactType.append(codingType.getCode());
                      }
                    }));

    return !contactType.isEmpty() ? contactType.toString() : null;
  }

  /**
   * Checks whether the given encounter is currently in an ICU location or not.
   *
   * @param icuLocationIds The list of ICU location IDs to check against. If its empty it will
   *     return {@code False}.
   * @return {@code True} if the encounter is currently in an ICU location; otherwise, {@code
   *     False}.
   */
  public boolean isCurrentlyOnIcu(List<String> icuLocationIds) {
    if (icuLocationIds == null || icuLocationIds.isEmpty()) {
      return false;
    }

    // Find the active transfer and if one can be found, check if it's an icu location.
    return this.getLocation().stream()
        .filter(x -> x.hasPeriod() && !x.getPeriod().hasEnd())
        .anyMatch(x -> icuLocationIds.contains(x.getLocation().getIdBase()));
  }

  /**
   * Determine via the discharge disposition which is part of {@link
   * UkbEncounter#getHospitalization()} whether the patient is deceased within the scope of the case
   * under review.
   *
   * @return <code>false</code>, if no valid discharge disposition can be found in the {@link
   *     UkbEncounter#getHospitalization()} instance and <code>true</code> if the discharge code
   *     ("07") was found
   */
  public boolean isPatientDeceased() {
    Encounter.EncounterHospitalizationComponent hospComp = this.getHospitalization();
    // check if encounter resource got a discharge disposition with a certain extension url
    if (hospComp != null
        && hospComp.hasDischargeDisposition()
        && hospComp.getDischargeDisposition().hasExtension(DISCHARGE_DISPOSITION_EXT_URL)) {
      Extension extDischargeDisp =
          hospComp.getDischargeDisposition().getExtensionByUrl(DISCHARGE_DISPOSITION_EXT_URL);
      Extension extPosFirstAndSec =
          extDischargeDisp.getExtensionByUrl(DISCHARGE_DISPOSITION_FIRST_AND_SECOND_POS_EXT_URL);
      if (extPosFirstAndSec != null) {
        // the extension always contains a coding as value
        try {
          Coding coding = (Coding) extPosFirstAndSec.getValue();
          // If the system is valid, check the code right after
          if (coding.hasSystem()
              && coding.getSystem().equals(DISCHARGE_DISPOSITION_FIRST_AND_SECOND_POS_SYSTEM)) {
            // the code must be "07" (Death)
            if (coding.hasCode() && coding.getCode().equals(DEATH_CODE)) {
              return true;
            }
          } else {
            return false;
          }
        } catch (ClassCastException cce) {
          log.error(
              "Encounter.hospitalization.dischargeDisposition.EntlassungsgrundErsteUndZweiteStelle.value"
                  + " must be from type Coding but found: {}",
              extPosFirstAndSec.getValue().getClass());
        }
      }
    }

    return false;
  }

  /**
   * Returns a list of reference IDs for discharge diagnoses from the patient's diagnosis list.
   *
   * <p>A discharge diagnosis is identified by a {@code use} coding with the system {@code
   * "http://terminology.hl7.org/CodeSystem/diagnosis-role"} and the code {@code "DD"}.
   *
   * @return a list of reference strings to the condition resources marked as discharge diagnoses;
   *     returns an empty list if no such diagnoses are found or if no diagnosis is present.
   */
  public List<String> getDischargeDiagnosisReferenceIds() {
    if (!this.hasDiagnosis()) {
      return Collections.emptyList();
    }

    final String DIAGNOSIS_ROLE_SYSTEM = "http://terminology.hl7.org/CodeSystem/diagnosis-role";
    final String DISCHARGE_DIAGNOSIS_CODE = "DD";

    return this.getDiagnosis().stream()
        .filter(
            dc ->
                dc.hasUse()
                    && dc.hasCondition()
                    && !dc.getCondition().isEmpty()
                    && dc.getUse().hasCoding(DIAGNOSIS_ROLE_SYSTEM, DISCHARGE_DIAGNOSIS_CODE))
        .map(dc -> extractReferenceId(dc.getCondition()))
        .collect(Collectors.toList());
  }
}
