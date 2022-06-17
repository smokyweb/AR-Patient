package com.ar.patient.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityDownloadingPatientModelBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.patientsresponse.PatientsListResponse;
import com.ar.patient.responsemodel.patientsresponse.Type;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadingPatientModelActivity extends BaseActivity {

    File myDir;
    private PowerManager.WakeLock mWakeLock;
    private ActivityDownloadingPatientModelBinding binding;
    private String frontImage, backImage;
    private Type bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downloading_patient_model);

        if (getIntent() != null) {
            frontImage = getIntent().getStringExtra("front_image");
            backImage = getIntent().getStringExtra("back_image");
            bean = (Type) getIntent().getSerializableExtra("Type");
            if (checkStoragePermission()) {
                downloadingModel();
            }
        }

        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
    }

    private void downloadingModel() {
        myDir = new File(this.getExternalCacheDir() + Config.MAINFOLDERNAME);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        new DownloadFileFromURL(frontImage).execute();
        new DownloadFileFromURL(backImage).execute();

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

                    showDialog(DownloadingPatientModelActivity.this, getString(R.string.str_permission_deny), new DialogClickListener() {
                        @Override
                        public void onClick() {
                            redirectSetting(DownloadingPatientModelActivity.this);
                        }
                    });
                }
            } else {
                downloadingModel();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        String downloadurl;

        public DownloadFileFromURL(String url) {
            this.downloadurl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            Log.d("mytag", "Values[0]" + "" + values[0]);
            binding.progressBar.setProgress(values[0]);
            binding.txtResult.setText("" + values[0] + "%");
        }

        @Override
        protected String doInBackground(String... f_url) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                int count;
                Log.d("mytag", "Downloaded url : " + downloadurl);
                URL url = new URL(downloadurl);
                URLConnection conection = url.openConnection();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                conection.connect();

                int lenghtOfFile = conection.getContentLength();
                conection.setConnectTimeout(5 * 60000);
                conection.setReadTimeout(5 * 60000);

                input = connection.getInputStream();

                output = new FileOutputStream(myDir + "/" + Utils.getFileNameFromURL(downloadurl));

//                output = new FileOutputStream(myDir);

                byte[] data = new byte[4096];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;


                    if (lenghtOfFile > 0) {
                        publishProgress((int) (total * 100 / lenghtOfFile));
                        output.write(data, 0, count);
                    }

                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException ignored) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Download error: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Toast.makeText(BaseActivity.this, "Download error: " + "", Toast.LENGTH_SHORT).show();
                }
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Download error: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(BaseActivity.this, "Download error: " + "", Toast.LENGTH_SHORT).show();
                }
                if (connection != null)
                    connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                mWakeLock.release();
                if (file_url != null) {
                    Log.d("mytag", "error : ");
                }
                Intent exam = new Intent(DownloadingPatientModelActivity.this, PatientExamActivity.class);
                exam.putExtra("Type", bean);
                exam.putExtra("BodyPartsArrayList", getIntent().getSerializableExtra("BodyPartsArrayList"));
                startActivity(exam);
                finish();
                startAnimation();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("mytag", "error : ");
            }
        }

    }
}