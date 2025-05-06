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

import java.util.List;

public class ConsentFixedValues {

  // Consent
  public static final String META_PROFILE =
      "https://www.medizininformatik-initiative.de/fhir/modul-consent/StructureDefinition/mii-pr-consent-einwilligung";
  public static final String CONSENT_CATEGORY_SYSTEM =
      "https://www.medizininformatik-initiative.de/fhir/modul-consent/CodeSystem/mii-cs-consent-consent_category";
  public static final String CONSENT_CATEGORY_CODE = "2.16.840.1.113883.3.1937.777.24.2.184";

  // POLICY_RULE("https://www.medizininformatik-initiative.de/sites/default/files/2020-04/MII_AG-Consent_Einheitlicher-Mustertext_v1.6d.pdf"),
  public static final String CONSENT_CATEGORY_CODES_URL =
      "http://terminology.hl7.org/CodeSystem/consentcategorycodes";
  public static final String V3_ACT_CODE_URL = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
  // PROVISION_DENY("deny"),
  // PROVISION_PERMIT("permit");

  // Versions for the Consent.policy.uri -> see
  // https://simplifier.net/guide/mii-ig-modul-consent-2025/MII-IG-Modul-Consent/TechnischeImplementierung/FHIRProfile/Consent.guide.md?version=current
  public static final String VERSION_OID_1_6_D = "2.16.840.1.113883.3.1937.777.24.2.1790";
  public static final String VERSION_OID_1_6_D_REVOKED_COMPLETE =
      "2.16.840.1.113883.3.1937.777.24.2.2718";
  public static final String VERSION_OID_1_6_D_REVOKED_PARTLY =
      "2.16.840.1.113883.3.1937.777.24.2.2719";

  public static final String VERSION_OID_1_6_F = "2.16.840.1.113883.3.1937.777.24.2.1791";
  public static final String VERSION_OID_1_6_F_REVOKED_COMPLETE =
      "2.16.840.1.113883.3.1937.777.24.2.2720";
  public static final String VERSION_OID_1_6_F_REVOKED_PARTLY =
      "2.16.840.1.113883.3.1937.777.24.2.2721";

  public static final String VERSION_OID_1_7_2 = "2.16.840.1.113883.3.1937.777.24.2.2079";
  public static final String VERSION_OID_1_7_2_REVOKED_COMPLETE =
      "2.16.840.1.113883.3.1937.777.24.2.2722";
  public static final String VERSION_OID_1_7_2_REVOKED_PARTLY =
      "2.16.840.1.113883.3.1937.777.24.2.2723";
  public static final String VERSION_OID_1_7_2_PARENTS = "2.16.840.1.113883.3.1937.777.24.2.3542";
  public static final String VERSION_OID_1_7_2_UNDERAGE_7_11 =
      "2.16.840.1.113883.3.1937.777.24.2.3543";
  public static final String VERSION_OID_1_7_2_UNDERAGE_12_17 =
      "2.16.840.1.113883.3.1937.777.24.2.3544";

  public static final String VERSION_OID_Z_MODULE_ACRIBIS =
      "2.16.840.1.113883.3.1937.777.24.2.4031";

  public static final List<String> VERSIONS_MAIN_FORM =
      List.of(VERSION_OID_1_6_D, VERSION_OID_1_6_F, VERSION_OID_1_7_2);
}
