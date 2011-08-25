//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

public class MockTextAccessor implements TextAccessor
{
  public boolean markAsDirtyCalled;
  public boolean markAsNeedingLayoutCalled;

  public void setText(String text, Prop panel)
  {
  }

  public String getText()
  {
    return null;
  }

  public void markAsDirty()
  {
    markAsDirtyCalled = true;
  }

  public void markAsNeedingLayout()
  {
    markAsNeedingLayoutCalled = true;
  }

  public boolean hasFocus()
  {
    return false;
  }
}
