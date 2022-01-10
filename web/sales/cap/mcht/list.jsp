<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '매입일 기준 매출조회');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
	</div>
</div>
<div class="portlet-body form light">
	<div class="table-scrollable">
		<!-- 리스트 본문 시작 -->
		<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>
					<th rowspan="3">No</th>
					<!-- <th rowspan="3">정산일</th> -->
					<th rowspan="3">가맹점명</th>
					<th style="min-width: 100px;" rowspan="3">매입시작일</th>
					<th style="min-width: 100px;" rowspan="3">매입종료일</th>
					<th colspan="12">매출정보</th>
					<th rowspan="3">예수금<br>(미출금)</th>
					<th rowspan="3">차감 금액</th>
					<th colspan="2" rowspan="2">지급정보</th>
				</tr>
				<tr>
					<th colspan="2">매입</th>
					<th colspan="2">매입취소</th>
					<th colspan="2">리스크</th>
					<th rowspan="2">합계</th>
					<th colspan="5">합계</th>
				</tr>
				<tr>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					
					<th>수수료</th>
					<th>입금수수료</th>
					<th>대행사수수료</th>
					<th>수익</th>
					<th>지급예정</th>
					<th>실지급액</th>
					<th>정산수수료율</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="15">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.idx}" 
					<c:if test="${status.index ne 0 && entry.stlDay ne CPR.data[status.index-1].stlDay}">
						class="bg-grey-cararra bg-font-grey-cararra"
					</c:if>>
						<td>${status.count}</td>
						<td>${entry.mchtName}</td>
						<td class="date">${entry.startDay}</td>
						<td class="date">${entry.endDay}</td>
						<td class="text-right digits">${entry.payCnt}</td>
						<td class="text-right digits">${entry.payAmt}</td>
						<td class="text-right digits">${entry.rfdCnt}</td>
						<td class="text-right digits">${entry.rfdAmt}</td>
						<td class="text-right digits">${entry.holdCnt}</td>
						<td class="text-right digits">${entry.holdAmt}</td>
						<td class="text-right digits">${entry.payAmt + entry.rfdAmt}</td>
						<td class="text-right digits">${(entry.payFee + entry.payVat) + (entry.rfdFee + entry.rfdVat) + (entry.relsFee + entry.relsVat)}</td>
						<td class="text-right digits">${entry.vanFee}</td>
						<td class="text-right digits">${entry.agencyFee + entry.distFee}</td>
						<td class="text-right digits">${entry.benefit}</td>
						<td class="text-right digits">${entry.stlAmount}</td>
						<td class="text-right digits">${(entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt}</td>
						<td class="text-right digits">${entry.deductAmt}</td>
						<td class="text-right digits">${entry.stlAmount + (entry.relsAmt - entry.relsFee - entry.relsVat) + entry.deductAmt}</td>
						<td class="text-right rate">${entry.stlRate}</td>
					
					</tr>
				</c:forEach>
				<tr>
						<c:set var="sum" value="${CPR.sum }"/>
						<td></td>
						<td></td>
						<td></td>
						<td>합계</td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.payCnt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.payAmt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.rfdCnt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.rfdAmt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.holdCnt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.holdAmt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.payAmt+sum.rfdAmt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${(sum.payFee + sum.payVat) + (sum.rfdFee + sum.rfdVat) + (sum.relsFee + sum.relsVat)}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.vanFee}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.agencyFee + sum.distFee}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.benefit}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.stlAmount}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${(sum.relsAmt - sum.relsFee - sum.relsVat) + sum.manualRelsAmt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.deductAmt}" type="number" maxFractionDigits="0"/></td>
						<td class="text-right digits"><fmt:formatNumber value="${sum.stlAmount + (sum.relsAmt - sum.relsFee - sum.relsVat) + sum.deductAmt}" type="number" maxFractionDigits="0"/></td>
					
						<td></td>
				</tr>
				
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
