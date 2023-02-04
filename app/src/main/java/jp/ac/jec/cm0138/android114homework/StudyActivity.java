package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {
    private int current = 0;
    private boolean isNextButton = false;

    private ArrayList<String> engWords = new ArrayList<>();
    private ArrayList<String> jpnWords = new ArrayList<>();

    private TextView back, english, japanese;
    private Button prev, fail, pass;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        back = findViewById(R.id.back);
        english = findViewById(R.id.english);
        japanese = findViewById(R.id.japanese);
        fail = findViewById(R.id.fail);
        pass = findViewById(R.id.pass);
        prev = findViewById(R.id.prev);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);

        engWords.clear();
        jpnWords.clear();
        String sql = "SELECT * FROM words";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            engWords.add(c.getString(c.getColumnIndex("eng_word")));
            jpnWords.add(c.getString(c.getColumnIndex("jpn_word")));
        }

        {
            Intent intent = getIntent();
            if (intent.getBooleanExtra("showRandom", false)) {
                // engWordsとjpnWordsをシャッフルする
                for (int i = 0; i < engWords.size(); i++) {
                    int random = (int) (Math.random() * engWords.size());
                    String tmp = engWords.get(i);
                    engWords.set(i, engWords.get(random));
                    engWords.set(random, tmp);
                    tmp = jpnWords.get(i);
                    jpnWords.set(i, jpnWords.get(random));
                    jpnWords.set(random, tmp);
                }
            }
        }

        prev.setOnClickListener(view -> {
            if (current > 0) {
                current--;
                english.setText(engWords.get(current));
            } else {
                // Dialogを表示する
                Toast.makeText(this, "最初のカードです。", Toast.LENGTH_SHORT).show();
            }
        });

        c.close();

        english.setText(engWords.get(0));
        japanese.setText("");

        back.setOnClickListener(v -> finish());

        pass.setOnClickListener(v -> {
            if (current >= engWords.size() - 1) {
                Intent intent = new Intent(this, FinishActivity.class);
                startActivity(intent);
                return;
            }
            current++;
            english.setText(engWords.get(current));
            japanese.setText("");
        });

        fail.setOnClickListener(v -> {
            if (isNextButton) {
                if (current >= engWords.size() - 1) {
                    Intent intent = new Intent(this, FinishActivity.class);
                    startActivity(intent);
                    return;
                }
                current++;
                english.setText(engWords.get(current));
                japanese.setText("");
                isNextButton = false;
                fail.setText("分からない");
                pass.setEnabled(true);
                fail.setWidth(0);
                pass.setVisibility(View.VISIBLE);
                prev.setVisibility(View.VISIBLE);
            } else {
                fail.setText("次へ");
                fail.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                pass.setEnabled(false);
                pass.setVisibility(View.INVISIBLE);
                prev.setVisibility(View.INVISIBLE);
                japanese.setText(jpnWords.get(current));
                isNextButton = true;
            }
        });
    }
}