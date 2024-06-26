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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import de.ukbonn.mwtek.utilities.Compare;
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.fhir.interfaces.CaseIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.PatientIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbContactHealthFacilityProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbPatientProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FhirTools;
import de.ukbonn.mwtek.utilities.fhir.misc.FieldAlreadyInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.MandatoryFieldNotInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.OptionalFieldNotAvailableException;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Reference;

@Slf4j
@ResourceDef(name = "Condition")
public class UkbCondition
  extends Condition
  implements
    UkbPatientProvider, PatientIdentifierValueProvider, UkbContactHealthFacilityProvider, CaseIdentifierValueProvider {

  protected UkbPatient patient;
  protected UkbContactHealthFacility encounter;
  protected String patientId;
  protected String caseId;

  /**
   * @deprecated This constructor is only used for Fhir resource validation purpose. Use other
   * constructors for creating an instance of this resource.
   */
  @Deprecated
  public UkbCondition() {
    super();
  }

  /**
   * Creates a new condition object without defined {@link UkbPatient} and
   * {@link UkbContactHealthFacility} objects, these objects may be assigned later using
   * {@link #initializeUkbPatient(UkbPatient)} or
   * {@link #initializeUkbContactHealthFacility(UkbContactHealthFacility)}. The patient is
   * mandatory, therefore the
   * <code>patientId</code> must be specified, the case is optional.
   *
   * @param patientId      the default system id of the patient
   * @param caseId         the default system id of the case (effectively "encounter", may be
   *                       <code>null</code>)
   * @param clinicalStatus
   * @param code
   * @param recordedDate
   */
  public UkbCondition(
    String patientId,
    String caseId,
    CodeableConcept clinicalStatus,
    CodeableConcept code,
    Date recordedDate
  ) {
    // validate arguments
    if (patientId == null) {
      log.warn("pid is null -> the patient might be canceled");
    }
    ExceptionTools.checkNullOrEmpty("patientId", patientId);
    ExceptionTools.checkNull("clinicalStatus", clinicalStatus);
    ExceptionTools.checkNull("code", code);
    ExceptionTools.checkNull("recordedDate", recordedDate);

    // set local variables
    this.patientId = patientId;
    this.caseId = caseId;

    // set fhir content
    this.setClinicalStatus(clinicalStatus);
    this.setCode(code);
    this.setRecordedDate(recordedDate);
  }

  public UkbCondition(
    UkbPatient patient,
    UkbContactHealthFacility encounter,
    CodeableConcept clinicalStatus,
    CodeableConcept code,
    Date recordedDate
  ) {
    // validate arguments
    ExceptionTools.checkNull("patient", patient);
    ExceptionTools.checkNullOrEmpty("patient.identifier", patient.getIdentifier());
    ExceptionTools.checkNull("clinicalStatus", clinicalStatus);
    ExceptionTools.checkNull("code", code);
    ExceptionTools.checkNull("recordedDate", recordedDate);

    // set local variables
    this.patient = patient;
    this.patientId = patient.getPatientId();
    this.encounter = encounter;
    this.caseId = (encounter != null) ? encounter.getCaseId() : null;

    // set fhir content
    this.setSubject(
        new Reference()
          .setIdentifier(
            FhirTools.getIdentifierBySystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT, patient.getIdentifier())
          )
      );
    this.setClinicalStatus(clinicalStatus);
    this.setCode(code);
    this.setRecordedDate(recordedDate);
  }

  public UkbCondition(
    UkbContactHealthFacility encounter,
    CodeableConcept clinicalStatus,
    CodeableConcept code,
    Date recordedDate
  ) throws MandatoryFieldNotInitializedException {
    // validate arguments
    ExceptionTools.checkNull("encounter", encounter);
    ExceptionTools.checkNull("encounter.patient", encounter.getUkbPatient());
    ExceptionTools.checkNullOrEmpty("encounter.patient.identifier", encounter.getUkbPatient().getIdentifier());
    ExceptionTools.checkNull("clinicalStatus", clinicalStatus);
    ExceptionTools.checkNull("code", code);
    ExceptionTools.checkNull("recordedDate", recordedDate);

    // set local variables
    this.patient = encounter.getUkbPatient();
    this.patientId = encounter.getUkbPatient().getPatientId();
    this.encounter = encounter;
    this.caseId = encounter.getCaseId();

    // set fhir content
    this.setSubject(
        new Reference()
          .setIdentifier(
            FhirTools.getIdentifierBySystem(
              StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT,
              encounter.getUkbPatient().getIdentifier()
            )
          )
      );
    this.setClinicalStatus(clinicalStatus);
    this.setCode(code);
    this.setRecordedDate(recordedDate);
  }

  @Override
  public String getCaseId() {
    return this.caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
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
  public String getPatientId() {
    return this.patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  @Override
  public String getPatientIdentifierValue(String system) throws MandatoryFieldNotInitializedException {
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

    // assign the patient to the fhir object
    this.setSubject(
        new Reference()
          .setIdentifier(
            FhirTools.getIdentifierBySystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT, patient.getIdentifier())
          )
      );
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
}
