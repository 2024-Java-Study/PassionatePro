package com.example.pro.common.exception;

import java.util.regex.Pattern;

public class Validator {

    public static String validString(String value) {
        if (value == null) {
            throw new ValidationException("null값이 포함되어 있습니다.");
        }
        if (value.isBlank())
            throw new ValidationException("공백인 값이 포함되어 있습니다.");
        return value;
    }

    public static String validEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (email == null || ! pattern.matcher(email).matches())
            throw new ValidationException("이메일 형식이 올바르지 않습니다.");
        return email;
    }
}
