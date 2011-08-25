//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class FakeKeyword
{
  public String value;

  public FakeKeyword(String value)
  {
    this.value = value;
  }

  @Override
  public String toString()
  {
    return ":" + value;
  }
}
