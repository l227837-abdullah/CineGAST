package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TicketSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        String movieName = getIntent().getStringExtra("movie_name");
        int seatCount = getIntent().getIntExtra("seat_count", 0);
        int ticketTotal = getIntent().getIntExtra("ticket_total", 0);
        double snacksTotal = getIntent().getDoubleExtra("snacks_total", 0.0);
        String seatsCsv = getIntent().getStringExtra("selected_seats_csv");

        double grandTotal = ticketTotal + snacksTotal;

        TextView txtMovie = findViewById(R.id.txtMovieName);
        TextView txtTickets = findViewById(R.id.txtTicketList);
        TextView txtSnacks = findViewById(R.id.txtSnackList);
        TextView txtTotal = findViewById(R.id.txtTotalPrice);
        Button btnSend = findViewById(R.id.btnSend);

        txtMovie.setText(movieName == null ? "N/A" : movieName);

        if (seatsCsv != null && !seatsCsv.isEmpty()) {
            String[] seats = seatsCsv.split(",");
            StringBuilder ticketBuilder = new StringBuilder();

            for (String seat : seats) {
                ticketBuilder.append("Seat ")
                        .append(seat)
                        .append(" — 16 USD\n");
            }

            txtTickets.setText(ticketBuilder.toString());
        } else {
            txtTickets.setText("No seats selected");
        }

        if (snacksTotal > 0)
            txtSnacks.setText(String.format(Locale.US,"Snacks total — %.2f USD", snacksTotal));
        else
            txtSnacks.setText("No snacks selected");

        txtTotal.setText(String.format(Locale.US,"%.2f USD", grandTotal));
        btnSend.setOnClickListener(v -> {

            String body =
                    "Movie: " + movieName + "\n" +
                            "Seats: " + seatsCsv + "\n" +
                            "Tickets: $" + ticketTotal + "\n" +
                            String.format(Locale.US,"Snacks: %.2f\n", snacksTotal) +
                            String.format(Locale.US,"TOTAL: %.2f", grandTotal);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket");
            share.putExtra(Intent.EXTRA_TEXT, body);

            startActivity(Intent.createChooser(share, "Send Ticket via"));
        });
    }
}
