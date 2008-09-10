//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;

public abstract class Update
{
  protected int severity;

  protected Update(int severity)
  {
    this.severity = severity;
  }

  public int getSeverity()
  {
    return severity;
  }

  public abstract void performUpdate(Panel panel);

  public boolean isMoreSevereThan(Update other)
  {
    return this.getSeverity() > other.getSeverity();
  }

  public Update prioritize(Update other)
  {
    return this.isMoreSevereThan(other) ? this : other;
  }
}
