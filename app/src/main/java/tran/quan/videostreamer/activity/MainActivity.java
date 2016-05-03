package tran.quan.videostreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import tran.quan.videostreamer.*;
import tran.quan.videostreamer.R;
import tran.quan.videostreamer.fragment.BlankFragment;
import tran.quan.videostreamer.fragment.VideoListFragment;
import tran.quan.videostreamer.fragment.VideoPlayerFragment;
import tran.quan.videostreamer.interfaces.VideoListFragmentSelectedItemListener;
import tran.quan.videostreamer.model.CameraViewItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VideoListFragmentSelectedItemListener {
    private String pathToFileOrUrl = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private io.vov.vitamio.widget.VideoView mVideoView;
    private EditText m_VideoUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(tran.quan.videostreamer.R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.action_settings, R.string.action_settings);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, VideoListFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void OnPlayItem(final CameraViewItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        transaction.replace(R.id.flContent, VideoPlayerFragment.newInstance(item));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
