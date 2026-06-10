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

import static de.ukbonn.mwtek.utilities.enums.ConsentCategory.C570168;
import static de.ukbonn.mwtek.utilities.enums.ConsentFixedValues.CONSENT_CATEGORY_CODE;
import static de.ukbonn.mwtek.utilities.enums.ConsentFixedValues.CONSENT_CATEGORY_SYSTEM;
import static de.ukbonn.mwtek.utilities.enums.ConsentFixedValues.VERSIONS_MAIN_FORM;
import static de.ukbonn.mwtek.utilities.enums.ConsentFixedValues.VERSION_OID_Z_MODULE_ACRIBIS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_ADDITIONAL_QUANTITIES;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_ADDITIONAL_SAMPLING;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_COLLECT;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_COLLECT_STORE_USE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_MERGE_ANALYSIS_DATA_THIRD_PARTIES;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_RETRO_MERGE_ANALYSIS_DATA_THIRD_PARTIES;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_RETRO_SCIENTIFIC_USAGE_DSGVO;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_RETRO_STORAGE_PROCESS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_RETRO_STORING_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_SCIENTIFIC_USAGE_DSGVO;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_STORE_TRANSFER;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.BIOMAT_TRANSFER_OWNERSHIP;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.IDAT_COLLECT;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.IDAT_SAVE_PROCESS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_PROSPECTIVE_SAVE_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_PROSPECTIVE_SCIENTIFIC_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_PROSPECTIVE_TRANSFER;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_PROSPECTIVE_TRANSFER_KVNR;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_RETRO_SCIENTIFIC_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_RETRO_TRANSFER;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_RETRO_TRANSFER_KVNR;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_5_YEARS_RETRO_TRANSFER_SAVE_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_PROSPECTIVE_TRANSFER_SAVE_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.KKDAT_RETRO_TRANSFER_SAVE_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_COLLECTION;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_MERGE_THIRD_PARTIES;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_RETRO_MERGE_THIRD_PARTIES;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_RETRO_SAVE_PROCESS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_RETRO_SCIENTIFIC_USAGE_DSGVO;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_SAVE_PROCESS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_SCIENTIFIC_USAGE_DSGVO;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.PATDAT_RETRIEVAL_SAVING_USING;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.PATDAT_RETROSPECTIVE_USAGE;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.PROVISION_CODE_SYSTEM;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_ADDITIONAL_FINDING;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_ADDITIONAL_FINDING_LVL_2;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_ADDITIONS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_FURTHER_COLLECTION;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_FURTHER_STUDIES;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_MERGING_DBS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_ON_EVENTS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.Z2_PAT_DATA;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.Z2_PAT_DATA_LVL_2;
import static de.ukbonn.mwtek.utilities.generic.time.DateTools.getCurrentDateTime;
import static org.hl7.fhir.r4.model.Consent.ConsentProvisionType.PERMIT;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import de.ukbonn.mwtek.utilities.Compare;
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.fhir.interfaces.CaseIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.MiiContactHealthFacilityProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.MiiPatientProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.PatientIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FieldAlreadyInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.MandatoryFieldNotInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.OptionalFieldNotAvailableException;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
import de.ukbonn.mwtek.utilities.generic.time.DateTools;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Enumeration;

