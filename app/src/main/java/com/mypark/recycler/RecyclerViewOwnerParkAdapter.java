package com.mypark.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mypark.R;
import com.mypark.models.OwnerSpot;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewOwnerParkAdapter extends RecyclerView.Adapter<RecyclerViewOwnerParkAdapter.ViewHolder> {

    private List<OwnerSpot> mData = new ArrayList<>();
    private LayoutInflater mInflater;


    public RecyclerViewOwnerParkAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_parking_spot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mAdress.setText(mData.get(position).adress);
        holder.mFinishTime.setText(mData.get(position).finish_time + ":00");
        holder.mStartTime.setText(mData.get(position).start_time+ ":00");
        holder.spotKey = mData.get(position).spot_key;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mStartTime;
        TextView mFinishTime;
        TextView mAdress;
        ImageView mDeleteButton;
        String spotKey;

        ViewHolder(View itemView) {
            super(itemView);
            mStartTime = itemView.findViewById(R.id.start_time);
            mFinishTime = itemView.findViewById(R.id.finish_time);
            mAdress = itemView.findViewById(R.id.adress_text);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
        }

    }

    public void setNewList(List<OwnerSpot> data) {
        mData = data;
    }
}
