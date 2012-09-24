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
import edu.csu.kfs.fp.document.InternalOrderDocument;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.KFSPropertyConstants;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.web.struts.KualiAccountingDocumentActionBase;
import org.kuali.rice.kns.service.DictionaryValidationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles Actions for InternalOrder.
 */
public class InternalOrderAction extends KualiAccountingDocumentActionBase {


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        InternalOrderForm ioForm = (InternalOrderForm) form;

        // only let initiator (docroutelevel=0) and FO (docroutelevel=1) set the originalAmount
        if (ioForm.hasDocumentId() && ioForm.getInternalOrderDocument().getDocumentHeader().getWorkflowDocument().getDocRouteLevel() <= 1) {
            InternalOrderDocument ioDoc = ioForm.getInternalOrderDocument();

            ioDoc.setOriginalAmount(ioDoc.getTotalDollarAmount());
//            System.out.println("\n\nAppDocId = " + ioDoc.getDocumentHeader().getWorkflowDocument().getAppDocId());
//            System.out.println("OriginalAmount = " + ioDoc.getOriginalAmount());
//            System.out.println("CurrentAmount = " + ioDoc.getTotalDollarAmount());
//            System.out.println("StatusDisplayValue = " + ioDoc.getDocumentHeader().getWorkflowDocument().getStatusDisplayValue());
//            System.out.println("CurrentRouteNodeNames = " + ioDoc.getDocumentHeader().getWorkflowDocument().getCurrentRouteNodeNames());
//            System.out.println("DocRouteLevel = " + ioDoc.getDocumentHeader().getWorkflowDocument().getDocRouteLevel());
//            System.out.println("RoutedByPrincipalId = " + ioDoc.getDocumentHeader().getWorkflowDocument().getRoutedByPrincipalId());
//            System.out.println("InitiatorPrincipalId = " + ioDoc.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId());

        }

        return super.execute(mapping, form, request, response);
    }
    

    /**
     * Adds a new InternalOrderItem from the Form to the Document if valid. This method is called reflectively from KualiAction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward insertItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InternalOrderForm internalOrderForm = (InternalOrderForm) form;
        if (validateNewItem(internalOrderForm)) {
            internalOrderForm.getInternalOrderDocument().addItem(internalOrderForm.getNewItem());
            internalOrderForm.setNewItem(new InternalOrderItem());
        }
        return mapping.findForward(KFSConstants.MAPPING_BASIC);
    }

    /**
     * Validates the new InternalOrderItem on the Form, adding a global error if invalid.
     *
     * @param internalOrderForm
     * @return whether the new item is valid
     */
    private static boolean validateNewItem(InternalOrderForm internalOrderForm) {
        return SpringContext.getBean(DictionaryValidationService.class).isBusinessObjectValid(internalOrderForm.getNewItem(), KFSPropertyConstants.NEW_ITEM);
    }

    /**
     * Deletes an InternalOrderItem from the Document. This method is called reflectively from KualiAction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InternalOrderForm internalOrderForm = (InternalOrderForm) form;
        internalOrderForm.getInternalOrderDocument().getItems().remove(getLineToDelete(request));
        return mapping.findForward(KFSConstants.MAPPING_BASIC);
    }
}