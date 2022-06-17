/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ar.patient.custom_progress;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ar.patient.R;


public class MyProgressBar {

    private ProgressDialog mProgressDialog;
    private float mDimAmount;
    private int mWindowColor;
    private float mCornerRadius;
    private Context mContext;

    private int mAnimateSpeed;

    private int mMaxProgress;
    private boolean mIsAutoDismiss;

    private int mGraceTimeMs;
    private Handler mGraceTimer;
    private boolean mFinished;

    public MyProgressBar(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        mDimAmount = 0;
        mWindowColor = context.getResources().getColor(R.color.kprogresshud_default_color);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
        mGraceTimeMs = 0;
        mFinished = false;

        View view = new SpinView(mContext);
        mProgressDialog.setView(view);
        mProgressDialog.setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    public static MyProgressBar create(Context context) {
        return new MyProgressBar(context);
    }


    public MyProgressBar setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            mDimAmount = dimAmount;
        }
        return this;
    }


    @Deprecated
    public MyProgressBar setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }

    public MyProgressBar setBackgroundColor(int color) {
        mWindowColor = color;
        return this;
    }

    public MyProgressBar setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    public MyProgressBar setAnimationSpeed(int scale) {
        mAnimateSpeed = scale;
        return this;
    }


    public MyProgressBar setLabel(String label) {
        mProgressDialog.setLabel(label);
        return this;
    }


    public MyProgressBar setLabel(String label, int color) {
        mProgressDialog.setLabel(label, color);
        return this;
    }

    public MyProgressBar setDetailsLabel(String detailsLabel) {
        mProgressDialog.setDetailsLabel(detailsLabel);
        return this;
    }


    public MyProgressBar setDetailsLabel(String detailsLabel, int color) {
        mProgressDialog.setDetailsLabel(detailsLabel, color);
        return this;
    }


    public MyProgressBar setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

    public void setProgress(int progress) {
        mProgressDialog.setProgress(progress);
    }

    public MyProgressBar setCustomView(View view) {
        if (view != null) {
            mProgressDialog.setView(view);
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    public MyProgressBar setCancellable(boolean isCancellable) {
        mProgressDialog.setCancelable(isCancellable);
        mProgressDialog.setOnCancelListener(null);
        return this;
    }


    public MyProgressBar setCancellable(DialogInterface.OnCancelListener listener) {
        mProgressDialog.setCancelable(null != listener);
        mProgressDialog.setOnCancelListener(listener);
        return this;
    }


    public MyProgressBar setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    public MyProgressBar setGraceTime(int graceTimeMs) {
        mGraceTimeMs = graceTimeMs;
        return this;
    }

    public MyProgressBar show() {
        if (!isShowing()) {
            mFinished = false;
            if (mGraceTimeMs == 0) {
                mProgressDialog.show();
            } else {
                mGraceTimer = new Handler();
                mGraceTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null && !mFinished) {
                            mProgressDialog.show();
                        }
                    }
                }, mGraceTimeMs);
            }
        }
        return this;
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void dismiss() {
        mFinished = true;
        if (mContext != null && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
    }

    private class ProgressDialog extends Dialog {

        private Determinate mDeterminateView;
        private CustomProgress mIndeterminateView;
        private View mView;
        private TextView mLabelText;
        private TextView mDetailsText;
        private String mLabel;
        private String mDetailsLabel;
        private FrameLayout mCustomViewContainer;
        private BackgroundLayout mBackgroundLayout;
        private int mWidth, mHeight;
        private int mLabelColor = Color.WHITE;
        private int mDetailColor = Color.WHITE;

        public ProgressDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.my_progress_bar);

            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = mDimAmount;
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);

            setCanceledOnTouchOutside(false);

            initViews();
        }

        private void initViews() {
            mBackgroundLayout = (BackgroundLayout) findViewById(R.id.background);
            mBackgroundLayout.setBaseColor(mWindowColor);
            mBackgroundLayout.setCornerRadius(mCornerRadius);
            if (mWidth != 0) {
                updateBackgroundSize();
            }

            mCustomViewContainer = (FrameLayout) findViewById(R.id.container);
            addViewToFrame(mView);

            if (mDeterminateView != null) {
                mDeterminateView.setMax(mMaxProgress);
            }
            if (mIndeterminateView != null) {
                mIndeterminateView.setAnimationSpeed(mAnimateSpeed);
            }

            mLabelText = (TextView) findViewById(R.id.label);
            setLabel(mLabel, mLabelColor);
            mDetailsText = (TextView) findViewById(R.id.details_label);
            setDetailsLabel(mDetailsLabel, mDetailColor);
        }

        private void addViewToFrame(View view) {
            if (view == null) return;
            int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
            mCustomViewContainer.addView(view, params);
        }

        private void updateBackgroundSize() {
            ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
            params.width = Helper.dpToPixel(mWidth, getContext());
            params.height = Helper.dpToPixel(mHeight, getContext());
            mBackgroundLayout.setLayoutParams(params);
        }

        public void setProgress(int progress) {
            if (mDeterminateView != null) {
                mDeterminateView.setProgress(progress);
                if (mIsAutoDismiss && progress >= mMaxProgress) {
                    dismiss();
                }
            }
        }

        public void setView(View view) {
            if (view != null) {
                if (view instanceof Determinate) {
                    mDeterminateView = (Determinate) view;
                }
                if (view instanceof CustomProgress) {
                    mIndeterminateView = (CustomProgress) view;
                }
                mView = view;
                if (isShowing()) {
                    mCustomViewContainer.removeAllViews();
                    addViewToFrame(view);
                }
            }
        }

        public void setLabel(String label) {
            mLabel = label;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            mDetailsLabel = detailsLabel;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setLabel(String label, int color) {
            mLabel = label;
            mLabelColor = color;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setTextColor(color);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel, int color) {
            mDetailsLabel = detailsLabel;
            mDetailColor = color;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setTextColor(color);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            if (mBackgroundLayout != null) {
                updateBackgroundSize();
            }
        }
    }
}
