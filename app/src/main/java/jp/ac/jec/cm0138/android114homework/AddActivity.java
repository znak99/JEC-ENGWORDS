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
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private TextView back, updateDelete;
    private EditText english, japanese;
    private Button add, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        back = findViewById(R.id.back);
        english = findViewById(R.id.english);
        japanese = findViewById(R.id.japanese);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        updateDelete = findViewById(R.id.updateDelete);

        back.setOnClickListener(view -> goToHome());

        updateDelete.setOnClickListener(view -> goToUpdateWord());


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);

        add.setOnClickListener(view -> {
            if (english.getText().toString().equals("") || japanese.getText().toString().equals("")) {
                Toast.makeText(this, "英単語または日本語を入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor c = db.rawQuery(
                    "SELECT * FROM words WHERE eng_word = '" +
                            english.getText().toString() + "'", null);
            if (c.getCount() > 0) {
                Toast.makeText(this, "既に登録されている英単語です", Toast.LENGTH_SHORT).show();
                return;
            }

            String sql = "INSERT INTO words VALUES ('" +
                    english.getText().toString() + "', '" + japanese.getText().toString() + "')";
            db.execSQL(sql);
            goToHome();
        });

        delete.setOnClickListener(view -> {
            showAlert("全部削除", "本当に単語カードを全部削除しますか？", "はい", (dialogInterface, i) -> {
                String sql = "DELETE FROM words";
                db.execSQL(sql);
                goToHome();
            });
        });
    }

    public void goToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToUpdateWord() {
        Intent intent = new Intent(this, UpdateWordActivity.class);
        startActivity(intent);
    }

    private void showAlert(String title, String message, String button, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title).setMessage(message);

        builder.setNegativeButton("いいえ", null);
        builder.setPositiveButton(button, listener);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}