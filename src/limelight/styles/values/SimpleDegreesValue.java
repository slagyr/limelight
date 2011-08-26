//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.values;

import limelight.styles.abstrstyling.DegreesValue;

public class SimpleDegreesValue extends SimpleIntegerValue implements DegreesValue
{
  public SimpleDegreesValue(int degrees)
  {
    super(degrees);
  }

  public int getDegrees()
  {
    return getValue();
  }
}
