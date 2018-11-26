package com.kaiwei.android.moneymanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
    private TextView tv_welcome;
    private TextView tv_intro;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        tv_welcome = (TextView)findViewById(R.id.tv_welcome);
        tv_intro = (TextView)findViewById(R.id.tv_intro);
        btn_start =(Button)findViewById(R.id.btn_start);

        splash = (ImageView)findViewById(R.id.iv_Splash);
        Glide.with(this)
                .load(R.drawable.splash_animation)
                .into(splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                ObjectAnimator animatorY = ObjectAnimator.ofFloat(splash, "y", -1f);
                animatorY.setDuration(1000);
                animatorY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tv_welcome.setVisibility(View.VISIBLE);
                        tv_welcome.animate()
                                .translationY(-80)
                                .alpha(1.0f)
                                .setDuration(1000)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        tv_intro.setVisibility(View.VISIBLE);
                                        tv_intro.animate()
                                                .translationY(-90)
                                                .alpha(1.0f)
                                                .setDuration(1000)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        super.onAnimationEnd(animation);
                                                        btn_start.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                    }
                                });
                    }
                });
                animatorY.start();

                btn_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SplashScreen.this, OverviewActivity.class));
                        SplashScreen.this.finish();
                    }
                });
            }
        },2500);

    }
}
