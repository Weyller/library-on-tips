package com.example.rajk.libraryontips.reminderForBook.admin.StudentLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.rajk.libraryontips.reminderForBook.admin.UI.viewbook;

import java.util.HashMap;

public class sessions {

    SharedPreferences StudentDetails;
    SharedPreferences.Editor editor;
    Context _context;
    int mode=0;

    String prefname="StudentDetails";
    private String is_login="IsLoggedIn";
    public String RollNo;

    public sessions(Context context)
    {
        this._context=context;
        StudentDetails= _context.getSharedPreferences(prefname,mode);
        editor=StudentDetails.edit();
    }

    public void createloginsession(String user,String name)
    {
        editor.putBoolean(is_login,true);
        editor.putString("RollNo",user);
        editor.putString("Name",name);
        editor.commit();
    }

    public boolean isloggedin()
    {
        return StudentDetails.getBoolean(is_login,false);
    }

    public void checklogin()
    {
        if(!this.isloggedin())
        {
            Intent intent =new Intent(_context,StudentLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
        else
        {
            Intent intent =new Intent(_context,viewbook.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }

    public HashMap<String, String>  getuserdetails()
    {
        HashMap<String,String> details = new HashMap<String, String>();
        details.put(RollNo,StudentDetails.getString(RollNo,null));

        return details;
    }

    public void clearloginsession()
    {
        editor.clear();
        editor.commit();
        Intent intent=new Intent(_context,StudentLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }
}
