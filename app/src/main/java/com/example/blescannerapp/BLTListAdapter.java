package com.example.blescannerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BLTListAdapter extends ArrayAdapter<BTLE_Device> {

    Activity activity;
    int layoutResourseId;
    ArrayList<BTLE_Device> devices;

    public BLTListAdapter(@NonNull Context context, int resource, Activity activity, int layoutResourseId, ArrayList<BTLE_Device> devices) {
        super(context, resource, devices);
        this.activity = activity;
        this.layoutResourseId = layoutResourseId;
        this.devices = devices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourseId, parent, false);

        }

        BTLE_Device device = devices.get(position);
        String name = device.getName();
        String address= device.getAddress();
        int rssi = device.getRessi();

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        if (tvName != null && name.length()>0){
            tvName.setText(device.getName());
        } else {
            tvName.setText("No Name");
        }

        TextView tvRssi = (TextView) convertView.findViewById(R.id.tvRssi);
        tvRssi.setText("RSSI: "+ Integer.toString(rssi));

        TextView tvMacAddr = (TextView) convertView.findViewById(R.id.tvMacAddr);
        if (address!=null && address.length()>0){
            tvMacAddr.setText(device.getAddress());
        } else {
            tvMacAddr.setText("No Address");
        }


        return convertView;
    }
}
