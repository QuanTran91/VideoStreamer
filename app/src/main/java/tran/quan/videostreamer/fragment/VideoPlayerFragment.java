package tran.quan.videostreamer.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import tran.quan.videostreamer.R;
import tran.quan.videostreamer.model.CameraViewItem;
import tran.quan.videostreamer.service.fcm.MyFirebaseMessagingService;
import tran.quan.videostreamer.service.fcm.webservice.servicemodel.SensorEvent;
import tran.quan.videostreamer.service.fcm.webservice.servicemodel.SensorEventType;

public class VideoPlayerFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = "MediaPlayer";
    private CameraViewItem cameraViewItem;
    private io.vov.vitamio.widget.VideoView mPreview;
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder holder;
    private String m_Path;
    private Bundle extras;
    //
    private TextView mSensorTemperature;
    private TextView mSensorAirQuality;
    private TextView mSensorHumidity;
    private TextView mSensorMotionDetection;

    //
    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private ProgressDialog progressDialog;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyFirebaseMessagingService.INTENT_ACTION)){
                String result = intent.getExtras().getString(MyFirebaseMessagingService.FCM_EXTRA);
                Gson gson = new Gson();
                SensorEvent sensorEvent = gson.fromJson(result, SensorEvent.class);
                if(cameraViewItem == null || !sensorEvent.getCameraid().equals(cameraViewItem.getCameraId())){
                    return;
                }

                switch (sensorEvent.getEventType()){
                    case SensorEventType.AIRQUALITY:
                        UpdateAirQuality(sensorEvent.getValue());
                        break;
                    case SensorEventType.TEMPERATURE:
                        UpdateTemperature(sensorEvent.getValue());
                        break;
                    case SensorEventType.HUMIDITY:
                        UpdateHumidity(sensorEvent.getValue());
                        break;
                    case SensorEventType.MOTION_DETECTION:
                        UpdateMotionDetection(sensorEvent.getValue());
                        break;
                }
            }
        }
    };

    private void UpdateMotionDetection(double value) {
        if(mSensorMotionDetection ==null){
            return;
        }

        if(value !=0){
            mSensorMotionDetection.setText("Motion detected");
        }else{
            mSensorMotionDetection.setText("");
        }
    }

    private void UpdateHumidity(double value) {
        if(mSensorHumidity == null){
            return;
        }

        mSensorHumidity.setText(String.valueOf(value)+"%");
    }

    private void UpdateTemperature(double value) {
        if(mSensorTemperature == null){
            return;
        }

        mSensorTemperature.setText(String.valueOf(value)+"°C");
    }

    private void UpdateAirQuality(double value) {
        if(mSensorAirQuality == null){
            return;
        }

        mSensorAirQuality.setText(String.valueOf(value)+"%");
    }

    public VideoPlayerFragment() {
    }

    public void updateCameraItem(CameraViewItem item) {
        cameraViewItem = item;
    }

    public static VideoPlayerFragment newInstance(CameraViewItem item) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragment.updateCameraItem(item);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(getActivity()))
            return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        mPreview = (io.vov.vitamio.widget.VideoView) view.findViewById(R.id.surface);
        m_Path = cameraViewItem.getUrl();
        mSensorHumidity = (TextView)view.findViewById(R.id.sensor_humidity);
        mSensorTemperature = (TextView)view.findViewById(R.id.sensor_temperature);
        mSensorAirQuality = (TextView)view.findViewById(R.id.sensor_air_quality);
        mSensorMotionDetection = (TextView)view.findViewById(R.id.sensor_motion_detection);


        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/videostreamer");
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyFirebaseMessagingService.INTENT_ACTION);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this.getContext());
        bm.registerReceiver(broadcastReceiver, filter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        doCleanUp();
        String path = cameraViewItem.getUrl();
        if (path == "") {
            return;
        }

        PlayVideo(path);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releaseMediaPlayer();
        doCleanUp();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/videostreamer");
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    public void onClick(View v) {

    }
    private void PlayVideo(String path) {
        mMediaPlayer = new MediaPlayer(getActivity());
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);
        progressDialog.setTitle("Loading "+ percent + "%");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion called");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.d(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.d(TAG, "startVideoPlayback");
        progressDialog.dismiss();
        getActivity().setTitle("Camera : "+cameraViewItem.getCameraName());
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
