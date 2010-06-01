//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleValue;
import limelight.util.Util;

public class FlatStyle extends Style
{
  private final StyleValue[] styles;

  public FlatStyle()
	{
    styles = new StyleValue[STYLE_COUNT];
  }

  protected StyleValue get(int key)
	{
    StyleValue style = styles[key];
    return style == null ? null : style;
	}

	public void put(StyleAttribute attribute, Object value)
	{
    if(value == null)
      return;

    StyleValue compiledValue = attribute.compile(value);

    StyleValue originalValue = styles[attribute.index];
    styles[attribute.index] = compiledValue;
    if(!Util.equal(originalValue, compiledValue))
      recordChange(attribute, compiledValue);
  }

  @Override
  protected void putCompiled(StyleAttribute attribute, StyleValue compiledValue)
  {
    StyleValue originalValue = styles[attribute.index];
    if(!Util.equal(originalValue, compiledValue))
    {
      styles[attribute.index] = compiledValue;
      notifyObserversOfChange(attribute, compiledValue);
    }
  }

  public StyleValue[] getStyles()
  {
    return styles;
  }
}
