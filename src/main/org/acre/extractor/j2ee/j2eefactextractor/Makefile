ACREHOME=../..
####################### Variables Definition ##################
SEP=`sh -f $(ACREHOME)/separator`
#BASEDIR=`pwd | sed 's|/cygdrive/l|l:|g'`
#MODULES=$(BASEDIR)/../modules
#J=$(BASEDIR)/classes/jars
#params=-verbose
#BUILDNUM=`date  +%Y.%m.%d.%H.%M.%S`
#BUILDNUM=`date	+%y%m%d%H%M`
#BUILDSPEC=`cat BUILDSPEC`
#RM=rm
#COPY=`sh -f $(ACREHOME)/copycommand`
#COPYDRIVE=`sh -f $(ACREHOME)/copydrive`
#DOS2UNIX=`zsh -c 'which dos2unix' > /dev/null; if [ $$? -eq 0 ] ; then echo 'dos2unix'; else  echo $(JAVA_HOME)/bin/native2ascii; fi`
#HERE=../salsa
ANT_HOME=$(ACREHOME)/apache-ant-1.6.1
ANT=@$(ANT_HOME)/bin/ant
MYMAKE=$(MAKE) ANT='$(ANT)' JAVA_HOME=$(JAVA_HOME) IDE_HOME=$(IDE_HOME) SEP=$(SEP) all
#GROOVY_VERSION=groovy-1.0-beta-7
#GROOVY_HOME=$(ACREHOME)/$(GROOVY_VERSION)
#MYSQL_DRIVER_JAR=$(ACREHOME)/external/lib/mysql-connector-java-3.0.15-ga-bin.jar


ACRE_REPOSITORY_DIRNAME=AcreRepository
MODELDIR=$(ACREHOME)/src/main/$(ACRE_REPOSITORY_DIRNAME)/Global/FactModelRepository

alldb:	gensql fixsql createdb genconsts

clean:
	$(ANT) clean

compile:
	$(ANT) compile

rebuildpsa:
	rm -rf PSA/build PSA/assemble
	$(MAKE) buildpsa

buildpsa:
	cd $(ACREHOME)/PSA/src; ../../apache-ant-1.6.1/bin/ant jasper ear

test: buildpsa t

t:
	$(ANT) test
gensql:
	cd $(MODELDIR); "$(IDE_USER_HOME)/modules/bin/dasl" -createSalsaSchema J2EEApplication.bos
fixsql:
	cd $(MODELDIR); \
		rm -f J2EEApplication.sql.tmp; \
		cat J2EEApplication.sql \
			| sed -e 's/id INTEGER,/id INTEGER AUTO_INCREMENT,/g'	\
			| grep -v '_PRIMARY_KEY ON ' \
			> J2EEApplication.sql.tmp;	\
		mv J2EEApplication.sql.tmp J2EEApplication.sql

clobberdb:
	$(mysql) -D salsa -u salsa -psalsa -e "DROP database salsa; create database salsa"

createdb:
	$(mysql) -D salsa -u salsa -psalsa < $(MODELDIR)/Salsa_schema_mysql.sql
	$(mysql) -D salsa -u salsa -psalsa < $(MODELDIR)/J2EEApplication.sql
addfacts:
	$(mysql) -D salsa -u salsa -psalsa < j2eefacts.sql

genconsts:
	@rm -f src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@echo '// THIS FILE HAS BEEN AUTOMATICALLY GENERATED, DO NOT CHANGE IT BY HAND' > src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@echo 'package com.sun.acre.factextractor.j2ee;'                              >> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@echo 'public interface SQLTableConstants {'                                   >> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@cat $(MODELDIR)/J2EEApplication.sql | grep DROP | awk '{print "    public static final String", $$5, "=\"" $$5 "\";"}' \
		>> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@echo '    public static final String[] SQL_TABLES = {' >> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@cat $(MODELDIR)/J2EEApplication.sql | grep DROP | awk '{print "        " $$5 ","}' \
		>> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@echo '    };' >> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java
	@echo '}' >> src/com/sun/salsa/factextractor/j2ee/SQLTableConstants.java

.PHONY: test
