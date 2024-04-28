package com.example.cinefast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        
        Toast.makeText(this, "CineFAST", Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set header email
        if (mAuth.getCurrentUser() != null) {
            TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_header_email);
            tvEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        if (savedInstanceState == null) {
            openHome();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            openHome();
        } else if (id == R.id.nav_my_bookings) {
            openMyBookings();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openHome() {
        replaceFragment(new HomeFragment(), false);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("CineFAST");
    }

    public void openMyBookings() {
        replaceFragment(new MyBookingsFragment(), true);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("My Bookings");
    }

    private void logout() {
        mAuth.signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("cinefast_session_pref_v3", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
