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
import android.content.IntentFilter;
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


    private Button startButton;
    private BleScanner scanner;
    private ScanResult scanResult;

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

            startButton.setText((result.getDevice().getName() + "연결"));
            scanResult = result;



        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.btn_start);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BleCommunicationActivity.class);
                intent.putExtra("SCAN.RESULT", scanResult);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

        scanner.stopScan();
        //currentstartButton.setText("스캔 중지");
    }

    @Override
    protected void onResume() {
        super.onResume();

        activityResultLauncher.launch(permissions);

        scanner = new BleScanner(this, listener);
        scanner.enableBluetoothLe();
        scanner.readyScan();
        scanner.startScan();


    }





}