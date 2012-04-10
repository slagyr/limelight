package limelight.ui.model;

import limelight.ui.Panel;

import java.util.Map;

public abstract class SimpleLayout implements Layout
{
  public void doExpansion(Panel panel)
  {
  }

  public void doContraction(Panel panel)
  {
  }

  public void doFinalization(Panel panel)
  {
  }

  public boolean overides(Layout other)
  {
    return false;
  }
}
