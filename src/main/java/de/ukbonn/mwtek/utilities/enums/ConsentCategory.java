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

package de.ukbonn.mwtek.utilities.enums;

import static de.ukbonn.mwtek.utilities.enums.ConsentFixedValues.CONSENT_CATEGORY_CODES_URL;
import static de.ukbonn.mwtek.utilities.enums.ConsentFixedValues.V3_ACT_CODE_URL;
import static de.ukbonn.mwtek.utilities.enums.TerminologySystems.LOINC;

import lombok.Getter;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

// uses: http://hl7.org/fhir/R4/valueset-consent-category
@Getter
public enum ConsentCategory {
  ACD("acd", CONSENT_CATEGORY_CODES_URL, "Advance Directive"),
  DNR("dnr", CONSENT_CATEGORY_CODES_URL, "Do Not Resuscitate"),
  EMRGONLY("emrgonly", CONSENT_CATEGORY_CODES_URL, "Emergency Only"),
  HCD("hcd", CONSENT_CATEGORY_CODES_URL, "Health Care Directive"),
  NPP("npp", CONSENT_CATEGORY_CODES_URL, "Notice of Privacy Practices"),
  POLST("polst", CONSENT_CATEGORY_CODES_URL, "POLST"),
  RESEARCH("research", CONSENT_CATEGORY_CODES_URL, "Research Information Access"),
  RSDID("rsdid", CONSENT_CATEGORY_CODES_URL, "De-identified Information Access"),
  RSREID("rsreid", CONSENT_CATEGORY_CODES_URL, "Re-identifiable Information Access"),
  ICOL("ICOL", V3_ACT_CODE_URL, "information collection"),
  IDSCL("IDSCL", V3_ACT_CODE_URL, "information disclosure"),
  INFA("INFA", V3_ACT_CODE_URL, "information access"),
  INFAO("INFAO", V3_ACT_CODE_URL, "access only"),
  INFASO("INFASO", V3_ACT_CODE_URL, "access and save only"),
  IRDSCL("IRDSCL", V3_ACT_CODE_URL, "information redisclosure"),
  RESEARCH2("RESEARCH", V3_ACT_CODE_URL, "research information access"),
  RSDID2("RSDID", V3_ACT_CODE_URL, "de-identified information access"),
  RSREID2("RSREID", V3_ACT_CODE_URL, "re-identifiable information access"),
  C592840("59284-0", LOINC, "Patient Consent"),
  C570168("57016-8", LOINC, "Privacy policy acknowledgement Document"),
  C570176("57017-6", LOINC, "Privacy policy Organization Document"),
  C642926("64292-6", LOINC, "Release of information consent");

  private final String code;
  private final String system;
  private final String display;

  ConsentCategory(String code, String system, String display) {
    this.code = code;
    this.system = system;
    this.display = display;
  }

  public CodeableConcept getCategory() {
    return new CodeableConcept()
        .addCoding(new Coding().setCode(code).setSystem(system).setDisplay(display));
  }
}
