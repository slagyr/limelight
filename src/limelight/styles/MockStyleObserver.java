//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleValue;

import java.util.LinkedList;

class MockStyleObserver implements StyleObserver
{
  public StyleAttribute changedStyle;
  public LinkedList<StyleAttribute> attributeChanges = new LinkedList<StyleAttribute>();
  public LinkedList<StyleValue> valueChanges = new LinkedList<StyleValue>();
  private boolean[] changes;

  public MockStyleObserver()
  {
    changes = new boolean[Style.STYLE_COUNT];
  }

  public void styleChanged(StyleAttribute attribute, StyleValue value)
  {
    changes[attribute.index] = true;
    changedStyle = attribute;
    attributeChanges.add(attribute);
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

  public boolean changed(StyleAttribute attribute)
  {
    return changes[attribute.index];
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
