set ACRE_HOME=.

set ANT_OPTS=-Xmx250m
ant -f runfact.xml -Dprojectname=%1 -Dprojectversion=%2 -Dappl.dist=src -lib "./classes;./lib/mysql-connector-java-3.0.14-production-bin.jar"

