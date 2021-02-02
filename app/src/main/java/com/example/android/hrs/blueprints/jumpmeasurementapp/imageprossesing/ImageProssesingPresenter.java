package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.TemplateDataSorce;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.TemplateRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ResisteredMediumContract;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.DbBitmapUtility;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.data.ImageProssesingData;
import com.google.common.base.Strings;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImageProssesingPresenter implements ImageProssesingContract.Presenter{

    private final MediumRepository mMediumRepository;

    private final TemplateRepository mTemplateRepository;

    private final ImageProssesingContract.View mImageProssesingView;

    private boolean mFirstLoad = true;
    String mPath;
    String mId;

    // Todo プログレッシブバー周りの処理
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    // Todo よくわからないがこれがないと上手く行かない
    static {
        System.loadLibrary("opencv_java4");
    }

    @Nullable
    private String mMediaId;


    public ImageProssesingPresenter(@Nullable String mediaId, @NonNull MediumRepository mediumRepository,
                                @NonNull ImageProssesingContract.View imageProssesingView,
                                    @NonNull TemplateRepository templateRepository) {
        mMediaId = mediaId;
        mMediumRepository = checkNotNull(mediumRepository);
        mTemplateRepository = checkNotNull(templateRepository);
        mImageProssesingView = checkNotNull(imageProssesingView);
        mImageProssesingView.setPresenter(this);
    }

    @Override
    public void start() {
        openMedia();
    }

    @Override
    public void saveTemplate(Bitmap bmp){

        createTemplate(bmp);

    }

    private void createTemplate(Bitmap bmp){
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        byte[] template_byte = dbBitmapUtility.getBytes(bmp);
        TemplateDb newTemplate = new TemplateDb(template_byte,mId);
        mTemplateRepository.saveTemplate(newTemplate);


    }






    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        // 次への遷移？？はパス
//        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
//            mMediumView.showSuccessfullySavedMessage();
//        }
    }



///////////////////////////////////////////////////////////////////////////////////////////
    private void openMedia() {
        if (Strings.isNullOrEmpty(mMediaId)) {
            mImageProssesingView.showMissingMedia();
            return;
        }

        mImageProssesingView.setLoadingIndicator(true);
        mMediumRepository.getMedia(mMediaId, new MediumDataSource.GetMediaCallBack() {
            @Override
            public void onMediaLoaded(MediaModel media) {
                // The view may not be able to handle UI updates anymore
                if (!mImageProssesingView.isActive()) {
                    return;
                }
                mImageProssesingView.setLoadingIndicator(false);
                if (null == media) {
                    mImageProssesingView.showMissingMedia();
                } else {
                    showMedia(media);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mImageProssesingView.isActive()) {
                    return;
                }
                mImageProssesingView.showMissingMedia();
            }
        });
    }

    // 画像の一フレームの確保と表示
    private void showMedia(@NonNull MediaModel media) {

        mPath = media.getPath();
        Bitmap bmp = getFtirstFrame(media.getPath());
        mImageProssesingView.showCropImage(media.getPath(),bmp);
    }

    // テンプレートから選択した画像の表示
    @Override
    public  void showTemplate(@NonNull String id){
        openTemplate(id);
    }


    private void openTemplate(@NonNull String id) {
        mTemplateRepository.getTemplate(id, new TemplateDataSorce.GetTemplateCallBack(){
            @Override
            public void onTemplateLoaded(TemplateDb media) {
                // The view may not be able to handle UI updates anymore
                if (!mImageProssesingView.isActive()) {
                    return;
                }
                mImageProssesingView.setLoadingIndicator(false);
                if (null == media) {
                    mImageProssesingView.showMissingMedia();
                } else {
                    showSelectTemplate(media);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mImageProssesingView.isActive()) {
                    return;
                }
                mImageProssesingView.showMissingMedia();
            }
        });

    }
    // 画像の一フレームの確保と表示
    private void showSelectTemplate(@NonNull TemplateDb media) {

        byte[] mediablob = media.getTemplate_blob();
//        byte[] mediabyte = getFtirstFrame(media.getId());
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        Bitmap bmp = dbBitmapUtility.getImage(mediablob);
        mImageProssesingView.showCropedImage(bmp);
    }


