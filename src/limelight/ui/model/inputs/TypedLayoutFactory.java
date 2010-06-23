package limelight.ui.model.inputs;

import limelight.ui.TypedLayout;

import java.awt.*;
import java.awt.font.FontRenderContext;

public interface TypedLayoutFactory
{
  TypedLayout createLayout(String text, Font font, FontRenderContext renderContext);
}
