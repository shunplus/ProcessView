package com.example.test.shundemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xushun on  2018/11/21 22:00.
 */

public class DemoTwoActivity extends Activity implements ProcessView1.OnItemClickListener {

    @BindView(R.id.process_view)
    ProcessView1 processView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_two);
        ButterKnife.bind(this);
        processView1.setOnItemClickListener(this);
        processView1.setData(getTextShow(), getMeasureRecorde());
    }

    public HashMap<Integer, List<String>> getTextShow() {
        List<String> text1 = Arrays.asList("执行通知");
        List<String> text2 = Arrays.asList("送达文书");
        List<String> text3 = Arrays.asList("强行措施", "财产调查", "解除措施");
        List<String> text4 = Arrays.asList("查询存款", "搜查", "传唤", "悬赏执行");
        List<String> text5 = Arrays.asList("查明财产");
        List<String> text6 = Arrays.asList("查封", "扣押", "冻结", "扣划", "评估", "拍卖");
        List<String> text7 = Arrays.asList("执行和解", "终本约谈", "自动履行");
        HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        map.put(0, text1);
        map.put(1, text2);
        map.put(2, text3);
        map.put(3, text4);
        map.put(4, text5);
        map.put(5, text6);
        map.put(6, text7);
        return map;
    }


    private HashMap<Integer, List<Boolean>> getMeasureRecorde() {
        List<Boolean> text1 = Arrays.asList(true);
        List<Boolean> text2 = Arrays.asList(true);
        List<Boolean> text3 = Arrays.asList(true, true, true);
        List<Boolean> text4 = Arrays.asList(true, true, true, true);
        List<Boolean> text5 = Arrays.asList(true);
        List<Boolean> text6 = Arrays.asList(true, true, true, true, true, true);
        List<Boolean> text7 = Arrays.asList(false, true, false);
        HashMap<Integer, List<Boolean>> map = new HashMap<Integer, List<Boolean>>();
        map.put(0, text1);
        map.put(1, text2);
        map.put(2, text3);
        map.put(3, text4);
        map.put(4, text5);
        map.put(5, text6);
        map.put(6, text7);
        return map;
    }

    @Override
    public void onItemClick(int vPosition, int hPosition, String text) {
        Log.d("manny", "vPosition=" + vPosition + ",hPosition=" + hPosition + ",text= " + text);
        Toast.makeText(this, "vPosition=" + vPosition + ",hPosition=" + hPosition + ",text= " + text, Toast.LENGTH_LONG).show();
    }
}
