//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.util.Box;

import java.awt.*;

public interface TextContainer
{
  Style getStyle();

  Point getAbsoluteLocation();

  int getWidth();

  int getHeight();

  Box getConsumableBounds();

  boolean hasFocus();
}
