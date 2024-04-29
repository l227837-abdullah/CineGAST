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

import java.util.List;

public class NowShowingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_showing, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Movie> movies = JsonUtils.getMovies(getContext(), "now_showing");

        MovieAdapter adapter = new MovieAdapter(movies, movie -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openSeatSelection(movie.getName(), movie.isComingSoon(), movie.getTrailerUrl());
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
