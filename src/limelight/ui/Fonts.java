package limelight.ui;

import limelight.styles.Style;

import java.awt.*;

public class Fonts
{
  public static Font fromStyle(Style style)
  {
    String fontFace = style.getCompiledFontFace().getValue();
    int fontStyle = style.getCompiledFontStyle().toInt();
    int fontSize = style.getCompiledFontSize().getValue();
    return new Font(fontFace, fontStyle, fontSize);
  }
}
