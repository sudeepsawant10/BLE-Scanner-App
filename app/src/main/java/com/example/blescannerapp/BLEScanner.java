package com.example.blescannerapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BLEScanner {
    MainActivity mainActivity;
    long scanPeriod;
    int signalStrength;

    private BluetoothAdapter bluetoothAdapter;
    private boolean scanning;
    private Handler handler;


    public BLEScanner(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        this.mainActivity = mainActivity;
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;
        final BluetoothManager bluetoothManager = (BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();

    }

    public boolean isScanning() {
        return scanning;
    }

    public void start() {
        if (!Utils.checkBluetooth(bluetoothAdapter)){
            Utils.requestUserBluetooth(mainActivity);
            mainActivity.stopScan();
        } else {
            scanLeDevice(true);
        }
    }
    public void stop() {
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable && !scanning) {
            Utils.showToast(mainActivity.context, "Starting BLE Scan...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallBack);
                    mainActivity.stopScan();
                }
            }, scanPeriod);

            scanning = true;
            bluetoothAdapter.startLeScan(leScanCallBack);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {
            final int newRssi = rssi;
            if (rssi > signalStrength){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addDevice(bluetoothDevice, newRssi);
                    }
                });
            }
        }
    };

}
