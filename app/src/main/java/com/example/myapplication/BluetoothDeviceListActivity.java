package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BluetoothDeviceListActivity extends AppCompatActivity {

    public static final String tag = "BluetoothAdapter";
    private static final int REQUEST_ENABLE_BT = 1;
    List<DeviceModel> allDevices = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    List<String> pairedDeviceAddresses = new ArrayList<>();
    RecyclerView listView;
    DeviceListAdapter adapter;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceModel deviceModel = new DeviceModel();
                deviceModel.setName(device.getName() == null ? "Not available" : device.getName());
                deviceModel.setAddress(device.getAddress());
                deviceModel.setPaired(pairedDeviceAddresses.contains(device.getAddress()));
                allDevices.add(deviceModel);

                if (listView.getVisibility() != View.VISIBLE)
                    listView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();

                Log.i(tag, "device name: " + device.getName());
                Log.i(tag, "device address: " + device.getAddress());
                Log.i(tag, "all device size: " + allDevices.size());
                Log.i(tag, "======================================================");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_list);

        //Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        listView = findViewById(R.id.deviceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
//        temporaryTestDevices();
        adapter = new DeviceListAdapter(allDevices);
        listView.setAdapter(adapter);

        if (bluetoothAdapter != null) {
            Log.i(tag, "Bluetooth available");
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                bluetoothAdapter.startDiscovery();
            }

        } else {
            Log.i(tag, "Bluetooth available");
        }

    }

    private void temporaryTestDevices() {
        DeviceModel nameValue = new DeviceModel();
        nameValue.setName("Test name");
        nameValue.setAddress("Test address");

        allDevices.add(nameValue);
        allDevices.add(nameValue);
        allDevices.add(nameValue);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            getPairedDevices();
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceAddresses.add(device.getAddress());
            }
        }
    }
}
