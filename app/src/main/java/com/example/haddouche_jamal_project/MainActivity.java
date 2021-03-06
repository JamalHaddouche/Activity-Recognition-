package com.example.haddouche_jamal_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=3000;
    View first,second,third,fourth,fifth,sixth;
    TextView fsm,developpeur;
    //Animation
    Animation topAnimation,bottomAnimation,middleAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        middleAnimation=AnimationUtils.loadAnimation(this,R.anim.middle_animation);
        bottomAnimation=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        first=findViewById(R.id.firstLine);
        second=findViewById(R.id.secondLine);
        third=findViewById(R.id.thirdLine);
        fourth=findViewById(R.id.fourthLine);
        fifth=findViewById(R.id.fifthLine);
        sixth=findViewById(R.id.sixthLine);

        fsm=findViewById(R.id.fsm);
        developpeur=findViewById(R.id.developpeur);

        first.setAnimation(topAnimation);
        second.setAnimation(topAnimation);
        third.setAnimation(topAnimation);
        fourth.setAnimation(topAnimation);
        fifth.setAnimation(topAnimation);
        sixth.setAnimation(topAnimation);

        fsm.setAnimation(middleAnimation);
        developpeur.setAnimation(bottomAnimation);

        //passer a l'activité login
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);






    }
}