package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.le.ScanResult;

public interface OnDeviceSelectListener {
    void onSelectDevice(ScanResult result);
}
