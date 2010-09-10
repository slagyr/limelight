//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;

public class TextInputKeyProcessor
{
  public static TextInputKeyProcessor instance = new TextInputKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    if(event.isAltDown() && (event.isCommandDown() || event.isControlDown()))
      return;

    switch(event.getKeyCode())
    {
      case KeyEvent.KEY_A:
        selectAll(event, model);
        break;
      case KeyEvent.KEY_C:
        copy(event, model);
        break;
      case KeyEvent.KEY_V:
        paste(event, model);
        break;
      case KeyEvent.KEY_X:
        cut(model, event);
        break;
      case KeyEvent.KEY_RIGHT:
        goRight(model, event);
        break;
      case KeyEvent.KEY_LEFT:
        goLeft(model, event);
        break;
      case KeyEvent.KEY_UP:
        goUp(model, event);
        break;
      case KeyEvent.KEY_DOWN:
        goDown(model, event);
        break;
      case KeyEvent.KEY_BACK_SPACE:
        backspace(model);
        break;
      case KeyEvent.KEY_DELETE:
        delete(model);
      case KeyEvent.KEY_CLEAR:
        clear(model);
        break;
    }
  }

  private void selectAll(KeyEvent event, TextModel model)
  {
    if(isJustCommandOrControl(event))
      model.selectAll();
  }

  private void copy(KeyEvent event, TextModel model)
  {
    if(isJustCommandOrControl(event))
      model.copySelection();
  }

  private void cut(TextModel model, KeyEvent event)
  {
    if(isJustCommandOrControl(event))
    {
      model.copySelection();
      model.deleteSelection();
    }
  }

  private void paste(KeyEvent event, TextModel model)
  {
    if(isJustCommandOrControl(event))
    {
      model.deleteSelection();
      model.pasteClipboard();
    }
  }

  private boolean isJustCommandOrControl(KeyEvent event)
  {
    return event.getModifiers() == KeyEvent.COMMAND_MASK || event.getModifiers() == KeyEvent.CONTROL_MASK;
  }

  private void delete(TextModel model)
  {
    if(model.isSelectionActivated())
      model.deleteSelection();
    else if(model.getCaretLocation().before(model.getEndLocation()))
    {
      TextLocation start = model.getCaretLocation();
      TextLocation end = start.moved(model.getLines(), 1);
      model.deleteEnclosedText(start, end);
    }
  }

  private void backspace(TextModel model)
  {
    if(model.isSelectionActivated())
      model.deleteSelection();
    else if(TextLocation.origin.before(model.getCaretLocation()))
    {
      TextLocation end = model.getCaretLocation();
      TextLocation start = end.moved(model.getLines(), -1);
      model.deleteEnclosedText(start, end);
    }
  }

  private void clear(TextModel model)
  {
    model.deleteSelection();
  }

  private void goLeft(TextModel model, KeyEvent event)
  {
    boolean hadSelectionActivated = model.isSelectionActivated();
    startOrStopSelection(model, event);

    if(hadSelectionActivated && !model.isSelectionActivated() && !event.hasModifier())
      model.setCaretLocation(model.getSelectionStart());
    else if(canMoveLeft(model))
    {
      if(event.isCommandDown() || event.isControlDown())
        model.sendCaretToStartOfLine();
      else if(event.isAltDown())
        model.setCaretLocation(model.locateNearestWordToTheLeft());
      else
        model.moveCaret(-1);
    }
  }

  private void goRight(TextModel model, KeyEvent event)
  {
    boolean hadSelectionActivated = model.isSelectionActivated();
    startOrStopSelection(model, event);

    if(hadSelectionActivated && !model.isSelectionActivated() && !event.hasModifier())
      model.setCaretLocation(model.getSelectionEnd());
    else if(canMoveRight(model))
    {
      if(event.isCommandDown() || event.isControlDown())
        model.sendCaretToEndOfLine();
      else if(event.isAltDown())
        model.setCaretLocation(model.locateNearestWordToTheRight());
      else
        model.moveCaret(+1);
    }
  }

  private void goDown(TextModel model, KeyEvent event)
  {
    startOrStopSelection(model, event);

    if(event.isCommandDown() || event.isControlDown())
      model.setCaretLocation(model.getEndLocation());
    if(model.isSingleLine() || event.isAltDown())
      model.sendCaretToEndOfLine();
    else if(canMoveDown(model))
      model.moveCaretDownALine();
  }

  private void goUp(TextModel model, KeyEvent event)
  {
    startOrStopSelection(model, event);

    if(event.isCommandDown() || event.isControlDown())
      model.setCaretLocation(TextLocation.origin);
    else if(model.isSingleLine() || event.isAltDown())
      model.sendCaretToStartOfLine();
    else if(canMoveUp(model))
      model.moveCaretUpALine();
  }

  private void startOrStopSelection(TextModel model, KeyEvent event)
  {
    if(!model.isSelectionActivated() && event.isShiftDown())
      model.startSelection(model.getCaretLocation());
    else if(!event.isShiftDown())
      model.deactivateSelection();
  }

  private boolean canMoveDown(TextModel model)
  {
    final int lineCount = model.getLines().size();
    return lineCount > 1 && model.getCaretLocation().line < (lineCount - 1);
  }

  private boolean canMoveUp(TextModel model)
  {
    return model.getLines().size() > 1 && model.getCaretLocation().line > 0;
  }

  private boolean canMoveLeft(TextModel model)
  {
    return model.getCaretLocation().index > 0 || canMoveUp(model);
  }

  private boolean canMoveRight(TextModel model)
  {
    final TextLocation caret = model.getCaretLocation();
    final TypedLayout line = model.getLines().get(caret.line);
    return caret.index < line.length() || canMoveDown(model);
  }
}