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

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Oppenheimer", "Drama/History", "180 min", R.drawable.poster_oppenheimer, "https://www.youtube.com/watch?v=uYPbbksJxIg", true));
        movies.add(new Movie("Dune: Part Two", "Sci-Fi", "166 min", R.drawable.poster_dune, "https://www.youtube.com/watch?v=Way9Dexny3w", true));
        movies.add(new Movie("The Batman", "Action/Crime", "176 min", R.drawable.poster_batman, "https://www.youtube.com/watch?v=mqqft22n7RE", true));

        MovieAdapter adapter = new MovieAdapter(movies, movie -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openSeatSelection(movie.getName(), movie.isComingSoon(), movie.getTrailerUrl());
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
