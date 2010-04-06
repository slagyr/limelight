//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.DegreesAttribute;

public class SimpleDegreesAttribute extends SimpleIntegerAttribute implements DegreesAttribute
{
  public SimpleDegreesAttribute(int degrees)
  {
    super(degrees);
  }

  public int getDegrees()
  {
    return getValue();
  }
}
