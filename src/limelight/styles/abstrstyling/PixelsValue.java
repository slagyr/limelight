//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.util.Box;

public interface PixelsValue extends StyleValue
{
  int pixelsFor(int max);

  int pixelsFor(Box dounds);
}
