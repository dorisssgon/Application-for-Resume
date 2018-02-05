package com.example.gongmeishan.afinal;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import com.example.gongmeishan.afinal.model.BasicInfo;
import com.example.gongmeishan.afinal.util.DateUtils;
import com.example.gongmeishan.afinal.util.ImageUtils;
import com.example.gongmeishan.afinal.util.PermissionUtils;

import static android.R.attr.data;

/**
 * Created by gongmeishan on 2017/8/23.
 */

public class BasicInfoActivity extends AppCompatActivity{
    public static final String KEY_BASIC = "basicinfo";
    private static final int REQ_CODE_PICK_IMAGE = 109;
    private BasicInfo basicinfo;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_edi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        basicinfo = getIntent().getParcelableExtra(KEY_BASIC);

        if(basicinfo != null) {
            setupUIForEdit(basicinfo);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                showImage(imageUri);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }
    public void setupUIForEdit(@NonNull BasicInfo data) {
        ((EditText) findViewById(R.id.basic_info_edit_name))
                .setText(data.name);
        ((EditText) findViewById(R.id.basic_info_edit_email))
                .setText(data.email);

        if (data.imageuri != null) {
            showImage(data.imageuri);
        }
        findViewById(R.id.basic_info_edit_image_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.checkPermission(BasicInfoActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils.requestReadExternalStoragePermission(BasicInfoActivity.this);
                } else {
                    pickImage();
                }
            }
        });
    }
    private void saveAndExit(@Nullable BasicInfo data) {
        if (data == null){
            data = new BasicInfo();
        }
        data.name = ((EditText)findViewById(R.id.basic_info_edit_name)).getText().toString();
        data.email=((EditText)findViewById(R.id.basic_info_edit_email)).getText().toString();
        data.imageuri =(Uri) findViewById(R.id.basic_info_edit_image).getTag();

        Intent resultintent = new Intent();
        resultintent.putExtra(KEY_BASIC,data);
        setResult(Activity.RESULT_OK, resultintent);
        finish();
    }
    private void showImage(@NonNull Uri imageUri) {
        ImageView imageView = (ImageView) findViewById(R.id.basic_info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setTag(imageUri);
        ImageUtils.loadImage(this, imageUri, imageView);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                REQ_CODE_PICK_IMAGE);
    }
    public boolean onOptionsItemSelected(MenuItem Item) {
        switch (Item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveAndExit(basicinfo);
                return true;
        }
        return super.onOptionsItemSelected(Item);
    }
    //let menu inflate in menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
}
