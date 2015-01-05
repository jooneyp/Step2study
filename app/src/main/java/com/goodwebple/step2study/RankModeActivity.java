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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Random;

/**
 * Created by j8n3y on 15. 1. 3..
 */
public class RankModeActivity extends Activity {
    SQLiteDatabase db;
    DatabaseHelper helper;

    private final String QUESTION_NUM = "100";

    private int score = 0;
    private int ansNum;
    private int qNum = 1;
    Cursor wordData = null;

    private String cardpack;

    private TextView question;
    private TextView ans1;
    private TextView ans2;
    private TextView ans3;
    private TextView ans4;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_rank);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        question = (TextView) findViewById(R.id.text_question);
        ans1 = (TextView) findViewById(R.id.text_answer1);
        ans2 = (TextView) findViewById(R.id.text_answer2);
        ans3 = (TextView) findViewById(R.id.text_answer3);
        ans4 = (TextView) findViewById(R.id.text_answer4);

        // TODO: should receive name of Cardpack from parent Activity
        cardpack = "wordlist";

        try {
            makeQuestion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        selectedAnswerListener sAL = new selectedAnswerListener();

        ans1.setOnClickListener(sAL);
        ans1.setTag(1);
        ans2.setOnClickListener(sAL);
        ans2.setTag(2);
        ans3.setOnClickListener(sAL);
        ans3.setTag(3);
        ans4.setOnClickListener(sAL);
        ans4.setTag(4);

        Log.i("0", "onCreate");
        printQuestion1();
    }



    public class selectedAnswerListener implements View.OnClickListener {

        public void onClick(View v) {
            Integer ans = Integer.parseInt(v.getTag().toString());
            if (ans == ansNum) {
                score += currentTime;
                Toast.makeText(getApplicationContext(), "Correct. Score : " + score, Toast.LENGTH_SHORT).show();
                changeBGColor(1);
            } else {
                Toast.makeText(getApplicationContext(), "Wrong. Score : " + score, Toast.LENGTH_SHORT).show();
                changeBGColor(0);
            }
            stopTimer();
            Log.i("stopTimer", "Time stopped");
        }
    }

    public void makeQuestion() throws SQLException {
        String[] columns = new String[]{"mean", "word"};

        helper = new DatabaseHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        wordData = db.query(cardpack, columns, null, null, null, null, "RANDOM()", QUESTION_NUM);
        wordData.moveToFirst();
    }

    public void printQuestion1() {
        qNum ++;
        String mean = wordData.getString(0);
        String word = wordData.getString(1);
        startTimer();
        printQuestion2(mean, word);
    }

    public void printQuestion2(String mean, String word) {
        Log.i("6", "printQuestion2");
        Random iRan = new Random();
        ansNum = iRan.nextInt(4) + 1;

        Cursor ranAnsData = null;
        String[] columns = new String[]{"word"};

        helper = new DatabaseHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        ranAnsData = db.query(cardpack, columns, null, null, null, null, "RANDOM()", "3");
        Log.i("ranAnsData", ranAnsData.toString());
        Log.i("ranAnsDataCount", String.valueOf(ranAnsData.getCount()));
        Log.i("ans", String.valueOf(ansNum));

        if (ranAnsData != null && ranAnsData.moveToFirst()) {
            Log.i("ranAnsData", ranAnsData.getString(0));
            question.setText(mean);
            switch (ansNum) {
                case 0:
                case 1:
                    Log.i("!!ANSWER!!", "1");
                    ranAnsData.moveToFirst();
                    ans1.setText(word);
                    ans2.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans3.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans4.setText(ranAnsData.getString(0));
                    break;
                case 2:
                    Log.i("!!ANSWER!!", "2");
                    ranAnsData.moveToFirst();
                    ans1.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans2.setText(word);
                    ans3.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans4.setText(ranAnsData.getString(0));
                    break;
                case 3:
                    Log.i("!!ANSWER!!", "3");
                    ranAnsData.moveToFirst();
                    ans1.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans2.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans3.setText(word);
                    ans4.setText(ranAnsData.getString(0));
                    break;
                case 4:
                    Log.i("!!ANSWER!!", "4");
                    ranAnsData.moveToFirst();
                    ans1.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans2.setText(ranAnsData.getString(0));
                    ranAnsData.moveToNext();
                    ans3.setText(ranAnsData.getString(0));
                    ans4.setText(word);
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
                    ans1.setBackgroundColor(Color.GREEN);
                    ans2.setBackgroundColor(Color.BLUE);
                    ans3.setBackgroundColor(Color.BLUE);
                    ans4.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    ans1.setBackgroundColor(Color.BLUE);
                    ans2.setBackgroundColor(Color.GREEN);
                    ans3.setBackgroundColor(Color.BLUE);
                    ans4.setBackgroundColor(Color.BLUE);
                    break;
                case 3:
                    ans1.setBackgroundColor(Color.BLUE);
                    ans2.setBackgroundColor(Color.BLUE);
                    ans3.setBackgroundColor(Color.GREEN);
                    ans4.setBackgroundColor(Color.BLUE);
                    break;
                case 4:
                    ans1.setBackgroundColor(Color.BLUE);
                    ans2.setBackgroundColor(Color.BLUE);
                    ans3.setBackgroundColor(Color.BLUE);
                    ans4.setBackgroundColor(Color.GREEN);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT);
            }
        } else if (correct == 0) {
            // if Wrong
            switch (ansNum) {
                case 0:
                case 1:
                    ans1.setBackgroundColor(Color.BLUE);
                    ans2.setBackgroundColor(Color.RED);
                    ans3.setBackgroundColor(Color.RED);
                    ans4.setBackgroundColor(Color.RED);
                    break;
                case 2:
                    ans1.setBackgroundColor(Color.RED);
                    ans2.setBackgroundColor(Color.BLUE);
                    ans3.setBackgroundColor(Color.RED);
                    ans4.setBackgroundColor(Color.RED);
                    break;
                case 3:
                    ans1.setBackgroundColor(Color.RED);
                    ans2.setBackgroundColor(Color.RED);
                    ans3.setBackgroundColor(Color.BLUE);
                    ans4.setBackgroundColor(Color.RED);
                    break;
                case 4:
                    ans1.setBackgroundColor(Color.RED);
                    ans2.setBackgroundColor(Color.RED);
                    ans3.setBackgroundColor(Color.RED);
                    ans4.setBackgroundColor(Color.BLUE);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT);
            }
        } else if (correct == 2) {
            ans1.setBackgroundColor(Color.WHITE);
            ans2.setBackgroundColor(Color.WHITE);
            ans3.setBackgroundColor(Color.WHITE);
            ans4.setBackgroundColor(Color.WHITE);
        }
        Log.i("8", "BGColor changed");
    }

    /*
        TIMER Threads
    */


    private ProgressBar progressBar;
    private volatile Thread timer = null;
    private int currentTime = 100;

    public synchronized void startTimer() {
        timer = null;
        timer = new Thread(null, timeChecker, "Timer");
        Log.i("timer!!@@", "right before timer starts");
        timer.start();
    }

    public synchronized void stopTimer() {
        if (timer != null) {
            Thread tmpThread = timer;
            timer = null;
            tmpThread.interrupt();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeBGColor(2);
                    wordData.moveToNext();
                    printQuestion1();
                }
            }, 2000);
        } else {
            Log.i("stopTimer", "null");
        }
    }

    private Runnable timeChecker = new Runnable() {
        @Override
        public void run() {
            if(Thread.currentThread() == timer) {
                currentTime = 100;
                progressBar.setProgress(currentTime);
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
                currentTime -= 1;
                progressBar.setProgress(currentTime);
                if(currentTime%5 == 0) {
                    Log.i("timer dec!!", String.valueOf(currentTime));
                }
                if(currentTime == 0) {
                    Log.i("Timer STOPPED", "time 0, timerstop");
                    Toast.makeText(getApplicationContext(), "Timeout. Score : " + score, Toast.LENGTH_SHORT).show();
                    changeBGColor(0);
                    stopTimer();
                }
            }
        };
    };

}