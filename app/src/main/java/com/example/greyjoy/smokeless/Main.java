package com.example.greyjoy.smokeless;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main extends AppCompatActivity {
    public static final String ADDED_STORE = "ADDEDSTORE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MediaPlayer startup = MediaPlayer.create(this,R.raw.startup_smokeless);
        final MediaPlayer nextClickSound = MediaPlayer.create(this,R.raw.nextclicksound);

        startup.start();
    }

    public void gotoJuice(View view) {
        final MediaPlayer nextClickSound = MediaPlayer.create(this,R.raw.nextclicksound);

        nextClickSound.start();
        CharSequence text = "Coming Soon!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
       // startActivity(new Intent(this,Juice.class));
    }

    public void gotoStore(View view) {
        final MediaPlayer nextClickSound = MediaPlayer.create(this,R.raw.nextclicksound);

       nextClickSound.start();
       startActivity(new Intent(this,Store.class));

    }
}
