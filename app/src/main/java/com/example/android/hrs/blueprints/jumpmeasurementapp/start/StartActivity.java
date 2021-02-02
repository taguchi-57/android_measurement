package com.example.android.hrs.blueprints.jumpmeasurementapp.start;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.hrs.blueprints.jumpmeasurementapp.Injection;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
//import com.example.android.hrs.blueprints.jumpmeasurementapp.statistics.StatisticsActivity;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.ActivityUtils;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.IdlingResource;

import com.example.android.hrs.blueprints.jumpmeasurementapp.R;

public class StartActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1000;

    private DrawerLayout mDrawerLayout;

    private StartPresenter mStartPresenter;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_act);

//        // ツールバーのセットアップ
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator();
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);  //ActionBar左上のホームボタンのイメージ変更(横線三本)
//        ab.setDisplayHomeAsUpEnabled(true);      //ActionBarに戻るボタンを作る
//
//
//        // Set up the navigation drawer.
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);     //ナビゲーションバーとかのバックグラウンド
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null) {
//            setupDrawerContent(navigationView);
//        }
//
        StartFragment startFragment =
                (StartFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (startFragment == null) {
            // Create the Fragment
            startFragment = StartFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),startFragment, R.id.contentFrame);
        }

        // Create the presenter
        mStartPresenter = new StartPresenter(
                Injection.provideMediumRepository(getApplicationContext()), startFragment);


        // 可能な場合は、以前い保存した状態をロードします。
        if(savedInstanceState != null) {
            //アーキテクチャサンプルではフィルタの保持と化しているみたいだけど今回はいらないので無し
        }

        //パーミッション確認及び許可-----------------------------------------------------------------------
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        //------------------------------------------------------------------------------------
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    /*
//    *  ナビゲーションの扱いはまだ決めていないので
//    * */
//    private void setupDrawerContent(NavigationView navigationView) {
//
//    }

//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
////                            case R.id.list_navigation_menu_item:
////                                // Do nothing, we're already on that screen
////                                break;
////                            case R.id.statistics_navigation_menu_item:
////                                Intent intent =
////                                        new Intent(TasksActivity.this, StatisticsActivity.class);
////                                startActivity(intent);
////                                break;
//                            default:
//                                break;
//                        }
//                        // Close the navigation drawer when an item is selected.
//                        menuItem.setChecked(true);
//                        mDrawerLayout.closeDrawers();
//                        return true;
//                    }
//                });
//    }





    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }



}
