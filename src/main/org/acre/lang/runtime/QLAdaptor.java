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
package org.acre.lang.runtime;

import ca.uwaterloo.cs.ql.fb.*;
import ca.uwaterloo.cs.ql.interp.*;
import ca.uwaterloo.cs.ql.lib.FunctionLib;
import ca.uwaterloo.cs.ql.util.Timing;
import org.acre.lang.analyser.Expression;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLResultSetImpl;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.lang.pql.translator.Translate;
import org.acre.lang.ql.lib.object;
import org.acre.lang.runtime.lib.PQLRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Backend connector implementation for QL Engine
 * 
 * <br>
 * This class shows the usage of the readline wrapper. It will read lines
 * from standard input using the GNU-Readline library. You can use the
 * standard line editing keys. You can also define application specific
 * keys. Put this into your ~/.inputrc (or into whatever file $INPUTRC
 * points to) and see what happens if you press function keys F1 to F3:
 * <pre>
 * $if ReadlineTest
 * "\e[11~":    "linux is great"
 * "\e[12~":    "jikes is cool"
 * "\e[13~":    "javac is slow"
 * $endif
 * </pre>
 * <p/>
 * If one argument is given to ReadlineTest, a private initialization file
 * is read. If a second argument is given, the appropriate library is
 * loaded.
 * 
 * @author Yury Kamen, Syed Ali
 *
 */
public class QLAdaptor implements RuntimeAdapter {
    /**
     * This class shows the usage of the readline wrapper. It will read lines
     * from standard input using the GNU-Readline library. You can use the
     * standard line editing keys. You can also define application specific
     * keys. Put this into your ~/.inputrc (or into whatever file $INPUTRC
     * points to) and see what happens if you press function keys F1 to F3:
     * <pre>
     * $if ReadlineTest
     * "\e[11~":    "linux is great"
     * "\e[12~":    "jikes is cool"
     * "\e[13~":    "javac is slow"
     * $endif
     * </pre>
     * <p/>
     * If one argument is given to ReadlineTest, a private initialization file
     * is read. If a second argument is given, the appropriate library is
     * loaded.
     */
    private Env env;
    private Timing timing;
    private Interp interp;
    private StringBuffer buffer;
    private ByteArrayInputStream byteStream;

    private Value value;
    private ScriptUnitNode unit;
    private SyntaxTreeNode node;

    public QLAdaptor() {
        initEnv();
    }

    private void initEnv(String[] args) {
        env = new Env();

        File file = new File(args[0]);
        try {
            interp = new Interp(file);
            unit = interp.parse();
            if (unit == null) System.exit(1);
            env.setMainUnit(unit);
            env.pushScope(unit);

            Variable var;     // Add $# (number of script args).
            var = new Variable(unit, "$#", new Value(args.length - 1));
            unit.addVariable(var);

            // Add script args: $0 $1 $2 ...  // The $0 is the script file.
            for (int i = 0; i < args.length; i++) {
                var = new Variable(unit, "$" + i, new Value(args[i]));
                unit.addVariable(var);
            }

            timing = null;
            buffer = new StringBuffer();
        } catch (FileNotFoundException ex) {
            System.err.println("No such a file " + file + " exists");
            System.exit(1);
        }
    }

    private void initEnv() {
        env = new Env();
        unit = new ScriptUnitNode();
        env.setMainUnit(unit);
        env.pushScope(unit);

//        Interp.ReInit((InputStream)null);
        // TODO: Big problem: Recreate ReQL parser without static initialization
//        Interp.ReInit(System.in);
        interp = new Interp(); // Interp.instance();
        buffer = new StringBuffer();
        object.register(FunctionLib.instance());
    }

    public void resetScope() {
        env.popScope();
        env.pushScope(interp.parse());
    }

    public void pushScope() {
        Scope scope = env.peepScope();
        Scope subScope = new ScriptUnitNode(scope);
        env.pushScope(subScope);
    }

    public void popScope() {
        env.popScope();
        //return env.popScope();
    }

    public Object getProperty(String name) {
        Scope scp = env.peepScope();
        try {
            Variable x = scp.lookup(name);
            return x.getValue().objectValue();
        } catch (LookupException e) {
            return null;
        }

    }

