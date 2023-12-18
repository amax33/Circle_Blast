package com.example.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    board board = new board();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout Layout = findViewById(R.id.Rlayout);
        context = this;
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        double width = size.x;
        double height = size.y;
        board.setXY(width,height);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.rotate);
            button.startAnimation(animation);
            Random rnd = new Random();
            int size1 = (rnd.nextInt((120-85)+1))+85;
            CircleView circleView = new CircleView(context,
                    size1, size1, size1, (rnd.nextInt((80-15)+1))+15,
                    (rnd.nextInt((5-1)+1))+1, paint());
            board.Circles.add(circleView);
            Layout.addView(circleView);
            button.animate().cancel();
        });
        final Handler handler = new Handler();
        final int delay = 1; // 1000 milliseconds == 1 second
        handler.postDelayed(new Runnable() {
            public void run() {
                for (int i = board.Circles.size() - 1; i >= 0; i--) {
                    CircleView new_one = board.move(context,board,i);
                    Layout.addView(new_one);
                    board.Circles.add(new_one);
                    Layout.removeViewAt(i + 1);
                    board.Circles.remove(board.Circles.get(i));
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MediaPlayer effect = MediaPlayer.create(context,R.raw.bubbles);
        ConstraintLayout Layout = findViewById(R.id.Rlayout);
        int x = (int)event.getX();
        int y = (int)event.getY();
        for (int i = 0; i < board.Circles.size(); i++) {
            CircleView circleView = board.Circles.get(i);
            if((x <= circleView.getPositionX()+circleView.getSize() &&
                    x>= circleView.getPositionX()-circleView.getSize()) &&
                    (y <= circleView.getPositionY()+circleView.getSize() &&
                    y>= circleView.getPositionY()-circleView.getSize())){
                effect.start();
                Layout.removeViewAt(i + 1);
                animation(board.Circles.get(i), i+1);
                board.Circles.remove(board.Circles.get(i));
            }
        }
        return false;
    }
    public void animation(CircleView circle, int i){
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.together);
        ConstraintLayout Layout = findViewById(R.id.Rlayout);
        int Angle = circle.getAngle();
        Paint paint = circle.getPaint();
        double x = circle.getPositionX();
        double y = circle.getPositionY();
        double r = circle.getSize();
        int size1 = (int) (r/5);
        double x1 = x+r, x2 = x-r, y1 = y+r, y2 = y-r;
        CircleView c1 = new CircleView(context, size1,x1,y,Angle,1,paint);
        CircleView c2 = new CircleView(context, size1,x2,y,Angle,1,paint);
        CircleView c3 = new CircleView(context, size1,x,y1,Angle,1,paint);
        CircleView c4 = new CircleView(context, size1,x,y2,Angle,1,paint);
        Layout.addView(c1);
        Layout.addView(c2);
        Layout.addView(c3);
        Layout.addView(c4);
        c1.startAnimation(animation);
        c2.startAnimation(animation);
        c3.startAnimation(animation);
        c4.startAnimation(animation);
        for (int j = i; j < i+4; j++) {
            Layout.removeViewAt(i);
        }
    }
    public Paint paint(){
        Paint paint1;
        //((max - min) + 1) + min
        paint1 = new Paint();
        Random rnd = new Random();
        paint1.setARGB(255, rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256));
        return paint1;
    }
}
class CircleView extends View {
    private Paint drawPaint;
    private int size, speed, angle;
    private double  positionX, positionY;
    public CircleView(Context context,int size, double X, double Y, int Angel, int speed, Paint paint) {
        super(context);
        setSize(size);
        setPositionX(X);
        setPositionY(Y);
        setAngle(Angel);
        setSpeed(speed);
        setPaint(paint);
    }
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((float) positionX, (float) positionY, size, drawPaint);
    }
    private void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
    public void setPaint(Paint paint){
        this.drawPaint = paint;
    }
    public Paint getPaint(){
        return drawPaint;
    }
    public double getPositionY() {
        return positionY;
    }
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }
    public double getPositionX() {
        return positionX;
    }
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }
    public int getAngle() {
        return angle;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getSpeed() {
        return speed;
    }
}
class board {
    private double X, Y;
    public void setXY(Double x, Double y){
        this.X = x;
        this.Y = y;
    }
    public ArrayList<CircleView> Circles = new ArrayList<>();
    public CircleView move(Context context,board board, int num){
        CircleView circle = board.Circles.get(num);
        double center_X = circle.getPositionX() + (circle.getSpeed() *
                Math.sin(circle.getAngle()* Math.PI / 360));
        double center_Y = circle.getPositionY() + (circle.getSpeed()*
                Math.cos(circle.getAngle()* Math.PI / 360));
        CircleView new_one = new CircleView(context, circle.getSize(), center_X, center_Y, circle.getAngle()
        ,circle.getSpeed(), circle.getPaint());
        if(center_X + new_one.getSize() >= X){
            new_one.setAngle(new_one.getAngle() * -1);
        }
        if(center_X - new_one.getSize() <= 0){
            new_one.setAngle(new_one.getAngle() * -1);
        }
        if(center_Y + new_one.getSize() >= Y){
            if (new_one.getAngle() >= 0) {
                new_one.setAngle(new_one.getAngle() + 90);
            }
            if (new_one.getAngle() <= 0) {
                new_one.setAngle(new_one.getAngle() - 90);
            }
        }
        if(center_Y - new_one.getSize() <= 0){
            if (new_one.getAngle() >= 0) {
                new_one.setAngle(new_one.getAngle() - 90);
            }
            if (new_one.getAngle() <= 0) {
                new_one.setAngle(new_one.getAngle() + 90);
            }
        }
        return new_one;
    }
}