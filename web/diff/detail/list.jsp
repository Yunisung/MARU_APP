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
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '차액정산 입금 내역 생성');">
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
					<th>본사차액정산액</th>
					<th>카드사입금예정액</th>
					<th>정산차액</th>
					<th>실 입금액</th>
					<th>입금차액</th>
					<th>차감</th>
					<th>과입보류</th>
					<th class="excel-hide" rowspan="2">사유</th>
				</tr>
				<tr>
					<th class="digits" id="amount-sum"></th>
					<th class="digits" id="diffAmount-sum"></th>
					<th class="digits" id="diffVanAmount-sum"></th>
					<th class="digits" id="diffSettleAmount-sum"></th>
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
						<td><a class="btn btn-sm blue-dark" href="javascript:trxCapCollectPop('/trx/cap/diff/maded/${entry.collectId}/${entry.mchtId}')">거래내역</a></td>
						<td class="date">${entry.collectDay}</td>
						<td style="mso-number-format:'\@'">${entry.mchtId}</td>
						<td style="mso-number-format:'\@'">${entry.name}</td>
						<td class="amount digits" data-amt="${entry.amount}">${entry.amount}</td>
						<td class="diffAmount digits" data-amt="${entry.diffAmount}">${entry.diffAmount}</td>
						<td class="diffVanAmount digits" data-amt="${entry.diffVanAmount}">${entry.diffVanAmount}</td>
						<td class="diffSettleAmount digits" data-amt="${entry.diffSettleAmount}">${entry.diffSettleAmount}</td>
						<td class="collectAmount digits" data-amt="${entry.collectAmount}">${entry.collectAmount}</td>
						<td class="deductAmount" data-amt="${entry.deductAmount }">${entry.deductAmount }</td>
						<td class="lowAmount" data-amt=""></td>
						<td class="overAmount" data-amt=""></td>
						<td class="excel-hide">${entry.summary}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
