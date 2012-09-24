/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.csu.kfs.fp.document.authorization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kfs.fp.document.authorization.FinancialProcessingAccountingLineAuthorizer;
import org.kuali.kfs.sys.businessobject.AccountingLine;
import org.kuali.kfs.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.web.AccountingLineRenderingContext;
import org.kuali.kfs.sys.document.web.RenderableAccountingLineContainer;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.bo.types.dto.AttributeSet;
import org.kuali.rice.kim.service.RoleManagementService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

import java.util.ArrayList;
import java.util.List;

public class InternalOrderAccountingLineAuthorizer extends FinancialProcessingAccountingLineAuthorizer {//FinancialSystemTransactionalDocumentAuthorizerBase {
    private static Log LOG = LogFactory.getLog(InternalOrderAccountingLineAuthorizer.class);

    static RoleManagementService roleManagementService = SpringContext.getBean(RoleManagementService.class);
    static String internalSupplierRoleId = roleManagementService.getRoleIdByName("KR-WKFLW", "InternalSupplier");
    static List<String> roleIds = new ArrayList<String>();

    @Override
    public boolean isGroupEditable(AccountingDocument accountingDocument, List<? extends AccountingLineRenderingContext> accountingLineRenderingContexts, Person currentUser) {
        // MJMC overiding isGroupEditable

        KualiWorkflowDocument workflowDocument = accountingDocument.getDocumentHeader().getWorkflowDocument();

        String accountingLineProperty = "";

        if (accountingLineRenderingContexts.toArray().length != 0)
            accountingLineProperty = ((RenderableAccountingLineContainer) accountingLineRenderingContexts.toArray()[0]).getAccountingLineProperty();


        // check if user is Internal Supplier; only make Source lines editable
        if (isCurrentUserSupplier(accountingDocument) && accountingLineProperty.equalsIgnoreCase("newSourceLine")) {
            return true;  // Internal Supplier should always have edit perms on non-final documents
        }

        if (workflowDocument.stateIsInitiated() || workflowDocument.stateIsSaved() || workflowDocument.stateIsEnroute() && accountingLineProperty.equalsIgnoreCase("newTargetLine")) {
            return workflowDocument.userIsInitiator(currentUser);
        }

        for (AccountingLineRenderingContext renderingContext : accountingLineRenderingContexts) {
            if (renderingContext.isEditableLine()) {
                return true;
            }
        }

        return false;

    }


    @Override
    public boolean renderNewLine(AccountingDocument accountingDocument, String accountingGroupProperty) {
        // MJMC overiding renderNewLine

        Person currentUser = GlobalVariables.getUserSession().getPerson();

        // display new lines if it is 'enroute' and user is supplier
        if (isCurrentUserSupplier(accountingDocument) && accountingGroupProperty.equalsIgnoreCase("document.sourceAccountingLines")) {
            return (accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsInitiated() || accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsSaved() || accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsEnroute());
        }

        if (accountingDocument.getDocumentHeader().getWorkflowDocument().userIsInitiator(currentUser) && accountingGroupProperty.equalsIgnoreCase("document.targetAccountingLines")) {
            return (accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsEnroute() || accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsInitiated() || accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsSaved());
        }

        if (isCurrentUserSupplier(accountingDocument) && accountingGroupProperty.contains("targetAccountingLine")) {
            return (accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsInitiated() || accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsSaved());//|| accountingDocument.getDocumentHeader().getWorkflowDocument().stateIsEnroute());
        } else {
            return super.renderNewLine(accountingDocument, accountingGroupProperty);

        }
    }


    @Override
     protected boolean approvedForUnqualifiedEditing(AccountingDocument accountingDocument, AccountingLine accountingLine, String accountingLineCollectionProperty, boolean currentUserIsDocumentInitiator) {
        // MJMC overiding approvedForUnqualifiedEditing in edu.csu.kfs.fp.document.authorization.InternalOrderAccountingLineAuthorizer

        // the fields in a new line should be always editable
        if (accountingLine.getSequenceNumber() == null) {
            return true;
        }

        // check the initiation permission on the document if it is in the state of preroute
        KualiWorkflowDocument workflowDocument = accountingDocument.getDocumentHeader().getWorkflowDocument();
        if ((workflowDocument.stateIsInitiated() || workflowDocument.stateIsSaved()) || workflowDocument.stateIsEnroute() && GlobalVariables.getUserSession() != null) {
            if (isCurrentUserSupplier(accountingDocument) && accountingLineCollectionProperty.equalsIgnoreCase("document.sourceAccountingLines")) {
                return true;
            }
            if (workflowDocument.userIsInitiator(GlobalVariables.getUserSession().getPerson()) && accountingLineCollectionProperty.equalsIgnoreCase("document.targetAccountingLines")) {
                return true;
            }
        }

        return false;


    }

    public static boolean isCurrentUserSupplier(AccountingDocument accountingDocument) {
        boolean isMember = false;
        if (ObjectUtils.isNotNull(accountingDocument)) {
            Person currentUser = GlobalVariables.getUserSession().getPerson();
            KualiWorkflowDocument workflowDocument = null;
            FinancialSystemDocumentHeader docHeader;
            docHeader = accountingDocument.getDocumentHeader();
            if (ObjectUtils.isNotNull(docHeader)) {
                workflowDocument = docHeader.getWorkflowDocument();
            }

            // check if user is Internal Supplier
            if (ObjectUtils.isNotNull(workflowDocument) && !workflowDocument.stateIsInitiated() && !workflowDocument.stateIsFinal()) {
                AttributeSet qualification = new AttributeSet();
                qualification.put("documentNumber", accountingDocument.getDocumentNumber());
                qualification.put("documentTypeName", "IO");
                qualification.put("routeNodeName", "InternalSupplier");

                List<String> roleIds = new ArrayList<String>();
                roleIds.add(roleManagementService.getRoleIdByName("KR-WKFLW", "InternalSupplier"));

                isMember = roleManagementService.principalHasRole(currentUser.getPrincipalId(), roleIds, qualification);

            }

        }
        return isMember;
    }


}