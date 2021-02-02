package com.example.android.hrs.blueprints.jumpmeasurementapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

import javax.annotation.Nonnull;


/*
 * mediaselectで使うメディアモデルの不変モデルクラス
 * */
@Entity(tableName = "medium")
public final class MediaModel {
    // ID
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "path")
    private final String mPath;

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = "resisreddate")
    private final String mDate;

    /*
    * このコンストラクターを使用して、新しいメディアモデルを作成する。
    * @param path
    * @param title
    * */
    @Ignore
    public MediaModel(@Nullable String path,@Nullable String title, @Nullable String date) {
        this(title,UUID.randomUUID().toString(),path,date);
    }

    /*
    * コンストラクタ
    * */
    public MediaModel(@Nullable String title, @Nullable String id, @Nullable String path, @Nullable String date) {
        mId = id;
        mPath = path;
        mDate = date;
        mTitle = title;
    }

    @Nonnull
    public String getId() { return mId; }

    @Nullable
    public String getPath() { return mPath; }

    @Nullable
    public String getTitle() { return mTitle; }

    @Nullable
    public String getPathForList() {
        if (!Strings.isNullOrEmpty(mPath)) {
            return mPath;
        } else {
            //タイトルが入っていない場合に何を返すかは用件等
            return "No";
        }
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mPath)) {
            return mTitle;
        } else {
            return "No";
        }

    }

    @Nullable
    public String getDateForList() {
        if (!Strings.isNullOrEmpty(mDate)) {
            return mDate;
        } else {
            return  "No";
        }
    }




    @Nullable
    public String getDate() { return mDate; }

    // 空かチェック、タイトルとパス、データはどうするか不明
    public boolean isEmpty() { return Strings.isNullOrEmpty(mTitle) &&
            Strings.isNullOrEmpty(mId) &&
            Strings.isNullOrEmpty(mDate) &&
            Strings.isNullOrEmpty(mPath)
            ;}

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if( o == null || getClass() != o.getClass()) return false;
        MediaModel mediaModel = (MediaModel) o;
        return  Objects.equal(mId,mediaModel.mId) &&
                Objects.equal(mTitle, mediaModel.mTitle) &&
                Objects.equal(mDate, mediaModel.mDate) &&
                Objects.equal(mPath,  mediaModel.mPath);
    }
}
