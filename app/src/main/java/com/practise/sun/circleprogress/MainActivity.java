package com.practise.sun.circleprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.custom.sun.circleprogresslibrary.CircleProgressView;

public class MainActivity extends AppCompatActivity {


    CircleProgressView mCircleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mCircleProgressView = (CircleProgressView) findViewById(R.id.circleprogress);
        mCircleProgressView.SetProgress(65);
        mCircleProgressView.SetAnimation(true);
        //动画时长和起始数值
        mCircleProgressView.SetAnimationDuration(2000,0);

    }
}
