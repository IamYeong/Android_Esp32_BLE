package kr.co.theresearcher.esp32blemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.service.controls.Control;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class BleCommunicationActivity extends AppCompatActivity {


    private ImageButton txCountInitialButton, rxCountInitialButton;
    private TextView deviceNameAndAddress, receiveDataText, receiveByteText;
    private TextView txByteCountText, rxByteCountText, statusText;
    private EditText sendCommandTextField;
    private Button sendCommandButton;

    private ScanResult scanResult;
    private int txCount, rxCount;
    private char characteristicStatus = 0x00;
    private BleService mService;
    private boolean isConnected = false;

    private Handler handler = new Handler();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(BleAttributes.ACTION_GATT_AVAILABLE_DATA)) {

                StringBuilder builder = new StringBuilder();
                char[] data = intent.getCharArrayExtra(BleAttributes.EXTRA_GATT_DATA);
                txCount += data.length;
                txByteCountText.setText(("TX : " + String.valueOf(txCount) + " Bytes"));
                for (char c : data) {
                    builder.append(c);
                }

                receiveDataText.setText(builder.toString());

                /*
                StringBuilder builder1 = new StringBuilder();
                for (char c : data) {
                    builder1.append()
                }

                 */


            } else if (action.equals(BleAttributes.ACTION_GATT_CONNECTED)) {

                statusText.setText(("CONNECTED"));

            } else if (action.equals(BleAttributes.ACTION_GATT_CONNECTING)) {

                statusText.setText(("CONNECTING"));

            } else if (action.equals(BleAttributes.ACTION_GATT_DISCONNECTED)) {

                statusText.setText(("DISCONNECTED"));

            } else if (action.equals(BleAttributes.ACTION_GATT_DISCONNECTING)) {

                statusText.setText(("DISCONNECTING"));

            } else if (action.equals(BleAttributes.ACTION_GATT_SERVICES_DISCOVERED)) {

                statusText.setText(("DISCOVERED"));

            } else if (action.equals(BleAttributes.ACTION_WRITE_DESCRIPTOR)) {

                statusText.setText(("WRITE DESCRIPTOR"));

            } else if (action.equals(BleAttributes.ACTION_READ_DESCRIPTOR)) {

            } else if (action.equals(BleAttributes.ACTION_WRITE_CHARACTERISTIC)) {

                statusText.setText(("WRITE CHARACTERISTIC"));

            }

        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            mService = ((BleService.LocalBinder)iBinder).getService();
            mService.connect(scanResult.getDevice().getAddress());

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_communication);

        Intent intent = getIntent();
        scanResult = (ScanResult)intent.getParcelableExtra("SCAN.RESULT");

        allFindViewById();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(scanResult.getDevice().getName()).append(" : ").append(scanResult.getDevice().getAddress());

        deviceNameAndAddress.setText(stringBuilder.toString());

        txCountInitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txCount = 0;
                txByteCountText.setText(("TX : 0 Bytes"));
            }
        });

        rxCountInitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxCount = 0;
                rxByteCountText.setText(("RX : 0 Bytes"));
            }
        });

        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String command = sendCommandTextField.getText().toString();
                byte[] data;
                if (command.equals("")) {
                    return;
                }

                data = new byte[command.length()];


                for (int i = 0; i < command.length(); i++){

                    data[i] = (byte)command.charAt(i);

                }

                //여기서 null
                if (mService.writeCharacteristic(data)) {
                    rxCount += command.length();
                    rxByteCountText.setText(("RX : " + String.valueOf(rxCount) + " Bytes"));
                }



            }
        });

        Intent serviceIntent = new Intent(getApplicationContext(), BleService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);

    }

    private void allFindViewById() {

        deviceNameAndAddress = findViewById(R.id.tv_ble_name_and_address);

        receiveDataText = findViewById(R.id.tv_receive_data);
        receiveByteText = findViewById(R.id.tv_receive_byte);
        sendCommandTextField = findViewById(R.id.et_command);
        sendCommandButton = findViewById(R.id.btn_send_byte);
        txCountInitialButton = findViewById(R.id.imgbtn_initial_tx_byte_count);
        rxCountInitialButton = findViewById(R.id.imgbtn_initial_rx_byte_count);
        txByteCountText = findViewById(R.id.tv_tx_byte_count);
        rxByteCountText = findViewById(R.id.tv_rx_byte_count);
        statusText = findViewById(R.id.tv_status);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, makeIntentFilter());
    }

    private IntentFilter makeIntentFilter() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(BleAttributes.ACTION_GATT_AVAILABLE_DATA);
        filter.addAction(BleAttributes.ACTION_GATT_CONNECTED);
        filter.addAction(BleAttributes.ACTION_GATT_CONNECTING);
        filter.addAction(BleAttributes.ACTION_GATT_DISCONNECTED);
        filter.addAction(BleAttributes.ACTION_GATT_DISCONNECTING);
        filter.addAction(BleAttributes.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(BleAttributes.ACTION_WRITE_DESCRIPTOR);
        filter.addAction(BleAttributes.ACTION_READ_DESCRIPTOR);

        return filter;

    }

}