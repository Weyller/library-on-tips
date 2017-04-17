package com.example.rajk.libraryontips.reminderForBook.admin.StudentLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.Student;
import com.example.rajk.libraryontips.reminderForBook.admin.UI.viewbook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class StudentLogin extends AppCompatActivity {

    EditText rollno, password;
    Button button;
    String RollNo , Password;
    DatabaseReference database;
    sessions session;
    TextInputLayout input_email, input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);

        session = new sessions(getApplicationContext());
        rollno = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText3);
        button = (Button) findViewById(R.id.login);
        input_email = (TextInputLayout)findViewById(R.id.input_emaillogin);
        input_password = (TextInputLayout)findViewById(R.id.input_passwordlogin);
        database = FirebaseDatabase.getInstance().getReference().child("Student");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollNo = rollno.getText().toString().trim();
                Password = password.getText().toString().trim();

                if (TextUtils.isEmpty(RollNo)) {
                    input_email.setError("Enter Email");
                    if (input_email.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }

                if (TextUtils.isEmpty(Password)) {
                    input_password.setError("Enter Password");
                    if (input_password.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                    }
                }

                if(!TextUtils.isEmpty(RollNo) && !TextUtils.isEmpty(Password)){
                    login();
                }
                else
                    Toast.makeText(getBaseContext(),"Enter Complete Details", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void login() {
        DatabaseReference db = database.child(RollNo).getRef();

            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Map<String,Object> mapBook= (Map<String,Object>)  dataSnapshot.getValue();

                        Student student = new Student();
                        student.setName((String) mapBook.get("Name"));
                        student.setPassword((String) mapBook.get("Password"));
                        student.setRollNo((String) mapBook.get("RollNo"));
                        student.setTotalFine((String) mapBook.get("TotalFine"));

                        if (!student.getPassword().equals(Password)) {
                            Toast.makeText(getBaseContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            session.createloginsession(RollNo,student.getName());
                            startActivity(new Intent(getBaseContext(), viewbook.class));
                            finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Student Not Registered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
