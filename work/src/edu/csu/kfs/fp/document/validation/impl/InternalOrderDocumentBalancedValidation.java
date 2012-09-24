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
package edu.csu.kfs.fp.document.validation.impl;

import edu.csu.kfs.fp.document.authorization.InternalOrderAccountingLineAuthorizer;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.KFSKeyConstants;
import org.kuali.kfs.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.kfs.sys.document.validation.impl.DebitsAndCreditsBalanceValidation;
import org.kuali.kfs.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.rice.kns.exception.ValidationException;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KualiDecimal;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

import java.util.List;

/**
 * A validation which checks if a Internal Order document is balanced before going final
 */
public class InternalOrderDocumentBalancedValidation extends DebitsAndCreditsBalanceValidation {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InternalOrderDocumentBalancedValidation.class);

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        // MJMC overiding validate

        AccountingDocument accountingDocument = this.getAccountingDocumentForValidation();
        
        //I017289. JWalker only balance at last approval node InternalSupplier
        //because approvers (fo,org,div,hosp) can also be suppliers
        KualiWorkflowDocument workflowDocument = accountingDocument.getDocumentHeader().getWorkflowDocument();

        // mjmc only do validation when currentUser is Supplier 
        if (InternalOrderAccountingLineAuthorizer.isCurrentUserSupplier(accountingDocument) && 
        		(workflowDocument.getCurrentRouteNodeNames().contains("InternalSupplier"))) {
            LOG.debug("Validation started");

            // generate GLPEs specifically here so that we can compare debits to credits
            if (!SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(accountingDocument)) {
                throw new ValidationException("general ledger GLPE generation failed");
            }

            // now loop through all of the GLPEs and calculate buckets for debits and credits
            KualiDecimal creditAmount = KualiDecimal.ZERO;
            KualiDecimal debitAmount = KualiDecimal.ZERO;

            List<GeneralLedgerPendingEntry> pendingEntries = accountingDocument.getGeneralLedgerPendingEntries();
            for (GeneralLedgerPendingEntry entry : pendingEntries) {
                if (entry.isTransactionEntryOffsetIndicator()) {
                    continue;
                }

                if (KFSConstants.GL_CREDIT_CODE.equals(entry.getTransactionDebitCreditCode())) {
                    creditAmount = creditAmount.add(entry.getTransactionLedgerEntryAmount());
                } else {
                    debitAmount = debitAmount.add(entry.getTransactionLedgerEntryAmount());
                }
            }

            boolean isValid = debitAmount.compareTo(creditAmount) == 0;

            if (!isValid) {
                GlobalVariables.getErrorMap().putError(KFSConstants.ACCOUNTING_LINE_ERRORS, KFSKeyConstants.ERROR_DOCUMENT_BALANCE);
            }

            return isValid;
        } else {
            return true;
        }

    }
}