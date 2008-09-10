//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.task;

public class MockTask extends Task
{
  public boolean askedIfReady;
  public boolean performed;
  public Runnable onPerform;
  public int performances;
  public boolean ready = true;

  public boolean isReady()
  {
    askedIfReady = true;
    return ready;
  }

  public void perform()
  {
    performed = true;
    if(onPerform != null)
      onPerform.run();
    performances++;
  }

}
