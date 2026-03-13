package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SnacksActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        movieName = getIntent().getStringExtra("movie_name");
        seatCount = getIntent().getIntExtra("seat_count", 0);
        ticketTotal = getIntent().getIntExtra("ticket_total", 0);
        seatsCsv = getIntent().getStringExtra("selected_seats_csv");

        txtQtyPopcorn = findViewById(R.id.txtQtyPopcorn);
        txtQtyNachos = findViewById(R.id.txtQtyNachos);
        txtQtyDrink = findViewById(R.id.txtQtyDrink);
        txtQtyCandy = findViewById(R.id.txtQtyCandy);
        txtSnackTotal = findViewById(R.id.txtSnackTotal);

        Button minusPop = findViewById(R.id.btnMinusPopcorn);
        Button plusPop = findViewById(R.id.btnPlusPopcorn);
        Button minusNach = findViewById(R.id.btnMinusNachos);
        Button plusNach = findViewById(R.id.btnPlusNachos);
        Button minusDrink = findViewById(R.id.btnMinusDrink);
        Button plusDrink = findViewById(R.id.btnPlusDrink);
        Button minusCandy = findViewById(R.id.btnMinusCandy);
        Button plusCandy = findViewById(R.id.btnPlusCandy);

        Button btnConfirm = findViewById(R.id.btnConfirm);

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

            Intent i = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            i.putExtra("movie_name", movieName);
            i.putExtra("seat_count", seatCount);
            i.putExtra("ticket_total", ticketTotal);
            i.putExtra("snacks_total", snacksTotal);
            i.putExtra("selected_seats_csv", seatsCsv);
            startActivity(i);
        });
    }
}
