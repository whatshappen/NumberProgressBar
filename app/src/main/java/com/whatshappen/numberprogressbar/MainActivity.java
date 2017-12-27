package com.whatshappen.numberprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.whatshappen.numberprogressbar.numberprogressbar.NumberHorizontalProgressBar;

public class MainActivity extends AppCompatActivity {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NumberHorizontalProgressBar num_pb = (NumberHorizontalProgressBar) findViewById(R.id.num_pb);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    count += 2;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            num_pb.setProgress(count);
                        }
                    });
                }
            }
        }).start();
    }
}
