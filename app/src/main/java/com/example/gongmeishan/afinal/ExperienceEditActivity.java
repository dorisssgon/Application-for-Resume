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

import com.example.gongmeishan.afinal.model.Education;
import com.example.gongmeishan.afinal.model.Experience;
import com.example.gongmeishan.afinal.util.DateUtils;

import java.util.Arrays;

/**
 * Created by gongmeishan on 2017/8/23.
 */

public class ExperienceEditActivity extends AppCompatActivity {
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_EXPERIENCE_ID = "experience_id";
    private Experience experience;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        experience=getIntent().getParcelableExtra(KEY_EXPERIENCE);
        if(experience != null) {
            setupUI();
        }
        else {
            findViewById(R.id.experiment_edit_delete).setVisibility(View.GONE);
        }
        setTitle(experience == null ? "New experience" : "Edit experience");
    }


    private void setupUI() {
        ((EditText)findViewById(R.id.experience_company_edit)).setText(experience.company);
        ((EditText)findViewById(R.id.experience_title_edit)).setText(experience.title);
        ((EditText) findViewById(R.id.experience_edit_start_date_edit)).setText
                (DateUtils.dateToString(experience.startDate));
        ((EditText) findViewById(R.id.experience_edit_end_date_edit))
                .setText(DateUtils.dateToString(experience.endDate));
        ((EditText) findViewById(R.id.experience_doing_edit))
                .setText(TextUtils.join("\n", experience.details));
        findViewById(R.id.experiment_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_EXPERIENCE_ID, experience.id);
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
                saveAndExit(experience);
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
    private void saveAndExit(Experience data) {
        if (data == null) {
            data = new Experience();
        }
        data.company = ((EditText)findViewById(R.id.experience_company_edit)).getText().toString();
        data.title = ((EditText)findViewById(R.id.experience_title_edit)).getText().toString();
        data.startDate = DateUtils.stringToDate(((EditText)findViewById
                (R.id.experience_edit_start_date_edit)).getText().toString());
        data.endDate =  DateUtils.stringToDate(((EditText)findViewById
                (R.id.experience_edit_end_date_edit)).getText().toString());
        data.details = Arrays.asList(TextUtils.split(((EditText)findViewById
                (R.id.experience_doing_edit)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXPERIENCE, data);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

