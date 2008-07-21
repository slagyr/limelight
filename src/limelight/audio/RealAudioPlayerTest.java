package limelight.audio;

import junit.framework.TestCase;
import limelight.caching.SimpleCache;
import limelight.Context;
import limelight.io.FileUtil;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

public class RealAudioPlayerTest extends TestCase
{
  private RealAudioPlayer player;

  public void setUp() throws Exception
  {
    player = new RealAudioPlayer();
  }

  public void testSomething() throws Exception
  {
    //...
  }

}
