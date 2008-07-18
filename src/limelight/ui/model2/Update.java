package limelight.ui.model2;

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
}
