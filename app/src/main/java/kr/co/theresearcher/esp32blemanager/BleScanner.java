package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class BleScanner {

    private Context context;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner scanner;
    private ScanFilter scanFilter;
    private ScanSettings scanSettings;
    private List<ScanFilter> filters;
    private ScanCallback scanCallback;

    private boolean scanning = false;
    private OnBleScanListener listener;

    public BleScanner(Context context, OnBleScanListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public boolean readyScan() {

        if (!checkBleService()) {
            return false;
        }

        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        scanner = bluetoothAdapter.getBluetoothLeScanner();

        scanFilter = new ScanFilter.Builder()
                .setDeviceName("SpiroKit-E")
                .setDeviceAddress("3C:61:05:11:CC:A2")
                .build();

        scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();

        filters = new ArrayList<>();
        filters.add(scanFilter);

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                listener.onScan(result);

            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };

        return true;

    }

    public void startScan() {

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        if (!scanning) {
            scanner.startScan(filters, scanSettings, scanCallback);
            scanning = true;
        }


    }

    public void stopScan() {

        if (scanning) {
            scanner.stopScan(scanCallback);
            scanning = false;
        }

    }

    public boolean isScanning() {
        return scanning;
    }

    public void enableBluetoothLe() {

        if ((bluetoothAdapter != null) && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

    }

    public void disableBluetoothLe() {

        if ((bluetoothAdapter != null) && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }

    }

    private boolean checkBleService() {

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        return true;

    }



}
