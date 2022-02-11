package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.le.ScanResult;

/**
 * Activity 와 Scanner 를 분리하기 위해 Scanner 객체와 Activity간의 소통을 담당할 Listener.
 */
public interface OnBleScanListener {
    void onScan(ScanResult result);
}
