package com.example.blescannerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    Context context = this;
    private final static String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_ENABLE_BT = 1;
    private HashMap<String, BTLE_Device> btleDeviceHashMap;
    private ArrayList<BTLE_Device> btleDeviceArrayList;
    private BLTListAdapter bltListAdapter;

    private Button btnScan;
    private BTStateBroadcastReceiver btStateBroadcastReceiver;
    private BLEScanner bLEScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.showToast(context, "BLE not supported");
            finish();
        }

        btStateBroadcastReceiver = new BTStateBroadcastReceiver(context);
        bLEScanner = new BLEScanner(this, 7500, -75);

        btleDeviceHashMap = new HashMap<>();
        btleDeviceArrayList = new ArrayList<>();

//        bltListAdapter = new BLTListAdapter(context, R.layout.btle_device_list_item, btleDeviceArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(bltListAdapter);
        listView.setOnItemClickListener(this);
        // ((ScrollView)) findViewById(R.id.scrollView)).addView(listView);

        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(btStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(btStateBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                Utils.showToast(context, "Scan Button Pressed");
                if (!bLEScanner.isScanning()) {
                    startScan();
                } else {
                    stopScan();;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (requestCode == RESULT_OK) {

            } else if (requestCode == RESULT_CANCELED) {
                Utils.showToast(context, "Please turn on Bluetooth");
            }
        }
    }

    public void addDevice(BluetoothDevice device, int newRssi){
        // get device mac address
        String address = device.getAddress();
        if (!btleDeviceHashMap.containsKey(address)) {
            BTLE_Device btleDevice = new BTLE_Device(device);
            btleDevice.setRessi(newRssi);
            btleDeviceHashMap.put(address, btleDevice);
            btleDeviceArrayList.add(btleDevice);
        } else {
            btleDeviceHashMap.get(address).setRessi(newRssi);
        }
        bltListAdapter.notifyDataSetChanged();
    }

    public void stopScan(){
        btnScan.setText("Scan Again!");
        bLEScanner.stop();;
    }
     public void startScan(){
         btnScan.setText("Scanning...");
         btleDeviceArrayList.clear();;
         btleDeviceHashMap.clear();

         bltListAdapter.notifyDataSetChanged();;
         bLEScanner.start();
    }

}