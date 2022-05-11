package com.example.miniuber.app.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.miniuber.R;

public class SliderAdapter extends PagerAdapter {
    Context context ;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public  int[] sliderImages ={R.drawable.on_board_one,R.drawable.on_board_two,R.drawable.on_board_three};
    public String[] sliderTitles={"Request Ride","Confirm Your Driver","Track your ride"};
    public String[] sliderDesc={"Request a ride get picked up by a nearby community driver","Huge drivers network helps you find comforable, safe and cheap ride","Know your driver in advance and be able to view current location in real time on the map"};

    @Override
    public int getCount() {
        return sliderImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_on_board,container,false);
        ImageView sliderImage = view.findViewById(R.id.onBoardImage);
        TextView sliderTitle =view.findViewById(R.id.onBoardTitle);
        TextView sliderInfo =view.findViewById(R.id.onBoardInfo);
        sliderImage.setImageResource(sliderImages[position]);
        sliderTitle.setText(sliderTitles[position]);
        sliderInfo.setText(sliderDesc[position]);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
