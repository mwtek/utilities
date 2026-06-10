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

import static de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT;

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
import lombok.Setter;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.QuestionnaireResponse;

@ResourceDef(name = "QuestionnaireResponse")
public class MiiQuestionnaireResponse extends QuestionnaireResponse
    implements MiiPatientProvider,
        PatientIdentifierValueProvider,
        MiiContactHealthFacilityProvider,
        CaseIdentifierValueProvider {

  protected MiiPatient patient;
  protected MiiContactHealthFacility encounter;
  @Setter protected String patientId;
  @Setter protected String caseId;

  /**
   * @deprecated This constructor is only used for Fhir resource validation purpose. Use other
   *     constructors for creating an instance of this resource.
   */
  @Deprecated
  public MiiQuestionnaireResponse() {
    super();
  }

  public MiiQuestionnaireResponse(
      String patientId,
      String caseId,
      Enumeration<QuestionnaireResponseStatus> status,
      CodeableConcept code)
      throws IllegalArgumentException {
    super(status);
    // validate arguments
    ExceptionTools.checkNullOrEmpty("patientId", patientId);

    // set local variables
    this.patientId = patientId;
    this.caseId = caseId;
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
  public String getPatientId() {
    return this.patientId;
  }

  @Override
  public String getPatientIdentifierValue(String system)
      throws MandatoryFieldNotInitializedException {
    if (Compare.isEqual(system, SYSTEM_WITH_IDENTIFIER_PATIENT)) {
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
    ExceptionTools.checkNullOrEmpty("patient.Identifier", patient.getIdentifier());

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
}
