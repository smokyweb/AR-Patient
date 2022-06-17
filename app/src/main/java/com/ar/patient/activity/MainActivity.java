package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ar.patient.R;
import com.ar.patient.databinding.ActivityMainBinding;
import com.ar.patient.fragment.EditProfileFragment;
import com.ar.patient.fragment.LeaderBoardFragment;
import com.ar.patient.fragment.MenuFragment;
import com.ar.patient.fragment.MyProfileFragment;
import com.ar.patient.fragment.PaitentListFragment;
import com.ar.patient.helper.Config;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean doubleBackToExitPressedOnce = false;
    private ActivityMainBinding binding;
    private Stack<Fragment> fragmentStack;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        intiviews();
    }

    private void intiviews() {
        binding.linPaitentList.setOnClickListener(this);
        binding.linleaderboard.setOnClickListener(this);
        binding.linMyProfile.setOnClickListener(this);
        binding.linmenu.setOnClickListener(this);

        binding.linPaitentList.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linPaitentList:
                binding.Imgpaitentlist.setSelected(true);
                binding.Imgleaderboard.setSelected(false);
                binding.ImgProfile.setSelected(false);
                binding.ImgMenu.setSelected(false);
                Loadfragment();
                tabReferesh();
                break;
            case R.id.linleaderboard:
                binding.Imgleaderboard.setSelected(true);
                binding.Imgpaitentlist.setSelected(false);
                binding.ImgProfile.setSelected(false);
                binding.ImgMenu.setSelected(false);
                addFragment(new LeaderBoardFragment());
                tabReferesh();
                break;
            case R.id.linMyProfile:
                binding.ImgProfile.setSelected(true);
                binding.Imgpaitentlist.setSelected(false);
                binding.Imgleaderboard.setSelected(false);
                binding.ImgMenu.setSelected(false);
                addFragment(new MyProfileFragment());
                tabReferesh();
                break;
            case R.id.linmenu:
                binding.ImgMenu.setSelected(true);
                binding.Imgpaitentlist.setSelected(false);
                binding.Imgleaderboard.setSelected(false);
                binding.ImgProfile.setSelected(false);
                addFragment(new MenuFragment());
                tabReferesh();
                break;

        }
    }

    public void tabReferesh() {
        Config.IS_PATIENT_DASHBOARD_REFERESH = false;
        Config.IS_LEADERBOARD_DASHBOARD_REFERESH = false;
        Config.IS_MYPROFILE_DASHBOARD_REFERESH = false;
        Config.IS_MENU_DASHBOARD_REFERESH = false;
    }

    public void Loadfragment() {
        fragmentStack = new Stack<Fragment>();
        fragment = new PaitentListFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.flMain, fragment);
        fragmentStack.push(fragment);
        ft.commit();
    }

    public void addFragment(Fragment fragment) {
        this.fragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flMain, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    public void exit() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_click), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
            if (fragment instanceof MyProfileFragment) {
                exit();
            } else if (fragment instanceof EditProfileFragment) {
                super.onBackPressed();
                addFragment(new MyProfileFragment());
            } else {
                super.onBackPressed();
            }
        } else {
            exit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("mytag", "On home activity result accures");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
        if (fragment instanceof EditProfileFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (fragment instanceof PaitentListFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}