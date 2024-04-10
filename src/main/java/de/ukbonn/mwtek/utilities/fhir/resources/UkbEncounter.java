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
import de.ukbonn.mwtek.utilities.fhir.interfaces.UkbPatientProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FhirTools;
import de.ukbonn.mwtek.utilities.fhir.misc.FieldAlreadyInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.MandatoryFieldNotInitializedException;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;

@ResourceDef(name = "Encounter")
public class UkbEncounter extends Encounter
    implements UkbPatientProvider, PatientIdentifierValueProvider, CaseIdentifierValueProvider {

  protected UkbPatient patient;
  protected String patientId;
  @Getter
  @Setter
  protected String facilityContactId;

  /**
   * @deprecated This constructor is only used for Fhir resource validation purpose. Use other
   * constructors for creating an instance of this resource.
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
   * @param patientId       the default system id of the patient
   * @param encounterStatus {@link EncounterStatus} e.g. {@link EncounterStatus#INPROGRESS}
   * @param encounterClass  The EncounterClass e.g. "pre-stationary"
   */
  public UkbEncounter(String patientId, Enumeration<EncounterStatus> encounterStatus,
      Coding encounterClass) {
    super(encounterStatus, encounterClass);

    // validate arguments
    ExceptionTools.checkNullOrEmpty("patientId", patientId);

    // set local variables
    this.patientId = patientId;
  }

  @Deprecated
  public UkbEncounter(UkbPatient patient, Enumeration<EncounterStatus> status,
      Coding encounterClass) throws IllegalArgumentException {
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
    this.setSubject(new Reference().setIdentifier(
        FhirTools.getIdentifierBySystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT,
            patient.getIdentifier())));

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
    this.setSubject(new Reference().setIdentifier(
        FhirTools.getIdentifierBySystem(StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT,
            patient.getIdentifier())));

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

  public void setPatientId(String patientId) {
    this.patientId = patientId;
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

  /**
   * Auxiliary function to return the textual value of the visit number.
   */
  public String getVisitNumberIdentifierValue() {
    if (this.hasIdentifier()) {
      Identifier visitNumberIdentifier = FhirTools.getVisitNumberIdentifier(this.getIdentifier(),
          false);
      if (visitNumberIdentifier != null && visitNumberIdentifier.hasValue()) {
        return visitNumberIdentifier.getValue();
      }
    }
    return null;
  }

  public boolean isIcuCase(Collection<String> icuLocationIds) {
    // Look for matches in the location attribute if there is at least one icu location.
    return this.getLocation().stream()
        .anyMatch(x -> icuLocationIds.contains(x.getLocation().getIdBase()));
  }

  public boolean isCurrentlyOnIcuWard(Collection<String> icuLocationIds) {
    // Look for active icu locations
    return this.getLocation().stream().filter(x -> x.hasPeriod() && !x.getPeriod().hasEnd())
        .anyMatch(x -> icuLocationIds.contains(x.getLocation().getIdBase()));
  }

  /**
   * Encounter.period.start is a mandatory field in the kds profile.
   *
   * @return True if {@link UkbEncounter#getPeriod() Encounter.period.start} is not null
   */
  public boolean isPeriodStartExistent() {
    return this.getPeriod() != null && this.getPeriod().getStart() != null;
  }

}
