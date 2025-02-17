package com.example.gongmeishan.afinal;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import com.example.gongmeishan.afinal.model.BasicInfo;
import com.example.gongmeishan.afinal.model.Education;
import com.example.gongmeishan.afinal.model.Experience;
import com.example.gongmeishan.afinal.model.Project;
import com.example.gongmeishan.afinal.util.DateUtils;
import com.example.gongmeishan.afinal.util.ImageUtils;
import com.example.gongmeishan.afinal.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_EDUCATION_EDIT = 100;
    private static final int REQ_CODE_EXPERIMENCE_EDIT = 101;
    private static final int REQ_CODE_PROJECT_EDIT = 102;
    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;
    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";
    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EXPERIMENCE_EDIT);
            }
        });

        ImageButton addProjectBtn = (ImageButton) findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        });

        setupBasicInfo();
        setupEducations();
        setupExperiences();
        setupProjects();
    }

    //GET DATA FROM EDUCATIONeDITActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_EDUCATION_EDIT:
                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if (educationId != null) {
                        deleteEducation(educationId);
                    } else {
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    break;
                case REQ_CODE_EXPERIMENCE_EDIT:
                    String experienceId = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                    if (experienceId != null) {
                        deleteExperience(experienceId);
                    } else {
                        Experience experience = data.getParcelableExtra
                                (ExperienceEditActivity.KEY_EXPERIENCE);
                        updateExperience(experience);
                    }
                    break;
                case REQ_CODE_PROJECT_EDIT:
                    String projectId = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                    if (projectId != null) {
                        deleteProject(projectId);
                    } else {
                        Project project = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);
                        updateProject(project);
                    }
                    break;
                case REQ_CODE_EDIT_BASIC_INFO:
                    BasicInfo basicInfo = data.getParcelableExtra(BasicInfoActivity.KEY_BASIC);
                    updateBasicInfo(basicInfo);
                    break;

            }
        }
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }
        if (!found) {
            educations.add(education);
            ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        }
        setupEducations();
    }
    private void setupEducations() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.educations);
        educationsLayout.removeAllViews();
        for (Education education : educations) {
            educationsLayout.addView(getEducationView(education));
        }
    }
    private View getEducationView(final Education education) {
        View view = getLayoutInflater().inflate(R.layout.education_item, null);
        String dateString = DateUtils.dateToString(education.startDate) + " ~ "
                + DateUtils.dateToString(education.endDate);
        ((TextView) view.findViewById(R.id.education_school)).setText
                (education.school + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.education_major)).setText
                (education.major);
        ((TextView) view.findViewById(R.id.education_courses)).
                setText(formatItems(education.courses));
        ImageButton editEducationBtn = view.findViewById(R.id.edit_education_btn);
        editEducationBtn.setOnClickListener((new View.OnClickListener(){
                    @Override
          public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
               intent.putExtra(EducationEditActivity.KEY_EDUCATION,education);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
           }
       }
       ));
        return view;
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experience.id)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }
        if (!found) {
            experiences.add(experience);
        }
        setupExperiences();
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
    }
    private void setupExperiences() {
        LinearLayout experiencesLayout = (LinearLayout) findViewById(R.id.experiences);
        experiencesLayout.removeAllViews();
        for (Experience experience : experiences) {
            experiencesLayout.addView(getExperienceView(experience));
        }
    }
    private View getExperienceView(final Experience experience) {
        View view = getLayoutInflater().inflate(R.layout.experience_item, null);
        String dateString = DateUtils.dateToString(experience.startDate) + " ~ "
                + DateUtils.dateToString(experience.endDate);
        ((TextView) view.findViewById(R.id.experience_company)).setText
                (experience.company + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.experience_title)).setText
                (experience.title);
        ((TextView) view.findViewById(R.id.experience_doing)).
                setText(formatItems(experience.details));
        ImageButton editExperience = view.findViewById(R.id.edit_experience_btn);
        editExperience.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE,experience);
                startActivityForResult(intent, REQ_CODE_EXPERIMENCE_EDIT);
            }
        }
        ));
        return view;
    }

    private void updateProject(Project project) {
        boolean found = false;
        for (int i = 0; i < projects.size(); ++i) {
            Project e = projects.get(i);
            if (TextUtils.equals(e.id, project.id)) {
                found = true;
                projects.set(i,project);
                break;
            }
        }
        if (!found) {
            projects.add(project);
        }
        setupProjects();
        ModelUtils.save(this, MODEL_PROJECTS, projects);
    }
    private void setupProjects() {
        LinearLayout projectLayout = (LinearLayout) findViewById(R.id.projects);
        projectLayout.removeAllViews();
        for (Project project : projects) {
            projectLayout.addView(getProjectView(project));
        }
    }
    private View getProjectView(final Project project) {
        View view = getLayoutInflater().inflate(R.layout.project_item, null);
        String dateString = DateUtils.dateToString(project.startDate) + " ~ "
                + DateUtils.dateToString(project.endDate);
        ((TextView) view.findViewById(R.id.project_name)).setText
                (project.name + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.project_details)).
                setText(formatItems(project.details));
        ImageButton editProjects = view.findViewById(R.id.edit_project_btn);
        editProjects.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECT,project);
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        }
        ));
        return view;
    }

    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this,
                MODEL_BASIC_INFO,
                new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,
                MODEL_EDUCATIONS,
                new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(this,
                MODEL_EXPERIENCES,
                new TypeToken<List<Experience>>(){});
        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProjects = ModelUtils.read(this,
                MODEL_PROJECTS,
                new TypeToken<List<Project>>(){});
        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name)
                ? "Your name"
                : basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email)
                ? "Your email"
                : basicInfo.email);

        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageuri != null) {
            ImageUtils.loadImage(this, basicInfo.imageuri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }
        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInfoActivity.class);
                intent.putExtra(BasicInfoActivity.KEY_BASIC, basicInfo);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });
    }
    private void updateBasicInfo(BasicInfo basicInfo) {
        ModelUtils.save(this, MODEL_BASIC_INFO, basicInfo);

        this.basicInfo = basicInfo;
        setupBasicInfo();
    }
    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void deleteExperience(@NonNull String experienceId) {
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experienceId)) {
                experiences.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void deleteProject(@NonNull String projectId) {
        for (int i = 0; i < projects.size(); ++i) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, projectId)) {
                projects.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }
}