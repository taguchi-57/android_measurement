package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.hrs.blueprints.jumpmeasurementapp.Injection;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.TemplateRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.MediumDatabase;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.TemplateDatabase;
import com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect.MediumSelectFragment;
import com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect.MediumSelectPresenter;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.ActivityUtils;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.AppExecutors;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.isseiaoki.simplecropview.CropImageView;

import static com.google.common.base.Preconditions.checkNotNull;

/*
* 画像処理のメイン処理を行う画面
* 表示項目：動画のサムネイル(動画再生できたらベストなんであろう)※※※※要検討
*      　:タイトル(text)　MediumSelectで登録した動画際のタイトル
* 　　　　　：日付　　　　　　MediumSelectで動画を登録した際の日付
* */
public class ImageProssesingActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;;
    public static final String EXTRA_TASK_ID = "ok";

    public static final String EXTRA_MEDIA_ID = "MEDIA_ID";

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    String mediaId;
    ImageProssesingFragment imageProssesingFragment;
    SelectTemplateFragment selectTemplateFragment;
    FragmentManager fragmentManager;


    private ImageProssesingPresenter mImageProssesingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageprossesing_act);

        mediaId = getIntent().getStringExtra(EXTRA_MEDIA_ID);
        fragmentManager = getSupportFragmentManager();

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }
        // Todo 動画処理選択フラグメント
        deleteFragment2();
        switchToFragment2();

        // Todo ボトムナビゲーション////////////////////////////////////////////////////////
        // ボトムナビゲーションを読み込む
        BottomNavigationView bottomavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // BottomNavigationViewHelperでアイテムのサイズ、アニメーションを調整
        BottomNavigationViewHelper.disableShiftMode(bottomavigation);
        // BottomNavigationViewを選択したときのリスナー
        bottomavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // 各選択したときの処理
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                        deleteFragment1();
                        switchToFragment1();
                        return true;
                    case R.id.nav_slideshow:
                        // Todo 動画処理選択フラグメント
                        deleteFragment2();
                        switchToFragment2();
                        return true;
                }
                return false;
            }
        });

/////////////////Todo end ボトムナビゲーション///////////////////////////
        //load previouslkyt saved state, if available.
        if (savedInstanceState != null) {
            // 前の状態に基づく部分は今のところなしと考えておく
        }
    }

    // Todo ボトムナビゲーションでのテンプレートフラグメントへの遷移
    public void switchToFragment1() {
        selectTemplateFragment =
                (SelectTemplateFragment) fragmentManager.findFragmentById(R.id.contentFrame);
            // Todo テンプレート選択フラグメント
            selectTemplateFragment = selectTemplateFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), selectTemplateFragment, R.id.contentFrame);
        TemplateDatabase database = TemplateDatabase.getInstance(getApplicationContext());
        TemplateRepository templateRepository = new TemplateRepository(new AppExecutors(),
                database.db_templateDao());

        // create the presenter
        new SelectTemplatePresenter(
                mediaId,
                templateRepository,
                selectTemplateFragment);
    }
    // Todo ボトムナビゲーションでの動画処理フラグメントへの遷移
    public void switchToFragment2() {
        imageProssesingFragment =
                (ImageProssesingFragment) fragmentManager.findFragmentById(R.id.contentFrame2);
            imageProssesingFragment = imageProssesingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), imageProssesingFragment,R.id.contentFrame2);

        TemplateDatabase database_template = TemplateDatabase.getInstance(getApplicationContext());
        TemplateRepository templateRepository_imageprossesing = new TemplateRepository(new AppExecutors(),
                database_template.db_templateDao());

        // create the presenter
        new ImageProssesingPresenter(
                mediaId,
                Injection.provideMediumRepository(getApplicationContext()),
                imageProssesingFragment,
                templateRepository_imageprossesing);
    }

    // Todo テンプレートクリックでの動画処理フラグメントの起動
    /*
    * @param bundle Bundle
    * */
    public void switchToFragment3(Bundle bundle) {
        BottomNavigationView bottomavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // BottomNavigationViewHelperでアイテムのサイズ、アニメーションを調整
        BottomNavigationViewHelper.disableShiftMode(bottomavigation);
        deleteFragment2();

        imageProssesingFragment =
                (ImageProssesingFragment) fragmentManager.findFragmentById(R.id.contentFrame2);
        imageProssesingFragment = imageProssesingFragment.newInstance();
        imageProssesingFragment.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), imageProssesingFragment,R.id.contentFrame2);

        TemplateDatabase database_template = TemplateDatabase.getInstance(getApplicationContext());
        TemplateRepository templateRepository_imageprossesing = new TemplateRepository(new AppExecutors(),
                database_template.db_templateDao());
        // create the presenter
        new ImageProssesingPresenter(
                mediaId,
                Injection.provideMediumRepository(getApplicationContext()),
                imageProssesingFragment,
                templateRepository_imageprossesing);
    }

    // Todo 動画処理フラグメントの削除  ???
    public void deleteFragment1(){
        if(imageProssesingFragment != null) {
//            getSupportFragmentManager().beginTransaction().detach(imageProssesingFragment;
            fragmentManager.beginTransaction().detach(imageProssesingFragment).commit();
            ActivityUtils.removeFragmentToActivity(
                    getSupportFragmentManager(), imageProssesingFragment);
            imageProssesingFragment.onDestroy();

        }
    }
    // Todo テンプレートフラグメントの削除  ???
    public  void deleteFragment2() {
        if(selectTemplateFragment != null) {
            fragmentManager.beginTransaction().detach(selectTemplateFragment).commit();
            ActivityUtils.removeFragmentToActivity(
                    getSupportFragmentManager(), selectTemplateFragment);
            selectTemplateFragment.onDestroy();
        }
    }

}
