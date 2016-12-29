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
    private static final String STORE_TABLE = "StoreDB";
    private int itemPosition = 0;
    private static final int RESULT_DELETE = 999;
    Button addStore;
    Button sortStore;
    ListView storeListView;
    StoreItem newStore;
    ArrayList<StoreItem> storeItemArray = new ArrayList<>();
    StoreList storeList;
    StoreItem selectedItem;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        addStore = (Button) findViewById(R.id.addStore);
        sortStore = (Button) findViewById(R.id.sortStore);
        storeListView = (ListView) findViewById(R.id.storeList);

        db=openOrCreateDatabase("StoreDB",MODE_PRIVATE,null);
      try {
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

        final EditText stName = new EditText(this);
        AlertDialog.Builder sbAlert = new AlertDialog.Builder(this);
        sbAlert.setView(stName);
        sbAlert.setTitle("Adding a store");
        sbAlert.setMessage("Please enter the name");
        sbAlert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newStName = stName.getText().toString();
                newStore = new StoreItem(stName.getText().toString());
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
        if (resultCode == RESULT_DELETE){
            storeItemArray.remove(selectedItem);
            loadStoreListView();
            removeFromDB(selectedItem);
        }else if(requestCode == ADD_STORE_RESULT){
            if(resultCode == RESULT_OK){
                newStore = data.getParcelableExtra(ADDED_STORE);
                storeItemArray.add(newStore);
                addToDB(newStore);
                loadStoreListView();
            }
        } else if(requestCode == STORE_UPDATE_RESULT){
            if (resultCode == RESULT_OK) {
                selectedItem = data.getParcelableExtra(ADDED_STORE);
                storeItemArray.set(itemPosition, selectedItem);
                loadStoreListView();
            }
        } else if(requestCode == RESULT_CANCELED){
        }
    }

    public void sortStore(View view) {
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
                selectedItem = (StoreItem) storeListView.getAdapter().getItem(position);
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
    public ArrayList<StoreItem> getAllShops() {
        ArrayList<StoreItem> shopList = new ArrayList<StoreItem>();
        String selectQuery = "SELECT * FROM " + STORE_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                StoreItem store = new StoreItem();
                store.setName(cursor.getString(0));
                store.setLocation(cursor.getString(1));
                store.setRank(Integer.parseInt(cursor.getString(2)));
                store.setPhone(cursor.getString(3));
                store.setComments(cursor.getString(4));
                store.setWebsite(cursor.getString(5));
                shopList.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return shopList;
    }
    public void removeFromDB(StoreItem store){
       if(!db.isOpen()){
           db=openOrCreateDatabase("StoreDB",MODE_PRIVATE,null);
       }
        db.execSQL("Delete from "+ STORE_TABLE +" where name = '" + store.getName()+"'");
    }
}

