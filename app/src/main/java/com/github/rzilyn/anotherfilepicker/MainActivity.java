package com.github.rzilyn.anotherfilepicker;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.MultiFilePicker;
import com.github.rzilyn.multifilepicker.utils.FileTypePreset;
import com.github.rzilyn.multifilepicker.utils.FileUpdateMethod;
import com.github.rzilyn.multifilepicker.utils.Orientation;
import com.rzilyn.github.anotherfilepicker.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonOpenPicker;
    ListView listViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOpenPicker = findViewById(R.id.button_openPick);
        listViewResult = findViewById(R.id.listview_results);

        buttonOpenPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fileFilter = {"jpg","txt"};

                FilePickerOptions options = new FilePickerOptions.Build()
                        .setOrientation(Orientation.PORTRAIT)
                        .enableDeepScan(false)
                        .setFileLimit(10)
//                        .setSinglePick(true)
                        .setHint("Pick a file")
                        .enableTab(true)
                        .addFileFilter(FileTypePreset.DOCUMENTS)
                        .setFileUpdateMethod(FileUpdateMethod.STREAM)
                        .setFileUpdateMethod(FileUpdateMethod.BUFFER)
//                        .setColorScheme(ContextCompat.getColor(MainActivity.this,R.color.colorBlue),
//                                ContextCompat.getColor(MainActivity.this,R.color.colorBrown),
//                                ContextCompat.getColor(MainActivity.this,R.color.colorGreen))
                        .build();

                MultiFilePicker.pickFile(MainActivity.this,options,10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data!=null){
            if(requestCode == 10){
                List<String> fileList = data.getStringArrayListExtra(MultiFilePicker.RESULT_FILE);
                BaseAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList);
                listViewResult.setAdapter(adapter);
            }
        }
    }
}
