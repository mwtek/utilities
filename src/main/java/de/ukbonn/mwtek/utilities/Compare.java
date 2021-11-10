package de.ukbonn.mwtek.utilities;

public class Compare {
  /**
   * Compares two <code>String</code> values and returns <code>true</code> if both are equal.
   * <p>
   * Note that this method will return <code>true</code> if both values are <code>null</code>!
   *
   * @param v1 first string to compare (may be <code>null</code>)
   * @param v2 second string to compare (may be <code>null</code>)
   * @return <code>true</code> if and only if both strings are equal
   */
  public static final boolean isEqual(String v1, String v2) {
    return (v1 == v2) || ((v1 != null) && (v2 != null) && (v1.equals(v2)));
  }
}
