package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

public class MockTextModel extends TextModel
{
  public boolean clearLayoutsCalled;

  public MockTextModel(TextContainer container)
  {
    super(container);
    setTypedLayoutFactory(MockTypedLayoutFactory.instance);
  }

  @Override
  public void clearCache()
  {
    clearLayoutsCalled = true;
  }

  @Override
  protected int getLineNumber(int index)
  {
    return 0;
  }

  @Override
  protected void buildLines(ArrayList<TypedLayout> lines)
  {
    lines.add(createLayout(getText()));
  }

  @Override
  public Dimension getTextDimensions()
  {
    TypedLayout activeLayout = getActiveLayout();
    return new Dimension(activeLayout.getWidth(), activeLayout.getHeight());
  }

  @Override
  public TextLocation getLocationAt(Point point)
  {
    return TextLocation.at(0, 0);
  }

  @Override
  public TypedLayout getActiveLayout()
  {
    return getLines().get(0);
  }

  @Override
  public Box getCaretShape()
  {
    return getLines().get(0).getCaretShape(getCaretIndex());
  }

  @Override
  public ArrayList<Box> getSelectionRegions()
  {
    return null;
  }

  @Override
  public boolean isMoveUpEvent(int keyCode)
  {
    return false;
  }

  @Override
  public boolean isMoveDownEvent(int keyCode)
  {
    return false;
  }

  public void addLayout(String value)
  {
    getLines().add(createLayout(value));
  }
}
