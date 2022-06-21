package com.example.android_final3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase myFirebase = FirebaseDatabase.getInstance();
    DatabaseReference myDB_Reference = myFirebase.getReference();

    private Button btnJoin, btnReload;
    private EditText txtJoinId, txtJoinPw, txtJoinRePw, txtJoinName, txtCaptcha;
    private TextView lblIdChk, lblPwChk;
    private ImageView imgCaptcha;
    private Captcha c;

    private boolean idChk, pwChk, cdChk = false;

    private Toast popup;

    private String cnt;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setTitle("회원가입");

        btnJoin = (Button)findViewById(R.id.btnJoin);
        btnReload = (Button)findViewById(R.id.btnReload);

        txtJoinId = (EditText)findViewById(R.id.txtJoinId);
        txtJoinPw = (EditText)findViewById(R.id.txtJoinPw);
        txtJoinRePw = (EditText)findViewById(R.id.txtJoinRePw);
        txtJoinName = (EditText)findViewById(R.id.txtJoinName);

        lblIdChk = (TextView)findViewById(R.id.lblIdChk);
        lblPwChk = (TextView)findViewById(R.id.lblPwChk);

        txtCaptcha = (EditText)findViewById(R.id.txtCaptcha);
        imgCaptcha = (ImageView)findViewById(R.id.imgCaptcha);

        c = loadCaptchaImage();
        code = c.answer;

        btnJoin.setOnClickListener(this);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = loadCaptchaImage();
                code = c.answer;
            }
        });

        txtJoinId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    Query query = myDB_Reference.child("member");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot postSnapShot: snapshot.getChildren()){
                                if(postSnapShot.child("id").getValue().equals(txtJoinId.getText().toString())){
                                    lblIdChk.setText("중복된 아이디입니다.");
                                    lblIdChk.setTextColor(Color.RED);
                                    idChk = false;
                                    return;
                                }else{
                                    lblIdChk.setText("사용 가능한 아이디입니다.");
                                    lblIdChk.setTextColor(Color.GREEN);
                                    idChk = true;
                                    return;
                                }
                            }
                            lblIdChk.setText("사용 가능한 아이디입니다.");
                            lblIdChk.setTextColor(Color.GREEN);
                            idChk = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Cancle");
                        }
                    });
                }
            }
        });

        txtJoinPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                pwChk(b);
            }
        });

        txtJoinRePw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                pwChk(b);
            }
        });

        txtCaptcha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(txtCaptcha.getText().toString().equals(code)) {
                    cdChk = true;
                } else {
                    cdChk = false;
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        String id, pw, name, captcha;
        id = txtJoinId.getText().toString();
        pw = txtJoinPw.getText().toString();
        name = txtJoinName.getText().toString();
        captcha = txtCaptcha.getText().toString();

        
        if(!valueCheck(txtJoinId, idChk, "아이디")) return;
        if(!valueCheck(txtJoinPw, pwChk, "비밀번호")) return;
        if(!valueCheck(txtCaptcha, cdChk, "보안문자")) return;
        if(!valueCheck(txtJoinId, id, "아이디")) return;
        if(!valueCheck(txtJoinPw, pw, "비밀번호")) return;
        if(!valueCheck(txtJoinName, name, "이름")) return;
        if(!valueCheck(txtCaptcha, captcha, "보안문자")) return;

        myDB_Reference.child("member").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String cnt = String.valueOf(task.getResult().getChildrenCount());

                    myDB_Reference.child("member").child(cnt).child("id").setValue(id);
                    myDB_Reference.child("member").child(cnt).child("pw").setValue(pw);
                    myDB_Reference.child("member").child(cnt).child("name").setValue(name);

                    Toast p = Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT);
                    p.show();
                    finish();
                    return;
                }
            }
        });
    }

    public void pwChk(boolean b){
        if(!b){
            if(txtJoinPw.getText().toString().equals(txtJoinRePw.getText().toString())){
                lblPwChk.setText("");
                pwChk = true;
            }else{
                lblPwChk.setText("비밀번호가 일치하지 않습니다.");
                lblPwChk.setTextColor(Color.RED);
                pwChk = false;
            }
        }
    }

    public Captcha loadCaptchaImage() {
        Captcha c = new TextCaptcha(550,150,6,TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
        imgCaptcha.setImageBitmap(c.image);

        return c;
    }

    public boolean valueCheck(EditText obj, String value, String text) {
        boolean result = true;
        if(value.equals("")) {
            popup = Toast.makeText(this, text+"을(를) 입력해주세요.", Toast.LENGTH_SHORT);
            popup.show();
            obj.requestFocus();
            result = false;
        }
        return result;
    }

    public boolean valueCheck(EditText obj, boolean b, String text) {
        boolean result = true;
        if(!b) {
            popup = Toast.makeText(this, text+"을(를) 입력해주세요.", Toast.LENGTH_SHORT);
            popup.show();
            obj.requestFocus();
            result = false;
        }
        return result;
    }
}