package com.example.android.hrs.blueprints.jumpmeasurementapp.data;

import static com.google.common.base.Preconditions.checkNotNull;

//　ローカルにあるmedexia

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.UUID;

public class LocalMedia {

    // privateでいいのか？？
    private String mId;      //
    private String mMediaPath;  //mediapath名
    private Boolean mChecked = false;   //チェック済みか否か


    public LocalMedia(String mediaPath) {
        //こういったクラスのコンストラクタの持ち方がよく割らない。
        // メンバ変数として初期化しているのなら特別な処理はいらないのか
        mMediaPath = mediaPath;
        mChecked = false;
        // 自動作成のIDみたいなのと考えています。
        mId = UUID.randomUUID().toString();
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id (copy of
     * another Task).
     * タスクにすでにID（別のタスクのコピー）がある場合は、
     * このコンストラクターを使用して、完了したタスクを指定します。
     * @param mediaPath       title of the task
     * @param edit description of the task
     * @param id          id of the task
//     * @param completed   true if the task is completed, false if it's active
     */
    public LocalMedia(@Nullable String mediaPath,@NonNull String id,
                      boolean edit) {
        mMediaPath = mediaPath;
        mId = id;
        mChecked = edit;

//        mCompleted = completed;
    }



    public String getId() {
        return mId;
    }

    public String getMediaPath() { return mMediaPath; }

    public boolean isCompleted() {
        return mChecked;
    }

    public boolean isChecked() {
        return !mChecked;
    }



//    public void completeMedia(@NonNull String mediapath, Boolean Check) {
//        checkNotNull(mediapath);
//        mChecked = Check;
//    }

    public String getTitleForList() {
        return mMediaPath;
    }

    public Boolean getCheckedForList() {
        return mChecked;
    }



}
