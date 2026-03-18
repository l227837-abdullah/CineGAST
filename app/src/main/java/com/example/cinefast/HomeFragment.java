package com.example.cinefast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String PREFS_NAME = "CineFastPrefs";
    private static final String KEY_MOVIE = "last_movie";
    private static final String KEY_SEATS = "last_seats";
    private static final String KEY_TOTAL = "last_total";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        ImageButton btnMenu = view.findViewById(R.id.btnMenu);

        viewPager.setAdapter(new HomePagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Now Showing" : "Coming Soon");
        }).attach();

        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenu().add("View Last Booking");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("View Last Booking")) {
                    showLastBooking();
                    return true;
                }
                return false;
            });
            popup.show();
        });

        return view;
    }

    private void showLastBooking() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String movie = prefs.getString(KEY_MOVIE, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Last Successful Booking");

        if (movie == null) {
            builder.setMessage("No previous booking found.");
        } else {
            int seats = prefs.getInt(KEY_SEATS, 0);
            float total = prefs.getFloat(KEY_TOTAL, 0.0f);

            String info = String.format(Locale.US, "Movie: %s\nSeats: %d\nTotal Price: $%.2f", 
                    movie, seats, total);
            builder.setMessage(info);
        }

        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private static class HomePagerAdapter extends FragmentStateAdapter {
        public HomePagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new NowShowingFragment() : new ComingSoonFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
