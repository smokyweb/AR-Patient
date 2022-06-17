package com.ar.patient.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.patient.responsemodel.SubscriptionfeaturedList;
import com.ar.patient.service.CommandModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class Utils {
    public final static Pattern Email_address_pattern = Pattern.compile("^[-a-zA-Z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-zA-Z0-9~!$%^&*_=+}{\\'?]+)*@([a-zA-Z0-9_][-a-z0-9_]*(\\.[-a-zA-Z0-9_]+)*\\.[A-Za-z]{2,})?$");
    public final static Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9.]*$");
    public final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,}");
    static Dialog dialog;
    private static Dialog imgdialog = null;

    public static void logPrint(String s) {
        // if (BuildConfig.DEBUG)
        Log.d("mytag", "log print : " + s);
    }


    public static String getPackageName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static String getCachePath(Context context) {
        String path = context.getApplicationInfo().dataDir + "/media";
        path = context.getExternalCacheDir().getPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }


    public static String getDurationString(double d) {
        int sec = 1;
        int minute = sec * 60;
        int hours = minute * 60;
        String duration = "";
        if (((int) d / hours) > 0) {
            duration = (int) (d / hours) + "hour ";
        }

        if (((int) d / minute) > 0) {
            duration = duration + (int) (d / minute) + " minute";
        }

        if (duration.equalsIgnoreCase(""))
            duration = "0 mints";

        return duration;
    }

    public static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static void allCaps(TextView view) {
        view.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
    }


    public static String getDeviceUUID(Activity activity) {
        if (Pref.getValue(activity, Config.PREF_UDID, "").isEmpty()) {
            String uuid = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
            Pref.setValue(activity, Config.PREF_UDID, uuid);
            return uuid;
        } else {
            return Pref.getValue(activity, Config.PREF_UDID, "");
        }
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static long compareDate(String strDateTime1, String strDateTime2, String currentFormate) {
        return convertStringToDate(strDateTime1, currentFormate).getTime() - convertStringToDate(strDateTime2, currentFormate).getTime();
    }

    public static Date convertStringToDate(String strDateTime, String currentFormate) {
        try {
            return new SimpleDateFormat(currentFormate).parse(strDateTime);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static String convertStringToString(String strDateTime, String parseFormat) {
        return convertStringToString(strDateTime, Config.FULL_SERVER_DATE_FORMATE, parseFormat);
    }

    public static String convertStringToString(String strDateTime, String currentFormate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).format(new SimpleDateFormat(currentFormate).parse(strDateTime));
        } catch (Exception e) {
            return "";
        }
    }


    public static String getCurrentDate(String currentFormate) {
        return new SimpleDateFormat(currentFormate).format(new Date());
    }

    public static String getCurrentDatenew() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(curDate);
    }

    public static String getCurrentDate() {
        return getCurrentDate(Config.FULL_SERVER_DATE_FORMATE);
    }


    public static String convertDateToString(Date dateTime, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).format(dateTime);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDate(String date, String inputFormate, String outputFormate) {
        try {
            if (date.equals(null)) {
                return "";
            }
            SimpleDateFormat your_format = new SimpleDateFormat("" + outputFormate);
            Date date1 = new SimpleDateFormat("" + inputFormate).parse(date);
            return your_format.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertStringToStringGMT(String strDateTime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.FULL_SERVER_DATE_FORMATE);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            return simpleDateFormat.format(new SimpleDateFormat(Config.FULL_SERVER_DATE_FORMATE).parse(strDateTime));
        } catch (Exception e) {
            return "";
        }
    }


    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    /*public static void enableFabric(Context context) {
        final Fabric fabric = new Fabric.Builder(context)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
    }


    public static String getFirebaseToken() {
        return FirebaseInstanceId.getInstance().getToken();

    }*/

    public static String getAmountFormat(Double amount) {
        DecimalFormat precision = new DecimalFormat("0.00");
        return precision.format(amount);
    }

    public static String getUSFormat(Double amount) {
        DecimalFormat precision = new DecimalFormat("#,##,##0.00");
        return precision.format(amount);
    }

    public static String getSessoinKey(Context context) {
        if (!Pref.getValue(context, Config.PREF_SESSION, "").equalsIgnoreCase("")) {
            return Pref.getValue(context, Config.PREF_SESSION, "");
        } else {
            return Config.API_KEY;
        }
    }

    public static void hideKeyboard(Context context, View mainLayout) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    public static String isSuccessRestclient(int code) {

        switch (code) {
            case 200:
                return "";
            case 500:
                return Config.ERROR_500;
            case 404:
                return Config.ERROR_404;
            case 405:
                return Config.ERROR_405;
            case 403:
                return Config.ERROR_403;
            case 401:
                return Config.ERROR_401;
            default:
                return Config.ERROR_MESSAGE;

        }
    }

    public static boolean isLocationEnabled(LocationManager locationManager, Context context) {
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }


    public static void uploadImg(Context context, String path, int placeHolderDrawable, ImageView mImageView) {

        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(placeHolderDrawable).error(placeHolderDrawable).circleCrop())
                .apply(RequestOptions.circleCropTransform())

                .into(mImageView);
    }

    public static void loadImage(Context context, String filepath, int placeHolderDrawable, ImageView imageView) {
        Glide.with(context).load(Config.MEDIA_URL + filepath).apply(new RequestOptions().placeholder(placeHolderDrawable).error(placeHolderDrawable)).into(imageView);

    }

    public static void loadImageWithoutMediaUrl(Context context, String filepath, int placeHolderDrawable, ImageView imageView) {
        Glide.with(context).load(filepath).apply(new RequestOptions().placeholder(placeHolderDrawable).error(placeHolderDrawable)).into(imageView);

    }

    public static void loadImageWithSmall(Context context, String filename, int placeHolderDrawable, ImageView imageView, int width, int height) {
        loadImageWithSmall(context, filename, placeHolderDrawable, imageView, true, width, height);
    }

    public static void loadImageWithSmall(Context context, String filename, int placeHolderDrawable, ImageView imageView, boolean isCricle, int width, int height) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolderDrawable);
        options.override(width, height);
        options.error(placeHolderDrawable);
        if (isCricle)
            options.circleCrop();

        Glide.with(context).load(filename).apply(options).into(imageView);
    }


    public static RequestBody getrequestBodyFromString(String s) {
        return RequestBody.create(MediaType.parse("text/plain"), s.trim());
    }

    public static String convertPhoneWithOutFormate(String s) {
        return s.replaceAll(" ", "").replaceAll("-", "").replaceAll("\\(", "").replaceAll("\\)", "");
    }

    public static Bitmap getImageCaptured(String fname, Context ctx) {
        File f = new File(Utils.getCachePath(ctx), fname);


        Bitmap rotatebitmap = null;

        try {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inPurgeable = true;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap bitmap_temp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);


            ExifInterface exif = new ExifInterface(f.toString());
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;

            }


            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            rotatebitmap = Bitmap.createBitmap(bitmap_temp, 0, 0, bitmap_temp.getWidth(), bitmap_temp.getHeight(), mtx, true);
            FileOutputStream out = new FileOutputStream(f);
            rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f1 = new File("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                Uri contentUri = Uri.fromFile(f1);
                mediaScanIntent.setData(contentUri);
                ctx.sendBroadcast(mediaScanIntent);
            } else {
                ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatebitmap;

    }

    public static File downloadImage(Context cxt, Bitmap bmp, String filename) {
        File file = null;
        Bitmap bmpTemp = null;
        FileOutputStream out = null;

        try {

            bmpTemp = getResizedBitmap(bmp, 640);

            file = new File(Utils.getCachePath(cxt), filename);

            out = new FileOutputStream(file);

            bmpTemp.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (Exception e) {

        }

        if (out != null) {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
            }
        }

        if (bmpTemp != null) {
            bmpTemp.recycle();
        }


        bmpTemp = null;

        return file;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        Bitmap bitmap = null;
        try {

            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        } catch (OutOfMemoryError e) {

        }

        return bitmap;
    }

    public static boolean getVersionCheck() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static void withoutEnterSpaceEdittextview(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = editText.getText().toString();
                if (str.length() > 0 && str.contains(" ")) {
                    editText.setText(editText.getText().toString().replaceAll(" ", ""));
                    editText.setSelection(editText.getText().length());
                }
            }
        });
    }

    public static void getEngConfig(Context mContext) {
        Configuration config = mContext.getResources().getConfiguration();
        Locale locale = new Locale("en"); // <---- your target language
        Locale.setDefault(locale);
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }

    public static void getLocaleConfig(Context mContext) {
        Configuration config = mContext.getResources().getConfiguration();
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void clearNotification(final Context context, final int id) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);
            }
        }, 500);
    }

    public static String getDeviceToken(Context context) {
        try {
            String deviceToken = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return deviceToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean CompareTwoDates(String StartDate, String endData) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            Date date1 = formatter.parse(StartDate);


            Date date2 = formatter.parse(endData);

            if (date2.compareTo(date1) < 0) {
                System.out.println("date2 is Smaller than my date1");
                Log.d("mytag", "Date Comapre Msg : " + "date2 is Smaller than my date1");
                return true;
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public static boolean check18yearold(int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            return false;
        }
        return true;
    }

    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e("mytag", "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e("mytag", "Unknown screen orientation. Defaulting to " +
                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    public static ArrayList<String> getTweleMonths() {
        try {
            ArrayList<String> allDates = new ArrayList<>();
            String maxDate = "dec-2099";
            SimpleDateFormat monthDate = new SimpleDateFormat("MMM");
            Calendar cal = Calendar.getInstance();
            cal.setTime(monthDate.parse(maxDate));
            for (int i = 1; i <= 12; i++) {
                String month_name1 = monthDate.format(cal.getTime());
                allDates.add(month_name1);
                cal.add(Calendar.MONTH, -1);
            }
            return reverse(allDates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> reverse(ArrayList<String> list) {
        for (int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        return list;
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
            //e.toString();
        }
    }

    public static class UsPhoneNumberFormatter implements TextWatcher {
        //This TextWatcher sub-class formats entered numbers as 1 (123) 456-7890
        private boolean mFormatting; // this is a flag which prevents the
        // stack(onTextChanged)
        private boolean clearFlag;
        private int mLastStartLocation;
        private String mLastBeforeText;
        private WeakReference<EditText> mWeakEditText;

        public UsPhoneNumberFormatter(WeakReference<EditText> weakEditText) {
            this.mWeakEditText = weakEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (after == 0 && s.toString().equals("-0 ")) {
                clearFlag = true;
            }
            mLastStartLocation = start;
            mLastBeforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO: Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Make sure to ignore calls to afterTextChanged caused by the work
            // done below
            if (!mFormatting) {
                mFormatting = true;
                int curPos = mLastStartLocation;
                String beforeValue = mLastBeforeText;
                String currentValue = s.toString();
                String formattedValue = formatUsNumber(s);
                if (currentValue.length() > beforeValue.length()) {
                    int setCusorPos = formattedValue.length()
                            - (beforeValue.length() - curPos);
                    mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                } else {
                    int setCusorPos = formattedValue.length()
                            - (currentValue.length() - curPos);
                    if (setCusorPos > 0 && !Character.isDigit(formattedValue.charAt(setCusorPos - 1))) {
                        setCusorPos--;
                    }
                    mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                }
                mFormatting = false;
            }
        }

        private String formatUsNumber(Editable text) {
            StringBuilder formattedString = new StringBuilder();
            // Remove everything except digits
            int p = 0;
            while (p < text.length()) {
                char ch = text.charAt(p);
                if (!Character.isDigit(ch)) {
                    text.delete(p, p + 1);
                } else {
                    p++;
                }
            }
            // Now only digits are remaining
            String allDigitString = text.toString();

            int totalDigitCount = allDigitString.length();

            if (totalDigitCount == 0
                    || (totalDigitCount > 10 && !allDigitString.startsWith("-0"))
                    || totalDigitCount > 11) {
                // May be the total length of input length is greater than the
                // expected value so we'll remove all formatting
                text.clear();
                text.append(allDigitString);
                return allDigitString;
            }
            int alreadyPlacedDigitCount = 0;
            // Only '1' is remaining and user pressed backspace and so we clear
            // the edit text.
            if (allDigitString.equals("-0") && clearFlag) {
                text.clear();
                clearFlag = false;
                return "";
            }
            if (allDigitString.startsWith("-0")) {
                formattedString.append("-0 ");
                alreadyPlacedDigitCount++;
            }
            // The first 3 numbers beyond '1' must be enclosed in brackets "()"
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append("("
                        + allDigitString.substring(alreadyPlacedDigitCount,
                        alreadyPlacedDigitCount + 3) + ") ");
                alreadyPlacedDigitCount += 3;
            }
            // There must be a '-' inserted after the next 3 numbers
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append(allDigitString.substring(
                        alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3)
                        + "-");
                alreadyPlacedDigitCount += 3;
            }
            // All the required formatting is done so we'll just copy the
            // remaining digits.
            if (totalDigitCount > alreadyPlacedDigitCount) {
                formattedString.append(allDigitString
                        .substring(alreadyPlacedDigitCount));
            }

            text.clear();
            text.append(formattedString.toString());
            return formattedString.toString();
        }

    }

    public static String join(String separator, ArrayList<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.size(); i++) {

            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }

        }

        return sb.toString();

    }

    public static void saveCommandList(Context context, ArrayList<CommandModel> commandModelArrayList) {
        Pref.setValue(context, Config.PREF_COMMAND_LIST, toJson(commandModelArrayList));
    }

    public static ArrayList<CommandModel> getCommandArrayList(Context context) {
        if (Pref.getValue(context, Config.PREF_COMMAND_LIST, "").equalsIgnoreCase("")) {
            return new ArrayList<CommandModel>();
        } else {
            return (ArrayList<CommandModel>) fromJson(Pref.getValue(context, Config.PREF_COMMAND_LIST, ""), new TypeToken<ArrayList<CommandModel>>() {
            }.getType());
        }
    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    public static String compressImage(Context context, String imageUri) {

        String filePath = getRealPathFromURI(context, imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public boolean checkDuplicateURL(String urlList, String name) {
        if (getFileNameFromURL(urlList).equalsIgnoreCase("" + name)) {
            return false;
        }
        return true;
    }

    public static String getDateFromMilliSecond(long millisecond) {
        Date date = new Date();
        date.setTime(millisecond);
        return new SimpleDateFormat(Config.SERVER_FULL_FORMAT).format(date);
    }

    public static String checkNullValueString(String string) {
        if (string == null || string.equalsIgnoreCase("null") || string.trim().length() == 0) {
            return "";
        } else {
            return string.trim();
        }
    }

    public static String featuredlist(ArrayList<SubscriptionfeaturedList> model) {
        if (model == null) return null;
        Gson gson = new Gson();
        String json = gson.toJson(model);
        return json;
    }

    /**
     * convert from string into CartModel Arrylist
     */

    public static ArrayList<SubscriptionfeaturedList> getfeaturedlist(String value) {
        if (value == null && value.equalsIgnoreCase("")) return null;

        Type type = new TypeToken<ArrayList<SubscriptionfeaturedList>>() {
        }.getType();
        Gson gson = new Gson();

        return gson.fromJson(value, type);
    }

}