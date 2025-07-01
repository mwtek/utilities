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
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.MDAT_COLLECTION;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.PATDAT_RETRIEVAL_SAVING_USING;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.PROVISION_CODE_SYSTEM;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_ADDITIONS;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.RECONTACTING_FURTHER_COLLECTION;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.Z2_PAT_DATA;
import static de.ukbonn.mwtek.utilities.enums.MiiConsentPolicyValueSet.Z2_PAT_DATA_LVL_2;
import static org.hl7.fhir.r4.model.Consent.ConsentProvisionType.PERMIT;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import de.ukbonn.mwtek.utilities.Compare;
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.fhir.interfaces.CaseIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.PatientIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbContactHealthFacilityProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbPatientProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FieldAlreadyInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.MandatoryFieldNotInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.OptionalFieldNotAvailableException;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
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
public class UkbConsent extends Consent
    implements UkbPatientProvider,
        PatientIdentifierValueProvider,
        UkbContactHealthFacilityProvider,
        CaseIdentifierValueProvider {

  public static final String URN_OID = "urn:oid:";
  protected UkbPatient patient;
  @Getter @Setter protected String patientId;
  protected UkbContactHealthFacility encounter;
  protected String caseId;

  public UkbConsent() {
    super();
  }

  public UkbConsent(
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

  public UkbConsent(
      UkbPatient patient,
      UkbContactHealthFacility encounter,
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

  public UkbConsent(
      UkbContactHealthFacility encounter,
      ConsentState status,
      CodeableConcept scope,
      List<CodeableConcept> category)
      throws IllegalArgumentException, MandatoryFieldNotInitializedException {
    // validate arguments
    ExceptionTools.checkNull("encounter", encounter);
    ExceptionTools.checkNull("encounter", encounter.getUkbPatient());
    ExceptionTools.checkNullOrEmpty(
        "encounter.identifier", encounter.getUkbPatient().getIdentifier());
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("scope", scope);
    ExceptionTools.checkNullOrEmpty("Category", category);

    // set local variables
    this.patient = encounter.getUkbPatient();
    this.patientId = encounter.getUkbPatient().getPatientId();
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

    return this.getUkbContactHealthFacility().getCaseIdentifierValue(system);
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
  public UkbPatient getUkbPatient() throws MandatoryFieldNotInitializedException {
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
  public UkbContactHealthFacility getUkbContactHealthFacility()
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
  public void initializeUkbPatient(UkbPatient patient)
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
  public void initializeUkbContactHealthFacility(UkbContactHealthFacility encounter)
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
  public boolean isUkbPatientInitialized() {
    return (this.patient != null);
  }

  @Override
  public boolean isUkbContactHealthFacilityInitialized() {
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
   * Checks if patient data usage is allowed based on consent form, policy, and specific provision
   * code.
   *
   * @return true if patient data usage is permitted; false otherwise.
   */
  public boolean isPatDataUsageAllowed() {
    return isMainConsentForm()
        && isPrivacyPolicyDocumentAndMiiConsentCategory()
        // Check level 1 and level 2 code;
        && (hasPermitWithCode(PATDAT_RETRIEVAL_SAVING_USING.getCode())
            || hasPermitWithCode(MDAT_COLLECTION.getCode()));
  }

  /**
   * Checks if recontacting the patient for further surveys is allowed.
   *
   * @return true if recontacting is permitted; false otherwise.
   */
  public boolean isRecontactingAllowed() {
    return isMainConsentForm()
        && isPrivacyPolicyDocumentAndMiiConsentCategory()
        // Check level 1 and level 2 code;
        && (hasPermitWithCode(RECONTACTING_ADDITIONS.getCode())
            || hasPermitWithCode(RECONTACTING_FURTHER_COLLECTION.getCode()));
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
   * Returns the policy URI of the first policy entry, stripped of the URN prefix.
   *
   * @return URI string without URN prefix.
   */
  private String getPolicyFirstRepUriWithoutPrefix() {
    return this.getPolicyFirstRep().getUri().replaceAll(URN_OID, "");
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

  private boolean hasPermitWithCode(String code) {
    return this.getProvision().getProvision().stream()
        .filter(pc -> pc.hasType() && pc.getType().equals(PERMIT))
        .anyMatch(
            pc -> pc.hasCode() && pc.getCodeFirstRep().hasCoding(PROVISION_CODE_SYSTEM, code));
  }
}
