package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private TextView back;
    private EditText english, japanese;
    private Button edit;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        back = findViewById(R.id.back);
        english = findViewById(R.id.english);
        japanese = findViewById(R.id.japanese);
        edit = findViewById(R.id.edit);

        Intent intent = getIntent();
        String eng = intent.getStringExtra("eng");

        english.setText(eng);

        back.setOnClickListener(v -> finish());

        edit.setOnClickListener(view -> {
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.onCreate(db);

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COL_ENG, english.getText().toString());
            cv.put(DBHelper.COL_JPN, japanese.getText().toString());

            db.update(DBHelper.TABLE_NAME, cv, DBHelper.COL_ENG + " = '" + eng + "'", null);

//            String sql =
//                    "UPDATE " + DBHelper.TABLE_NAME + " " +
//                            "SET " + DBHelper.COL_ENG + " = '" + english.getText().toString() + "', " +
//                            "" + DBHelper.COL_JPN + " = '" + japanese.getText().toString() + "'" +
//                    " WHERE " + DBHelper.COL_ENG + " = '" + eng + "'";
//            Cursor c = db.rawQuery(sql, null);



            showAlert("成功", "単語カードを編集しました。", "確認", (dialogInterface, i) -> {
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
            });
        });
    }

    private void showAlert(String title, String message, String button, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title).setMessage(message);

        builder.setPositiveButton(button, listener);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}