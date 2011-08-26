//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.Stage;
import limelight.model.Theater;
import limelight.model.api.TheaterProxy;

import java.util.Map;

public class JavaTheater implements TheaterProxy
{
  private Theater peer;

  public JavaTheater(Theater peer)
  {
    this.peer = peer;
    peer.setProxy(this);
  }

  public Stage buildStage(String name, Map<String, Object> options)
  {
    return new JavaStage(name, options).getPeer();
  }

  public void add(Stage stage)
  {
    peer.add(stage);
  }
}
