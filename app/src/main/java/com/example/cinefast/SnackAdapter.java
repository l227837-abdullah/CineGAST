package com.example.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class SnackAdapter extends ArrayAdapter<Snack> {

    private List<Snack> snackList;
    private OnSnackQtyChangeListener listener;

    public interface OnSnackQtyChangeListener {
        void onQtyChanged();
    }

    public SnackAdapter(@NonNull Context context, @NonNull List<Snack> snacks, OnSnackQtyChangeListener listener) {
        super(context, 0, snacks);
        this.snackList = snacks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_snack, parent, false);
        }

        Snack snack = snackList.get(position);

        ImageView imgSnack = convertView.findViewById(R.id.imgSnack);
        TextView txtName = convertView.findViewById(R.id.txtSnackName);
        TextView txtPrice = convertView.findViewById(R.id.txtSnackPrice);
        TextView txtQty = convertView.findViewById(R.id.txtQty);
        Button btnMinus = convertView.findViewById(R.id.btnMinus);
        Button btnPlus = convertView.findViewById(R.id.btnPlus);

        imgSnack.setImageResource(snack.getImageResId());
        txtName.setText(snack.getName());
        txtPrice.setText(String.format(Locale.US, "$%.2f", snack.getPrice()));
        txtQty.setText(String.valueOf(snack.getQuantity()));

        btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            txtQty.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) listener.onQtyChanged();
        });

        btnMinus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() - 1);
            txtQty.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) listener.onQtyChanged();
        });

        return convertView;
    }
}
