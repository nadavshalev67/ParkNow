package com.mypark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mypark.R;
import com.mypark.models.MyRentedParking;
import com.mypark.models.OwnerSpot;
import com.mypark.network.RetrofitInst;
import com.mypark.recycler.RecyclerViewOwnerParkAdapter;
import com.mypark.recycler.RecylerViewMyRentedParkings;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private View mFragment;
    private RecyclerViewOwnerParkAdapter mAdapterFirst;
    private RecyclerView mRecyclerViewFirst;
    private ProgressBar mProgresBarFirst;


    private RecyclerView mRecyclerViewSecond;
    private ProgressBar mProgresBarSecond;
    private RecylerViewMyRentedParkings mAdapterSecond;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.home_fragment, container, false);
        initRecyclerOwnerParking();
        initRecyclerMyRenteredParking();
        return mFragment;
    }

    private void initRecyclerMyRenteredParking() {
        mRecyclerViewSecond = mFragment.findViewById(R.id.recyler_view_my_rented_spots);
        mRecyclerViewSecond.setVisibility(View.INVISIBLE);
        mProgresBarSecond = mFragment.findViewById(R.id.progress_bar_two);
        mRecyclerViewSecond.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapterSecond = new RecylerViewMyRentedParkings(getContext());
        mRecyclerViewSecond.setAdapter(mAdapterSecond);
        String uid = FirebaseAuth.getInstance().getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uuid_taken", uid);
        Call<List<MyRentedParking>> listCall = RetrofitInst.getInstance().executeGetSpotReserverd(hashMap);
        listCall.enqueue(new Callback<List<MyRentedParking>>() {
            @Override
            public void onResponse(Call<List<MyRentedParking>> call, Response<List<MyRentedParking>> response) {
                mAdapterSecond.setNewList(response.body());
                mProgresBarSecond.setVisibility(View.INVISIBLE);
                mRecyclerViewSecond.setVisibility(View.VISIBLE);
                mAdapterSecond.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<MyRentedParking>> call, Throwable t) {
                mProgresBarSecond.setVisibility(View.INVISIBLE);
                mRecyclerViewSecond.setVisibility(View.VISIBLE);
                mAdapterSecond.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerOwnerParking() {
        mRecyclerViewFirst = mFragment.findViewById(R.id.recyler_view_my_parking_spot);
        mRecyclerViewFirst.setVisibility(View.INVISIBLE);
        mProgresBarFirst = mFragment.findViewById(R.id.progress_bar);
        mRecyclerViewFirst.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapterFirst = new RecyclerViewOwnerParkAdapter(getContext());
        mRecyclerViewFirst.setAdapter(mAdapterFirst);
        String uid = FirebaseAuth.getInstance().getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uuid", uid);
        Call<List<OwnerSpot>> listCall = RetrofitInst.getInstance().executeGetOwnerSpot(hashMap);
        listCall.enqueue(new Callback<List<OwnerSpot>>() {
            @Override
            public void onResponse(Call<List<OwnerSpot>> call, Response<List<OwnerSpot>> response) {
                mAdapterFirst.setNewList(response.body());
                mProgresBarFirst.setVisibility(View.INVISIBLE);
                mRecyclerViewFirst.setVisibility(View.VISIBLE);
                mAdapterFirst.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<OwnerSpot>> call, Throwable t) {
                mProgresBarFirst.setVisibility(View.INVISIBLE);
                mRecyclerViewFirst.setVisibility(View.VISIBLE);
                mAdapterFirst.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
