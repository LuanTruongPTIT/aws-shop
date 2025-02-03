package com.aws.identity.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class MessageUtils {
  static ResourceBundle messageBundle = ResourceBundle.getBundle("messages.messages", Locale.getDefault());

  public static String getMessage(String errorCode, Object... var2) {
    String message;

    try {
      message = messageBundle.getString(errorCode);
    } catch (MissingResourceException e) {
      message = errorCode;
    }
    FormattingTuple formatingTuple = MessageFormatter.arrayFormat(message, var2);
    return formatingTuple.getMessage();
  }
}
