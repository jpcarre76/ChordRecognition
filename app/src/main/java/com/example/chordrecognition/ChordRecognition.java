/*
 * Copyright (C) 2012 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.chordrecognition;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChordRecognition {

  String recordedAudio;
  Context context ;

  public void setRecordedAudio(String filename, Context sContext){
      this.recordedAudio = filename;
      this.context = sContext;
  }

  public String getChord() {

      String myDirectoryPath = "chords";
      String recordedClip = recordedAudio;
      Wave waveRec = new Wave(recordedClip);
      float maxSimilarity = -100;
      String maxSimilartyFileName = "";

      String[] list;
      try {
          list = context.getAssets().list(myDirectoryPath);
          for (String child : list) {
              // Do something with child
              InputStream oFile = context.getAssets().open(myDirectoryPath+"/"+child);
              Wave chord = new Wave(oFile);
              FingerprintSimilarity similarity;
              similarity = chord.getFingerprintSimilarity(waveRec);
              System.out.println("similarity is " + similarity.getSimilarity());
              if (similarity.getSimilarity() > maxSimilarity) {
                  maxSimilarity = similarity.getSimilarity();
                  maxSimilartyFileName = child;
              }
          }

          System.out.println("Chord is " + maxSimilartyFileName);


      } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
      }

      return maxSimilartyFileName;

  }
}