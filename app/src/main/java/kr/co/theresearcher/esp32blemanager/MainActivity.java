package kr.co.theresearcher.esp32blemanager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private Button bleSwitch, deviceScanButton;
    private TextView currentStatusText;
    private DevicesAdapter adapter;
    private BleScanner scanner;
    private ScanResult scanResult;
    private BleService mService;

    private String[] permissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH
    };

    private List<String> deniedPermissions = new ArrayList<>();

    private ActivityResultLauncher<String[]> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {

                            for (String permission : result.keySet()) {

                                //해당 권한이 미동의 상태라면?
                                if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                                    shouldShowRequestPermissionRationale(permission);
                                }

                            }

                            if (deniedPermissions.isEmpty()) {
                                //Toast.makeText(SignActivity.this, "금일 자로 동의!", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(SignActivity.this, deniedPermissions.size() + "개의 권한 미동의", Toast.LENGTH_SHORT).show();
                                for (String permission : deniedPermissions) {
                                    shouldShowRequestPermissionRationale(permission);
                                }
                            }


                        }
                    }
            );

    private OnBleScanListener listener = new OnBleScanListener() {
        @Override
        public void onScan(ScanResult result) {

            adapter.addDevice(result);

        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();



        }
    };


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            currentStatusText.setText(("BLE 서비스 연결 완료"));

            mService = ((BleService.LocalBinder)iBinder).getService();
            mService.connect(scanResult.getDevice().getAddress());


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            currentStatusText.setText(("BLE 서비스 연결 종료"));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_main);
        bleSwitch = findViewById(R.id.imgbtn_main_switch);
        deviceScanButton = findViewById(R.id.imgbtn_main_scan_switch);
        currentStatusText = findViewById(R.id.tv_main_current_status);

        scanner = new BleScanner(this, listener);

        adapter = new DevicesAdapter(this, new OnDeviceSelectListener() {
            @Override
            public void onSelectDevice(ScanResult result) {

                currentStatusText.setText(("BLE 서비스 연결 중"));
                scanResult = result;
                Intent intent = new Intent(getApplicationContext(), BleService.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);

        bleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanner.enableBluetoothLe();

            }
        });

        deviceScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (scanner.isScanning()) {

                    scanner.stopScan();
                    currentStatusText.setText("스캔 중지");

                } else {

                    scanner.readyScan();
                    scanner.startScan();
                    currentStatusText.setText("스캔 중");

                }



            }
        });



    }


    @Override
    protected void onPause() {
        super.onPause();

        scanner.stopScan();
        currentStatusText.setText("스캔 중지");
    }

    @Override
    protected void onResume() {
        super.onResume();

        activityResultLauncher.launch(permissions);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}