@echo off

rem Slurp the command line arguments.  This loop allows for an unlimited number of 
rem agruments (up to the command line limit, anyway).

set ANT_CMD_LINE_ARGS=
:setupArgs
if %1a==a goto doneArgs
set ANT_CMD_LINE_ARGS=%ANT_CMD_LINE_ARGS% %1
shift
goto setupArgs

:doneArgs
rem The doneArgs label is here just to provide a place for the argument list loop
rem to break out to.

:checkJava
if "%JAVACMD%" == "" set JAVACMD=java

set LOCALCLASSPATH=%CLASSPATH%
for %%i in ("..\lib\*.jar") do call "lcp.bat" "%%i"

%JAVACMD% -classpath ..\lib\antidote.jar;..\lib\crimson.jar;%LOCALCLASSPATH% -Dant.home="%ANT_HOME%" org.apache.tools.ant.gui.Main %ANT_CMD_LINE_ARGS%
goto end

:end
set LOCALCLASSPATH=
set LOCAL_ANT_HOME=
set ANT_CMD_LINE_ARGS=


