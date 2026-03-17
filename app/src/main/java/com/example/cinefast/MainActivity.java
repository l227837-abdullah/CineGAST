package com.example.cinefast;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            openHome();
        }
    }

    public void openHome() {
        replaceFragment(new HomeFragment(), false);
    }

    public void openSeatSelection(String movieName, boolean isComingSoon, String trailerUrl) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString("movie_name", movieName);
        args.putBoolean("is_coming_soon", isComingSoon);
        args.putString("trailer_url", trailerUrl);
        fragment.setArguments(args);
        replaceFragment(fragment, true);
    }

    public void openSnacks(String movieName, int seatCount, int ticketTotal, String selectedSeatsCsv) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putString("movie_name", movieName);
        args.putInt("seat_count", seatCount);
        args.putInt("ticket_total", ticketTotal);
        args.putString("selected_seats_csv", selectedSeatsCsv);
        fragment.setArguments(args);
        replaceFragment(fragment, true);
    }

    public void openTicketSummary(String movieName, int seatCount, int ticketTotal, double snacksTotal, String selectedSeatsCsv) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putString("movie_name", movieName);
        args.putInt("seat_count", seatCount);
        args.putInt("ticket_total", ticketTotal);
        args.putDouble("snacks_total", snacksTotal);
        args.putString("selected_seats_csv", selectedSeatsCsv);
        fragment.setArguments(args);
        replaceFragment(fragment, true);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
