package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private TextView back;
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

        back.setOnClickListener(view -> finish());

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);

        add.setOnClickListener(view -> {
            if (english.getText().toString().equals("") || japanese.getText().toString().equals("")) {
                Toast.makeText(this, "英単語または日本語を入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            String sql = "INSERT INTO words VALUES ('" +
                    english.getText().toString() + "', '" + japanese.getText().toString() + "')";
            db.execSQL(sql);
            finish();
        });

        delete.setOnClickListener(view -> {
            String sql = "DELETE FROM words";
            db.execSQL(sql);
            finish();
        });
    }
}