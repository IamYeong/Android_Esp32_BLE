package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import java.util.List;

public interface OnSelectCharacteristicListener {
    void onSelectCharacteristic(List<BluetoothGattCharacteristic> characteristics);
}
