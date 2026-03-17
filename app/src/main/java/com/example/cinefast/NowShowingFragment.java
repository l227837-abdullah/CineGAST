package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NowShowingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_showing, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Dark Knight", "Action", "152 min", R.drawable.poster_dark_knight, "https://www.youtube.com/watch?v=EXeTwQWrcwY", false));
        movies.add(new Movie("Inception", "Sci-Fi", "148 min", R.drawable.poster_inception, "https://www.youtube.com/watch?v=YoHD9XEInc0", false));
        movies.add(new Movie("Interstellar", "Sci-Fi", "169 min", R.drawable.poster_interstellar, "https://www.youtube.com/watch?v=zSWdZVtXT7E", false));

        MovieAdapter adapter = new MovieAdapter(movies, movie -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openSeatSelection(movie.getName(), movie.isComingSoon(), movie.getTrailerUrl());
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
