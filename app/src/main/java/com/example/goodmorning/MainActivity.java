package com.example.goodmorning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
ImageView image;
Animation anim, frombottom;
ListView mylistview;
String[] items;
TextView text,opti;
public int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frombottom=AnimationUtils.loadAnimation(this,R.anim.frombottom);
        opti=(TextView)findViewById(R.id.opt);
        image=(ImageView)findViewById(R.id.imageview);
        text=(TextView)findViewById((R.id.good));
        mylistview=(ListView)findViewById(R.id.list);

        anim= AnimationUtils.loadAnimation(this,R.anim.anim);
        image.animate().translationY(-1600).setDuration(1000).setStartDelay(2000);
        text.animate().translationY(-1400).setDuration(1000).setStartDelay(2000);
        //opti.animate().translationY(100).translationX(30).setDuration(1000).setStartDelay(2500);
        opti.startAnimation(frombottom);
        mylistview.startAnimation(frombottom);

     /*   MediaPlayer mediaPlayer=MediaPlayer.create(MainActivity.this, R.raw.song);
        if(!mediaPlayer.isPlaying() )
        {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }else
        {
            mediaPlayer.pause();
            mediaPlayer.release();
        }*/
        mylistview=(ListView)findViewById(R.id.list);
        runtimepermission();

       /* Vibrator V=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.0){
            V.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            V.vibrate(1000);
            < uses-permission android:name="android.permission.VIBRATE" />
        }*/
    }
    public void runtimepermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();
    }
    public ArrayList<File> findsong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for (File singlefile:  files){
            if(singlefile.isDirectory()&& !singlefile.isHidden()){
                arrayList.addAll(findsong(singlefile));
            }
            else
            {
                if (singlefile.getName().endsWith(".mp3")||
                    singlefile.getName().endsWith(".wav")){
                        arrayList.add(singlefile);
                    }

            }

        }
        return arrayList;
    }
    void display(){
        final ArrayList<File> mysong =findsong(Environment.getExternalStorageDirectory());
            items= new String[mysong.size()];
            for (int i=0;i<mysong.size();i++){

                items[i]=mysong.get(i).getName().toString().replace(".mp3","").replace(".wav","");
            }
           ArrayAdapter<String> myadapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items)/*{
                   @Override
                    public View getView(int position, View convertview, ViewGroup parent){
                       View view= super.getView(position,convertview,parent);
                       TextView tv=(TextView)view.findViewById(android.R.id.text1);
                       tv.setTextColor(android.R.color.black);
                       return view;
                   }
            }*/;
            mylistview.setAdapter(myadapter);


            mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                    String songName= mylistview.getItemAtPosition(i).toString();

                    startActivity(new Intent(getApplicationContext(),play.class)
                    .putExtra("songs",mysong).putExtra("songname",songName).putExtra("pos",i));
                }
            });

    }

}
