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
