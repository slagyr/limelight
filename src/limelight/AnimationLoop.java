//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.util.NanoTimer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AnimationLoop extends IdleThreadLoop
{
  private LinkedList<Animation> animations = new LinkedList<Animation>();
  private ArrayList<Animation> buffer = new ArrayList<Animation>(50);
  private long optimalSleepNanos = 1000000000;
  private NanoTimer timer = new NanoTimer();
  private long lastExecutionDuration;

  public boolean shouldBeIdle()
  {
    return animations.isEmpty();
  }

  protected void execute()
  {
    timer.markTime();
    updateAnimations();
    lastExecutionDuration = timer.getIdleNanos();
  }

  protected void delay()
  {
    timer.sleep(optimalSleepNanos - lastExecutionDuration);
  }

  public void add(Animation animation)
  {
    synchronized(animations)
    {
      animations.add(animation);
      calculateOptimalSleepTime();
    }
  }

  public void remove(Animation animation)
  {
    synchronized(animations)
    {
      boolean wasRemoved = animations.remove(animation);
      if(wasRemoved)
        calculateOptimalSleepTime();
    }
  }

  private void calculateOptimalSleepTime()
  {
    long min = 1000000000;
    for(Animation animation : animations)
    {
      if(animation.getDelayNanos() < min)
        min = animation.getDelayNanos();
    }
    optimalSleepNanos = min;
  }

  public long getOptimalSleepNanos()
  {
    return optimalSleepNanos;
  }

  public void updateAnimations()
  {
    buffer.clear();
    synchronized(animations)
    {
      buffer.addAll(animations);
    }
    
    for(Animation animation : buffer)
    {
      if(animation.isReady())
        animation.update();
    }
  }

  public List<Animation> getAnimations()
  {
    return animations;
  }

  public AnimationLoop started()
  {
    start();
    return this;
  }
}
