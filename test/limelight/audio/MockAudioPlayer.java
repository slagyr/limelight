//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.audio;

import limelight.AudioPlayer;

public class MockAudioPlayer implements AudioPlayer
{
  public String playedFile;

  public void playAuFile(String filename)
  {
    playedFile = filename;
  }
}
