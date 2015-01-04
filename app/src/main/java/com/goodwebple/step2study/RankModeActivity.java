package com.goodwebple.step2study;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Random;

/**
 * Created by j8n3y on 15. 1. 3..
 */
public class RankModeActivity extends Activity {
    private final int QUESTION_NUM = 100;
    private final int cardpackEndIdxNum = 7500;

    private int score = 0;
    private int ansNum;

    private String cardpack;

    private TextView question;
    private TextView ans1;
    private TextView ans2;
    private TextView ans3;
    private TextView ans4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_rank);

        question = (TextView) findViewById(R.id.text_question);
        ans1 = (TextView) findViewById(R.id.text_answer1);
        ans2 = (TextView) findViewById(R.id.text_answer2);
        ans3 = (TextView) findViewById(R.id.text_answer3);
        ans4 = (TextView) findViewById(R.id.text_answer4);

        // TODO: should receive name of Cardpack from parent Activity
        cardpack = "Wordlist";
        try {
            makeQuestion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeQuestion() throws SQLException {
        Cursor wordData = null;

        // read Database.
        DatabaseAdapter mDbAdapter = new DatabaseAdapter(this.getApplicationContext());

        mDbAdapter.createDatabase();
        mDbAdapter.open();

        wordData = mDbAdapter.getRankModeData(cardpack, QUESTION_NUM);

        printQuestion1(wordData);
    }

    public void printQuestion1(Cursor wordData) {
        int score_flag = 0;

        View.OnClickListener selectedAnswerListener = new View.OnClickListener() {
            public void onClick(View v) {
                Integer ans = Integer.parseInt(v.getTag().toString());
                if(ans == ansNum)
                    score ++;
            }
        };

        ans1.setOnClickListener(selectedAnswerListener);
        ans1.setTag(1);
        ans2.setOnClickListener(selectedAnswerListener);
        ans2.setTag(2);
        ans3.setOnClickListener(selectedAnswerListener);
        ans3.setTag(3);
        ans4.setOnClickListener(selectedAnswerListener);
        ans4.setTag(4);

        while (wordData.getPosition() < QUESTION_NUM) {
            score_flag = score;
            String mean = wordData.getString(0);
            String word = wordData.getString(1);
            printQuestion2(mean, word);
            if (score > score_flag) {
                // if Correct
                switch(ansNum) {
                    case 0:
                    case 1:
                        ans1.setBackgroundColor(Color.GREEN);
                        ans2.setBackgroundColor(Color.BLUE);
                        ans3.setBackgroundColor(Color.BLUE);
                        ans4.setBackgroundColor(Color.BLUE);
                    case 2:
                        ans1.setBackgroundColor(Color.BLUE);
                        ans2.setBackgroundColor(Color.GREEN);
                        ans3.setBackgroundColor(Color.BLUE);
                        ans4.setBackgroundColor(Color.BLUE);
                    case 3:
                        ans1.setBackgroundColor(Color.BLUE);
                        ans2.setBackgroundColor(Color.BLUE);
                        ans3.setBackgroundColor(Color.GREEN);
                        ans4.setBackgroundColor(Color.BLUE);
                    case 4:
                        ans1.setBackgroundColor(Color.BLUE);
                        ans2.setBackgroundColor(Color.BLUE);
                        ans3.setBackgroundColor(Color.BLUE);
                        ans4.setBackgroundColor(Color.GREEN);
                    default:
                        Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT);
                }
            } else {
                // if Wrong
                switch(ansNum) {
                    case 0:
                    case 1:
                        ans1.setBackgroundColor(Color.BLUE);
                        ans2.setBackgroundColor(Color.RED);
                        ans3.setBackgroundColor(Color.RED);
                        ans4.setBackgroundColor(Color.RED);
                    case 2:
                        ans1.setBackgroundColor(Color.RED);
                        ans2.setBackgroundColor(Color.BLUE);
                        ans3.setBackgroundColor(Color.RED);
                        ans4.setBackgroundColor(Color.RED);
                    case 3:
                        ans1.setBackgroundColor(Color.RED);
                        ans2.setBackgroundColor(Color.RED);
                        ans3.setBackgroundColor(Color.BLUE);
                        ans4.setBackgroundColor(Color.RED);
                    case 4:
                        ans1.setBackgroundColor(Color.RED);
                        ans2.setBackgroundColor(Color.RED);
                        ans3.setBackgroundColor(Color.RED);
                        ans4.setBackgroundColor(Color.BLUE);
                    default:
                        Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT);
                }
            }
            wordData.moveToNext();
        }
    }

    public void printQuestion2(String mean, String word) {
        Random iRan = new Random();
        ansNum = iRan.nextInt(4) + 1;
        Integer idx;

        Cursor ranAnsData = null;

        DatabaseAdapter mDbAdapter = new DatabaseAdapter(this.getApplicationContext());
        ranAnsData = mDbAdapter.getRankModeData(cardpack, 3);

        question.setText(mean);
        switch(ansNum) {
            case 0:
            case 1:
                ans1.setText(word);
                ans2.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans3.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans4.setText(ranAnsData.getString(1));
            case 2:
                ans1.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans2.setText(word);
                ans3.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans4.setText(ranAnsData.getString(1));
            case 3:
                ans1.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans2.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans3.setText(word);
                ans4.setText(ranAnsData.getString(1));
            case 4:
                ans1.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans2.setText(ranAnsData.getString(1));
                ranAnsData.moveToNext();
                ans3.setText(ranAnsData.getString(1));
                ans4.setText(word);
        }
    }
}
