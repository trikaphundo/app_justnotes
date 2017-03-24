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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView theList;
    ArrayList<String> listNote;
    ArrayAdapter<String> addaptedAray;
    Boolean askDelete;
    int toDelete;
    Set<String> stringSet;
    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Defining the list view that I want by id.
        theList = (ListView) findViewById(R.id.theList);

        // Defining the array of lattitude.
        listNote = new ArrayList();

        // Defining an adapter, to adapt my array list to the correct format.
        addaptedAray = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listNote);

        // Using the adapter to adapt my array list to the defined list view that I declared already.
        theList.setAdapter(addaptedAray);

        askDelete = false;
        stringSet = new HashSet<String>();
        stringSet.clear();
        myPrefs = this.getSharedPreferences("com.alaskalinuxuser.justnotes", Context.MODE_PRIVATE);

        try {

            stringSet.addAll(myPrefs.getStringSet("ssnotes", null));
            //Testing only, not needed//Log.i("WJH", "Got saved notes.");

        } catch (Exception e) {

            Log.i("WJH", "Malformed saved notes.");

        }

        if (stringSet.size() > 0) {

            listNote.addAll(stringSet);
            //Testing only, not needed//Log.i("WJH", "Added saved notes.");

        } else {

            Log.i("WJH", "No saved notes.");

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String c = "";

                Intent myIntent = new Intent(getApplicationContext(), writenote.class);
                myIntent.putExtra("listNote", c);
                startActivityForResult(myIntent, 1);

            }
        });



        if (listNote.size() <= 1) {

            // Adding to the array list.
            listNote.add("Welcome to the note app! You can save new notes and delete old ones. I hope this is useful." +
                    "You can delete this note from the main menu with a long press if you want. " +
                    "Just click back to go back to the main menu without saving, or press the save button to keep your note.");


            // Testing only //Log.i("WJH", "Adding note to blank list.");// Testing only //
        }

        // Setting up a listener to "listen" for me to click on something in the list.
        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Overriding the generic code that Android uses for list views to do something specific.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int z, long l) {


                if (askDelete) {

                    Log.i("WJH", "Can't click, because we are deleting something.");

                } else {

                    // Converting the integer of "z" to a string with the name of the item clicked in the list.
                    String c = (String) listNote.get(z);
                    //Testing only, not needed//Log.i("WJH", "sending note." + c);


                    Intent myIntent = new Intent(getApplicationContext(), writenote.class);
                    myIntent.putExtra("listNote", c);
                    startActivityForResult(myIntent, 1);

                }

            }
        });

        theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {

                askDelete = true;

                toDelete = arg2;

                dialogBuild();

                return false;

            }
        });

    }

        public void dialogBuild () {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to delete this message?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            listNote.remove(toDelete);

                            addaptedAray.notifyDataSetChanged();

                            saveMessages();

                            askDelete = false;
                            // Testing only //Log.i("WJH", "Chose yes.");// Testing only //
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Testing only //Log.i("WJH", "Chose no.");// Testing only //

                            askDelete = false;

                        }
                    })
                    .show();

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

               String alistNote = data.getStringExtra("alistNote");

                if (alistNote != null) {

                    listNote.add(alistNote);

                    addaptedAray.notifyDataSetChanged();

                    saveMessages();

                } else {

                    Log.i("WJH", "Note was null.");

                }

            }

            if (resultCode == Activity.RESULT_CANCELED) {

                Log.i("WJH", "There was no result.");

            }
        }
    }//onActivityResult



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {


            // Of course, we need to tell people about our open source app, the github link for source, etc.

            // Call an intent to go to the second screen when you click the about button.
            // First you define it.
            Intent myintent = new Intent(MainActivity.this, about.class);
            // Now you call it.
            startActivity(myintent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveMessages () {

        // First we clear the stringset. Don't worry, the listNote is holding our notes right now.
        stringSet.clear();
        // Then we clear the old saved notes.
        myPrefs.edit ().putStringSet("ssnotes", stringSet).apply();

        // Now that it's all cleared, we add the list notes to the string set.
        stringSet.addAll(listNote);

        // Just a note for the logs that it worked.
        //Testing only, not needed//Log.i("WJH", "Stringset saved.");

        // Now that we have the stringset of notes, we save them to our preferences.
        myPrefs.edit ().putStringSet("ssnotes", stringSet).apply();

        // Just a note for the logs that it worked.
        //Testing only, not needed//Log.i("WJH", "myprefs saved.");

    }
}
