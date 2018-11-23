package com.kaiwei.android.moneymanager;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    private ImageView splash;
    private TextView tv_availableBudget;
    private TextView tv_totalSpent;
    private TextView tv_balance;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        tv_availableBudget = (TextView)findViewById(R.id.tv_availableBudget);
        tv_totalSpent = (TextView)findViewById(R.id.tv_totalSpent);
        tv_balance =(TextView)findViewById(R.id.tv_balance);
        btn_start =(Button)findViewById(R.id.btn_start);

        splash = (ImageView)findViewById(R.id.iv_Splash);
        Glide.with(this)
                .load(R.drawable.splash_gif)
                .into(splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                ObjectAnimator animatorY = ObjectAnimator.ofFloat(splash, "y", -1f);
                animatorY.setDuration(1000);
                animatorY.start();

                tv_availableBudget.setVisibility(View.VISIBLE);
                tv_totalSpent.setVisibility(View.VISIBLE);
                tv_balance.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                btn_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        SplashScreen.this.finish();
                    }
                });
            }
        },3000);

    }
}
