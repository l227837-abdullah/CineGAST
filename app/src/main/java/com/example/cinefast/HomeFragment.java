package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private void openTrailer(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    private void bookMovie(String movieName) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openSeatSelection(movieName, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        TextView m1 = view.findViewById(R.id.txtMovie1);
        TextView m2 = view.findViewById(R.id.txtMovie2);
        TextView m3 = view.findViewById(R.id.txtMovie3);

        Button b1 = view.findViewById(R.id.btnBook1);
        Button b2 = view.findViewById(R.id.btnBook2);
        Button b3 = view.findViewById(R.id.btnBook3);

        Button t1 = view.findViewById(R.id.btnTrailer1);
        Button t2 = view.findViewById(R.id.btnTrailer2);
        Button t3 = view.findViewById(R.id.btnTrailer3);

        b1.setOnClickListener(v -> bookMovie(m1.getText().toString()));
        b2.setOnClickListener(v -> bookMovie(m2.getText().toString()));
        b3.setOnClickListener(v -> bookMovie(m3.getText().toString()));

        t1.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=EXeTwQWrcwY"));
        t2.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=YoHD9XEInc0"));
        t3.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=zSWdZVtXT7E"));

        return view;
    }
}
