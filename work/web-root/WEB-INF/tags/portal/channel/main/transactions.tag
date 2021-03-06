<%--
 Copyright 2007 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>
<%@ tag import="org.kuali.kfs.sys.context.SpringContext,
                 org.kuali.rice.kns.service.ParameterService"%>
<%
    final boolean show_tr_link =
 SpringContext.getBean(ParameterService.class).getIndicatorParameter("KFS-TEM",
 "TravelReimbursement", "ALLOW_TR_WITHOUT_TA_IND");
    request.setAttribute("show_tr_link", show_tr_link);
%>
<channel:portalChannelTop channelTitle="Transactions" />
<div class="body">
<%--
	<strong>Accounts Receivable</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Cash Control" url="arCashControlDocument.do?methodToCall=docHandler&command=initiate&docTypeName=CTRL" /></li>
        <li><portal:portalLink displayTitle="true" title="Customer Credit Memo" url="arCustomerCreditMemoDocument.do?methodToCall=docHandler&command=initiate&docTypeName=CRM" /></li>
        <li><portal:portalLink displayTitle="true" title="Customer Invoice" url="arCustomerInvoiceDocument.do?methodToCall=docHandler&command=initiate&docTypeName=INV" /></li>
        <li><portal:portalLink displayTitle="true" title="Customer Invoice Writeoff" url="arCustomerInvoiceWriteoffDocument.do?methodToCall=docHandler&command=initiate&docTypeName=INVW" /></li>
		<li><portal:portalLink displayTitle="true" title="Customer Invoice Writeoff Lookup" url="arCustomerInvoiceWriteoffLookup.do?methodToCall=start&businessObjectClassName=org.kuali.kfs.module.ar.businessobject.CustomerInvoiceWriteoffLookupResult&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>        
        <li><portal:portalLink displayTitle="true" title="Payment Application" url="arPaymentApplicationDocument.do?methodToCall=docHandler&command=initiate&docTypeName=APP" /></li>
    </ul>
--%>
    <strong>Budget Construction</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Budget Construction Selection" url="budgetBudgetConstructionSelection.do?methodToCall=loadExpansionScreen" /></li>
    </ul>

	<strong>Financial Processing</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Advance Deposit" url="financialAdvanceDeposit.do?methodToCall=docHandler&command=initiate&docTypeName=AD" /></li>
<%--
		<li><portal:portalLink displayTitle="true" title="Auxiliary Voucher" url="financialAuxiliaryVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=AV" /></li>
--%>
		<li><portal:portalLink displayTitle="true" title="Adjustment/Accrual Voucher" url="financialAuxiliaryVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=AV" /></li>
        <li><portal:portalLink displayTitle="true" title="Budget Adjustment" url="financialBudgetAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=BA" /></li>
               
        <li><portal:portalLink displayTitle="true" title="Cash Receipt" url="financialCashReceipt.do?methodToCall=docHandler&command=initiate&docTypeName=CR" /></li>
<%--
        <li><portal:portalLink displayTitle="true" title="Credit Card Receipt" url="financialCreditCardReceipt.do?methodToCall=docHandler&command=initiate&docTypeName=CCR" /></li>
--%>
        <li><portal:portalLink displayTitle="true" title="Disbursement Voucher" url="financialDisbursementVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=DV" /></li>
        <li><portal:portalLink displayTitle="true" title="Distribution of Income and Expense" url="financialDistributionOfIncomeAndExpense.do?methodToCall=docHandler&command=initiate&docTypeName=DI" /></li>
		<li><portal:portalLink displayTitle="true" title="General Error Correction" url="financialGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=GEC" /></li>
		<li><portal:portalLink displayTitle="true" title="Indirect Cost Adjustment" url="financialIndirectCostAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=ICA" /></li>
		<li><portal:portalLink displayTitle="true" title="Internal Billing" url="financialInternalBilling.do?methodToCall=docHandler&command=initiate&docTypeName=IB" /></li>
		<li><portal:portalLink displayTitle="true" title="Internal Order" url="financialInternalOrder.do?methodToCall=docHandler&command=initiate&docTypeName=IO" /></li>
		<li><portal:portalLink displayTitle="true" title="Pre-Encumbrance" url="financialPreEncumbrance.do?methodToCall=docHandler&command=initiate&docTypeName=PE" /></li>
        <li><portal:portalLink displayTitle="true" title="Single Sided Budget Adjustment" url="singleSidedBudgetAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=SSBA" /></li> 		
		<li><portal:portalLink displayTitle="true" title="Transfer of Funds" url="financialTransferOfFunds.do?methodToCall=docHandler&command=initiate&docTypeName=TF" /></li>
        <li><portal:portalLink displayTitle="true" title="Work Order Authorization" url="financialWoa.do?methodToCall=docHandler&command=initiate&docTypeName=WOA" /></li>

    </ul>
    
