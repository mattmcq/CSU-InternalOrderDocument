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
package edu.csu.kfs.fp.document;

import edu.csu.kfs.fp.businessobject.InternalOrderItem;
import edu.csu.kfs.fp.businessobject.options.InternalSupplierValuesFinder;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.businessobject.AccountingLine;
import org.kuali.kfs.sys.businessobject.AccountingLineBase;
import org.kuali.kfs.sys.businessobject.AccountingLineParser;
import org.kuali.kfs.sys.businessobject.AccountingLineParserBase;
import org.kuali.kfs.sys.businessobject.GeneralLedgerPendingEntrySourceDetail;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.AccountingDocumentBase;
import org.kuali.kfs.sys.document.AmountTotaling;
import org.kuali.kfs.sys.document.GeneralLedgerPostingDocumentBase;
import org.kuali.kfs.sys.document.service.DebitDeterminerService;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kns.document.Copyable;
import org.kuali.rice.kns.util.KualiDecimal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This is the business object that represents the InternalOrderDocument in Kuali. This is a transactional document that will
 * eventually post transactions to the G/L. It integrates with workflow and also contains two groupings of accounting lines: Expense
 * and Income.
 */
public class InternalOrderDocument extends AccountingDocumentBase implements Copyable, AmountTotaling {

    private List items;
    private Integer nextItemLineNumber;
    private KualiDecimal originalAmount;


    private String internalSupplierId = "";

    /**
     * Initializes the array lists and some basic info.
     */
    public InternalOrderDocument() {
        super();
        setItems(new ArrayList());
        this.nextItemLineNumber = new Integer(1);
        this.internalSupplierId = "";
    }

    /**
     * Adds a new item to the item list.
     *
     * @param item
     */
    public void addItem(InternalOrderItem item) {
        item.setItemSequenceId(this.nextItemLineNumber);
        this.items.add(item);
        this.nextItemLineNumber = new Integer(this.nextItemLineNumber.intValue() + 1);
    }

    /**
     * Retrieve a particular item at a given index in the list of items. For Struts, the requested item and any intervening ones are
     * initialized if necessary.
     *
     * @param index
     * @return the item
     */
    public InternalOrderItem getItem(int index) {
        while (getItems().size() <= index) {
            getItems().add(new InternalOrderItem());
        }
        return (InternalOrderItem) getItems().get(index);
    }

    /**
     * @return Returns the items.
     */
    public List getItems() {
        return items;
    }
    
    /**
     * Allows items (in addition to accounting lines) to be deleted from the database after being saved there.
     *
     * @see org.kuali.rice.kns.document.TransactionalDocumentBase#buildListOfDeletionAwareLists()
     */
    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getItems());
        return managedLists;
    }    

    /**
     * Iterates through the list of items and sums up their totals.
     *
     * @return the total
     */
    public KualiDecimal getItemTotal() {
        KualiDecimal total = KualiDecimal.ZERO;
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            total = total.add(((InternalOrderItem) iterator.next()).getTotal());
        }
        return total;
    }
    
    /**
     * CSU Override the delivered method. Perform the copy function, but then
     * initialize the source/seller lines
     */
    @Override
    public void toCopy() throws WorkflowException {
        super.toCopy();
        //copyAccountingLines(false);
        //updatePostingYearForAccountingLines(getSourceAccountingLines());
        //updatePostingYearForAccountingLines(getTargetAccountingLines());
        this.nextSourceLineNumber = new Integer(1);
        setSourceAccountingLines(new ArrayList());
    }    

    /**
     * Retrieves the next item line number.
     *
     * @return The next available item line number
     */
    public Integer getNextItemLineNumber() {
        return this.nextItemLineNumber;
    }

    /**
     * @param items
     */
    public void setItems(List items) {
        this.items = items;
    }

    /**
     * Setter for OJB to get from database and JSP to maintain state in hidden fields. This property is also incremented by the
     * <code>addItem</code> method.
     *
     * @param nextItemLineNumber
     */
    public void setNextItemLineNumber(Integer nextItemLineNumber) {
        this.nextItemLineNumber = nextItemLineNumber;
    }

    /**
     * @return "Income"
     */
    @Override
    public String getSourceAccountingLinesSectionTitle() {
        return KFSConstants.INCOME;
    }

    /**
     * @return "Expense"
     */
    @Override
    public String getTargetAccountingLinesSectionTitle() {
        return KFSConstants.EXPENSE;
    }

    /**
     * @see org.kuali.kfs.sys.document.AccountingDocumentBase#getAccountingLineParser()
     */
    @Override
    public AccountingLineParser getAccountingLineParser() {
        return new AccountingLineParserBase();
    }

    /**
     * This method determines if an accounting line is a debit accounting line by calling IsDebitUtils.isDebitConsideringSection().
     *
     */
    public boolean isDebit(GeneralLedgerPendingEntrySourceDetail postable) {
        DebitDeterminerService isDebitUtils = SpringContext.getBean(DebitDeterminerService.class);
        return isDebitUtils.isDebitConsideringSection(this, (AccountingLine) postable);
    }


    public String getInternalSupplierId() {
        return internalSupplierId;
    }

    public void setInternalSupplierId(String internalSupplierId) {
        this.internalSupplierId = internalSupplierId;
    }

    public KualiDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(KualiDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getSupplierName() {
        return new InternalSupplierValuesFinder().getKeyLabel(internalSupplierId);
    }
}