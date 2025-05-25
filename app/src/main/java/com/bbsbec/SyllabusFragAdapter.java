package com.bbsbec;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SyllabusFragAdapter extends FragmentStateAdapter {
    String base_file;
    public SyllabusFragAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String basefile) {
        super(fragmentManager, lifecycle);
        this.base_file = basefile;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle args = new Bundle();
        args.putString("BASEFILE",base_file);
        if (position == 0){
            fragment = new SyllabusTheoryFragement();
        }else {
            fragment = new SyllabusPracticalFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
