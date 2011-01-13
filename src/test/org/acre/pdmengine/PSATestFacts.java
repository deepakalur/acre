package org.acre.pdmengine;

/**
 *
 * Copyright (c) 2004
 * Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @author rajmohan@Sun.com
 * @version Nov 29, 2004 10:55:03 AM
 */
public class PSATestFacts {
    static public final String BDCallsSF[][] = new String[][]
    {
        {"com.sun.sjc.psa.delegate.CommitmentDelegate", "com.sun.sjc.psa.ejb.CommitmentFacade"},
        {"com.sun.sjc.psa.delegate.CustomerDelegate", "com.sun.sjc.psa.ejb.CustomerFacade"},
        {"com.sun.sjc.psa.delegate.EmployeeDelegate", "com.sun.sjc.psa.ejb.EmployeeFacade"},
        {"com.sun.sjc.psa.delegate.ProjectDelegate", "com.sun.sjc.psa.ejb.ProjectFacade"},
        {"com.sun.sjc.psa.delegate.ResourceDelegate", "com.sun.sjc.psa.ejb.ResourceFacade"},
        {"com.sun.sjc.psa.delegate.SearchDelegate", "com.sun.sjc.psa.ejb.SearchFacade"},
        {"com.sun.sjc.psa.delegate.SkillDelegate", "com.sun.sjc.psa.ejb.SkillFacade"},
        {"com.sun.sjc.psa.delegate.SystemDelegate", "com.sun.sjc.psa.ejb.SystemFacade"}
    };

    static public final String IFCallsFC[][] = new String[][]
    {
        {"com.sun.sjc.psa.request.PSAFilter", "com.sun.sjc.psa.request.PSAServlet"}
    };

    static public final String CommandCallsBD[][] = new String[][]
    {
        {"com.sun.sjc.psa.request.helper.CustomerCRUDHelper","com.sun.sjc.psa.delegate.CustomerDelegate"},
        {"com.sun.sjc.psa.request.helper.EmployeeCRUDHelper","com.sun.sjc.psa.delegate.EmployeeDelegate"},
        {"com.sun.sjc.psa.request.helper.EmployeeCRUDHelper","com.sun.sjc.psa.delegate.ResourceDelegate"},
        {"com.sun.sjc.psa.request.helper.FindCustomerHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.FindEmployeeHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.FindProjectHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.FindResourceHelper", "com.sun.sjc.psa.delegate.ResourceDelegate"},
        {"com.sun.sjc.psa.request.helper.FindResourceHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.ProjectCRUDHelper", "com.sun.sjc.psa.delegate.ProjectDelegate"},
        {"com.sun.sjc.psa.request.helper.ProjectMgrProjectsHelper", "com.sun.sjc.psa.delegate.ProjectDelegate"},
        {"com.sun.sjc.psa.request.helper.ProjectMgrProjectsHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.ProjectMgrStaffingHelper", "com.sun.sjc.psa.delegate.CommitmentDelegate"},
        {"com.sun.sjc.psa.request.helper.ProjectMgrStaffingHelper", "com.sun.sjc.psa.delegate.ProjectDelegate"},
        {"com.sun.sjc.psa.request.helper.ResourceBlockoutsHelper", "com.sun.sjc.psa.delegate.ResourceDelegate"},
        {"com.sun.sjc.psa.request.helper.ResourceCommitmentsHelper", "com.sun.sjc.psa.delegate.CommitmentDelegate"},
        {"com.sun.sjc.psa.request.helper.ResourceMgrCommitmentsHelper", "com.sun.sjc.psa.delegate.CommitmentDelegate"},
        {"com.sun.sjc.psa.request.helper.ResourceMgrCommitmentsHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.ResourceMgrResourcesHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.ResourceSkillsHelper", "com.sun.sjc.psa.delegate.ResourceDelegate"},
        {"com.sun.sjc.psa.request.helper.SkillsCRUDHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.SkillsCRUDHelper", "com.sun.sjc.psa.delegate.SkillDelegate"},
        {"com.sun.sjc.psa.request.helper.StaffResourceHelper", "com.sun.sjc.psa.delegate.ProjectDelegate"},
        {"com.sun.sjc.psa.request.helper.StaffResourceHelper", "com.sun.sjc.psa.delegate.ResourceDelegate"},
        {"com.sun.sjc.psa.request.helper.StaffResourceHelper", "com.sun.sjc.psa.delegate.SearchDelegate"},
        {"com.sun.sjc.psa.request.helper.SubmitChangeRequestHelper", "com.sun.sjc.psa.delegate.SystemDelegate"},
    };

    static public final String FCCallsMapper[][] = new String[][]
    {
        {"com.sun.sjc.psa.request.PSAServlet", "com.sun.sjc.psa.request.ViewMapManager"}
    };

    static public final String MapperUsesMap[][] = new String[][]
    {
        {"com.sun.sjc.psa.request.ViewMapManager", "com.sun.sjc.psa.request.ViewMap"}
    };


