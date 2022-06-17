package com.ar.patient.uc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
            return Typeface.createFromAsset(context.getAssets(), "poppins_regular.ttf");
        } else if (tag == 200) {
            return Typeface.createFromAsset(context.getAssets(), "poppins_medium.ttf");
        } else if (tag == 300) {
            return Typeface.createFromAsset(context.getAssets(), "poppins_semibold.ttf");
        } else if (tag == 400) {
            return Typeface.createFromAsset(context.getAssets(), "poppins_bold.ttf");
        } else if (tag == 500) {
            return Typeface.createFromAsset(context.getAssets(), "poppins_extrabold.ttf");
        } else if (tag == 600) {
            return Typeface.createFromAsset(context.getAssets(), "poppins_black.ttf");
        }
        return Typeface.DEFAULT;
    }

    public static boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static String getAmountFormat(Double amount) {
        DecimalFormat precision = new DecimalFormat("0.00");
        return precision.format(amount);
    }

    public static void share(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "http://www.believenation.com" + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}