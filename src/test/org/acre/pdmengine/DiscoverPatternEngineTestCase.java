package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;
import org.acre.dao.PatternRepository;
import org.acre.dao.DAOFactory;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jan 5, 2005
 * Time: 7:02:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoverPatternEngineTestCase extends PatternEngineBaseTestCase {

    public void testSimpleHostPDM() {
        System.out.println("Discover calls relationship between BusinessDelegate and SessionFacade");
        System.out.println("-------------------------------------------------------");

        String [] pdms = new String[] {"BusinessDelegate", "SessionFacade"};

        PatternResult newPatternResult = engine.discoverPattern("newPDM", pdms, new String[]{"Calls"});
        System.out.println("Newly discovered PDM");
        System.out.println(newPatternResult.toString());

        PatternRepository facade = DAOFactory.getPatternRepository();
        facade.insertGlobalPatternModel(newPatternResult.getPdm());

        facade.save();
        System.out.println("-------------------------------------------------------");
    }

    public void testSimpleHostPDMWithGroovyScript() {
        System.out.println("Discover calls relationship between SessionFacade and SingletonGroovyPDM");

        String [] pdms = new String[] {"SessionFacade", "SingletonGroovyPDM"};

        PatternResult newPatternResult = engine.discoverPattern("SFCallsSingletonPDM", pdms, new String[]{"Calls"});
        System.out.println("Newly discovered PDM");
        System.out.println(newPatternResult.toString());

        PatternRepository facade = DAOFactory.getPatternRepository();
        facade.insertGlobalPatternModel(newPatternResult.getPdm());

        facade.save();
        System.out.println("-------------------------------------------------------");

    }

    public void testComplexHostPDM() {
        System.out.println("Discover calls, Implements relationship among Command, FC, BD, SF, Singleton");
        System.out.println("-------------------------------------------------------");
        String pdms[] = new String[] {"Command", "FrontController", "BusinessDelegate",
                                      "SessionFacade", "SingletonGroovyPDM" /*, "DataAccessObject"*/};
        String types[] = new String[]{"Calls", "Implements"};



        PatternResult newPatternResult = engine.discoverPattern("ComplexPDM", pdms, types);
        System.out.println("Newly discovered PDM");
        System.out.println(newPatternResult.toString());

        PatternRepository facade = DAOFactory.getPatternRepository();
        facade.insertGlobalPatternModel(newPatternResult.getPdm());

        facade.save();
        System.out.println("-------------------------------------------------------");

    }


    public void testPublicClassesInteractionPDM() {
        String pdms[] = new String[] {"authPCPDM", "ejbPCPDM", "corePCPDM",
                                      "delegatePCPDM", "daoPCPDM", "handlersPCPDM", "requestPCPDM", "handlerPCPDM",
                                      "helperPCPDM", "tagPCPDM", "utilPCPDM"};
        String types[] = new String[]{"Calls"};
        System.out.println("Discover calls among public classes");
        System.out.println("-------------------------------------------------------");

        PatternResult newPatternResult = engine.discoverPattern("PublicClassesInteractionPDM", pdms, types);
        System.out.println("Newly discovered PDM");
        System.out.println(newPatternResult.toString());

        PatternRepository facade = DAOFactory.getPatternRepository();
        facade.insertGlobalPatternModel(newPatternResult.getPdm());

        facade.save();
        System.out.println("-------------------------------------------------------");


    }
}
