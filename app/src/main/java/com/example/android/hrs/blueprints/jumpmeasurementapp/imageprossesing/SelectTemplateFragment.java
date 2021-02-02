package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.CardStackLayoutManager;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.CardStackView;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ResisteredMediumFragment;
import com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing.ScrollChildSwipeRefreshLayout;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.DbBitmapUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.isseiaoki.simplecropview.CropImageView;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SelectTemplateFragment extends Fragment implements SelectTemplateContract.View{

    private SelectTemplateContract.Presenter mPresenter;

    private SelectTemplateFragment.CardRecyclerAdapter mListAdapter;

    private View mNoMediumView;
    private ImageView mNoMediaIcon;
    private TextView mNoMediaMainView;
    private TextView mNoMediaAddView;
    private LinearLayout mMediumView;

    public SelectTemplateFragment() {
        // 空のコンストラクタ
    }

    public static SelectTemplateFragment newInstance() {
        return new SelectTemplateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mListAdapter = new SelectTemplateFragment.CardRecyclerAdapter(new ArrayList<TemplateDb>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull SelectTemplateContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mFragments.dispatchDestroy();
//        mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    // Todo 必要性疑問
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mPresenter.result(requestCode, resultCode);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.selecttemplate_frag, container, false);

        //Setup medium View
//      ListView listView = (ListView) root.findViewById(R.id.medium_list);
//      listView.setAdapter(mListAdapter);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
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
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout_selecttemplate);
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
                mPresenter.loadTemplates(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout_select);

        // Make sure setRefreshing() is called after the layout is done with everything else.
//        srl.post(new Runnable() {
////        getView().post(new Runnable() {
//            @Override
//            public void run() {
////                getView().setRefreshing(active);
//                srl.setRefreshing(active);
//            }
//        });
    }

    @Override
    public void showTemplates(List<TemplateDb> templates) {
        mListAdapter.replaceData(templates);

        mMediumView.setVisibility(View.VISIBLE);
        mNoMediumView.setVisibility(View.GONE);
    }


    @Override
    public void showNoTemplates() {
        showNoTemplatesViews(
                getResources().getString(R.string.no_medium_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }


    private void showNoTemplatesViews(String mainText, int iconRes, boolean showAddView) {
        mMediumView.setVisibility(View.GONE);
        mNoMediumView.setVisibility(View.VISIBLE);

        mNoMediaMainView.setText(mainText);
        mNoMediaIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoMediaAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadingTemplatesError() {
        showMessage(getString(R.string.loading_medium_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showImageProssesingUi(String id) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Bundle bundle = new Bundle();
        bundle.putString("TEMPLATE_ID", id);
        ImageProssesingActivity activity = (ImageProssesingActivity) getActivity();
        //Fileインスタンスを書き込む。
//        activity.deleteFragment1();
        activity.switchToFragment3(bundle);

    }


    SelectTemplateFragment.MediaItemListener mItemListener = new SelectTemplateFragment.MediaItemListener() {
        //        @Override
//        public void onMediaClick(MediaModel clickedMedia) {
////            mPresenter.openTaskDetails(clickedTask);
//            mPresenter.openImageProssesing(clickedMedia);
//        }
        @Override
        public void onMediaClick(View v, int i, String template_id, List<TemplateDb> medium) {


            mPresenter.openImageProssesing(v, i, template_id, medium);
        }

//        @Override
//        public void onCompleteTaskClick(Task completedTask) {
//            mPresenter.completeTask(completedTask);
//        }
//
//        @Override
//        public void onActivateTaskClick(Task activatedTask) {
//            mPresenter.activateTask(activatedTask);
//        }
    };


    // ToDo リサイクルビューのアダプターの予定(仮)
    public class CardRecyclerAdapter extends RecyclerView.Adapter<SelectTemplateFragment.CardRecyclerAdapter.ViewHolder>{
        private LayoutInflater mInflater;
        private List<TemplateDb> mMedium;
        //        private Context mContext;
        private SelectTemplateFragment.MediaItemListener mListener;

        //        public CardRecyclerAdapter(Context context, ArrayList<MediaModel> data, MediaItemListener listener) {
        public CardRecyclerAdapter(ArrayList<TemplateDb> data, SelectTemplateFragment.MediaItemListener listener) {
//            mInflater = LayoutInflater.from(context);
//            mContext = context;
//            mData = data;
            mListener = listener;
            mMedium = data;
        }

        public void replaceData(List<TemplateDb> medium) {
            setList(medium);
            notifyDataSetChanged();    //←これはいったい何ですか？？
        }

        public void setList(List<TemplateDb> medium) {
            mMedium = checkNotNull(medium);
        }



        @Override
        public CardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // 表示するレイアウトを設定
//            return new ViewHolder(mInflater.inflate(R.layout.media_item, viewGroup, false));
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_row, viewGroup,false);
            SelectTemplateFragment.CardRecyclerAdapter.ViewHolder vh = new ViewHolder(inflate);
            return vh;

        }

        @Override
        public void onBindViewHolder(SelectTemplateFragment.CardRecyclerAdapter.ViewHolder viewHolder, final int i) {
            // データ表示
            if (mMedium != null && mMedium.size() > i && mMedium.get(i) != null) {
                // 指定したデータを取ってくる上から順番
                viewHolder.titleView.setText("a");
                DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
                Bitmap thumbnail = dbBitmapUtility.getImage(mMedium.get(i).getTemplate_blob());
                //Todo ここでblobをbitimapに変換してセットする
                viewHolder.image_view.setImageBitmap(thumbnail);
            }

            // クリック処理
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ハッシュidで取り出すため
                    String id = mMedium.get(i).getId();
//                    DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
//                    Bitmap thumbnail = dbBitmapUtility.getImage(mMedium.get(i).getTemplate_blob());
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
//            TextView dateView;

            public ViewHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.title);
//                dateView = (TextView) itemView.findViewById(R.id.date);
                image_view = (ImageView) itemView.findViewById(R.id.image_view);
            }
        }
    }

    public interface MediaItemListener {

        void onMediaClick(View v, int position, String template_id,List<TemplateDb> medium);    //リサイクルビューを作った際は自分で作った情報が取れるように自作する。

    }



}
