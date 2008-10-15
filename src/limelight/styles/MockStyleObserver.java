//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleAttribute;

import java.util.LinkedList;

class MockStyleObserver implements StyleObserver
{
  public StyleDescriptor changedStyle;
  public LinkedList<StyleDescriptor> descriptorChanges = new LinkedList<StyleDescriptor>();
  public LinkedList<StyleAttribute> valueChanges = new LinkedList<StyleAttribute>();
  private boolean changed;
  private boolean[] changes;

  public MockStyleObserver()
  {
    changes = new boolean[Style.STYLE_COUNT];
  }

  public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
  {
    changes[descriptor.index] = true;
    changedStyle = descriptor;
    descriptorChanges.add(descriptor);
    valueChanges.add(value);
  }

  public boolean changed()
  {
    for(boolean change : changes)
    {
      if(change)
        return true;
    }
    return false;
  }

  public boolean changed(StyleDescriptor descriptor)
  {
    return changes[descriptor.index];
  }

  public void flushChanges()
  {
    for (int i = 0; i < changes.length; i++)
      changes[i] = false;
  }

  public int getChangedCount()
  {
    int count = 0;
    for(boolean change : changes)
      if(change)
        count++;
    return count;
  }
}
