package com.example.android_final3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase myFirebase;
    DatabaseReference myDB_Reference = null;

    private Button btnLogin, btnGoJoin, btnGoFindPw;
    private EditText txtLoginId, txtLoginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("로그인");

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnGoJoin = (Button)findViewById(R.id.btnGoJoin);
        btnGoFindPw = (Button)findViewById(R.id.btnGoFindPw);

        txtLoginId = (EditText)findViewById(R.id.txtLoginId);
        txtLoginPw = (EditText)findViewById(R.id.txtLoginPw);

        btnLogin.setOnClickListener(this);
        btnGoJoin.setOnClickListener(this);
        btnGoFindPw.setOnClickListener(this);

        myFirebase = FirebaseDatabase.getInstance();
        myDB_Reference = myFirebase.getReference();
    }

    @Override
    public void onClick(View v){
        Toast popup;
        if(v == btnLogin){
            String id, pw;
            id = txtLoginId.getText().toString();
            pw = txtLoginPw.getText().toString();

            if(id.equals("")){
                popup = Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT);
                popup.show();
                txtLoginId.requestFocus();
                return;
            }

            if(pw.equals("")){
                popup = Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
                popup.show();
                txtLoginPw.requestFocus();
                return;
            }

            myDB_Reference.child("member").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Boolean login = false;
                        String name = null;
                        Toast p;

                        ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                        for(HashMap<String, String> member : members){
                            if(member.get("id").equals(txtLoginId.getText().toString()) && member.get("pw").equals(txtLoginPw.getText().toString())){
                                Log.d("firebase", "와 로그인 성공이다");
                                login = true;
                                name = member.get("name");
                                break;
                            }else{
                                Log.d("firebase", "와 로그인 실패다");
                            }
                        }

                        if(login) {
                            p = Toast.makeText(getApplicationContext(), name+"님 안녕하세요.", Toast.LENGTH_SHORT);
                            Intent testMap = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(testMap);
                        }else{
                            p = Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                        }
                        p.show();
                    }
                }
            });
        }else if(v == btnGoJoin) {
            Intent joinIntent = new Intent(MainActivity.this, JoinActivity.class);
            startActivity(joinIntent);
        }else{
            Intent findPwIntent = new Intent(MainActivity.this, FindPwActivity.class);
            startActivity(findPwIntent);
        }
    }
}