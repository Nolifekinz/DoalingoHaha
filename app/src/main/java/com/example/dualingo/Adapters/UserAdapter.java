package com.example.dualingo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dualingo.Models.User;
import com.example.dualingo.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvPosition.setText(String.valueOf(position + 1));
        holder.tvUserName.setText(user.getUsername());
        holder.tvScore.setText(String.valueOf(user.getExp()));
        // Load avatar người dùng nếu có
        Glide.with(holder.itemView.getContext())
                .load(user.getProfilePic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.fire)
                .into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvUserName, tvScore;
        ImageView imgUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvScore = itemView.findViewById(R.id.tvScore);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }
}

