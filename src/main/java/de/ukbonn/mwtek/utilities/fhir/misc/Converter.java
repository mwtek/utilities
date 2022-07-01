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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hl7.fhir.r4.model.*;

import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbCondition;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbKontaktGesundheitseinrichtung;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbLocation;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbObservation;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbPatient;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbProcedure;

import static de.ukbonn.mwtek.utilities.fhir.misc.FhirTools.getReference;

public class Converter {

  // Test
  public static void main(String[] args) {
    Patient p = new Patient();
    Encounter e = new Encounter();
    Observation o = new Observation();
    Procedure pr = new Procedure();
    Condition c = new Condition();
    Location l = new Location();

    DomainResource np = convert(p);
    DomainResource ne = convert(e);
    DomainResource no = convert(o);
    DomainResource npr = convert(pr);
    DomainResource nc = convert(c);
    DomainResource nl = convert(l);

    List<DomainResource> list = Arrays.asList(p, e, o, pr, c, l);
    convert(list);
  }

  // can't be instantiated
  private Converter() {
  }

  // list implementation
  public static List<? extends DomainResource> convert(List<? extends DomainResource> res,
          boolean check) {
    List<DomainResource> resources = new ArrayList<>();
    res.forEach((temp) -> {
      resources.add(convert(temp, check));
    });
    return resources;
  }

  public static List<? extends DomainResource> convert(List<? extends DomainResource> res) {
    return convert(res, false);
  }

  // single conversion
  public static DomainResource convert(DomainResource res, boolean check) {
    switch (res.fhirType()) {
      case "Encounter":
        return convertEncounter((Encounter) res, check);
      case "Patient":
        return convertPatient((Patient) res, check);
      case "Observation":
        return convertObservation((Observation) res, check);
      case "Procedure":
        return convertProcedure((Procedure) res, check);
      case "Condition":
        return convertCondition((Condition) res, check);
      case "Location":
        return convertLocation((Location) res, check);
    }
    return res;
  }

  public static DomainResource convert(DomainResource res) {
    return convert(res, false);
  }

  private static UkbKontaktGesundheitseinrichtung convertEncounter(Encounter e, boolean check) {
    UkbKontaktGesundheitseinrichtung res = new UkbKontaktGesundheitseinrichtung();

    if (check) {
      ExceptionTools.checkNull("Period", e.getStatusHistory().get(0).getPeriod());
    }

    res.setIdentifier(e.getIdentifier());
    res.setStatus(e.getStatus());
    res.setStatusHistory(e.getStatusHistory());
    res.setClass_(e.getClass_());
    res.setClassHistory(e.getClassHistory());
    res.setType(e.getType());
    res.setServiceType(e.getServiceType());
    res.setPriority(e.getPriority());
    res.setSubject(e.getSubject());
    res.setEpisodeOfCare(e.getEpisodeOfCare());
    res.setBasedOn(e.getBasedOn());
    res.setParticipant(e.getParticipant());
    res.setAppointment(e.getAppointment());
    res.setPeriod(e.getPeriod());
    res.setLength(e.getLength());
    res.setReasonCode(e.getReasonCode());
    res.setReasonReference(e.getReasonReference());
    res.setDiagnosis(e.getDiagnosis());
    res.setAccount(e.getAccount());
    res.setHospitalization(e.getHospitalization());
    res.setServiceProvider(e.getServiceProvider());
    res.setPartOf(e.getPartOf());

    // Extra
    res.setMeta(e.getMeta());
    res.setId(e.getIdElement().getIdPart());

    // Eigene Attribute
    // CHECK
    res.setPatientId(getReferenceId(e.getSubject()));


    // store the ID of each location WITHOUT the resource type
    e.getLocation().forEach(loc -> {
      loc.getLocation().setIdElement(new StringType(split(loc.getLocation().getReference())));
    });
    res.setLocation(e.getLocation());

    return res;
  }

