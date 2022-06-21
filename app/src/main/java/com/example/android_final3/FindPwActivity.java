package com.example.android_final3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FindPwActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase myFirebase = FirebaseDatabase.getInstance();
    DatabaseReference myDB_Reference = myFirebase.getReference();

    private EditText txtFindId, txtFindName;
    private Button btnFindPw;
    private Toast popup;

    String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
        setTitle("비밀번호 찾기");

        txtFindId = (EditText)findViewById(R.id.txtFindId);
        txtFindName = (EditText)findViewById(R.id.txtFindName);

        btnFindPw = (Button)findViewById(R.id.btnFindPw);

        btnFindPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        id = txtFindId.getText().toString();
        name = txtFindName.getText().toString();

        if(id.equals("")){
            popup = Toast.makeText(this, "아이디를 입력해주세요..", Toast.LENGTH_SHORT);
            popup.show();
        }else if(name.equals("")){
            popup = Toast.makeText(this, "이름을 입력해주세요..", Toast.LENGTH_SHORT);
            popup.show();
        }else{
            myDB_Reference.child("member").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        String findPw = null;

                        ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                        for(HashMap<String, String> member : members){
                            Log.d("firebase", "하이");
                            if(member.get("id").equals(id) && member.get("name").equals(name)){
                                Log.d("firebase", "걸림");
                                findPw = member.get("pw");
                                break;
                            }
                        }

                        if(findPw != "") {
                            popup = Toast.makeText(getApplicationContext(), "찾으시는 비밀번호는 "+findPw+"입니다.", Toast.LENGTH_LONG);
                        }else{
                            popup = Toast.makeText(getApplicationContext(), "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT);
                        }
                        popup.show();
                    }
                }
            });
        }
    }
}