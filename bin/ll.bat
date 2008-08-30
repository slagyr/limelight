set LIMELIGHT_HOME=Y:\Projects\limelight

java -cp lib\limelight.jar;jruby\lib\jruby.jar %JAVA_OPTS% -Dlimelight.home=%LIMELIGHT_HOME% limelight.Main %1 %2 %3 %4 %5


