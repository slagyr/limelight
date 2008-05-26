//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

public class StyleDescriptor
{

  public final int index;
  public final String defaultValue;
  public final String name;

  public StyleDescriptor(int i, String name, String defaultValue)
  {
    index = i;
    this.name = name;
    this.defaultValue = defaultValue;
  }
}
