package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Formula;
import com.example.dualingo.Models.Grammar;
import com.example.dualingo.Models.Introduction;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.Models.Session;
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.Models.Vocabulary;
import com.example.dualingo.Models.VocabularyLesson;
import com.example.dualingo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment homeFragment;
    private Fragment rankFragment;
    private Fragment personalInfoFragment;
    private Fragment grammarFragment;
    private Fragment vocabularyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

//        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            return;
//        }

        // Khởi tạo các fragment
        homeFragment = new HomeFragment();
        rankFragment = new RankFragment();
        personalInfoFragment = new PersonalInfoFragment();
        grammarFragment = new GrammarFragment();
        vocabularyFragment = new VocabularyFragment();

        setInitialFragment(homeFragment);

        DataSyncManager dataSyncManager = new DataSyncManager(this);
        dataSyncManager.syncData(this);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                switchFragment(homeFragment);
            } else if (itemId == R.id.rank) {
                switchFragment(rankFragment);
            } else if (itemId == R.id.avata) {
                switchFragment(personalInfoFragment);
            } else if (itemId == R.id.duoal) {
                switchFragment(grammarFragment);
            } else if (itemId == R.id.train) {
                switchFragment(vocabularyFragment);
            }
            return true;
        });
    }

    private void setInitialFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment frag : fragmentManager.getFragments()) {
            transaction.hide(frag);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.frameLayout, fragment);
        }

        transaction.commit();
    }