@Slf4j
@ResourceDef(name = "Consent")
public class MiiConsent extends Consent
    implements MiiPatientProvider,
        PatientIdentifierValueProvider,
        MiiContactHealthFacilityProvider,
        CaseIdentifierValueProvider {

  public static final String URN_OID = "urn:oid:";
  protected MiiPatient patient;
  @Getter @Setter protected String patientId;
  protected MiiContactHealthFacility encounter;
  protected String caseId;

  public MiiConsent() {
    super();
  }

  public MiiConsent(
      String patientId,
      String caseId,
      ConsentState status,
      CodeableConcept scope,
      List<CodeableConcept> category)
      throws IllegalArgumentException {
    // validate argument
    ExceptionTools.checkNullOrEmpty("patientId", patientId);
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("scope", scope);
    ExceptionTools.checkNullOrEmpty("Category", category);

    this.patientId = patientId;
    this.caseId = caseId;

    this.setStatus(status);
    this.setScope(scope);
    this.setCategory(category);
  }

  public MiiConsent(
      MiiPatient patient,
      MiiContactHealthFacility encounter,
      ConsentState status,
      CodeableConcept scope,
      List<CodeableConcept> category) {
    // validate arguments
    ExceptionTools.checkNull("patient", patient);
    ExceptionTools.checkNullOrEmpty("patient.identifier", patient.getIdentifier());
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("scope", scope);
    ExceptionTools.checkNullOrEmpty("Category", category);

    // set local variables
    this.patient = patient;
    this.patientId = patient.getPatientId();
    this.encounter = encounter;
    this.caseId = (encounter != null) ? encounter.getCaseId() : null;

    // set fhir content
    this.setStatus(status);
    this.setScope(scope);
    this.setCategory(category);
  }

  public MiiConsent(
      MiiContactHealthFacility encounter,
      ConsentState status,
      CodeableConcept scope,
      List<CodeableConcept> category)
      throws IllegalArgumentException, MandatoryFieldNotInitializedException {
    // validate arguments
    ExceptionTools.checkNull("encounter", encounter);
    ExceptionTools.checkNull("encounter", encounter.getMiiPatient());
    ExceptionTools.checkNullOrEmpty(
        "encounter.identifier", encounter.getMiiPatient().getIdentifier());
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("scope", scope);
    ExceptionTools.checkNullOrEmpty("Category", category);

    // set local variables
    this.patient = encounter.getMiiPatient();
    this.patientId = encounter.getMiiPatient().getPatientId();
    this.encounter = encounter;
    this.caseId = encounter.getCaseId();

    // set fhir content
    this.setStatus(status);
    this.setScope(scope);
    this.setCategory(category);
  }

  @Override
  public String getCaseId() {
    return this.caseId;
  }

  @Override
  public String getCaseIdentifierValue(String system)
      throws MandatoryFieldNotInitializedException, OptionalFieldNotAvailableException {
    if (Compare.isEqual(system, StaticValueProvider.SYSTEM_WITH_IDENTIFIER_ENCOUNTER)) {
      return this.caseId;
    } // if

    return this.getMiiContactHealthFacility().getCaseIdentifierValue(system);
  }

  @Override
  public String getPatientIdentifierValue(String system)
      throws MandatoryFieldNotInitializedException {
    if (Compare.isEqual(system, StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT)) {
      return this.patientId;
    } // if

    return this.getMiiPatient().getPatientIdentifierValue(system);
  }

  @Override
  public MiiPatient getMiiPatient() throws MandatoryFieldNotInitializedException {
    // the patient field is mandatory!
    if (this.patient == null) {
      throw new MandatoryFieldNotInitializedException();
    } // if
    return this.patient;
  }

  public Enumeration<ConsentProvisionType> getProvisionFirstRepTypeElement() {
    return this.getProvision().getProvisionFirstRep().getTypeElement();
  }

  @Override
  public MiiContactHealthFacility getMiiContactHealthFacility()
      throws MandatoryFieldNotInitializedException, OptionalFieldNotAvailableException {
    // the case is optional
    if (this.encounter == null) {
      if (this.caseId == null) {
        throw new OptionalFieldNotAvailableException();
      } // if
      throw new MandatoryFieldNotInitializedException();
    } // if
    return this.encounter;
  }

  @Override
  public void initializeMiiPatient(MiiPatient patient)
      throws IllegalArgumentException, FieldAlreadyInitializedException {
    // validate arguments
    ExceptionTools.checkNull("patient", patient);
    ExceptionTools.checkNullOrEmpty("patient.identifier", patient.getIdentifier());

    // must not be initialized more than once!
    if (this.patient != null) {
      throw new FieldAlreadyInitializedException();
    } // if

    // assign the patient to the local fields
    this.patient = patient;
    this.patientId = patient.getPatientId();
  }

  @Override
  public void initializeMiiContactHealthFacility(MiiContactHealthFacility encounter)
      throws IllegalArgumentException, FieldAlreadyInitializedException {
    // validate arguments
    ExceptionTools.checkNull("encounter", encounter);

    // must not be initialized more than once!
    if (this.encounter != null) {
      throw new FieldAlreadyInitializedException();
    } // if

    // assign the patient to the local fields (only, no fhir assignment)
    this.encounter = encounter;
    this.caseId = encounter.getCaseId();
  }

  @Override
  public boolean isMiiPatientInitialized() {
    return (this.patient != null);
  }

  @Override
  public boolean isMiiContactHealthFacilityInitialized() {
    return (this.encounter != null);
  }

  public boolean isPrivacyPolicyDocumentAndMiiConsentCategory() {
    return isMiiConsentCategory() && isLoincCategoryPrivacyPolicy();
  }

  protected boolean isMiiConsentCategory() {
    // Check mii coding
    return this.getCategory().stream()
        .anyMatch(x -> x.hasCoding(CONSENT_CATEGORY_SYSTEM, CONSENT_CATEGORY_CODE));
  }

  protected boolean isLoincCategoryPrivacyPolicy() {
    // Check loinc coding
    return this.getCategory().stream()
        .anyMatch(x -> x.hasCoding(C570168.getSystem(), C570168.getCode()));
  }

  public boolean isAcribisConsentAllowed() {
    return isAcribisForm()
        && isPrivacyPolicyDocumentAndMiiConsentCategory()
        && this.getProvision().getProvision().stream()
            .filter(pc -> pc.hasType() && pc.getType().equals(PERMIT))
            .anyMatch(
                pc ->
                    pc.hasCode()
                        // Checking level 1 and 2
                        && (pc.getCodeFirstRep()
                                .hasCoding(PROVISION_CODE_SYSTEM, Z2_PAT_DATA.getCode())
                            || pc.getCodeFirstRep()
                                .hasCoding(PROVISION_CODE_SYSTEM, Z2_PAT_DATA_LVL_2.getCode())));
  }

  /**
   * Retrieves the start date of the first permit provision that matches the Acribis criteria.
   *
   * <p>This method checks if the current context is an Acribis form and if the document is both a
   * privacy policy and falls under the MII consent category. If so, it filters the provision list
   * to find the first provision that:
   *
   * <p>It returns the start date of the matching provision's period.
   *
   * @return the start date of the first matching provision, or {@code null} if no match is found
   */
  public Date getAcribisPermitStartDate() {
    if (isAcribisForm() && isPrivacyPolicyDocumentAndMiiConsentCategory()) {
      return this.getProvision().getProvision().stream()
          .filter(pc -> pc.hasType() && pc.getType().equals(PERMIT))
          .filter(pc -> pc.hasPeriod() && pc.getPeriod().hasStart())
          .filter(
              pc ->
                  pc.hasCode()
                          // Checking level 1 and 2
                          && (pc.getCodeFirstRep()
                              .hasCoding(PROVISION_CODE_SYSTEM, Z2_PAT_DATA.getCode()))
                      || pc.getCodeFirstRep()
                          .hasCoding(PROVISION_CODE_SYSTEM, Z2_PAT_DATA_LVL_2.getCode()))
          .map(pc -> pc.getPeriod().getStart())
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  /**
   * Retrieves the start date of the first permit provision that matches the Acribis criteria.
   *
   * <p>This method checks if the current context is an Acribis form and if the document is both a
   * privacy policy and falls under the MII consent category. If so, it filters the provision list
   * to find the first provision that:
   *
   * <p>It returns the start date of the matching provision's period.
   *
   * @return the start date of the first matching provision, or {@code null} if no match is found
   */
  public Date getMainConsentPermitStartDate() {
    if (isMainConsentForm() && isPrivacyPolicyDocumentAndMiiConsentCategory()) {
      return this.getProvision().getProvision().stream()
          .filter(pc -> pc.hasType() && pc.getType().equals(PERMIT))
          .filter(pc -> pc.hasPeriod() && pc.getPeriod().hasStart())
          .filter(
              pc ->
                  pc.hasCode()
                          // Checking level 1 and 2
                          && (pc.getCodeFirstRep()
                              .hasCoding(
                                  PROVISION_CODE_SYSTEM, PATDAT_RETRIEVAL_SAVING_USING.getCode()))
                      || pc.getCodeFirstRep()
                          .hasCoding(PROVISION_CODE_SYSTEM, IDAT_COLLECT.getCode()))
          .map(pc -> pc.getPeriod().getStart())
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  /**
   * Checks if patient data usage is allowed based on consent form, policy, and specific provision
   * code.
   *
   * @param validationDate the date used to validate permit periods; if {@code null}, the validity
   *     period check is skipped
   * @return {@code true} if patient data usage is permitted; otherwise {@code false}
   */
  public boolean isPatDataUsageAllowed(Date validationDate) {
    return isMainConsentForm()
        && isPrivacyPolicyDocumentAndMiiConsentCategory()
        // Check level 1 and level 2 code
        && isPatDataUsage(validationDate);
  }

  public boolean isPatDataUsageCurrentlyAllowed() {
    return isPatDataUsageAllowed(getCurrentDateTime());
  }

  /**
   * Checks only whether the patient data usage provision code is permitted (via single L1 code or
   * all L2 codes).
   *
   * @param validationDate the date used to validate permit periods; if {@code null}, the validity
   *     period check is skipped
   * @return {@code true} if patient data usage is permitted, otherwise {@code false}
   */
  public boolean isPatDataUsage(Date validationDate) {
    return hasPermitWithCode(PATDAT_RETRIEVAL_SAVING_USING.getCode(), validationDate)
        || (hasPermitWithCode(MDAT_COLLECTION.getCode(), validationDate)
            && hasPermitWithCode(MDAT_SAVE_PROCESS.getCode(), validationDate)
            && hasPermitWithCode(MDAT_SCIENTIFIC_USAGE_DSGVO.getCode(), validationDate)
            && hasPermitWithCode(MDAT_MERGE_THIRD_PARTIES.getCode(), validationDate)
            && hasPermitWithCode(RECONTACTING_ON_EVENTS.getCode(), validationDate));
  }

  /**
   * Checks if patient data usage is allowed based on consent form, policy, and specific provision
   * code.
   *
   * @return true if patient data usage is permitted; false otherwise.
   */
  public boolean isMDataUsageAllowed(Date validationDate) {
    return isMainConsentForm()
        && isPrivacyPolicyDocumentAndMiiConsentCategory()
        // Check level 1 and level 2 code;
        && isMDataSaveUsage(validationDate);
  }

  /**
   * Checks whether the patient data usage provision code is permitted or the idat/mdat save and
   * usage processes are allowed.
   *
   * @param validationDate the date used to validate permit periods; if {@code null}, the validity
   *     period check is skipped
   * @return {@code true} if the mdata save and usage process is permitted, otherwise {@code false}
   */
  public boolean isMDataSaveUsage(Date validationDate) {
    return hasPermitWithCode(PATDAT_RETRIEVAL_SAVING_USING.getCode(), validationDate)
        || (hasPermitWithCode(IDAT_SAVE_PROCESS.getCode(), validationDate)
            && hasPermitWithCode(MDAT_SAVE_PROCESS.getCode(), validationDate)
            && hasPermitWithCode(MDAT_SCIENTIFIC_USAGE_DSGVO.getCode(), validationDate));
  }

  /**
   * Checks if "Patientendaten retrospektiv verarbeiten, nutzen" is permitted (via single L1 code or
   * all L2 codes).
   */
  public boolean isPatDataRetroUsage(Date validationDate) {
    return hasPermitWithCode(PATDAT_RETROSPECTIVE_USAGE.getCode(), validationDate)
        || (hasPermitWithCode(MDAT_RETRO_SAVE_PROCESS.getCode(), validationDate)
            && hasPermitWithCode(MDAT_RETRO_SCIENTIFIC_USAGE_DSGVO.getCode(), validationDate)
            && hasPermitWithCode(MDAT_RETRO_MERGE_THIRD_PARTIES.getCode(), validationDate));
  }

  /**
   * Checks if "Krankenkassendaten retrospektiv übertragen, speichern, nutzen" is permitted (via
   * single L1 code or all L2 codes).
   */
  public boolean isHealthInsuranceRetroUsage(Date validationDate) {
    return hasPermitWithCode(KKDAT_RETRO_TRANSFER_SAVE_USAGE.getCode(), validationDate)
        || (hasPermitWithCode(KKDAT_5_YEARS_RETRO_TRANSFER.getCode(), validationDate)
            && hasPermitWithCode(KKDAT_5_YEARS_RETRO_TRANSFER_SAVE_USAGE.getCode(), validationDate)
            && hasPermitWithCode(KKDAT_5_YEARS_RETRO_SCIENTIFIC_USAGE.getCode(), validationDate)
            && hasPermitWithCode(KKDAT_5_YEARS_RETRO_TRANSFER_KVNR.getCode(), validationDate));
  }

  /**
   * Checks if "Krankenkassendaten prospektiv übertragen, speichern, nutzen" is permitted (via
   * single L1 code or all L2 codes).
   */
  public boolean isHealthInsuranceProspectiveUsage(Date validationDate) {
    return hasPermitWithCode(KKDAT_PROSPECTIVE_TRANSFER_SAVE_USAGE.getCode(), validationDate)
        || (hasPermitWithCode(KKDAT_5_YEARS_PROSPECTIVE_TRANSFER.getCode(), validationDate)
            && hasPermitWithCode(KKDAT_5_YEARS_PROSPECTIVE_SAVE_USAGE.getCode(), validationDate)
            && hasPermitWithCode(
                KKDAT_5_YEARS_PROSPECTIVE_SCIENTIFIC_USAGE.getCode(), validationDate)
            && hasPermitWithCode(
                KKDAT_5_YEARS_PROSPECTIVE_TRANSFER_KVNR.getCode(), validationDate));
  }

  /**
   * Checks if "Biomaterial erheben, lagern, nutzen" is permitted (via single L1 code or all L2
   * codes).
   */
  public boolean isBioMatDataUsage(Date validationDate) {
    return hasPermitWithCode(BIOMAT_COLLECT_STORE_USE.getCode(), validationDate)
        || (hasPermitWithCode(BIOMAT_COLLECT.getCode(), validationDate)
            && hasPermitWithCode(BIOMAT_STORE_TRANSFER.getCode(), validationDate)
            && hasPermitWithCode(BIOMAT_TRANSFER_OWNERSHIP.getCode(), validationDate)
            && hasPermitWithCode(BIOMAT_SCIENTIFIC_USAGE_DSGVO.getCode(), validationDate)
            && hasPermitWithCode(
                BIOMAT_MERGE_ANALYSIS_DATA_THIRD_PARTIES.getCode(), validationDate));
  }

  /** Checks if "Biomaterial Zusatzentnahme" is permitted (via single L1 code or all L2 codes). */
  public boolean isBioMatAdditionalCollection(Date validationDate) {
    return hasPermitWithCode(BIOMAT_ADDITIONAL_SAMPLING.getCode(), validationDate)
        || (hasPermitWithCode(BIOMAT_ADDITIONAL_QUANTITIES.getCode(), validationDate));
  }

  /**
   * Checks if "Biomaterial retrospektiv speichern, nutzen" is permitted (via single L1 code or all
   * L2 codes).
   */
  public boolean isBioMatRetroUsage(Date validationDate) {
    return hasPermitWithCode(BIOMAT_RETRO_STORING_USAGE.getCode(), validationDate)
        || (hasPermitWithCode(BIOMAT_RETRO_STORAGE_PROCESS.getCode(), validationDate)
            && hasPermitWithCode(BIOMAT_RETRO_SCIENTIFIC_USAGE_DSGVO.getCode(), validationDate)
            && hasPermitWithCode(
                BIOMAT_RETRO_MERGE_ANALYSIS_DATA_THIRD_PARTIES.getCode(), validationDate));
  }

  /** Checks if "Rekontaktierung Ergänzungen" is permitted (via single L1 code or all L2 codes). */
  public boolean isRecontactingAdditions(Date validationDate) {
    return hasPermitWithCode(RECONTACTING_ADDITIONS.getCode(), validationDate)
        || (hasPermitWithCode(RECONTACTING_MERGING_DBS.getCode(), validationDate)
            && hasPermitWithCode(RECONTACTING_FURTHER_COLLECTION.getCode(), validationDate)
            && hasPermitWithCode(RECONTACTING_FURTHER_STUDIES.getCode(), validationDate));
  }

  /** Checks if "Rekontaktierung Zusatzbefund" is permitted (via single L1 code or all L2 codes). */
  public boolean isRecontactingAdditionalFindings(Date validationDate) {
    return hasPermitWithCode(RECONTACTING_ADDITIONAL_FINDING.getCode(), validationDate)
        || (hasPermitWithCode(RECONTACTING_ADDITIONAL_FINDING_LVL_2.getCode(), validationDate));
  }

  /**
   * Checks if recontacting the patient for further surveys is allowed.
   *
   * @return true if recontacting is permitted; false otherwise.
   */
  public boolean isRecontactingAllowed(Date validationDate) {
    return isMainConsentForm()
        && isPrivacyPolicyDocumentAndMiiConsentCategory()
        // Check level 1 and level 2 code;
        && (hasPermitWithCode(RECONTACTING_ADDITIONS.getCode(), validationDate)
            || hasPermitWithCode(RECONTACTING_FURTHER_COLLECTION.getCode(), validationDate));
  }

  /**
   * Checks if the consent form is the Acribis-specific form.
   *
   * @return true if the form is an Acribis form; false otherwise.
   */
  private boolean isAcribisForm() {
    return this.hasPolicyUriWithoutPrefix(VERSION_OID_Z_MODULE_ACRIBIS);
  }

  /**
   * Checks if the consent form is one of the main recognized consent forms.
   *
   * @return true if the form is a main consent form; false otherwise.
   */
  private boolean isMainConsentForm() {
    return this.hasAnyPolicyUriWithoutPrefix(VERSIONS_MAIN_FORM);
  }

  /**
   * Checks whether there is any policy URI matching one of the given OIDs (without the URN_OID
   * prefix).
   *
   * @param oids The list of OIDs to check for (without URN prefix).
   * @return true if a matching policy URI exists for any of the given OIDs, false otherwise.
   */
  private boolean hasAnyPolicyUriWithoutPrefix(List<String> oids) {
    return this.getPolicy().stream()
        .filter(ConsentPolicyComponent::hasUri)
        .map(x -> x.getUri().replaceFirst(URN_OID, ""))
        .anyMatch(oids::contains);
  }

  /**
   * Checks whether there is any policy URI matching the given OID (without the URN_OID prefix).
   *
   * @param oid The OID to check for (without URN prefix).
   * @return true if a matching policy URI exists, false otherwise.
   */
  private boolean hasPolicyUriWithoutPrefix(String oid) {
    return hasAnyPolicyUriWithoutPrefix(Collections.singletonList(oid));
  }

  /**
   * Checks whether a permit with the given code exists.
   *
   * <p>If {@code validationDate} is not {@code null}, only permits that are valid at the given
   * point in time based on their period are considered.
   *
   * @param code the permit code to search for
   * @param validationDate the date used to validate the permit period; if {@code null}, the
   *     validity period check is skipped
   * @return {@code true} if a matching permit exists, otherwise {@code false}
   */
  public boolean hasPermitWithCode(String code, Date validationDate) {
    return this.getProvision().getProvision().stream()
        .filter(pc -> pc.hasType() && pc.getType().equals(PERMIT))
        .filter(
            pc ->
                validationDate == null || DateTools.isWithinPeriod(validationDate, pc.getPeriod()))
        .anyMatch(
            pc ->
                pc.hasCode()
                    && pc.getCode().stream()
                        .anyMatch(c -> c.hasCoding(PROVISION_CODE_SYSTEM, code)));
  }

  // Filter rejected/canceled consents
  public boolean isActive() {
    return this.hasStatus() && this.getStatus() == ConsentState.ACTIVE;
  }
}
