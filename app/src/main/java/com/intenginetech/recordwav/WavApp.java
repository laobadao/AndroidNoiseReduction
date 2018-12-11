package com.intenginetech.recordwav;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.File;

@ReportsCrashes(
        mode = ReportingInteractionMode.DIALOG,
        mailTo = "jjzhao@intenginetech.com",
        resToastText = R.string.crash_toast_text,
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = R.drawable.ic_launcher,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        // resDialogTheme = R.style.AppTheme_Dialog,
        resDialogOkToast = R.string.crash_dialog_ok_toast
)
public class WavApp extends Application {
    private static WavApp app;

    public static String rootPath = "/wav_file/";
    public static String reductionPath = "/reduction_file/";

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();

        ACRA.init(this);
        initPath();
    }

    /**
     * 初始化存储路径
     */
    private void initPath() {
        String ROOT = "";// /storage/emulated/0
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            ROOT = getBaseContext().getFilesDir().getPath();
            ROOT = Environment.getExternalStorageDirectory().getPath();
            Log.e("app", "系统方法：" + ROOT);
        }
        rootPath = ROOT + rootPath;
        reductionPath = ROOT + reductionPath;
        File lrcFile = new File(rootPath);
        if (!lrcFile.exists()) {
            lrcFile.mkdirs();
        }

        File reductionPathFile = new File(reductionPath);
        if (!reductionPathFile.exists()) {
            reductionPathFile.mkdirs();
        }
    }


    public static WavApp app() {
        return app;
    }


}
