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
