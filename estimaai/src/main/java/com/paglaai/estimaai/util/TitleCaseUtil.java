package com.paglaai.estimaai.util;

public class TitleCaseUtil {
  public static String convertToTitleCase(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    StringBuilder converted = new StringBuilder();

    boolean convertNext = true;
    for (char ch : input.toCharArray()) {
      if (Character.isSpaceChar(ch)) {
        convertNext = true;
      } else if (convertNext) {
        ch = Character.toTitleCase(ch);
        convertNext = false;
      } else {
        ch = Character.toLowerCase(ch);
      }
      converted.append(ch);
    }

    return converted.toString();
  }
}
