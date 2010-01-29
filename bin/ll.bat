set LIMELIGHT_HOME=Z:\Projects\limelight

java -cp out\production\Limelight;lib\jruby-complete-1.4.0.jar %JAVA_OPTS% -Dlimelight.home=%LIMELIGHT_HOME% limelight.Main %1 %2 %3 %4 %5


