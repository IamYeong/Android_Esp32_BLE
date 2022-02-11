package kr.co.theresearcher.esp32blemanager;

import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CharacteristicsDialog extends Dialog {

    private RecyclerView rv;
    private Button cancelButton;
    private CharacteristicsAdapter adapter;
    private Context context;
    private OnSelectCharacteristicListener mListener;

    public CharacteristicsDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    public void setSelectListener(OnSelectCharacteristicListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_characteristics);

        cancelButton = findViewById(R.id.btn_cancel_characteristic_dialog);
        rv = findViewById(R.id.rv_characteristic_dialog);
        adapter = new CharacteristicsAdapter();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        adapter.setListener(new OnSelectCharacteristicListener() {
            @Override
            public void onSelectCharacteristic(List<BluetoothGattCharacteristic> characteristics) {

                mListener.onSelectCharacteristic(characteristics);
                dismiss();

            }
        });
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

    }

    public void setCharacteristics(List<BluetoothGattCharacteristic> characteristics) {

        for (BluetoothGattCharacteristic c : characteristics) {
            adapter.addCharacteristic(c);
        }
        adapter.notifyDataSetChanged();

    }


}
