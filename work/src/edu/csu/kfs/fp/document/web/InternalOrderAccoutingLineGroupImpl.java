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
package edu.csu.kfs.fp.document.web;

import org.kuali.kfs.sys.KFSPropertyConstants;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.datadictionary.AccountingLineGroupDefinition;
import org.kuali.kfs.sys.document.web.AccountingLineRenderingContext;
import org.kuali.kfs.sys.document.web.DefaultAccountingLineGroupImpl;
import org.kuali.kfs.sys.document.web.RenderableAccountingLineContainer;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.util.GlobalVariables;

import java.util.List;
import java.util.Map;

public class InternalOrderAccoutingLineGroupImpl extends DefaultAccountingLineGroupImpl {

    private List<RenderableAccountingLineContainer> containers;


    @Override
    public void initialize(AccountingLineGroupDefinition groupDefinition, AccountingDocument accountingDocument, List<RenderableAccountingLineContainer> containers, String collectionPropertyName, String collectionItemPropertyName, Map<String, Object> displayedErrors, Map<String, Object> displayedWarnings, Map<String, Object> displayedInfo, boolean canEdit) {
        // MJMC overiding initialize in edu.csu.kfs.fp.document.web.InternalOrderAccoutingLineGroupImpl
        this.containers = containers;
        super.initialize(groupDefinition, accountingDocument, containers, collectionPropertyName, collectionItemPropertyName, displayedErrors, displayedWarnings, displayedInfo, canEdit);    //To change body of overridden methods use File | Settings | File Templates.
    }



    @Override
    protected boolean hasEnoughAccountingLinesForDelete() {
        // MJMC overiding hasEnoughAccountingLinesForDelete in edu.csu.kfs.fp.document.web.InternalOrderAccoutingLineGroupImpl

        // mjmc - the Rice version of this is wrong -- we want to show the delete button if there is just one entry
        // mjmc  AND accountingLineRenderingContext.isEditableLine() calls a method that is final - org.kuali.kfs.sys.document.authorization.AccountingLineAuthorizerBase#hasEditPermissionOnAccountingLine


        // 1. get the count of how many accounting lines are editable
        int editableLineCount = 0;
        for (AccountingLineRenderingContext accountingLineRenderingContext : containers) {
            if (!accountingLineRenderingContext.isNewLine() ){ //&& accountingLineRenderingContext.isEditableLine()) {
                editableLineCount += 1;
            }
            if (editableLineCount == 1) return true; // we know we're good...skip out early
        }
        return false;


    }


    @Override
    public void updateDeletabilityOfAllLines() {

        Person currentUser = GlobalVariables.getUserSession().getPerson();

        super.updateDeletabilityOfAllLines();

            if (hasEnoughAccountingLinesForDelete()) {
                for (AccountingLineRenderingContext accountingLineRenderingContext : containers) {
                    if (getGroupDefinition().getAccountingLineAuthorizer().hasEditPermissionOnField(getAccountingDocument(), accountingLineRenderingContext.getAccountingLine(), getCollectionItemPropertyName(), KFSPropertyConstants.AMOUNT, true, true,  currentUser) ) {
                        accountingLineRenderingContext.makeDeletable();
                    }
                }

        }
    }


}