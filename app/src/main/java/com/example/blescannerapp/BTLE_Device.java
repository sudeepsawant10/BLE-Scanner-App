package com.example.blescannerapp;

import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class BTLE_Device {
    private BluetoothDevice bluetoothDevice;
    private int ressi;

    public BTLE_Device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        return bluetoothDevice.getName();
    }

    public int getRessi() {
        return ressi;
    }

    public void setRessi(int ressi) {
        this.ressi = ressi;
    }
}
