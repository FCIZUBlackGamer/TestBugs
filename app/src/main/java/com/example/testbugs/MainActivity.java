package com.example.testbugs;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.leondzn.simpleanalogclock.SimpleAnalogClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> strings;
    String ex = "";
    SimpleAnalogClock clock;
    Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clock = findViewById(R.id.clock);


        handelTime();

        clock.setSecondTint(getColor(R.color.colorAccent))
                .setFaceTint(getColor(R.color.colorAccent1))
//                .setHourTint(getColor(R.color.colorAccent2))
                .setMinuteTint(getColor(R.color.colorAccent2))
        ;

        if (isExternalStorageWritable()) {

            File appDirectory = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TestBugs");
            File logDirectory = new File(appDirectory + "/log");
            File logFile = new File(logDirectory, "logcat" + System.currentTimeMillis() + ".txt");

            // create app folder
            if (!appDirectory.exists()) {
                appDirectory.mkdir();
            }

            // create log folder
            if (!logDirectory.exists()) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch (IOException r) {
                r.printStackTrace();
            }

        } else if (isExternalStorageReadable()) {
            // only readable
        } else {
            // not accessible
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
                Snackbar.make(view, "Replace with your own action" + strings.size() + 1, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                } catch (Exception e) {
//                    ex += e.getMessage();
//
//                }


            }
        });
    }

    public void handelTime() {
        currentTime = Calendar.getInstance().getTime();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clock.setTime(currentTime.getHours(), currentTime.getMinutes(), currentTime.getSeconds());
                handelTime();
            }
        }, 1000);

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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

    @Override
    protected void onDestroy() {
//        Todo: Upload Log File
        Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
