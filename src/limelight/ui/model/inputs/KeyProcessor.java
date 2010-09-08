//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;

import java.lang.reflect.Field;

public abstract class KeyProcessor
{
  protected KeyProcessor()
  {
    try
    {
      Class klass = getClass();
      Field field = klass.getDeclaredField("instance");
      Object value = field.get(klass);
      if(value != null)
        throw new RuntimeException("Attempt to create second instance of " + klass.getName() + ".");
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  protected static boolean canMoveDown(TextModel model)
  {
    final int lineCount = model.getLines().size();
    return lineCount > 1 && model.getCaretLocation().line < (lineCount - 1);
  }

  protected static boolean canMoveUp(TextModel model)
  {
    return model.getLines().size() > 1 && model.getCaretLocation().line > 0;
  }

  protected static boolean canMoveLeft(TextModel model)
  {
    return model.getCaretLocation().index > 0 || canMoveUp(model);
  }

  protected static boolean canMoveRight(TextModel model)
  {
    final TextLocation caret = model.getCaretLocation();
    final TypedLayout line = model.getLines().get(caret.line);
    return caret.index < (line.length() - 1) || canMoveDown(model);
  }

  public KeyProcessor(Object o)
  {
    // to bypass normal constructor
  }

  public abstract void processKey(KeyEvent event, TextModel model);
}

