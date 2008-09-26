//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.util.Util;

public class FlatStyle extends BaseStyle
{
  private String[] styles;

  public FlatStyle()
	{
    styles = new String[STYLE_COUNT];
  }

  protected String get(int key)
	{
		return styles[key];
	}

	public void put(StyleDescriptor descriptor, String value)
	{
    if(value == null)
      return;
    value = value.trim();
    if(value.length() == 0)
      value = null;

    String originalValue = styles[descriptor.index];
    styles[descriptor.index] = value;
    if(!Util.equal(originalValue, value))
      recordChange(descriptor, value);
  }

  public String[] getStyles()
  {
    return styles;
  }
}
