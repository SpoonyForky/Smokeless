package com.example.greyjoy.smokeless;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import static com.example.greyjoy.smokeless.R.id.edtLocation;
import static com.example.greyjoy.smokeless.R.id.mapButton;

public class AddStore extends AppCompatActivity {

    private static final String ADDED_STORE = "ADDEDSTORE";
    private static final String ADDRESS = "ADDRESS";
    private static final int RESULT_CODE = 0;
     // grab those values from the inputs G
    EditText edtName;
    EditText edtLocation;
    RatingBar rtRank;
    EditText edtPhone;
    EditText edtComments;
    EditText edtWebsite;
    Button goMap;
    ///other stuff
    StoreItem creatingStore;
    int RESULT_DELETE = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        edtName = (EditText) findViewById(R.id.edtName);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        // check this
        rtRank = (RatingBar) findViewById(R.id.rtRank);
        //double down on this
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        edtWebsite = (EditText)findViewById(R.id.edtWebsite);
        edtComments = (EditText) findViewById(R.id.edtComments);
        goMap = (Button) findViewById(R.id.mapButton);

        // Lets create a listen for Location
      //anon listener just 2b different
        goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findMap = new Intent(AddStore.this, smokeMap.class);
                if (edtLocation.getText().toString() != null || !(edtLocation.getText().toString() == "")) {
                    findMap.putExtra(ADDRESS,edtLocation.getText().toString());

                }
                    startActivityForResult(findMap, RESULT_CODE);

            }
        } );

        //lets open up that bundle

        Bundle createStoreBundle = getIntent().getExtras();

        if (createStoreBundle != null){
            creatingStore = createStoreBundle.getParcelable(ADDED_STORE);

            Log.d("passed store correctly",creatingStore.getName());
            edtName.setText(creatingStore.getName());
            edtLocation.setText(creatingStore.getLocation());
            edtPhone.setText(creatingStore.getPhone());
            edtComments.setText(creatingStore.getComments());
            edtWebsite.setText(creatingStore.getWebsite());
            rtRank.setRating(creatingStore.getRank());

        }



    }

    public void createStore(View view) {
        String strName = edtName.getText().toString();
        String strLocation = edtLocation.getText().toString();
        String strPhone = edtPhone.getText().toString();
        int intRate = (int) rtRank.getRating();
        String strComments = edtComments.getText().toString();
        String strWebsite = edtWebsite.getText().toString();

        // so they just pressed the button,  we have the store created in case they decided
        //to change name  it will change
        //because the object is created we just need to set the variables and then add it to an array
        creatingStore.setName(strName);
        creatingStore.setLocation(strLocation);
        creatingStore.setPhone(strPhone);
        creatingStore.setRank(intRate);
        creatingStore.setComments(strComments);
        creatingStore.setWebsite(strWebsite);

        Intent createStoreIntent = new Intent();
        createStoreIntent.putExtra(ADDED_STORE,creatingStore);
        setResult(RESULT_OK,createStoreIntent);
        finish();

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void deleteStore(View view) {
        setResult(RESULT_DELETE);
        finish();
    }

    public void GoToWeb(View view) {

        //grab the website edittext
        EditText edtWeb = (EditText) findViewById(R.id.edtWebsite);

        ///uri
        String userWebInput = edtWeb.getText().toString();
        String site;
        //just incase something ugly happens+
        try {
            //Add http:// to beginning of user input
            if (!userWebInput.startsWith("http://")) {
                site = "http://" + userWebInput;
            }
            else {
                //user is a smart cookie
                site = userWebInput;
            }
            Uri webUri = Uri.parse(site);
            //Intentize that shit
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, webUri);

            startActivity(launchBrowser);
        }
        catch(Exception e){
            Toast.makeText(this,"Oops an error occured", Toast.LENGTH_LONG).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK){
        String address = data.getStringExtra(ADDRESS);
        edtLocation.setText(address);

    } else{
        //do nothing
    }

    }

    }
