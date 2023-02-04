package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {

    private TextView txt1, txt2, txt3, txt4, moveToHome;
    private Button addWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        moveToHome = findViewById(R.id.moveToHome);
        addWord = findViewById(R.id.addWord);

        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);
        moveToHome.setVisibility(View.GONE);
        addWord.setVisibility(View.GONE);

        moveToHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        addWord.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        });

        new Handler().postDelayed(() -> {
            txt1.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                txt2.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    txt3.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        txt4.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> {
                            addWord.setVisibility(View.VISIBLE);
                            moveToHome.setVisibility(View.VISIBLE);
                        }, 300);
                    }, 300);
                }, 300);
            }, 300);
        }, 500);
    }
}