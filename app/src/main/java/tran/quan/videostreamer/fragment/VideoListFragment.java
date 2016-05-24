package tran.quan.videostreamer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tran.quan.videostreamer.R;
import tran.quan.videostreamer.activity.AddOrUpdateCameraActivity;
import tran.quan.videostreamer.adapter.CameraItemAdapter;
import tran.quan.videostreamer.business.DatabaseHandler;
import tran.quan.videostreamer.interfaces.VideoListFragmentSelectedItemListener;
import tran.quan.videostreamer.model.CameraViewItem;
import tran.quan.videostreamer.service.fcm.webservice.CameraService;
import tran.quan.videostreamer.service.fcm.webservice.servicemodel.Camera;
import tran.quan.videostreamer.viewlistener.RecyclerItemClickListener;

public class VideoListFragment extends Fragment {

    public static String WebApiAddress = "http://192.168.1.4:64015/";
    RecyclerView cameraItemsList;
    CameraItemAdapter adapter;
    private VideoListFragmentSelectedItemListener mListener;

    public VideoListFragment() {
        // Required empty public constructor
    }

    public static VideoListFragment newInstance() {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("List of cameras");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        cameraItemsList = (RecyclerView) view.findViewById(R.id.camera_items);
        List<CameraViewItem> items = DatabaseHandler.INSTANCE.getCameraList();
        adapter = new CameraItemAdapter(items);
        cameraItemsList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        cameraItemsList.setItemAnimator(new DefaultItemAnimator());
        cameraItemsList.setAdapter(adapter);
        cameraItemsList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PlayItem(position);
            }
        }));

        adapter.notifyDataSetChanged();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpCamera(null);
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebApiAddress).addConverterFactory(GsonConverterFactory.create()).build();
        CameraService cameraService = retrofit.create(CameraService.class);
        Call<List<Camera>> call = cameraService.getCamera();
        call.enqueue(new Callback<List<Camera>>() {
            @Override
            public void onResponse(Call<List<Camera>> call, Response<List<Camera>> response) {
                    UpdateCamera(response.body());
            }

            @Override
            public void onFailure(Call<List<Camera>> call, Throwable t) {

            }
        });
        return view;
    }

    private void UpdateCamera(List<Camera> body) {
        List<CameraViewItem> items = new ArrayList<>();

        for (Camera item:body) {
            CameraViewItem cameraItem = new CameraViewItem();
            cameraItem.setCameraId(item.getId());
            cameraItem.setCameraName(item.getName());
            cameraItem.setUrl(item.getVideoUrl());
            items.add(cameraItem);
            DatabaseHandler.INSTANCE.addCamera(cameraItem);
        }

        adapter = new CameraItemAdapter(items);
        cameraItemsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void PlayItem(int position){
        mListener.OnPlayItem(adapter.getCameraItem(position));
    }

    private void addOrUpCamera(CameraViewItem item){
        Intent intent = new Intent(getActivity(), AddOrUpdateCameraActivity.class);
        if(item != null){
            Bundle bundle = new Bundle();
            bundle.putLong("cameraId",item.getId());
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        updateCameraList();
        super.onResume();
    }

    private void updateCameraList() {
        adapter.UpdateCameraList(DatabaseHandler.INSTANCE.getCameraList());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof VideoListFragmentSelectedItemListener) {
            mListener = (VideoListFragmentSelectedItemListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String action = item.getTitle().toString();
        CameraViewItem cameraViewItem = adapter.getCameraItem(itemId);

        if ("Delete".equals(action)) {
             DatabaseHandler.INSTANCE.deleteCamera(cameraViewItem);
        }
        else if("Edit".equals(action)){
            addOrUpCamera(cameraViewItem);
        }

        updateCameraList();
        return super.onContextItemSelected(item);
    }
}
