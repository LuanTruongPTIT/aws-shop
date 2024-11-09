package com.aws.identity.view;

import java.util.Date;

public record TokenSigning(
    String token,
    Date expiryDate) {

}
