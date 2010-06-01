//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

public class NoneableValue<T> implements StyleValue
{
  private final T attribute;

  public NoneableValue(T attribute)
  {
    this.attribute = attribute;
  }

  public boolean isNone()
  {
    return attribute == null;
  }

  public T getAttribute()
  {
    return attribute;
  }

  public String toString()
  {
    if(isNone())
      return "none";
    else
      return attribute.toString();
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof NoneableValue)
    {
      NoneableValue other = (NoneableValue)obj;
      if(isNone() && other.isNone())
        return true;
      else if(isNone())
        return false;
      else
        return attribute.equals(other.getAttribute());
    }
    else if(attribute != null)
      return attribute.equals(obj);
    else
      return false;
  }
}
