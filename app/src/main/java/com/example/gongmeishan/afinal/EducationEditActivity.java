package com.example.gongmeishan.afinal;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.Activity;
import android.text.TextUtils;
import com.example.gongmeishan.afinal.model.Education;
import com.example.gongmeishan.afinal.util.DateUtils;
import com.example.gongmeishan.afinal.MainActivity;
import java.util.Arrays;

public class EducationEditActivity extends AppCompatActivity {
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_EDUCATION_ID = "education_id";
    private Education education;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        education = getIntent().getParcelableExtra(KEY_EDUCATION);
        if(education != null) {
            setupUI();
        }
        else {findViewById(R.id.education_edit_delete).setVisibility(View.GONE);}
        setTitle(education == null ? "New education" : "Edit education");
    }


    private void setupUI() {
        ((EditText)findViewById(R.id.education_edit_school)).setText(education.school);
        ((EditText)findViewById(R.id.education_edit_major)).setText(education.major);
        ((EditText) findViewById(R.id.education_edit_start_date)).setText
                (DateUtils.dateToString(education.startDate));
        ((EditText) findViewById(R.id.education_edit_end_date))
                .setText(DateUtils.dateToString(education.endDate));
        ((EditText) findViewById(R.id.education_edit_course))
                .setText(TextUtils.join("\n", education.courses));
        findViewById(R.id.education_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_EDUCATION_ID, education.id);
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
                saveAndExit(education);
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
    private void saveAndExit(Education data) {
        if (data == null) {
            data = new Education();
        }
        data.school = ((EditText)findViewById(R.id.education_edit_school)).getText().toString();
        data.major = ((EditText)findViewById(R.id.education_edit_major)).getText().toString();
        data.startDate = DateUtils.stringToDate(((EditText)findViewById
                (R.id.education_edit_start_date)).getText().toString());
        data.endDate =  DateUtils.stringToDate(((EditText)findViewById
                (R.id.education_edit_end_date)).getText().toString());
        data.courses = Arrays.asList(TextUtils.split(((EditText)findViewById
                (R.id.education_edit_course)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDUCATION, data);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}