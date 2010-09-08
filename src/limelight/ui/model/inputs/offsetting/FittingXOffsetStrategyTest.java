package limelight.ui.model.inputs.offsetting;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.inputs.MockTextContainer;
import limelight.ui.model.inputs.SingleLineTextModel;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FittingXOffsetStrategyTest
{
  private XOffsetStrategy strategy;
  private SingleLineTextModel model;
  private MockTextContainer container;

  @Before
  public void setUp() throws Exception
  {
    strategy = XOffsetStrategy.FITTING;
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 100, 50);
    model = new SingleLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("0123456789012345678901234567890");
  }

  @Test
  public void shouldMoveOneCharToTheRight() throws Exception
  {
    model.setCaretLocation(TextLocation.at(0, 11));
    model.setOffset(0, 0);

    assertEquals(-11, strategy.calculateXOffset(model));
  }

  @Test
  public void shouldJumpToRightCursorLocation() throws Exception
  {
    model.setCaretLocation(TextLocation.at(0, 20));
    model.setOffset(0, 0);

    assertEquals(-101, strategy.calculateXOffset(model));
  }
  
  @Test
  public void shouldMoveOneCharToTheLeft() throws Exception
  {
    model.setCaretLocation(TextLocation.at(0, 9));
    model.setOffset(-100, 0);

    assertEquals(-90, strategy.calculateXOffset(model));
  }

  @Test
  public void shouldJumpToLeftCursorLocation() throws Exception
  {
    model.setCaretLocation(TextLocation.at(0, 2));
    model.setOffset(-200, 0);

    assertEquals(-20, strategy.calculateXOffset(model));
  }
}
