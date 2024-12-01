package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.StringListAdapter;
import com.example.dualingo.Adapters.VocabularyAdapter;
import com.example.dualingo.Models.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class VocabularyFragment extends Fragment {

    private RecyclerView rvVocabulary;
    //private VocabularyAdapter adapter;
    private VocabularyAdapter adapter;
    private List<Vocabulary> vocabularyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvVocabulary = view.findViewById(R.id.VocabOfLesson);


        vocabularyList = getVocabularyList("",""); // Hàm để tạo danh sách từ vựng

        adapter = new VocabularyAdapter(vocabularyList, getContext());
        rvVocabulary.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVocabulary.setAdapter(adapter);

    }

    private List<Vocabulary> getVocabularyList(String Session, String Lecture) {
        List<Vocabulary> list = new ArrayList<>();
        list.add(new Vocabulary("1", "apple", "quả táo", "noun", "I ate an apple.", "/ˈæp.əl/")); // Thêm cách phát âm
        list.add(new Vocabulary("2", "run", "chạy", "verb", "He can run fast.", "/rʌn/")); // Thêm cách phát âm
        return list;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (adapter != null) {
//            adapter.shutdown(); // Giải phóng tài nguyên
//        }
//    }
}
