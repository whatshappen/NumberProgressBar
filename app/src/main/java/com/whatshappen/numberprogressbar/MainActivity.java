package com.whatshappen.numberprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.whatshappen.numberprogressbar.numberprogressbar.CircleProgressBar;
import com.whatshappen.numberprogressbar.numberprogressbar.NumberHorizontalProgressBar;

public class MainActivity extends AppCompatActivity {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NumberHorizontalProgressBar num_pb = (NumberHorizontalProgressBar) findViewById(R.id.num_pb);
        final NumberHorizontalProgressBar num_pb2 = (NumberHorizontalProgressBar) findViewById(R.id.num_pb2);
        final NumberHorizontalProgressBar num_pb3 = (NumberHorizontalProgressBar) findViewById(R.id.num_pb3);
        final CircleProgressBar cpb = (CircleProgressBar) findViewById(R.id.cpb);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    if(count ==0){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count += 2;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            num_pb.setProgress(count);
                            num_pb2.setProgress(count);
                            num_pb3.setProgress(count);
                            cpb.setProgress(count);
                        }
                    });
                }
            }
        }).start();
    }
}
