package com.example.cinefast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class TicketSummaryFragment extends Fragment {

    private static final String PREFS_NAME = "CineFastPrefs";
    private static final String KEY_MOVIE = "last_movie";
    private static final String KEY_SEATS = "last_seats";
    private static final String KEY_TOTAL = "last_total";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        String movieName = "";
        int seatCount = 0;
        int ticketTotal = 0;
        double snacksTotal = 0.0;
        String seatsCsv = "";

        if (getArguments() != null) {
            movieName = getArguments().getString("movie_name", "");
            seatCount = getArguments().getInt("seat_count", 0);
            ticketTotal = getArguments().getInt("ticket_total", 0);
            snacksTotal = getArguments().getDouble("snacks_total", 0.0);
            seatsCsv = getArguments().getString("selected_seats_csv", "");
        }

        double grandTotal = ticketTotal + snacksTotal;

        TextView txtMovie = view.findViewById(R.id.txtMovieName);
        TextView txtTickets = view.findViewById(R.id.txtTicketList);
        TextView txtSnacks = view.findViewById(R.id.txtSnackList);
        TextView txtTotal = view.findViewById(R.id.txtTotalPrice);
        Button btnConfirm = view.findViewById(R.id.btnConfirmBooking);

        txtMovie.setText(movieName.isEmpty() ? "N/A" : movieName);
        
        if (!seatsCsv.isEmpty()) {
            txtTickets.setText("Seats: " + seatsCsv + " (" + seatCount + " tickets)\nTotal: $" + ticketTotal);
        } else {
            txtTickets.setText("No seats selected");
        }

        if (snacksTotal > 0) {
            txtSnacks.setText(String.format(Locale.US, "Selected Snacks Total: $%.2f", snacksTotal));
        } else {
            txtSnacks.setText("No snacks selected");
        }

        txtTotal.setText(String.format(Locale.US, "$%.2f", grandTotal));

        final String finalMovieName = movieName;
        final int finalSeatCount = seatCount;
        final double finalGrandTotal = grandTotal;
        final String finalSeatsCsv = seatsCsv;

        btnConfirm.setOnClickListener(v -> {
            // 1. Save to SharedPreferences
            saveBooking(finalMovieName, finalSeatCount, finalGrandTotal);

            // 2. Show Toast
            Toast.makeText(getContext(), "Booking Confirmed & Saved!", Toast.LENGTH_SHORT).show();

            // 3. Share Intent
            shareTicket(finalMovieName, finalSeatsCsv, finalGrandTotal);
        });

        return view;
    }

    private void saveBooking(String movie, int seats, double total) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MOVIE, movie);
        editor.putInt(KEY_SEATS, seats);
        editor.putFloat(KEY_TOTAL, (float) total);
        editor.apply();
    }

    private void shareTicket(String movie, String seats, double total) {
        String message = "CineFAST Ticket\n" +
                "Movie: " + movie + "\n" +
                "Seats: " + seats + "\n" +
                String.format(Locale.US, "Grand Total: $%.2f", total);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "My CineFAST Booking");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share Ticket via"));
    }
}
