package com.example.gmisproject.user;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmisproject.BinsModel;
import com.example.gmisproject.R;
import com.white.progressview.HorizontalProgressView;

import java.util.ArrayList;

public class UserBinAdapter extends RecyclerView.Adapter<UserBinAdapter.UserBinHolder> {

    private ArrayList<BinsModel> binsModels;
    private OnItemClickListener mListener;

    public UserBinAdapter(ArrayList<BinsModel> binsModels) {
        this.binsModels = binsModels;
    }

    @NonNull
    @Override
    public UserBinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bin_item, parent, false);
        UserBinHolder viewHolder = new UserBinHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserBinHolder holder, int position) {
        BinsModel currentBin = binsModels.get(position);

        holder.binId.setText(String.valueOf(currentBin.getBinId()));
        holder.clientAddress.setText(currentBin.getAddress());
        holder.binStatus.setText(currentBin.getStatus());
        holder.binPercentage.setProgress(currentBin.getPercentage());
        if (currentBin.getStatus().equals("تعمل")) {
            holder.binStatus.setTextColor(Color.parseColor("#12AC5A"));
        } else {
            holder.binStatus.setTextColor(Color.parseColor("#E60039"));
        }
    }

    @Override
    public int getItemCount() {
        return binsModels.size();
    }

    public void SetOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class UserBinHolder extends RecyclerView.ViewHolder {
        public ImageView trash;
        public TextView binId;
        public TextView clientAddress;
        public TextView binStatus;
        public HorizontalProgressView binPercentage;

        public UserBinHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            trash = itemView.findViewById(R.id.trash);
            binId = itemView.findViewById(R.id.binId);
            clientAddress = itemView.findViewById(R.id.user_address);
            binStatus = itemView.findViewById(R.id.bin_status);
            binPercentage = itemView.findViewById(R.id.bin_percentage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
