package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

public class MockTextInputPanel extends TextInputPanel
{
  public MockTextModel mockModel;
  public Box bounds;

  @Override
  protected TextModel createModel()
  {
    return mockModel = new MockTextModel(this);
  }

  @Override
  protected void markCursorRegionAsDirty()
  {
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
  }

  @Override
  public void paintOn(Graphics2D graphics)
  {
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
