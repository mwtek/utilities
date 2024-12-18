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
 */ package de.ukbonn.mwtek.utilities.fhir.misc;

import static de.ukbonn.mwtek.utilities.fhir.mapping.location.valuesets.LocationFixedValues.ICU;
import static de.ukbonn.mwtek.utilities.fhir.mapping.location.valuesets.LocationFixedValues.WARD;

import de.ukbonn.mwtek.utilities.fhir.resources.UkbEncounter;
import de.ukbonn.mwtek.utilities.fhir.resources.UkbLocation;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;

public class LocationTools {

  public static final String ICU_DUMMY_ID = "ICU_DUMMY";

  public static final String ICU_DUMMY_REF = "Location/" + ICU_DUMMY_ID;

  // Static variable for the dummy ICU location
  private static UkbLocation dummyIcuLocation;

  /**
   * Creates a dummy ICU location that can be used to simulate ICU encounters if {@link
   * UkbEncounter#getServiceProvider()} is used by a provider to flag stays in ICU wards.
   *
   * <p>The location is created only once and reused on subsequent calls.
   *
   * <p>This method is just thread-safe on reading operations.
   *
   * @return The dummy ICU location.
   */
  public static UkbLocation createDummyIcuWardLocation() {
    // Check if the dummy ICU location is already initialized
    if (dummyIcuLocation == null) {
      // Initialize the dummy ICU location
      dummyIcuLocation = new UkbLocation();
      dummyIcuLocation.setId(ICU_DUMMY_ID);
      dummyIcuLocation.setPhysicalType(
          new CodeableConcept(
              new Coding(
                  "http://terminology.hl7.org/CodeSystem/location-physical-type", WARD, "Ward")));
      dummyIcuLocation.addType(
          new CodeableConcept(
              new Coding(
                  "http://terminology.hl7.org/CodeSystem/v3-RoleCode",
                  ICU,
                  "Intensive care unit")));
    }
    // Return the static dummy ICU location
    return dummyIcuLocation;
  }

  public static boolean isDummyIcuLocation(Reference locationReference) {
    return locationReference.hasReference() && locationReference.getId().equals(ICU_DUMMY_ID);
  }
}
