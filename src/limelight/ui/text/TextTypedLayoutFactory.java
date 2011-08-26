//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.text;

import java.awt.*;
import java.awt.font.FontRenderContext;

public class TextTypedLayoutFactory implements TypedLayoutFactory
{
  public static TypedLayoutFactory instance = new TextTypedLayoutFactory();

  public TypedLayout createLayout(String text, Font font, FontRenderContext renderContext)
  {
    return new TextLayoutImpl(text, font, renderContext);
  }
}
