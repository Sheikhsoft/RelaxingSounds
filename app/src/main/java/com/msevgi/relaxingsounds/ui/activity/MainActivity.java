package com.msevgi.relaxingsounds.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.databinding.MainBinding;
import com.msevgi.relaxingsounds.ui.fragment.BaseFragment;
import com.msevgi.relaxingsounds.ui.fragment.CategoryFragment;
import com.msevgi.relaxingsounds.ui.fragment.LikedSoundsFragment;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class MainActivity extends MediaBaseActivity {

    public static final int TRANSITION_TYPE_ADD = 1;
    public static final int TRANSITION_TYPE_REPLACE = 2;
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION = "agrre";
    public static final String EXTRA_START_FULLSCREEN = "fagerer";

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
        final MainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            showFragment(new CategoryFragment(), TRANSITION_TYPE_REPLACE);
            mBinding.navigation.setSelectedItemId(R.id.navigation_home);
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
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContainer);
        if (fragment instanceof CategoryFragment) {
            finish();
        } else
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
