//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.util.Box;
import limelight.styles.HorizontalAlignment;

public interface HorizontalAlignmentAttribute extends StyleAttribute
{
  HorizontalAlignment getAlignment();
  int getX(int consumed, Box area);
}
