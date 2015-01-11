package com.goodwebple.step2study;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by j8n3y on 15. 1. 3..
 */
public class RankModeActivity extends Activity {
    private static ArrayList<Activity> activities = new ArrayList<Activity>();

    final int ANS_WRONG = 0;
    final int ANS_CORRECT = 1;
    final int INIT_COLOR = 2;

    SQLiteDatabase db;
    DatabaseHelper helper;

    private final String QUESTION_NUM = "5";

    String QUESTION = "mean";
    String ANSWER = "word";

    private int score = 0;
    private int ansNum;
    Cursor wordData = null;

    private String cardpack;

    private TextView tView_question;
    private TextView tView_ans1;
    private TextView tView_ans2;
    private TextView tView_ans3;
    private TextView tView_ans4;
    private FrameLayout frame_inner;
    private TextView text_score;
    private Button btn_retry;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null) {
            Thread tmpThread = timer;
            timer = null;
            tmpThread.interrupt();
        }
        activities.remove(this);
        Log.i("Destroy", "!!!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_rank);
        activities.add(this);

        timeBar = (ProgressBar) findViewById(R.id.progressBar);
        tView_question = (TextView) findViewById(R.id.text_question);
        tView_ans1 = (TextView) findViewById(R.id.text_answer1);
        tView_ans2 = (TextView) findViewById(R.id.text_answer2);
        tView_ans3 = (TextView) findViewById(R.id.text_answer3);
        tView_ans4 = (TextView) findViewById(R.id.text_answer4);

        frame_inner = (FrameLayout) findViewById(R.id.frame_inner);
        text_score = (TextView) findViewById(R.id.text_score);
        btn_retry = (Button) findViewById(R.id.btn_retry);

        frame_inner.setVisibility(View.INVISIBLE);

        // TODO: should receive name of Cardpack from parent Activity
        cardpack = "wordlist";

        try {
            readQuestion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_inner.setVisibility(View.INVISIBLE);
                try {
                    readQuestion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                changeBGColor(INIT_COLOR);
                printQuestion();
            }
        });

        selectedAnswerListener sAL = new selectedAnswerListener();

        tView_ans1.setOnClickListener(sAL); tView_ans1.setTag(1);
        tView_ans2.setOnClickListener(sAL); tView_ans2.setTag(2);
        tView_ans3.setOnClickListener(sAL); tView_ans3.setTag(3);
        tView_ans4.setOnClickListener(sAL); tView_ans4.setTag(4);

        printQuestion();
    }

    public class selectedAnswerListener implements View.OnClickListener {

        public void onClick(View v) {
            Integer ans = Integer.parseInt(v.getTag().toString());
            if (ans == ansNum) {
                score += currentTime;
                Toast.makeText(getApplicationContext(), "Correct. Score : " + score, Toast.LENGTH_SHORT).show();
                changeBGColor(ANS_CORRECT);
            } else {
                Toast.makeText(getApplicationContext(), "Wrong. Score : " + score, Toast.LENGTH_SHORT).show();
                changeBGColor(ANS_WRONG);
            }
            stopTimer();
        }
    }

    public void readQuestion() throws SQLException {
        String[] columns = new String[]{ QUESTION, ANSWER };

        helper = new DatabaseHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        wordData = db.query(cardpack, columns, null, null, null, null, "RANDOM()", QUESTION_NUM);
        wordData.moveToFirst();
    }

    public void printQuestion() {
        String question = wordData.getString(0);
        String answer = wordData.getString(1);

        Random iRan = new Random();
        ansNum = iRan.nextInt(4) + 1;

        Cursor ranAnsData = null;
        String[] columns = new String[]{ ANSWER };

        helper = new DatabaseHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        ranAnsData = db.query(cardpack, columns, null, null, null, null, "RANDOM()", "3");

        if (ranAnsData != null && ranAnsData.moveToFirst()) {
            startTimer();
            this.tView_question.setText(question);
            switch (ansNum) {
                case 1:
                    Log.i("!!ANSWER!!", "1");
                    ranAnsData.moveToFirst();
                    tView_ans1.setText(answer);
                    tView_ans2.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans3.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans4.setText(ranAnsData.getString(0));
                    break;
                case 2:
                    Log.i("!!ANSWER!!", "2");
                    ranAnsData.moveToFirst();
                    tView_ans1.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans2.setText(answer);
                    tView_ans3.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans4.setText(ranAnsData.getString(0));
                    break;
                case 3:
                    Log.i("!!ANSWER!!", "3");
                    ranAnsData.moveToFirst();
                    tView_ans1.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans2.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans3.setText(answer);
                    tView_ans4.setText(ranAnsData.getString(0));
                    break;
                case 4:
                    Log.i("!!ANSWER!!", "4");
                    ranAnsData.moveToFirst();
                    tView_ans1.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans2.setText(ranAnsData.getString(0)); ranAnsData.moveToNext();
                    tView_ans3.setText(ranAnsData.getString(0));
                    tView_ans4.setText(answer);
                    break;
            }
        }
    }

    public void changeBGColor(int correct) {
        if (correct == 1) {
            // if Correct
            switch (ansNum) {
                case 0:
                case 1:
                    tView_ans1.setBackgroundColor(Color.GREEN); tView_ans2.setBackgroundColor(Color.BLUE);
                    tView_ans3.setBackgroundColor(Color.BLUE); tView_ans4.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    tView_ans1.setBackgroundColor(Color.BLUE); tView_ans2.setBackgroundColor(Color.GREEN);
                    tView_ans3.setBackgroundColor(Color.BLUE); tView_ans4.setBackgroundColor(Color.BLUE);
                    break;
                case 3:
                    tView_ans1.setBackgroundColor(Color.BLUE); tView_ans2.setBackgroundColor(Color.BLUE);
                    tView_ans3.setBackgroundColor(Color.GREEN); tView_ans4.setBackgroundColor(Color.BLUE);
                    break;
                case 4:
                    tView_ans1.setBackgroundColor(Color.BLUE); tView_ans2.setBackgroundColor(Color.BLUE);
                    tView_ans3.setBackgroundColor(Color.BLUE); tView_ans4.setBackgroundColor(Color.GREEN);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT);
            }
        } else if (correct == 0) {
            // if Wrong
            switch (ansNum) {
                case 0:
                case 1:
                    tView_ans1.setBackgroundColor(Color.BLUE); tView_ans2.setBackgroundColor(Color.RED);
                    tView_ans3.setBackgroundColor(Color.RED); tView_ans4.setBackgroundColor(Color.RED);
                    break;
                case 2:
                    tView_ans1.setBackgroundColor(Color.RED); tView_ans2.setBackgroundColor(Color.BLUE);
                    tView_ans3.setBackgroundColor(Color.RED); tView_ans4.setBackgroundColor(Color.RED);
                    break;
                case 3:
                    tView_ans1.setBackgroundColor(Color.RED); tView_ans2.setBackgroundColor(Color.RED);
                    tView_ans3.setBackgroundColor(Color.BLUE); tView_ans4.setBackgroundColor(Color.RED);
                    break;
                case 4:
                    tView_ans1.setBackgroundColor(Color.RED); tView_ans2.setBackgroundColor(Color.RED);
                    tView_ans3.setBackgroundColor(Color.RED); tView_ans4.setBackgroundColor(Color.BLUE);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT);
            }
        } else if (correct == 2) {
            tView_ans1.setBackgroundColor(Color.WHITE); tView_ans2.setBackgroundColor(Color.WHITE);
            tView_ans3.setBackgroundColor(Color.WHITE); tView_ans4.setBackgroundColor(Color.WHITE);
        }
    }

    /*
        TIMER Methods
    */
    private ProgressBar timeBar;
    private volatile Thread timer = null;
    private int currentTime = 100;

    public synchronized void startTimer() {
        timer = null;
        timer = new Thread(null, timeChecker, "Timer");
        timer.start();
    }

    public synchronized void stopTimer() {
        if (timer != null) {
            Thread tmpThread = timer;
            timer = null;
            tmpThread.interrupt();
            if(wordData.moveToNext()) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeBGColor(INIT_COLOR);
                        printQuestion();
                    }
                }, 1500);
            } else {
                frame_inner.setVisibility(View.VISIBLE);
                text_score.setText(String.valueOf(score));
                score = 0;
            }
        } else {
            Log.i("stopTimer", "Timer Not Stopped");
        }
    }

    private Runnable timeChecker = new Runnable() {
        @Override
        public void run() {
            if(Thread.currentThread() == timer) {
                currentTime = 100;
                timeBar.setProgress(currentTime);
                final int zero = 0;
                while (currentTime > zero) {
                    try {
                        progressBarHandle.sendMessage(progressBarHandle.obtainMessage());
                        Thread.sleep(100);
                    } catch (final InterruptedException e) {
                        return;
                    } catch (final Exception e) {
                        return;
                    }
                }
            }
        }

        Handler progressBarHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                currentTime --;
                timeBar.setProgress(currentTime);
                if(currentTime%5 == 0) {
                    Log.i("timer dec!!", String.valueOf(currentTime));
                }
                if(currentTime == 0) {
                    Toast.makeText(getApplicationContext(), "Timeout. Score : " + score, Toast.LENGTH_SHORT).show();
                    changeBGColor(ANS_WRONG);
                    stopTimer();
                }
            }
        };
    };

}