//private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Khởi tạo Firestore
//        db = FirebaseFirestore.getInstance();
//
//        // Gọi hàm thêm dữ liệu mẫu
//        addSampleData();
//    }
//
//    private void addSampleData() {
//        addSessions();
//        addLectures();
//        addVocabularies();
//        addArrangingExercises();
//        addFillBlankExercises();
//        addVocabularyLessons();
//        addSpeakingExercises();
//        addListeningExercises();
//        addGrammarExercises();
//        addIntroductions();
//    }
//
//    // Giả sử constructor Listening yêu cầu thêm một danh sách các câu hỏi
//    private void addListeningExercises() {
//        List<Listening> listeningExercises = Arrays.asList(
//                new Listening("1", "1", "Hello!", "Xin chào", Arrays.asList("Xin","chào")),
//                new Listening("2", "1", "Nice to meet you!", "Rất vui được gặp bạn", Arrays.asList("Rất", "vui","được", "gặp", "bạn")),
//                new Listening("3", "2", "What is your name?", "Tên bạn là gì", Arrays.asList("Tên","bạn","là","gì")),
//                new Listening("4", "2", "My name is Anna.", "Tên tôi là Anna", Arrays.asList("Tên","tôi","là","Anna"))
//        );
//
//        for (Listening listening : listeningExercises) {
//            db.collection("Listening").document(listening.getIdListening()).set(listening);
//        }
//    }
//
//
//    // Thêm bài tập Grammar
//    private void addGrammarExercises() {
//        Grammar grammar = new Grammar(
//                "1", "Present Simple Tense",
//                "Thì Hiện Tại Đơn diễn tả hành động thường xuyên xảy ra.",
//                "I eat breakfast every morning.",
//                new Formula("I/You/We/They + Verb", "I/You/We/They + do not + Verb", "Do I/you/we/they + Verb?"),
//                Arrays.asList("Dùng để diễn tả sự thật hiển nhiên.", "Dùng để nói về thói quen."),
//                Arrays.asList("I go to school.", "She reads a book every night.")
//        );
//        db.collection("Grammar").document(grammar.getIdGrammar()).set(grammar);
//    }
//
//    private void addIntroductions() {
//        List<Introduction> introductions = Arrays.asList(
//                new Introduction("1", "Basic Vocabulary and Grammar Rules", Arrays.asList("1", "2"), Arrays.asList("1", "2")),  // idVocabulary và idGrammar
//                new Introduction("2", "Conversational Phrases and Common Grammar", Arrays.asList("3", "4"), Arrays.asList("3", "4")),
//                new Introduction("3", "Formal and Informal Greetings", Arrays.asList("5", "6"), Arrays.asList("5", "6"))
//        );
//
//        for (Introduction introduction : introductions) {
//            db.collection("Introduction").document(introduction.getIdIntroduction()).set(introduction);
//        }
//    }
//
//
//    private void addSessions() {
//        List<Session> sessions = Arrays.asList(
//                new Session("1", "English for Beginners", "url_to_image1", new ArrayList<>()),
//                new Session("2", "Basic Conversations", "url_to_image2", new ArrayList<>())
//        );
//
//        for (Session session : sessions) {
//            db.collection("Session").document(session.getIdSession()).set(session);
//        }
//    }
//
//    private void addLectures() {
//        List<Lecture> lectures = Arrays.asList(
//                new Lecture("1", "Greetings", 1),
//                new Lecture("2", "Introductions", 2),
//                new Lecture("3", "Common Phrases", 3)
//        );
//
//        for (Lecture lecture : lectures) {
//            db.collection("Lecture").document(lecture.getIdLecture()).set(lecture);
//        }
//    }
//
//    private void addVocabularies() {
//        List<Vocabulary> vocabularyList = Arrays.asList(
//                new Vocabulary("1", "Hello", "Xin chào", "Noun", "Hello! How are you?", "heˈlō"),
//                new Vocabulary("2", "Goodbye", "Tạm biệt", "Noun", "Goodbye! See you later.", "ɡo͝odˈbī"),
//                new Vocabulary("3", "Please", "Làm ơn", "Adverb", "Please pass the salt.", "plēz"),
//                new Vocabulary("4", "Thank you", "Cảm ơn", "Phrase", "Thank you for your help.", "THaNGk ˌyo͞o")
//        );
//
//        for (Vocabulary vocab : vocabularyList) {
//            db.collection("Vocabulary").document(vocab.getIdVocabulary()).set(vocab);
//        }
//    }
//
//    private void addArrangingExercises() {
//        List<Arranging> arrangingExercises = Arrays.asList(
//                new Arranging("1", "1", "Arrange the words to form a sentence", "Good morning", Arrays.asList("Good", "morning")),
//                new Arranging("2", "1", "Arrange the words to form a sentence", "Nice to meet you", Arrays.asList("Nice", "to", "meet", "you")),
//                new Arranging("3", "2", "Arrange the words to form a question", "What is your name?", Arrays.asList("What", "is", "your", "name?")),
//                new Arranging("4", "2", "Arrange the words to form a sentence", "My name is Anna", Arrays.asList("My", "name", "is", "Anna"))
//        );
//
//        for (Arranging arranging : arrangingExercises) {
//            db.collection("Arranging").document(arranging.getIdArranging()).set(arranging);
//        }
//    }
//
//    private void addFillBlankExercises() {
//        List<FillBlank> fillBlankExercises = Arrays.asList(
//                new FillBlank("1", "1", "Fill in the blank: ___ morning", "Good", Arrays.asList("Good", "Bad", "Early")),
//                new FillBlank("2", "1", "Fill in the blank: ___ you later", "See", Arrays.asList("See", "Meet", "Come")),
//                new FillBlank("3", "2", "Fill in the blank: My ___ is Anna", "name", Arrays.asList("name", "age", "job")),
//                new FillBlank("4", "3", "Fill in the blank: I ___ coffee every morning", "drink", Arrays.asList("drink", "eat", "make")),
//                new FillBlank("5", "3", "Fill in the blank: She ___ the piano well", "plays", Arrays.asList("plays", "drives", "writes"))
//        );
//
//        for (FillBlank fillBlank : fillBlankExercises) {
//            db.collection("FillBlank").document(fillBlank.getIdFillBlank()).set(fillBlank);
//        }
//    }
//
//    private void addVocabularyLessons() {
//        List<VocabularyLesson> vocabularyLessons = Arrays.asList(
//                new VocabularyLesson("1", "1", "Match the word with its meaning", "url_to_image1", "Hello", Arrays.asList("Hello", "Goodbye")),
//                new VocabularyLesson("2", "1", "Select the correct word", "url_to_image2", "Thank you", Arrays.asList("Please", "Thank you", "Sorry")),
//                new VocabularyLesson("3", "2", "Choose the correct word: I am ___", "url_to_image3", "happy", Arrays.asList("happy", "sad", "angry")),
//                new VocabularyLesson("4", "3", "What is the opposite of 'Goodbye'?", "url_to_image4", "Hello", Arrays.asList("Hello", "Please", "Thanks"))
//        );
//
//        for (VocabularyLesson vocabLesson : vocabularyLessons) {
//            db.collection("VocabularyLesson").document(vocabLesson.getIdVocabularyLesson()).set(vocabLesson);
//        }
//    }
//
//    private void addSpeakingExercises() {
//        List<Speaking> speakingExercises = Arrays.asList(
//                new Speaking("1", "1", "Say the greeting: Hello!"),
//                new Speaking("2", "1", "Say: Nice to meet you!"),
//                new Speaking("3", "2", "Introduce yourself: My name is ___"),
//                new Speaking("4", "2", "Ask: What is your name?"),
//                new Speaking("5", "3", "Say: Goodbye! See you later.")
//        );
//
//        for (Speaking speaking : speakingExercises) {
//            db.collection("Speaking").document(speaking.getIdSpeaking()).set(speaking);
//        }
//    }

}
