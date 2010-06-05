//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import java.awt.event.KeyEvent;
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

  public KeyProcessor(Object o)
  {
    // to bypass normal constructor
  }

  public abstract void processKey(KeyEvent event, TextModel boxInfo);

  protected boolean isACharacter(int keyCode)
  {
    return (keyCode > 40 && keyCode < 100 || keyCode == 222 || keyCode == 32);
  }
}

