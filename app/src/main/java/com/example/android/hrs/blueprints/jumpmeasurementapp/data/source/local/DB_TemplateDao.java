package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;

import java.util.List;

@Dao
public interface DB_TemplateDao {

    /**
     * 全てのカラムを取得
     */
    @Query("SELECT * FROM templateMedia")
    List<TemplateDb> getAllDBTemplate();



    /*
     * idでテンプレートを選択する。
     * */
    @Query("SELECT * FROM templateMedia WHERE template_id = :templateId")
    TemplateDb getTemplateById(String templateId);

    /*
    * インサート、もし競合したら更新
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemplate(TemplateDb templateDb);

   /*
   * TODO アップデート処理今回は使わない
   * */


   /*
   * 削除機能の追加
   * */
   @Query("DELETE FROM templateMedia WHERE template_id = :templateId")
   int deleteTemplateId(String templateId);


//   /*
//   * 全てのテンプレートを削除する
//   * Todo ここを生かすとエラーが出る　　多分DB系のエラーのため後回し
//   * */
//   @Query("DELETE FROM　templateMedia")
//   void deletetemplate();


}
