package com.ar.patient.fancygifdialoglib;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ar.patient.R;


/**
 * Created by Shashank Singhal on 06/01/2018.
 */

public class FancyGifDialog {
    public static class Builder {
        int gifImageResource;
        private String title, message, positiveBtnText, negativeBtnText, pBtnColor, nBtnColor;
        private Activity activity;
        private FancyGifDialogListener pListener, nListener;
        private boolean cancel;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveBtnText(String positiveBtnText) {
            this.positiveBtnText = positiveBtnText;
            return this;
        }

        public Builder setPositiveBtnBackground(String pBtnColor) {
            this.pBtnColor = pBtnColor;
            return this;
        }


        public Builder setNegativeBtnText(String negativeBtnText) {
            this.negativeBtnText = negativeBtnText;
            return this;
        }

        public Builder setNegativeBtnBackground(String nBtnColor) {
            this.nBtnColor = nBtnColor;
            return this;
        }

        //set Positive listener
        public Builder OnPositiveClicked(FancyGifDialogListener pListener) {
            this.pListener = pListener;
            return this;
        }

        //set Negative listener
        public Builder OnNegativeClicked(FancyGifDialogListener nListener) {
            this.nListener = nListener;
            return this;
        }

        public Builder isCancellable(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setGifResource(int gifImageResource) {
            this.gifImageResource = gifImageResource;
            return this;
        }

        public FancyGifDialog build() {
            TextView message1, title1;
            Button nBtn, pBtn;
            View viewone;
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.fancygifdialog);


            //getting resources
            title1 = dialog.findViewById(R.id.title);
            message1 = dialog.findViewById(R.id.message);
            nBtn = dialog.findViewById(R.id.negativeBtn);
            pBtn = dialog.findViewById(R.id.positiveBtn);
            viewone = dialog.findViewById(R.id.viewone);

            title1.setText(title);
            message1.setText(message);


            if (positiveBtnText != null) {
                pBtn.setText(positiveBtnText);
                viewone.setVisibility(View.GONE);
                if (pBtnColor != null) {
//                    GradientDrawable bgShape = (GradientDrawable) pBtn.getBackground();
                    pBtn.setTextColor(Color.parseColor(pBtnColor));
                }

                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pListener != null) pListener.OnClick();
                        dialog.dismiss();
                    }

                });
            } else {
                pBtn.setVisibility(View.GONE);
            }
            if (negativeBtnText != null) {
                nBtn.setText(negativeBtnText);
                viewone.setVisibility(View.VISIBLE);
                nBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (nListener != null) nListener.OnClick();
                        dialog.dismiss();
                    }
                });
                if (nBtnColor != null) {
//                    GradientDrawable bgShape = (GradientDrawable) nBtn.getBackground();
                    nBtn.setTextColor(Color.parseColor(nBtnColor));
                }
            } else {
                nBtn.setVisibility(View.GONE);
            }

            dialog.show();

            return new FancyGifDialog();

        }
    }

}
