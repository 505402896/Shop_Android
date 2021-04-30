package com.example.a50540.lastorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ForgetActivity extends Activity {
    Button btn_next;
    ImageView btn_return2;
    EditText et_username,et_name,et_id_card;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        btn_return2 = (ImageView) findViewById(R.id.btn_return2);
        btn_next = (Button)findViewById(R.id.btn_next);
        et_name = (EditText) findViewById(R.id.et_forget_name);
        et_username = (EditText) findViewById(R.id.et_forget_user);
        et_id_card = (EditText)findViewById(R.id.et_forget_idcard);

        username = getIntent().getStringExtra("username");
        et_username.setText(username);

        btn_return2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetActivity.this,LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetActivity.this,UpdateActivity.class);
                intent.putExtra("username",et_username.getText().toString().trim());
                intent.putExtra("name",et_name.getText().toString().trim());
                intent.putExtra("id_card",et_id_card.getText().toString().trim());
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }
}
