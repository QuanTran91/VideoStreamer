package tran.quan.videostreamer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.provider.MediaStore;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.*;
import io.vov.vitamio.Vitamio;

public class MainActivity extends AppCompatActivity {
    private String pathToFileOrUrl = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private io.vov.vitamio.widget.VideoView mVideoView;
    private EditText m_VideoUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        SetupCameraSdk();
        setContentView(R.layout.activity_main);
        m_VideoUrlEditText = (EditText) findViewById(R.id.urlText);
        m_VideoUrlEditText.setText(pathToFileOrUrl);
        mVideoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.surface_view);

        if (pathToFileOrUrl == "") {
            Toast.makeText(this, "Please set the video path for your media file", Toast.LENGTH_LONG).show();
            return;
        } else {
             mVideoView.setVideoPath(pathToFileOrUrl);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();
            mVideoView.setKeepScreenOn(true);
        }
    }

    private void SetupCameraSdk() {
        //set the video cache path
//        if (DeviceUtils.isZte()) {
//            if(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).exists()) {
//
////                        Vitamio.setVideoCachePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/VCameraDemo/");
//            } else {
//                VCamera.setVideoCachePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath().replace("/sdcard/", "/sdcard-ext/") +"/Camera/VCameraDemo/");
//            }
//        } else {
//            VCamera.setVideoCachePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/VCameraDemo/");
//        }
////open log output,FFmpeg output into logcat
//        VCamera.setDebugMode(true);
////initializing VCamera SDK is essential
//        VCamera.initialize(this);
    }

    public void startPlay(View view) {
        pathToFileOrUrl = m_VideoUrlEditText.getText().toString();
        if (!TextUtils.isEmpty(pathToFileOrUrl)) {
            mVideoView.setVideoPath(pathToFileOrUrl);
        }
    }
}
