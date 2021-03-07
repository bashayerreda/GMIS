package com.example.gmisproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ImageView sliderImageView;
    TextView slidHeading,slidDesc;

    public SliderAdapter(Context context) {
        this.context = context;
    }
    public int[] slide_image ={
            R.drawable.tip_msg,
            R.drawable.tip_request,
            R.drawable.tip_bin

    };
    public String[] slide_inputs={
            "الرسائل",
            "الطلب",
            "السله"
    };
    public String[] slide_disc = {
            "يمكنك متابعه طلبك و معرفه التكاليف والتواصل مع فروع الشركه",
            "يمكنك طلب خدمه السلات الالكترونيه من خلال ملىء الفورم",
            "يمكنك رؤية السلات الخاصه بك و متابعه نسب امتلاء كل سله"

    };

    @Override

    public int getCount() {
        return slide_inputs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.side_layout,container,false);
        //views with casting
        sliderImageView = (ImageView) view.findViewById(R.id.image_view_slide);
        slidHeading = (TextView) view.findViewById(R.id.textview_heading);
        slidDesc = (TextView) view.findViewById(R.id.textview_desc);
        sliderImageView.setImageResource(slide_image[position]);
        slidHeading.setText(slide_inputs[position]);
        slidDesc.setText(slide_disc[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}

