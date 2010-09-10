package limelight.ui.events;

import limelight.ui.Panel;

public class CharTypedEvent extends ModifiableEvent
{
  private char c;

  public CharTypedEvent(Panel panel, int modifiers, char c)
  {
    super(panel, modifiers);
    this.c = c;
  }

  public char getChar()
  {
    return c;
  }

  @Override
  public String toString()
  {
    return super.toString() + " char=" + (int)c + "/" + c;
  }
}
