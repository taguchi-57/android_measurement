package com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium;


import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.IdlingResource;

import com.example.android.hrs.blueprints.jumpmeasurementapp.Injection;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.ActivityUtils;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;

/*
* DB(SQLite)に登録済みの動画ファイル
* */
public class ResisteredMediumActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    private DrawerLayout mDrawerLayout;

    private ResisteredMediumPresenter mResisteredMediumPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resisteredmedium_act);

        // set up toolbar.
        // 無し

        // Setupnavigation drawer
        // 無し

        ResisteredMediumFragment resisteredMediumFragment =
                (ResisteredMediumFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(resisteredMediumFragment == null) {
            //create the fragment
            resisteredMediumFragment = ResisteredMediumFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), resisteredMediumFragment, R.id.contentFrame);
        }

        mResisteredMediumPresenter = new ResisteredMediumPresenter(
                Injection.provideMediumRepository(getApplicationContext()), resisteredMediumFragment);

        // load previously saved state, if available.
        if(savedInstanceState != null) {
                //前回読み込んだ際の情報は特別今のところ要らない
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingesource() {
        return EspressoIdlingResource.getIdlingResource();
    }




}
