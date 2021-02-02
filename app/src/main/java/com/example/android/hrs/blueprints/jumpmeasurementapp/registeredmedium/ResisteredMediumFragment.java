package com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium;

import android.app.Activity;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing.ImageProssesingActivity;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ScrollChildSwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResisteredMediumFragment extends Fragment implements ResisteredMediumContract.View{

    private ResisteredMediumContract.Presenter mPresenter;

    private CardRecyclerAdapter mListAdapter;

    private View mNoMediumView;

    private ImageView mNoMediaIcon;

    private TextView mNoMediaMainView;

    private TextView mNoMediaAddView;

    private LinearLayout mMediumView;

//    private TextView

    public ResisteredMediumFragment() {
        // パブリックなコンストラクタは現在のところ作る予定はない
    }

    public static ResisteredMediumFragment newInstance() {
        return new ResisteredMediumFragment();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mListAdapter = new CardRecyclerAdapter(new ArrayList<MediaModel>(0), mItemListener);
        mListAdapter = new CardRecyclerAdapter(new ArrayList<MediaModel>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull ResisteredMediumContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.registeredmedium_frag, container, false);

        RecyclerView  recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        recyclerView.setAdapter(mListAdapter);
        // ToDo ↓よく分からない。
        LinearLayoutManager llm = new LinearLayoutManager(getContext());  //E/RecyclerView: No layout manager attached; skipping layout

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llm);


        mMediumView = (LinearLayout) root.findViewById(R.id.mediumLL);

        // setup no medium View
        mNoMediumView = root.findViewById(R.id.noMedium);
        mNoMediaIcon = (ImageView) root.findViewById(R.id.noMediumIcon);
        mNoMediaMainView = (TextView) root.findViewById(R.id.noMediumMain);
        mNoMediaAddView = (TextView) root.findViewById(R.id.noMediumAdd);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout_resistered);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(recyclerView);

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
        public void onMediaClick(View v, int i, String id, List<MediaModel> medium) {
            mPresenter.openImageProssesing(v, i, id, medium);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout_resistered);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMedium(List<MediaModel> medium) {
        mListAdapter.replaceData(medium);

        mMediumView.setVisibility(View.VISIBLE);
        mNoMediumView.setVisibility(View.GONE);
    }

    @Override
    public void showNoActiveMedium() {
        showNoTasksViews(
                getResources().getString(R.string.no_medium_active),
                R.drawable.ic_check_circle_24,
                false
        );
    }

    @Override
    public void showNoMedium() {
        showNoTasksViews(
                getResources().getString(R.string.no_medium_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showNoCompletedMedium() {
        showNoTasksViews(
                getResources().getString(R.string.no_medium_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_media_message));
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mMediumView.setVisibility(View.GONE);
        mNoMediumView.setVisibility(View.VISIBLE);

        mNoMediaMainView.setText(mainText);
        mNoMediaIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoMediaAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showImageProssesingUi(String mediaId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), ImageProssesingActivity.class);
        intent.putExtra(ImageProssesingActivity.EXTRA_MEDIA_ID, mediaId);   //Todo EXTRA_TASK_IDのところはさっぱり
        startActivity(intent);
    }

    @Override
    public void showMediaMarkedComplete() {
        showMessage(getString(R.string.media_marked_complete));
    }

    @Override
    public void showMediaMarkedActive() {
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
    public boolean isActive() {
        return isAdded();
    }

    // ToDo リサイクルビューのアダプターの予定(仮)
    public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>{
        private LayoutInflater mInflater;
        private List<MediaModel> mMedium;
        private MediaItemListener mListener;

        public CardRecyclerAdapter(ArrayList<MediaModel> data, MediaItemListener listener) {
            mListener = listener;
            mMedium = data;
        }

        public void replaceData(List<MediaModel> medium) {
            setList(medium);
            notifyDataSetChanged();    //←これはいったい何ですか？？
        }

        public void setList(List<MediaModel> medium) {
            mMedium = checkNotNull(medium);
        }



        @Override
        public CardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // 表示するレイアウトを設定
//            return new ViewHolder(mInflater.inflate(R.layout.media_item, viewGroup, false));
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_row, viewGroup,false);
            ViewHolder vh = new ViewHolder(inflate);
            return vh;

        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            // データ表示
            if (mMedium != null && mMedium.size() > i && mMedium.get(i) != null) {
                // 指定したデータを取ってくる上から順番
                viewHolder.titleView.setText(mMedium.get(i).getTitle());
                viewHolder.dateView.setText(mMedium.get(i).getDate());
//                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(mMedium.get(i).getPath(),
//                        MediaStore.Images.Thumbnails.MICRO_KIND);
//                Bitmap thumbnail = mPresenter.setthumbnail(mMedium.get(i).getPath());
                Glide.with(getContext())
                        .load(mMedium.get(i).getPath()) // or URI/path
                        .into(viewHolder.image_view); //image
//                viewHolder.image_view.setImageBitmap(thumbnail);
            }

            // クリック処理
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ハッシュidで取り出すため
                    String id = mMedium.get(i).getId();
                    mListener.onMediaClick(v, i, id, mMedium);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (mMedium != null) {
                return mMedium.size();
            } else {
                return 0;
            }
        }

        // ViewHolder(固有ならインナークラスでOK)
        class ViewHolder extends RecyclerView.ViewHolder {

            TextView titleView;
            ImageView image_view;
            TextView dateView;

            public ViewHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.title);
                dateView = (TextView) itemView.findViewById(R.id.date);
                image_view = (ImageView) itemView.findViewById(R.id.image_view);
            }
        }
    }

    public interface MediaItemListener {

        void onMediaClick(View v, int position, String id,List<MediaModel> medium);    //リサイクルビューを作った際は自分で作った情報が取れるように自作する。

    }
}
