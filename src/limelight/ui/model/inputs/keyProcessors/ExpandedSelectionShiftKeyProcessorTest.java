package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpandedSelectionShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = ExpandedSelectionOnShiftKeyProcessor.instance;
    modifiers = 1;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRight()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeft()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(1, 3));

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(0, 3));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }
}
