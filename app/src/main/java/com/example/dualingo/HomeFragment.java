package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dualingo.Adapters.LectureAdapter;
import com.example.dualingo.Adapters.SessionAdapter;
import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;
import com.example.dualingo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements LectureAdapter.OnLectureClickListener {

    private FragmentHomeBinding binding;
    private List<String> wordList = new ArrayList<>(Arrays.asList("Hello", "world", "this", "is", "a", "test"));

    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;

    private List<Session> sessionList ;

    private List<Lecture> lectures;

    private SessionAdapter sessionAdapter;

    private RecyclerView recyclerViewSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerViewSession = view.findViewById(R.id.rvSession);

        setupRecyclerViews();


        return view;
    }

    private void setupRecyclerViews() {

        sessionList = new ArrayList<Session>();

        lectures = new ArrayList<Lecture>();

        lectures.add(new Lecture("1","1",R.drawable.coin));
        lectures.add(new Lecture("2","2",R.drawable.coin));
        lectures.add(new Lecture("3","3",R.drawable.coin));
        lectures.add(new Lecture("4","4",R.drawable.coin));
        lectures.add(new Lecture("5","5",R.drawable.coin));

        sessionList.add(new Session("1","1",R.drawable.fire,lectures));
        sessionList.add(new Session("2","2",R.drawable.fire,lectures));

        sessionAdapter = new SessionAdapter(getContext(),sessionList,this);

        recyclerViewSession.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSession.setAdapter(sessionAdapter);
    }
//
//    // Callback khi một từ được chọn từ danh sách ban đầu
//    private void onWordClicked(String word) {
//        wordList.remove(word);
//        wordAdapter.notifyDataSetChanged();
//
//        selectedWords.add(word);
//        resultAdapter.notifyDataSetChanged();
//    }
//
//    // Callback khi một từ trong thanh kết quả bị nhấn
//    private void onResultWordClicked(String word) {
//        selectedWords.remove(word);
//        resultAdapter.notifyDataSetChanged();
//
//        wordList.add(word);
//        wordAdapter.notifyDataSetChanged();
//    }
//
//    // ItemTouchHelper.Callback để xử lý kéo thả trong thanh kết quả
//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            int fromPosition = viewHolder.getAdapterPosition();
//            int toPosition = target.getAdapterPosition();
//
//            // Thay đổi vị trí của từ trong thanh kết quả
//            Collections.swap(selectedWords, fromPosition, toPosition);
//            resultAdapter.notifyItemMoved(fromPosition, toPosition);
//
//            return true;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//            // Không xử lý vuốt
//        }
//    };
//
//    private void checkResult() {
//        StringBuilder resultSentence = new StringBuilder();
//        for (String word : selectedWords) {
//            resultSentence.append(word).append(" ");
//        }
//        Intent intent= new Intent(getActivity(), IntroductionActivity.class);
//        startActivity(intent);
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onLectureClick(int sessionPosition, int lecturePosition) {
        Intent intent = new Intent(getContext(), LearningActivity.class);
        String idLecture = lectures.get(lecturePosition).getIdLecture();

        intent.putExtra("idLecture",idLecture);

        startActivity(intent);
    }
}
