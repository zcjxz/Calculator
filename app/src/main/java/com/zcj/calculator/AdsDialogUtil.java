package com.zcj.calculator;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdsDialogUtil {
    public interface AdsDialogListener{
        void onYesPress(Dialog dialog);
        void onMorePress(Dialog dialog);
        void onNoPress(Dialog dialog);
    }
    public static void showAdsDialog(Context context, final AdsDialogListener listener){
        //填充布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.layout_dialog_exit, null,false);
        //创建广告
        AdView adView = (AdView) dialogView.findViewById(R.id.ad_view);
        setBannerAds(adView);
        //创建dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.MyDialogStyle);
        builder.setCancelable(false);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        //设置大小
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        dialog.getWindow().setAttributes(layoutParams);
        //设置监听
        Button btnYes = (Button) dialogView.findViewById(R.id.bt_yes);
        Button btnMore = (Button) dialogView.findViewById(R.id.bt_more);
        Button btnNo = (Button) dialogView.findViewById(R.id.bt_no);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onYesPress(dialog);
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMorePress(dialog);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoPress(dialog);
            }
        });
    }
    public static void setBannerAds(AdView adView){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("73957908AF204D3C3BD6DD4DA2BD36F4")//红米4测试码
//                .addTestDevice("46E4E6B0DD6C71F38DC6F64A53BEAC0D")//华为测试码
//                .addTestDevice("00324B61CF9CF3A064D03C379CA05E5F")//联想测试码
                .build();
        adView.loadAd(adRequest);
    }
}
