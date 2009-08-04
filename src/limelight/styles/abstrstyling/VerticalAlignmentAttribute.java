//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.util.Box;
import limelight.styles.VerticalAlignment;

public interface VerticalAlignmentAttribute extends StyleAttribute
{
  VerticalAlignment getAlignment();
  int getY(int consumed, Box area);
}
