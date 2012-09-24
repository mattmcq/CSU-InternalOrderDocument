/*
 * Copyright 2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.csu.kfs.fp.document.validation.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.fp.document.InternalBillingDocument;
import org.kuali.kfs.sys.businessobject.AccountingLine;
import org.kuali.kfs.sys.businessobject.SourceAccountingLine;
import org.kuali.kfs.sys.businessobject.TargetAccountingLine;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.validation.GenericValidation;
import org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.kfs.sys.document.validation.impl.AccountingLineValueAllowedValidation;
import org.kuali.kfs.sys.service.impl.KfsParameterConstants;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.service.ParameterService;
import org.kuali.rice.kns.util.ObjectUtils;

import edu.csu.kfs.fp.document.InternalOrderDocument;

/**
 * A validation which uses parameters to determine if a value on an accounting line is valid.
 * Create CSU version for I03019/T05742
 */
public class CsuAccountingLineValueAllowedValidation extends AccountingLineValueAllowedValidation {

    private static final String OBJECT_TYPES         = "OBJECT_TYPES";
    private static final String OBJECT_SUB_TYPES     = "OBJECT_SUB_TYPES"; 
    private static final String OBJECT_LEVELS        = "OBJECT_LEVELS";
    private static final String OBJECT_TYPES_INC     = "OBJECT_TYPES_INC";
    private static final String OBJECT_SUB_TYPES_INC = "OBJECT_SUB_TYPES_INC"; 
    private static final String OBJECT_LEVELS_INC    = "OBJECT_LEVELS_INC";
    private static final String OBJECT_TYPES_EXP     = "OBJECT_TYPES_EXP";
    private static final String OBJECT_SUB_TYPES_EXP = "OBJECT_SUB_TYPES_EXP"; 
    private static final String OBJECT_LEVELS_EXP    = "OBJECT_LEVELS_EXP";    

    /**
     * Checks if a value in a given accounting line is allowed, based on system parameters.
     * For CSU override OBJECT_LEVELS, OBJECT_TYPES, and OBJECT_SUB_TYPES for IO and IB 
     * documents to use seperate sys parms for income/source and expense/target lines.
     */
    public boolean validate(AttributedDocumentEvent event) {
        
        if (!StringUtils.isBlank(propertyPath)) {
            refreshByPath(accountingLineForValidation);
        }
        // CSU I03019. JWalker. Use CSU sys parms for IO and IB documents (split Income and Expense)
        if (accountingDocumentForValidation instanceof InternalOrderDocument || accountingDocumentForValidation instanceof InternalBillingDocument) {
        	if (accountingLineForValidation instanceof SourceAccountingLine) {
        	    if (StringUtils.equals(parameterToCheckAgainst, OBJECT_TYPES)) {
        		    return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), 
        				accountingLineForValidation, OBJECT_TYPES_INC, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
        	    }
        	    if (StringUtils.equals(parameterToCheckAgainst, OBJECT_SUB_TYPES)) {
        		    return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), 
        				accountingLineForValidation, OBJECT_SUB_TYPES_INC, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
        	    } 
        	    if (StringUtils.equals(parameterToCheckAgainst, OBJECT_LEVELS)) {
        		    return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), 
        				accountingLineForValidation, OBJECT_LEVELS_INC, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
        	    }         	    
        	}
        	if (accountingLineForValidation instanceof TargetAccountingLine) {
        	    if (StringUtils.equals(parameterToCheckAgainst, OBJECT_TYPES)) {
        		    return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), 
        				accountingLineForValidation, OBJECT_TYPES_EXP, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
        	    }
        	    if (StringUtils.equals(parameterToCheckAgainst, OBJECT_SUB_TYPES)) {
        		    return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), 
        				accountingLineForValidation, OBJECT_SUB_TYPES_EXP, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
        	    } 
        	    if (StringUtils.equals(parameterToCheckAgainst, OBJECT_LEVELS)) {
        		    return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), 
        				accountingLineForValidation, OBJECT_LEVELS_EXP, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
        	    }         	    
        	}         	
        }
        
        return isAccountingLineValueAllowed(accountingDocumentForValidation.getDocumentClassForAccountingLineValueAllowedValidation(), accountingLineForValidation, parameterToCheckAgainst, propertyPath, (responsibleProperty != null ? responsibleProperty : propertyPath));
    }
    
    /**
     * Checks that a value on an accounting line is valid, based on parameters, for a document of the given class
     * @param documentClass the class of the document to check
     * @param accountingLine the accounting line to check
     * @param parameterName the name of the parameter to check
     * @param propertyName the name of the property to check
     * @param userEnteredPropertyName the value the user entered on the line
     * @return true if this passes validation, false otherwise
     */
    public boolean isAccountingLineValueAllowed(Class documentClass, AccountingLine accountingLine, String parameterName, String propertyName, String userEnteredPropertyName) {
        boolean isAllowed = true;
        String exceptionMessage = "Invalue property name provided to AccountingDocumentRuleBase isAccountingLineValueAllowed method: " + propertyName;
        try {
            String propertyValue = (String) PropertyUtils.getProperty(accountingLine, propertyName);
            if (getParameterService().parameterExists(KfsParameterConstants.FINANCIAL_PROCESSING_DOCUMENT.class, parameterName)) {
                isAllowed = getParameterService().getParameterEvaluator(KfsParameterConstants.FINANCIAL_PROCESSING_DOCUMENT.class, parameterName, propertyValue).evaluateAndAddError(SourceAccountingLine.class, propertyName, userEnteredPropertyName);
            }
            if (getParameterService().parameterExists(documentClass, parameterName)) {
                isAllowed = getParameterService().getParameterEvaluator(documentClass, parameterName, propertyValue).evaluateAndAddError(SourceAccountingLine.class, propertyName, userEnteredPropertyName);
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(exceptionMessage, e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(exceptionMessage, e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(exceptionMessage, e);
        }
        return isAllowed;
    }
  

}
