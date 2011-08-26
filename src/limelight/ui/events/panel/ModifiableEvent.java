//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.events.panel;

public class ModifiableEvent extends PanelEvent
{
  public static final int SHIFT_MASK = 1;
  public static final int CONTROL_MASK = 1 << 1;
  public static final int COMMAND_MASK = 1 << 2;
  public static final int ALT_MASK = 1 << 3;

  private int modifiers;

  public ModifiableEvent(int modifiers)
  {
    super();
    this.modifiers = modifiers;
  }

  @Override
  public String toString()
  {
    return super.toString() + " modifiers=" + getModifiers();
  }

  public boolean hasModifier()
  {
    return modifiers != 0;
  }

  public int getModifiers()
  {
    return modifiers;
  }

  public void setModifiers(int modifiers)
  {
    this.modifiers = modifiers;
  }

  public boolean isShiftDown()
  {
    return (modifiers & SHIFT_MASK) != 0;
  }

  public boolean isControlDown()
  {
    return (modifiers & CONTROL_MASK) != 0;
  }

  public boolean isCommandDown()
  {
    return (modifiers & COMMAND_MASK) != 0;
  }

  public boolean isAltDown()
  {
    return (modifiers & ALT_MASK) != 0;
  }
}
