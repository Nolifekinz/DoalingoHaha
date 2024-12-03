package com.example.dualingo;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dualingo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment homeFragment;
    private Fragment rankFragment;
    private Fragment personalInfoFragment;
    private Fragment grammarFragment;
    private Fragment vocabularyFragment;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Kết thúc MainActivity
            return;
        }

        DataSyncManager dataSyncManager = new DataSyncManager(this);
        dataSyncManager.syncData(this);

        // Khởi tạo các fragment
        homeFragment = new HomeFragment();
        rankFragment = new RankFragment();
        personalInfoFragment = new PersonalInfoFragment();
        grammarFragment = new GrammarFragment();
        vocabularyFragment = new VocabularyFragment();

        setInitialFragment(homeFragment);

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
        transaction.replace(R.id.frameLayout, fragment); // Sử dụng replace để đảm bảo fragment ban đầu
        transaction.commit();
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!fragment.isAdded()) {
            transaction.add(R.id.frameLayout, fragment);
        }

        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            if (frag == fragment) {
                transaction.show(frag);
            } else {
                transaction.hide(frag);
            }
        }

        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}


//
//
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
//    // Thêm Sessions
//    private void addSessions() {
//        List<Session> sessions = Arrays.asList(
//                new Session("1", "English for Beginners", "url_to_image1", Arrays.asList("1", "2", "3", "4", "5", "6"), 1),
//                new Session("2", "Basic Conversations", "url_to_image2", Arrays.asList("7", "8", "9", "10", "11", "12"), 2),
//                new Session("3", "Advanced English", "url_to_image3", Arrays.asList("13", "14", "15", "16", "17", "18"),3)
//        );
//
//        for (Session session : sessions) {
//            db.collection("Session").document(session.getIdSession()).set(session);
//        }
//    }
//
//    private void addLectures() {
//        List<Lecture> lectures = Arrays.asList(
//                new Lecture("1", "Greetings", "image_res_id_1", 1),
//                new Lecture("2", "Introductions", "image_res_id_2", 2),
//                new Lecture("3", "Common Phrases", "image_res_id_3", 3),
//                new Lecture("4", "Formal Greetings", "image_res_id_4", 4),
//                new Lecture("5", "Small Talk", "image_res_id_5", 5),
//                new Lecture("6", "Travel English", "image_res_id_6", 6),
//                new Lecture("7", "Business Vocabulary", "image_res_id_7", 1),
//                new Lecture("8", "Workplace Conversations", "image_res_id_8", 2),
//                new Lecture("9", "English for Meetings", "image_res_id_9", 3),
//                new Lecture("10", "English for Phone Calls", "image_res_id_10", 4),
//                new Lecture("11", "Shopping Vocabulary", "image_res_id_11", 5),
//                new Lecture("12", "Ordering Food", "image_res_id_12", 6),
//                new Lecture("13", "English for Interviews", "image_res_id_13", 1),
//                new Lecture("14", "Making Appointments", "image_res_id_14", 2),
//                new Lecture("15", "Health and Fitness Vocabulary", "image_res_id_15", 3),
//                new Lecture("16", "English for Travel Emergencies", "image_res_id_16", 4),
//                new Lecture("17", "English for Social Media", "image_res_id_17", 5),
//                new Lecture("18", "Cultural Differences", "image_res_id_18", 6)
//        );
//
//        for (Lecture lecture : lectures) {
//            db.collection("Lecture").document(lecture.getIdLecture()).set(lecture);
//        }
//    }
//
//
//    // Thêm Vocabularies
//    private void addVocabularies() {
//        List<Vocabulary> vocabularyList = Arrays.asList(
//                new Vocabulary("1", "Hello", "Xin chào", "Noun", "Hello! How are you?", "heˈlō"),
//                new Vocabulary("2", "Goodbye", "Tạm biệt", "Noun", "Goodbye! See you later.", "ɡo͝odˈbī"),
//                new Vocabulary("3", "Please", "Làm ơn", "Adverb", "Please pass the salt.", "plēz"),
//                new Vocabulary("4", "Thank you", "Cảm ơn", "Phrase", "Thank you for your help.", "THaNGk ˌyo͞o"),
//                new Vocabulary("5", "Excuse me", "Xin lỗi", "Phrase", "Excuse me, where is the restroom?", "ikˈskyo͞oz"),
//                new Vocabulary("6", "Sorry", "Xin lỗi", "Adjective", "Sorry for the mistake.", "ˈsôrē"),
//                new Vocabulary("7", "Please", "Làm ơn", "Adverb", "Please wait a moment.", "plēz"),
//                new Vocabulary("8", "Yes", "Vâng", "Adverb", "Yes, I understand.", "yes"),
//                new Vocabulary("9", "No", "Không", "Adverb", "No, I don't think so.", "nō"),
//                new Vocabulary("10", "Help", "Giúp đỡ", "Verb", "Can you help me?", "help"),
//                new Vocabulary("11", "Go", "Đi", "Verb", "I go to school every day.", "ɡō"),
//                new Vocabulary("12", "Work", "Làm việc", "Verb", "She works in a hospital.", "wərk"),
//                new Vocabulary("13", "Eat", "Ăn", "Verb", "He eats breakfast every morning.", "ēt"),
//                new Vocabulary("14", "Live", "Sống", "Verb", "They live in London.", "liv"),
//                new Vocabulary("15", "Like", "Thích", "Verb", "I like reading books.", "līk")
//        );
//
//        for (Vocabulary vocab : vocabularyList) {
//            db.collection("Vocabulary").document(vocab.getIdVocabulary()).set(vocab);
//        }
//    }
//
//    private void addSpeakingExercises() {
//        List<Speaking> speakingExercises = Arrays.asList(
//                new Speaking("1", "1", "Hello"),
//                new Speaking("2", "1", "Nice to meet you"),
//                new Speaking("3", "2", "My name is Hung"),
//                new Speaking("4", "2", "What is your name"),
//                new Speaking("5", "3", "Goodbye See you later"),
//                new Speaking("6", "3", "Good morning"),
//                new Speaking("7", "4", "How are you today"),
//                new Speaking("8", "4", "Where are you from"),
//                new Speaking("9", "5", "I am from America"),
//                new Speaking("10", "5", "Do you like English")
//        );
//
//        for (Speaking speaking : speakingExercises) {
//            db.collection("Speaking").document(speaking.getIdSpeaking()).set(speaking);
//        }
//    }
//
//
//
//    private void addArrangingExercises() {
//        List<Arranging> arrangingExercises = Arrays.asList(
//                new Arranging("1", "1", "Chào buổi sáng", "Good morning", Arrays.asList("Good", "morning")),
//                new Arranging("2", "1", "Rất vui được gặp bạn", "Nice to meet you", Arrays.asList("Nice", "to", "meet", "you")),
//                new Arranging("3", "2", "Bạn tên gì?", "What is your name?", Arrays.asList("What", "is", "your", "name?")),
//                new Arranging("4", "2", "Tên tôi là Anna", "My name is Anna", Arrays.asList("My", "name", "is", "Anna")),
//                new Arranging("5", "3", "Bạn sống ở đâu?", "Where do you live?", Arrays.asList("Where", "do", "you", "live?")),
//                new Arranging("6", "3", "Tôi thích đọc sách", "I like reading books", Arrays.asList("I", "like", "reading", "books")),
//                new Arranging("7", "1", "Tôi đi học mỗi ngày", "I go to school every day", Arrays.asList("I", "go", "to", "school", "every", "day")),
//                new Arranging("8", "1", "Cô ấy làm việc trong bệnh viện", "She works in a hospital", Arrays.asList("She", "works", "in", "a", "hospital")),
//                new Arranging("9", "2", "Tôi đã đến công viên hôm qua", "I went to the park yesterday", Arrays.asList("I", "went", "to", "the", "park", "yesterday")),
//                new Arranging("10", "2", "Cô ấy đã làm việc tuần trước", "She worked last week", Arrays.asList("She", "worked", "last", "week"))
//        );
//
//        for (Arranging arranging : arrangingExercises) {
//            db.collection("Arranging").document(arranging.getIdArranging()).set(arranging);
//        }
//    }
//
//
//    private void addFillBlankExercises() {
//        List<FillBlank> fillBlankExercises = Arrays.asList(
//                new FillBlank("1", "1", "___ morning", "Good", Arrays.asList("Good", "Bad", "Early")),
//                new FillBlank("2", "1", "___ you later", "See", Arrays.asList("See", "Meet", "Come")),
//                new FillBlank("3", "2", "My ___ is Anna", "name", Arrays.asList("name", "age", "job")),
//                new FillBlank("4", "3", "I ___ coffee every morning", "drink", Arrays.asList("drink", "eat", "make")),
//                new FillBlank("5", "3", "She ___ the piano well", "plays", Arrays.asList("plays", "drives", "writes")),
//                new FillBlank("6", "4", "I ___ a good book", "read", Arrays.asList("read", "ate", "cook")),
//                new FillBlank("7", "1", "I ___ to school every day.", "go", Arrays.asList("go", "went", "goes")),
//                new FillBlank("8", "1", "She ___ in a hospital.", "works", Arrays.asList("work", "works", "worked")),
//                new FillBlank("9", "1", "They ___ in London.", "live", Arrays.asList("live", "lived", "lives")),
//                new FillBlank("10", "2", "I ___ to the park yesterday.", "went", Arrays.asList("go", "went", "gone")),
//                new FillBlank("11", "2", "She ___ in a company last year.", "worked", Arrays.asList("works", "worked", "working")),
//                new FillBlank("12", "2", "They ___ in Paris for five years.", "lived", Arrays.asList("live", "lived", "living")),
//                new FillBlank("13", "3", "I ___ my ___ every morning", "brush,teeth", Arrays.asList("brush", "teeth", "wash", "head", "play")),
//                new FillBlank("14", "4", "She ___ a ___ in the kitchen", "cooked,meal", Arrays.asList("cooked", "meal", "ate", "baked", "cooking", "dish", "soup")),
//                new FillBlank("15", "5", "We ___ to the ___ every weekend", "go,beach", Arrays.asList("go", "beach", "went","play")),
//                new FillBlank("16", "6", "I ___ my ___ to school", "take,bike", Arrays.asList("take", "bike", "drive", "foot")),
//                new FillBlank("17", "7", "They ___ a ___ at the party", "had,great time", Arrays.asList("had", "great time", "make", "enjoyed"))
//
//        );
//
//        for (FillBlank fillBlank : fillBlankExercises) {
//            db.collection("FillBlank").document(fillBlank.getIdFillBlank()).set(fillBlank);
//        }
//    }
//
//
//        private void addGrammarExercises() {
//            Grammar grammar = new Grammar(
//                    "1",
//                    "Present Simple",
//                    "Dùng để diễn tả một thói quen hàng ngày hoặc một chân lý.",
//                    "I go to school every day.",
//                    new Formula(
//                            "Subject + V(s/es) + Object",
//                            "Subject + do/does not + V + Object",
//                            "Do/Does + Subject + V + Object?"
//                    ),
//                    List.of("Thường dùng với các trạng từ chỉ tần suất như always, usually",
//                            "Động từ chia nguyên thể ngoại trừ ngôi he/she/it"),
//                    List.of("I go to school.", "She watches TV every day.")
//            );
//            db.collection("Grammar").document(grammar.getIdGrammar()).set(grammar);
//        }
//
//        private void addVocabularyLessons() {
//            List<VocabularyLesson> vocabularyLessons = Arrays.asList(
//                    new VocabularyLesson("1", "1", "Match the word with its meaning", "url_to_image1", "Hello", Arrays.asList("Hello", "Goodbye")),
//                    new VocabularyLesson("2", "1", "Select the correct word", "url_to_image2", "Thank you", Arrays.asList("Please", "Thank you", "Sorry")),
//                    new VocabularyLesson("3", "2", "Choose the correct word: I am ___", "url_to_image3", "happy", Arrays.asList("happy", "sad", "angry")),
//                    new VocabularyLesson("4", "3", "What is the opposite of 'Goodbye'?", "url_to_image4", "Hello", Arrays.asList("Hello", "Please", "Thanks")),
//                    new VocabularyLesson("5", "3", "What is the opposite of 'hot'?", "url_to_image5", "cold", Arrays.asList("cold", "warm", "cool")),
//                    new VocabularyLesson("6", "4", "Choose the correct word: This is a ___", "url_to_image6", "dog", Arrays.asList("dog", "cat", "bird"))
//            );
//
//            for (VocabularyLesson vocabLesson : vocabularyLessons) {
//                db.collection("VocabularyLesson").document(vocabLesson.getIdVocabularyLesson()).set(vocabLesson);
//            }
//        }
//
//    private void addListeningExercises() {
//        List<Listening> listeningExercises = Arrays.asList(
//                new Listening("1", "1", "Hello!", "Xin chào", Arrays.asList("Xin", "chào")),
//                new Listening("2", "1", "Nice to meet you", "Rất vui được gặp bạn", Arrays.asList("Rất", "vui", "được", "gặp", "bạn")),
//                new Listening("3", "1", "What is your name", "Tên bạn là gì", Arrays.asList("Tên", "bạn", "là", "gì")),
//                new Listening("4", "2", "My name is Anna", "Tên tôi là Anna", Arrays.asList("Tên", "tôi", "là", "Anna")),
//                new Listening("5", "2", "Where do you live", "Bạn sống ở đâu?", Arrays.asList("Bạn", "sống", "ở", "đâu")),
//                new Listening("6", "3", "I live in New York", "Tôi sống ở New York", Arrays.asList("Tôi", "sống", "ở", "New", "York")),
//                new Listening("7", "1", "I go to school every day", "Tôi đi học mỗi ngày", Arrays.asList("Tôi", "đi", "học", "mỗi", "ngày")),
//                new Listening("8", "1", "She works in a hospital", "Cô ấy làm việc ở bệnh viện", Arrays.asList("Cô", "ấy", "làm", "việc", "ở", "bệnh", "viện")),
//                new Listening("9", "2", "I went to the park yesterday", "Tôi đã đi đến công viên hôm qua", Arrays.asList("Tôi", "đã", "đi", "đến", "công", "viên", "hôm", "qua")),
//                new Listening("10", "2", "She worked last week", "Cô ấy đã làm việc vào tuần trước", Arrays.asList("Cô", "ấy", "đã", "làm", "việc", "vào", "tuần", "trước"))
//        );
//
//        for (Listening listening : listeningExercises) {
//            db.collection("Listening").document(listening.getIdListening()).set(listening);
//        }
//    }
//
//    private void addIntroductions() {
//        List<Introduction> introductions = Arrays.asList(
//                new Introduction("1", "Basic Vocabulary and Grammar Rules", Arrays.asList("1", "2"), Arrays.asList("1", "2"), "1"),  // Thêm idLecture
//                new Introduction("2", "Conversational Phrases and Common Grammar", Arrays.asList("3", "4"), Arrays.asList("3", "4"), "2"),
//                new Introduction("3", "Formal and Informal Greetings", Arrays.asList("5", "6"), Arrays.asList("5", "6"), "3"),
//                new Introduction("4", "Phrasal Verbs and Common Idioms", Arrays.asList("7", "8"), Arrays.asList("7", "8"), "4"),
//                new Introduction("5", "Common Mistakes in English", Arrays.asList("9", "10"), Arrays.asList("9", "10"), "5")
//        );
//
//        for (Introduction introduction : introductions) {
//            db.collection("Introduction").document(introduction.getIdIntroduction()).set(introduction);
//        }
//    }
//
//    }
//
