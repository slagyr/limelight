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

  public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
  {
    changedStyle = descriptor;
    descriptorChanges.add(descriptor);
    valueChanges.add(value);
  }
}
