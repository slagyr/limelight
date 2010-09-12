//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.LimelightError;
import java.util.Iterator;
import java.util.Stack;

// TODO MDM Does anyone use me?
public class PanelIterator implements Iterator<Panel>
{
  private final Stack<Iterator<Panel>> iterators = new Stack<Iterator<Panel>>();
  private Panel next;

  public PanelIterator(PanelBase root)
  {
    this.next = root;
    if(root instanceof ParentPanel)
      iterators.push(((ParentPanel)root).getChildren().iterator());
  }

  public boolean hasNext()
  {
    return next != null;
  }

  public Panel next()
  {
    Panel value = next;
    next = null;
    if(value != null)
    {
      Iterator<Panel> iterator = findNextValidIterator();
      if(iterator != null)
      {
        next = iterator.next();

        if(next instanceof ParentPanelBase)
        {
          final ParentPanelBase nextParent = (ParentPanelBase) next;
          if(nextParent.hasChildren())
            iterators.push(nextParent.getChildren().iterator());
        }
      }
    }

    return value;
  }

  private Iterator<Panel> findNextValidIterator()
  {
    while(!iterators.empty() && !iterators.peek().hasNext())
      iterators.pop();

    if(iterators.empty())
      return null;
    else
      return iterators.peek();
  }

  public void remove()
  {
    throw new LimelightError("Iterator.remove is not allowed");
  }
}
