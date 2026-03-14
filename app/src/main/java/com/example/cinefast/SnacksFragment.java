package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class SnacksFragment extends Fragment {

    private String movieName;
    private int seatCount;
    private int ticketTotal;
    private String seatsCsv;

    // Prices
    private final double PRICE_POPCORN = 8.99;
    private final double PRICE_NACHOS = 7.99;
    private final double PRICE_DRINK = 5.99;
    private final double PRICE_CANDY = 6.99;

    // Qty
    private int qPop = 0, qNach = 0, qDrink = 0, qCandy = 0;

    private TextView txtQtyPopcorn, txtQtyNachos, txtQtyDrink, txtQtyCandy, txtSnackTotal;

    private void updateSnackTotal() {
        double total = qPop * PRICE_POPCORN
                + qNach * PRICE_NACHOS
                + qDrink * PRICE_DRINK
                + qCandy * PRICE_CANDY;

        txtSnackTotal.setText(String.format(Locale.US, "Snacks Total: $%.2f", total));
    }

    private int clampNonNegative(int v) {
        return Math.max(0, v);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_snacks, container, false);

        if (getArguments() != null) {
            movieName = getArguments().getString("movie_name");
            seatCount = getArguments().getInt("seat_count", 0);
            ticketTotal = getArguments().getInt("ticket_total", 0);
            seatsCsv = getArguments().getString("selected_seats_csv");
        }

        txtQtyPopcorn = view.findViewById(R.id.txtQtyPopcorn);
        txtQtyNachos = view.findViewById(R.id.txtQtyNachos);
        txtQtyDrink = view.findViewById(R.id.txtQtyDrink);
        txtQtyCandy = view.findViewById(R.id.txtQtyCandy);
        txtSnackTotal = view.findViewById(R.id.txtSnackTotal);

        Button minusPop = view.findViewById(R.id.btnMinusPopcorn);
        Button plusPop = view.findViewById(R.id.btnPlusPopcorn);
        Button minusNach = view.findViewById(R.id.btnMinusNachos);
        Button plusNach = view.findViewById(R.id.btnPlusNachos);
        Button minusDrink = view.findViewById(R.id.btnMinusDrink);
        Button plusDrink = view.findViewById(R.id.btnPlusDrink);
        Button minusCandy = view.findViewById(R.id.btnMinusCandy);
        Button plusCandy = view.findViewById(R.id.btnPlusCandy);

        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        minusPop.setOnClickListener(v -> { qPop = clampNonNegative(qPop - 1); txtQtyPopcorn.setText(String.valueOf(qPop)); updateSnackTotal(); });
        plusPop.setOnClickListener(v -> { qPop++; txtQtyPopcorn.setText(String.valueOf(qPop)); updateSnackTotal(); });

        minusNach.setOnClickListener(v -> { qNach = clampNonNegative(qNach - 1); txtQtyNachos.setText(String.valueOf(qNach)); updateSnackTotal(); });
        plusNach.setOnClickListener(v -> { qNach++; txtQtyNachos.setText(String.valueOf(qNach)); updateSnackTotal(); });

        minusDrink.setOnClickListener(v -> { qDrink = clampNonNegative(qDrink - 1); txtQtyDrink.setText(String.valueOf(qDrink)); updateSnackTotal(); });
        plusDrink.setOnClickListener(v -> { qDrink++; txtQtyDrink.setText(String.valueOf(qDrink)); updateSnackTotal(); });

        minusCandy.setOnClickListener(v -> { qCandy = clampNonNegative(qCandy - 1); txtQtyCandy.setText(String.valueOf(qCandy)); updateSnackTotal(); });
        plusCandy.setOnClickListener(v -> { qCandy++; txtQtyCandy.setText(String.valueOf(qCandy)); updateSnackTotal(); });

        updateSnackTotal();

        btnConfirm.setOnClickListener(v -> {
            double snacksTotal = qPop * PRICE_POPCORN
                    + qNach * PRICE_NACHOS
                    + qDrink * PRICE_DRINK
                    + qCandy * PRICE_CANDY;

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openTicketSummary(movieName, seatCount, ticketTotal, snacksTotal, seatsCsv);
            }
        });

        return view;
    }
}
