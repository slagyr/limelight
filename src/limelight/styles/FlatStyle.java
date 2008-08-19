//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.util.Util;

public class FlatStyle extends Style
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

  protected boolean has(int key)
  {
    return styles[key] != null && styles[key].length() > 0;
  }

  public String[] getStyles()
  {
    return styles;
  }
}
