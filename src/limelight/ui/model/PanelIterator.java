package limelight.ui.model;

import limelight.ui.Panel;
import limelight.LimelightError;
import java.util.Iterator;
import java.util.Stack;

public class PanelIterator implements Iterator<Panel>
{
  private Stack<Iterator<Panel>> iterators = new Stack<Iterator<Panel>>();
  private Panel next;

  public PanelIterator(Panel root)
  {
    this.next = root;
    iterators.push(root.getChildren().iterator());
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
        if(next.hasChildren())
          iterators.push(next.getChildren().iterator());
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
