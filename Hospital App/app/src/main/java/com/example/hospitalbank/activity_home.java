package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.hospitalbank.Classes.RegionLanguage;
import com.example.hospitalbank.Fragments.fragment_chat;
import com.example.hospitalbank.Fragments.fragment_donor;
import com.example.hospitalbank.Fragments.fragment_home;
import com.example.hospitalbank.Fragments.fragment_requests;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class activity_home extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    Deque<Integer> mDeque = new ArrayDeque<>(4);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationbar);

        initializing();

        //first fragment in mDeque list
        mDeque.push(R.id.home_page);
        loadFragment(new fragment_home());
        mBottomNavigationView.setSelectedItemId(R.id.home_page);

        runBottomNavigation();

    }

    private void initializing(){
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        new RegionLanguage(this);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linear_navigation, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void runBottomNavigation() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (mDeque.contains(id)) {
                    if (id == R.id.home) {
                        if (mDeque.size() != 1) {
                            if (flag) {
                                mDeque.addFirst(R.id.home_page);
                                flag = false;
                            }
                        }
                    }
                    //remove selected from deque
                    mDeque.remove(id);
                }
                //push selected id in deque
                mDeque.push(id);
                loadFragment(getFragment(item.getItemId()));
                return true;
            }
        });
    }

    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.home_page:
                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new fragment_home();

            case R.id.request_page:
                mBottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new fragment_requests();

            case R.id.donor_page:
                mBottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new fragment_donor();

            case R.id.chat_page:
                mBottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new fragment_chat();
        }
        mBottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new fragment_home();
    }

    @Override
    public void onBackPressed() {
        //open previous fragment
        mDeque.pop();
        if (!mDeque.isEmpty()) {
            loadFragment(getFragment(mDeque.peek()));
        } else {
            finish();
        }
    }
}