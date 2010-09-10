package limelight.ui.model.inputs;


import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.events.KeyEvent;
import limelight.ui.events.KeyPressedEvent;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

import static org.junit.Assert.assertEquals;

public class TextInputKeyProcessorTest
{
  public static final int NO_MODIFIERS = 0;
  public static final int SHIFT = KeyEvent.SHIFT_MASK;
  public static final int ALT = KeyEvent.ALT_MASK;
  public static final int CMD = KeyEvent.COMMAND_MASK;


  public TextModel model;
  public MockTextContainer container;
  public TextInputKeyProcessor processor;

  @Before
  public void setUp() throws Exception
  {
    container = new MockTextContainer();
    processor = TextInputKeyProcessor.instance;
  }

  public TextInputKeyProcessorTest setUp(TextModel model, String text)
  {
    this.model = model;
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText(text);
    return this;
  }

  private TextInputKeyProcessorTest setupSingleLine(String text)
  {
    container.bounds = new Box(0, 0, 150, 20);
    return setUp(new SingleLineTextModel(container), text);
  }

  private TextInputKeyProcessorTest setupMultiLine(String text)
  {
    container.bounds = new Box(0, 0, 150, 75);
    return setUp(new MultiLineTextModel(container), text);
  }

  private TextInputKeyProcessorTest withCaretAt(int line, int index)
  {
    model.setCaretLocation(TextLocation.at(line, index));
    return this;
  }

  private TextInputKeyProcessorTest andSelectionAt(int line, int index)
  {
    model.startSelection(TextLocation.at(line, index));
    return this;
  }

  @Test
  public void backSpace()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_BACK_SPACE, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals("ere are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void delete()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DELETE, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("Hre are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void rightArrow()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void leftArrow()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }


  @Test
  public void backSpaceWithMultipleLines()
  {
    setupMultiLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_BACK_SPACE, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals("ere are four words", model.getText());
  }

  @Test
  public void rightArrowWithMultipleLines()
  {
    setupMultiLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void leftArrowWithMultipleLines()
  {
    setupMultiLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void upArrowDoesNothingWhenAllTheWayUp()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(0, 3);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void upArrowMovesUpALine()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void downArrowDoesNothingWhenAtBottom()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void downArrowMovesDownALine()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void downArrowMovesDownALineToTheLastCharacterIfTheFirstLineRunsPastTheSecond()
  {
    setupMultiLine("This is a longer\nMulti lined.").withCaretAt(0, 15);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void goToTheEndOfPreviousLineEvenIfItEndsWithNewline() throws Exception
  {
    setupMultiLine("Some more text\nand some more");
    model.setCaretLocation(model.getEndLocation());

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(0, 13), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void goToTheEndOfNextLineEvenIfItEndsWithNewline() throws Exception
  {
    setupMultiLine("blah\nasdf\nasdf").withCaretAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(1, 4), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void backSpaceDeleteSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_BACK_SPACE, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("H are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void deleteDeletesSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DELETE, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("H are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void clearDeletesSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_CLEAR, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("H are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void rightArrowWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 4), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void downArrowWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void leftArrowWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void upArrowWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void upArrowWithMiltipleLinesAndSelection()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void downArrowWithMultipleLinesAndSelection()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(0, 3).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, NO_MODIFIERS, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void shiftRightArrowBeginsSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftRightArrowWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftLeftArrowBeginsSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftLeftArrowWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftUpArrowBeginsSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftDownArrowBeginsSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftRightArrowBeingsSelectionWithMultipleLines()
  {
    setupMultiLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftLeftArrowBeginsSelectionWithMultipleLines()
  {
    setupMultiLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftUpArrowMovesUpALineAndBeginsSelectionWithMultipleLines()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3);

    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(1, 3), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftDownArrowMovesDownALineAndBeginsSelectionWithMultipleLines()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(0, 3);

    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 3), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shitUpArrowMovesUpALineWithSelectionWithMutlipleLines()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftDownArrowMovesDownALineWithSelection()
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(0, 3).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void altRightArrowJumpsToTheNextWord()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);
    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
  }

  @Test
  public void altLeftArrowJumpsToThePreviousWord()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 9);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
  }

  @Test
  public void altDownGoesToEndOfLine() throws Exception
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
  }

  @Test
  public void altUpGoesToBeginningOfLine() throws Exception
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
  }

  @Test
  public void altDownGoesToEndOfLineWithMultipleLines() throws Exception
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(0, 3);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(TextLocation.at(0, 7), model.getCaretLocation());
  }

