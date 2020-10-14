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
import com.mypark.models.OwnerSpot;
import com.mypark.network.RetrofitInst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewOwnerParkAdapter extends RecyclerView.Adapter<RecyclerViewOwnerParkAdapter.ViewHolder> {

    private List<OwnerSpot> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;


    public RecyclerViewOwnerParkAdapter(Context context) {
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
        holder.spotKey = mData.get(position).spot_key;
        holder.ownerSpot = mData.get(position);
        holder.position = position;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        int position;
        TextView mStartTime;
        TextView mFinishTime;
        TextView mAdress;
        ImageView mDeleteButton;
        String spotKey;
        OwnerSpot ownerSpot;


        ViewHolder(View itemView) {
            super(itemView);
            mStartTime = itemView.findViewById(R.id.start_time);
            mFinishTime = itemView.findViewById(R.id.finish_time);
            mAdress = itemView.findViewById(R.id.adress_text);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("uuid", ownerSpot.uuid);
                    hashMap.put("lng", ownerSpot.lng);
                    hashMap.put("lat", ownerSpot.lat);
                    Call<Void> listCall = RetrofitInst.getInstance().executetDeleteParkingSpot(hashMap);
                    listCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            mData.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }

    public void setNewList(List<OwnerSpot> data) {
        mData = data;
    }
}
