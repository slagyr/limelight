//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import java.awt.*;

public interface Drawable
{
  public void draw(Graphics2D graphics2D, int x, int y, int scaledWidth, int scaledHeight);
}
