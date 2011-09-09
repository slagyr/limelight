//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.builtin.BuiltinBeacon;
import limelight.java.JavaCastingDirector;
import limelight.model.FakeProduction;
import limelight.model.PlayerRecruiter;
import limelight.model.api.CastingDirector;
import limelight.model.api.FakeCastingDirector;
import limelight.model.api.FakePropProxy;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class PlayerRecruiterTest
{
  private PropPanel panel;
  private PlayerRecruiter recruiter;
  private FakeCastingDirector castingDirector;
  private ScenePanel scene;
  private FakeProduction production;
  private FakeCastingDirector buildingCastingDirector;

  @Before
  public void setUp() throws Exception
  {
    production = new FakeProduction("/path/to/testProduction");
    scene = new ScenePanel(new FakePropProxy(), Util.toMap("name", "theScene", "path", "/path/to/testProduction/theScene"));
    scene.setProduction(production);
    panel = new PropPanel(new FakePropProxy());
    scene.add(panel);
    castingDirector = new FakeCastingDirector();
    recruiter = new PlayerRecruiter();
    buildingCastingDirector = new FakeCastingDirector();
    recruiter.setBuiltinCastingDirector(buildingCastingDirector);
  }

  @Test
  public void recruitingFromTheScene() throws Exception
  {
    castingDirector.recruitablePath = "file:/path/to/testProduction/theScene/players";

    String player = recruiter.recruit(panel, "blah", castingDirector);

    assertEquals("theScene/blah", player);
    assertEquals(1, castingDirector.castings.size());
    final List<String> casts = castingDirector.castings.get(panel.getProxy());
    assertEquals(1, casts.size());
    assertEquals("file:/path/to/testProduction/theScene/players/blah", casts.get(0));
  }

  @Test
  public void recruitingFromTheProduction() throws Exception
  {
    castingDirector.recruitablePath = "file:/path/to/testProduction/players";

    recruiter.recruit(panel, "blah", castingDirector);

    assertEquals(1, castingDirector.castings.size());
    final List<String> casts = castingDirector.castings.get(panel.getProxy());
    assertEquals(1, casts.size());
    assertEquals("file:/path/to/testProduction/players/blah", casts.get(0));
  }

  @Test
  public void builtinCastingDirector() throws Exception
  {
    recruiter = new PlayerRecruiter();
    final CastingDirector director = recruiter.getBuiltinCastingDirector();
    assertNotNull(director);
    assertEquals(JavaCastingDirector.class, director.getClass());
    assertEquals(ClassLoader.getSystemClassLoader(), ((JavaCastingDirector)director).getClassLoader());
  }

  @Test
  public void recruitBuiltinPlayers() throws Exception
  {
    castingDirector.recruitablePath = "none";
    buildingCastingDirector.recruitablePath = BuiltinBeacon.getBuiltinPlayersPath();

    recruiter.recruit(panel, "blah", castingDirector);

    assertEquals(1, buildingCastingDirector.castings.size());
    final List<String> casts = buildingCastingDirector.castings.get(panel.getProxy());
    assertEquals(1, casts.size());
    assertEquals(BuiltinBeacon.getBuiltinPlayersPath() + "/blah", casts.get(0));
  }
}
