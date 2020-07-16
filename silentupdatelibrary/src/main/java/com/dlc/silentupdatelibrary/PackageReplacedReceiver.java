package com.dlc.silentupdatelibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2018\9\11 0011.
 */

public class PackageReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
//            spm("onReceive PACKAGE_REPLACED："+intent.getDataString());
            if (intent.getDataString().contains(context.getPackageName())) {
                spm("from my app");
                SilentUpdateUtil.getInstance().setUpdate(true);
            }else {
                spm("not from my app");
            }
//            Toast.makeText(context,"升级了一个安装包",Toast.LENGTH_SHORT).show();
//            startActivity(context);
        }
    }





    private void spm(String msg) {
        Log.d("spm", msg);
    }
}
