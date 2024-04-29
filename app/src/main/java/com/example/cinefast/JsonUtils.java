package com.example.cinefast;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<Movie> getMovies(Context context, String type) {
        List<Movie> movies = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "movies.json");
            JSONObject obj = new JSONObject(jsonString);
            JSONArray array = obj.getJSONArray(type);
            for (int i = 0; i < array.length(); i++) {
                JSONObject movieObj = array.getJSONObject(i);
                String name = movieObj.getString("name");
                String genre = movieObj.getString("genre");
                String duration = movieObj.getString("duration");
                String posterName = movieObj.getString("poster");
                String trailerUrl = movieObj.getString("trailerUrl");
                
                int resId = context.getResources().getIdentifier(posterName, "drawable", context.getPackageName());
                if (resId == 0) resId = R.drawable.app_logo;

                movies.add(new Movie(name, genre, duration, resId, trailerUrl, type.equals("coming_soon")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
