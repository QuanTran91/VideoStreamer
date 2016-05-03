package tran.quan.videostreamer.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tran.quan.videostreamer.R;
import tran.quan.videostreamer.model.CameraViewItem;

/**
 * Created by myrap_000 on 4/25/2016.
 */
public class CameraItemAdapter extends RecyclerView.Adapter<CameraItemAdapter.MyViewHolder>  {

    private List<CameraViewItem> cameraViewItemList;

    public CameraItemAdapter(List<CameraViewItem> cameraViewItemList) {
        this.cameraViewItemList = cameraViewItemList;
    }

    public void UpdateCameraList(List<CameraViewItem> cameraViewItemList){
        this.cameraViewItemList = cameraViewItemList;
        notifyDataSetChanged();
    }

    public CameraViewItem getCameraItem(int position){
        return cameraViewItemList.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CameraViewItem item = cameraViewItemList.get(position);
        holder.cameraName.setText(item.getCameraName());
        holder.url.setText(item.getUrl());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return cameraViewItemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView cameraName, url;
        public int position;
        public MyViewHolder(View itemView) {
            super(itemView);
            cameraName = (TextView) itemView.findViewById(R.id.camera_name);
            url = (TextView) itemView.findViewById(R.id.ip_address);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, position, 0, "Edit");//groupId, itemId, order, title
            menu.add(0, position, 0, "Delete");
        }
    }
}
