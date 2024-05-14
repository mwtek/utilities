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
import de.ukbonn.mwtek.utilities.ExceptionTools;
import de.ukbonn.mwtek.utilities.fhir.interfaces.PatientIdentifierValueProvider;
import de.ukbonn.mwtek.utilities.fhir.misc.FhirTools;
import de.ukbonn.mwtek.utilities.fhir.misc.StaticValueProvider;
import java.util.Date;
import java.util.List;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

@ResourceDef(name = "Patient")
public class UkbPatient extends Patient implements PatientIdentifierValueProvider {

  @Deprecated
  public UkbPatient() {
    super();
  }

  public UkbPatient(
    List<Identifier> identifier,
    List<HumanName> name,
    AdministrativeGender gender,
    Date birthDate,
    List<Address> address
  ) {
    ExceptionTools.checkNullOrEmpty("identifier", identifier);
    ExceptionTools.checkNullOrEmpty("name", name);
    ExceptionTools.checkNull("gender", gender);
    ExceptionTools.checkNull("birthdate", birthDate);
    ExceptionTools.checkNullOrEmpty("address", address);

    this.setIdentifier(identifier);
    this.setName(name);
    this.setGender(gender);
    this.setBirthDate(birthDate);
    this.setAddress(address);
  }

  public UkbPatient(
    List<Identifier> identifier,
    List<HumanName> name,
    AdministrativeGender gender,
    List<Address> address
  ) {
    ExceptionTools.checkNullOrEmpty("identifier", identifier);
    ExceptionTools.checkNullOrEmpty("name", name);
    ExceptionTools.checkNull("gender", gender);
    ExceptionTools.checkNullOrEmpty("address", address);

    this.setIdentifier(identifier);
    this.setName(name);
    this.setGender(gender);
    this.setAddress(address);
  }

  @Override
  public String getPatientIdentifierValue(String system) {
    if (system == null) {
      system = StaticValueProvider.SYSTEM_WITH_IDENTIFIER_PATIENT;
    }
    Identifier identifier = FhirTools.getIdentifierBySystem(system, this.getIdentifier());
    if (identifier != null) {
      return identifier.getValue();
    }
    return null;
  }
}
