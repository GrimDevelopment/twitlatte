/*
 * Copyright 2015-2018 The twitlatte authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.moko256.twitlatte;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by moko256 on 2016/11/14.
 *
 * @author moko256
 */

public class LicensesActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);

        TextView licenseTextView = findViewById(R.id.license_text);

        if (getIntent()!=null){
            String title = getIntent().getStringExtra("title");
            if (title != null){
                actionBar.setTitle(title);
            }

            String libName=getIntent().getStringExtra("library_name");
            if (libName!=null){
                try {
                    InputStream textStream;

                    String path;

                    switch (libName){
                        case "support_v4":
                        case "support_v7":
                        case "support_v13":
                        case "support_v14":
                        case "support_design":
                        case "support_custom_tabs":
                            path = "licenses/android_support.txt";
                            break;

                        default:
                            path = "licenses/"+libName+".txt";
                            break;
                    }

                    textStream=getAssets().open(path);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(textStream));
                    StringBuilder builder = new StringBuilder();

                    String lineText;

                    while ((lineText = reader.readLine()) != null){
                        builder.append(lineText).append("\n");
                    }

                    licenseTextView.setText(builder.toString());

                    reader.close();
                    textStream.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}