package com.ar.patient.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivitySignUpBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.DrawableClickListener;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.signupResoponse;
import com.ar.patient.uc.ActionSheet;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, ActionSheet.ActionSheetListener {

    private ActivitySignUpBinding binding;
    private File selectedFile = null;
    private String isPublic = "0";
    protected int CROP_IMAGE = 1;
    private Uri picuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        InitViews();
    }

    private void InitViews() {
        binding.tvAlreadyAccount.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.imgSmallCam.setOnClickListener(this);
        binding.ChkOtherUser.setOnClickListener(this);
        binding.ImgBack.setOnClickListener(this);
        binding.txtAgreeTermsAndCondition.setOnClickListener(this);
        setTheme(R.style.ActionSheetStyleiOS7);

        binding.EdtPassword.setTag("false");
        binding.EdtPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.EdtPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.EdtPassword.getTag().equals("true")) {
                    binding.EdtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_white, 0);
                    binding.EdtPassword.setTag("false");
                    binding.EdtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.EdtPassword.setSelection(binding.EdtPassword.length());
                } else {
                    binding.EdtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_off_white, 0);
                    binding.EdtPassword.setTag("true");
                    binding.EdtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.EdtPassword.setSelection(binding.EdtPassword.length());
                }
                return false;
            }
        });

        binding.EdtConfirmPassword.setTag("false");
        binding.EdtConfirmPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.EdtConfirmPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.EdtConfirmPassword.getTag().equals("true")) {
                    binding.EdtConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_white, 0);
                    binding.EdtConfirmPassword.setTag("false");
                    binding.EdtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.EdtConfirmPassword.setSelection(binding.EdtConfirmPassword.length());
                } else {
                    binding.EdtConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_off_white, 0);
                    binding.EdtConfirmPassword.setTag("true");
                    binding.EdtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.EdtConfirmPassword.setSelection(binding.EdtConfirmPassword.length());
                }
                return false;
            }
        });

        binding.EdtPassword.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (end == 1) {
                if (Character.isWhitespace(source.charAt(0))) {
                    return "";
                }
            }
            return null;
        }});
        binding.EdtConfirmPassword.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (end == 1) {
                if (Character.isWhitespace(source.charAt(0))) {
                    return "";
                }
            }
            return null;
        }});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImgBack:
                finish();
                backAnimation();
                break;
            case R.id.tvAlreadyAccount:
                finish();
                backAnimation();
                break;
            case R.id.ChkOtherUser:
                if (((CheckBox) view).isChecked()) {
                    isPublic = "1";
                    binding.ChkOtherUser.setChecked(true);
                    Log.e("mytag", "Is check True");
                } else {
                    isPublic = "0";
                    binding.ChkOtherUser.setChecked(false);
                    Log.e("mytag", "Is check false");
                }
                break;
            case R.id.imgSmallCam:
                if (checkCameraStoragePermission()) {
                    showActionSheet();
                }
                break;
            case R.id.btnRegister:
                if (selectedFile == null) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.image_blank), null);
                } else if (binding.EdtFullname.getText().toString().isEmpty()) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.fullname_blank_alert), null);
                } else if (binding.EdtEmail.getText().toString().isEmpty()) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.email_blank_alert), null);
                } else if (!com.ar.patient.uc.Utils.isValidEmailId(binding.EdtEmail.getText().toString())) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.email_alert), null);
                } else if (binding.EdtPassword.getText().toString().isEmpty()) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.password_blank_alert), null);
                } else if (binding.EdtPassword.getText().length() < 8) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.password_alert), null);
                } else if (binding.EdtConfirmPassword.getText().toString().isEmpty()) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.confirm_password_blank_alert), null);
                } else if (binding.EdtConfirmPassword.getText().length() < 8) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.password_alert), null);
                } else if (!binding.EdtConfirmPassword.getText().toString().equals(binding.EdtPassword.getText().toString())) {
                    showValidationDialog(SignUpActivity.this, getString(R.string.con_password_alert), null);
                } else {
                    callSignUp();
                }
                break;
            case R.id.txtAgreeTermsAndCondition:
                Intent intent = new Intent(SignUpActivity.this, TermsAndConditionActivity.class);
                intent.putExtra("Code", Config.TERMS_AND_CONDITION);
                startActivity(intent);
                startAnimation();
                break;
        }
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("Cancel")
                .setOtherButtonTitles("Camera", "Gallery")
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }


    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if (index == 0) {
            selectedFile = null;
            openCamera();
        } else {
            selectedFile = null;
            galleryClick();
        }
    }

    private void openCamera() {
        try {
          /*  File file = new File(getExternalCacheDir().getPath(), "profile.png");
            Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);
            intentPicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intentPicture, Config.CODE_REQUEST_PERMISSION_CAMERA);*/

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, Config.CODE_REQUEST_PERMISSION_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void galleryClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Config.CODE_REQUEST_PERMISSION_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.CODE_REQUEST_PERMISSION_CAMERA && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String time = String.valueOf(System.currentTimeMillis());
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + time + "_profile" + ".jpg");

            try {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                CompressResizeImage(photo).compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();
                selectedFile = file;
                Utils.loadImageWithoutMediaUrl(SignUpActivity.this, selectedFile.getAbsolutePath(), R.drawable.avtar, binding.imgImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == Config.CODE_REQUEST_PERMISSION_STORAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    String time = String.valueOf(System.currentTimeMillis());

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + time + "_profile" + ".jpg");

                    try {
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        CompressResizeImage(photo).compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        selectedFile = file;
                        Utils.loadImageWithoutMediaUrl(SignUpActivity.this, selectedFile.getAbsolutePath(), R.drawable.avtar, binding.imgImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Bitmap CompressResizeImage(Bitmap bm) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        int ivWidth = binding.imgImage.getWidth();
        int ivHeight = binding.imgImage.getHeight();


        int new_height = (int) Math.floor((double) bmHeight * ((double) ivWidth / (double) bmWidth));
        Bitmap newbitMap = Bitmap.createScaledBitmap(bm, ivWidth, new_height, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        Bitmap bm1 = BitmapFactory.decodeByteArray(b, 0, b.length);

        return bm1;
    }

    public void performCrop(Uri imageUri) {
        CropImage.activity(imageUri).
                setCropMenuCropButtonTitle("Done")
                .setAspectRatio(1, 1)
                .setMinCropResultSize(Config.IMAGE_WIDTH,
                        Config.IMAGE_HEIGHT)
                .setMaxCropResultSize(Config.IMAGE_WIDTH,
                        Config.IMAGE_HEIGHT)
                .start(this);
    }

    public void callSignUp() {
        if (!isOnline())
            return;


        MultipartBody.Part filePart;
        filePart = MultipartBody.Part.createFormData("avatar", selectedFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), selectedFile));

        ARPatientApplication.getRestClient().getApiService().Signup(
                Utils.getrequestBodyFromString("" + isPublic),
                Utils.getrequestBodyFromString("" + binding.EdtFullname.getText().toString().trim()),
                Utils.getrequestBodyFromString("" + binding.EdtEmail.getText().toString().trim()),
                Utils.getrequestBodyFromString("" + binding.EdtPassword.getText().toString().trim()), filePart
        ).enqueue(new Callback<signupResoponse>() {
            @Override
            public void onResponse(Call<signupResoponse> call, Response<signupResoponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        showValidationDialog(SignUpActivity.this, getString(R.string.registration_sucess_msg), new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Pref.setValue(SignUpActivity.this, Config.PREF_USERID, response.body().getUserId());
                                Pref.setValue(SignUpActivity.this, Config.PREF_SESSION, response.body().getSessionId());
                                Pref.setValue(SignUpActivity.this, Config.PREF_AVATAR, response.body().getAvatar());
                                Pref.setValue(SignUpActivity.this, Config.PREF_ROLE, response.body().getRole());
                                Pref.setValue(SignUpActivity.this, Config.PREF_IS_PUBLIC, response.body().getIsPublic());

                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                startAnimation();
                                finish();
                            }
                        });

                    } else {
                        showValidationDialog(SignUpActivity.this, response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(SignUpActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<signupResoponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.CODE_REQUEST_PERMISSION_CAMERA) {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        Utils.logPrint("denied" + permission);
                        somePermissionsForeverDenied = true;
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            Utils.logPrint("allowed" + permission);
                        } else {
                            Utils.logPrint("set to never ask again" + permission);
                            somePermissionsForeverDenied = true;
                        }
                    }
                }
                if (somePermissionsForeverDenied) {

                    showDialog(SignUpActivity.this, getString(R.string.str_permission_deny), new DialogClickListener() {
                        @Override
                        public void onClick() {
                            redirectSetting(SignUpActivity.this);
                        }
                    });
                }
            } else {
                showActionSheet();
            }
        }
    }
}