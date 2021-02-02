package com.example.android.hrs.blueprints.jumpmeasurementapp.start;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect.MediumSelectActivity;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ResisteredMediumActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class StartFragment extends Fragment implements StartContract.View {

    private StartContract.Presenter mPresenter;

    private ImageButton mRegisteredButton;

    private ImageButton mMediumSelectButton;

    public StartFragment() {
        // コンストラクタ
    }


    // シングルトン？？
    public static StartFragment newInstance() {
        return new StartFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mListAdapter = new MediaSelectAdapter(new ArrayList<MediaModel>(0), mMediaItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@Nonnull StartContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /*
     * todoアプリだとこれは何に使っている？？
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.start_frag, container, false);

        // スタートフラグメントのセットアップ
        mMediumSelectButton = (ImageButton) root.findViewById(R.id.medium_select_button);
        mRegisteredButton = (ImageButton) root.findViewById(R.id.registered_button);

        mMediumSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openMediumSelect();
            }
        });

        mRegisteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openRegisteredMedium();
            }
        });
        return root;

    }

    @Override
    public void showMediumSelect() {
        Intent intent = new Intent(getContext(), MediumSelectActivity.class);
        startActivityForResult(intent, MediumSelectActivity.REQUEST_ADD_TASK);
    }


    @Override
    public void showRegisteredMedium() {
        Intent intent = new Intent(getContext(), ResisteredMediumActivity.class);
        startActivityForResult(intent, ResisteredMediumActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.test));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}
