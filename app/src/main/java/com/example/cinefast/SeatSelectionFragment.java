package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;

public class SeatSelectionFragment extends Fragment {

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
        Button seat = new Button(getContext());
        seat.setText("");
        seat.setAllCaps(false);
        
        // Using layout params for size instead of fixed pixel width/height for better compatibility
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = 90;
        lp.height = 90;
        lp.setMargins(10, 10, 10, 10);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_seat_selection, container, false);

        txtMovieName = view.findViewById(R.id.txt_movie_name);
        txtTotals = view.findViewById(R.id.txtTotals);
        btnBookSkip = view.findViewById(R.id.btnBookSkip);
        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);
        gridLeft = view.findViewById(R.id.gridLeft);
        gridRight = view.findViewById(R.id.gridRight);

        if (getArguments() != null) {
            movieName = getArguments().getString("movie_name", "Movie");
        }
        txtMovieName.setText(movieName);

        buildSeatGrids();
        updateTotals();

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
