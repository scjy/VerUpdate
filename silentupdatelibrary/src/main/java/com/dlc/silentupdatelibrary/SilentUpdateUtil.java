package com.dlc.silentupdatelibrary;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * Created by Administrator on 2018\9\11 0011.
 */

public class SilentUpdateUtil {
    private static SilentUpdateUtil instance;
    private boolean isUpdate;
    private String TAG = "spm";

    public static SilentUpdateUtil getInstance() {
        if (instance == null) {
            instance = new SilentUpdateUtil();
        }
        return instance;
    }

//    public boolean isUpdate() {
//        return isUpdate;
//    }

    void setUpdate(boolean update) {
        isUpdate = update;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (silentUpdateListener != null && isUpdate) {
                    silentUpdateListener.onUpdateSuccess();
                }
            }
        }, 1000);

    }

    /**
     * 安装apk
     *
     * @param path apk文件路径
     */
    public void installAPK(String path) {
        Log.i(TAG, "installAPK:" + path);
        ShellUtils utils = new ShellUtils();
        if (utils.isRoot()) {
            utils.run("pm install -r " + path, 60 * 1000);
        } else {
            spm("没有root");
        }

    }

    /**
     * 下载并安装apk
     *
     * @param url 下载路径
     */
    public void downloadAndInstall(String url) {
        Log.i(TAG, "downloadAndInstall:" + url);
        OkGo.<File>get(url).execute(new FileCallback("") {

            @Override
            public void onSuccess(Response<File> response) {
                File file = response.body();
                Log.i(TAG, "onSuccess:" + file.getPath());
                installAPK(file.getPath());
            }

            @Override
            public void uploadProgress(Progress progress) {
                Log.i(TAG, "uploadProgress:" + progress.fraction);
            }

            @Override
            public void onError(Response<File> response) {
                Log.i(TAG, "onError:" + response.getException().toString());
                if (silentUpdateListener != null) {
                    silentUpdateListener.onUpdateFail();
                }
            }
        });
    }

    public void downloadAndInstallThroughCache(Context context, String url) {
        Log.i(TAG, "downloadAndInstallThroughCache:" + url);
        Log.i(TAG, "apk下载路径:" + context.getExternalCacheDir().getPath());
        OkGo.<File>get(url).execute(new FileCallback(context.getExternalCacheDir().getPath(), "") {

            @Override
            public void onSuccess(Response<File> response) {
                File file = response.body();
                Log.i(TAG, "onSuccess:" + file.getPath());
                installAPK(file.getPath());
            }

            @Override
            public void uploadProgress(Progress progress) {
                Log.i(TAG, "uploadProgress:" + progress.fraction);
            }

            @Override
            public void onError(Response<File> response) {
                Log.i(TAG, "onError:" + response.getException().toString());
                if (silentUpdateListener != null) {
                    silentUpdateListener.onUpdateFail();
                }
            }
        });
    }

    private SilentUpdateListener silentUpdateListener;

    public void setSilentUpdateListener(SilentUpdateListener silentUpdateListener) {
        this.silentUpdateListener = silentUpdateListener;
    }

    private void spm(String msg) {
        Log.i("spm", msg);
    }
}
