package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;

public class SeatSelectionFragment extends Fragment {

    private static final int PRICE_PER_SEAT = 16;

    private TextView txtMovieName, txtTotals, txtStatusTag;
    private Button btnBookSkip, btnProceedSnacks, btnComingSoon, btnWatchTrailer;
    private GridLayout gridLeft, gridRight;

    private final HashSet<String> booked = new HashSet<>();
    private final HashSet<String> selected = new HashSet<>();
    private final ArrayList<Button> seatButtons = new ArrayList<>();

    private String movieName = "Movie";
    private String trailerUrl = "";
    private boolean isComingSoon = false;

    private void updateTotals() {
        int count = selected.size();
        int total = count * PRICE_PER_SEAT;
        txtTotals.setText("Selected: " + count + " | Ticket Total: $" + total);

        btnProceedSnacks.setEnabled(count > 0);
        btnProceedSnacks.setAlpha(count > 0 ? 1f : 0.5f);
        btnBookSkip.setEnabled(count > 0);
        btnBookSkip.setAlpha(count > 0 ? 1f : 0.5f);
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
        
        // Disable click if coming soon
        if (isComingSoon) {
            b.setEnabled(false);
            b.setAlpha(0.6f);
        }
    }

    private Button createSeat(String seatId) {
        Button seat = new Button(getContext());
        seat.setText("");
        
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = 80;
        lp.height = 80;
        lp.setMargins(8, 8, 8, 8);
        seat.setLayoutParams(lp);

        styleSeat(seat, seatId);

        seat.setOnClickListener(v -> {
            if (isComingSoon || booked.contains(seatId)) return;

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

        for (int r = 0; r < 6; r++) {
            char row = (char) ('A' + r);
            for (int c = 1; c <= 4; c++) {
                gridLeft.addView(createSeat(row + String.valueOf(c)));
            }
            for (int c = 5; c <= 8; c++) {
                gridRight.addView(createSeat(row + String.valueOf(c)));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        txtMovieName = view.findViewById(R.id.txt_movie_name);
        txtStatusTag = view.findViewById(R.id.txtStatusTag);
        txtTotals = view.findViewById(R.id.txtTotals);
        
        btnBookSkip = view.findViewById(R.id.btnBookSkip);
        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);
        btnComingSoon = view.findViewById(R.id.btnComingSoon);
        btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
        
        gridLeft = view.findViewById(R.id.gridLeft);
        gridRight = view.findViewById(R.id.gridRight);

        if (getArguments() != null) {
            movieName = getArguments().getString("movie_name", "Movie");
            isComingSoon = getArguments().getBoolean("is_coming_soon", false);
            trailerUrl = getArguments().getString("trailer_url", "");
        }

        txtMovieName.setText(movieName);
        
        if (isComingSoon) {
            txtStatusTag.setText("COMING SOON");
            txtStatusTag.setBackgroundResource(R.drawable.bg_gray_tag);
            
            btnBookSkip.setVisibility(View.GONE);
            btnProceedSnacks.setVisibility(View.GONE);
            btnComingSoon.setVisibility(View.VISIBLE);
            btnWatchTrailer.setVisibility(View.VISIBLE);
            
            txtTotals.setText("Seats are currently unavailable for booking.");
        } else {
            txtStatusTag.setText("NOW SHOWING");
            txtStatusTag.setBackgroundResource(R.drawable.bg_green_tag);
            updateTotals();
        }

        buildSeatGrids();

        btnWatchTrailer.setOnClickListener(v -> {
            if (!trailerUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                startActivity(intent);
            }
        });

        btnProceedSnacks.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openSnacks(
                        movieName, 
                        selected.size(), 
                        selected.size() * PRICE_PER_SEAT, 
                        String.join(",", selected)
                );
            }
        });

        btnBookSkip.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openTicketSummary(
                        movieName, 
                        selected.size(), 
                        selected.size() * PRICE_PER_SEAT, 
                        0.0, 
                        String.join(",", selected)
                );
            }
        });

        return view;
    }
}
