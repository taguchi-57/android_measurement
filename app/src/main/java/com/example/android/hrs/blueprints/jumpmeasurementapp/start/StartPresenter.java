package com.example.android.hrs.blueprints.jumpmeasurementapp.start;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.start.StartContract;

import static com.google.common.base.Preconditions.checkNotNull;

public class StartPresenter implements StartContract.Presenter{

    private final MediumRepository mMdiumRepository;

    private final StartContract.View mStartView;

    private boolean mFirstLoad = true;

    // コンストラクタ
    public StartPresenter(@NonNull MediumRepository mediumRepository, @NonNull StartContract.View mediumView) {
        mMdiumRepository = checkNotNull(mediumRepository, "tasksRepository cannot be null");
        mStartView = checkNotNull(mediumView, "tasksView cannot be null!");

        mStartView.setPresenter(this);

    }



    @Override
    public void start() {
        // 最初にDBからデータを読み込む必要も特にないが一応作っておく
    }

    @Override
    public void result(int requestCode, int resultCode) {
////         If a task was successfully added, show snackbar
//        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
//            mMediumSelectView.showSuccessfullySavedMessage();
//        }
    }


    @Override
    public void openRegisteredMedium() {
        // アクティビティ遷移　登録済みの動画
        mStartView.showRegisteredMedium();

    }

    @Override
    public void openMediumSelect() {
        //アクティビティの遷移   メディアの選択
        mStartView.showMediumSelect();

    }




}
