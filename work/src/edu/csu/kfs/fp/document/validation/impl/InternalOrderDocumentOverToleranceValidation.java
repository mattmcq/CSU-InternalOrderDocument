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

import edu.csu.kfs.fp.document.InternalOrderDocument;
import edu.csu.kfs.fp.document.authorization.InternalOrderAccountingLineAuthorizer;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.validation.GenericValidation;
import org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KualiDecimal;

/**
 * A validation which checks if a Internal Order document's total amount is not increased by more than 20% by the Supplier before going final
 */
public class InternalOrderDocumentOverToleranceValidation extends GenericValidation {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InternalOrderDocumentOverToleranceValidation.class);

    private AccountingDocument accountingDocumentForValidation;

    public boolean validate(AttributedDocumentEvent event) {
        // MJMC overiding validate

        InternalOrderDocument ioDoc = (InternalOrderDocument) accountingDocumentForValidation;

        // mjmc only do validation when currentUser is Supplier
        if (InternalOrderAccountingLineAuthorizer.isCurrentUserSupplier(ioDoc)) {
            LOG.debug("OverTolerance Validation started");

            KualiDecimal currentTotal = ioDoc.getTotalDollarAmount();
            KualiDecimal originalTotal = ioDoc.getOriginalAmount();
            KualiDecimal potentialMax = (originalTotal.multiply(new KualiDecimal(0.20)).add(originalTotal));


            boolean isValid = true; // (currentTotal.subtract(originalTotal)).divide(originalTotal).isLessEqual(new KualiDecimal(0.20));

            if (!isValid) {
                GlobalVariables.getErrorMap().putError(KFSConstants.TARGET_ACCOUNTING_LINE_ERRORS, "error.document.targetSectionOverTolerance", potentialMax.toString());
            }

            return isValid;
        } else {
            return true;
        }

    }


    public AccountingDocument getAccountingDocumentForValidation() {
        return accountingDocumentForValidation;
    }

    public void setAccountingDocumentForValidation(AccountingDocument accountingDocumentForValidation) {
        this.accountingDocumentForValidation = accountingDocumentForValidation;
    }
}