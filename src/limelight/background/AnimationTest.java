package limelight.background;

import junit.framework.TestCase;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;
import limelight.ui.MockPanel;
import limelight.background.MockAnimation;
import limelight.Context;

public class AnimationTest extends TestCase
{
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
    assertEquals(1000000000 / 100, animation.getDelayNanos());
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

    assertEquals(3, animation.updates);
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
}
