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

import org.hl7.fhir.r4.model.Address;

public class FhirPatientTools {

  /**
   * Checks if the given {@link Address} contains a specified country code.
   *
   * <p>The method verifies the following conditions:
   *
   * <ul>
   *   <li>The {@code firstAddress} is not {@code null}.
   *   <li>The {@code firstAddress} has a postal code.
   *   <li>The {@code firstAddress} has a country specified.
   *   <li>The {@code firstAddress} has a country element of type string and does not just hold an
   *       data absent reason extension.
   *   <li>The country value in {@code firstAddress} matches the given {@code countryCode}.
   * </ul>
   *
   * @param firstAddress the {@link Address} object to check.
   * @param countryCode the country code to compare with.
   * @return {@code true} if all the conditions are met and the country code matches; otherwise,
   *     {@code false}.
   */
  public static boolean isAddressContainingCountyCode(Address firstAddress, String countryCode) {
    return firstAddress != null
        && firstAddress.hasPostalCode()
        && firstAddress.hasCountry()
        && firstAddress.getCountryElement().hasValue()
        && firstAddress.getCountry().equals(countryCode);
  }
}
