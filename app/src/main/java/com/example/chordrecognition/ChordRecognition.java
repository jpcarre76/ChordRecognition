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
import android.os.Environment;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;
import java.io.File;
import java.io.IOException;

public class ChordRecognition {

  String recordedAudio;

  public void setRecordedAudio(String filename){
      this.recordedAudio = filename;
  }

  public String getChord(){

      String myDirectoryPath = "/storage/emulated/0/ChordRecognition/chords/";
      String recordedClip = recordedAudio;
      Wave waveRec = new Wave(recordedClip);
      float maxSimilarity = -100 ;
      String maxSimilartyFileName = "" ;

      File dir = new File(myDirectoryPath);
      File[] directoryListing = dir.listFiles();
      if (directoryListing != null) {
          for (File child : directoryListing) {
              // Do something with child
              Wave chord = new Wave(myDirectoryPath+child.getName());
              FingerprintSimilarity similarity;
              similarity = chord.getFingerprintSimilarity(waveRec);
              System.out.println("similarity is " + similarity.getSimilarity() );
              if (similarity.getSimilarity() > maxSimilarity) {
                  maxSimilarity = similarity.getSimilarity() ;
                  maxSimilartyFileName = child.getName();
              }
          }
      } else {
          // Handle the case where dir is not really a directory.
          // Checking dir.isDirectory() above would not be sufficient
          // to avoid race conditions with another process that deletes
          // directories.
      }

      System.out.println("Chord is " + maxSimilartyFileName );
      return maxSimilartyFileName;

  }

}