package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public interface OnSelectCharacteristicListener {
    void onSelectCharacteristic(BluetoothGattCharacteristic characteristic);
}
