package limelight.ui.text;

import limelight.ui.text.TypedLayout;

import java.awt.*;
import java.awt.font.FontRenderContext;

public interface TypedLayoutFactory
{
  TypedLayout createLayout(String text, Font font, FontRenderContext renderContext);
}
