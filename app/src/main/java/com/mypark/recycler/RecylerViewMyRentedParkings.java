package com.mypark.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mypark.R;
import com.mypark.models.MyRentedParking;
import com.mypark.models.OwnerSpot;
import com.mypark.network.RetrofitInst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecylerViewMyRentedParkings extends RecyclerView.Adapter<RecylerViewMyRentedParkings.ViewHolder> {

    private List<MyRentedParking> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;


    public RecylerViewMyRentedParkings(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
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
        holder.mStartTime.setText(mData.get(position).start_time + ":00");

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


        ViewHolder(View itemView) {
            super(itemView);
            mStartTime = itemView.findViewById(R.id.start_time);
            mFinishTime = itemView.findViewById(R.id.finish_time);
            mAdress = itemView.findViewById(R.id.adress_text);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
            mDeleteButton.setVisibility(View.INVISIBLE);
        }

    }

    public void setNewList(List<MyRentedParking> data) {
        mData = data;
    }
}
