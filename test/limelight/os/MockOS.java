//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.os;

public class MockOS extends OS
{
  public boolean systemPropertiesConfigured;
  public String dataRoot;

  protected void turnOnKioskMode()
  {
  }

  protected void turnOffKioskMode()
  {
  }

  protected void launch(String URL)
  {
  }

  public void configureSystemProperties()
  {
    systemPropertiesConfigured = true;
  }

  @Override
  public String dataRoot()
  {
    if(dataRoot != null)
      return dataRoot;
    else
      return super.dataRoot();
  }
}
