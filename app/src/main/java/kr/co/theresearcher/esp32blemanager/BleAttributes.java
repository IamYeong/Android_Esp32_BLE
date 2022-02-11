package kr.co.theresearcher.esp32blemanager;

import java.util.UUID;

public class BleAttributes {

    public static final String ACTION_GATT_CONNECTED = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.SERVICES.DISCOVERED";
    public static final String ACTION_GATT_CONNECTING = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.CONNECTING";
    public static final String ACTION_GATT_DISCONNECTING = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.DISCONNECTING";
    public static final String ACTION_GATT_AVAILABLE_DATA = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.DATA";
    public static final String EXTRA_GATT_DATA = "kr.co.theresearcher.esp32blemanager.INTENT.EXTRA.DATA";
    public static final String ACTION_WRITE_DESCRIPTOR = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.WRITE.DESCRIPTOR";
    public static final String ACTION_READ_DESCRIPTOR = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.READ.DESCRIPTOR";
    public static final String ACTION_WRITE_CHARACTERISTIC = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.WRITE.CHARACTERISTIC";
    public static final String ACTION_READ_CHARACTERISTIC = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.READ.CHARACTERISTIC";
    public static final String ACTION_CHARACTERISTIC_CHANGE = "kr.co.theresearcher.esp32blemanager.INTENT.ACTION.GATT.CHARACTERISTIC.CHANGE";

    public static final UUID ESP32_TX_CHARACTERISTIC_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID ESP32_RX_CHARACTERISTIC_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID ESP32_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID ESP32_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

}
