//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.KeyEvent;

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

  protected static boolean hasLineBelowCaret(TextModel model)
  {
    final int lineCount = model.getLines().size();
    return lineCount > 1 && model.getCaretLocation().line < (lineCount - 1);
  }

  protected static boolean hasLineAboveCaret(TextModel model)
  {
    return model.getLines().size() > 1 && model.getCaretLocation().line > 0;
  }

  public KeyProcessor(Object o)
  {
    // to bypass normal constructor
  }

  public abstract void processKey(KeyEvent event, TextModel model);
}

