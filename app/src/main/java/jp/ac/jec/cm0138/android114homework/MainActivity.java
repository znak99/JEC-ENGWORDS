package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private int current = 0;

    private TextView userName, editWordsTxt, todayWord, todayWordWarning;
    private ImageView setUserName, editWordsImg;
    private LinearLayout wordList;
    private CheckBox showRandom;
    private Button start;

    private ArrayList<String> engWords = new ArrayList<>();
    private ArrayList<String> engWordsTemp = new ArrayList<>();

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        setUserName = findViewById(R.id.setUserName);

        wordList = findViewById(R.id.wordList);
        todayWord = findViewById(R.id.todayWord);
        todayWordWarning = findViewById(R.id.todayWordWarning);

        editWordsTxt = findViewById(R.id.editWordsTxt);
        editWordsImg = findViewById(R.id.editWordsImg);

        userName.setOnClickListener(this::moveToSetName);
        setUserName.setOnClickListener(this::moveToSetName);

        wordList.setOnClickListener(view -> showNextWord());

        showRandom = findViewById(R.id.showRandom);
        start = findViewById(R.id.start);

        start.setOnClickListener(this::moveToStudy);

        editWordsTxt.setOnClickListener(this::moveToEdit);
        editWordsImg.setOnClickListener(this::moveToEdit);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);

        engWords.clear();
        String sql = "SELECT eng_word FROM words";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            engWordsTemp.add(c.getString(c.getColumnIndex("eng_word")));
        }

        if (engWordsTemp.size() > 0) {
            Collections.shuffle(engWordsTemp);
            Log.i("engWordsTemp", engWordsTemp.toString());
            for (int i = 0; i < engWordsTemp.size(); i++) {
                engWords.add(engWordsTemp.get(i));
                if (i == 4) {
                    break;
                }
            }
        }

        c.close();

        if (engWords.size() == 0) {
            todayWord.setText(R.string.today_word_noContent);
            todayWordWarning.setVisibility(View.INVISIBLE);
        } else {
            todayWord.setText(engWords.get(current));
            if(engWords.size() > 1) {
                todayWordWarning.setVisibility(View.VISIBLE);
            }
        }

        // 프리퍼런스에서 이름 가져오기
        SharedPreferences pref = getSharedPreferences("ANDROID114", MODE_PRIVATE);
        if (pref.getString("name", "").equals("")) {
            userName.setText(R.string.set_name);
        } else {
            userName.setText(pref.getString("name", "") + "さん");
        }
    }

    public void moveToSetName(View view) {
        Intent intent = new Intent(this, SetNameActivity.class);
        if (userName.getText().toString().equals("名前を登録してください！")) {
            intent.putExtra("name", "");
        } else {
            intent.putExtra("name", userName.getText().toString().substring(0, userName.getText().toString().length() - 2));
        }
        startActivityForResult(intent, SetNameActivity.REQUEST_CODE);
    }

    private void showNextWord() {
        if (engWords.size() <= 1) {
            return;
        }
        current++;
        todayWord.setText(engWords.get(current));

        if (current == engWords.size() - 1 || current == 4) {
            current = -1;
        }
    }

    // StudyActivity로 이동
    public void moveToStudy(View view) {
        Intent intent = new Intent(this, StudyActivity.class);
        intent.putExtra("showRandom", showRandom.isChecked());
        startActivity(intent);
    }

    // EditActivity로 이동
    public void moveToEdit(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, AddActivity.REQUEST_CODE);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetNameActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // 유저 이름 설정
                userName.setText(data.getStringExtra("name") + "さん");

                // 프리퍼런스에 저장
                SharedPreferences preferences = getSharedPreferences("ANDROID114", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name", data.getStringExtra("name"));
                editor.apply();
            }
        } else if (requestCode == AddActivity.REQUEST_CODE) {
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            dbHelper.onCreate(db);

            engWords.clear();

            String sql = "SELECT eng_word FROM words";
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                engWordsTemp.add(c.getString(c.getColumnIndex("eng_word")));
            }

            if (engWordsTemp.size() > 0) {
                Collections.shuffle(engWordsTemp);
                Log.i("engWordsTemp", engWordsTemp.toString());
                for (int i = 0; i < engWordsTemp.size(); i++) {
                    engWords.add(engWordsTemp.get(i));
                    if (i == 4) {
                        break;
                    }
                }
            }
            c.close();

            Log.i("main", engWords.toString());

            if (engWords.size() == 0) {
                todayWord.setText(R.string.today_word_noContent);
                todayWordWarning.setVisibility(View.INVISIBLE);
            } else {
                todayWord.setText(engWords.get(current));
                if(engWords.size() > 1) {
                    todayWordWarning.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}