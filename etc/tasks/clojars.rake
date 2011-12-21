namespace "clojars" do

  POM_XML= <<POM
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>limelight</groupId>
    <artifactId>limelight</artifactId>
    <version>!-VERSION-!</version>

    <repositories>
        <repository>
            <id>clojars.org</id>
            <url>http://clojars.org/repo</url>
        </repository>
    </repositories>

    <build>
        <sourceDirectory> src/ </sourceDirectory>
        <testSourceDirectory> test/ </testSourceDirectory>
    </build>

</project>
POM

  desc "Generate pom file"
  task :pom do
    version = limelight_version
    xml = POM_XML.gsub("!-VERSION-!", version)
    File.open("pom.xml", "w") do |file|
      file.write xml
    end
  end

  desc "Push limelight.jar to clojars"
  task :push => %{pom} do
    run_command "scp pom.xml limelight.jar clojars@clojars.org:"
  end

end