////////////////////////////    //////////////////////////////////////////////////////////////////////////////////////

    // 動画ファイルの一フレーム目のビットマップを獲得するメソッド
    public Bitmap getFtirstFrame(String path) {
        Bitmap bmp = null;
        FFmpegMediaMetadataRetriever med = new FFmpegMediaMetadataRetriever();

        File file = new File(path);
        float mount_frame_time = 0.0f;

        if (file.exists())
        {
            File file2 = new File(path);

            if(file2.exists())
            {
                med.setDataSource(file2.getPath());

                String duration = med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                convertMillieToHMmSs(Long.valueOf(duration));
                Log.i("i*1000000", convertMillieToHMmSs(Long.valueOf(duration)));

                //総フレーム数
                double total_frame = Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)) /1000.0  * Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
                Log.i("総フレーム数", String.valueOf(total_frame));
                for (int i = 0; i < 1; i++) {
//                    double frameRate = Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
//
                    float per_frame = 1 / Float.parseFloat(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
                    mount_frame_time = mount_frame_time + per_frame;
                    int int_mount_frame_time = (int)(mount_frame_time * 1000 * 1000);
                    bmp = med.getFrameAtTime(int_mount_frame_time, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);


                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    return  rotatedBitmap;
                }
            }
        }
        return bmp;
    }



