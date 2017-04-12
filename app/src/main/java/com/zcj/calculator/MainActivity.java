package com.zcj.calculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.guowei.guowei_general.ADSystem.MoreActivity;
import com.guowei.guowei_general.ADSystem.XMLUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;


public class MainActivity extends Activity {

    private boolean isError=false;
    private String Error="";
    private EditText et_show;
    private TextView tv_number;
    private TextView tv_calculat;
    private int index=0;
    private Editable editable;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_show = (EditText) findViewById(R.id.et_show);
        adView = (AdView) findViewById(R.id.adView);
        AdsDialogUtil.setBannerAds(adView);
        editable=et_show.getEditableText();
        et_show.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] spits = s.toString().split("\n");
                int longest = 0;
                for (int i = 0; i < spits.length; i++) {
                    if (longest < spits[i].length()) {
                        longest = spits[i].length();
                    }
                }
                if (longest>12){
                    et_show.setTextSize(35);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //5.0以上可以用"setShowSoftInputOnFocus"这条函数
        if (android.os.Build.VERSION.SDK_INT <= 10) {//3.0以下
            et_show.setInputType(InputType.TYPE_NULL);
        } else {//3.0以上
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(et_show, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void Number(View v){
        tv_number = (TextView) v;
        String number= (String) tv_number.getText();
        index=et_show.getSelectionStart();
        if (index<0||index>=editable.length()){
            editable.append(number);
        }else{
            editable.insert(index, number);
        }
    }
    public void Calculats(View v){
        tv_calculat=(TextView)v;
        String calculat= (String) tv_calculat.getText();
        index=et_show.getSelectionStart();
        String text=et_show.getText().toString();
        String before_index;
        if (et_show.length()==0){
            before_index="";
        }else{
        before_index=text.substring(index - 1, index);
        }
        if (before_index.equals("+")||
                before_index.equals("-")||
                before_index.equals("×")||
                before_index.equals("÷")){
//            if (calculat.equals("-")){
//                if (before_index.equals("-")||before_index.equals("+")){
//                    editable.replace(index-1,index,calculat);
//                }else{
//                    editable.insert(index,calculat);
//                }
//            }else
                if(calculat.equals(".")) {
            }
             else{
                editable.replace(index - 1, index, calculat);
            }
        }else if(calculat.equals(".")) {
            String[] texts=text.split("\n");
            String index_text=texts[texts.length-1];
            int now_index=index-(text.length()-texts[texts.length-1].length());
            String B_index=index_text.substring(0,now_index);
            String A_index=index_text.substring(now_index,index_text.length());
            String[] B_split = B_index.split("\\+|-|×|÷");
            String[] A_split = A_index.split("\\+|-|×|÷");
            String B_number=B_split[B_split.length-1];
            String A_number=A_split[A_split.length-1];
            String index_number=B_number+A_number;
            if (index_number.indexOf('.')!=-1){

            }else{
                editable.insert(index,".");
            }
        }
         else if (before_index.equals("")) {
            if (calculat.equals("-")){
                editable.insert(index,calculat);
            }
        }else{
            editable.insert(index,calculat);
        }
    }
    public void Back(View v){
        index=et_show.getSelectionStart();
        editable = et_show.getEditableText();
        if (index<=0){

        }else if (index<= editable.length()){
            editable.delete(index - 1, index);
        }
    }
    public void BigC(View v){
        editable.delete(0, et_show.length());
    }
    public void Equal(View v){
        String first_text=et_show.getText().toString();
        String text=first_text.replace("×-","aa");
        text=text.replace("/-","bb");
        String result=count(text);
        if (isError){
            editable.replace(0,et_show.length(),Error+"\n");
            isError=false;
        }else {
            editable.insert(et_show.getText().length(), "\n" + result);
        }
        et_show.setSelection(et_show.length());
    }
    private String count(String text){
        String[] texts=text.split("\n");
        String result;
        if (jia(texts[texts.length-1])==(int)jia(texts[texts.length-1])){
            result=(int)jia(texts[texts.length-1])+"";
        }
        else{
            result=(float)jia(texts[texts.length-1])+"";
        }
        return result;
    }
    private double jia(String text){
        String[] jia=text.split("\\+");
        double[] jias=new double[jia.length];
        for (int i = 0; i < jia.length; i++) {
            jias[i]=jian(jia[i]);
        }
        double sum=0;
        double shu=0;
        for (int i = 0; i < jias.length; i++) {
            shu=jias[i];
            sum=Arith.add(sum,shu);
        }
        return sum;
    }
    private double jian(String text){
        String[] jian=text.split("-");
        double[] jians=new double[jian.length];

        double sum;
        double shu;
        if (text.startsWith("-")){
            jians[0]=0;
            for (int i = 1; i < jian.length; i++) {
                jians[i]=chen(jian[i]);
            }
        }else {
            for (int i = 0; i < jian.length; i++) {
                jians[i]=chen(jian[i]);
            }
        }
        sum = jians[0];
        for (int i = 1; i < jians.length; i++) {
            shu=jians[i];
            sum=Arith.sub(sum,shu);
        }

        return sum;
    }
    private double chen(String text){
        String[] chen=text.split("×");
        double[] chens=new double[chen.length];

            for (int i = 0; i < chen.length; i++) {
                chens[i]=chen_fu(chen[i]);

        }
        double sum=1;
        double shu=1;
        for (int i = 0; i < chens.length; i++) {
            shu=chens[i];
            sum=Arith.mul(sum,shu);
        }
        return sum;
    }

    private  double chen_fu(String text){
        String [] fu=text.split("aa");
        double[] fus=new double[fu.length];
        for (int i = 0; i < fu.length; i++) {
            fus[i]=chu_fu(fu[i]);
        }
        double sum=fus[0];
        double shu=1;
        for (int i = 1; i < fus.length; i++) {
            shu=fus[i];
            sum=(Arith.mul(sum,shu)*(-1));
        }
        return sum;
    }

    private double chu_fu(String text){
        String [] fu=text.split("bb");
        double[] fus=new double[fu.length];
        for (int i = 0; i < fu.length; i++) {
            fus[i]=chu(fu[i]);
        }
        double sum=fus[0];
        double shu=1;
        for (int i = 1; i < fus.length; i++) {
            shu=fus[i];
            sum=(Arith.div(sum,shu)*(-1));
        }
        return sum;
    }
    private double chu(String text){
        String[] chu=text.split("÷");
        double[] chus=new double[chu.length];
        for (int i = 0; i < chu.length; i++) {
            if (chu[i].equals(".")){
                chus[i]=0;
            }else if (chu[i].equals("")){
                isError=true;
                Error=getResources().getString(R.string.content_not_be_blank);
            }else{
                try {
                chus[i] = new BigDecimal(chu[i]).doubleValue();
            }catch (Exception e){
                isError=true;
                Error=getResources().getString(R.string.can_not_have_characters);
                return 0;
            }
            }
        }
        double sum=chus[0];
        double shu=1;
        for (int i = 1; i < chus.length; i++) {
            shu=chus[i];
            if (shu!=0){
            sum=Arith.div(sum,shu);
            }else {
                isError=true;
                Error="除数不能为零";
                return 0;
            }
        }
        return sum;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        AdsDialogUtil.showAdsDialog(this, new AdsDialogUtil.AdsDialogListener() {
            @Override
            public void onYesPress(Dialog dialog) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onMorePress(Dialog dialog) {
                MainActivity.this.startActivity(
                        new Intent(MainActivity.this,MoreActivity.class)
                                .putExtra(
                                        "values",(Serializable) XMLUtils.getAppBeans(MainActivity.this)
                                )
                );
            }

            @Override
            public void onNoPress(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }
}
