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
import com.mypark.models.OwnerSpot;
import com.mypark.network.RetrofitInst;
import com.mypark.recycler.RecyclerViewOwnerParkAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private View mFragment;
    private ActivitytFragmentListener mListener;
    private RecyclerViewOwnerParkAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.home_fragment, container, false);
        initRecyclerOwnerParking();

        return mFragment;
    }

    private void initRecyclerOwnerParking() {
        mRecyclerView = mFragment.findViewById(R.id.recyler_view_my_parking_spot);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar = mFragment.findViewById(R.id.progress_bar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mAdapter = new RecyclerViewOwnerParkAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        String uid = FirebaseAuth.getInstance().getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uuid", uid);
        Call<List<OwnerSpot>> listCall = RetrofitInst.getInstance().executeGetOwnerSpot(hashMap);
        listCall.enqueue(new Callback<List<OwnerSpot>>() {
            @Override
            public void onResponse(Call<List<OwnerSpot>> call, Response<List<OwnerSpot>> response) {
                mAdapter.setNewList(response.body());
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<OwnerSpot>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }
}
