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
