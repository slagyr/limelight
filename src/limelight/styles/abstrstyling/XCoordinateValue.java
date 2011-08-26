//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.abstrstyling;

import java.awt.*;

public interface XCoordinateValue extends StyleValue
{
  int getX(int consumed, Rectangle area);
}
