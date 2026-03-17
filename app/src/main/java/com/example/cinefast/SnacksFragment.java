package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SnacksFragment extends Fragment {

    private String movieName;
    private int seatCount;
    private int ticketTotal;
    private String seatsCsv;

    private List<Snack> snackList;
    private TextView txtSnackTotal;
    private double currentSnackTotal = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        if (getArguments() != null) {
            movieName = getArguments().getString("movie_name");
            seatCount = getArguments().getInt("seat_count", 0);
            ticketTotal = getArguments().getInt("ticket_total", 0);
            seatsCsv = getArguments().getString("selected_seats_csv");
        }

        txtSnackTotal = view.findViewById(R.id.txtSnackTotal);
        ListView lvSnacks = view.findViewById(R.id.lvSnacks);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        snackList = new ArrayList<>();
        snackList.add(new Snack("Popcorn", 8.99, R.drawable.snack_popcorn));
        snackList.add(new Snack("Nachos", 7.99, R.drawable.snack_nachos));
        snackList.add(new Snack("Soft Drink", 5.99, R.drawable.snack_softdrink));
        snackList.add(new Snack("Candy Mix", 6.99, R.drawable.snack_candy));

        SnackAdapter adapter = new SnackAdapter(getContext(), snackList, this::calculateTotal);
        lvSnacks.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openTicketSummary(movieName, seatCount, ticketTotal, currentSnackTotal, seatsCsv);
            }
        });

        calculateTotal();
        return view;
    }

    private void calculateTotal() {
        currentSnackTotal = 0.0;
        for (Snack s : snackList) {
            currentSnackTotal += (s.getPrice() * s.getQuantity());
        }
        txtSnackTotal.setText(String.format(Locale.US, "Snacks Total: $%.2f", currentSnackTotal));
    }
}
