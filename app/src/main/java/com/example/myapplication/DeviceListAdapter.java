package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private List<DeviceModel> deviceList;

    DeviceListAdapter(List<DeviceModel> deviceList){
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_devices, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText("Name: " + deviceList.get(position).getName());
        holder.address.setText("Address: " + deviceList.get(position).getAddress());
        holder.isPaired.setText(deviceList.get(position).isPaired() ? "Paired: Yes" : "Paired: No");
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, address, isPaired;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            isPaired = itemView.findViewById(R.id.paired);
        }
    }
}
