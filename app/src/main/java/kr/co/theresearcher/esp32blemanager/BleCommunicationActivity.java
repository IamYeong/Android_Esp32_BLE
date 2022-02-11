package kr.co.theresearcher.esp32blemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class BleCommunicationActivity extends AppCompatActivity {


    private ImageButton findTxButton, findRxButton, txCountInitialButton, rxCountInitialButton;
    private TextView deviceNameAndAddress, txCharacteristicText, rxCharacteristicText, receiveDataText, receiveByteText;
    private TextView txByteCountText, rxByteCountText, statusText;
    private EditText sendCommandTextField;
    private Button sendCommandButton, connectSwitchButton, runButton;

    private ScanResult scanResult;
    private int txCount, rxCount;
    private BleService mService;
    private boolean isConnected = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(BleAttributes.ACTION_GATT_AVAILABLE_DATA)) {

            } else if (action.equals(BleAttributes.ACTION_GATT_CONNECTED)) {

            } else if (action.equals(BleAttributes.ACTION_GATT_CONNECTING)) {

            } else if (action.equals(BleAttributes.ACTION_GATT_DISCONNECTED)) {

            } else if (action.equals(BleAttributes.ACTION_GATT_DISCONNECTING)) {

            } else if (action.equals(BleAttributes.ACTION_GATT_SERVICES_DISCOVERED)) {

            } else if (action.equals(BleAttributes.ACTION_WRITE_DESCRIPTOR)) {

            } else if (action.equals(BleAttributes.ACTION_READ_DESCRIPTOR)) {

            } else {

            }

        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            mService = ((BleService.LocalBinder) iBinder).getService();
            mService.connect(scanResult.getDevice().getAddress());

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            statusText.setText(("Service Disconnected"));

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

        connectSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected) {

                    connectSwitchButton.setText(("연결끊는 중"));
                    if (mService != null) {
                        mService.close();
                        mService.disconnect();
                        unbindService(serviceConnection);
                        return;
                    }

                    connectSwitchButton.setText(("연결끊김"));


                } else {

                    connectSwitchButton.setText(("연결 중"));
                    Intent serviceIntent = new Intent(getApplicationContext(), BleService.class);
                    bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

                }

            }
        });

        findTxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Dialog 불렀다가 값 return 받아서
                txCharacteristicText.setText("?");
                //mService.setTxCharacteristic();

            }
        });

        findRxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService != null) {
                    mService.run();
                }
            }
        });

        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String command = sendCommandTextField.getText().toString();
                char[] data;
                if (command.equals("")) {
                    return;
                }

                data = new char[command.length()];

                for (int i = 0; i < command.length(); i++){

                    data[i] = command.charAt(i);

                }

                //mService.writeData(data);

            }
        });



    }

    private void allFindViewById() {

        deviceNameAndAddress = findViewById(R.id.tv_ble_name_and_address);
        connectSwitchButton = findViewById(R.id.btn_connect_switch);
        txCharacteristicText = findViewById(R.id.tv_characteristic_tx);
        rxCharacteristicText = findViewById(R.id.tv_characteristic_rx);
        findTxButton = findViewById(R.id.imgbtn_find_tx_characteristic);
        findRxButton = findViewById(R.id.imgbtn_find_rx_characteristic);
        runButton = findViewById(R.id.btn_run);
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