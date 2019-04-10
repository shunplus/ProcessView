package com.example.test.shundemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {


    @BindView(R.id.btn_demo_one)
    Button btnDemoOne;
    @BindView(R.id.btn_demo_two)
    Button btnDemoTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_demo_one, R.id.btn_demo_two, R.id.btn_demo_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_demo_one:
                startActivity(new Intent(this, DemoActivity.class));
                break;
            case R.id.btn_demo_two:
                startActivity(new Intent(this, DemoTwoActivity.class));
                break;
            case R.id.btn_demo_three:
                startActivity(new Intent(this, DemoThreeActivity.class));
                break;


        }
    }
}
