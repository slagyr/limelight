//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.styles.VerticalAlignment;

import java.awt.*;

public interface VerticalAlignmentValue extends StyleValue
{
  VerticalAlignment getAlignment();
  int getY(int consumed, Rectangle area);
}