  private static UkbPatient convertPatient(Patient p, boolean check) {
    UkbPatient res = new UkbPatient();

    if (check) {
      ExceptionTools.checkNullOrEmpty("identifier", p.getIdentifier());
      ExceptionTools.checkNullOrEmpty("name", p.getName());
      ExceptionTools.checkNull("gender", p.getGender());
      ExceptionTools.checkNull("birthdate", p.getBirthDate());
      ExceptionTools.checkNull("address", p.getAddress());
    }

    res.setIdentifier(p.getIdentifier());
    res.setActive(p.getActive());
    res.setName(p.getName());
    res.setTelecom(p.getTelecom());
    res.setGender(p.getGender());
    res.setBirthDate(p.getBirthDate());
    res.setDeceased(p.getDeceased());
    res.setAddress(p.getAddress());
    res.setMaritalStatus(p.getMaritalStatus());
    res.setMultipleBirth(p.getMultipleBirth());
    res.setPhoto(p.getPhoto());
    res.setContact(p.getContact());
    res.setCommunication(p.getCommunication());
    res.setGeneralPractitioner(p.getGeneralPractitioner());
    res.setManagingOrganization(p.getManagingOrganization());

    // Extra
    res.setMeta(p.getMeta());
    res.setId(p.getIdElement().getIdPart());

    return res;
  }

  private static UkbObservation convertObservation(Observation o, boolean check) {
    UkbObservation res = new UkbObservation();

    if (check) {
      // CHECK Patient = Subject
      ExceptionTools.checkNull("patient", o.getSubject().getReference());
      ExceptionTools.checkNullOrEmpty("patient.Identifier",
              o.getSubject().getIdentifier().toString());
    }

    res.setIdentifier(o.getIdentifier());
    res.setBasedOn(o.getBasedOn());
    res.setPartOf(o.getPartOf());
    res.setStatus(o.getStatus());
    res.setCategory(o.getCategory());
    res.setCode(o.getCode());
    res.setSubject(o.getSubject());
    res.setFocus(o.getFocus());
    res.setEncounter(o.getEncounter());
    res.setEffective(o.getEffective());
    res.setIssued(o.getIssued());
    res.setPerformer(o.getPerformer());
    res.setValue(o.getValue());
    res.setDataAbsentReason(o.getDataAbsentReason());
    res.setInterpretation(o.getInterpretation());
    res.setNote(o.getNote());
    res.setBodySite(o.getBodySite());
    res.setMethod(o.getMethod());
    res.setSpecimen(o.getSpecimen());
    res.setDevice(o.getDevice());
    res.setReferenceRange(o.getReferenceRange());
    res.setHasMember(o.getHasMember());
    res.setDerivedFrom(o.getDerivedFrom());
    res.setComponent(o.getComponent());

    // Extra
    res.setMeta(o.getMeta());
    res.setId(o.getIdElement().getIdPart());

    res.setPatientId(getReferenceId(o.getSubject()));
    res.setCaseId(getReferenceId(o.getEncounter()));
    return res;
  }

  private static UkbProcedure convertProcedure(Procedure p, boolean check) {
    UkbProcedure res = new UkbProcedure();

    if (check) {
      // CHECK Ref = ID
      ExceptionTools.checkNullOrEmpty("patientId", p.getSubject().getReference());
      ExceptionTools.checkNull("status", p.getStatus());
      ExceptionTools.checkNull("code", p.getCode());
      ExceptionTools.checkNull("performed", p.getPerformed());
    }

    res.setIdentifier(p.getIdentifier());
    res.setInstantiatesCanonical(p.getInstantiatesCanonical());
    res.setInstantiatesUri(p.getInstantiatesUri());
    res.setBasedOn(p.getBasedOn());
    res.setPartOf(p.getPartOf());
    res.setStatus(p.getStatus());
    res.setStatusReason(p.getStatusReason());
    res.setCategory(p.getCategory());
    res.setCode(p.getCode());
    res.setSubject(p.getSubject());
    res.setEncounter(p.getEncounter());
    res.setPerformed(p.getPerformed());
    res.setRecorder(p.getRecorder());
    res.setAsserter(p.getAsserter());
    res.setPerformer(p.getPerformer());
    res.setLocation(p.getLocation());
    res.setReasonCode(p.getReasonCode());
    res.setReasonReference(p.getReasonReference());
    res.setBodySite(p.getBodySite());
    res.setOutcome(p.getOutcome());
    res.setReport(p.getReport());
    res.setComplication(p.getComplication());
    res.setComplicationDetail(p.getComplicationDetail());
    res.setFollowUp(p.getFollowUp());
    res.setNote(p.getNote());
    res.setFocalDevice(p.getFocalDevice());
    res.setUsedReference(p.getUsedReference());
    res.setUsedCode(p.getUsedCode());

    // Extra
    res.setMeta(p.getMeta());
    res.setId(p.getIdElement().getIdPart());

    res.setPatientId(getReferenceId(p.getSubject()));
    res.setCaseId(getReferenceId(p.getEncounter()));

    return res;
  }

