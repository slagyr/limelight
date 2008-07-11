package limelight.styles;

import java.util.LinkedList;

class MockStyleObserver implements StyleObserver
{
  public StyleDescriptor changedStyle;
  public LinkedList<StyleDescriptor> descriptorChanges = new LinkedList<StyleDescriptor>();
  public LinkedList<String> valueChanges = new LinkedList<String>();

  public void styleChanged(StyleDescriptor descriptor, String value)
  {
    changedStyle = descriptor;
    descriptorChanges.add(descriptor);
    valueChanges.add(value);
  }
}
