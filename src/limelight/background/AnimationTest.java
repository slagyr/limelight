//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import junit.framework.TestCase;
import limelight.ui.MockPanel;
import limelight.background.MockAnimation;
import limelight.Context;

public class AnimationTest extends TestCase
{
  public static final int oneSecond = 1000000000;

  private MockAnimation animation;
  private AnimationLoop loop;

  public void setUp() throws Exception
  {
    animation = new MockAnimation(100, new MockPanel());
    loop = new AnimationLoop();
    Context.instance().animationLoop = loop;
  }

  public void testGetOptimalSleepNanos() throws Exception
  {
    assertEquals(oneSecond / 100, animation.getDelayNanos());
  }

  public void testIsReady() throws Exception
  {
    assertEquals(true, animation.isReady());

    animation.update();
    assertEquals(false, animation.isReady());

    Thread.sleep(20);
    assertEquals(true, animation.isReady());

    animation.update();
    assertEquals(false, animation.isReady());
  }

  public void testLastExecutionTime() throws Exception
  {
    animation.update();

    assertEquals("too long: " + animation.nanosSinceLastUpdate(), true, animation.nanosSinceLastUpdate() < 100000000);
  }

  public void testCallsDoPerform() throws Exception
  {
    animation.update();

    assertEquals(1, animation.updates);
  }

  public void testMissedPerformancesAreMadeup() throws Exception
  {
    animation.update();

    Thread.sleep(25);
    animation.update();
    
    assertEquals(true, animation.updates >= 3);
  }

  public void testMaximumMakeupPerformancesIs5() throws Exception
  {
    animation.update();

    Thread.sleep(100);
    animation.update();

    assertEquals(7, animation.updates);
  }

  public void testStartAndStop() throws Exception
  {
    animation.start();

    assertEquals(1, loop.getAnimations().size());
    assertSame(animation, loop.getAnimations().get(0));

    animation.stop();
    assertEquals(0, loop.getAnimations().size());
  }

  public void testTolerance() throws Exception
  {
    animation = new MockAnimation(100, new MockPanel());

    long delay = animation.getDelayNanos();
    long tolerableDelay = (long)( delay * 0.95);
    assertEquals(tolerableDelay, animation.getTolerableDelay());
  }

  public void testLessThanOneUpdatePerSecond() throws Exception
  {
    animation = new MockAnimation(0.5, new MockPanel());
    animation.update();

    assertEquals(false, animation.isReady());
    animation.getTimer().moveMarkBackInTime(oneSecond);
    assertEquals(false, animation.isReady());
    animation.getTimer().moveMarkBackInTime(oneSecond);
    assertEquals(true, animation.isReady());          
  }
}
