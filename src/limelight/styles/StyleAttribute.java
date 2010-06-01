//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.Context;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.ui.Panel;

public class StyleAttribute
{
  public final int index;
  public final String name;
  public final StyleAttributeCompiler compiler; 
  public final StyleValue defaultValue;

  public StyleAttribute(int i, String name, StyleAttributeCompiler compiler, StyleValue defaultValue)
  {
    index = i;
    this.name = name;
    this.compiler = compiler;
    this.defaultValue = defaultValue;
  }

  public String toString()
  {
    return index + ": " + name;
  }

  public StyleValue compile(Object value)
  {
    return compiler.compile(value);
  }

  public boolean nameMatches(String value)
  {
    return value.toLowerCase().replaceAll("_|\\-", " ").equals(name.toLowerCase());
  }

  public void applyChange(Panel panel, StyleValue value)
  {
    Context.instance().bufferedImageCache.expire(panel);
    panel.markAsDirty();
  }
}
