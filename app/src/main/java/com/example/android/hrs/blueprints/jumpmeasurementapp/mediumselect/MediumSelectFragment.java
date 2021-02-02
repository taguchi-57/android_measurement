package com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ResisteredMediumActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediumSelectFragment extends Fragment implements MediumSelectContract.View {

    private MediumSelectContract.Presenter mPresenter;
    private MediumAdapter mMediumAdapter;

    private View mNoMediumView;
    private ImageView mNoMediaIcon;
    private TextView mNoMediaMainView;
    private TextView mNoMediaAddView;
    private LinearLayout mMediumView;
    private TextView mTitle;
    private CalendarView mCalendarView;
    private String mMediaPath;
    private String mDate;

    public MediumSelectFragment() {
        // Requires empty public constructor
    }

    public static MediumSelectFragment newInstance() {
        return new MediumSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediumAdapter = new MediumAdapter(new ArrayList<LocalMedia>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull MediumSelectContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.mediumselect_frag, container, false);

        GridView gridView = (GridView) root.findViewById(R.id.medium_list);
        gridView.setAdapter(mMediumAdapter);
        mMediumView = (LinearLayout) root.findViewById(R.id.mediumLL);
        mTitle = (TextView) root.findViewById(R.id.add_media_title);
//        mDate = (TextView) root.findViewById(R.id.add_media_date);
        mCalendarView =(CalendarView) root.findViewById(R.id.calendarView);
        mTitle.setVisibility(View.GONE);
        mCalendarView.setVisibility(View.GONE);
//        mCalendarView.date = System.currentTimeMillis();

        // CalendarViewで日にちが選択された時に呼び出されるリスナー
        DateChangeListener listener = new DateChangeListener();
        mCalendarView.setOnDateChangeListener(listener);

        // Set up  no tasks view
        mNoMediumView = root.findViewById(R.id.noMedium);
        mNoMediaIcon = (ImageView) root.findViewById(R.id.noMediumIcon);
        mNoMediaMainView = (TextView) root.findViewById(R.id.noMediumMain);
        mNoMediaAddView = (TextView) root.findViewById(R.id.noMediumAdd);
        mNoMediaAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMedia();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_media);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveMedia(mMediumAdapter.mMediaPath,mTitle.getText().toString(), mDate);
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout_select);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
//        swipeRefreshLayout.setScrollUpChild(listView);
        swipeRefreshLayout.setScrollUpChild(gridView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadMedium(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }


    /**
     * Listener for clicks on tasks in the ListView.
     */
    MediaItemListener mItemListener = new MediaItemListener() {
        @Override
        public void onMediaClick(LocalMedia clickedMedia) {
            // チェックが入っていない状態から、チェックを入れる方(初期状態から最初にチェックを入れる際)
            mPresenter.editDetails(clickedMedia);
            // チェックボックスの初期化
            mTitle.getEditableText().clear();
//            mDate.getEditableText().clear();
            mTitle.setVisibility(View.VISIBLE);
            mCalendarView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNonActiveMediaClick(LocalMedia completedMedia) {
            // こっちがチェックが入っている状態から、チェックを外す方
            mPresenter.endEditMedia(completedMedia);
            // チェックボックスの初期化
            mTitle.getEditableText().clear();
            mTitle.setVisibility(View.GONE);
            mCalendarView.setVisibility(View.GONE);
        }

        @Override
        public void onAllMediumFalseMediaClick(List<LocalMedia> medium) {
            mPresenter.endAllEditMode(medium);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout_select);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMedium(List<LocalMedia> medium) {
        mMediumAdapter.replaceData(medium);

        mMediumView.setVisibility(View.VISIBLE);
        mNoMediumView.setVisibility(View.GONE);
    }

    @Override
    public void showNoActiveMedium() {
        showNoMediumViews(
                getResources().getString(R.string.no_medium_active),
                R.drawable.ic_check_circle_24,
                false
        );
    }

    @Override
    public void showNoMedium() {
        showNoMediumViews(
                getResources().getString(R.string.no_medium_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showNoCompletedMedium() {
        showNoMediumViews(
                getResources().getString(R.string.no_medium_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_media_message));
    }

    private void showNoMediumViews(String mainText, int iconRes, boolean showAddView) {
        mMediumView.setVisibility(View.GONE);
        mNoMediumView.setVisibility(View.VISIBLE);

        mNoMediaMainView.setText(mainText);
        mNoMediaIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoMediaAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAddMedia() {
        Intent intent = new Intent(getContext(), ResisteredMediumActivity.class);
        startActivityForResult(intent, ResisteredMediumActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showMediaDetailsUi(String mediaId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
//        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
//        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, mediaId);
//        startActivity(intent);
    }

    @Override
    public void showMediaMarkedComplete() {
        showMessage(getString(R.string.media_marked_complete));
    }

    @Override
    public void showMediaResistered() {
        showMessage(getString(R.string.media_resistered));
    }


    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.media_marked_active));
    }

    @Override
    public void showCompletedMediumCleared() {
        showMessage(getString(R.string.completed_medium_cleared));
    }

    @Override
    public void showLoadingMediumError() {
        showMessage(getString(R.string.loading_medium_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMediumList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    private class MediumAdapter extends BaseAdapter {

        class  ViewHolder {
            ImageView imageView;
            TextView textView;
        }

        private List<LocalMedia> mMedium;
        public String mMediaPath;      //ToDo アダプター内の変数へのアクセス方法は検討が必要
        private MediaItemListener mMediaItemListener;

        public MediumAdapter(List<LocalMedia> medium, MediaItemListener mediaItemListener) {
            setList(medium);
            mMediaItemListener = mediaItemListener;
        }

        public void replaceData(List<LocalMedia> medium) {
            setList(medium);
            notifyDataSetChanged();
        }

        private void setList(List<LocalMedia> medium) { mMedium = checkNotNull(medium); }

        @Override
        public int getCount() { return mMedium.size(); }

        @Override
        public LocalMedia getItem(int i) {
            return mMedium.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // Todo 参考URL: https://codechacha.com/ja/view-viewgroup/
            // Todo getViewが複数回通るために、処理が重くなっている？？？
            //      そもそも複数回通ること自体が問題なのかが分からない
            //      サムネイル取得の処理がもっと軽かったら、複数回回ること自体は問題にならない？？
            // Todo viewが分からない希望の回数以上getviewが走る
            View gridView = view;
            ViewHolder holder;

            // ViewHolderを生成
            final LocalMedia media = getItem(i);
            if (gridView == null) {
                //            View rowView = view;
                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                gridView = inflater.inflate(R.layout.grid_items, viewGroup, false);
                holder.imageView = gridView.findViewById(R.id.image_view);
                holder.textView = gridView.findViewById(R.id.text_view);
                gridView.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }


//                final LocalMedia media = getItem(i);

//            TextView titleTV = (TextView) gridView.findViewById(R.id.text_view);
            holder.textView.setText(media.getTitleForList());
            // 動画のサムネイルの表示
//            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(media.getTitleForList(),
//                    MediaStore.Images.Thumbnails.MICRO_KIND);
//            Bitmap thumbnail = mPresenter.setthumbnail(media.getTitleForList());
            Glide.with(getContext())
                    .load(media.getTitleForList()) // or URI/path
                    .into(holder.imageView); //imageview to set thumbnail to
//            holder.imageView.setImageBitmap(thumbnail);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (media.isCompleted()) {
                        // こっちがメディア選択済みの状態からチェックマークを外す方
                        mMediaPath = media.getMediaPath();
                        mMediaItemListener.onNonActiveMediaClick(media);
                    }else {
                        // 外当以外のものがクリックされた場合、すべてのデータを取り出してfalseにする
                        // ToDo これやると処理が遅くなる気がしてならない、何か他の解決策はないものか？？
                        // ToDO  mMediaPathだけでなくLocalMediaクラスのすべてをクラスとして持っておけばイケルはず
                        // ToDo　これは自信があるので後でかならずやるものと考えてコードを作るとする。
                        mMediaItemListener.onAllMediumFalseMediaClick(mMedium);

                        //                  mMediaItemListener.onActivateMediaClick(media);
//                  こっちが最初にチェックマークがつく方。
                        mMediaPath = media.getMediaPath();
                        mMediaItemListener.onMediaClick(media);
                    }
                }
            });


            CheckBox completeCB = (CheckBox) gridView.findViewById(R.id.complete);
            // Active/completed task UI
            // UI部分
            completeCB.setChecked(media.isCompleted());



            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            if (media.isCompleted()) {
                gridView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
            } else {
                gridView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.touch_feedback));
            }





            return gridView;
        }
    }

    public interface MediaItemListener {

        void onMediaClick(LocalMedia clickedMedia);

        void onNonActiveMediaClick(LocalMedia nonActiveMedia);
//
        void onAllMediumFalseMediaClick(List<LocalMedia> activateMedia);
    }

    // CalendarViewで日にちが選択された時に呼び出されるリスナークラス
    private class DateChangeListener implements CalendarView.OnDateChangeListener {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
            // monthは0起算のため+1する
            int displayMonth = month + 1;
            setDate(year, month, dayOfMonth);
        }
    }

    public void setDate(int year, int month, int dayOfMonth){
        mDate = String.valueOf(year)+String.valueOf(month)+String.valueOf(dayOfMonth);
    }

}
