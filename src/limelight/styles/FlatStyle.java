//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.LimelightException;
import limelight.util.Util;
import limelight.styles.abstrstyling.StyleAttribute;

public class FlatStyle extends Style
{
  private final StyleAttribute[] styles;

  public FlatStyle()
	{
    styles = new StyleAttribute[STYLE_COUNT];
  }

  protected StyleAttribute get(int key)
	{
    StyleAttribute style = styles[key];
    return style == null ? null : style;
	}

	public void put(StyleDescriptor descriptor, Object value)
	{
    if(value == null)
      return;

    StyleAttribute compiledValue = descriptor.compile(value);

    StyleAttribute originalValue = styles[descriptor.index];
    styles[descriptor.index] = compiledValue;
    if(!Util.equal(originalValue, compiledValue))
      recordChange(descriptor, compiledValue);
  }

  @Override
  protected void putCompiled(StyleDescriptor descriptor, StyleAttribute compiledValue)
  {
    StyleAttribute originalValue = styles[descriptor.index];
    if(!Util.equal(originalValue, compiledValue))
    {
      styles[descriptor.index] = compiledValue;
      notifyObserversOfChange(descriptor, compiledValue);
    }
  }

  public StyleAttribute[] getStyles()
  {
    return styles;
  }
}
