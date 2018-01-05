package com.msevgi.relaxingsounds.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.databinding.MainBinding;
import com.msevgi.relaxingsounds.ui.fragment.BaseFragment;
import com.msevgi.relaxingsounds.ui.fragment.CategoryFragment;
import com.msevgi.relaxingsounds.ui.fragment.LikedSoundsFragment;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class MainActivity extends AppCompatActivity {

    public static final int TRANSITION_TYPE_ADD = 1;
    public static final int TRANSITION_TYPE_REPLACE = 2;

    @IntDef({TRANSITION_TYPE_ADD, TRANSITION_TYPE_REPLACE})
    public @interface transitionType {
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new CategoryFragment(), TRANSITION_TYPE_REPLACE);
                    return true;
                case R.id.navigation_favorites:
                    showFragment(new LikedSoundsFragment(), TRANSITION_TYPE_ADD);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            showFragment(new CategoryFragment(), TRANSITION_TYPE_REPLACE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showFragment(BaseFragment fragment, @transitionType int transitionType) {
        if (!isDestroyed()) {
            String tag = fragment.getClass().getName();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (transitionType) {
                case TRANSITION_TYPE_ADD:
                    transaction.add(R.id.flContainer, fragment, tag);
                    break;
                case TRANSITION_TYPE_REPLACE:
                    transaction.replace(R.id.flContainer, fragment, tag);
                    break;
            }
            transaction.addToBackStack(tag).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
    }
}
