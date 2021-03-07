package com.example.gmisproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class tips extends AppCompatActivity {
    private ViewPager mainViewPager;
    private LinearLayout mDotsLinearLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button previousBtn ;
    private Button nextBtn ;
    private int currentPage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        mainViewPager = (ViewPager)findViewById(R.id.view_pager_id);
        mDotsLinearLayout=(LinearLayout)findViewById(R.id.linear_layout_id);
        previousBtn =(Button)findViewById(R.id.button_previous);
        nextBtn =(Button)findViewById(R.id.button_next);
        sliderAdapter = new SliderAdapter(this);
        mainViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mainViewPager.addOnPageChangeListener(viewlistner);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(currentPage +1);
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(currentPage -1);
            }
        });
    }
    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotsLinearLayout.removeAllViews();
        for(int i =0 ; i < mDots.length ; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colortransportwhite));
            mDotsLinearLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }

    }
    ViewPager.OnPageChangeListener viewlistner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage= i;
            if(i==0){
                nextBtn.setEnabled(true);
                previousBtn.setEnabled(false);
                previousBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("Next");
                previousBtn.setText("");
            }
            else if (i == mDots.length -1){
                nextBtn.setEnabled(true);
                previousBtn.setEnabled(true);
                previousBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("finish");
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(tips.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                previousBtn.setText("previous");
            } else {
                nextBtn.setEnabled(true);
                previousBtn.setEnabled(true);
                previousBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Next");
                previousBtn.setText("previous");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}




