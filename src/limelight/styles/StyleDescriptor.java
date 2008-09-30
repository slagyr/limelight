//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StyleAttributeCompiler;

public class StyleDescriptor
{

  public final int index;
  public final String name;
  public final StyleAttributeCompiler compiler; 
  public final StyleAttribute defaultValue;

  public StyleDescriptor(int i, String name, StyleAttributeCompiler compiler, StyleAttribute defaultValue)
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

  public StyleAttribute compile(String value)
  {
    return compiler.compile(value);
  }
}
