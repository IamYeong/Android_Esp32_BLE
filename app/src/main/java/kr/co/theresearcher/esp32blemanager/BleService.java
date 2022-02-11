package kr.co.theresearcher.esp32blemanager;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BleService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private Queue<char[]> mQueue = new LinkedList<>();
    private Thread thread;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothDevice bluetoothDevice;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (newState == BluetoothProfile.STATE_CONNECTED) {

            } else if (newState == BluetoothProfile.STATE_CONNECTING) {

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {

            } else {

            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            List<BluetoothGattService> services = gatt.getServices();

            for (BluetoothGattService service : services) {

                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {



                }

            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
            super.onServiceChanged(gatt);
        }
    };


    public BleService() {
    }

    public class LocalBinder extends Binder {
        BleService getService() {
            return BleService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        thread.interrupt();
        return mBinder;
    }

    private void startQueue() {

        thread = new Thread() {

            @Override
            public void run() {
                super.run();

                Looper.prepare();


                while(!thread.isInterrupted()) {

                    if (!mQueue.isEmpty()) {

                        char[] data = mQueue.poll();
                        if (data != null) {

                            Intent intent = new Intent(BleAttributes.ACTION_GATT_AVAILABLE_DATA);
                            intent.putExtra(BleAttributes.EXTRA_GATT_DATA, data);
                            sendBroadcast(intent);

                        }

                    }

                }


                Looper.loop();

            }
        };

    }

    public void run() {

        startQueue();

    }

    public void connect(String deviceAddress) {

        bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
        bluetoothGatt = bluetoothDevice.connectGatt(this, false, gattCallback);

        startQueue();


    }

    public void disconnect() {

        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }


    }

    public void discoverServices() {
        if (bluetoothGatt != null) {
            bluetoothGatt.discoverServices();
        }
    }

    public void close() {

        if (bluetoothGatt != null) {
            bluetoothGatt.close();
        }

    }

    public boolean initialize() {

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        return true;

    }



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {

    }

}