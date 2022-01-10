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
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '가맹점 입금 내역 생성');">
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
					<th rowspan="2">No</th>
					<th rowspan="2">거래내역</th>
					<th rowspan="2">입금일</th>
					<th rowspan="2">가맹점ID</th>
					<th rowspan="2">가맹점</th>
					<th>매입금액</th>
					<th>입금 수수료</th>
					<th>예정 금액</th>
					<th>실 입금액</th>
					<th>차액</th>
					<th>차감</th>
					<th>과입보류</th>
					<th rowspan="2">사유</th>
				</tr>
				<tr>
					<th class="digits" id="amount-sum"></th>
					<th class="digits" id="stlVanFee-sum"></th>
					<th class="digits" id="calcAmount-sum"></th>
					<th class="digits" id="collectAmount-sum"></th>
					<th class="digits" id="deductAmount-sum"></th>
					<th class="digits" id="lowAmount-sum"></th>
					<th class="digits" id="overAmount-sum"></th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="9">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td><a class="btn btn-sm blue-dark" href="javascript:trxCapCollectPop('/trx/cap/collect/maded/${entry.collectId}/${entry.mchtId}')">거래내역</a></td>
						<td class="date">${entry.collectDay}</td>
						<td style="mso-number-format:'\@'">${entry.mchtId}</td>
						<td style="mso-number-format:'\@'">${entry.name}</td>
						<td class="amount digits" data-amt="${entry.amount}">${entry.amount}</td>
						<td class="stlVanFee digits" data-amt="${entry.stlVanFee}">${entry.stlVanFee}</td>
						<td class="calcAmount digits" data-amt="${entry.calcAmount}">${entry.calcAmount}</td>
						<td class="collectAmount digits" data-amt="${entry.collectAmount}">${entry.collectAmount}</td>
						<td class="deductAmount" data-amt="${entry.deductAmount }">${entry.deductAmount }</td>
						<td class="lowAmount" data-amt=""></td>
						<td class="overAmount" data-amt=""></td>
						<td>${entry.summary}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
