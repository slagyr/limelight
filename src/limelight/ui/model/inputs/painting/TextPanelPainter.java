//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import limelight.ui.model.inputs.TextModel;

import java.awt.*;

public abstract class TextPanelPainter
{
  abstract public void paint(Graphics2D graphics, TextModel boxInfo);
}
