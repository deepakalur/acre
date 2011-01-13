-- =========================== Auxiliary tables to calculate transitive closure =======================
DROP TABLE IF EXISTS stage1;
DROP TABLE IF EXISTS stage3;
DROP TABLE IF EXISTS stage2;

-- ========================== Salsa tables ==============================================================
DROP TABLE IF EXISTS Variables ;
DROP TABLE IF EXISTS WriteVariable ;
DROP TABLE IF EXISTS ReadVariable ;
DROP TABLE IF EXISTS SalsaCookie ;
DROP TABLE IF EXISTS SalsaProject ;
DROP TABLE IF EXISTS SalsaIdCounter;

-- =========================== Auxiliary tables to calculate transitive closure =======================
--CREATE TABLE stage1(a INTEGER NOT NULL, b INTEGER NOT NULL, PRIMARY KEY(a,b), KEY(a), KEY(b)) type=MyISAM;
--CREATE TABLE stage2(a INTEGER NOT NULL, b INTEGER NOT NULL, PRIMARY KEY(a,b), KEY(a), KEY(b)) type=MyISAM;
--CREATE TABLE stage3(a INTEGER NOT NULL, b INTEGER NOT NULL, PRIMARY KEY(a,b), KEY(a), KEY(b)) type=MyISAM;

CREATE TABLE SalsaIdCounter(counter INTEGER);
INSERT INTO SalsaIdCounter set counter = 1;

-- ========================== Salsa tables ==============================================================
CREATE TABLE SalsaProject(
        id              INTEGER AUTO_INCREMENT,
        system          VARCHAR(255),
        project         VARCHAR(255),
        version         VARCHAR(255),
        source          VARCHAR(255),
        description     VARCHAR(255),
        extraction_time TIMESTAMP,
        PRIMARY KEY ( id )
    ) type=MyISAM;

CREATE UNIQUE INDEX SalsaProject_PRIMARY_KEY ON SalsaProject(id);
CREATE INDEX SalsaProject_INDEX_KEY_IndexProject ON SalsaProject(project);
CREATE UNIQUE INDEX SalsaProject_UNIQUE_KEY_UniqueProjectVersion ON SalsaProject(project,version);
COMMIT;

CREATE TABLE SalsaCookie(
        id INTEGER AUTO_INCREMENT,
        PRIMARY KEY ( id )
    ) type=MyISAM;

CREATE UNIQUE INDEX SalsaCookie_PRIMARY_KEY ON SalsaCookie(id);
COMMIT;

CREATE TABLE WriteVariable(

        cookie VARCHAR(255),
        scope INTEGER,
        name VARCHAR(255),
        nodeId0 DECIMAL(20,0), nodeName0 VARCHAR(255),
        nodeId1 DECIMAL(20,0), nodeName1 VARCHAR(255),
        nodeId2 DECIMAL(20,0), nodeName2 VARCHAR(255),
        nodeId3 DECIMAL(20,0), nodeName3 VARCHAR(255),
        nodeId4 DECIMAL(20,0), nodeName4 VARCHAR(255),
        nodeId5 DECIMAL(20,0), nodeName5 VARCHAR(255),
        nodeId6 DECIMAL(20,0), nodeName6 VARCHAR(255),
        nodeId7 DECIMAL(20,0), nodeName7 VARCHAR(255),
        nodeId8 DECIMAL(20,0), nodeName8 VARCHAR(255),
        nodeId9 DECIMAL(20,0), nodeName9 VARCHAR(255),
        varIndex INTEGER
    ) type=MyISAM;

COMMIT;

CREATE TABLE ReadVariable(

        cookie VARCHAR(255),
        scope INTEGER,
        name VARCHAR(255),
        nodeId0 DECIMAL(20,0), nodeName0 VARCHAR(255),
        nodeId1 DECIMAL(20,0), nodeName1 VARCHAR(255),
        nodeId2 DECIMAL(20,0), nodeName2 VARCHAR(255),
        nodeId3 DECIMAL(20,0), nodeName3 VARCHAR(255),
        nodeId4 DECIMAL(20,0), nodeName4 VARCHAR(255),
        nodeId5 DECIMAL(20,0), nodeName5 VARCHAR(255),
        nodeId6 DECIMAL(20,0), nodeName6 VARCHAR(255),
        nodeId7 DECIMAL(20,0), nodeName7 VARCHAR(255),
        nodeId8 DECIMAL(20,0), nodeName8 VARCHAR(255),
        nodeId9 DECIMAL(20,0), nodeName9 VARCHAR(255),
        varIndex INTEGER
    ) type=MyISAM;

COMMIT;

