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

import lombok.Getter;

/**
 * Names and Codes from <a
 * href="https://simplifier.net/medizininformatikinitiative-modulconsent/2.16.840.1.113883.3.1937.777.24.11.36--20210423105554">the
 * kds module consent in the simplifier</a> Used in plugin.orbis and plugin.biobank.
 */
@Getter
public enum MiiConsentPolicyValueSet {
  PATDAT_RETRIEVAL_SAVING_USING(
      "2.16.840.1.113883.3.1937.777.24.5.3.1", "Patientendaten erheben, speichern, nutzen"),
  IDAT_COLLECT("2.16.840.1.113883.3.1937.777.24.5.3.2", "IDAT erheben"),
  IDAT_SAVE_PROCESS("2.16.840.1.113883.3.1937.777.24.5.3.3", "IDAT speichern, verarbeiten"),
  IDAT_MERGE_THIRD_PARTIES("2.16.840.1.113883.3.1937.777.24.5.3.4", "IDAT zusammenfuehren Dritte"),
  IDAT_OFFER_DSGVO("2.16.840.1.113883.3.1937.777.24.5.3.5", "IDAT bereitstellen EU DSGVO NIVEAU"),
  MDAT_COLLECTION("2.16.840.1.113883.3.1937.777.24.5.3.6", "MDAT erheben"),
  MDAT_SAVE_PROCESS("2.16.840.1.113883.3.1937.777.24.5.3.7", "MDAT speichern, verarbeiten"),
  MDAT_SCIENTIFIC_USAGE_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.8", "MDAT wissenschaftlich nutzen EU DSGVO NIVEAU"),
  MDAT_MERGE_THIRD_PARTIES("2.16.840.1.113883.3.1937.777.24.5.3.9", "MDAT zusammenfuehren Dritte"),
  RECONTACTING_ON_EVENTS(
      "2.16.840.1.113883.3.1937.777.24.5.3.37", "Rekontaktierung Ergebnisse erheblicher Bedeutung"),
  PATDAT_RETROSPECTIVE_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.44", "Patientendaten retrospektiv verarbeiten, nutzen"),
  MDAT_RETRO_SAVE_PROCESS(
      "2.16.840.1.113883.3.1937.777.24.5.3.45", "MDAT retrospektiv speichern verarbeiten"),
  MDAT_RETRO_SCIENTIFIC_USAGE_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.46",
      "MDAT retrospektiv wissenschaftlich nutzen EU DSGVO NIVEAU"),
  MDAT_RETRO_MERGE_THIRD_PARTIES(
      "2.16.840.1.113883.3.1937.777.24.5.3.47", "MDAT retrospektiv zusammenfuehren Dritte"),
  PATDAT_APPROVAL_FORWARDING_NON_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.48", "Patientendaten Weitergabe non DSGVO NIVEAU"),
  MDAT_OFFER_NON_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.49", "MDAT bereitstellen non EU DSGVO NIVEAU"),
  KKDAT_RETRO_TRANSFER_SAVE_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.10",
      "Krankenkassendaten retrospektiv uebertragen, speichern, nutzen"),
  KKDAT_5_YEARS_RETRO_TRANSFER(
      "2.16.840.1.113883.3.1937.777.24.5.3.11", "KKDAT 5J retrospektiv uebertragen"),
  KKDAT_5_YEARS_RETRO_TRANSFER_SAVE_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.12", "KKDAT 5J retrospektiv speichern verarbeiten"),
  KKDAT_5_YEARS_RETRO_SCIENTIFIC_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.13", "KKDAT 5J retrospektiv wissenschaftlich nutzen"),
  KKDAT_5_YEARS_RETRO_TRANSFER_KVNR(
      "2.16.840.1.113883.3.1937.777.24.5.3.38", "KKDAT 5J retrospektiv uebertragen KVNR"),
  KKDAT_PROSPECTIVE_TRANSFER_SAVE_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.14", "KKDAT prospektiv uebertragen speichern nutzen"),
  KKDAT_5_YEARS_PROSPECTIVE_TRANSFER(
      "2.16.840.1.113883.3.1937.777.24.5.3.15", "KKDAT 5J prospektiv uebertragen"),
  KKDAT_5_YEARS_PROSPECTIVE_SAVE_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.16", "KKDAT 5J prospektiv speichern verarbeiten"),
  KKDAT_5_YEARS_PROSPECTIVE_SCIENTIFIC_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.17", "KKDAT 5J prospektiv wissenschaftlich nutzen"),
  KKDAT_5_YEARS_PROSPECTIVE_TRANSFER_KVNR(
      "2.16.840.1.113883.3.1937.777.24.5.3.39", "KKDAT 5J prospektiv uebertragen KVNR"),
  BIOMAT_COLLECT_STORE_USE(
      "2.16.840.1.113883.3.1937.777.24.5.3.18", "Biomaterial erheben, lagern, nutzen"),
  BIOMAT_COLLECT("2.16.840.1.113883.3.1937.777.24.5.3.19", "BIOMAT erheben"),
  BIOMAT_STORE_TRANSFER("2.16.840.1.113883.3.1937.777.24.5.3.20", "BIOMAT lagern verarbeiten"),
  BIOMAT_TRANSFER_OWNERSHIP("2.16.840.1.113883.3.1937.777.24.5.3.21", "BIOMAT Eigentum übertragen"),
  BIOMAT_SCIENTIFIC_USAGE_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.22", "BIOMAT wissenschaftlich nutzen EU DSGVO NIVEAU"),
  BIOMAT_MERGE_ANALYSIS_DATA_THIRD_PARTIES(
      "2.16.840.1.113883.3.1937.777.24.5.3.23", "BIOMAT Analysedaten zusammenfuehren Dritte"),
  BIOMAT_ADDITIONAL_SAMPLING(
      "2.16.840.1.113883.3.1937.777.24.5.3.24", "Biomaterial Zusatzentnahme"),
  BIOMAT_ADDITIONAL_QUANTITIES(
      "2.16.840.1.113883.3.1937.777.24.5.3.25", "BIOMAT Zusatzmengen entnehmen"),
  BIOMAT_RETRO_STORING_USAGE(
      "2.16.840.1.113883.3.1937.777.24.5.3.50", "Biomaterial retrospektiv speichern, nutzen"),
  BIOMAT_RETRO_STORAGE_PROCESS(
      "2.16.840.1.113883.3.1937.777.24.5.3.51", "BIOMAT retrospektiv lagern verarbeiten"),
  BIOMAT_RETRO_SCIENTIFIC_USAGE_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.52",
      "BIOMAT retrospektiv wissenschaftlich nutzen EU DSGVO NIVEAU"),
  BIOMAT_RETRO_MERGE_ANALYSIS_DATA_THIRD_PARTIES(
      "2.16.840.1.113883.3.1937.777.24.5.3.53",
      "BIOMAT retrospektiv Analysedaten zusammenfuehren Dritte"),
  BIOMAT_FORWARDING_NON_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.54", "Biomaterial Weitergabe non EU DSGVO NIVEAU"),
  BIOMAT_OFFERING_NON_DSGVO(
      "2.16.840.1.113883.3.1937.777.24.5.3.55", "BIOMAT bereitstellen ohne EU DSGVO NIVEAU"),
  RECONTACTING_ADDITIONS("2.16.840.1.113883.3.1937.777.24.5.3.26", "Rekontaktierung Ergänzungen"),
  RECONTACTING_MERGING_DBS(
      "2.16.840.1.113883.3.1937.777.24.5.3.27", "Rekontaktierung Verknüpfung Datenbanken"),
  RECONTACTING_FURTHER_COLLECTION(
      "2.16.840.1.113883.3.1937.777.24.5.3.28", "Rekontaktierung weitere Erhebung"),
  RECONTACTING_FURTHER_STUDIES(
      "2.16.840.1.113883.3.1937.777.24.5.3.29", "Rekontaktierung weitere Studien"),
  RECONTACTING_ADDTIONAL_FINDING(
      "2.16.840.1.113883.3.1937.777.24.5.3.30", "Rekontaktierung Zusatzbefund"),
  RECONTACTING_ADDTIONAL_FINDING_LVL_2(
      "2.16.840.1.113883.3.1937.777.24.5.3.31", "Rekontaktierung Zusatzbefund"),
  Z1_GECCO83_USAGE_NUM_CODEX(
      "2.16.840.1.113883.3.1937.777.24.5.3.32", "Z1 GECCO83 Nutzung NUM/CODEX"),
  MDAT_GECCO83_FULFILLING_ONCE(
      "2.16.840.1.113883.3.1937.777.24.5.3.40", "MDAT GECCO83 komplettieren einmalig"),
  MDAT_GECC083_GATHERING("2.16.840.1.113883.3.1937.777.24.5.3.43", "MDAT GECCO83 erheben"),
  MDAT_GECCO83_OFFERING_NUM_CODEX(
      "2.16.840.1.113883.3.1937.777.24.5.3.33", "MDAT GECCO83 bereitstellen NUM/CODEX"),
  MDAT_GECCO83_STORAGE_PROCESSING_NUM_CODEX(
      "2.16.840.1.113883.3.1937.777.24.5.3.34", "MDAT GECCO83 speichern verarbeiten NUM/CODEX"),
  MDAT_GECCO83_SCIENTIFIC_USAGE_NUM_CODEX_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.56",
      "MDAT GECCO83 wissenschaftlich nutzen NUM/CODEX EU DSGVO NIVEAU"),

  // ACRIBIS
  Z2_PAT_DATA(
      "2.16.840.1.113883.3.1937.777.24.5.3.57",
      "Z2 Patientendaten erheben, nutzen, Kontaktierung im acribis-Projekt"),
  Z2_PAT_DATA_LVL_2(
      "2.16.840.1.113883.3.1937.777.24.5.3.58",
      "PATDAT erheben, nutzen, Kontaktierung im acribis-Projekt"),
  Z2_IDAT(
      "2.16.840.1.113883.3.1937.777.24.5.3.59",
      "Z2 IDAT Melderegister abfragen, speichern, verarbeiten im acribis-Projekt"),
  Z2_IDAT_LVL_2(
      "2.16.840.1.113883.3.1937.777.24.5.3.60",
      "Anschrift und Vitalstatus Melderegister abfragen, speichern, verarbeiten im"
          + " acribis-Projekt"),
  Z2_MDAT(
      "2.16.840.1.113883.3.1937.777.24.5.3.61",
      "Z2 MDAT Hausarzt erheben, speichern, verarbeiten, nutzen im acribis-Projekt"),
  Z2_MDAT_LVL_2(
      "2.16.840.1.113883.3.1937.777.24.5.3.61",
      "MDAT Hausarzt erheben, speichern, verarbeiten, nutzen im acribis-Projekt");

  public static final String PROVISION_CODE_SYSTEM = "urn:oid:2.16.840.1.113883.3.1937.777.24.5.3";
  @Getter private final String code;
  private final String display;

  MiiConsentPolicyValueSet(String code, String display) {
    this.display = display;
    this.code = code;
  }
}
