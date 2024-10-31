package com.aws.account.ViewModel.Error;

import java.util.List;
import java.util.ArrayList;

public record ErrorVm(String statusCode,
    String title,
    String detail,
    List<String> fieldErrors) {
  public ErrorVm(String statusCode, String title, String detail) {
    this(statusCode, title, detail, new ArrayList<>());
  }
}
