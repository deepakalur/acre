#!/bin/sh
# READ ReadMe.CreateFactDB.txt first

ACREHOME=.
mysql -usalsa -psalsa salsa < $ACREHOME/src/main/AcreRepository/Global/FactModelRepository/ACRE_SCHEMA.SQL
mysql -usalsa -psalsa salsa < $ACREHOME/src/main/AcreRepository/Global/FactModelRepository/ACRE_JAVA_SCHEMA_MYSQL.SQL
