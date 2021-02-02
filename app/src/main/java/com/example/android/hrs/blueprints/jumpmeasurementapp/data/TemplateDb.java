package com.example.android.hrs.blueprints.jumpmeasurementapp.data;


// Todo テンプレート画像の保存クラス

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

import javax.annotation.Nonnull;

/*
* Todo テンプレート画像の保存を行う、リンクするのはMediaModelのidの予定
* */
@Entity(tableName = "templateMedia")
public final class TemplateDb {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "template_id")
    private final String mId;

    @Nullable
    @ColumnInfo(name ="template_blob")
    private final byte[] mTemplate_blob;

    // MediaModelのぷらいまりーID
    @Nullable
    @ColumnInfo(name= "media_model_id")
    private  final String mMediaModelBlobId;


    /*
    * このコンストラクターを使用して、新しいメディアを作成する
    *
    * */
    @Ignore
    public TemplateDb(@Nullable byte[] template_blob,
                           @Nullable String mediaModelBlobID){
        this(UUID.randomUUID().toString(), template_blob,mediaModelBlobID);
    }

    /*
    *   コンストラクタ
    */
    public TemplateDb(@Nullable String id,@Nullable byte[] template_blob,
                      @Nullable String mediaModelBlobId){
        mId = id;
        mTemplate_blob = template_blob;
        mMediaModelBlobId = mediaModelBlobId;
    }


    @Nonnull
    public String getId() { return mId; }

    @Nullable
    public byte[] getTemplate_blob() { return mTemplate_blob; }

    @Nullable
    public String getMediaModelBlobId() { return mMediaModelBlobId; }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if( o == null || getClass() != o.getClass()) return false;
        TemplateDb templateDb = (TemplateDb) o;
        return  Objects.equal(mId,templateDb.mId) &&
                Objects.equal(mTemplate_blob, templateDb.mTemplate_blob) &&
                Objects.equal(mMediaModelBlobId, templateDb.mMediaModelBlobId);
    }






}

