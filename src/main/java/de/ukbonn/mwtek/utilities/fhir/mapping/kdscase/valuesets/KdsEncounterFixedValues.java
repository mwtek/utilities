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
package de.ukbonn.mwtek.utilities.fhir.mapping.kdscase.valuesets;

import com.google.common.collect.ImmutableList;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbEncounter;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

public class KdsEncounterFixedValues {

  public static final String IDENTIFIER_VN_TYPE_SYSTEM =
      "http://terminology.hl7" + ".org/CodeSystem/v2-0203";
  public static final String IDENTIFIER_VN_TYPE_CODE = "VN";
  public static final CodeableConcept IDENTIFIER_TYPE_VISIT_NUMBER_CC =
      new CodeableConcept()
          .addCoding(new Coding(IDENTIFIER_VN_TYPE_SYSTEM, IDENTIFIER_VN_TYPE_CODE, null));

  // Encounter.case.type.kontaktart (new fhir profile ->
  // https://simplifier.net/packages/de.basisprofil.r4/1.0.0/files/397801)
  public static final String CASETYPE_PRESTATIONARY = "vorstationaer";
  public static final String CASETYPE_POSTSTATIONARY = "nachstationaer";
  public static final String CASETYPE_PARTSTATIONARY = "teilstationaer";
  public static final String CASETYPE_NORMALSTATIONARY = "normalstationaer";
  public static final String CASETYPE_INTENSIVESTATIONARY = "intensivstationaer";
  public static final String CASETYPE_CONTACT_ART_SYSTEM =
      "http://fhir.de/CodeSystem/kontaktart-de";

  /**
   * List of valid {@link UkbEncounter#getClass_() encounter.class.code} codes for inpatient
   * encounters. Both the value set from the case module version 1.0 and 2.0 are checked. <a
   * href="https://simplifier.net/packages/de.basisprofil.r4/1.0.0/files/397957">The value set of
   * version 2.0 can be found here.</a>.
   */
  public static final ImmutableList<String> ENCOUNTER_CLASS_INPATIENT_CODES =
      ImmutableList.of("IMP", "stationaer");

  /**
   * List of valid {@link UkbEncounter#getClass_() encounter.class.code} codes for outpatient
   * encounters. Both the value set from the case module version 1.0 and 2.0 are checked. <a
   * href="https://simplifier.net/packages/de.basisprofil.r4/1.0.0/files/397957">The value set of
   * version 2.0 can be found here.</a>.
   */
  public static final ImmutableList<String> ENCOUNTER_CLASS_OUTPATIENT_CODES =
      ImmutableList.of("AMB", "ambulant");

  // dischargeCoding [FHIR Value Set]
  public static final String DISCHARGE_DISPOSITION_EXT_URL =
      "http://fhir.de/StructureDefinition" + "/Entlassungsgrund";
  public static final String DISCHARGE_DISPOSITION_FIRST_AND_SECOND_POS_EXT_URL =
      "ErsteUndZweiteStelle";
  public static final String DISCHARGE_DISPOSITION_FIRST_AND_SECOND_POS_SYSTEM =
      "http://fhir.de/CodeSystem/dkgev/EntlassungsgrundErsteUndZweiteStelle";
  public static final String DEATH_CODE = "07";
  public static final String DEATH_CODE_DISPLAY = "Tod";
}
