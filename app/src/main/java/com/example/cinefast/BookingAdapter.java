package com.example.cinefast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private OnCancelClickListener cancelClickListener;

    public interface OnCancelClickListener {
        void onCancelClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookingList, OnCancelClickListener cancelClickListener) {
        this.bookingList = bookingList;
        this.cancelClickListener = cancelClickListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvMovieName.setText(booking.getMovieName());
        holder.tvDateTime.setText(booking.getDateTime());
        holder.tvTickets.setText("Seats: " + booking.getSeats());
        
        // Poster logic - normally you'd use a library like Glide/Picasso, 
        // but here we can try to match the movie name to a resource for demo purposes 
        // or just use a default logo as per assignment simplicity if not specified.
        holder.imgPoster.setImageResource(R.drawable.app_logo);

        holder.btnCancel.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieName, tvDateTime, tvTickets;
        ImageButton btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_movie_poster);
            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvTickets = itemView.findViewById(R.id.tv_tickets);
            btnCancel = itemView.findViewById(R.id.btn_cancel_booking);
        }
    }
}
