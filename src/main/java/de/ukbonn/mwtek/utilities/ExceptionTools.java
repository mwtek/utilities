package de.ukbonn.mwtek.utilities;
import java.util.Collection;

public class ExceptionTools {

  public static final <T extends Object> T checkNull(String parameterName, T argumentValue) throws IllegalArgumentException {
    if (argumentValue == null) {
      throw new IllegalArgumentException(parameterName);
    }
    return argumentValue;
  }

  public static final String checkNullOrEmpty(String parameterName, String argumentValue) throws IllegalArgumentException {
    if (argumentValue == null || argumentValue.isEmpty()) {
      throw new IllegalArgumentException(parameterName);
    }
    return argumentValue;
  }

  public static final <T extends Collection<?>> T checkNullOrEmpty(String parameterName, T argumentValue) throws IllegalArgumentException {
    if (argumentValue == null || argumentValue.isEmpty()) {
      throw new IllegalArgumentException(parameterName);
    }
    return argumentValue;
  }
}
