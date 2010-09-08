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

    assertEquals(2, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeft()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(3, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(11, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }
}
