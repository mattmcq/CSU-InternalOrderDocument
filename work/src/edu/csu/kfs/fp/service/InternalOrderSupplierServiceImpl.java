package edu.csu.kfs.fp.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import edu.csu.kfs.fp.service.InternalOrderSupplierService;
import edu.csu.kfs.fp.businessobject.InternalOrderSupplier;

import org.kuali.rice.kns.service.BusinessObjectService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class InternalOrderSupplierServiceImpl implements InternalOrderSupplierService {
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InternalOrderSupplierServiceImpl.class);
    private BusinessObjectService businessObjectService;
    
    public String getInternalSupplierId(String docId) {
    	Map keys = new HashMap();
    	keys.put("documentNumber",docId);
    	Collection coll = businessObjectService.findMatching(InternalOrderSupplier.class, keys);
    	if (coll.size()==1) {
    		Iterator iter = coll.iterator();
    		InternalOrderSupplier iod = (InternalOrderSupplier) iter.next();
    		return iod.getInternalSupplierId();
    	}
    	else {
            Iterator iter = coll.iterator();
            while (iter.hasNext()) {
        		InternalOrderSupplier iod = (InternalOrderSupplier) iter.next();
        		return iod.getInternalSupplierId();
            }
        }
    		
    	return "";
    }
    
    /**
     * Gets the businessObjectService attribute.
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * Sets the businessObjectService attribute.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