  private static UkbCondition convertCondition(Condition c, boolean check) {
    UkbCondition res = new UkbCondition();

    if (check) {
      // CHECK Ref = ID
      ExceptionTools.checkNullOrEmpty("patientId", c.getSubject().getReference());
      ExceptionTools.checkNull("clinicalStatus", c.getClinicalStatus());
      ExceptionTools.checkNull("code", c.getCode());
      ExceptionTools.checkNull("recordedDate", c.getRecordedDate());
    }

    res.setIdentifier(c.getIdentifier());
    res.setClinicalStatus(c.getClinicalStatus());
    res.setVerificationStatus(c.getVerificationStatus());
    res.setCategory(c.getCategory());
    res.setSeverity(c.getSeverity());
    res.setCode(c.getCode());
    res.setBodySite(c.getBodySite());
    res.setSubject(c.getSubject());
    res.setEncounter(c.getEncounter());
    res.setOnset(c.getOnset());
    res.setAbatement(c.getAbatement());
    res.setRecordedDate(c.getRecordedDate());
    res.setRecorder(c.getRecorder());
    res.setAsserter(c.getAsserter());
    res.setStage(c.getStage());
    res.setEvidence(c.getEvidence());
    res.setNote(c.getNote());

    // Extra
    res.setMeta(c.getMeta());
    res.setId(c.getIdElement().getIdPart());

    res.setPatientId(getReferenceId(c.getSubject()));

    res.setCaseId(getReferenceId(c.getEncounter()));

    return res;
  }

  private static UkbLocation convertLocation(Location l, boolean check) {
    UkbLocation res = new UkbLocation();

    if (check) {
      ExceptionTools.checkNullOrEmpty("Identifier", l.getIdentifier());
      ExceptionTools.checkNull("PhysicalType", l.getPhysicalType());
    }

    res.setIdentifier(l.getIdentifier());
    res.setStatus(l.getStatus());
    res.setOperationalStatus(l.getOperationalStatus());
    res.setName(l.getName());
    res.setAlias(l.getAlias());
    res.setDescription(l.getDescription());
    res.setMode(l.getMode());
    res.setType(l.getType());
    res.setTelecom(l.getTelecom());
    res.setAddress(l.getAddress());
    res.setPhysicalType(l.getPhysicalType());
    res.setPosition(l.getPosition());
    res.setManagingOrganization(l.getManagingOrganization());
    res.setPartOf(l.getPartOf());
    res.setHoursOfOperation(l.getHoursOfOperation());
    res.setAvailabilityExceptions(l.getAvailabilityExceptions());
    res.setEndpoint(l.getEndpoint());

    // Extra
    res.setMeta(l.getMeta());
    res.setId(l.getIdElement().getIdPart());

    return res;
  }

  private static String split(String s) {
    String[] parts = s.split("/");
    return parts[parts.length - 1];
  }

  /**
   * Determination of the ID of a FHIR reference, i.e. the consequence of the removal of the resource type.
   * @param reference FHIR reference as "Encounter/123"
   * @return The plain id of the reference as "123"
   */
  private static String getReferenceId(Reference reference) {
    if (reference.getReference() != null) {
     return split(reference.getReference());
    } else
      return reference.getIdentifier().getValue();
  }
}
