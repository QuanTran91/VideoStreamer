package tran.quan.videostreamer.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import tran.quan.videostreamer.R;
import tran.quan.videostreamer.model.CameraViewItem;

public class VideoPlayerFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = "MediaPlayer";
    private CameraViewItem cameraViewItem;
    private io.vov.vitamio.widget.VideoView mPreview;
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    // private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private ProgressDialog progressDialog;

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
        TextView cameraName = (TextView) view.findViewById(R.id.camera_name);
        TextView cameraurl = (TextView) view.findViewById(R.id.camera_url);
        cameraName.setText(cameraViewItem.getCameraName());
        cameraurl.setText(cameraViewItem.getUrl());

        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        progressDialog = new ProgressDialog(getActivity());
        //progressDialog.show();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        //  mMediaPlayer.getMetadata();
        //setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        doCleanUp();
        String path = cameraViewItem.getUrl();
        if (path == "") {
            // Tell the user to provide a media file URL.
            Toast.makeText(getActivity(), "Please edit MediaPlayerDemo_Video Activity," + " and set the path variable to your media file URL.", Toast.LENGTH_LONG).show();
            return;
        }
        PlayVideo(path);
        progressDialog.dismiss();
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
