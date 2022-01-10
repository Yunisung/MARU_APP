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
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '입금정산내역');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
	</div>
</div>
<div class="portlet-body form light">
	<c:set var="SALES_GRADE" value="에이전시"></c:set> <%-- ${CP_SESSION.grade} --%>
	<div class="table-scrollable">
		<!-- 리스트 본문 시작 -->
		<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>						
					<th>No</th>
					<th>입금정산일</th>
					<th>매입금액</th>
					<th>입금 수수료</th>
					<th>예정 금액</th>
					<th>계좌 내역</th>
					<th>차액</th>
					<th>차감</th>
					<th>과입</th>
				</tr>
			</thead>
			<tbody id="list">
			<c:set var="var1" value="0"/>
			<c:set var="var2" value="0"/>
			<c:set var="var3" value="0"/>
			<c:set var="var4" value="0"/>
			<c:set var="var5" value="0"/>
			<c:set var="var6" value="0"/>
			<c:set var="var7" value="0"/>
				<c:forEach var="entry" items="${DATAMAP}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td class="date">${entry.collectDay}</td>
						<td class="text-right digits">${entry.amount}</td>
						<td class="text-right digits">${entry.stlVanFee}</td>
						<td class="text-right digits">${entry.calcAmount}</td>
						<td class="text-right digits">${entry.collectAmount}</td>
						<td class="text-right digits">${entry.deductAmount}</td>
						<td class="text-right digits">${entry.minusAmount}</td>
						<td class="text-right digits">${entry.plusAmount}</td>
						<c:set var="var1"  value="${var1 +  entry.amount}"/>
						<c:set var="var2"  value="${var2 +  entry.stlVanFee}"/>
						<c:set var="var3"  value="${var3 +  entry.calcAmount}"/>
						<c:set var="var4"  value="${var4 +  entry.collectAmount}"/>
						<c:set var="var5"  value="${var5 +  entry.deductAmount}"/>
						<c:set var="var6"  value="${var6 +  entry.minusAmount}"/>
						<c:set var="var7"  value="${var7 +  entry.plusAmount}"/>
					</tr>
				</c:forEach>
				<tr class="warning">
						<td colspan="2">합계</td>
						<td class="text-right digits"><c:out value="${var1}" /></td> <%-- ${entry.payCnt} --%>
						<td class="text-right digits"><c:out value="${var2}" /></td> <%-- ${entry.payAmt} --%>
						<td class="text-right digits"><c:out value="${var3}" /></td> <%-- ${entry.rfdCnt} --%>
						<td class="text-right digits"><c:out value="${var4}" /></td> <%-- ${entry.rfdAmt} --%>
						<td class="text-right digits"><c:out value="${var5}" /></td> <%-- ${entry.holdCnt} --%>
						<td class="text-right digits"><c:out value="${var6}" /></td> <%-- ${entry.holdAmt} --%>
						<td class="text-right digits"><c:out value="${var7}" /></td> <%-- ${entry.payAmt + entry.rfdAmt - entry.holdAmt} --%>
						
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
