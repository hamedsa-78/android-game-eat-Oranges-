package com.example.eating_oranges;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.NumericShaper;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        //private  double screenx;
        /*

        DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int height = displayMetrics.heightPixels;
    int width = displayMetrics.widthPixels;
         */
       double screenx  = Resources.getSystem().getDisplayMetrics().widthPixels;

    //Layouts
        private FrameLayout gameframe;
        private LinearLayout linearLayout;

    //Shared_prefences for high_score;

        private SharedPreferences settings;

        //Images
        private ImageView black;
        private ImageView orange;
        private ImageView pink;
        private ImageView box;

        //Text_views
        private TextView score_board;
        private TextView High_score_board;

        private Button start_button;
        private Drawable image_box_right;
        private Drawable image_box_left;

        //counter
        private int time_count=0;

        //statues
        private boolean start_game=false;
        private boolean action_flag=false;

        //Positions
          private float Box_x,Box_y;
          private int framewidth;
          private int ff;
          private float blackx,blacky;
          private float orangex,orangey;
          private float pinkx,pinky;

          //classes
            private Timer timer=new Timer();
            private Handler handler=new Handler();

            //Variables
            private int point;
            private int highscore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameframe=findViewById(R.id.frame);
        score_board=findViewById(R.id.score);
        High_score_board=findViewById(R.id.high_score);
        start_button=findViewById(R.id.start_btn);
        black=findViewById(R.id.black);
        orange=findViewById(R.id.orange);
        pink=findViewById(R.id.pink);
        box=findViewById(R.id.box_right);
        start_button.setOnClickListener(this);
        linearLayout=findViewById(R.id.layout_start);

        settings=getSharedPreferences("Game Data", Context.MODE_PRIVATE);
        int hs= settings.getInt("High_Score",0);
        High_score_board.setText("high score : "+hs);
        High_score_board.setTextColor(Color.GREEN);
        highscore=hs;

        //initial_frame_layout=findViewById(R.id.frame);

        image_box_right=getResources().getDrawable(R.drawable.box_right);
        image_box_left=getResources().getDrawable(R.drawable.box_left);
        Box_x=box.getX();
        Box_y=box.getY();
        //framewidth=gameframe.getWidth();
        //ff=gameframe.getWidth();
    }
    public void changepos() {//***************************************************
        Random random = new Random();


        orangey += 13;
        if (orangey <= -460)
            orangex = random.nextInt((int) (framewidth-orange.getWidth())-orange.getWidth()+1)+orange.getWidth();

        pinky += 7;
        if (pinky <= -980)
            pinkx =random.nextInt((int) (framewidth-pink.getWidth())-pink.getWidth()+1)+pink.getWidth();

        blacky += 10;
        if (blacky <= -780)
            blackx = random.nextInt((int) (framewidth-black.getWidth())-black.getWidth()+1)+black.getWidth();


        if (framewidth <= 0) {
            framewidth = gameframe.getWidth();
            Box_x = box.getX();
            Box_y = box.getY();
        }
        if (action_flag) {
            Box_x += 10;
            box.setImageDrawable(image_box_right);
        } else  {
            Box_x -= 10;
           box.setImageDrawable(image_box_left);
        }

        // set the bound of Box
        if (Box_x >= framewidth - box.getWidth()) {
            Box_x = framewidth - box.getWidth();
        }
        if (Box_x < 0) {
            Box_x = 0;
        }


        //set the bound of orange
        if (orangey > gameframe.getHeight())
            orangey = -500;

        //set the bound of pink
        if (pinky > gameframe.getHeight())
            pinky = -3000;

        //set the bound of black
        if (blacky > gameframe.getHeight()) {
            blacky = -800;
        }
            // check the ...E..s..a..b...a...t...
        Rect box_rect=new Rect((int) Box_x+box.getWidth()-20, (int) (Box_y), (int) (Box_x + box.getWidth()), (int) (Box_y + box.getHeight()));
        //Check blak
        if (Rect.intersects( new Rect((int) Box_x, (int) (Box_y), (int) (Box_x + box.getWidth()), (int) (Box_y + box.getHeight())),new Rect((int) blackx, (int) blacky, (int) blackx + black.getWidth(), (int) blacky + black.getHeight()))) {
            blacky=-800;
            framewidth=framewidth * 80/100;
            change_frame(framewidth);
        }
        // Check orange
             if (Rect.intersects(box_rect, new Rect((int) orangex, (int) orangey, (int) orangex + orange.getWidth(), (int) orangey + orange.getHeight()))) {
              point+=30;
                 orangey = -500;
            }
        // Check pink
        if (Rect.intersects(box_rect, new Rect((int) pinkx, (int) pinky, (int) pinkx + pink.getWidth(), (int) pinky + pink.getHeight()))) {
            pinky=-3000;
            point+=60;
            pinky = -3000;
            framewidth=framewidth*105/100;
            change_frame((int) framewidth);
        }

        score_board.setText("Score :"+point);
        score_board.setTextColor(Color.RED);

        if(framewidth<=box.getWidth()*2){
          Gameover();
        }



                //isplaying=false;
      //********************** draw();
        box.setX(Box_x);

        orange.setX(orangex);
        orange.setY(orangey);

        pink.setX(pinkx);
        pink.setY(pinky);

        black.setX(blackx);
        black.setY(blacky);

    } // End of the function change_pos***************************************

    private void Gameover() {
        timer.cancel();
        timer=null;
        start_game=false;
        score_board.setText("Score : 0");
        if(point>highscore){
            highscore=point;
            High_score_board.setText("high score :"+highscore);
            High_score_board.setTextColor(Color.GREEN);
            SharedPreferences.Editor editor=settings.edit();
            editor.putInt("High_Score",highscore);
            editor.apply();

        }
        point=0;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////////
        change_frame(ff);
        //////////////////////////////////////////////////////////////////////

        linearLayout.setVisibility(View.VISIBLE);
        orange.setVisibility(View.INVISIBLE);
        black.setVisibility(View.INVISIBLE);
        pink.setVisibility(View.INVISIBLE);
        box.setVisibility(View.INVISIBLE);

    }
            private void change_frame(int framewidth){
               ViewGroup.LayoutParams params= gameframe.getLayoutParams();
                params.width=framewidth;
                gameframe.setLayoutParams(params);

            }
    @Override
    public void onClick(View v) {
        if (v.getId() == start_button.getId()) {

            initial();
            start_game = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (start_game) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                    changepos();
                            }
                        });
                    }

                }
            }, 0, 18);

        }//bottom
    }

    private void initial() {
        ff=gameframe.getWidth();
        Box_y=gameframe.getHeight()-box.getHeight();
        Random random=new Random();


            framewidth=gameframe.getWidth();

        black.setY((float) -800.0);
        black.setX(random.nextInt((int) framewidth));

        pink.setY((float) -1000.0);
        pink.setX(random.nextInt((int) framewidth));

        orange.setY((float) -600.0);
        orange.setX(random.nextInt((int) framewidth));

        linearLayout.setVisibility(View.INVISIBLE);//this also omits button and other things on the linearlayout
        box.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        pink.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(start_game){
            if(event.getX()>screenx/2){
            /*if(event.getAction()==MotionEvent.ACTION_DOWN)  {*/
                action_flag=true;

            } else if(event.getX()<=screenx/2){
            /*else if (event.getAction()==MotionEvent.ACTION_UP){*/
                action_flag=false;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"OnPause",Toast.LENGTH_SHORT).show();
    }
}
