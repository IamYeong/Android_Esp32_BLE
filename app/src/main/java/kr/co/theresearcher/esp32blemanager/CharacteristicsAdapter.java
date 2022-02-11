package kr.co.theresearcher.esp32blemanager;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CharacteristicsAdapter extends RecyclerView.Adapter<CharacteristicViewHolder> {

    private List<BluetoothGattCharacteristic> characteristics;
    private OnSelectCharacteristicListener listener;


    public CharacteristicsAdapter() {
        characteristics = new ArrayList<>();
    }

    public void setListener(OnSelectCharacteristicListener listener) {
        this.listener = listener;
    }

    public void addCharacteristic(BluetoothGattCharacteristic characteristic) {
        characteristics.add(characteristic);
    }


    @NonNull
    @Override
    public CharacteristicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_characteristic, parent, false);
        return new CharacteristicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacteristicViewHolder holder, int position) {

        holder.characteristicText.setText(characteristics.get(position).getUuid().toString());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BluetoothGattCharacteristic characteristic = characteristics.get(position);
                List<BluetoothGattCharacteristic> list = new ArrayList<>();
                list.add(characteristic);
                listener.onSelectCharacteristic(list);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (characteristics != null ? characteristics.size() : 0);
    }
}

class CharacteristicViewHolder extends RecyclerView.ViewHolder {

    protected ConstraintLayout constraintLayout;
    protected TextView characteristicText;

    public CharacteristicViewHolder(@NonNull View itemView) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.constraint_characteristic_dialog);
        characteristicText = itemView.findViewById(R.id.tv_characteristic_uuid);
    }
}
