package limelight.java;

import limelight.model.api.SceneProxy;
import limelight.ui.model.PropPanel;
import limelight.ui.model.Scene;
import limelight.ui.model.ScenePanel;

import java.util.Map;

public class JavaScene extends JavaProp implements SceneProxy
{
  private JavaCastingDirector castingDirector;

  public JavaScene(JavaProduction production, Map<String, Object> options)
  {
    super(options);
    castingDirector = new JavaCastingDirector(production.getPlayerLoader());
    getPeer().setCastingDirector(castingDirector);
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
