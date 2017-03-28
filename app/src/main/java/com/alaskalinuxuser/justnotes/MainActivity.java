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

    // Declare my variables.
    ListView theList;
    ArrayList<String> listNote;
    ArrayAdapter<String> addaptedAray;
    Boolean askDelete, newNote;
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

        // Define ask delete as false, so I can click on things.
        askDelete = false;

        // Set up my string set as new and make sure it is clear.
        stringSet = new HashSet<String>();
        stringSet.clear();

        // Set up my shared preferences, where I am hiding the notes.
        myPrefs = this.getSharedPreferences("com.alaskalinuxuser.justnotes", Context.MODE_PRIVATE);

        // Wrap this in try in case it fails.
        try {

            // Since the app just opened, we are checking if there are some notes from previous use.
            // Let's try to add them to our current notes.
            stringSet.addAll(myPrefs.getStringSet("ssnotes", null));
            //Testing only, not needed//Log.i("WJH", "Got saved notes.");

        } catch (Exception e) {

            // Oops, there is no notes, or they are malformed.
            Log.i("WJH", "Malformed saved notes.");

        }

        // So, if the notes got imported, are there any notes?
        if (stringSet.size() > 0) {

            // If so, let's put them in our table so the user can use them.
            listNote.addAll(stringSet);
            //Testing only, not needed//Log.i("WJH", "Added saved notes.");

        } else {

            // Guess not, no notes here....
            Log.i("WJH", "No saved notes.");

        }

        // The FAB is the clickable button at the bottom of the screen. In this case, the add note button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Okay, since it is a new note, let's make it blank.
                String c = "";
                // And define the boolean as true for a new note. This is for later, when we return from it.
                newNote = true;

                // Now make our intent to go to the "writenote" activity/class to actually write the note.
                Intent myIntent = new Intent(getApplicationContext(), writenote.class);
                // And put the extra information, our blank string in it.
                myIntent.putExtra("listNote", c);
                // And start that intent, but ask for a result.
                startActivityForResult(myIntent, 1);

            }
        });


        // So, since we are still in "on create" for opening the app, we can check to see if we have
        // any previous notes.
        if (listNote.size() <= 1) {

            // If we don't have any notes, then we can add these instructions to the array list.
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

                // Are we busy deleting something in the longclick? This prevents us accidently long clicking
                // to delete something, then accidently clicking on something else.
                if (askDelete) {

                    // We are deleting something, so we can't click here.
                    Log.i("WJH", "Can't click, because we are deleting something.");

                    // And if we are not deleting something, then:
                } else {

                    // Converting the integer of "z" to a string with the name of the item clicked in the list.
                    String c = (String) listNote.get(z);
                    //Testing only, not needed//Log.i("WJH", "sending note." + c);
                    // And set our integer to delete to that z. This is so we can delete any duplicate note.
                    toDelete = z;
                    // And change our boolean to false for a new note, since it is an old note.
                    newNote = false;

                    // And call our intent.
                    Intent myIntent = new Intent(getApplicationContext(), writenote.class);
                    // Add our old note as a string.
                    myIntent.putExtra("listNote", c);
                    // and start that activity expecting a result.
                    startActivityForResult(myIntent, 1);

                }

            }
        });

        // Now for a long click to delete a note.
        theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {

                // Mark our boolean as true that we are deleting something.
                askDelete = true;

                // Turn our arg2 into our toDelete variable for outside of this method.
                toDelete = arg2;

                // Call the method dialog build to ask if we really want to delet that.
                dialogBuild();

                return false;

            }
        });

    }

        // Okay, so here we build the popup to ask if we really want to delete that.
        public void dialogBuild () {

            // Here is what the popup will do.
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to delete this message?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // since we said yes, delete that note by removing it from our table.
                            listNote.remove(toDelete);

                            // Update the view to the user.
                            addaptedAray.notifyDataSetChanged();

                            // Now let's make sure our notes are saved in the event they close the app.
                            saveMessages();

                            // And set our boolean back to false as we are no longer deleting anything.
                            askDelete = false;
                            // Testing only //Log.i("WJH", "Chose yes.");// Testing only //
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Testing only //Log.i("WJH", "Chose no.");// Testing only //

                            // They said no, don't delete it, so just set our boolean back to false.
                            askDelete = false;

                        }
                    })
                    .show(); // Make sure you show your popup or it wont work very well!

        }

    @Override // So, what to do when we get the result back from saving our new or edited note.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If it was code 1 (from our intent).
        if (requestCode == 1) { // And if it was OK, not a fail.
            if(resultCode == Activity.RESULT_OK){

                // Then, go ahead and grab the note they just edited or wrote.
               String alistNote = data.getStringExtra("alistNote");

                // If it is not blank, then:
                if (alistNote != null) {

                    // If it is an edited, old note, then delete the duplicate (squashed that bug).
                    if (newNote == false) {

                        // Remember how we called that variable just before the intent, here we use it
                        // to mark which old duplicate note to delete.
                        listNote.remove(toDelete);

                    }

                    // So, add the note.
                    listNote.add(alistNote);

                    // Update the view to the user.
                    addaptedAray.notifyDataSetChanged();

                    // Save the messages so they can close the app and keep the messages.
                    saveMessages();

                } else {

                    // Hey, the note was blank. Don't do anything.
                    Log.i("WJH", "Note was null.");

                }

            }

            // If the result wan not okay...
            if (resultCode == Activity.RESULT_CANCELED) {

                // Just log that it didn't return a result.
                Log.i("WJH", "There was no result.");

            }
        }
    }//onActivityResult



    @Override // Load the menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override // Call the "About" activity.
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

    // How to save our notes and messages for later use by the user. E.G., when they close the app
    // and open the app, the notes are persistent.
    public void saveMessages () {

        // First we clear the stringset. Don't worry, the listNote is holding all our notes right now.
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
