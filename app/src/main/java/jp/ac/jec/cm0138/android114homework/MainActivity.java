package jp.ac.jec.cm0138.android114homework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView userName, editWordsTxt;
    private ImageView setUserName, editWordsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        setUserName = findViewById(R.id.setUserName);

        editWordsTxt = findViewById(R.id.editWordsTxt);
        editWordsImg = findViewById(R.id.editWordsImg);

        userName.setOnClickListener(this::moveToSetName);
        setUserName.setOnClickListener(this::moveToSetName);

        editWordsTxt.setOnClickListener(this::moveToEdit);
        editWordsImg.setOnClickListener(this::moveToEdit);

        // 프리퍼런스에 이름 저장
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

    // EditActivity로 이동
    public void moveToEdit(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

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
        }
    }
}