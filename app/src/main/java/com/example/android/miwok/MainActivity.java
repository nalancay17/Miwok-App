/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
// import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        setListListener(findViewById(R.id.numbers), NumbersActivity.class);
        setListListener(findViewById(R.id.family), FamilyActivity.class);
        setListListener(findViewById(R.id.colors), ColorsActivity.class);
        setListListener(findViewById(R.id.phrases), PhrasesActivity.class);
    }

    /**
     * @param view is the view we want to set an onClicklistener.
     * @param cls  is the kind of activity we want to open when the view is clicked.
     */
    private void setListListener(View view, Class<?> cls) {
        TextView list = (TextView) view;
        list.setOnClickListener(caughtEvent -> {
            Intent openListActivity = new Intent(view.getContext(), cls);
            startActivity(openListActivity);
        });
    }

}
