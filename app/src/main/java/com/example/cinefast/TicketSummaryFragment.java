package com.example.cinefast;

import android.content.Intent;
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

public class TicketSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_summary, container, false);

        String movieName = "";
        int seatCount = 0;
        int ticketTotal = 0;
        double snacksTotal = 0.0;
        String seatsCsv = "";

        if (getArguments() != null) {
            movieName = getArguments().getString("movie_name");
            seatCount = getArguments().getInt("seat_count", 0);
            ticketTotal = getArguments().getInt("ticket_total", 0);
            snacksTotal = getArguments().getDouble("snacks_total", 0.0);
            seatsCsv = getArguments().getString("selected_seats_csv");
        }

        double grandTotal = ticketTotal + snacksTotal;

        TextView txtMovie = view.findViewById(R.id.txtMovieName);
        TextView txtTickets = view.findViewById(R.id.txtTicketList);
        TextView txtSnacks = view.findViewById(R.id.txtSnackList);
        TextView txtTotal = view.findViewById(R.id.txtTotalPrice);
        Button btnSend = view.findViewById(R.id.btnSend);

        txtMovie.setText(movieName == null || movieName.isEmpty() ? "N/A" : movieName);

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
            txtSnacks.setText(String.format(Locale.US, "Snacks total — %.2f USD", snacksTotal));
        else
            txtSnacks.setText("No snacks selected");

        txtTotal.setText(String.format(Locale.US, "%.2f USD", grandTotal));
        
        final String finalMovieName = movieName;
        final String finalSeatsCsv = seatsCsv;
        final int finalTicketTotal = ticketTotal;
        final double finalSnacksTotal = snacksTotal;

        btnSend.setOnClickListener(v -> {
            String body =
                    "Movie: " + finalMovieName + "\n" +
                            "Seats: " + finalSeatsCsv + "\n" +
                            "Tickets: $" + finalTicketTotal + "\n" +
                            String.format(Locale.US, "Snacks: %.2f\n", finalSnacksTotal) +
                            String.format(Locale.US, "TOTAL: %.2f", grandTotal);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket");
            share.putExtra(Intent.EXTRA_TEXT, body);

            startActivity(Intent.createChooser(share, "Send Ticket via"));
        });

        return view;
    }
}
