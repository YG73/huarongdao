package com.example.huarongdao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RankingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        SharedPreferences prefs = getSharedPreferences("huarongdao_ranking", MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 10; i++) { // 假设10关
            int best = prefs.getInt("level_" + i + "_best_steps", -1);
            if (best != -1) {
                sb.append("第").append(i).append("关：").append(best).append("步\n");
            } else {
                sb.append("第").append(i).append("关：暂无记录\n");
            }
        }
        TextView tv = findViewById(R.id.tv_ranking);
        tv.setText(sb.toString());
    }
} 