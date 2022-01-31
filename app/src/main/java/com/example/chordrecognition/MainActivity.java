package com.example.chordrecognition;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    ChordRecognition myChordRecognition ;
    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        stop.setEnabled(false);
        play.setEnabled(false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        outputFile = Environment.getExternalStorageDirectory() + "/recording.3gp";


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO },
                    10);
        }else{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }else {
                myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);
            }
        }

        //Record button listener
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        //Stop button listener
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                //myAudioRecorder.release();
                myAudioRecorder = null;
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();

                File file = new File(outputFile);
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                Log.i(TAG,"Audio recorded to file:"+ outputFile);
                Log.i(TAG,"Filesize:"+ file_size);

                myChordRecognition = new ChordRecognition();
                myChordRecognition.setRecordedAudio(outputFile,getApplicationContext());
                myChordRecognition.getChord();
            }
        });

        //Play button listener
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG,"Error playing file");
                }
            }
        });
    }

    @Override public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                                     int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);
            }else{
                //User denied Permission.
            }
        }
    }
}