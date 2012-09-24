<%--
 Copyright 2005-2007 The Kuali Foundation.

 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.opensource.org/licenses/ecl1.php

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/jsp/sys/kfsTldHeader.jsp" %>

<%@ taglib uri="/kr/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/kr/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/kr/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/kr/WEB-INF/tlds/displaytag.tld" prefix="display" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-bean-el.tld" prefix="bean-el" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-html-el.tld" prefix="html-el" %>
<%@ taglib uri="/kr/WEB-INF/tlds/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/kr/WEB-INF/tlds/displaytag-el.tld" prefix="display-el" %>
<%@ taglib uri="/kr/WEB-INF/tlds/kuali-func.tld" prefix="kfunc" %>
<%@ taglib tagdir="/WEB-INF/tags/kr" prefix="kul" %>
<%@ taglib tagdir="/WEB-INF/tags/kim" prefix="kim" %>
<%@ taglib tagdir="/WEB-INF/tags/kew" prefix="kew" %>
<%@ taglib tagdir="/WEB-INF/tags/kr/dd" prefix="dd" %>

<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp" %>

<%@ taglib tagdir="/WEB-INF/tags/fp" prefix="fp" %>

<%@ taglib tagdir="/WEB-INF/tags/gl" prefix="gl" %>
<%@ taglib tagdir="/WEB-INF/tags/gl/glcp" prefix="glcp" %>

<%@ taglib tagdir="/WEB-INF/tags/sys" prefix="sys" %>

<%@ taglib tagdir="/WEB-INF/tags/module/cg" prefix="cg" %>

<%@ taglib tagdir="/WEB-INF/tags/module/bc" prefix="bc" %>

<%@ taglib tagdir="/WEB-INF/tags/module/ld" prefix="ld" %>

<%@ taglib tagdir="/WEB-INF/tags/module/purap" prefix="purap" %>

<%@ taglib tagdir="/WEB-INF/tags/module/cams" prefix="cams" %>

<%@ taglib tagdir="/WEB-INF/tags/module/cab" prefix="cab" %>

<%@ taglib tagdir="/WEB-INF/tags/module/ar" prefix="ar" %>

<%@ taglib tagdir="/WEB-INF/tags/pdp" prefix="pdp" %>

<%@ taglib tagdir="/WEB-INF/tags/module/ec" prefix="ec" %>

<%@ taglib tagdir="/WEB-INF/tags/portal" prefix="portal" %>
<%@ taglib tagdir="/WEB-INF/tags/portal/channel" prefix="channel" %>
<%@ taglib tagdir="/WEB-INF/tags/portal/channel/administration" prefix="admininstrationChannel" %>
<%@ taglib tagdir="/WEB-INF/tags/portal/channel/future" prefix="futureChannel" %>
<%@ taglib tagdir="/WEB-INF/tags/portal/channel/main" prefix="mainChannel" %>
<%@ taglib tagdir="/WEB-INF/tags/portal/channel/maintenance" prefix="maintenanceChannel" %>

<%-- utility web functions --%>
<%@ taglib uri="/WEB-INF/tlds/kfsfunc.tld" prefix="kfsfunc" %>

<%@ taglib uri="/WEB-INF/tlds/kfssys.tld" prefix="sys-java" %>

<%@ taglib uri="/WEB-INF/tlds/csusys.tld" prefix="csu-java" %>


<c:set var="internalOrderAttributes"
       value="${DataDictionary['InternalOrderDocument'].attributes}"/>
<c:set var="readOnly"
       value="${!KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}"/>
<c:set var="editingMode" value="${KualiForm.editingMode}"/>

<kul:documentPage showDocumentInfo="true"
                  htmlFormAction="financialInternalOrder"
                  documentTypeName="InternalOrderDocument" renderMultipart="true"
                  showTabButtons="true">

    <sys:documentOverview editingMode="${KualiForm.editingMode}"/>


    <!-- INTERNAL ORDER SPECIFIC FIELDS -->
    <kul:tab tabTitle="Internal Order Details" defaultOpen="true"
             tabErrorKey="${KFSConstants.EDIT_JOURNAL_VOUCHER_ERRORS}" highlightTab="false">
        <div class="tab-container" align=center>
            <h3>Internal Supplier</h3>
            <table cellpadding=0 class="datatable"
                   summary="view/edit ad hoc recipients">
                <tbody>

                <tr>
                    <th width="35%" class="bord-l-b">
                        <div align="right"><kul:htmlAttributeLabel
                                labelFor="" attributeEntry="${internalOrderAttributes.internalSupplierId}"
                                useShortLabel="false"/></div>
                    </th>
                    <td class="datacell-nowrap">


                        <c:choose>
                            <c:when test="${KualiForm.editingMode['buyerMode']}">
                                <kul:htmlControlAttribute property="document.internalSupplierId"
                                                          attributeEntry="${internalOrderAttributes.internalSupplierId}"
                                                          onchange="submitForm()"/>
                            </c:when>
                            <c:otherwise>

                                <kul:inquiry boClassName="edu.csu.kfs.fp.businessobject.InternalSupplier"
                                             keyValues="internalSupplierId=${KualiForm.document.internalSupplierId}"
                                             render="true">

                                    <c:out value="${KualiForm.document.supplierName}"/>

                                </kul:inquiry>

                            </c:otherwise>
                        </c:choose>


                    </td>


                </tr>
                </tbody>
            </table>
        </div>
    </kul:tab>


    <kul:tab tabTitle="Buyer Accounting Lines" defaultOpen="true"
             tabErrorKey="${KFSConstants.ACCOUNTING_LINE_ERRORS}" hidden="false" highlightTab="false">
        <sys-java:accountingLines>
            <csu-java:accountingLineGroup newLinePropertyName="newTargetLine"
                                          collectionPropertyName="document.targetAccountingLines"
                                          collectionItemPropertyName="document.targetAccountingLine"
                                          attributeGroupName="target"/>
        </sys-java:accountingLines>
    </kul:tab>


    <c:if test="${!KualiForm.editingMode['buyerMode']}">

        <kul:tab tabTitle="Supplier Accounting Lines" defaultOpen="true"
                 tabErrorKey="${KFSConstants.ACCOUNTING_LINE_ERRORS}" hidden="false" highlightTab="false">
            <sys-java:accountingLines>
                <csu-java:accountingLineGroup newLinePropertyName="newSourceLine"
                                              collectionPropertyName="document.sourceAccountingLines"
                                              collectionItemPropertyName="document.sourceAccountingLine"
                                              attributeGroupName="source"/>
            </sys-java:accountingLines>
        </kul:tab>

    </c:if>

    <fp:items editingMode="${KualiForm.editingMode}"/>


    <c:set var="readOnly" value="${!KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}"/>

    <gl:generalLedgerPendingEntries/>
    <kul:notes/>
    <kul:adHocRecipients/>
    <kul:routeLog/>
    <kul:panelFooter/>
    <sys:documentControls
            transactionalDocument="${documentEntry.transactionalDocument}" extraButtons="${KualiForm.extraButtons}"/>

</kul:documentPage>
