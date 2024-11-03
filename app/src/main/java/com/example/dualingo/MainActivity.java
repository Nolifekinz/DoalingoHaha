package com.example.dualingo;

import android.content.Intent;
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

        // Thêm fragment đầu tiên là HomeFragment
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

    // Phương thức để thêm fragment đầu tiên
    private void setInitialFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();
    }

    // Phương thức chuyển đổi fragment bằng show và hide
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Ẩn các fragment đang hiện
        for (Fragment frag : fragmentManager.getFragments()) {
            transaction.hide(frag);
        }

        // Hiển thị fragment mong muốn
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.frameLayout, fragment);
        }

        transaction.commit();
    }
}
