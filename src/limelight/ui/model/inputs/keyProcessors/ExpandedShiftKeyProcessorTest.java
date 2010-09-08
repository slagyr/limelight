package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpandedShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    processor = ExpandedShiftKeyProcessor.instance;
    modifiers = 1;
  }

  @Test
  public void canProcessRightArrowAndBeingSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndBeginSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(1, 3));

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(1, 3), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(0, 3));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 3), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }
}
