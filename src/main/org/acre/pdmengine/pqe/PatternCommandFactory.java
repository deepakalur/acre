/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.pdmengine.pqe;

import org.acre.dao.PDMXMLConstants;
import org.acre.pdmengine.PatternEngineException;

import java.util.HashMap;
import java.util.Map;

public class PatternCommandFactory {

    private static PatternCommandFactory ourInstance = new PatternCommandFactory();

    public static PatternCommandFactory getInstance() {
        return ourInstance;
    }

    public PatternBaseCommand getPDMRelationalOperator(String relationType) {
        Class relationOpClass = (Class) relOpTypeMap.get(relationType.toLowerCase());
        try {
            return (PatternBaseCommand) relationOpClass.newInstance();
        } catch (Throwable e) {
            throw new PatternEngineException("Invalid Relationship type specified in PDM - "+ relationType, e);
        }
    }
    private PatternCommandFactory() {
        initialize();
    }

    private Map relOpTypeMap = new HashMap();
    private void initialize() {
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_CALLS.toLowerCase(),PatternBaseCommand.CallsCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_CATCHES.toLowerCase(),PatternBaseCommand.CatchesCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_CONTAINS.toLowerCase(),PatternBaseCommand.ContainsCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_CREATES.toLowerCase(), PatternBaseCommand.CreatesCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_EXTENDS.toLowerCase(), PatternBaseCommand.ExtendsCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_IMPLEMENTS.toLowerCase(), PatternBaseCommand.ImplementsCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_INSTANTIATES.toLowerCase(), PatternBaseCommand.CreatesCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_ISOFTYPE.toLowerCase(), PatternBaseCommand.IsOfTypeCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_THROWS.toLowerCase(), PatternBaseCommand.ThrowsCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_USES.toLowerCase(), PatternBaseCommand.UsesCommand.class);
            relOpTypeMap.put(PDMXMLConstants.RELATIONSHIP_TYPE_ABSCO.toLowerCase(), AbscoCommand.class);
     }

}
