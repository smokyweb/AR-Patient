package com.ar.patient.baseclass;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ar.patient.R;
import com.ar.patient.custom_progress.MyProgressBar;
import com.ar.patient.fancygifdialoglib.FancyGifDialog;
import com.ar.patient.fancygifdialoglib.FancyGifDialogListener;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Utils;


public class BaseActivity extends AppCompatActivity {

    private MyProgressBar myProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGlobalLayoutListener();
    }

    private void setGlobalLayoutListener() {
        final View layout = findViewById(Window.ID_ANDROID_CONTENT);
        ViewTreeObserver observer = layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onGlobalLayoutCompleted();
            }
        });
    }

    public void redirectSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Config.CODE_REQUEST_PERMISSION_SETTING);
        startAnimation();
    }

    /**
     * Give a chance to obtain view layout attributes when the
     * content view layout process is completed.
     * Some layout attributes will be available here but not
     * in onCreate(), like measured width/height.
     * This callback will be called ONLY ONCE before the whole
     * window content is ready to be displayed for first time.
     */
    protected void onGlobalLayoutCompleted() {

    }

    private int getStatusBarHeight() {
        int id = getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        return id > 0 ? getResources().getDimensionPixelSize(id) : id;
    }


    public void startAnimation() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void backAnimation() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public String tag() {
        return getClass().getSimpleName();
    }

    public void log(String message) {
        Log.d(tag(), message);
    }

    public void toast(final String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline() {
        return isOnline(true);
    }

    public boolean isOnline(boolean ShowProgress) {
        if (Utils.isOnline(this)) {
            if (ShowProgress)
                showProgress();
            return true;
        } else {
            if (ShowProgress)
                showValidationDialog(this, this.getString(R.string.msg_no_internet), null);
            return false;
        }
    }

    public void hideKeyBord(AppCompatActivity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress() {
        if (myProgressBar == null) {
            myProgressBar = MyProgressBar.create(this);
            myProgressBar.setCancellable(false);
            myProgressBar.show();
        }

        if (!myProgressBar.isShowing()) {
            myProgressBar.show();
        }
    }

    public void hideProgress() {
        if (myProgressBar != null) {
            if (myProgressBar.isShowing()) {
                myProgressBar.dismiss();
            }
        }
    }

    public void showSingleButtonDialog(final Context context, String msg,
                                       final DialogClickListener dialogClickListener) {
        new FancyGifDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveBtnBackground("#0F2F6C")
                .setPositiveBtnText("Ok")
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        if (dialogClickListener != null) {
                            dialogClickListener.onClick();
                        }
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        if (dialogClickListener != null) {
                            dialogClickListener.onClick();
                        }
                    }
                })
                .build();

    }

    public void showSingleButtonDialogNoRecord(AppCompatActivity activity, String msg, final DialogClickListener dialogClickListener) {
        new FancyGifDialog.Builder(activity)
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveBtnBackground("#0F2F6C")
                .setPositiveBtnText("Ok")
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        onBackPressed();
                    }
                })
                .build();

    }

    public void showValidationDialog(final Context context, String msg,
                                     final DialogClickListener dialogClickListener) {
        new FancyGifDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveBtnBackground("#0375F3")
                .setPositiveBtnText("OK")
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        if (dialogClickListener != null) {
                            dialogClickListener.onClick();
                        }
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    public void showDialog(final Context context, String msg,
                           final DialogClickListener dialogClickListener) {
        new FancyGifDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveBtnBackground("#0375F3")
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground("#000000")
                .setNegativeBtnText("No")
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        if (dialogClickListener != null) {
                            dialogClickListener.onClick();
                        }
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean checkCalenderPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, Config.CODE_REQUEST_PERMISSION_CALENDER);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkCameraStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Config.CODE_REQUEST_PERMISSION_CAMERA);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Config.CODE_REQUEST_PERMISSION_CAMERA);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, Config.CODE_REQUEST_PERMISSION_RECORD_AUDIO);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean checkAgoraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, Config.CODE_REQUEST_PERMISSION_CAMERA);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void permissionResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, PermissoinsResult permissoinsResult) {
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    Utils.logPrint("denied" + permission);
                    somePermissionsForeverDenied = true;
                } else {
                    if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                        Utils.logPrint("allowed" + permission);
                    } else {
                        Utils.logPrint("set to never ask again" + permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {

                showSingleButtonDialog(activity, getString(R.string.str_permission_deny), new DialogClickListener() {
                    @Override
                    public void onClick() {
                        redirectSetting(activity);
                    }
                });
            }
        } else {
            permissoinsResult.onReturn(requestCode);
        }
    }

    public interface PermissoinsResult {
        void onReturn(int requestcode);
    }

    public interface DialogClickListener {
        void onClick();
    }

}
