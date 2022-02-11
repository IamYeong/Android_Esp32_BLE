package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesViewHolder> {

    private List<ScanResult> deviceScanResults;
    private Context context;
    //private OnDeviceSelectListener listener;

    public DevicesAdapter(Context context) {
        deviceScanResults = new ArrayList<>();
        //this.listener = listener;
        this.context = context;
    }


    public void addDevice(ScanResult device) {

        for (ScanResult d : deviceScanResults) {
            if (d.getDevice().getAddress().equals(device.getDevice().getAddress())) {
                return;
            }
        }

        deviceScanResults.add(device);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_device, parent, false);
        return new DevicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {


        StringBuilder builder = new StringBuilder();
        builder.append(deviceScanResults.get(position).getDevice().getName());
        if (builder.toString().equals("")) {
            builder.append("No name");
        }
        holder.deviceName.setText(builder.toString());
        holder.deviceAddress.setText(deviceScanResults.get(position).getDevice().getAddress());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BleCommunicationActivity.class);
                intent.putExtra("SCAN.RESULT", deviceScanResults.get(position));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return (deviceScanResults != null ? deviceScanResults.size() : 0);
    }
}

class DevicesViewHolder extends RecyclerView.ViewHolder {

    protected TextView deviceName;
    protected TextView deviceAddress;
    protected LinearLayout linearLayout;

    public DevicesViewHolder(@NonNull View itemView) {
        super(itemView);
        deviceName = itemView.findViewById(R.id.tv_scan_device_name);
        deviceAddress = itemView.findViewById(R.id.tv_scan_device_mac_address);
        linearLayout = itemView.findViewById(R.id.linear_device_item);
    }
}
