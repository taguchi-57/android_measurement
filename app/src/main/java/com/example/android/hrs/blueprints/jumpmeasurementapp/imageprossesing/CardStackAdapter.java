package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.hrs.blueprints.jumpmeasurementapp.R;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.CardStackListener;
import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.Direction;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ResisteredMediumFragment;

import java.util.ArrayList;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {


    private List<Spot> spots;
    private CardStackListener mListener;


    public CardStackAdapter(ArrayList<Spot> data,CardStackListener listener) {
        spots = data;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 表示するレイアウトを設定
//            return new ViewHolder(mInflater.inflate(R.layout.media_item, viewGroup, false));
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spot, viewGroup,false);
        ViewHolder vh = new ViewHolder(inflate);
//        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//        View inflate = inflater.inflate(R.layout.item_spot, viewGroup, false);
//        ViewHolder vh = new ViewHolder(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Spot spot = spots.get(i);
        if(i==0){
            viewHolder.image.setImageResource(R.drawable.prossesing1);
        }else if(i==1){
            viewHolder.image.setImageResource(R.drawable.prossesing2);
        }else if(i==2){
            viewHolder.image.setImageResource(R.drawable.prossesing3);
        }


//        switch (i){
//            case 0:
//                viewHolder.image.setImageResource(R.drawable.prossesing1);
//            case 1:
//                viewHolder.image.setImageResource(R.drawable.prossesing2);
//            case 2:
//                viewHolder.image.setImageResource(R.drawable.prossesing3);
//        }
//        viewHolder.name.setText(spot.getName());
//        viewHolder.city.setText(spot.getCity());
//        Glide.with(viewHolder.image)
//                .load(spot.getURL())
//                .into(viewHolder.image);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //
//                Log.i("総フレーム数", "てすとOK");
//                // ハッシュidで取り出すため
//                Toast.makeText(v.getContext(), spot.getName(), Toast.LENGTH_SHORT).show();
//            }
////            @Override
////            public void onCardSwiped(Direction direction) {
////
////            }
//        });

    }


    @Override
    public int getItemCount() {
        if (spots != null) {
            return spots.size();
        } else {
            return 0;
        }
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    public List<Spot> getSpots() {
        return spots;
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView city;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.name);
//            city = (TextView) itemView.findViewById(R.id.city);
            image = (ImageView) itemView.findViewById(R.id.item_image);

        }
    }
}
