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
package org.acre.lang.analyser;

import org.acre.dao.DAOFactory;
import org.acre.dao.PDMException;
import org.acre.lang.TreeBuilder;
import org.acre.lang.analysis.DepthFirstAdapter;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.node.*;
import org.acre.lang.parser.ParserException;
import org.acre.model.metamodel.FactMetaModel;

import java.io.IOException;

/**
 * Semantic analyser for preQL script
 *
 * @author Syed Ali
 */
public class ScriptWalker extends DepthFirstAdapter {

    private static final int MODE_NONE = 0;
    private static final int MODE_EXPRESSION = 1;
    private static final int MODE_QUERY = 2;

    private int queryLevel;
    private ScriptModel model;
    private QueryAnalyser currentAnalyser = null;
    private int analysisMode = MODE_NONE;

    public ScriptWalker() {
        this(new ScriptModel(), false);
    }

    public ScriptWalker(boolean targetQL) {
        this(null, targetQL);
        //env should be defaulted only if model is not initialized
        model.setDefaultEnvironmentVariableName();
    }

    public ScriptWalker(ScriptModel model, boolean targetQL) {
        if (model == null) 
            model = new ScriptModel();
        this.model = model;
        model.setTargetSQL(targetQL);
    }

/*
    public void inAOldImportFromClause(AOldImportFromClause node) {
        if (analysisMode == MODE_NONE) {
            String path = node.getQualifiedName().toString();
            path = path.replace('_', '/');
            path = path.replaceAll(" ", "");

            model.addCommand(Translate.getImport(path));
            for (int i = 0; i < Translate.PREQL_PROLOGUE.length; i++) {
                model.addCommand(Translate.PREQL_PROLOGUE[i]);
            }
        } else {
            QueryAnalyser.notYetImplemented("ScriptWalker.inAQuery", node);
        }
    }
*/
    public void inASimpleIncludeClause(ASimpleIncludeClause node) {
        //AQualifiedName AQualifiedName = (AQualifiedName) node.getFromIdentifier();
        String pqlFileName = node.getQualifiedName().toString().replaceAll(" ", "");
        processImport(pqlFileName);
    }
    /*
    public void inASingleImportFromClause(ASingleImportFromClause node) {
        String pqlFileName = node.getFromIdentifier().toString().replaceAll(" ", "");
        processImport(pqlFileName);
    }
    public void inAMultipleImportFromClause(AMultipleImportFromClause node) {
        String pqlFileName = node.getFromIdentifier().toString().replaceAll(" ", "");
        processImport(pqlFileName);
    }
    */
    protected void processImport(String scriptName) {
        try {
            String iScript = getImportedScript(scriptName);
            Node iAST = TreeBuilder.getNode(iScript, true);
            //pass in the existing model so that the commands are added to the current model
            model.pushInclude();
            ScriptWalker iWalker = new ScriptWalker(model, model.isTargetSQL());
            iAST.apply(iWalker);
            model.popInclude();
            //extract info from scriptmodel
            // not need to import as everything as added to the existing model.
            //model.importScript(iWalker.getModel());
        } catch (IOException e) {
            throw new PDMException("IOException in imported script: "+scriptName, e);
        } catch (LexerException e) {
            throw new PDMException("LexerException in imported script: "+scriptName, e);
        } catch (ParserException e) {
            throw new PDMException("ParserException in imported script: "+scriptName, e);
        }
    }

    /**
     * @return
     */
    private String getImportedScript(String scriptName) {
        String iScript = DAOFactory.getPatternQueryRepository().getGlobalQueryFile(scriptName);
        return iScript;
    }