    static public final String SFHomeCreatesSFInterface[][] = new String[][]
    {
        {"com.sun.sjc.psa.ejb.CommitmentFacadeHome", "com.sun.sjc.psa.ejb.CommitmentFacade"},
        {"com.sun.sjc.psa.ejb.CommitmentHome", "com.sun.sjc.psa.ejb.Commitment"},
        {"com.sun.sjc.psa.ejb.CustomerFacadeHome", "com.sun.sjc.psa.ejb.CustomerFacade"},
        {"com.sun.sjc.psa.ejb.CustomerHome", "com.sun.sjc.psa.ejb.Customer"},
        {"com.sun.sjc.psa.ejb.EmployeeFacadeHome", "com.sun.sjc.psa.ejb.EmployeeFacade"},
        {"com.sun.sjc.psa.ejb.EmployeeHome", "com.sun.sjc.psa.ejb.Employee"},
        {"com.sun.sjc.psa.ejb.ProjectFacadeHome", "com.sun.sjc.psa.ejb.ProjectFacade"},
        {"com.sun.sjc.psa.ejb.ProjectHome", "com.sun.sjc.psa.ejb.Project"},
        {"com.sun.sjc.psa.ejb.ResourceFacadeHome", "com.sun.sjc.psa.ejb.ResourceFacade"},
        {"com.sun.sjc.psa.ejb.ResourceHome", "com.sun.sjc.psa.ejb.Resource"},
        {"com.sun.sjc.psa.ejb.SearchFacadeHome", "com.sun.sjc.psa.ejb.SearchFacade"},
        {"com.sun.sjc.psa.ejb.SkillFacadeHome", "com.sun.sjc.psa.ejb.SkillFacade"},
        {"com.sun.sjc.psa.ejb.SkillHome", "com.sun.sjc.psa.ejb.Skill"},
        {"com.sun.sjc.psa.ejb.SystemFacadeHome", "com.sun.sjc.psa.ejb.SystemFacade"},
        {"com.sun.sjc.psa.ejb.CommitmentLocalHome", "com.sun.sjc.psa.ejb.CommitmentLocal"},
        {"com.sun.sjc.psa.ejb.CustomerLocalHome", "com.sun.sjc.psa.ejb.CustomerLocal"},
        {"com.sun.sjc.psa.ejb.EmployeeLocalHome", "com.sun.sjc.psa.ejb.EmployeeLocal"},
        {"com.sun.sjc.psa.ejb.ProjectLocalHome", "com.sun.sjc.psa.ejb.ProjectLocal"},
        {"com.sun.sjc.psa.ejb.ResourceLocalHome", "com.sun.sjc.psa.ejb.ResourceLocal"},
        {"com.sun.sjc.psa.ejb.SkillLocalHome", "com.sun.sjc.psa.ejb.SkillLocal"}
    };

    static public final String FCCallsFC[][] = new String[][]
    {
        {"com.sun.sjc.psa.request.PSAServlet", "com.sun.sjc.psa.request.PSAServlet"}
    };

    static public final String RelcoOfBDCallsSF[][] = new String[][]
    {
        {"com.sun.sjc.psa.ejb.CommitmentFacadeSession", "com.sun.sjc.psa.ejb.CommitmentLocal"},
        {"com.sun.sjc.psa.ejb.CustomerFacadeSession", "com.sun.sjc.psa.ejb.CustomerLocal"},
        {"com.sun.sjc.psa.ejb.EmployeeFacadeSession", "com.sun.sjc.psa.ejb.EmployeeLocal"},
        {"com.sun.sjc.psa.ejb.ProjectFacadeSession", "com.sun.sjc.psa.ejb.ProjectLocal"},
        {"com.sun.sjc.psa.ejb.ResourceFacadeSession", "com.sun.sjc.psa.ejb.ResourceLocal"},
        {"com.sun.sjc.psa.ejb.SkillFacadeSession", "com.sun.sjc.psa.ejb.SkillLocal"}
    };

    static public final String RelcoOfFCCreatesPSAException[][] = new String[][]
    {
        {"com.sun.sjc.psa.dao.DAOFactory", "com.sun.sjc.psa.core.PSAException"},
        {"com.sun.sjc.psa.delegate.SearchDelegate", "com.sun.sjc.psa.core.PSAException"},
        {"com.sun.sjc.psa.handlers.ScrollingValueListHandler", "com.sun.sjc.psa.core.PSAException"},
        {"com.sun.sjc.psa.request.BDFactory", "com.sun.sjc.psa.core.PSAException"},
        {"com.sun.sjc.psa.request.handler.DefaultFormBeanHandler", "com.sun.sjc.psa.core.PSAException"},
        {"com.sun.sjc.psa.request.helper.PsaHelper", "com.sun.sjc.psa.core.PSAException"},
        {"com.sun.sjc.psa.request.helper.PagingHelper", "com.sun.sjc.psa.core.PSAException"},
    };