    public static void main1(String[] args) {
        QLAdaptor r = new QLAdaptor();
        r.setProperty("x", new Integer(1));
        r.setProperty("y", new Integer(3));
        EdgeSet eSet = new EdgeSet("call");
        eSet.add("x", "y");
        eSet.add("y", "z");
        r.setProperty("call", eSet);
        Object y;
        y = r.evaluateNative("x + y");
        System.out.println("class=" + y.getClass().getName() + " value=" + y);
        y = r.evaluateNative("call");
        System.out.println("class=" + y.getClass().getName() + " value=" + y);
    }

    /**
     * Sets PQL/QL variable to a given value
     * @param name   variable name
     * @param value  variable value
     */
    public void setProperty(String name, Object value) {
        this.value = null;
        if (value instanceof Value) {
            this.value = (Value) value;
        } else if (value instanceof Integer) {
            this.value = new Value(((Integer) value).intValue());
        } else if (value instanceof Float) {
            this.value = new Value(((Float) value).floatValue());
        } else if (value instanceof Double) {
            this.value = new Value(((Double) value).floatValue());
        } else if (value instanceof Long) {
            this.value = new Value(((Long) value).intValue());
        } else if (value instanceof Boolean) {
            this.value = new Value(((Boolean) value).booleanValue());
        } else {
            this.value = new Value(value);
        }
        Scope scp = env.peepScope();
        Variable x = new Variable(scp, name, this.value);
        scp.addVariable(x);
    }

    public Object evaluateNative(String line) {
        try {
            byte[] bytes = line.getBytes();
            byteStream = new ByteArrayInputStream(bytes);
            interp.ReInit(byteStream);
            unit = interp.parse();
            try {
//                env.pushScope(unit);
                if (unit != null) 
                    value = unit.evaluate(env);
            } finally {
//                env.popScope();
            }
        } catch (TokenMgrError e) {
            env.out.println("Exception: " + e.getMessage());
            throw new PQLRuntimeException(e.getMessage(), e);
        } catch (EvaluationException e) {
            env.out.println("Exception: " + e.getMessage());
            throw new PQLRuntimeException(e.getMessage(), e);
        }
        return value.objectValue();
    }

