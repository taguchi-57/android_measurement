package com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect;

import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.IdlingResource;

import com.example.android.hrs.blueprints.jumpmeasurementapp.Injection;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.ActivityUtils;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;

public class MediumSelectActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;   //Todo これがまだ全く理解できていない何のために存在しているのか

    private DrawerLayout mDrawerLayout;

    private MediumSelectPresenter mMediumSelectPresenter;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediumselect_act);

        //　ツールバーのセットアップ
        //  ナビゲーションバーのセットアップ


        MediumSelectFragment mediumSelectFragment =
                (MediumSelectFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(mediumSelectFragment == null) {
            //create the fragment
            mediumSelectFragment = mediumSelectFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mediumSelectFragment,R.id.contentFrame);
        }

        mMediumSelectPresenter = new MediumSelectPresenter(
                Injection.provideLocalMediumRepository(getApplicationContext()),mediumSelectFragment);

        //load previouslkyt saved state, if available.
        if (savedInstanceState != null) {
            // 前の状態に基づく部分は今のところなしと考えておく

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //特に今のところ情報を登録することもない
//        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }



    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

}
