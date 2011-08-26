//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
