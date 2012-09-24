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
package edu.csu.kfs.fp.identity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.identity.KfsKimAttributes;
import org.kuali.rice.kim.bo.types.dto.AttributeSet;
import org.kuali.rice.kim.service.support.impl.KimRoleTypeServiceBase;
import org.kuali.rice.kim.util.KimCommonUtils;
import edu.csu.kfs.fp.service.InternalOrderSupplierService;
import edu.csu.kfs.fp.businessobject.InternalOrderSupplier;
import edu.csu.kfs.fp.identity.CsuKimAttributes;

/**
 * This class supports the CSU Internal Order Document which was extended from IB.
 * This workflow class compares the Qualifiers of the Internal Order document with
 * the roles qualifiers of everyone in the InternalSupplier role, returning true or
 * false depending on whether they match. Really, the only document qualifier is the
 * documentNumber which is then used to retrieve the internalSupplierId of the 
 * Internal Order document which triggered the execution of this class.
 * 
 * When Internal Order documents are saved/submitted, the supplier id is stored with
 * the document number. Use document number to retrieve the supplier id. Then compare
 * this retrieved document supplier id to the internalSupplier roles.
 * Currently, this program uses the document number as the key to retrieve the 
 * supplier id from the csuf_int_order_doc table using a business object service. We 
 * could replace this (and eliminate the bus obj service) by just retrieving the document.
 */
public class InternalSupplierRoleTypeServiceImpl extends KimRoleTypeServiceBase {
 
    @Override
    protected boolean performMatch(AttributeSet qualification, AttributeSet roleQualifier) {
        validateRequiredAttributesAgainstReceived(roleQualifier);
        validateRequiredAttributesAgainstReceived(qualification);
        
        String docNumber = "";
        String docSuppId = "";
        if ( qualification != null ) {
            docNumber  = qualification.get(KfsKimAttributes.DOCUMENT_NUMBER);
        }
        docSuppId = SpringContext.getBean(InternalOrderSupplierService.class).getInternalSupplierId(docNumber);        
        
        String roleSupplierId = roleQualifier.get(CsuKimAttributes.INTERNAL_SUPPLIER_ID);
        
        // Compare the Internal Order Document Supplier Id with the Role Qualifier Supplier ID
        if (StringUtils.isBlank(roleSupplierId)) {
        	return false;
        }        
        if (!StringUtils.equals(roleSupplierId,docSuppId)) {
        	return false;
        }        
        return true;
    }


}
