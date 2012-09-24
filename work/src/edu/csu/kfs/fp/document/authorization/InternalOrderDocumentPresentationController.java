/*
 * Copyright 2008 The Kuali Foundation.
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

import org.kuali.kfs.sys.KfsAuthorizationConstants;
import org.kuali.kfs.sys.document.authorization.AccountingDocumentPresentationControllerBase;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

import java.util.Set;


public class InternalOrderDocumentPresentationController extends AccountingDocumentPresentationControllerBase {


    @Override
    public Set<String> getEditModes(Document document) {
        // MJMC overiding getEditModes
        Set<String> editModes = super.getEditModes(document);
        KualiWorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        Person currentUser = GlobalVariables.getUserSession().getPerson();

        if (workflowDocument.stateIsInitiated() || workflowDocument.stateIsSaved() && workflowDocument.userIsInitiator(currentUser)) {
            editModes.add("buyerMode"); // MJMC - setup constants.java somewhere
            editModes.add(KfsAuthorizationConstants.TransactionalEditMode.EXPENSE_ENTRY);  // mjmc - "expenseEntry" not sure if i can set incomeEntry also??
        }
        if (workflowDocument.stateIsEnroute()) {
            editModes.add("supplierMode"); // MJMC - setup constants.java somewhere
        }
        if (workflowDocument.stateIsEnroute() && (workflowDocument.userIsInitiator(currentUser))) {
            editModes.add("canEdit");
            editModes.add(KfsAuthorizationConstants.TransactionalEditMode.EXPENSE_ENTRY);
        }

        return editModes;
    }


}