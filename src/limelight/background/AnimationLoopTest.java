//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import junit.framework.TestCase;

import limelight.ui.MockPanel;
import limelight.background.MockAnimation;
import limelight.background.IdleThreadLoop;

public class AnimationLoopTest extends TestCase
{
  private AnimationLoop loop;
  private MockAnimation animation20;
  private MockAnimation animation30;
  private MockAnimation animation10;

  public void setUp() throws Exception
  {
    loop = new AnimationLoop();
    animation20 = new MockAnimation(20, new MockPanel());
    animation30 = new MockAnimation(30, new MockPanel());
    animation10 = new MockAnimation(10, new MockPanel());
  }

  public void testShouldBeAnIdleThreadLoop() throws Exception
  {
    assertEquals(true, loop instanceof IdleThreadLoop);
  }

  public void testShouldBeIdleWithoutAnimationTasks() throws Exception
  {
    assertEquals(true, loop.shouldBeIdle());
  }

  public void testShouldNotBeIdleWithAnimationTasks() throws Exception
  {
    loop.add(new MockAnimation(20, new MockPanel()));

    assertEquals(false, loop.shouldBeIdle());
  }

  public void testCalulateOptimalSleepTimeWhenAddingTasks() throws Exception
  {
    assertEquals(-1, loop.getOptimalSleepNanos());

    loop.add(animation20);
    assertEquals(1000000000 / 20, loop.getOptimalSleepNanos());

    loop.add(animation30);
    assertEquals(1000000000 / 30, loop.getOptimalSleepNanos());

    loop.add(animation10);
    assertEquals(1000000000 / 30, loop.getOptimalSleepNanos());
  }

  public void testShouldRecalculateOptimalSleepTimeWhenRemovingTasks() throws Exception
  {
    loop.add(animation20);
    loop.add(animation30);
    loop.add(animation10);

    assertEquals(1000000000 / 30, loop.getOptimalSleepNanos());
    loop.remove(animation30);
    assertEquals(1000000000 / 20, loop.getOptimalSleepNanos());
    loop.remove(animation10);
    assertEquals(1000000000 / 20, loop.getOptimalSleepNanos());
    loop.remove(animation20);
    assertEquals(-1, loop.getOptimalSleepNanos());
  }

  public void testExecute() throws Exception
  {
    loop.add(animation20);
    loop.add(animation30);
    loop.add(animation10);

    loop.updateAnimations();

    assertEquals(1, animation20.updates);
    assertEquals(1, animation10.updates);
    assertEquals(1, animation30.updates);
  }

}
