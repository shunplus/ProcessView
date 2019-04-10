package com.example.test.shundemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xushun on  2018/11/21 21:57.
 */

public class DemoActivity extends Activity implements ProcessView.OnItemClickListener {
    @BindView(R.id.process_view)
    ProcessView processView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_one);
        ButterKnife.bind(this);
        processView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int vPosition, int hPosition, String text) {
        Log.d("manny", "vPosition=" + vPosition + ",hPosition=" + hPosition + ",text= " + text);
        Toast.makeText(this, "vPosition=" + vPosition + ",hPosition=" + hPosition + ",text= " + text, Toast.LENGTH_LONG).show();
    }
}
