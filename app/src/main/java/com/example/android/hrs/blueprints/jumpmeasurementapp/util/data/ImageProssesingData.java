package com.example.android.hrs.blueprints.jumpmeasurementapp.util.data;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class ImageProssesingData {

    List<Integer> mFrame_no = new ArrayList<Integer>();
    List<Float> mMount_frame_time = new ArrayList<Float>();
    List<Mat> mResult_mat = new ArrayList<Mat>();
    String mMediaPath;
    String mId;   // idはMediaModelから持ってきたものがよい

    ////////////////////////////////////////////////////////////////
    //csv関連の処理は一次的なものになるかも知れない
    FileWriter fw;
    PrintWriter pw;
    ///////////////////////////////////////////////////////////////

    public ImageProssesingData(String mediaPath, String id) {
        // Todo Listの初期化のについてはベストプラクティスを要検討
        mMediaPath = mediaPath;
        mId = id;
    }

    /*
    *
    * @param frame_mo
    * @param mount_frame_time
    * @param result_mat
    * */
    public void adImageProssesing(Integer frame_no, Float mount_frame_time
                                    ) {
        mFrame_no.add(frame_no);
        mMount_frame_time.add(mount_frame_time);
//        mResult_mat.add(result_mat);
    }

    public void createCSV(String path){

        try {
            //出力先を作成する
//            FileWriter fw = new FileWriter(Environment.getDataDirectory().getPath()+"/test.csv", false);
            fw = new FileWriter(path, false);
            pw = new PrintWriter(new BufferedWriter(fw));
            //内容を指定する
//            pw.print("a");
//            pw.print(",");
//            pw.print("b");
////            pw.println();
//            for (Integer p : mFrame_no) {
//
//                pw.print(p);
//                pw.print(",");
//                pw.println();
//            }
//
//
//            pw.print("01");
//            pw.print(",");
//            pw.print("02");
//            pw.println();

//            //ファイルに書き出す
//            pw.close();
//
//            //終了メッセージを画面に出力する
//            System.out.println("出力が完了しました。");

        } catch (IOException ex) {
            //例外時処理
            ex.printStackTrace();
        }

    }

    public void closeCSV(){

        pw.close();

        //終了メッセージを画面に出力する
        System.out.println("出力が完了しました。");

    }


    public void adCSV(Integer frame_no, Float mount_frame_time ,double max_val, Point matchLoc,double Threshold) {

        pw.print(frame_no);
        pw.print(",");
        pw.print(mount_frame_time);
        pw.print(",");
        pw.print(matchLoc);
        pw.print(",");
        pw.print(max_val);
        pw.print(",");
        pw.print(Threshold);
        pw.println();


    }








}
