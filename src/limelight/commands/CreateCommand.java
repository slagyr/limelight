//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.commands;

import limelight.About;
import limelight.Context;
import limelight.io.FileSystem;
import limelight.io.Templater;
import limelight.util.StringUtil;

import java.util.Map;

public class CreateCommand extends Command
{
  private static Arguments arguments;
  private Templater.TemplaterLogger logger;
  private FileSystem fs;

  public static Arguments arguments()
  {
    if(arguments == null)
    {
      arguments = new Arguments();
      arguments.addParameter("component", "The component can be: production (to create a new production), scene (to create a scene within a production), project (experimental - creates a limelight project)");
      arguments.addParameter("path", "The path or name of the component that will be created.  For example, if you choose to create a production and specify a path of 'hello', the production will be create in a directory named 'hello' in the current directory.");
      arguments.addValueOption("s", "scene-name", "name", "The name of the default scene when creating a production.  Defaults to 'default_scene'.");
      arguments.addValueOption("p", "production-path", "path", "Path to the production containing the scene to be created.  Defaults to '.'.");
      arguments.addValueOption("t", "test-path", "path", "Path to the directory containing the production's tests. Defaults to 'spec'.");
    }
    return arguments;
  }

  @Override
  public String description()
  {
    return "Creates the directories and file stubs for a production and/or scene.";
  }

  @Override
  public String name()
  {
    return "create";
  }

  @Override
  public Arguments getArguments()
  {
    return arguments();
  }

  @Override
  public void doExecute(Map<String, String> args)
  {
    String component = args.get("component").toLowerCase();
    if("scene".equals(component))
      createScene(args);
    else if("production".equals(component))
      createProduction(args);
    else if("project".equals(component))
      createProject(args);
    else
      sayError("I don't know how to create '" + component + "'s");
  }

  private void createProject(Map<String, String> args)
  {
    final String projectPath = args.get("path");
    String testsPath = fs.join(projectPath, getArgOrDefault(args, "test-path", "spec"));
    String sceneName = getArgOrDefault(args, "scene-name", "default_scene");

    String projectName = fs.filename(projectPath);
    Templater templater = createTemplater(fs.parentPath(projectPath));

    templater.addToken("LLP_NAME", projectName);

    templater.file(fs.join(projectName, "features/step_definitions/limelight_steps.rb"), "features/step_definitions/limelight_steps.rb.template");
    templater.file(fs.join(projectName, "features/support/env.rb"), "features/support/env.rb.template");
    templater.file(fs.join(projectName, "Rakefile"), "project/Rakefile.template");

    createProduction(templater, fs.join(projectName, "production"), projectName, sceneName, testsPath);
  }

  private void createProduction(Map<String, String> args)
  {
    String productionPath = args.get("path");
    String testsPath = fs.join(productionPath, getArgOrDefault(args, "test-path", "spec"));
    String sceneName = getArgOrDefault(args, "scene-name", "default_scene");

    Templater templater = createTemplater(fs.parentPath(productionPath));

    String productionName = fs.filename(productionPath);
    productionPath = productionName;
    createProduction(templater, productionPath, productionName, sceneName, testsPath);
  }

  private void createProduction(Templater templater, String productionPath, String productionName, String sceneName, String testsPath)
  {
    templater.addToken("DEFAULT_SCENE_NAME", sceneName);
    templater.addToken("PRODUCTION_NAME", StringUtil.titleCase(productionName));
    templater.addToken("CURRENT_VERSION", About.version.toString());

    templater.file(fs.join(productionPath, "production.rb"), "production/production.rb.template");
    templater.file(fs.join(productionPath, "stages.rb"), "production/stages.rb.template");
    templater.file(fs.join(productionPath, "styles.rb"), "production/styles.rb.template");
    templater.file(fs.join(testsPath, "spec_helper.rb"), "production/spec/spec_helper.rb.template");

    createScene(templater, fs.join(productionPath, sceneName), testsPath);
  }

  private void createScene(Map<String, String> args)
  {
    final String path = args.get("path");
    final String productionPath = getArgOrDefault(args, "production-path", ".");
    final String testsPath = getArgOrDefault(args, "test-path", "spec");

    Templater templater = createTemplater(productionPath);

    final String scenePath = fs.filename(path);
    createScene(templater, scenePath, testsPath);
  }

  private void createScene(Templater templater, String scenePath, String testsPath)
  {
    final String sceneName = fs.filename(scenePath);
    templater.addToken("SCENE_NAME", sceneName);
    templater.addToken("SCENE_TITLE", StringUtil.titleCase(sceneName));

    templater.file(fs.join(scenePath, "props.rb"), "scene/props.rb.template");
    templater.file(fs.join(scenePath, "styles.rb"), "scene/styles.rb.template");
    templater.directory(fs.join(scenePath, "players"));
    templater.file(fs.join(testsPath, sceneName, sceneName + "_spec.rb"), "scene_spec/scene_spec.rb.template");
  }

  private Templater createTemplater(String path)
  {
    Templater templater = new Templater(path, fs.join(Context.instance().limelightHome, "ruby", "lib", "limelight", "templates", "sources"));
    if(logger != null)
      templater.setLogger(logger);
    if(fs != null)
      templater.setFs(fs);
    return templater;
  }

  public void setTemplaterLoger(Templater.TemplaterLogger logger)
  {
    this.logger = logger;
  }

  public void setFileSystem(FileSystem fileSystem)
  {
    this.fs = fileSystem;
  }
}
