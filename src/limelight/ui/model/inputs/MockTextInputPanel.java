package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.model.inputs.keyProcessors.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

public class MockTextInputPanel extends TextInputPanel
{
  public MockTextModel mockModel;

  public MockTextInputPanel()
  {
    model = mockModel = new MockTextModel(this);
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

  @Override
  public KeyProcessor getKeyProcessorFor(int modifiers)
  {
    return new MockKeyProcessor();
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
