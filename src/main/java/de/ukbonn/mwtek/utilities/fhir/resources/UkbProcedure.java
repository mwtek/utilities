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

import static de.ukbonn.mwtek.utilities.enums.TerminologySystems.SNOMED;
import static de.ukbonn.mwtek.utilities.fhir.misc.FhirCodingTools.getCodeBySystem;
import static de.ukbonn.mwtek.utilities.fhir.misc.FhirCodingTools.getCodeOfFirstCoding;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import de.ukbonn.mwtek.utilities.Compare;
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.enums.TerminologySystems;
import de.ukbonn.mwtek.utilities.fhir.interfaces.CaseIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.PatientIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbPatientProvider;
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbVersorgungsfallProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FhirTools;
import de.ukbonn.mwtek.utilities.fhir.misc.FieldAlreadyInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.MandatoryFieldNotInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.OptionalFieldNotAvailableException;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
import java.util.Collection;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Type;

@ResourceDef(name = "Procedure")
public class UkbProcedure
  extends Procedure
  implements
    UkbPatientProvider, PatientIdentifierValueProvider, UkbVersorgungsfallProvider, CaseIdentifierValueProvider {

  protected UkbPatient patient;
  protected UkbVersorgungsfall versorgungsfall;
  protected String patientId;
  protected String caseId;

  @Deprecated
  public UkbProcedure() {
    super();
  }

  public UkbProcedure(String patientId, String caseId, ProcedureStatus status, CodeableConcept code, Type performed) {
    super();
    // validate arguments
    ExceptionTools.checkNullOrEmpty("patientId", patientId);
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("code", code);
    ExceptionTools.checkNull("performed", performed);

    // set local variables
    this.patientId = patientId;
    this.caseId = caseId;

    // set fhir content
    this.setSubject(
        new Reference()
          .setType("Patient")
          .setIdentifier(
            new Identifier().setSystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT).setValue(patientId)
          )
      );

    this.setEncounter(
        new Reference()
          .setType("Encounter")
          .setIdentifier(
            new Identifier().setSystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_ENCOUNTER).setValue(caseId)
          )
      );
    this.setStatus(status);
    this.setCode(code);
    this.setPerformed(performed);
  }

  public UkbProcedure(
    UkbPatient patient,
    UkbVersorgungsfall versorgungsfall,
    ProcedureStatus status,
    CodeableConcept code,
    Type performed
  ) throws IllegalArgumentException {
    super();
    // validate arguments
    ExceptionTools.checkNull("patient", patient);
    ExceptionTools.checkNullOrEmpty("patient.identifier", patient.getIdentifier());
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("code", code);
    ExceptionTools.checkNull("performed", performed);

    // set local variables
    this.patient = patient;
    this.patientId = patient.getPatientId();
    this.versorgungsfall = versorgungsfall;
    this.caseId = (versorgungsfall != null) ? versorgungsfall.getCaseId() : null;

    // set fhir content
    this.setSubject(
        new Reference()
          .setIdentifier(
            FhirTools.getIdentifierBySystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT, patient.getIdentifier())
          )
      );
    this.setStatus(status);
    this.setCode(code);
    this.setPerformed(performed);
  }

  public UkbProcedure(UkbVersorgungsfall versorgungsfall, ProcedureStatus status, CodeableConcept code, Type performed)
    throws IllegalArgumentException, MandatoryFieldNotInitializedException {
    super();
    // validate arguments
    ExceptionTools.checkNull("versorgungsfall", versorgungsfall);
    ExceptionTools.checkNull("versorgungsfall.patient", versorgungsfall.getUkbPatient());
    ExceptionTools.checkNullOrEmpty(
      "versorgungsfall.patient.identifier",
      versorgungsfall.getUkbPatient().getIdentifier()
    );
    ExceptionTools.checkNull("status", status);
    ExceptionTools.checkNull("code", code);
    ExceptionTools.checkNull("performed", performed);

    // set local variables
    this.patient = versorgungsfall.getUkbPatient();
    this.patientId = versorgungsfall.getUkbPatient().getPatientId();
    this.versorgungsfall = versorgungsfall;
    this.caseId = versorgungsfall.getCaseId();

    // set fhir content
    this.setSubject(
        new Reference()
          .setIdentifier(
            FhirTools.getIdentifierBySystem(
              StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT,
              versorgungsfall.getUkbPatient().getIdentifier()
            )
          )
      );
    this.setStatus(status);
    this.setCode(code);
    this.setPerformed(performed);
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
    }

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
  public boolean isUkbPatientInitialized() {
    return (this.patient != null);
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
    }

    return this.getUkbVersorgungsfall().getCaseIdentifierValue(system);
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
    }

    return this.getUkbPatient().getPatientIdentifierValue(system);
  }

  @Override
  public UkbVersorgungsfall getUkbVersorgungsfall()
    throws MandatoryFieldNotInitializedException, OptionalFieldNotAvailableException {
    // the case is optional
    if (this.versorgungsfall == null) {
      if (this.caseId == null) {
        throw new OptionalFieldNotAvailableException();
      }
      throw new MandatoryFieldNotInitializedException();
    }
    return this.versorgungsfall;
  }

  @Override
  public void initializeVersorgungsfall(UkbVersorgungsfall versorgungsfall)
    throws IllegalArgumentException, FieldAlreadyInitializedException {
    // validate arguments
    ExceptionTools.checkNull("versorgungsfall", versorgungsfall);

    // must not be initialized more than once!
    if (this.versorgungsfall != null) {
      throw new FieldAlreadyInitializedException();
    }

    this.versorgungsfall = versorgungsfall;
    this.caseId = versorgungsfall.getCaseId();
  }

  @Override
  public boolean isUkbVersorgungsfallInitialized() {
    return (this.versorgungsfall != null);
  }

  public boolean isCodeExistingInFirstCoding(Collection<String> codes) {
    if (this.hasCode() && this.getCode().hasCoding()) {
      // The procedure retrieval is not fixed to a system at the moment since some
      // providers are using ops instead of snomed.
      String icuCode = getCodeOfFirstCoding(this.getCode().getCoding());
      // Return true if the found icu code is part of the input list
      return codes.contains(icuCode);
    }
    return false;
  }

  /**
   * Looking if code is existing in the given terminology system. If the terminology system is
   * unset, the snomed code is taken if existence. Otherwise, the first coding found will be used.
   *
   * @param codes                         The snomed/ops codes to test against.
   * @param preferredTerminologySystemUrl By default, the value of {@link TerminologySystems#SNOMED}
   *                                      is used.
   * @return If the code in the procedure resource was found in the given collection return
   * <code>true</code>, otherwise return <code>false</code>.
   */
  public boolean isCodeExistingInValueSet(
    Collection<String> codes,
    String preferredTerminologySystemUrl,
    boolean forceSystemCheck
  ) {
    if (preferredTerminologySystemUrl == null && !forceSystemCheck) {
      preferredTerminologySystemUrl = SNOMED;
    }

    if (preferredTerminologySystemUrl != null && this.hasCode() && this.getCode().hasCoding()) {
      // The procedure retrieval is not fixed to a system at the moment since some
      // providers are using ops instead of snomed.
      String icuCode = getCodeBySystem(this.getCode().getCoding(), preferredTerminologySystemUrl);
      // Return true if the found icu code is part of the input list
      if (codes.contains(icuCode)) {
        return true;
      } else {
        // Otherwise, try the first coding found
        return isCodeExistingInFirstCoding(codes);
      }
    }
    return false;
  }

  public boolean isInProgress() {
    return this.hasStatus() && this.getStatus() == ProcedureStatus.INPROGRESS;
  }
}