  @Test
  public void altUpGoesToBeginningOfLineWithMultipleLines() throws Exception
  {
    setupMultiLine("This is\nMulti lined.").withCaretAt(1, 3);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.at(1, 0), model.getCaretLocation());
  }

  @Test
  public void altRightArrowMovesToStartOfNextWordWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altLeftArrowMovesToEndOfPreviousWord()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 9).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altUpArrowMovesToStartOfLineWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altDownArrowMovesToEndOfLine()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 9).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altCmdRightArrowDoesNothing()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT + CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altCmdCharacterDoesNothing()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT + CMD, KeyEvent.KEY_A, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altCmdCharacterAndDoNothingWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT + CMD, KeyEvent.KEY_A, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void altShiftCmdRightArrowDoesNothing()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT + CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altShiftCmdCharacterDoesNothing()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT + CMD, KeyEvent.KEY_A, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void altShiftCmdCharacterDoesNothingWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT + CMD, KeyEvent.KEY_A, 0), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void altShiftRightArrowSelectsToTheNextWord()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void altShiftLeftArrowSelectsToThePreviousWord()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 9), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void altShiftRightArrowSelectsToStartOfNextWordWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void altShiftLeftArrowSelectsToEndOfPreviousWordWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 9).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, ALT + SHIFT, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void cmdASelectsAll()
  {
    setupSingleLine("Bob");

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_A, 0), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void cmdVPastesAtCursor()
  {
    setupSingleLine("Bob");
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(" Dole"), model);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_V, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals("Bob Dole", model.getText());
  }

  @Test
  public void cmdRightArrowGoesToEndOfLine()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdRightArrowGoesToEndOfLineWithMultipleText()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(TextLocation.at(0, 8), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdLeftArrowGoesToStartOfLine()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 5);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdLeftArrowGoesToStartOfLineWithMultipleLines()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(1, 5);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.at(1, 0), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdUpArrowGoesToStartOfText()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 5);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdDownArrowGoesToEndOfText()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdUpArrowGoesToStartOfTextWithMultipleLines()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(1, 5);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdDownArrowGoesToEndOfTextWithMultipleLines()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(1, 3);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdASelectsAllWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_A, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void cmdVPastesAtCursorWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("oot"), model);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_V, 0), model);

    assertEquals("Hoot are four words", model.getText());
    assertEquals(TextLocation.at(0, 4), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdCCopiesSelectedTextWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_C, 0), model);

    assertEquals("Here are four words", model.getText());
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void cmdXCutsSelectedTextWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_X, 0), model);

    assertEquals("H are four words", model.getText());
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void cmdRightArrowJumpsToRightEdgeAndEndsSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdLeftArrowJumpsToLeftEdgeAndEndsSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdUpArrowJumpsToTheStartWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void cmdDownArrowJumpsToTheEndWithSelection()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdRightArrowSelectsToTheRightEdge()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdLeftArrowSelectsToTheLeftEdge()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdUpArrowSelectsToStartOfText()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdDownArrowSelectsToEndOfText()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdUpArrowSelectsToStartOfTextWithMultipleLines()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(1, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(1, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdDownArrowSelectsToEndOfTextMultipleLines()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(0, 1);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdRightArrowSelectsToTheRightEdgeWithSelectionOn()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_RIGHT, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdLeftArrowSelectsToTheLeftEdgeWithSelectionOn()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_LEFT, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdUpArrowSelectsToStartOfTextWithSelectionOn()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdDownArrowSelectsToEndOfTextWithSelectionOn()
  {
    setupSingleLine("Here are four words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdUpArrowSelectsToStartOfTextWithMultipleLinesWithSelectionOn()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(1, 4).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_UP, 0), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void shiftCmdDownArrowSelectsToEndOfTextMultipleLinesWithSelectionOn()
  {
    setupMultiLine("Here are\nfour words").withCaretAt(0, 1).andSelectionAt(0, 4);

    processor.processKey(new KeyPressedEvent(container, SHIFT + CMD, KeyEvent.KEY_DOWN, 0), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }
}
