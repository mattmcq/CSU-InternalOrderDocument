package edu.csu.kfs.fp.businessobject;

import java.util.LinkedHashMap;

import org.kuali.kfs.sys.KFSPropertyConstants;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

public class InternalOrderSupplier extends PersistableBusinessObjectBase{

    private String  documentNumber;
    private String  internalSupplierId;
    private Integer nextExpenseNumber;
    private Integer nextIncomeNumber;
    private Integer nextItemNumber;
    private Integer postingYear;

    public InternalOrderSupplier() {
    }
 
    public String getDocumentNumber() {
        return documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    } 
    public String getInternalSupplierId() {
        return internalSupplierId;
    }
    public void setInternalSupplierId(String internalSupplierId) {
        this.internalSupplierId = internalSupplierId;
    } 
    public Integer getNextExpenseNumber() {
        return nextExpenseNumber;
    }
    public void setNextExpenseNumber(Integer nextExpenseNumber) {
        this.nextExpenseNumber = nextExpenseNumber;
    } 
    public Integer getNextIncomeNumber() {
        return nextIncomeNumber;
    }
    public void setNextIncomeNumber(Integer nextIncomeNumber) {
        this.nextIncomeNumber = nextIncomeNumber;
    } 
    public Integer getNextItemNumber() {
        return nextItemNumber;
    }
    public void setNextItemNumber(Integer nextItemNumber) {
        this.nextItemNumber = nextItemNumber;
    } 
    public Integer getPostingYear() {
        return postingYear;
    }
    public void setPostingYear(Integer postingYear) {
        this.postingYear = postingYear;
    }    
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("documentNumber", getDocumentNumber());
        return m;
    }    
}