    /**
     * Callback for select clause
     * */
    public void inAQuery(AQuery node) {
        PExpr expr = node.getExpr();
        if (expr instanceof ASelectExpr) {
            if (analysisMode == MODE_NONE) {
                analysisMode = MODE_QUERY;
                currentAnalyser = new QueryAnalyser(model);
            }
            queryLevel++;
        } else {
            if (analysisMode == MODE_NONE) {
                analysisMode = MODE_EXPRESSION;
                queryLevel++;
                handleExpression(expr);
            }
        }
    }
    protected void handleExpression(PExpr expr) {
        Expression expression = null;
        if (false) {
        } else if (expr instanceof AAdditionExpr) {
            AAdditionExpr aExpr = (AAdditionExpr) expr;
            expression = model.parseExpression(aExpr.getLeft(), aExpr.getRight(), aExpr.getPlus().getText());
        } else if (expr instanceof ASubstractionExpr) {
            ASubstractionExpr aExpr = (ASubstractionExpr) expr;
            expression = model.parseExpression(aExpr.getLeft(), aExpr.getRight(), aExpr.getMinus().getText());
        } else if (expr instanceof AIntersectionExpr) {
            AIntersectionExpr aExpr = (AIntersectionExpr) expr;
            expression = model.parseExpression(aExpr.getLeft(), aExpr.getRight(), aExpr.getSetAnd().getText());
        } else if (expr instanceof ANestedExpr) {
            ANestedExpr aExpr = (ANestedExpr) expr;
            handleExpression(((AQuery) aExpr.getQuery()).getExpr());
        }
        if (!model.isTargetSQL()) {
            model.addCommand(expr.toString());
        } else if (expression != null) {
            model.addCommand(expression.getQuery());
        }
    }

/**
     * Callback for select clause
     * */
    public void outAQuery(AQuery node) {
        if (analysisMode == MODE_NONE) {
            return;
        }
        PExpr expr = node.getExpr();
        if (analysisMode == MODE_QUERY && expr instanceof ASelectExpr) {
            if (queryLevel == 1) {
                model.addCommand(currentAnalyser.getQuery());
                currentAnalyser = null;
                analysisMode = MODE_NONE;
            }
            queryLevel--;
        } else if (analysisMode == MODE_EXPRESSION) {
            if (queryLevel == 1) {
                analysisMode = MODE_NONE;
            }
            queryLevel--;
        }
    }

    public void inAIdentifierQuery(AIdentifierQuery node) {
        if (!model.isTargetSQL()) {
            if (analysisMode == MODE_NONE) {
                model.addCommand(node.toString());
            } else {
                QueryAnalyser.notYetImplemented("ScriptWalker.inAQuery", node);
            }
        }
    }


    /**
     * Callback for select clause
     * */
    public void inAListProjectionAttributes(AListProjectionAttributes node) {
        if (analysisMode == MODE_NONE || queryLevel > 1) {
            return;
        }
        //Remember selectClause until from and where clause have been analysed
        currentAnalyser.setSelectClause(node);
    }

    /**
     * Callback for where clause
     * */
    public void inAWhereClause(AWhereClause node) {
        if (analysisMode == MODE_NONE || queryLevel > 1) {
            return;
        }
        currentAnalyser.processWhereClause(node);
    }


    /**
     * Callback for from clause
     * */
    public void inAFromClause(AFromClause node) {
        if (analysisMode == MODE_NONE || queryLevel > 1) {
            return;
        }
        super.inAFromClause(node);
        currentAnalyser.processFromClauseList(node.getFromClauseList());
    
        //process select clause from clause has been processed. 
        currentAnalyser.processSelectClause();
    }

    public void inASortCriterion(ASortCriterion node) {
        if (analysisMode == MODE_NONE || queryLevel > 1) {
            return;
        }
        boolean descending = node.getSortCriterionT() != null 
            && node.getSortCriterionT().getClass() == ADescSortCriterionT.class;
        currentAnalyser.processOrderByExpression(node.getExpr(), descending);
    }
    