////////////////////////////////////////////////////////////////////////////////////////////////////

    // ToDo 以下は画像処理の本丸の処理。
    //      本来は別ファイルで行いたいところの処理なのだが、
    //      ロジックに集中したいのでとりあえずここでロジックだけ完成させる

    // ToDo 要検討項目一覧
    //      ・メインスレッドで行うため、UIにかかわる処理が停止し、処理落ちなど、アプリ設計上避けないといけない設計になる
    //      ・非同期処理は必須
    //      ・非同期に際は、UIを検討(ボタン押せなくするなど)しなければ、余計な処理で、エラーが起こるのが見える
    //      ・プログレッシブバーなりで進捗の表示は必須、ここにも非同期が絡んでくる。

    public void startImageProssesing(Bitmap bmp){

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/storage/emulated/0/Movies/B2.PNG");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageprossesing(bmp);
    }

    public void imageprossesing(final Bitmap temp_bmp) {

        //Todo 非同期処理
        new Thread(new Runnable() {
            public void run() {
                ImageProssesingData imageProssesingData = new ImageProssesingData(mPath,mId) ;

                FFmpegMediaMetadataRetriever med = new FFmpegMediaMetadataRetriever();

                File file = new File(mPath);
                float mount_frame_time = 0.0f;

                if (file.exists())
                {
                    File file2 = new File(mPath);

                    // テンプレートの処理
                    Mat rec_mat = new Mat();

//            Bitmap bm = BitmapFactory.decodeFile(file.getPath() + "/changed.jpg");
                    Utils.bitmapToMat(temp_bmp,rec_mat);
                    //resize(rec_mat, dst, dst.size(), rec_mat.rows() / 10, rec_mat.cols() / 10);
                    //  ToDO ここにテンプレートのフィルタリング処理------------------------------------------------------------
                    //  グレースケール
                    Imgproc.cvtColor(rec_mat, rec_mat, Imgproc.COLOR_RGB2GRAY);
                    // 　ソーベル
                    myFiltering(rec_mat);
                    //　　ガウシアン
                    rec_mat = gausian(rec_mat);
                    Imgproc.resize(rec_mat, rec_mat,
                            new Size(rec_mat.size().width * 0.5, rec_mat.size().height * 0.5));
                    //　　ガウシアン
                    rec_mat = gausian(rec_mat);
                    Imgproc.resize(rec_mat, rec_mat,
                            new Size(rec_mat.size().width * 0.5, rec_mat.size().height * 0.5));
                    //　　ガウシアン Todo ガウシアンの回数
                    rec_mat = gausian(rec_mat);
                    rec_mat = gausian(rec_mat);
                    rec_mat = gausian(rec_mat);
                    Imgproc.resize(rec_mat, rec_mat,
                            new Size(rec_mat.size().width * 0.5, rec_mat.size().height * 0.5));


                    if(file2.exists())
                    {
                        imageProssesingData.createCSV("/storage/emulated/0/Movies/B2.csv");

                        med.setDataSource(file2.getPath());

                        String duration = med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                        convertMillieToHMmSs(Long.valueOf(duration));
                        Log.i("i*1000000", convertMillieToHMmSs(Long.valueOf(duration)));

                        //総フレーム数
                        final double total_frame = Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)) /1000.0  * Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
                        Log.i("総フレーム数", String.valueOf(total_frame));
                        for (int i = 0; i < total_frame; i++) {

                            Float per_frame = 1 / Float.parseFloat(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));

                            mount_frame_time = mount_frame_time + per_frame;
                            int int_mount_frame_time = (int)(mount_frame_time * 1000 * 1000);
                            Bitmap bmp = med.getFrameAtTime(int_mount_frame_time, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);

                            // Todo //////////////////////////////////////////////////////////////
                            Matrix matrix_video = new Matrix();
                            matrix_video.postRotate(90);
                            Bitmap rotatedBitmap_video = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix_video, true);
                            bmp = rotatedBitmap_video;
                            //  ToDO End//////////////////////////////////////////////////////////////////////////////////////



                            Mat mat = new Mat();

                            Utils.bitmapToMat(bmp,mat);
                            //  ToDO ここにフレームのフィルタリング処理ーーーーーーーーーーーーーーーーーーーーーーーーーーーー
                            //  グレースケール
                            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
                            // 　ソーベル
                            myFiltering(mat);
                            //　　ガウシアン
                            mat = gausian(mat);
                            Imgproc.resize(mat, mat,
                                    new Size(mat.size().width * 0.5, mat.size().height * 0.5));
                            //　　ガウシアン
                            mat = gausian(mat);
                            Imgproc.resize(mat, mat,
                                    new Size(mat.size().width * 0.5, mat.size().height * 0.5));
                            //　　ガウシアン   Todo ガウシアンの回数
                            mat = gausian(mat);
                            mat = gausian(mat);
                            mat = gausian(mat);
                            Imgproc.resize(mat, mat,
                                    new Size(mat.size().width * 0.5, mat.size().height * 0.5));



                            // ToDO フレームのフィルタリング処理終了--------------------------------------------------------------
//////////////////////////////////////////////////////////////////////////////////////////////////
                            Mat result = new Mat(mat.rows() - rec_mat.rows() + 1, mat.cols() - rec_mat.cols() + 1, CvType.CV_32FC1);

                            Imgproc.matchTemplate(mat, rec_mat, result, Imgproc.TM_CCOEFF_NORMED);

                            // Localizing the best match with minMaxLoc
                            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

                            Point matchLoc;
                            double max_val;
                            matchLoc = mmr.maxLoc;
                            max_val = mmr.maxVal;
                            double threshold;
                            // 閾値決め打ち
                            if(mmr.maxVal>=0.7){
                                threshold = 1;
                            } else {
                                threshold = 0;
                            }

                            //プログレッシブバーのアップデート
                            final int finalI = i;
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    mImageProssesingView.setProgressBarHandler(finalI,total_frame);
                                }
                            });
                            imageProssesingData.adCSV(i,mount_frame_time,max_val,matchLoc,threshold);
                        }
                        imageProssesingData.closeCSV();
                    }
                }
            }
        }).start();
    }

    ////Util////////////////////////////////////////////////////////////////////////////////////////
    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format("%02d:%02d" , minute, second);
        }

    }

    // ToDO やらなければならないことは、まず、pythonで作った処理の移植
    //       フィルタリング処理
    public Mat gausian(Mat orgMat){

        Mat newMat = new Mat();
        Imgproc.GaussianBlur(
                orgMat, newMat, new Size(3, 3), 0.0, 0.0);  // (2)
        return newMat;
    }

    //  https://qiita.com/johejo/items/68f94daf6365ffb88e03
    //  ↑グレースケールにせず、RGBの最大値を画素値とするパターン
    private void myFiltering(Mat src){
        Mat matX = new Mat();
        Mat matY = new Mat();

        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);
        Imgproc.Sobel(src, matX, src.depth(), 0, 1);
        Imgproc.Sobel(src, matY, src.depth(), 1, 0);
        matRMS(matX, matY, src);
    }

    private void matRMS(Mat src1, Mat src2, Mat dst) {

        int size = (int) (src1.total() * src1.channels());
        // Todo ここがわからない
        byte[] temp1 = new byte[size];
        byte[] temp2 = new byte[size];
        byte[] temp3 = new byte[size];
//        src1.convertTo(src1, CvType.CV_64FC3); // New line added.
//        src2.convertTo(src2, CvType.CV_64FC3); // New line added.
//        double[] temp1 = new double[size];
//        double[] temp2 = new double[size];
//        double[] temp3 = new double[size];
        src1.get(0, 0, temp1);
        src2.get(0, 0, temp2);

        for (int i = 0; i < size; i++) {
            temp3[i] = (byte)Math.sqrt((temp1[i] * temp1[i] + temp2[i] * temp2[i]) / 2);
//            temp3[i] = Math.sqrt((temp1[i] * temp1[i] + temp2[i] * temp2[i]) / 2);
        }

        dst.put(0, 0, temp3);
    }

    private void matMax3(Mat src1, Mat src2, Mat src3, Mat dst) {

        int size = (int) (src1.total() * src1.channels());
        byte[] temp1 = new byte[size];
        byte[] temp2 = new byte[size];
        byte[] temp3 = new byte[size];
        byte[] temp4 = new byte[size];
        src1.get(0, 0, temp1);
        src2.get(0, 0, temp2);
        src3.get(0, 0, temp3);

        for (int i = 0; i < size; i++) {
            temp4[i] = chooseBig(chooseBig(temp1[i], temp2[i]), temp3[i]);
        }

        dst.put(0, 0, temp4);
    }

    private byte chooseBig(byte a, byte b) {
        if(b > a) {
            return b;
        }else {
            return a;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
}
