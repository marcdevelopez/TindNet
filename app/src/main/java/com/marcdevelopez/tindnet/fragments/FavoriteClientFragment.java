package com.marcdevelopez.tindnet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcdevelopez.tindnet.R;

public class FavoriteClientFragment extends Fragment {


    public FavoriteClientFragment() {
        // Required empty public constructor
    }


    public static FavoriteClientFragment newInstance(String param1, String param2) {
        FavoriteClientFragment fragment = new FavoriteClientFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_client, container, false);
    }
}