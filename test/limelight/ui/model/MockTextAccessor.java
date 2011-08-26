//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
