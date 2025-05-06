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
  PATDAT_erheben_speichern_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.1", "PATDAT_erheben_speichern_nutzen"),
  IDAT_erheben("2.16.840.1.113883.3.1937.777.24.5.3.2", "IDAT_erheben"),
  IDAT_speichern_verarbeiten("2.16.840.1.113883.3.1937.777.24.5.3.3", "IDAT_speichern_verarbeiten"),
  IDAT_zusammenfuehren_Dritte(
      "2.16.840.1.113883.3.1937.777.24.5.3.4", "IDAT_zusammenfuehren_Dritte"),
  IDAT_bereitstellen_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.5", "IDAT_bereitstellen_EU_DSGVO_NIVEAU"),
  MDAT_COLLECTION("2.16.840.1.113883.3.1937.777.24.5.3.6", "MDAT_erheben"),
  MDAT_speichern_verarbeiten("2.16.840.1.113883.3.1937.777.24.5.3.7", "MDAT_speichern_verarbeiten"),
  MDAT_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.8", "MDAT_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU"),
  MDAT_zusammenfuehren_Dritte(
      "2.16.840.1.113883.3.1937.777.24.5.3.9", "MDAT_zusammenfuehren_Dritte"),
  Rekontaktierung_Ergebnisse_erheblicher_Bedeutung(
      "2.16.840.1.113883.3.1937.777.24.5.3.37", "Rekontaktierung_Ergebnisse_erheblicher_Bedeutung"),
  PATDAT_retrospektiv_verarbeiten_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.44", "PATDAT_retrospektiv_verarbeiten_nutzen"),
  MDAT_retro_speichern_verarbeiten(
      "2.16.840.1.113883.3.1937.777.24.5.3.45", "MDAT_retro_speichern_verarbeiten"),
  MDAT_retro_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.46",
      "MDAT_retro_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU"),
  MDAT_retro_zusammenfuehren_Dritte(
      "2.16.840.1.113883.3.1937.777.24.5.3.47", "MDAT_retro_zusammenfuehren_Dritte"),
  PATDAT_Weitergabe_non_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.48", "PATDAT_Weitergabe_non_DSGVO_NIVEAU"),
  MDAT_bereitstellen_non_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.49", "MDAT_bereitstellen_non_EU_DSGVO_NIVEAU"),
  KKDAT_retrospektiv_uebertragen_speichern_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.10", "KKDAT_retrospektiv_uebertragen_speichern_nutzen"),
  KKDAT_5J_retro_uebertragen(
      "2.16.840.1.113883.3.1937.777.24.5.3.11", "KKDAT_5J_retro_uebertragen"),
  KKDAT_5J_retro_speichern_verarbeiten(
      "2.16.840.1.113883.3.1937.777.24.5.3.12", "KKDAT_5J_retro_speichern_verarbeiten"),
  KKDAT_5J_retro_wissenschaftlich_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.13", "KKDAT_5J_retro_wissenschaftlich_nutzen"),
  KKDAT_5J_retro_uebertragen_KVNR(
      "2.16.840.1.113883.3.1937.777.24.5.3.38", "KKDAT_5J_retro_uebertragen_KVNR"),
  KKDAT_prospektiv_uebertragen_speichern_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.14", "KKDAT_prospektiv_uebertragen_speichern_nutzen"),
  KKDAT_5J_pro_uebertragen("2.16.840.1.113883.3.1937.777.24.5.3.15", "KKDAT_5J_pro_uebertragen"),
  KKDAT_5J_pro_speichern_verarbeiten(
      "2.16.840.1.113883.3.1937.777.24.5.3.16", "KKDAT_5J_pro_speichern_verarbeiten"),
  KKDAT_5J_pro_wissenschaftlich_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.17", "KKDAT_5J_pro_wissenschaftlich_nutzen"),
  KKDAT_5J_pro_uebertragen_KVNR(
      "2.16.840.1.113883.3.1937.777.24.5.3.39", "KKDAT_5J_pro_uebertragen_KVNR"),
  BIOMAT_erheben_lagern_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.18", "BIOMAT_erheben_lagern_nutzen"),
  BIOMAT_erheben("2.16.840.1.113883.3.1937.777.24.5.3.19", "BIOMAT_erheben"),
  BIOMAT_lagern_verarbeiten("2.16.840.1.113883.3.1937.777.24.5.3.20", "BIOMAT_lagern_verarbeiten"),
  BIOMAT_Eigentum_uebertragen(
      "2.16.840.1.113883.3.1937.777.24.5.3.21", "BIOMAT_Eigentum_uebertragen"),
  BIOMAT_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.22", "BIOMAT_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU"),
  BIOMAT_Analysedaten_zusammenfuehren_Dritte(
      "2.16.840.1.113883.3.1937.777.24.5.3.23", "BIOMAT_Analysedaten_zusammenfuehren_Dritte"),
  BIOMAT_Zusatzentnahme("2.16.840.1.113883.3.1937.777.24.5.3.24", "BIOMAT_Zusatzentnahme"),
  BIOMAT_Zusatzmengen_entnehmen(
      "2.16.840.1.113883.3.1937.777.24.5.3.25", "BIOMAT_Zusatzmengen_entnehmen"),
  BIOMAT_retrospektiv_speichern_nutzen(
      "2.16.840.1.113883.3.1937.777.24.5.3.50", "BIOMAT_retrospektiv_speichern_nutzen"),
  BIOMAT_retro_lagern_verarbeiten(
      "2.16.840.1.113883.3.1937.777.24.5.3.51", "BIOMAT_retro_lagern_verarbeiten"),
  BIOMAT_retro_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.52",
      "BIOMAT_retro_wissenschaftlich_nutzen_EU_DSGVO_NIVEAU"),
  BIOMAT_retro_Analysedaten_zusammenfuehren_Dritte(
      "2.16.840.1.113883.3.1937.777.24.5.3.53", "BIOMAT_retro_Analysedaten_zusammenfuehren_Dritte"),
  BIOMAT_Weitergabe_non_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.54", "BIOMAT_Weitergabe_non_EU_DSGVO_NIVEAU"),
  BIOMAT_bereitstellen_non_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.55", "BIOMAT_bereitstellen_non_EU_DSGVO_NIVEAU"),
  Rekontaktierung_Ergaenzungen(
      "2.16.840.1.113883.3.1937.777.24.5.3.26", "Rekontaktierung_Ergaenzungen"),
  Rekontaktierung_Verknuepfung_Datenbanken(
      "2.16.840.1.113883.3.1937.777.24.5.3.27", "Rekontaktierung_Verknuepfung_Datenbanken"),
  RECONTACTING_FURTHER_SURVEY(
      "2.16.840.1.113883.3.1937.777.24.5.3.28", "Rekontaktierung_weitere_Erhebung"),
  Rekontaktierung_weitere_Studien(
      "2.16.840.1.113883.3.1937.777.24.5.3.29", "Rekontaktierung_weitere_Studien"),
  Rekontaktierung_Zusatzbefund(
      "2.16.840.1.113883.3.1937.777.24.5.3.30", "Rekontaktierung_Zusatzbefund"),
  Rekontaktierung_Zusatzbefund2(
      "2.16.840.1.113883.3.1937.777.24.5.3.31", "Rekontaktierung_Zusatzbefund"),
  Z1_GECCO83_Nutzung_NUM_CODEX(
      "2.16.840.1.113883.3.1937.777.24.5.3.32", "Z1_GECCO83_Nutzung_NUM_CODEX"),
  MDAT_GECCO83_komplettieren_einmalig(
      "2.16.840.1.113883.3.1937.777.24.5.3.40", "MDAT_GECCO83_komplettieren_einmalig"),
  MDAT_GECC083_erheben("2.16.840.1.113883.3.1937.777.24.5.3.43", "MDAT_GECC083_erheben"),
  MDAT_GECCO83_bereitstellen_NUM_CODEX(
      "2.16.840.1.113883.3.1937.777.24.5.3.33", "MDAT_GECCO83_bereitstellen_NUM_CODEX"),
  MDAT_GECCO83_speichern_verarbeiten_NUM_CODEX(
      "2.16.840.1.113883.3.1937.777.24.5.3.34", "MDAT_GECCO83_speichern_verarbeiten_NUM_CODEX"),
  MDAT_GECCO83_wissenschaftlich_nutzen_NUM_CODEX_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.56",
      "MDAT_GECCO83_wissenschaftlich_nutzen_NUM_CODEX_EU_DSGVO_NIVEAU"),
  Z1_GECCO83_Weitergabe_NUM_CODEX_non_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.35",
      "Z1_GECCO83_Weitergabe_NUM_CODEX_non_EU_DSGVO_NIVEAU"),
  MDAT_GECCO83_bereitstellen_NUM_CODEX_non_EU_DSGVO_NIVEAU(
      "2.16.840.1.113883.3.1937.777.24.5.3.36",
      "MDAT_GECCO83_bereitstellen_NUM_CODEX_non_EU_DSGVO_NIVEAU"),

  // ACRIBIS
  Z2_PAT_DATA(
      "2.16.840.1.113883.3.1937.777.24.5.3.57",
      "Z2 Patientendaten erheben, nutzen, Kontaktierung im acribis-Projekt"),
  Z2_IDAT(
      "2.16.840.1.113883.3.1937.777.24.5.3.59",
      "Z2 IDAT Melderegister abfragen, speichern, verarbeiten im acribis-Projekt"),
  Z2_MDAT(
      "2.16.840.1.113883.3.1937.777.24.5.3.61",
      "Z2 MDAT Hausarzt erheben, speichern, verarbeiten, nutzen im acribis-Projekt");

  public static final String PROVISION_CODE_SYSTEM = "urn:oid:2.16.840.1.113883.3.1937.777.24.5.3";
  @Getter private final String code;
  private final String display;

  MiiConsentPolicyValueSet(String code, String display) {
    this.display = display;
    this.code = code;
  }
}