    static public final String RelcoOfNullImplementsSFInterfaces[][] = new String[][]
    {
        {"com.sun.sjc.psa.ejb.CommitmentFacadeSession", "com.sun.sjc.psa.ejb.CommitmentFacade"},
        {"com.sun.sjc.psa.ejb.CustomerFacadeSession", "com.sun.sjc.psa.ejb.CustomerFacade"},
        {"com.sun.sjc.psa.ejb.ResourceFacadeSession" , "com.sun.sjc.psa.ejb.ResourceFacade"},
    };

    static public final String RelcoIsOfType[][] =  new String[][]
    {
        {"com.sun.sjc.psa.ejb.EmployeeFacadeSession", "com.sun.sjc.psa.ejb.EmployeeLocal"},
        {"com.sun.sjc.psa.ejb.EmployeeLocalHome", "com.sun.sjc.psa.ejb.EmployeeLocal"}
    } ;

    static public final String RelcoUses[][] =  new String[][]
    {
        {"com.sun.sjc.psa.auth.Auth", "com.sun.sjc.psa.util.ObjectPool"},
        {"com.sun.sjc.psa.dao.DAOFactory", "com.sun.sjc.psa.util.ObjectPool"},
        {"com.sun.sjc.psa.request.WebCache", "com.sun.sjc.psa.util.ObjectPool"},
        {"com.sun.sjc.psa.validate.ValidatorFactory", "com.sun.sjc.psa.util.ObjectPool"}
    };

    static public final String InvRelcoOfBDCallsSF[][] = new String[][]
    {
/*
            {"com.sun.sjc.psa.delegate.CommitmentDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.CustomerDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.EmployeeDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.ProjectDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.ResourceDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.SearchDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.SkillDelegate", "java.lang.Class"},
            {"com.sun.sjc.psa.delegate.SystemDelegate", "java.lang.Class"}
    {"com.sun.sjc.psa.delegate.SearchDelegate", "com.sun.sjc.psa.delegate.SearchDelegate"},
    {"com.sun.sjc.psa.delegate.CommitmentDelegate", "com.sun.sjc.psa.ejb.CommitmentFacadeHome"},
    {"com.sun.sjc.psa.delegate.CustomerDelegate", "com.sun.sjc.psa.ejb.CustomerFacadeHome"},
    {"com.sun.sjc.psa.delegate.EmployeeDelegate", "com.sun.sjc.psa.ejb.EmployeeFacadeHome"},
    {"com.sun.sjc.psa.delegate.ProjectDelegate", "com.sun.sjc.psa.ejb.ProjectFacadeHome"},
    {"com.sun.sjc.psa.delegate.ResourceDelegate", "com.sun.sjc.psa.ejb.ResourceFacadeHome"},
    {"com.sun.sjc.psa.delegate.SearchDelegate", "com.sun.sjc.psa.ejb.SearchFacadeHome"},
    {"com.sun.sjc.psa.delegate.SkillDelegate", "com.sun.sjc.psa.ejb.SkillFacadeHome"},
    {"com.sun.sjc.psa.delegate.SystemDelegate", "com.sun.sjc.psa.ejb.SystemFacadeHome"},
    {"com.sun.sjc.psa.delegate.CommitmentDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.CustomerDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.EmployeeDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.ProjectDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.ResourceDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.SearchDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.SkillDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.SystemDelegate", "com.sun.sjc.psa.util.PSAServiceLocator"},
    {"com.sun.sjc.psa.delegate.SystemDelegate", "java.lang.Throwable"},    
    {"com.sun.sjc.psa.delegate.CommitmentDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.CustomerDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.EmployeeDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.ProjectDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.ResourceDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.SearchDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.SkillDelegate", "javax.rmi.PortableRemoteObject"},
    {"com.sun.sjc.psa.delegate.SystemDelegate", "javax.rmi.PortableRemoteObject"}
    **/
    };
    public static String[][] RelcoExtends = new String[][] {
        {"com.sun.sjc.psa.core.EmployeeTypes", "com.sun.sjc.psa.core.TypeCodes"},
        {"com.sun.sjc.psa.core.PositionStatus", "com.sun.sjc.psa.core.TypeCodes"}
    };

    public static String[][] RelcoContains = new String[][] {
        {"com.sun.sjc.psa.dao.SkillDAO", "com.sun.sjc.psa.dao.SkillDAO.SkillAR"}
    };

    public static String[][] RelcoThrows = new String[][] {
        {"com.sun.sjc.psa.auth.Auth", "com.sun.sjc.psa.core.AuthorizationException"},
        {"com.sun.sjc.psa.ejb.CommitmentFacadeSession", "com.sun.sjc.psa.core.AuthorizationException"}
    };

    public static String[][] RelcoCatches = new String[][] {
        {"com.sun.sjc.psa.ejb.CommitmentFacadeSession", "com.sun.sjc.psa.core.AuthorizationException"}
    };
}
