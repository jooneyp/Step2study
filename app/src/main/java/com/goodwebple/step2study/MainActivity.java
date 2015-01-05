package com.goodwebple.step2study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", false);

        if (firstRun == false) { // if running this app first time.
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
            // Show IntroActivity.
            Intent i = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(i);
        } else {
            setContentView(R.layout.activity_main);
        }

        // TODO : Allow user to make firstRun boolean "false" in Settings menu.

        Button introBtn = (Button) findViewById(R.id.btn_show_intro);
        Button rankBtn = (Button) findViewById(R.id.btn_challengeMode);
        Button dbInitBtn = (Button) findViewById(R.id.btn_dbinit);
        // for Debugging (show IntroPage)
        rankBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intentSubActivity = new Intent(MainActivity.this, RankModeActivity.class);
                startActivity(intentSubActivity);
            }
        });
        introBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent dbManager = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(dbManager);
            }
        });
        dbInitBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                try {
                    dbHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
