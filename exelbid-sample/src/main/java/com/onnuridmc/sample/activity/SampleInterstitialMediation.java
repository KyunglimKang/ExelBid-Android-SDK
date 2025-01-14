package com.onnuridmc.sample.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onnuridmc.exelbid.ExelBid;
import com.onnuridmc.exelbid.ExelBidInterstitial;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.exelbid.common.OnMediationOrderResultListener;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationOrderResult;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationType;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

public class SampleInterstitialMediation extends SampleBase implements View.OnClickListener {

    private Button loadBtn;
    private Button showBtn;
    EditText mEdtAdUnit;

    // Exelbid
    private ExelBidInterstitial exelbidInterstitial;
    private MediationOrderResult mediationOrderResult;
    private MediationType currentMediationType;

    // AdMob
    private InterstitialAd adMobInterstitialAd;

    //FaceBook
    private com.facebook.ads.InterstitialAd fanInterstitialAd;
    private com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig fanLoadAdConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_interstitial);

        TextView titleTv = findViewById(R.id.tv);
        titleTv.setText(TAG);
        loadBtn = findViewById(R.id.load_btn);
        showBtn = findViewById(R.id.show_btn);
        loadBtn.setOnClickListener(this);
        showBtn.setOnClickListener(this);
        showBtn.setEnabled(false);

        mEdtAdUnit = findViewById(R.id.unit_id);
        mEdtAdUnit.setText(PrefManager.getPref(this, PrefManager.KEY_INTERSTIAL_AD, UNIT_ID_EXELBID_INTERSTITTIAL));

        logText = findViewById(R.id.log_txt);
        logText.setMovementMethod(new ScrollingMovementMethod());
        logsb = new StringBuffer("------------- LOG -------------\n");

        /********************************************************************************
         * Exelbid 설정
         *******************************************************************************/
        exelbidInterstitial = new ExelBidInterstitial(this, mEdtAdUnit.getText().toString());
        exelbidInterstitial.setInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialLoaded() {
                printLog("Exelbid","Loaded");
                // Exelbid는 여기서 노출
                exelbidInterstitial.show();
            }

            @Override
            public void onInterstitialShow() {
            }

            @Override
            public void onInterstitialDismiss() {
            }

            @Override
            public void onInterstitialClicked() {
            }

            @Override
            public void onInterstitialFailed(ExelBidError errorCode) {
                printLog("Exelbid","Fail : " + errorCode.getErrorMessage());
                loadMediation();
            }
        });

        /********************************************************************************
         * AdMob 설정
         *******************************************************************************/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                printLog("ADMOB","onInitializationComplete initializationStatus : " + initializationStatus.toString());
            }
        });
        AdListener adListener = new AdListener() {
            @Override
            public void onAdClosed() {
                printLog("ADMOB","onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                printLog("ADMOB","onAdFailedToLoad : " + loadAdError.toString());
                loadMediation();
            }

            @Override
            public void onAdOpened() {
                printLog("ADMOB","onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                if (adMobInterstitialAd == null || !adMobInterstitialAd.isLoaded()) {
                    printLog("ADMOB","onAdLoaded - Ad did not load");
                    loadMediation();
                } else {
                    printLog("ADMOB","onAdLoaded");
                    showBtn.setEnabled(true);
                }
            }

            @Override
            public void onAdClicked() {
                printLog("ADMOB","onAdClicked");
            }

            @Override
            public void onAdImpression() {
                printLog("ADMOB","onAdImpression");
            }
        };

        adMobInterstitialAd = new InterstitialAd(this);
        adMobInterstitialAd.setAdUnitId(UNIT_ID_ADMOB_INTERSTITTIAL);
        adMobInterstitialAd.setAdListener(adListener);


        /********************************************************************************
         * FAN 설정
         *******************************************************************************/
        fanInterstitialAd = new com.facebook.ads.InterstitialAd(this, UNIT_ID_FAN_INTERSTITTIAL);
        fanLoadAdConfig = fanInterstitialAd
                .buildLoadAdConfig()
                .withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        fanInterstitialAd.destroy();
                        fanInterstitialAd = null;
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        printLog("FAN","Fail : " + adError.getErrorMessage());
                        loadMediation();
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        if (fanInterstitialAd == null
                                || ad != fanInterstitialAd
                                || !fanInterstitialAd.isAdLoaded()
                                || fanInterstitialAd.isAdInvalidated()) {
                            // Ad not ready to show.
                            printLog("FAN","Fail - Ad not loaded. Click load to request an ad.");
                            loadMediation();
                        } else {
                            // Ad was loaded, show it!
                            printLog("FAN","Loaded");
                            showBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                })
                .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
                .build();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.load_btn) {
            showBtn.setEnabled(false);
            mediationOrderResult = null;

            ArrayList<MediationType> mediationUseList = new ArrayList(Arrays.asList(MediationType.ADMOB, MediationType.FAN));
            ExelBid.getMediationData(SampleInterstitialMediation.this, mEdtAdUnit.getText().toString(), mediationUseList,
                    new OnMediationOrderResultListener() {
                        @Override
                        public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                            printLog("Mediation","onMediationOrderResult");
                            if(mediationOrderResult != null && mediationOrderResult.getSize() > 0) {
                                SampleInterstitialMediation.this.mediationOrderResult = mediationOrderResult;
                                loadMediation();
                            }
                        }

                        @Override
                        public void onMediationFail(int errorCode, String errorMsg) {
                            printLog("Mediation","onMediationFail :: " + errorMsg + "(" + errorCode + ")");
                        }
                    });
            printLog("Mediation","Request...");
        } else if(v.getId() == R.id.show_btn) {
            showMediation();
        }
    }

    @SuppressLint("MissingPermission")
    private void loadMediation() {

        if(mediationOrderResult == null) {
            return;
        }

        currentMediationType = mediationOrderResult.poll();
        if(currentMediationType != null) {
            if (currentMediationType.equals(MediationType.EXELBID)) {
                exelbidInterstitial.load();
                printLog("Exelbid","Request...");
            } else if (currentMediationType.equals(MediationType.ADMOB)) {
                if (!adMobInterstitialAd.isLoading() && !adMobInterstitialAd.isLoaded()) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    adMobInterstitialAd.loadAd(adRequest);
                    printLog("ADMOB","Request...");
                }

            } else if (currentMediationType.equals(MediationType.FAN)) {
                fanInterstitialAd.loadAd(fanLoadAdConfig);
                printLog("FAN","Request...");
            }
        }
    }

    private void showMediation() {
        if(currentMediationType != null) {
            if (currentMediationType.equals(MediationType.ADMOB)) {
                if (adMobInterstitialAd != null && adMobInterstitialAd.isLoaded()) {
                    printLog("ADMOB","Show");
                    adMobInterstitialAd.show();
                }
            } else if (currentMediationType.equals(MediationType.FAN)) {
                printLog("FAN","Show");
                if(fanInterstitialAd != null) {
                    fanInterstitialAd.show();
                }
            }
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if(exelbidInterstitial != null) {
            exelbidInterstitial.destroy();
        }
        super.onDestroy();
    }
}