    public Object evaluateOld(String line) {
        env.pushScope(unit);
        try {
            byte[] bytes = line.getBytes();
            byteStream = new ByteArrayInputStream(bytes);
            interp.ReInit(byteStream);
            try {
                node = interp.Expression();
//                node = interp.Statement();
            } catch (ParseException e) {
                byteStream.close();
                byteStream = new ByteArrayInputStream(bytes);
                interp.ReInit(byteStream);
                node = interp.Statement();
//                node = interp.Expression();
            }

            if (unit.isTimeOn()) {
                if (timing == null) timing = new Timing();
                timing.start();
            }

            // Do evaluation.
            value = node.evaluate(env);

            if (node instanceof ExpressionNode && unit.isEchoOn()) {
                value.print(env.out);
            }

            if (unit.isTimeOn()) {
                if (timing == null)
                    timing = new Timing();
                else {
                    timing.stop();
                    env.out.println("time:");
                    env.out.println("\t" + timing.getTime());
                }
            }

            if (unit.isEchoOn()) {
                env.out.println("echo:");
                env.out.println(node.toString());
            }

            byteStream.close();
            byteStream = null;
            buffer.delete(0, buffer.length());

        } catch (IOException e) {
            env.out.println("Exception: " + e.getMessage());
            throw new PQLRuntimeException(e.getMessage(), e);
        } catch (TokenMgrError e) {
            env.out.println("Exception: " + e.getMessage());
            throw new PQLRuntimeException(e.getMessage(), e);
        } catch (ParseException e) {
            env.out.println("Exception: unable to parse " + buffer);
            throw new PQLRuntimeException(e.getMessage(), e);
        } catch (EvaluationException e) {
            env.out.println("Exception: " + e.getMessage());
            throw new PQLRuntimeException(e.getMessage(), e);
        } finally {
            env.popScope();
        }
        return value.objectValue();
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public Interp getInterp() {
        return interp;
    }

    public void setInterp(Interp interp) {
        this.interp = interp;
    }

    public ScriptUnitNode getUnit() {
        return unit;
    }

    public void setUnit(ScriptUnitNode unit) {
        this.unit = unit;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public SyntaxTreeNode getNode() {
        return node;
    }

    public void setNode(SyntaxTreeNode node) {
        this.node = node;
    }

    public PQLValueHolder getPQLData(long nodeId, String type) {
        return getPQLData(nodeId, true);
    }
    
    public PQLValueHolder getPQLData(long nodeId, boolean fetchInverseRelationships) {
        try {
            EdgeSet instances = null;
            Scope scp = env.peepScope();
            Variable variable = scp.lookup("$INSTANCE");
            if (variable != null) {
                instances = (EdgeSet) (variable.getValue().objectValue());
            }
            return Helper.createValueHolder(nodeId, scp, instances, fetchInverseRelationships);
        } catch (LookupException e) {
            e.printStackTrace();
            throw new IllegalAccessError("$INSTANCE set not intialized: " + e.getMessage());
        }
    }

    public void complete(PQLValueHolder data) {
        complete(data, true);
    }
    
    public void complete(PQLValueHolder data, boolean fetchInverseRelationships) {
        try {
            EdgeSet instances = null;
            Scope scp = env.peepScope();
            Variable variable = scp.lookup("$INSTANCE");
            if (variable != null) {
                instances = (EdgeSet) (variable.getValue().objectValue());
            }
            Helper.populate(data, scp, instances, fetchInverseRelationships);
        } catch (LookupException e) {
            e.printStackTrace();
            throw new IllegalAccessError("$INSTANCE set not intialized: " + e.getMessage());
        }
    }

    public PQLResultSet getVariable(Expression variable) {
        String name = variable.getName();
        //Expression variable = scriptModel.getVariable(name);
        if (variable == null) {
            throw new IllegalArgumentException("Variable " + name + " is not found");
        }
        Object pqlVariable = getProperty(name);
        PQLResultSetImpl result = null;
        if (variable.isPrimitive()) {
            if (pqlVariable instanceof NodeSet) {
                Node[] rows = ((NodeSet) pqlVariable).getAllNodes();
                if (rows != null) {
                    result = PQLResultSetImpl.create(variable, pqlVariable);
                    result.setIntervalValue(pqlVariable);
                    result.populate(rows);
                }
                return result;
            } else
                throw new IllegalArgumentException("Variable " + name + " is not of expected type (NodeSet)");
                
        } else {
            Object pqlObject = evaluateNative(getObjectFunction(variable));
            if (pqlObject instanceof List) {
                List rows = (List) pqlObject;
                result = PQLResultSetImpl.create(variable, pqlVariable);
                result.setIntervalValue(pqlVariable);
                result.populate(rows);
                return result;
            } else
                throw new IllegalArgumentException("Variable " + name + "("+pqlVariable.getClass()+") is not of expected List type");
        }
    }

    private String getObjectFunction(Expression variable) {
        String columns = "";
        if (variable.isStruct()) {
            boolean required = false;
            Expression[] columnTypes = variable.getStructType();
            for (int i = 0; i < columnTypes.length; i++) {
                if (!columnTypes[i].isPrimitive()) {
                    if (columns == "")
                        columns += i;
                    else 
                        columns += ","+i;
                } else {
                    required = true;
                }
            }
            if (required) {
                columns = ", {"+columns+"}";
            } else {
                columns = "";
            }
        }
        return "return object("+variable.getName()+columns+")";
    }

    private String translate(ScriptModel model, String line) {
        try {
            return Translate.translate(model, line);
        } catch (Exception e) {
            throw new RuntimeException("Exception while translating: " + line, e);
        }
        
    }

    public Object evaluatePQL(ScriptModel model, String line) {
        String pqlString = translate(model, line).trim();
//        System.out.println("QLAdaptor->pqlString = " + pqlString + "#");
        return evaluateNative(pqlString);
    }

    public void connect() {
    }

    public void disconnect() {
    }


}
