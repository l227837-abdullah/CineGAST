package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private void openTrailer(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    private void bookMovie(String movieName) {
        Intent i = new Intent(this, SeatSelectionActivity.class);
        i.putExtra("movie_name", movieName);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView m1 = findViewById(R.id.txtMovie1);
        TextView m2 = findViewById(R.id.txtMovie2);
        TextView m3 = findViewById(R.id.txtMovie3);

        Button b1 = findViewById(R.id.btnBook1);
        Button b2 = findViewById(R.id.btnBook2);
        Button b3 = findViewById(R.id.btnBook3);

        Button t1 = findViewById(R.id.btnTrailer1);
        Button t2 = findViewById(R.id.btnTrailer2);
        Button t3 = findViewById(R.id.btnTrailer3);

        b1.setOnClickListener(v -> bookMovie(m1.getText().toString()));
        b2.setOnClickListener(v -> bookMovie(m2.getText().toString()));
        b3.setOnClickListener(v -> bookMovie(m3.getText().toString()));

        t1.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=EXeTwQWrcwY"));
        t2.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=YoHD9XEInc0"));
        t3.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=zSWdZVtXT7E"));
    }
}
