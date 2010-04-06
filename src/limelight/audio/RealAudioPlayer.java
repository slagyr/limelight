//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.audio;

import limelight.AudioPlayer;
import limelight.Context;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RealAudioPlayer implements AudioPlayer
{
  public void playAuFile(String filename)
  {
    try
    {
      Clip clip = createClip(filename);
      clip.start();
    }
    catch(Exception e)
    {
      //TODO MDM What else to do here?
      e.printStackTrace();
    }
  }

  private Clip createClip(String filename) throws Exception
  {
    AudioInputStream inputStream = createAudioInputStream(filename);

    DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
    Clip clip = (Clip) AudioSystem.getLine(info);
    clip.open(inputStream);
    inputStream.close();

    return clip;
  }

  private AudioInputStream createAudioInputStream(String filename) throws UnsupportedAudioFileException, IOException
  {
    File file = new File(filename);
    AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
    AudioFormat format = inputStream.getFormat();

    if(format.getEncoding() == AudioFormat.Encoding.ULAW || format.getEncoding() == AudioFormat.Encoding.ALAW)
    {
      AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
      inputStream = AudioSystem.getAudioInputStream(newFormat, inputStream);
    }
    return inputStream;
  }
}
