package limelight.ui.events;

import limelight.ui.Panel;

public abstract class Event
{
  private Panel panel;
  private boolean consumed;

  public Event(Panel source)
  {
    this.panel = source;
  }

  public Panel getPanel()
  {
    return panel;
  }

  public boolean isConsumed()
  {
    return consumed;
  }

  public void consume()
  {
    consumed = true;
  }

  public boolean isInheritable()
  {
    return false;
  }
}