<%--
    <strong>Labor Distribution</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Benefit Expense Transfer" url="laborBenefitExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=BT" /></li>	            
		<li><portal:portalLink displayTitle="true" title="Salary Expense Transfer" url="laborSalaryExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=ST" /></li>	
    </ul>
--%>

	<strong>Purchasing/Accounts Payable</strong><br />
    <ul class="chan">
<%--
        <li><portal:portalLink displayTitle="true" title="Bulk Receiving" url="purapBulkReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=RCVB" /></li>
--%>
        <li><portal:portalLink displayTitle="true" title="Contract Manager Assignment" url="purapContractManagerAssignment.do?methodToCall=docHandler&command=initiate&docTypeName=ACM" /></li>
		<li><portal:portalLink displayTitle="true" title="Payment Request" url="purapPaymentRequest.do?methodToCall=docHandler&command=initiate&docTypeName=PREQ" /></li>
		<li><portal:portalLink displayTitle="true" title="Receiving" url="purapLineItemReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=RCVL" /></li>
        <li><portal:portalLink displayTitle="true" title="Requisition" url="purapRequisition.do?methodToCall=docHandler&command=initiate&docTypeName=REQS" /></li>
        <li><portal:portalLink displayTitle="true" title="Shop Catalogs" url="b2b.do?methodToCall=shopCatalogs" /></li>
        <li><portal:portalLink displayTitle="true" title="Vendor Credit Memo" url="purapVendorCreditMemo.do?methodToCall=docHandler&command=initiate&docTypeName=CM" /></li>
    </ul>
    
<%--
    <strong>Endowment</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Asset Decrease" url="endowAssetDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=EAD" /></li>
        <li><portal:portalLink displayTitle="true" title="Asset Increase" url="endowAssetIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=EAI" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Decrease" url="endowCashDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=ECDD" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Increase" url="endowCashIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=ECI" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Transfer" url="endowCashTransferDocument.do?methodToCall=docHandler&command=initiate&docTypeName=ECT" /></li>
        <li><portal:portalLink displayTitle="true" title="Endowment To GL Transfer Of Funds" url="endowEndowmentToGLTransferOfFundsDocument.do?methodToCall=docHandler&command=initiate&docTypeName=EGLT" /></li>
        <li><portal:portalLink displayTitle="true" title="GL To Endowment Transfer Of Funds" url="endowGLToEndowmentTransferOfFundsDocument.do?methodToCall=docHandler&command=initiate&docTypeName=GLET" /></li>        
  		<li><portal:portalLink displayTitle="true" title="Liability Decrease" url="endowLiabilityDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=ELD" /></li>
        <li><portal:portalLink displayTitle="true" title="Liability Increase" url="endowLiabilityIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=ELI" /></li>
        <li><portal:portalLink displayTitle="true" title="Security Transfer" url="endowSecurityTransferDocument.do?methodToCall=docHandler&command=initiate&docTypeName=EST" /></li>
     </ul>

    <strong>Travel</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Travel Authorization" url="temTravelAuthorization.do?methodToCall=docHandler&command=initiate&docTypeName=TA" /></li>
        <c:if test="${show_tr_link}">
        <li><portal:portalLink displayTitle="true" title="Travel Reimbursement" url="temTravelReimbursement.do?methodToCall=docHandler&command=initiate&docTypeName=TR" /></li>
        </c:if>
   </ul>
--%>   
</div>
<channel:portalChannelBottom />

