package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.model.inputs.keyProcessors.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

public class MockTextInputPanel extends TextInputPanel
{
  public MockTextModel mockModel;

  public MockTextInputPanel()
  {
    boxInfo = mockModel = new MockTextModel(this);
    keyProcessors = new ArrayList<KeyProcessor>(16);
//    mouseProcessor = new MouseProcessor(boxInfo);
//    painterComposite = new TextPanelPainterComposite(boxInfo);
//    horizontalTextAlignment = new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT);
//    verticalTextAlignment = new SimpleVerticalAlignmentValue(VerticalAlignment.CENTER);

    for(int i = 0; i < 16; i++)
      keyProcessors.add(i, new MockKeyProcessor());
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
  public void initKeyProcessors()
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
