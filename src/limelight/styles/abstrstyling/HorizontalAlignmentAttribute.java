//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.styles.HorizontalAlignment;

import java.awt.*;

public interface HorizontalAlignmentAttribute extends StyleAttribute
{
  HorizontalAlignment getAlignment();
  int getX(int consumed, Rectangle area);
}
