package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private StringListAdapter adapter;
    private List<Vocabulary> vocabularyList;

    private List<String> NameLessonList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvVocabulary = view.findViewById(R.id.rvVocabulary);
        NameLessonList = getLessonList();
//        vocabularyList = getLessonList(); // Hàm để tạo danh sách từ vựng
//
//        adapter = new VocabularyAdapter(vocabularyList, getContext());
//        rvVocabulary.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvVocabulary.setAdapter(adapter);

        adapter = new StringListAdapter(NameLessonList, new StringListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String item = NameLessonList.get(position);
                Intent intent = new Intent(getContext(),VocabActivity.class);
                intent.putExtra("lessname",item);
                startActivity(intent);
            }
        });
        rvVocabulary.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVocabulary.setAdapter(adapter);
    }

    private List<String> getLessonList() {
        List<String> list = new ArrayList<>();
        list.add("Lesson 1"); // Thêm cách phát âm
        list.add("Lesson 2"); // Thêm cách phát âm
        list.add("Lesson 3");
        list.add("Lesson 4");
        list.add("Lesson 5");
        list.add("Lesson 6");
        list.add("Lesson 7");
        // Thêm nhiều từ vựng khác...
        return list;
    }


    private List<Vocabulary> getVocabularyList() {
        List<Vocabulary> list = new ArrayList<>();
        list.add(new Vocabulary("1", "apple", "quả táo", "noun", "I ate an apple.", "/ˈæp.əl/")); // Thêm cách phát âm
        list.add(new Vocabulary("2", "run", "chạy", "verb", "He can run fast.", "/rʌn/")); // Thêm cách phát âm
        // Thêm nhiều từ vựng khác...
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
