package com.example.greyjoy.smokeless;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Store extends AppCompatActivity{
    private static final String ADDED_STORE = "ADDEDSTORE";
    private static final int ADD_STORE_RESULT = 1;
    private static final String STORE_UPDATE = "UpdateStore";
    private static final int STORE_UPDATE_RESULT = 2;
    //Store DB table
    private static final String STORE_TABLE = "StoreDB";
    private int itemPosition = 0;
    private static final int RESULT_DELETE = 999;
    /* Sounds */
    //Get the button field yo
    Button addStore;
    Button sortStore;
    ListView storeListView;
    //Store stuff
    StoreItem newStore;
    ArrayList<StoreItem> storeItemArray = new ArrayList<>();

    StoreList storeList;
    StoreItem selectedItem;



    //SQLLite

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        addStore = (Button) findViewById(R.id.addStore);
        sortStore = (Button) findViewById(R.id.sortStore);
        storeListView = (ListView) findViewById(R.id.storeList);

        // openFile();
        //create or open db
        db=openOrCreateDatabase("StoreDB",MODE_PRIVATE,null);
      try {
     //     db.execSQL("Drop table " + STORE_TABLE);
      } catch (Exception err){
          Log.d("DropTable",err.toString());
      }
        db.execSQL("create table if not exists "+STORE_TABLE+" (name varchar, location varchar, rank varchar, phone varchar," +
                "comments varchar, website varchar)");
        storeItemArray = getAllShops();
        loadStoreListView();
        db.close();
    }


    public void addStore(View view) {
        final MediaPlayer nextClickSound = MediaPlayer.create(this,R.raw.nextclicksound);

        nextClickSound.start();
        // thought about another view- but lets keep this simple and small
        // lets add up a pop over

        final EditText stName = new EditText(this);
        //  stName = (EditText) findViewById(R.id.edtDBName);

        AlertDialog.Builder sbAlert = new AlertDialog.Builder(this);
        sbAlert.setView(stName);

        // Get a view for the edit text
        sbAlert.setTitle("Adding a store");
        sbAlert.setMessage("Please enter the name");
        sbAlert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newStName = stName.getText().toString();
                newStore = new StoreItem(stName.getText().toString());
                // should probably refresh the listview now and then open up a new intent to the
/*
                Intent addStoreIntent = new Intent(Store.this,AddStore.class);
                addStoreIntent.putExtra(ADDED_STORE, newStore);
                startActivityForResult(addStoreIntent,ADD_STORE_RESULT);
                */
                startIntent(newStore, ADD_STORE_RESULT);
            }

        });
        sbAlert.setNegativeButton("Oops", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing
            }
        });

        AlertDialog alertAdd = sbAlert.create();
        alertAdd.show();


    }

    //////RESULTS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final MediaPlayer backClickSound = MediaPlayer.create(this,R.raw.backclicksound);

        backClickSound.start();

        if (resultCode == RESULT_DELETE){
            //remove from array
            storeItemArray.remove(selectedItem);
            //reload view
            loadStoreListView();
            //remove from sql
            removeFromDB(selectedItem);
        }else if(requestCode == ADD_STORE_RESULT){
            if(resultCode == RESULT_OK){
                //we got it baaaack!

                newStore = data.getParcelableExtra(ADDED_STORE);
                storeItemArray.add(newStore);
                addToDB(newStore);
                loadStoreListView();
                //alert dialog store added
            }
            // if store updated similar
        } else if(requestCode == STORE_UPDATE_RESULT){
            if (resultCode == RESULT_OK) {
                selectedItem = data.getParcelableExtra(ADDED_STORE);
                storeItemArray.set(itemPosition, selectedItem);

                //Log.d("toStringTest"," "+newStore.toString());
                loadStoreListView();
            }

        } else if(requestCode == RESULT_CANCELED){
            //do nothing
        }

        //lets save to file after
        //storeToFile(storeItemArray);
    }

    public void sortStore(View view) {
        //not 100% sure on how i want to do this yet - keep thinking about it
        //sort by name obvi and location
    }


    public void loadStoreListView() {

        StoreItem[] storeItemsArray = new StoreItem[storeItemArray.size()];

        for (int i = 0; i < storeItemsArray.length; i++) {
            storeItemsArray[i] = storeItemArray.get(i);
        }
        ArrayList<StoreItem> storesLVA = StoreItem.getStores(this, storeItemArray);
        MyArrayAdapter adapter = new MyArrayAdapter(this, R.layout.listview_store, storesLVA);
        storeListView.setAdapter(adapter);
        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // things are listed now lets make them editable
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  final MediaPlayer selectSound = MediaPlayer.create(this,R.raw.smokeless_select);
             //   selectSound.start();
                selectedItem = (StoreItem) storeListView.getAdapter().getItem(position); // this doesn't crash it so we must be ok
                //Don't lose that position
                itemPosition = position;
                startIntent(selectedItem,STORE_UPDATE_RESULT);
            }
        });

    }

    public void startIntent (StoreItem store,int resultcall) {
        Intent addStoreIntent = new Intent(Store.this,AddStore.class);
        addStoreIntent.putExtra(ADDED_STORE, store);
        final MediaPlayer selectSound = MediaPlayer.create(this,R.raw.smokeless_select);
        selectSound.start();
        startActivityForResult(addStoreIntent,resultcall);
    }


    public void addToDB(StoreItem store) {

        String name = store.getName();
        String location = store.getLocation();
        String rank = String.valueOf(store.getRank());
        String phone = String.valueOf(store.getPhone());
        String comments = store.getComments();
        String website = store.getWebsite();
        if(!db.isOpen()){
            db=openOrCreateDatabase("StoreDB",MODE_PRIVATE,null);
        }
        db.execSQL("insert into StoreDB values('" + name + "','" + location + "','" + rank + "','" + phone + "','" + comments + "','" + website + "')");
        db.close();
    }
    ////////////// List all shops
    // Getting All Shops
    public ArrayList<StoreItem> getAllShops() {
        ArrayList<StoreItem> shopList = new ArrayList<StoreItem>();
// Select All Query
        String selectQuery = "SELECT * FROM " + STORE_TABLE;


        Cursor cursor = db.rawQuery(selectQuery, null);


// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StoreItem store = new StoreItem();
                store.setName(cursor.getString(0));
                store.setLocation(cursor.getString(1));
                store.setRank(Integer.parseInt(cursor.getString(2)));
                store.setPhone(cursor.getString(3));
                store.setComments(cursor.getString(4));
                store.setWebsite(cursor.getString(5));

// Adding contact to list
                shopList.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
// return contact list
        Log.wtf("Inside getshops", "returned all shops");
        return shopList;
    }

    /////////////////SQL DB
    public void removeFromDB(StoreItem store){
       if(!db.isOpen()){
           db=openOrCreateDatabase("StoreDB",MODE_PRIVATE,null);
       }
        db.execSQL("Delete from "+ STORE_TABLE +" where name = '" + store.getName()+"'");
    }
}

