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

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + ": panel=" + getPanel(); 
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

  public Event withPanel(Panel panel)
  {
    this.panel = panel;
    return this;
  }
}
