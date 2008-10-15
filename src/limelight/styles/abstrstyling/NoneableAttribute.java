package limelight.styles.abstrstyling;

public class NoneableAttribute<T> implements StyleAttribute
{
  private final T attribute;

  public NoneableAttribute(T attribute)
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
    if(obj instanceof NoneableAttribute)
    {
      NoneableAttribute other = (NoneableAttribute)obj;
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
