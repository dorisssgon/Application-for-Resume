package com.example.gongmeishan.afinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.gongmeishan.afinal.model.Experience;
import com.example.gongmeishan.afinal.model.Project;
import com.example.gongmeishan.afinal.util.DateUtils;

import java.util.Arrays;

/**
 * Created by gongmeishan on 2017/8/23.
 */

public class ProjectEditActivity extends AppCompatActivity {
    public static final String KEY_PROJECT = "project";
    public static final String KEY_PROJECT_ID = "project_id";
    private Project project;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        project=getIntent().getParcelableExtra(KEY_PROJECT);
        if(project != null) {
            setupUI();
        }
        else {
            findViewById(R.id.project_edit_delete).setVisibility(View.GONE);
        }
        setTitle(project == null ? "New project" : "Edit project");
    }


    private void setupUI() {
        ((EditText)findViewById(R.id.project_name_edit)).setText(project.name);
        ((EditText) findViewById(R.id.project__edit_start_date_edit)).setText
                (DateUtils.dateToString(project.startDate));
        ((EditText) findViewById(R.id.project__edit_end_date_edit))
                .setText(DateUtils.dateToString(project.endDate));
        ((EditText) findViewById(R.id.project_details_edit))
                .setText(TextUtils.join("\n", project.details));
        findViewById(R.id.project_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_PROJECT_ID, project.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem Item) {
        switch (Item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveAndExit(project);
                return true;
        }
        return super.onOptionsItemSelected(Item);
    }
    //let menu inflate in menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
    //saved data flow then mainacitivity apply data
    private void saveAndExit(Project data) {
        if (data == null) {
            data = new Project();
        }
        data.name = ((EditText)findViewById(R.id.project_name_edit)).getText().toString();
        data.startDate = DateUtils.stringToDate(((EditText)findViewById
                (R.id.project__edit_start_date_edit)).getText().toString());
        data.endDate =  DateUtils.stringToDate(((EditText)findViewById
                (R.id.project__edit_end_date_edit)).getText().toString());
        data.details = Arrays.asList(TextUtils.split(((EditText)findViewById
                (R.id.project_details_edit)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PROJECT, data);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
