package com.example.mostafaeisam.movieschallenge.activites;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.mostafaeisam.movieschallenge.fragments.DiscoverFragment;
import com.example.mostafaeisam.movieschallenge.fragments.MyListFragment;
import com.example.mostafaeisam.movieschallenge.fragments.NewsFragment;
import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.fragments.SearchFragment;
import com.example.mostafaeisam.movieschallenge.fragments.SettingsFragment;
import java.lang.reflect.Field;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btv_buttons)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        defultFragment();

        BottomNavigationViewHelper.removeShiftMode(mBottomNavigationView);
        mBottomNavigationView.setSelectedItemId(R.id.action_discover);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Bundle params = new Bundle();
                params.putString("Fragments", ""+item.getItemId());

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.action_discover:
                        DiscoverFragment discoverFragment = new DiscoverFragment();
                        fragmentTransaction.replace(R.id.fragment_container, discoverFragment);
                        break;
                    case R.id.action_myList:
                        MyListFragment myListFragment = new MyListFragment();
                        fragmentTransaction.replace(R.id.fragment_container, myListFragment);
                        break;
                    case R.id.action_search:
                        Toast.makeText(MainActivity.this, "Search Clicked", Toast.LENGTH_SHORT).show();
                        SearchFragment searchFragment = new SearchFragment();
                        fragmentTransaction.replace(R.id.fragment_container, searchFragment);
                        break;
                    case R.id.action_news:
                        Toast.makeText(MainActivity.this, "News Clicked", Toast.LENGTH_SHORT).show();
                        NewsFragment newsFragment = new NewsFragment();
                        fragmentTransaction.replace(R.id.fragment_container, newsFragment);
                        break;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        SettingsFragment settingsFragment = new SettingsFragment();
                        fragmentTransaction.replace(R.id.fragment_container, settingsFragment);
                        break;
                }
                //commit
                fragmentTransaction.commit();
                return true;
            }
        });

    }

    private void defultFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DiscoverFragment discoverFragment = new DiscoverFragment();
        fragmentTransaction.add(R.id.fragment_container, discoverFragment);
        fragmentTransaction.commit();
    }

    // Used to Stop BottomNavigationButtons Animation
    static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }

}
