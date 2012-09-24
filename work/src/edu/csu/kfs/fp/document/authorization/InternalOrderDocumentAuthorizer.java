/*
 * Copyright 2009 The Kuali Foundation.
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

import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.authorization.AccountingDocumentAuthorizerBase;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

import java.util.Set;

/**
 * The customized document authorizer for the Internal Order document
 */
public class InternalOrderDocumentAuthorizer extends AccountingDocumentAuthorizerBase {
    @Override
    public Set<String> getDocumentActions(Document document, Person user, Set<String> documentActionsFromPresentationController) {
        // MJMC overiding getDocumentActions in edu.csu.kfs.fp.document.authorization.InternalOrderDocumentAuthorizer

        Set<String> documentActions = super.getDocumentActions(document, user, documentActionsFromPresentationController);

        documentActions.contains(KNSConstants.KUALI_ACTION_CAN_APPROVE);
        if (documentActions.contains(KNSConstants.KUALI_ACTION_CAN_BLANKET_APPROVE)) {
            documentActions.remove(KNSConstants.KUALI_ACTION_CAN_BLANKET_APPROVE);
        }

        KualiWorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        
/* I01729 JAW. JHunter requested to remove this Initiator update of buyer accounting lines.        
        if ((workflowDocument.getCurrentRouteNodeNames().contains("InternalSupplier")) && workflowDocument.userIsInitiator(user) && !workflowDocument.getStatusDisplayValue().equalsIgnoreCase("FINAL")) {
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_EDIT);
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_SAVE);
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_CANCEL);
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_ANNOTATE);
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_FYI);

        }
*/
        
        // If this was AdHoced to the Supplier, then add these actions (can_edit is required) I01729 JAW
        if ((workflowDocument.getCurrentRouteNodeNames().contains("AdHoc")) && InternalOrderAccountingLineAuthorizer.isCurrentUserSupplier((AccountingDocument)document) && !workflowDocument.getStatusDisplayValue().equalsIgnoreCase("FINAL")) {
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_EDIT);
        }  
       
        return documentActions;
    }


}