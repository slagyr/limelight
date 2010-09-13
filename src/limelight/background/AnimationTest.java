//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import limelight.ui.MockPanel;
import limelight.Context;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class AnimationTest
{
  public static final int oneSecond = 1000000000;

  private MockAnimation animation;
  private AnimationLoop loop;

  @Before
  public void setUp() throws Exception
  {
    animation = new MockAnimation(100, new MockPanel());
    loop = new AnimationLoop();
    Context.instance().animationLoop = loop;
    IdleThreadLoop.verbose = false;
  }

  @Test
  public void getOptimalSleepNanos() throws Exception
  {
    assertEquals(oneSecond / 100, animation.getDelayNanos());
  }

  @Test
  public void isReady() throws Exception
  {
    assertEquals(true, animation.isReady());

    animation.update();
    assertEquals(false, animation.isReady());

    Thread.sleep(20);
    assertEquals(true, animation.isReady());

    animation.update();
    assertEquals(false, animation.isReady());
  }

  @Test
  public void lastExecutionTime() throws Exception
  {
    animation.update();

    assertEquals("too long: " + animation.nanosSinceLastUpdate(), true, animation.nanosSinceLastUpdate() < 100000000);
  }

  @Test
  public void callsDoPerform() throws Exception
  {
    animation.update();

    assertEquals(1, animation.updates);
  }

  @Test
  public void missedPerformancesAreMadeup() throws Exception
  {
    animation.update();

    Thread.sleep(25);
    animation.update();
    
    assertEquals(true, animation.updates >= 3);
  }

  @Test
  public void maximumMakeupPerformancesIs5() throws Exception
  {
    animation.update();

    Thread.sleep(100);
    animation.update();

    assertEquals(7, animation.updates);
  }

  @Test
  public void startAndStop() throws Exception
  {
    animation.start();

    assertEquals(1, loop.getAnimations().size());
    assertSame(animation, loop.getAnimations().get(0));

    animation.stop();
    assertEquals(0, loop.getAnimations().size());
  }
       
  @Test
  public void tolerance() throws Exception
  {
    animation = new MockAnimation(100, new MockPanel());

    long delay = animation.getDelayNanos();
    long tolerableDelay = (long)( delay * 0.95);
    assertEquals(tolerableDelay, animation.getTolerableDelay());
  }

  @Test
  public void lessThanOneUpdatePerSecond() throws Exception
  {
    animation = new MockAnimation(0.5, new MockPanel());
    animation.update();

    assertEquals(false, animation.isReady());
    animation.getTimer().moveMarkBackInTime(oneSecond);
    assertEquals(false, animation.isReady());
    animation.getTimer().moveMarkBackInTime(oneSecond);
    assertEquals(true, animation.isReady());          
  }

  @Test
  public void settingUpdatesPerSecondToZero() throws Exception
  {
    animation.setUpdatesPerSecond(0);
    assertEquals(0, animation.getUpdatesPerSecond(), 0.0001);
    assertEquals(0, loop.getAnimations().size());
    
    animation.start();
    assertEquals(false, animation.isRunning());
    assertEquals(0, loop.getAnimations().size());
  }
  
  @Test
  public void settingUpdatesPerSecondToNegativeValue() throws Exception
  {
    animation.setUpdatesPerSecond(-1);
    assertEquals(0, animation.getUpdatesPerSecond(), 0.0001);
    assertEquals(0, loop.getAnimations().size());
  }

  @Test
  public void stopsItselfWhenAndExceptionIsThrownDuringUpdate() throws Exception
  {
    Animation animation = new Animation(60){
      @Override
      protected void doUpdate()
      {
        throw new RuntimeException("blah");
      }
    }; 
    animation.start();
    loop.start();
    Thread.sleep(50);
    loop.stop();
    assertEquals(false, animation.isRunning());
    assertEquals(0, loop.getAnimations().size());
  }
}
