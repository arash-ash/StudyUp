package com.tonikamitv.loginregister;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends ActionBarActivity {

    Button buttonCreateTable, buttonJoinTable, buttonCourses, buttonStudents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        buttonCreateTable = (Button) findViewById(R.id.button_cTable);
        buttonCourses = (Button) findViewById(R.id.button_courses);
        buttonJoinTable = (Button) findViewById(R.id.button_jTable);
        buttonStudents = (Button) findViewById(R.id.button_students);

    }

    public void secondActivity (View v){
        Intent goToSecond = new Intent();

        if (v.equals(buttonCreateTable))
            goToSecond.setClass(this, Tables.class);
            startActivity(goToSecond);

        if (v.equals(buttonCourses)) {
            goToSecond.setClass(this, viewCourses.class);
            startActivity(goToSecond);
        }

        if (v.equals(buttonJoinTable)) {
            goToSecond.setClass(this, viewTables.class);
            startActivity(goToSecond);
        }

        if (v.equals(buttonStudents)){
            goToSecond.setClass(this, viewStudents.class);
            startActivity(goToSecond);
        }

    }
}
