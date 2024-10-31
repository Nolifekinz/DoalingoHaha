// ViewPagerAdapter.java
package com.example.dualingo.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dualingo.GrammarFragment;
import com.example.dualingo.VocabularyFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new VocabularyFragment();
            case 1:
                return new GrammarFragment();
            default:
                return new VocabularyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng fragment là 2
    }
}
