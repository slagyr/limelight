//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;


public class MockStage implements Stage
{
  public MockTheater theater;

  public MockStage()
  {
    theater = new MockTheater();
  }

  public Theater theater()
  {
    return theater;
  }
}
