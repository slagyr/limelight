//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui;

import limelight.styles.ScreenableStyle;
import limelight.ui.painting.Border;
import limelight.util.Box;

public interface PaintablePanel extends Panel
{
  ScreenableStyle getStyle();

  Border getBorderShaper();

  Box getBorderedBounds();

  Box getMarginedBounds();
}
