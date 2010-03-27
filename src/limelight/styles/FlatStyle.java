//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.LimelightException;
import limelight.util.Util;
import limelight.styles.abstrstyling.StyleAttribute;

public class FlatStyle extends BaseStyle
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

  public boolean hasScreen()
  {
    return false;
  }

  public void removeScreen()
  {
    throw new LimelightException("Can't remove screen from FlatStyle");
  }

  public void applyScreen(Style screenStyle)
  {
    throw new LimelightException("Can't apply screen to FlatStyle");
  }

  public Style getScreen()
  {
    return null;
  }

  public StyleAttribute[] getStyles()
  {
    return styles;
  }
}
