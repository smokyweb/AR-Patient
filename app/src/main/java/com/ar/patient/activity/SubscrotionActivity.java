package com.ar.patient.activity;

import static com.android.billingclient.api.BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_FULL_PRICE;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivitySubscrotionBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.InAppCreateResponse;
import com.ar.patient.responsemodel.SubscriptionfeaturedList;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscrotionActivity extends BaseActivity implements View.OnClickListener {

    public static final String SKU_MONTLY_SILVER = "com.ar.patient.silver";
    public static final String SKU_MONTLY_GOLD = "com.ar.patient.gold";
    BillingClient billingClient;
    SkuDetails skuDetailsMonthlySilver, skuDetailsMonthlyGold;
    ArrayList<String> purchasedList = new ArrayList<>();
    ArrayList<SubscriptionfeaturedList> list = new ArrayList<>();
    PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
            showProgress();
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null && purchases.size() > 0) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else
                hideProgress();
        }
    };
    private ActivitySubscrotionBinding binding;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscrotion);

        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        showProgress();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add(SKU_MONTLY_SILVER);
                    skuList.add(SKU_MONTLY_GOLD);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    hideProgress();
                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null && skuDetailsList.size() > 0) {

                                        Collections.reverse(skuDetailsList);

                                        if (skuDetailsList.size() > 0 && skuDetailsList.get(0) != null) {
                                            skuDetailsMonthlySilver = skuDetailsList.get(0);
                                            Log.d("mytag", "::::skuDetailsMonthly Silver::::" + skuDetailsMonthlySilver.toString());
                                        }

                                        if (skuDetailsList.size() > 1 && skuDetailsList.get(1) != null) {
                                            skuDetailsMonthlyGold = skuDetailsList.get(1);
                                            Log.d("mytag", "::::skuDetailsMonthly Gold::::" + skuDetailsMonthlyGold.toString());
                                        }
                                        setData();
                                    }
                                }
                            });

                    billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, new PurchasesResponseListener() {
                        @Override
                        public void onQueryPurchasesResponse(@NonNull @NotNull BillingResult billingResult, @NonNull @NotNull List<Purchase> list) {
                            Log.d("mytag", ":::::onQueryPurchasesResponse::::" + billingResult.getResponseCode());
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                purchasedList.clear();
                                for (Purchase purchase : list) {
                                    Log.d("mytag", "::::Purchase::::" + purchase.getOriginalJson());
                                    purchasedList.add(purchase.getOriginalJson());
                                }
                            }
                        }
                    });


                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
                        @Override
                        public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
                            Log.d("mytag", "::::onPurchaseHistoryResponse::::" + billingResult.getResponseCode());
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                for (PurchaseHistoryRecord purchaseHistoryRecord : list) {
                                    Log.d("mytag", "::::PurchaseHistoryRecord::::" + purchaseHistoryRecord.getOriginalJson());
                                }
                            }
                        }
                    });

                } else {
                    hideProgress();
//                    showMessageAlert(billingResult.getDebugMessage());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //showMessageAlert("::::onBillingServiceDisconnected:::::");
            }
        });

        binding.btnSilverPlan.setOnClickListener(this);
        binding.btnGoldPlan.setOnClickListener(this);
        binding.btnManageSubscription.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);

        list = new ArrayList<SubscriptionfeaturedList>(Utils.getfeaturedlist(Pref.getValue(SubscrotionActivity.this, Config.FEATURE_LIST, "")));
        if (list.size() > 0) {
            //binding.txtexpiremsg.setVisibility(View.GONE);
            if (view == null) {
                for (int i = 0; i < list.size(); i++) {
                    view = LayoutInflater.from(this).inflate(R.layout.row_subscription_point, null);
                    TextView txtPoint = view.findViewById(R.id.points);
                    txtPoint.setText(list.get(i).getPosttitle());
                    txtPoint.setTextColor(Color.BLACK);
                    txtPoint.setTextSize(12);
                    binding.llpoints.addView(view);
                }
            }

        }


    }

    private void setData() {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                if (Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_IS_SUBSCRIPTION, "").equalsIgnoreCase("1")) {
                    binding.btnManageSubscription.setVisibility(View.VISIBLE);
                    binding.tvdescription.setText(skuDetailsMonthlySilver.getDescription());
                    if (Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_BUNDLE, "").equalsIgnoreCase(SKU_MONTLY_SILVER)) {
                        binding.tvrightsilverprice.setText(skuDetailsMonthlySilver.getPrice());

                        binding.tvprice.setText("Renewal Date: " + Utils.convertDate(Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_EX_DATE, "")
                                , Config.SERVER_FULL_FORMAT, "MMM dd,yyyy"));
                        binding.tvprice.setTextSize(12);
                        binding.tvplanname.setText(skuDetailsMonthlySilver.getTitle().substring(0, skuDetailsMonthlySilver.getTitle().indexOf("(") - 1));
                        binding.btnSilverPlan.setVisibility(View.GONE);
                        binding.linSilver.setBackground(getResources().getDrawable(R.drawable.plans_corner_curved_white_bg));

                        binding.tvpricegolden.setText(skuDetailsMonthlyGold.getPrice());
                        binding.tvrightgoldprice.setText(skuDetailsMonthlyGold.getPrice());
                        binding.tvpricegolden.setTextSize(12);
                        binding.tvrightgoldprice.setTextSize(12);
                        binding.tvplannamegolden.setText(skuDetailsMonthlyGold.getTitle().substring(0, skuDetailsMonthlyGold.getTitle().indexOf("(") - 1));
                        binding.linGold.setBackground(getResources().getDrawable(R.drawable.plans_corner_curved_bg));
                        binding.btnGoldPlan.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvrightgoldprice.setText(skuDetailsMonthlyGold.getPrice());
                        binding.tvpricegolden.setText("Renewal Date: " + Utils.convertDate(Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_EX_DATE, "")
                                , Config.SERVER_FULL_FORMAT, "MMM dd,yyyy"));

                        binding.tvpricegolden.setTextSize(12);
                        binding.tvplannamegolden.setText(skuDetailsMonthlyGold.getTitle().substring(0, skuDetailsMonthlyGold.getTitle().indexOf("(") - 1));
                        binding.btnGoldPlan.setVisibility(View.GONE);
                        binding.linGold.setBackground(getResources().getDrawable(R.drawable.plans_corner_curved_white_bg));

                        binding.tvprice.setText(skuDetailsMonthlySilver.getPrice());
                        binding.tvrightsilverprice.setText(skuDetailsMonthlySilver.getPrice());
                        binding.tvplanname.setText(skuDetailsMonthlySilver.getTitle().substring(0, skuDetailsMonthlySilver.getTitle().indexOf("(") - 1));
                        binding.linSilver.setBackground(getResources().getDrawable(R.drawable.plans_corner_curved_bg));
                        binding.btnSilverPlan.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.btnManageSubscription.setVisibility(View.GONE);

                    binding.tvprice.setText(skuDetailsMonthlySilver.getPrice());
                    binding.tvprice.setTextSize(12);
                    binding.tvrightsilverprice.setText(skuDetailsMonthlySilver.getPrice());
                    binding.tvrightsilverprice.setTextSize(12);
                    binding.tvplanname.setText(skuDetailsMonthlySilver.getTitle().substring(0, skuDetailsMonthlySilver.getTitle().indexOf("(") - 1));
                    binding.linSilver.setBackground(getResources().getDrawable(R.drawable.plans_corner_curved_bg));
                    binding.btnSilverPlan.setVisibility(View.VISIBLE);

                    binding.tvpricegolden.setText(skuDetailsMonthlyGold.getPrice());
                    binding.tvpricegolden.setTextSize(12);
                    binding.tvrightgoldprice.setText(skuDetailsMonthlyGold.getPrice());
                    binding.tvrightgoldprice.setTextSize(12);
                    binding.tvplannamegolden.setText(skuDetailsMonthlyGold.getTitle().substring(0, skuDetailsMonthlyGold.getTitle().indexOf("(") - 1));
                    binding.linGold.setBackground(getResources().getDrawable(R.drawable.plans_corner_curved_bg));
                    binding.btnGoldPlan.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    void handlePurchase(Purchase purchase) {
        Log.d("mytag", ":::::::::::::handlePurchase::::::::" + purchase.toString());
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        Log.d("mytag", ":::onAcknowledgePurchaseResponse:::" + billingResult.getResponseCode());

                        /*if (!isOnline(false))
                            return;*/

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(purchase.getPurchaseTime());
                        if (purchase.getSkus().get(0).equalsIgnoreCase(SKU_MONTLY_SILVER))
                            calendar.add(Calendar.MONTH, 1);
                        else
                            calendar.add(Calendar.MONTH, 1);


                        ARPatientApplication.getRestClient().getApiService().inappCreate(
                                Utils.getDateFromMilliSecond(calendar.getTimeInMillis()),
                                purchase.getSkus().get(0),
                                Utils.getDateFromMilliSecond(purchase.getPurchaseTime()),
                                purchase.getOrderId(),
                                purchase.getPurchaseToken(), "0",
                                purchase.getSkus().get(0).equalsIgnoreCase(SKU_MONTLY_SILVER) ? Config.PLAN_TYPE_SILVER : Config.PLAN_TYPE_GOLD)
                                .enqueue(new Callback<InAppCreateResponse>() {
                                    @Override
                                    public void onResponse(Call<InAppCreateResponse> call, Response<InAppCreateResponse> response) {
                                        hideProgress();

                                        Pref.setValue(SubscrotionActivity.this, Config.PREF_KEY_IS_SUBSCRIPTION, response.body().getIsSubscribed());
                                        Pref.setValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_EX_DATE, response.body().getExpDate());
                                        Pref.setValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_TOKEN, purchase.getPurchaseToken());
                                        Pref.setValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_BUNDLE,
                                                purchase.getSkus().get(0).equalsIgnoreCase(SKU_MONTLY_SILVER) ? SKU_MONTLY_SILVER : SKU_MONTLY_GOLD);

                                        showSingleButtonDialog(SubscrotionActivity.this, response.body().getMessage(), new DialogClickListener() {
                                            @Override
                                            public void onClick() {
                                                setResult(RESULT_OK);
                                                finish();
                                                backAnimation();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(Call<InAppCreateResponse> call, Throwable t) {

                                    }
                                });

                    }
                });
            } else {
                Log.d("mytag", "::::::::::::isAcknowledged::::::::::" + purchase.isAcknowledged());
            }
        } else {
            Log.d("mytag", "::::::::::::purchase.getPurchaseState()::::::::::" + purchase.getPurchaseState());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                backAnimation();
                break;
            case R.id.btnSilverPlan:
                if (skuDetailsMonthlySilver == null)
                    return;

                for (String s : purchasedList) {
                    try {
                        Log.d("mytag", "S value : " + s);
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("productId").equalsIgnoreCase(SKU_MONTLY_SILVER)) {
                            if (jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))) {
                                //already you have purchased monthly plan
                                showValidationDialog(SubscrotionActivity.this, getResources().getString(R.string.user_already_purchased_plan), null);

                            } else {
                                //already another user purchased monthly plan
                                showValidationDialog(SubscrotionActivity.this, getResources().getString(R.string.another_user_already_purchased_plan), null);
                            }
                            return;
                        } else if (jsonObject.getString("productId").equalsIgnoreCase(SKU_MONTLY_GOLD) && !jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))) {
                            //already another user purchased yearly plan
                            showValidationDialog(SubscrotionActivity.this, getResources().getString(R.string.another_user_already_purchased_plan), null);
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               /* if (!isOnline(false))
                    return;*/

                if (Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_IS_SUBSCRIPTION, "").equalsIgnoreCase("1") &&
                        !Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_TOKEN, "").isEmpty()) {
                    showValidationDialog(SubscrotionActivity.this, getString(R.string.downgrade_message), null);
                } else {
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsMonthlySilver)
                            .setObfuscatedAccountId(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))
                            .build();
                    int code = billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                    Log.d("mytag", ":::::launchBillingFlow:::::" + code);
                }
                break;
            case R.id.btnGoldPlan:
                if (skuDetailsMonthlyGold == null)
                    return;

                for (String s : purchasedList) {
                    try {
                        Log.d("mytag", "S Gold value : " + s);
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("productId").equalsIgnoreCase(SKU_MONTLY_GOLD)) {
                            if (jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))) {
                                showValidationDialog(SubscrotionActivity.this, getResources().getString(R.string.user_already_purchased_plan), null);
                            } else {
                                showValidationDialog(SubscrotionActivity.this, getResources().getString(R.string.another_user_already_purchased_plan), null);
                            }
                            return;
                        } else if (jsonObject.getString("productId").equalsIgnoreCase(SKU_MONTLY_SILVER) &&
                                !jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))) {
                            showValidationDialog(SubscrotionActivity.this, getResources().getString(R.string.another_user_already_purchased_plan), null);
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               /* if (!isOnline(false))
                    return;*/

                if (Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_IS_SUBSCRIPTION, "").equalsIgnoreCase("1") &&
                        !Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_TOKEN, "").isEmpty()) {
                    if (Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_PLATFORM, "").equalsIgnoreCase("IOS")) {
                        showValidationDialog(SubscrotionActivity.this, getString(R.string.subscription_other_platform_monthly), null);
                    } else {
                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                .setSubscriptionUpdateParams(BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                                        .setOldSkuPurchaseToken(Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_TOKEN, ""))
                                        .setReplaceSkusProrationMode(IMMEDIATE_AND_CHARGE_FULL_PRICE).build())
                                .setObfuscatedAccountId(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))
                                .setSkuDetails(skuDetailsMonthlyGold)
                                .build();
                        int code = billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                        Log.d("mytag", ":::::launchBillingFlow:::::" + code);
                    }

                } else {
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsMonthlyGold)
                            .setObfuscatedAccountId(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))
                            .build();
                    int code = billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                    Log.d("mytag", ":::::launchBillingFlow:::::" + code);
                }
                break;
            case R.id.btnManageSubscription:
                for (String s : purchasedList) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(Pref.getValue(SubscrotionActivity.this, Config.PREF_USERID, ""))
                                || Pref.getValue(SubscrotionActivity.this, Config.PREF_KEY_SUBSCRIPTION_PLATFORM, "").equalsIgnoreCase("1")) {

                            try {

                                startActivityForResult(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/account/subscriptions?sku=" + SKU_MONTLY_SILVER + "&package=" + getPackageName())), Config.CODE_CANCEL_SUBSCRIPTION);
                            } catch (ActivityNotFoundException e) {
                                showValidationDialog(SubscrotionActivity.this, "Cant open the browser", null);
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                break;
        }
    }
}