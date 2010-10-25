package limelight.commands;

import limelight.About;
import limelight.Context;
import limelight.io.FakeFileSystem;
import limelight.io.Templater;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CreateCommandTest
{
  private CreateCommand command;
  private FakeFileSystem fileSystem;
  private ByteArrayOutputStream output;

  private static class NullTemplaterLogger extends Templater.TemplaterLogger
  {
    @Override
    public void say(String message)
    {
    }
  }

  @Before
  public void setUp() throws Exception
  {
    System.setProperty("limelight.home", "home");
    Context.removeInstance();

    output = new ByteArrayOutputStream();
    Command.setOutput(new PrintStream(output));
    fileSystem = new FakeFileSystem();

    command = new CreateCommand();
    command.setTemplaterLoger(new NullTemplaterLogger());
    command.setFileSystem(fileSystem);
  }

  private void setupSceneTemplates()
  {
    fileSystem.createTextFile("home/lib/limelight/templates/sources/scene/props.rb.template", "props content !-SCENE_TITLE-!");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/scene/styles.rb.template", "styles content !-SCENE_NAME-!");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/scene_spec/scene_spec.rb.template", "scene_spec content");
  }

  private void setupProductionTemplates()
  {
    fileSystem.createTextFile("home/lib/limelight/templates/sources/production/production.rb.template", "production content !-PRODUCTION_NAME-! !-CURRENT_VERSION-!");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/production/stages.rb.template", "stages content !-DEFAULT_SCENE_NAME-!");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/production/styles.rb.template", "styles content");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/production/spec/spec_helper.rb.template", "spec_helper content");
  }

  private void setupProjectTemplates()
  {
    fileSystem.createTextFile("home/lib/limelight/templates/sources/features/step_definitions/limelight_steps.rb.template", "limelight_steps content");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/features/support/env.rb.template", "env content");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/project/Rakefile.template", "Rakefile content !-LLP_NAME-!");
    fileSystem.createTextFile("home/lib/limelight/templates/sources/project/spec_helper.template", "project spec_helper content");
  }

  @Test
  public void argumentsAreNotNull() throws Exception
  {
    assertNotNull(command.getArguments());
  }

  @Test
  public void badComponent() throws Exception
  {
    command.execute("blah", "blah");

    assertEquals("ERROR! I don't know how to create 'blah's", output.toString().trim());
  }

  @Test
  public void creatingScene() throws Exception
  {
    setupSceneTemplates();

    command.execute("scene", "howdy");

    assertEquals("props content Howdy", fileSystem.readTextFile("howdy/props.rb"));
    assertEquals("styles content howdy", fileSystem.readTextFile("howdy/styles.rb"));
    assertEquals(true, fileSystem.exists("howdy/players"));
    assertEquals("scene_spec content", fileSystem.readTextFile("spec/howdy/howdy_spec.rb"));
  }

  @Test
  public void creatingProduction() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();

    command.execute("production", "hello");

    assertEquals("production content Hello " + About.version.toString(), fileSystem.readTextFile("hello/production.rb"));
    assertEquals("stages content default_scene", fileSystem.readTextFile("hello/stages.rb"));
    assertEquals("styles content", fileSystem.readTextFile("hello/styles.rb"));
    assertEquals("spec_helper content", fileSystem.readTextFile("hello/spec/spec_helper.rb"));
    assertEquals("props content Default Scene", fileSystem.readTextFile("hello/default_scene/props.rb"));
    assertEquals("styles content default_scene", fileSystem.readTextFile("hello/default_scene/styles.rb"));
    assertEquals(true, fileSystem.exists("hello/default_scene/players"));
    assertEquals("scene_spec content", fileSystem.readTextFile("hello/spec/default_scene/default_scene_spec.rb"));
  }

  @Test
  public void creatingProductionUsingCustomSceneName() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();

    command.execute("production", "hello", "--scene-name=baby");

    assertEquals("props content Baby", fileSystem.readTextFile("hello/baby/props.rb"));
    assertEquals("styles content baby", fileSystem.readTextFile("hello/baby/styles.rb"));
    assertEquals(true, fileSystem.exists("hello/baby/players"));
    assertEquals("scene_spec content", fileSystem.readTextFile("hello/spec/baby/baby_spec.rb"));
  }

  @Test
  public void creatingSceneWithCustomProductionPath() throws Exception
  {
    fileSystem.createDirectory("myProd");
    setupSceneTemplates();

    command.execute("scene", "howdy", "--production-path=myProd");

    assertEquals("props content Howdy", fileSystem.readTextFile("myProd/howdy/props.rb"));
    assertEquals("styles content howdy", fileSystem.readTextFile("myProd/howdy/styles.rb"));
    assertEquals(true, fileSystem.exists("myProd/howdy/players"));
    assertEquals("scene_spec content", fileSystem.readTextFile("myProd/spec/howdy/howdy_spec.rb"));
  }

  @Test
  public void creatingProductionUsingCustomTestPath() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();

    command.execute("production", "hello", "--test-path=tests");

    assertEquals("scene_spec content", fileSystem.readTextFile("hello/tests/default_scene/default_scene_spec.rb"));
    assertEquals("spec_helper content", fileSystem.readTextFile("hello/tests/spec_helper.rb"));
  }

  @Test
  public void creatingProject() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();
    setupProjectTemplates();

    command.execute("project", "hello");

    assertEquals("limelight_steps content", fileSystem.readTextFile("hello/features/step_definitions/limelight_steps.rb"));
    assertEquals("env content", fileSystem.readTextFile("hello/features/support/env.rb"));
    assertEquals("Rakefile content hello", fileSystem.readTextFile("hello/Rakefile"));
    assertEquals("spec_helper content", fileSystem.readTextFile("hello/spec/spec_helper.rb"));
    assertEquals("scene_spec content", fileSystem.readTextFile("hello/spec/default_scene/default_scene_spec.rb"));
    assertEquals("production content Hello " + About.version.toString(), fileSystem.readTextFile("hello/production/production.rb"));
    assertEquals("stages content default_scene", fileSystem.readTextFile("hello/production/stages.rb"));
    assertEquals("styles content", fileSystem.readTextFile("hello/production/styles.rb"));
    assertEquals("props content Default Scene", fileSystem.readTextFile("hello/production/default_scene/props.rb"));
    assertEquals("styles content default_scene", fileSystem.readTextFile("hello/production/default_scene/styles.rb"));
    assertEquals(true, fileSystem.exists("hello/production/default_scene/players"));
  }
}
