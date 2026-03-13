package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final int PRICE_PER_SEAT = 16;

    private TextView txtMovieName, txtTotals;
    private Button btnBookSkip, btnProceedSnacks;
    private GridLayout gridLeft, gridRight;

    private final HashSet<String> booked = new HashSet<>();
    private final HashSet<String> selected = new HashSet<>();
    private final ArrayList<Button> seatButtons = new ArrayList<>();

    private String movieName = "Movie";

    private void updateTotals() {
        int count = selected.size();
        int total = count * PRICE_PER_SEAT;
        txtTotals.setText("Selected: " + count + " | Ticket Total: $" + total);

        btnProceedSnacks.setEnabled(count > 0);
        btnProceedSnacks.setAlpha(count > 0 ? 1f : 0.5f);
    }

    private void styleSeat(Button b, String seatId) {
        if (booked.contains(seatId)) {
            b.setBackgroundResource(R.drawable.seat_booked);
            b.setEnabled(false);
        } else if (selected.contains(seatId)) {
            b.setBackgroundResource(R.drawable.seat_selected);
        } else {
            b.setBackgroundResource(R.drawable.seat_available);
        }
    }

    private Button createSeat(String seatId) {

        Button seat = new Button(this);
        seat.setText("");
        seat.setAllCaps(false);
        seat.setWidth(90);
        seat.setHeight(90);
        seat.setPadding(0,0,0,0);

        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = 90;
        lp.height = 90;
        lp.setMargins(10,10,10,10);
        seat.setLayoutParams(lp);

        styleSeat(seat, seatId);

        seat.setOnClickListener(v -> {
            if (booked.contains(seatId)) return;

            if (selected.contains(seatId))
                selected.remove(seatId);
            else
                selected.add(seatId);

            styleSeat(seat, seatId);
            updateTotals();
        });

        seatButtons.add(seat);
        return seat;
    }

    private void buildSeatGrids() {
        booked.add("A2");
        booked.add("B4");
        booked.add("C1");
        booked.add("D6");
        booked.add("F7");
        booked.add("E3");


        for (int r = 0; r < 6; r++) {

            char row = (char) ('A' + r);


            for (int c = 1; c <= 4; c++) {
                String id = row + String.valueOf(c);
                gridLeft.addView(createSeat(id));
            }


            for (int c = 5; c <= 8; c++) {
                String id = row + String.valueOf(c);
                gridRight.addView(createSeat(id));
            }
        }
    }

    private void goToSnacks() {
        Intent i = new Intent(this, SnacksActivity.class);
        i.putExtra("movie_name", movieName);
        i.putExtra("seat_count", selected.size());
        i.putExtra("ticket_total", selected.size() * PRICE_PER_SEAT);
        i.putExtra("selected_seats_csv", String.join(",", selected));
        startActivity(i);
    }

    private void goToSummarySkipSnacks() {
        Intent i = new Intent(this, TicketSummaryActivity.class);
        i.putExtra("movie_name", movieName);
        i.putExtra("seat_count", selected.size());
        i.putExtra("ticket_total", selected.size() * PRICE_PER_SEAT);
        i.putExtra("snacks_total", 0);
        i.putExtra("selected_seats_csv", String.join(",", selected));
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        txtMovieName = findViewById(R.id.txt_movie_name);
        txtTotals = findViewById(R.id.txtTotals);
        btnBookSkip = findViewById(R.id.btnBookSkip);
        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);

        gridLeft = findViewById(R.id.gridLeft);
        gridRight = findViewById(R.id.gridRight);

        String name = getIntent().getStringExtra("movie_name");
        if (name != null) movieName = name;
        txtMovieName.setText(movieName);

        buildSeatGrids();
        updateTotals();

        btnProceedSnacks.setOnClickListener(v -> goToSnacks());
        btnBookSkip.setOnClickListener(v -> goToSummarySkipSnacks());
    }
}
