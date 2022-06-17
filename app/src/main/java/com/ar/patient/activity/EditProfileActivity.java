package com.ar.patient.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.FragmentEditProfileBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.ChangeAvatar;
import com.ar.patient.responsemodel.ChangeProfileResponse;
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

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, ActionSheet.ActionSheetListener {

    private FragmentEditProfileBinding binding;
    private File selectedFile = null;
    private String isPublic = "0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_edit_profile);

        initviews();
    }


    private void initviews() {

        binding.EdtFullname.setText(Pref.getValue(EditProfileActivity.this, Config.PREF_NAME, ""));
        binding.EdtEmail.setText(Pref.getValue(EditProfileActivity.this, Config.PREF_EMAIL, ""));

        Utils.loadImage(EditProfileActivity.this, Pref.getValue(EditProfileActivity.this, Config.PREF_AVATAR, ""), R.drawable.avtar, binding.ImgUser);

        isPublic = Pref.getValue(EditProfileActivity.this, Config.PREF_IS_PUBLIC, "");
        if (Pref.getValue(EditProfileActivity.this, Config.PREF_IS_PUBLIC, "").equalsIgnoreCase("1")) {
            binding.ChkOtherUser.setChecked(true);
        } else {
            binding.ChkOtherUser.setChecked(false);
        }


        binding.imgBack.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
        binding.imgSmallCam.setOnClickListener(this);
        binding.ChkOtherUser.setOnClickListener(this);

        setTheme(R.style.ActionSheetStyleiOS7);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
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
            case R.id.btnUpdate:
                if (binding.EdtFullname.getText().toString().isEmpty()) {
                    showValidationDialog(EditProfileActivity.this, getString(R.string.fullname_blank_alert), null);
                } else if (binding.EdtEmail.getText().toString().isEmpty()) {
                    showValidationDialog(EditProfileActivity.this, getString(R.string.email_blank_alert), null);
                } else if (!com.ar.patient.uc.Utils.isValidEmailId(binding.EdtEmail.getText().toString())) {
                    showValidationDialog(EditProfileActivity.this, getString(R.string.email_alert), null);
                } else {
                    callUpdateProfile();
                }
                break;
        }
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(EditProfileActivity.this, EditProfileActivity.this.getSupportFragmentManager())
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                Utils.loadImageWithoutMediaUrl(EditProfileActivity.this, selectedFile.getAbsolutePath(), R.drawable.avtar, binding.ImgUser);
                changeavatar();
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
                        Utils.loadImageWithoutMediaUrl(EditProfileActivity.this, selectedFile.getAbsolutePath(), R.drawable.avtar, binding.ImgUser);
                        changeavatar();
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
        int ivWidth = binding.ImgUser.getWidth();
        int ivHeight = binding.ImgUser.getHeight();


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
                .start(EditProfileActivity.this);
    }

    public void callUpdateProfile() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(EditProfileActivity.this, Config.PREF_USERID, ""))
                .addFormDataPart("name", "" + binding.EdtFullname.getText().toString())
                .addFormDataPart("email", "" + binding.EdtEmail.getText().toString())
                .addFormDataPart("is_public", "" + isPublic)
                .build();


        ARPatientApplication.getRestClient().getApiService().chnageprofile(requestBody).enqueue(new Callback<ChangeProfileResponse>() {
            @Override
            public void onResponse(Call<ChangeProfileResponse> call, Response<ChangeProfileResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        showSingleButtonDialog(EditProfileActivity.this, getString(R.string.profile_update_sucess_msg), new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Pref.setValue(EditProfileActivity.this, Config.PREF_NAME, response.body().getName());
                                Pref.setValue(EditProfileActivity.this, Config.PREF_IS_PUBLIC, response.body().getIsPublic());
                                Pref.setValue(EditProfileActivity.this, Config.PREF_EMAIL, response.body().getEmail());
                                finish();
                                backAnimation();
                            }
                        });
                    } else {
                        showValidationDialog(EditProfileActivity.this, response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(EditProfileActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<ChangeProfileResponse> call, Throwable t) {

            }
        });

    }

    public void changeavatar() {
        if (!isOnline())
            return;


        MultipartBody.Part filePart;
        filePart = MultipartBody.Part.createFormData("avatar", selectedFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), selectedFile));

        ARPatientApplication.getRestClient().getApiService().changeavatar(
                Utils.getrequestBodyFromString("" + Pref.getValue(EditProfileActivity.this, Config.PREF_USERID, "")), filePart
        ).enqueue(new Callback<ChangeAvatar>() {
            @Override
            public void onResponse(Call<ChangeAvatar> call, Response<ChangeAvatar> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        showSingleButtonDialog(EditProfileActivity.this, response.body().getMessage(), new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Utils.loadImage(EditProfileActivity.this, response.body().getAvatar(), R.drawable.avtar, binding.ImgUser);
                                Pref.setValue(EditProfileActivity.this, Config.PREF_AVATAR, response.body().getAvatar());
                            }
                        });
                    } else {
                        showValidationDialog(EditProfileActivity.this, response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(EditProfileActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<ChangeAvatar> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
    }
}