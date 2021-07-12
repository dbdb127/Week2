package com.seungwoodev.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity_NotSDK extends AppCompatActivity {

    private String strNick, strProfileImg, strEmail;
    private Button tab_button, logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
//        strProfileImg = intent.getStringExtra("profileImg");
        strEmail = intent.getStringExtra("email");

        TextView tv_nick = findViewById(R.id.tv_nickName);
        TextView tv_email = findViewById(R.id.tv_email);
//        ImageView iv_profile = findViewById(R.id.iv_profile);

        // set nickname
        tv_nick.setText(strNick);
        // set email
        tv_email.setText(strEmail);
        // set profile image
//        Glide.with(this).load(strProfileImg).into(iv_profile);

        // log out
        logout_button = (Button)findViewById(R.id.btn_logout);
        logout_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(SubActivity_NotSDK.this,"Logout", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SubActivity_NotSDK.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // when click, go to the tab
        tab_button = (Button)findViewById(R.id.btn_tab);
        tab_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity_NotSDK.this, MainActivity_Tab.class);
                intent.putExtra("email", strEmail);
                startActivity(intent);
            }
        });

    }
}