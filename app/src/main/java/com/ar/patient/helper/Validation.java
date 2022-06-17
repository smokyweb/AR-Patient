package com.ar.patient.helper;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static int EMAIL = 1;
    public static int EMPTY = 2;
    public static int PASSWORD = 3;
    public static int PHONE = 4;
    public static int ZIPCODE = 5;

    public static boolean isValid(int type, EditText edtVal) {

        String value = edtVal.getText().toString().trim();
        switch (type) {
            case 1:
                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(value);
                return matcher.matches();

            case 2:
                return (value.trim().equals("")) ? false : true;
            case 3:
                if (value.trim().length() < 8) {
                    return false;
                } else {
                    return true;
                }
            case 4:
                if (value.trim().length() < 14) {
                    return false;
                } else {
                    return true;
                }
            case 5:
                if (value.trim().length() < 5 || value.trim().length() > 10) {
                    return false;
                } else {
                    return true;
                }
            default:
                return false;

        }

    }
}
