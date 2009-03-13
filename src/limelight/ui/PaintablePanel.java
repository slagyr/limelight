//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.styles.Style;
import limelight.ui.painting.Border;
import limelight.ui.api.Prop;
import limelight.util.Box;

public interface PaintablePanel
{
  Style getStyle();

  Border getBorderShaper();

  Box getBoxInsideBorders();

  Prop getProp();

}
