/*  Copyright 2017 by AlaskaLinuxUser (https://thealaskalinuxuser.wordpress.com)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.alaskalinuxuser.justnotes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static com.alaskalinuxuser.justnotes.MainActivity.colorChoice;
import static com.alaskalinuxuser.justnotes.MainActivity.fabColorChoice;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch (colorChoice) {

            case 0:
                toolbar.setBackgroundColor(Color.BLUE);
                break;

            case 1:
                toolbar.setBackgroundColor(Color.RED);
                break;

            case 2:
                toolbar.setBackgroundColor(Color.GREEN);
                break;

            case 3:
                toolbar.setBackgroundColor(Color.GRAY);
                break;

            case 4:
                toolbar.setBackgroundColor(Color.BLACK);
                break;

            case 5:
                toolbar.setBackgroundColor(Color.YELLOW);
                break;

            case 6:
                toolbar.setBackgroundColor(Color.MAGENTA);
                break;

            case 7:
                toolbar.setBackgroundColor(Color.CYAN);
                break;

        }

        FloatingActionButton fabGit = (FloatingActionButton) findViewById(R.id.fabGithub);

        switch (fabColorChoice) {

            case 0:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                break;

            case 1:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;

            case 2:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;

            case 3:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                break;

            case 4:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                break;

            case 5:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                break;

            case 6:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                break;

            case 7:
                fabGit.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                break;

        }

        fabGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myHubGit();

            }
        });

        FloatingActionButton fabWord = (FloatingActionButton) findViewById(R.id.fabWord);

        switch (fabColorChoice) {

            case 0:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                break;

            case 1:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;

            case 2:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;

            case 3:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                break;

            case 4:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                break;

            case 5:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                break;

            case 6:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                break;

            case 7:
                fabWord.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                break;

        }

        fabWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myWebsite();

            }
        });

        FloatingActionButton fabBack = (FloatingActionButton) findViewById(R.id.fabReturn);

        switch (fabColorChoice) {

            case 0:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                break;

            case 1:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;

            case 2:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;

            case 3:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                break;

            case 4:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                break;

            case 5:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                break;

            case 6:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                break;

            case 7:
                fabBack.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                break;

        }

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goBack();

            }
        });
    }

    // When they click on the github icon.
    public void myHubGit () {

        goToUrl ( "https://github.com/alaskalinuxuser");

    }

    // When they click on the wordpress icon.
    public void myWebsite () {

        goToUrl ( "https://thealaskalinuxuser.wordpress.com");

    }

    // When they click on the how to text.
    public void howL (View view) {

        goToUrl ( "http://www.apache.org/licenses/LICENSE-2.0");

    }

    // To launch one of the above URL's.
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    // To kill this activity and go back to Activity Main.
    public void goBack () {
        finish();
    }


}