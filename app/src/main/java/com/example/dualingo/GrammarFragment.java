package com.example.dualingo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dualingo.Adapters.GrammarAdapter;
import com.example.dualingo.Models.Grammar;
import java.util.ArrayList;
import java.util.List;

public class GrammarFragment extends Fragment {

    private RecyclerView rvGrammar;
    private GrammarAdapter adapter;
    private List<Grammar> grammarList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        return inflater.inflate(R.layout.fragment_grammar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGrammar = view.findViewById(R.id.rvGrammar);
        grammarList = getGrammarList(); // Hàm để tạo danh sách ngữ pháp

        adapter = new GrammarAdapter(grammarList);
        rvGrammar.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGrammar.setAdapter(adapter);
    }

    private List<Grammar> getGrammarList() {
        List<Grammar> list = new ArrayList<>();
        list.add(new Grammar(
                "1",
                "Present Simple",
                "Dùng để diễn tả một thói quen hàng ngày hoặc một chân lý.",
                "I go to school every day.",
                new Grammar.Formula(
                        "Subject + V(s/es) + Object",
                        "Subject + do/does not + V + Object",
                        "Do/Does + Subject + V + Object?"
                ),
                List.of("Thường dùng với các trạng từ chỉ tần suất như always, usually",
                        "Động từ chia nguyên thể ngoại trừ ngôi he/she/it"),
                List.of("I go to school.", "She watches TV every day.")
        ));

        list.add(new Grammar(
                "2",
                "Present Continuous",
                "Dùng để diễn tả hành động đang diễn ra ngay tại thời điểm nói.",
                "I am reading a book.",
                new Grammar.Formula(
                        "Subject + am/is/are + V-ing",
                        "Subject + am/is/are not + V-ing",
                        "Am/Is/Are + Subject + V-ing?"
                ),
                List.of("Diễn tả hành động đang diễn ra ở hiện tại",
                        "Dùng với các trạng từ như now, right now, at the moment"),
                List.of("I am watching TV.", "They are playing soccer right now.")
        ));
        // Thêm nhiều mục ngữ pháp khác...
        return list;
    }
}
