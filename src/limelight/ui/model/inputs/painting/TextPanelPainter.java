//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs.painting;

import limelight.ui.model.inputs.TextModel;

import java.awt.*;

public abstract class TextPanelPainter
{
  abstract public void paint(Graphics2D graphics, TextModel boxInfo);
}
