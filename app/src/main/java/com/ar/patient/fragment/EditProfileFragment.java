package com.ar.patient.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.BuildConfig;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseFragment;
import com.ar.patient.databinding.FragmentEditProfileBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.ChangeAvatar;
import com.ar.patient.responsemodel.ChangeProfileResponse;
import com.ar.patient.responsemodel.myprofile.MyProfileResponse;
import com.ar.patient.uc.ActionSheet;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener, ActionSheet.ActionSheetListener {

    private FragmentEditProfileBinding binding;
    private MyProfileResponse myProfileResponse;
    private File selectedFile = null;
    private String isPublic = "0";

    public EditProfileFragment(MyProfileResponse myProfileResponse) {
        this.myProfileResponse = myProfileResponse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);

        initviews();
        return binding.getRoot();
    }


    private void initviews() {

        binding.EdtFullname.setText(myProfileResponse.getName());
        binding.EdtEmail.setText(Pref.getValue(getActivity(), Config.PREF_EMAIL, ""));

        Utils.loadImage(getActivity(), Pref.getValue(getActivity(), Config.PREF_AVATAR, ""), R.drawable.avtar, binding.ImgUser);

        isPublic = Pref.getValue(getActivity(), Config.PREF_IS_PUBLIC, "");
        if (Pref.getValue(getActivity(), Config.PREF_IS_PUBLIC, "").equalsIgnoreCase("1")) {
            binding.ChkOtherUser.setChecked(true);
        } else {
            binding.ChkOtherUser.setChecked(false);
        }


        binding.imgBack.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
        binding.imgSmallCam.setOnClickListener(this);
        binding.ChkOtherUser.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                getActivity().onBackPressed();
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
                    showValidationDialog(getActivity(), getString(R.string.fullname_blank_alert), null);
                } else if (binding.EdtEmail.getText().toString().isEmpty()) {
                    showValidationDialog(getActivity(), getString(R.string.email_blank_alert), null);
                } else if (!com.ar.patient.uc.Utils.isValidEmailId(binding.EdtEmail.getText().toString())) {
                    showValidationDialog(getActivity(), getString(R.string.email_alert), null);
                } else {
                    callUpdateProfile();
                }
                break;
        }
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
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
            File file = new File(getActivity().getExternalCacheDir().getPath(), "profile.png");
            Uri apkURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file);
            Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);
            intentPicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intentPicture, Config.CODE_REQUEST_PERMISSION_CAMERA);
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
        if (requestCode == Config.CODE_REQUEST_PERMISSION_CAMERA && resultCode == RESULT_OK) {
            File file = new File(getActivity().getExternalCacheDir().getPath(), "profile.png");
            Uri uri = Uri.fromFile(file);
            performCrop(uri);
        } else if (requestCode == Config.CODE_REQUEST_PERMISSION_STORAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    String file = Utils.getPathFromUri(getActivity(), uri);
                    performCrop(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String file = resultUri.getPath();
                selectedFile = new File(file);
                changeavatar();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void performCrop(Uri imageUri) {
        CropImage.activity(imageUri).
                setCropMenuCropButtonTitle("Done")
                .setAspectRatio(1, 1)
                .setMinCropResultSize(Config.IMAGE_WIDTH,
                        Config.IMAGE_HEIGHT)
                .setMaxCropResultSize(Config.IMAGE_WIDTH,
                        Config.IMAGE_HEIGHT)
                .start(getActivity());
    }

    public void callUpdateProfile() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(getActivity(), Config.PREF_USERID, ""))
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
                        showSingleButtonDialog(getActivity(), "Profile has been updated successfully.", new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Pref.setValue(getActivity(), Config.PREF_NAME, response.body().getName());
                                Pref.setValue(getActivity(), Config.PREF_IS_PUBLIC, response.body().getIsPublic());
                                Pref.setValue(getActivity(), Config.PREF_EMAIL, response.body().getEmail());
                                getActivity().onBackPressed();
                                backAnimation();
                            }
                        });
                    } else {
                        showValidationDialog(getActivity(), response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(getActivity(), response.body().getMessage(), null);
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
                Utils.getrequestBodyFromString("" + Pref.getValue(getActivity(), Config.PREF_USERID, "")), filePart
        ).enqueue(new Callback<ChangeAvatar>() {
            @Override
            public void onResponse(Call<ChangeAvatar> call, Response<ChangeAvatar> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        showSingleButtonDialog(getActivity(), response.body().getMessage(), new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Utils.loadImage(getActivity(), response.body().getAvatar(), R.drawable.avtar, binding.ImgUser);
                                Pref.setValue(getActivity(), Config.PREF_AVATAR, response.body().getAvatar());
                            }
                        });
                    } else {
                        showValidationDialog(getActivity(), response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(getActivity(), response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<ChangeAvatar> call, Throwable t) {

            }
        });

    }
}