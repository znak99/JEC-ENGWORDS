package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetNameActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;

    private TextView cancel;
    private EditText userName;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        cancel = findViewById(R.id.cancel);
        userName = findViewById(R.id.userName);
        save = findViewById(R.id.save);

        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("name"));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (userName.getText().toString().equals("")) {
                    // alert dialog  でエラーを表示する
                    new AlertDialog.Builder(SetNameActivity.this)
                            .setTitle("エラー")
                            .setMessage("名前を入力してください")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    intent.putExtra("name", userName.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}