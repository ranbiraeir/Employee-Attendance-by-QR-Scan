package com.ranbiraeir.attendanceregister;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Validator {
    public static boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[\\p{L} .'-]+$");
        return pattern.matcher(name).matches();
    }

    public static boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile("[7-9][0-9]{9}");
        return pattern.matcher(phone).matches();
    }
}
