package com.paglaai.estimaai.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StringUtil {
  public static String dateToString(Date date) {
    if (date == null) {
      return null;
    }
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(date);
  }

  public static String localDateTime(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
  }

  public static LocalDateTime localDateTime(String localDateTime) {
    try {
      return LocalDateTime.parse(
          localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    } catch (Exception ignored) {
      return null;
    }
  }

  public static String nullToTitleString(String param) {
    return param == null ? "Project Estimation" : TitleCaseUtil.convertToTitleCase(param);
  }

  public static String nullToEmptyString(String param) {
    return param == null ? "" : param;
  }
}