    public void inADefineQuery(ADefineQuery node) {
        String varName = node.getIdentifier().getText();
        if (varName != null && varName.length() > 0) {
            if (varName.charAt(0) == '@') {
                varName = varName.substring(1);
                if (node.getQuery() instanceof AIdentifierQuery) {
                    AIdentifierQuery identifier = (AIdentifierQuery) node.getQuery();
                    String envVarName = ScriptModel.getEnvironmentVariableColumnName(varName);
                    String envVarValue = identifier.getIdentifier().getText();
                    if (envVarName == null) {
                        QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.ENVIRONMENT_VARIABLE_NOT_FOUND") 
                                + " for variable :" + envVarName, node);
                    }
                    if (!"null".equalsIgnoreCase(envVarValue)) {
                        model.addEnvironmentVariable(envVarName, envVarValue);
                    } else {
                        model.removeEnvironmentVariable(envVarName);
                    }
                } else if (node.getQuery() instanceof AQuery) {
                    AQuery aQuery = (AQuery) node.getQuery();
                    if (aQuery.getExpr() instanceof ALiteralExpr) {
                        ALiteralExpr literal = (ALiteralExpr) aQuery.getExpr();
                        String envVarName = ScriptModel.getEnvironmentVariableColumnName(varName);
                        //StringBuffer envVarValue = new StringBuffer(((AStringLiteral) (literal.getLiteral())).getStringLiteral().getText());
                        String envVarValue = ((AStringLiteral) (literal.getLiteral())).getStringLiteral().getText();
                        int len = envVarValue.length();
                        if (len > 0) {
                            envVarValue = envVarValue.substring(1, len - 1);
                        } 
                        if (envVarName == null) {
                            QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.ENVIRONMENT_VARIABLE_NOT_FOUND") 
                                    + " for variable :" + envVarName, node);
                        }
                        model.addEnvironmentVariable(envVarName, envVarValue);
                    } else {
                        QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.INVALID_ENVIRONMENT_VARIABLE_Value") 
                                + " :" + node.getQuery().toString(), node);
                    }
                } else {
                    QueryAnalyser.notYetImplemented("ScriptWalker.inADefineQuery", node.getQuery());
                }
            } else if (varName.charAt(0) == ':') {
                QueryAnalyser.notYetImplemented("ScriptWalker.inADefineQuery", node);
            } else {
//                System.out.println("inADefineQuery: simple var " + varName);
            }
        } else {
            QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.INVALID_VARIABLE_NAME") + " :" + varName, node);
        }
    }
    
    public void outADefineQuery(ADefineQuery node) {
        String variableName = node.getIdentifier().getText().trim();
        if (variableName != null && variableName.length() > 0) {
            if (variableName.charAt(0) == '@' && variableName.charAt(0) == ':') {
                return;
            }
        }
        int last = model.getCommands().size() - 1;
        if (last >= 0) {
            if (variableName != null && FactMetaModel.getInstance().isMetaType(variableName)) {
                QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PRE_DEFINE_VARIABLE_OVERRIDE") + " for variable :" + variableName, node);
            }
            Object o = model.getCommands().get(last);
            if (o instanceof QueryModel) {
                QueryModel lastQuery = (QueryModel) o;
                lastQuery.setAssignVariable(model.createVariable(variableName, lastQuery.getSelectExpressions()));
            } else if (o instanceof String) {
                String command = (String) model.getCommands().get(last);
                model.getCommands().set(last, variableName + " = " + command);
                model.createVariable(variableName, command);
            }
        }
    }


    public void outAReturnQuery(AReturnQuery node) {
        if (node.getQuery() instanceof AIdentifierQuery) {
            AIdentifierQuery aIdentifierQuery = (AIdentifierQuery) node.getQuery();
            returnVariable(aIdentifierQuery.getIdentifier());
        } else {
            int last = model.getCommands().size() - 1;
            if (last >= 0) {
                String variableName = ScriptModel.DEFAULT_RETURN_VARIABLE;
                if (FactMetaModel.getInstance().isMetaType(variableName)) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PRE_DEFINE_VARIABLE_OVERRIDE") + " for variable :" + variableName, node);
                }
                Object o = model.getCommands().get(last);
                if (o instanceof QueryModel) {
                    QueryModel lastQuery = (QueryModel) o;
                    lastQuery.setAssignVariable(model.createVariable(variableName, lastQuery.getSelectExpressions()));
                    model.addReturnVariable(variableName);
                } else if (o instanceof String) {
                    String command = (String) model.getCommands().get(last);
                    model.getCommands().set(last, variableName + " = " + command);
                    model.createVariable(variableName, command);
                    model.addReturnVariable(variableName);
                }
            }
        }
    }

    public void inAMultipleTupleList(AMultipleTupleList node) {
        returnVariable(node.getIdentifier());
        if (node.getIdentifierList() != null) {
            returnVariable(node.getIdentifierList());
        }
    }
    public void returnVariable(TIdentifier node) {
        String variableName = node.getText();
        model.addReturnVariable(variableName);
    }
    public void returnVariable(PIdentifierList node) {
        if (node instanceof AIdentifierList) {
            AIdentifierList aIdentifierList = (AIdentifierList) node;
            returnVariable(aIdentifierList.getIdentifier());
            if (aIdentifierList.getIdentifierList() != null)
                returnVariable(aIdentifierList.getIdentifierList());
        } else if (node instanceof ASingleIdentifierList) {
            ASingleIdentifierList aSingleIdentifierList = (ASingleIdentifierList) node;
            returnVariable(aSingleIdentifierList.getIdentifier());
        }

    }

    public ScriptModel getModel() {
        return model;
    }

}
