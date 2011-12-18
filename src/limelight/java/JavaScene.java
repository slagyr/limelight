//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.api.SceneProxy;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;

import java.util.Map;

public class JavaScene extends JavaProp implements SceneProxy
{
  private JavaPlayerRecruiter castingDirector;

  public JavaScene(JavaProduction production, Map<String, Object> options)
  {
    super(options);
    castingDirector = new JavaPlayerRecruiter(production.getPlayerLoader());
    getPeer().setPlayerRecruiter(castingDirector);
    getPeer().setProduction(production);
  }

  @Override
  protected PropPanel createPeer(Map<String, Object> options)
  {
    final ScenePanel panel = new ScenePanel(this);
    panel.addOptions(options);
    return panel;
  }

  public ScenePanel getPeer()
  {
    return (ScenePanel)peer;
  }

  public JavaProduction getProduction()
  {
    return (JavaProduction)getPeer().getProduction();
  }

  public JavaProp findProp(String id)
  {
    final PropPanel panel = getPeer().find(id);
    if(panel != null)
      return (JavaProp)panel.getProxy();
    else
      return null;
  }
}
