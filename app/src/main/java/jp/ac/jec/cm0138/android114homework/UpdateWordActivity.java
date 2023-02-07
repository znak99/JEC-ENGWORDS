package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class UpdateWordActivity extends AppCompatActivity {

    private boolean isDeleteMode = false;

    private ToggleButton toggleButton;
    private EditText eng;
    private Button submit;
    private TextView back, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_word);

        toggleButton = findViewById(R.id.toggleButton);
        eng = findViewById(R.id.eng);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);

        back.setOnClickListener(v -> finish());

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDeleteMode = isChecked;
            title.setText(isDeleteMode ? "単語カードを削除する" : "単語カードを編集する");
            submit.setText(isDeleteMode ? "削除" : "検索");
            toggleButton.setText(isDeleteMode ? "削除" : "編集");
            submit.setBackgroundResource(isDeleteMode ? R.drawable.red_round : R.drawable.blue_round);
            toggleButton.setBackgroundResource(isDeleteMode ? R.drawable.blue_round : R.drawable.red_round);
        });

        submit.setOnClickListener(view -> {
            // db에서 eng찾기
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            dbHelper.onCreate(db);

            String sql = "SELECT eng_word FROM words WHERE eng_word = '" + eng.getText().toString() + "'";
            Cursor c = db.rawQuery(sql, null);

            if (c.getCount() == 0) {
                showAlert("エラー", "一致する単語カードがありません。", null);
            } else {
                if (isDeleteMode) {
                    sql = "DELETE FROM words WHERE eng_word = '" + eng.getText().toString() + "'";
                    db.execSQL(sql);
                    showAlert("削除",
                            "単語カード「" + eng.getText().toString() + "」を削除しました。",
                            (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateWordActivity.this, MainActivity.class);
                                startActivity(intent);
                            });
                } else {
                    // moveToEdit
                }
            }
        });
    }

    private void showAlert(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title).setMessage(message);

        builder.setNeutralButton("確認", listener);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}