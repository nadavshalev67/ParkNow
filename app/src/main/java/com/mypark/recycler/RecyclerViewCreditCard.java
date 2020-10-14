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
import com.mypark.models.CreditCard;
import com.mypark.models.OwnerSpot;
import com.mypark.network.RetrofitInst;
import com.mypark.utilities.Utilites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewCreditCard extends RecyclerView.Adapter<RecyclerViewCreditCard.ViewHolder> {

    private List<CreditCard> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;


    public RecyclerViewCreditCard(Context context, RecyclerView recyclerView) {
        this.mInflater = LayoutInflater.from(context);
        this.mRecyclerView = recyclerView;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.row_credit_card, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                for (CreditCard creditCard : mData) {
                    creditCard.isChecked = false;
                }
                mData.get(itemPosition).isChecked = true;
                Utilites.saveCardListToSharedPrefence(mContext, mData);
                notifyDataSetChanged();

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                if (mData.get(itemPosition).isChecked) {
                    Toast.makeText(mContext, "Cannot removed main card", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    mData.remove(itemPosition);
                    Utilites.saveCardListToSharedPrefence(mContext, mData);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Card removed", Toast.LENGTH_SHORT).show();
                    return true;
                }

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkedImageView.setVisibility(mData.get(position).isChecked ? View.VISIBLE : View.INVISIBLE);
        holder.fourLastDigits.setText(mData.get(position).fourLastDigits + "");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fourLastDigits;
        ImageView checkedImageView;

        ViewHolder(View itemView) {
            super(itemView);
            fourLastDigits = itemView.findViewById(R.id.four_last_digit);
            checkedImageView = itemView.findViewById(R.id.checked);
        }

    }

    public void setNewList(List<CreditCard> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<CreditCard> getCreditCardList() {
        return mData;
    }
}
