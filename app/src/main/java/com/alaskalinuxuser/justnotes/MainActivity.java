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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Declare my variables.
    ListView theList;
    ArrayList<String> listNote, titleNote;
    ArrayAdapter<String> addaptedAray;
    Boolean askDelete, newNote;
    int toDelete;
    Set<String> stringSet;
    static SharedPreferences myPrefs;
    String exportNotes, tempNotes, currentDateandTime, alistNote, subString, tempString;
    static int textColorChoice, colorChoice, titleChoice, fabColorChoice;
    FloatingActionButton fab;
    Layout mainLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up my shared preferences, where I am hiding the notes.
        myPrefs = this.getSharedPreferences("com.alaskalinuxuser.justnotes", Context.MODE_PRIVATE);

        try {

            // Let's import our preference for colors and texts...
            colorChoice = Integer.parseInt(myPrefs.getString("colorPref", null));
            textColorChoice = Integer.parseInt(myPrefs.getString("textColorPref", null));
            fabColorChoice = Integer.parseInt(myPrefs.getString("fabColorPref", null));
            titleChoice = Integer.parseInt(myPrefs.getString("titlePref", null));

            Log.i("WJH", String.valueOf(colorChoice));
            Log.i("WJH", String.valueOf(textColorChoice));
            Log.i("WJH", String.valueOf(fabColorChoice));
            Log.i("WJH", String.valueOf(titleChoice));

        } catch (Exception a) {

            // What to log if it fails.
            Log.i("WJH", "No pref." + a);

        }

        // Defining the list view that I want by id.
        theList = (ListView) findViewById(R.id.theList);

        // Defining the array of notes.
        listNote = new ArrayList();
        titleNote = new ArrayList<>();

        // Defining an adapter, to adapt my array list to the correct format.
        addaptedAray = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleNote) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                switch (textColorChoice) {

                    case 0:
                       textView.setTextColor(Color.GRAY);
                        break;

                    case 1:
                        textView.setTextColor(Color.RED);
                        break;

                    case 2:
                        textView.setTextColor(Color.GREEN);
                        break;

                    case 3:
                        textView.setTextColor(Color.BLUE);
                        break;

                    case 4:
                        textView.setTextColor(Color.BLACK);
                        break;

                    case 5:
                        textView.setTextColor(Color.YELLOW);
                        break;

                    case 6:
                        textView.setTextColor(Color.MAGENTA);
                        break;

                }

                return view;

            }
        };


        // Using the adapter to adapt my array list to the defined list view that I declared already.
        theList.setAdapter(addaptedAray);

        // Define ask delete as false, so I can click on things.
        askDelete = false;

        // Set up my string and title set as new and make sure it is clear.
        stringSet = new HashSet<String>();
        stringSet.clear();

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

            // Testing only // Log.i("WJH", stringSet.toString());

            // If so, let's put them in our table so the user can use them.
            listNote.addAll(stringSet);
            //Testing only, not needed//Log.i("WJH", "Added saved notes.");

            setTitleNote();

        } else {

            // Guess not, no notes here....
            Log.i("WJH", "No saved notes.");

        }

        // The FAB is the clickable button at the bottom of the screen. In this case, the add note button.
        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
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

        // Okay, now let's set our colors.
        selectColors();

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

                            // And we should update our note titles.
                            setTitleNote();

                            // Update the view to the user.
                            //addaptedAray.notifyDataSetChanged();

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
                alistNote = data.getStringExtra("alistNote");

                makeNote();

            }

            // If the result wan not okay...
            if (resultCode == Activity.RESULT_CANCELED) {

                // Just log that it didn't return a result.
                Log.i("WJH", "There was no  one result.");

            }

        } else if (requestCode == 2) { // From settings, and if it was OK, not a fail.
            if(resultCode == Activity.RESULT_OK){


                colorChoice = Integer.parseInt(data.getStringExtra("colorChoice"));
                textColorChoice = Integer.parseInt(data.getStringExtra("textColorChoice"));
                fabColorChoice = Integer.parseInt(data.getStringExtra("fabColorChoice"));
                titleChoice = Integer.parseInt(data.getStringExtra("titleChoice"));

                Log.i("WJH", String.valueOf(colorChoice));
                Log.i("WJH", String.valueOf(textColorChoice));
                Log.i("WJH", String.valueOf(fabColorChoice));
                Log.i("WJH", String.valueOf(titleChoice));

                //And let's save our new preferences.
                myPrefs.edit ().putString("colorPref", String.valueOf(colorChoice)).apply();
                myPrefs.edit ().putString("textColorPref", String.valueOf(textColorChoice)).apply();
                myPrefs.edit ().putString("fabColorPref", String.valueOf(fabColorChoice)).apply();
                myPrefs.edit ().putString("titlePref", String.valueOf(titleChoice)).apply();

                selectColors();

            }

            // If the result wan not okay...
            if (resultCode == Activity.RESULT_CANCELED) {

                // Just log that it didn't return a result.
                Log.i("WJH", "There was no two result.");

            }
        }
    }//onActivityResult



    @Override // Load the menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void makeNote () {

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

            // And we should update our note titles again.
            setTitleNote();
            // Update the view to the user.
            // addaptedAray.notifyDataSetChanged();

            // Save the messages so they can close the app and keep the messages.
            saveMessages();

        } else {

            // Hey, the note was blank. Don't do anything.
            Log.i("WJH", "Note was null.");

        }

    }

    @Override // Call the "About" activity.
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            // Call an intent to go to the settings screen.
            // First you define it.
            Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
            // Now you call it.
            startActivityForResult(settingIntent, 2);

            return true;
        }

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

        // Import our notes.
        if (id == R.id.action_import) {

            // Okay, so we should back up the current notes.
            writeToFile();/*
            * The benefit to backing up first, is that we can do three things:
            * #1. Ensure that they have read/write permissions, as the popups will tell them if
            * they have an error about that while trying to back up.
            * #2. Ensure the justnotes directory exists so we can tell them to put their import
            * notes there.
            * #3. We can actually back up what they have now, before we add to it.
            */

            // Of course, we need to tell people where to put their files for import.
            // Here is what the popup will do.
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Are you ready?")
                    .setMessage(
                            "Please put your justnotestxt files are in the justnotes folder, and rename the file 'import.justnotestxt' so we can import it.")
                    .setPositiveButton("Yes, I have done that.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Testing only // Log.i("WJH", "Clicked yes.");

                                // Try this, in case it fails.
                                try {

                                    // Load the file location.
                                    File file = new File(Environment.getExternalStorageDirectory()+File.separator+"justnotes"+File.separator+"import.justnotestxt");

                                    // Get the length of the file.
                                    int length = (int) file.length();

                                    // Set your bytes to length.
                                    byte[] bytes = new byte[length];

                                    // Open an input stream.
                                    FileInputStream in = new FileInputStream(file);

                                    // And try to read it, when done, close it.
                                    try {
                                        in.read(bytes);
                                    } finally {
                                        in.close();
                                    }

                                    // Set those bytes to a string.
                                    String contents = new String(bytes);

                                    // Now we create an array and split it by the magic symbols we input when we exported it.
                                    String[] splitString = contents.split("justnotestxt");

                                    for (int j=0; j < splitString.length;j++) {

                                        // Testing only.// Log.i("WJH", splitString[j]);

                                        alistNote = splitString[j];
                                        newNote = true;

                                        makeNote();

                                    }


                                } catch (Exception e) {
                                    Log.i("WJH", "Can not read file: " + e.toString());
                                }

                                //what to do.


                        }
                    })
                    .setNegativeButton("No, I need to do that now.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.i("WJH", "Clicked no.");

                        }
                    })
                    .show(); // Make sure you show your popup or it wont work very well!

        }

        // If we select to export...
        if (id == R.id.action_export) {

            // Call to write to the file.
            writeToFile();


        }

        if (id == R.id.action_help) {

            Toast.makeText(getApplicationContext(),
                    "Click the + to add a note. Long press a note to delete it.",
                    Toast.LENGTH_LONG).show();

        }

        return super.onOptionsItemSelected(item);
    }

    // To write the file.
    public  void writeToFile() {

        // First, let's get the date and time by setting the format and then pulling it.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        currentDateandTime = sdf.format(new Date());

        // Log it for a check.
        // For testing only, not needed // Log.i("WJH", "Clicked export. " + currentDateandTime);

        // Set our tempNote string to be the current date and time plus our homemade seperator.
        tempNotes = currentDateandTime + " justnotestxt ";

        // And set our export notes to the temp notes for our below loop.
        exportNotes = tempNotes;

        // We should try this, in case they try to export when there are no notes.
        try {

            // Start a for loop, based on the size of the notes.
            for (int k = 0; k <= listNote.size(); k++) {

                // Make our export notes equal the temp notes + the next note + our seperator.
                exportNotes = tempNotes + listNote.get(k) + " justnotestxt ";

                // And set temp notes to export notes for our loop to continue.
                tempNotes = exportNotes;

            }

        } catch (Exception e) {

            Log.i("WJH", "No notes to export.");

        }

        // Log it for a check.
        // Testing only, not needed. // Log.i("WJH", exportNotes);

        FileOutputStream fos = null;

        // Try this in case it fails.
        try {

            File appDir = new File(Environment.getExternalStorageDirectory()+File.separator+"justnotes");

            if(!appDir.exists() && !appDir.isDirectory())
            {
                // create empty directory
                if (appDir.mkdirs())
                {

                    Log.i("WJH","App dir created");

                }
                else
                {

                    Toast.makeText(getApplicationContext(), "You do not have permission to create a directory.",
                            Toast.LENGTH_SHORT).show();

                }
            } else {

                Log.i("WJH","App dir already exists");

            }

                        // Put our file together, we will name it the current date and time.
            final File myFile = new File(appDir, currentDateandTime + ".justnotestxt");

            // If it exists?
            if (!myFile.exists())
            {
                // Make a new one.
                myFile.createNewFile();
            }

            // Start our file output stream.
            fos = new FileOutputStream(myFile);

            // Put our export notes into it.
            fos.write(exportNotes.getBytes());

            // And close the stream.
            fos.close();

            // Catch any exception.
        } catch (Exception eX) {

            eX.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: Please check permissions....",
                    Toast.LENGTH_SHORT).show();

        }
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

    public void setTitleNote () {

        // We will clear our title notes to make sure that they match the list notes.
        titleNote.clear();

        // So, for each item in our list notes we will:
        for (int k = 0; k <= stringSet.size(); k++) {

            try {
                // Make a temporary string of the note.
                tempString = listNote.get(k);

                // Log it for testing.
                //Log.i("WJH", tempString);

                // If the title choice length is 0, then:
                if (titleChoice == 0) {

                    // Set it to 250 instead.
                    titleChoice = 250;

                }

                // See how long it is.
                if (tempString.length() < titleChoice) {

                    // Since it is so short a note, just use the whole thing for the title.
                    subString = tempString;

                } else {

                    // Set the subString to the first characters for a shorter title. Limited by
                    // the user preferences for title choice.
                    subString = tempString.substring(0, titleChoice);

                }

                // Let's log our substring.
                // Testing only // Log.i("WJH", subString);

                // Let's add that substring as our title for each note.
                titleNote.add(subString);

            } catch (Exception e) {

                Log.i("WJH", "Exception " + e);
            }

        }

        // And when we are done, let's notify our adapter of the status change.
        addaptedAray.notifyDataSetChanged();

    }

    // My method to set all the colors.
    public void selectColors () {

        // Set the title bar color.
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

        // Set the fab color.
        switch (fabColorChoice) {

            case 0:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                break;

            case 1:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;

            case 2:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;

            case 3:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                break;

            case 4:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                break;

            case 5:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                break;

            case 6:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                break;

            case 7:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                break;

        }

        // And set the title notes to update it's colors.
        setTitleNote();

    }
}
