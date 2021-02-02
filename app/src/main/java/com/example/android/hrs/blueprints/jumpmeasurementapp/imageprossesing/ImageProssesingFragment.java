package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.CardStackLayoutManager;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.CardStackListener;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.CardStackView;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.Direction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.isseiaoki.simplecropview.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import butterknife.ButterKnife;
//// Todo https://dev.classmethod.jp/articles/android-butterknifezelezny/
//import butterknife.InjectView;
//import butterknife.OnClick;


//import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImageProssesingFragment extends Fragment implements ImageProssesingContract.View,CardStackListener {

    private ImageProssesingContract.Presenter mPresenter;

    CropImageView mcropImageView;
    ImageView mcroppedImageView;
    CardView mCroppedCardView;
    CardStackView mCardStackView;

    int mMode = 0;
    int counter = 0;
    ArrayList<Spot> spots = new ArrayList<>();

    public  CardStackListener mListener;

    // Todo プログレッシブバー周りの処理
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();


    public ImageProssesingFragment() {
        // 空のコンストラクタ
    }

    public static ImageProssesingFragment newInstance() {
        return new ImageProssesingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setPresenter(@NonNull ImageProssesingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.imageprossesing_frag, container, false);
        mCardStackView = (CardStackView)root.findViewById(R.id.card_stack_view);
        mcropImageView = (CropImageView)root.findViewById(R.id.cropImageView);
        mcroppedImageView = (ImageView)root.findViewById(R.id.croppedImageView);
        mCroppedCardView = (CardView)root.findViewById(R.id.card_view);
        //        Todo 切り取られるまでカードビューを隠す
        mCroppedCardView.setVisibility(View.GONE);
        // Todo テンプレートからテンプレートクリック
        if(getArguments() != null) {
            String template_id = getArguments().getString("TEMPLATE_ID");
            mCardStackView.setVisibility(View.GONE);
//            mcroppedImageView.setVisibility(View.VISIBLE);
            mCroppedCardView.setVisibility(View.VISIBLE);
            mPresenter.showTemplate(template_id);

            mMode=1;
        }
        // アスペクト比をフリーにする
        mcropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        // 切り取りの最小サイズ(dp)を指定する。デフォルトは50
        mcropImageView.setMinFrameSizeInDp(10);
        mcroppedImageView = (ImageView)root.findViewById(R.id.croppedImageView);


///////////Todo 切り取り//////////////////////////////////////////////////////////////////////////////
        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_imageprossesing);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //これで切り出されたビットマップを獲得できているはず。
                if (mMode==0){
                    mCroppedCardView.setVisibility(View.VISIBLE);
                    mcroppedImageView.setImageBitmap(mcropImageView.getCroppedBitmap());

                    // Todo テンプレートの保存のタイミングとロジック
                    mPresenter.saveTemplate(mcropImageView.getCroppedBitmap());


                    mMode=1;
                } else if (mMode==1){

                    Bitmap bmp = ((BitmapDrawable)mcroppedImageView.getDrawable()).getBitmap();
                
                    //ToDO プログレッシブバーの仮
                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(true);
                    progressBar.setMessage("処理中ですしばらくお待ちください。。。。。。。");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();

                    //reset progress bar status
                    progressBarStatus = 0;
                    // TODO end
                    mPresenter.startImageProssesing(bmp);
                }
                
            }
        });



///////////////////////Todo tinderみたいなカードビュー///////////////////////////////////////////////////

        CardStackAdapter adapter = new CardStackAdapter(createSpots(),mListener);
        // lisnerを継承したthisを渡すことでこのクラスで、インターフェースをoverrideした関数を使うことができる。
        CardStackLayoutManager layoutManager = new CardStackLayoutManager(getContext(),this);
        mCardStackView.setLayoutManager(layoutManager);
        mCardStackView.setAdapter(adapter);
///////////////////////Todo End tinderみたいなカードビュー///////////////////////////////////////////////////

        return root;
    }




    //Todo カードリスナーのオーバーライドend

    private Spot createSpot(String name, String city, String url) {
        Spot spot = new Spot(name,city,url);
        return spot;
    }



    private ArrayList<Spot> createSpots()  {

        spots.add(createSpot("Yasaka Shrine", "Kyoto", "https://source.unsplash.com/Xq1ntWruZQI/600x800"));
        spots.add(createSpot("Fushimi Inari Shrine","Kyoto", "https://source.unsplash.com/NYyCqdBOKwc/600x800"));
        spots.add(createSpot("Bamboo Forest", "Kyoto", "https://source.unsplash.com/buF62ewDLcQ/600x800"));
        return spots;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
    }

    @Override
    public void showCropImage(String path, Bitmap bmp) {
        mcropImageView.setImageBitmap(bmp);
//        mcroppedImageView.setImageBitmap
    }

    @Override
    public void showCropedImage(Bitmap bmp) {
//        mcropImageView.setImageBitmap(bmp);
        mcroppedImageView.setImageBitmap(bmp);
    }


    public void settemp(int temp) {
        progressBar.setProgress(temp);
    };


    // ToDo progressbbar
    //Update the progress bar
    @Override
    public void setProgressBarHandler(final int i, final double total_frame){
        progressBarHandler.post(new Runnable() {
        public void run() {
            progressBarStatus = doSomeTasks(i,total_frame);
            progressBar.setProgress(progressBarStatus);
            if (progressBarStatus >= 100) {

                //sleep 2 seconds, so that you can see the 100%
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //close the progress bar dialog
                progressBar.dismiss();
            }
            }
        });
    }

    public int doSomeTasks(int i, double total_frame) {
        i++;
        while (i <= total_frame) {

            float int_total_frame = (float) total_frame;

            return (int)((i/int_total_frame)*100);
        }

        return 100;

    }

    @Override
    public void showLoadingMediumError() {
        showMessage(getString(R.string.loading_medium_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMissingMedia() {
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.i("aaaaaa","aaaaaaaaa");
        counter += 1;
        if (counter == spots.size()) {
            Log.i("カウンター", String.valueOf(counter));
            mCardStackView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
