package com.example.dualingo.LearningFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dualingo.R;
public class FillInBlankFragment extends Fragment {


    Button btnback ;

    public FillInBlankFragment() {
        // Required empty public constructor
    }
    public static FillInBlankFragment newInstance(String param1, String param2) {
        FillInBlankFragment fragment = new FillInBlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fill_in_blank, container, false);

        btnback = view.findViewById(R.id.buttonBack);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}