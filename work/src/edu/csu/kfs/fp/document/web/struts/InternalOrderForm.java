/*
 * Copyright 2005-2007 The Kuali Foundation.
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
package edu.csu.kfs.fp.document.web.struts;

import edu.csu.kfs.fp.businessobject.InternalOrderItem;
import edu.csu.kfs.fp.businessobject.InternalSupplier;
import edu.csu.kfs.fp.document.InternalOrderDocument;
import edu.csu.kfs.fp.service.InternalSupplierService;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.web.struts.KualiAccountingDocumentFormBase;

import javax.servlet.http.HttpServletRequest;


/**
 * This class is the action form for Internal Order.
 */
public class InternalOrderForm extends KualiAccountingDocumentFormBase {
    private static final long serialVersionUID = 1L;
    private InternalOrderItem newItem;
    private InternalSupplier selectedInternalSupplier;

    private String financialDocumentStatusCode;


    public InternalOrderForm() {
        super();
        setDocument(new InternalOrderDocument());
        newItem = new InternalOrderItem();
        selectedInternalSupplier = new InternalSupplier();
    }

    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);

        selectedInternalSupplier = getPopulatedInternalSupplierInstance(getInternalOrderDocument().getInternalSupplierId());

    }


    /**
     * @return Returns the internalOrderDocument.
     */
    public InternalOrderDocument getInternalOrderDocument() {
        return (InternalOrderDocument) getDocument();
    }

    /**
     * @param internalOrderDocument The internalOrderDocument to set.
     */
    public void setInternalOrderDocument(InternalOrderDocument internalOrderDocument) {
        setDocument(internalOrderDocument);
    }

    /**
     * @return Returns the newItem.
     */
    public InternalOrderItem getNewItem() {
        return newItem;
    }

    /**
     * @param newItem The newItem to set.
     */
    public void setNewItem(InternalOrderItem newItem) {
        this.newItem = newItem;
    }


    protected InternalSupplier getPopulatedInternalSupplierInstance(String internalSupplierId) {
        // now we have to get the Id and the name of the original and new suppliers
        InternalSupplierService its = SpringContext.getBean(InternalSupplierService.class);
        return its.getInternalSupplierById(internalSupplierId);
    }

    public String getFinancialDocumentStatusCode() {
        return financialDocumentStatusCode;
    }

    public void setFinancialDocumentStatusCode(String financialDocumentStatusCode) {
        this.financialDocumentStatusCode = financialDocumentStatusCode;
    }


}



