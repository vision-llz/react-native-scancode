package com.xiaomo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaomo on 2018/9/12   永无bug
 */

public class ScancodePlugin extends ReactContextBaseJavaModule{
    private String BROADCASTNAME = "";
    private String CODENAME = "";
    String codeStr="";
    Pattern p = Pattern.compile("\\s*|\r|\n");
            /*\n 回车(\u000a)
              \t 水平制表符(\u0009)
              \s 空格(\u0008)
              \r 换行(\u000d)*/

    public ScancodePlugin(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "WmsScanCode";
    }

    /**
     *
     * @param broadcastSettingName  PDA设置的广播名称
     * @param list  设置的对象数组，对象格式为{key:"",value:""}
     */
    @ReactMethod
    public void setBroadcastSetting(String broadcastSettingName,ReadableArray list) {
        //PDA设备的设置
        if (list.size()>0){
            for(int i=0;i<list.size();i++){
                setBroadcastReceiver(broadcastSettingName,list.getMap(i));
            }
        }
    }

    @ReactMethod
    public void getCode(String broadcastName,String setCodeName){
        BROADCASTNAME=broadcastName;
        CODENAME=setCodeName;
        Log.e("list","lista .............................adadasdas");

        try{
            getReactApplicationContext().unregisterReceiver(scanReceiver);
        }catch (IllegalArgumentException e){
        }
        initBroadcastReciever();
    }

    private BroadcastReceiver scanReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCASTNAME)){
                String code=intent.getStringExtra(CODENAME);
                Matcher matcher = p.matcher(code);
                codeStr = matcher.replaceAll("");
                WritableMap params= Arguments.createMap();
                params.putString("code",codeStr);
                sendEvent(getReactApplicationContext(),"scannerCodeShow",params);
            }
        }
    };


    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params){
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName,params);
    }

    //设置PDA工具项
    private void setBroadcastReceiver(String broadcastSettingName, ReadableMap map) {
        try{
            // 发送广播到扫描工具内的应用设置项
            Intent intent = new Intent(broadcastSettingName);
            // 修改扫描工具内应用设置
            intent.putExtra(map.getString("key"), map.getString("value"));
            getReactApplicationContext().sendBroadcast(intent);
        }catch (Exception e){
            Log.e("error",e.toString());
        }
    }

    //初始化广播
    private void initBroadcastReciever() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROADCASTNAME);
        intentFilter.setPriority(Integer.MAX_VALUE);
        getReactApplicationContext().registerReceiver(scanReceiver,intentFilter);
    }

    /**
     * 初始化广播接收器，AUTOID系列安卓产品上的系统软件扫描工具相对应
     */
//	private void initBroadcastReciever() {
//		// 发送广播到扫描工具内的应用设置项
//		Intent intent = new Intent("com.android.scanner.service_settings");
//		// 修改扫描工具内应用设置中的开发者项下的广播名称
//		// intent.putExtra(BroadcastConfig.BROADCAST_KEY, BroadcastConfig.CUSTOM_NAME);
//		// 修改扫描工具内应用设置下的条码发送方式为 "广播"
//		intent.putExtra("barcode_send_mode", "BROADCAST");
//		 // 修改扫描工具内应用设置下的结束符为 "NONE"
//		// intent.putExtra(BroadcastConfig.END_KEY, "NONE");
//		getReactApplicationContext().sendBroadcast(intent);
//	}

}
