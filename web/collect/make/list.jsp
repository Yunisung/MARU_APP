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
	
	<script>
		function histoyBack() {
			history.back(-1);
		}
	</script>
	
	<div class="actions">
		<a class="btn btn-circle btn-icon-only btn-default" onClick="histoyBack()" data-original-title="" title="">
			<i class="fa fa-list" aria-hidden="true"></i>
		</a>
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
					<th>No</th>
					<th>거래내역</th>
					<th>입금예정일</th>
					<th>VAN</th>
					<th>VAN ID</th>
					<th>가맹점ID</th>
					<th>가맹점</th>
					<th>매입금액</th>
					<th>입금 수수료</th>
					<th>예정 금액<br><span class="digits" id="collect-sum"></span></th>
					<th style="padding-left:15px;text-align:left;">계좌 내역: <span id="real-def"></span>
					<br>실 입금액: <span id="real-sum"></span></th>
					<th>차액<br><span id="diffrence-sum"></span></th>
					<th>차감<br><span id="low-sum"></span></th>
					<th>과입보류<br><span id="over-sum"></span></th>
					<th class="excel-hide">차감/과입 반영</th>
					<th class="excel-hide"><a class="btn btn-sm purple-plum" href="javascript:collectDtlMake('${entry.stlVanDay}','')">전체저장</a></th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="19">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td>
							<%-- <a class="btn btn-sm blue-dark" href="javascript:trxCapCollectPop('/trx/cap/collect/${entry.stlVanDay}/${entry.mchtId}')">거래내역</a></td> --%>
							<a class="btn btn-sm blue-dark" href="javascript:trxCapCollectPop('/trx/cap/collect/temp/${entry.stlVanDay}/${entry.mchtId}/${entry.vanId}')">거래내역</a>
						<td class="date">${entry.stlVanDay}</td>
						<td>${entry.van}</td>
						<td class="out-col-vanId" style="mso-number-format:'\@'">${entry.vanId}</td>
						<td class="out-col-mchtId" style="mso-number-format:'\@'">${entry.mchtId}</td>
						<td style="mso-number-format:'\@'">${entry.name}</td>
						<td class="out-col-amount"><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td class="out-col-stlVanFee"><fmt:formatNumber type="number" value="${entry.stlVanFee}" pattern="#,##0" /></td>
						<td class="out-col-calcAmount" data-amt="${entry.MARUAmt}"><fmt:formatNumber type="number" value="${entry.MARUAmt}" pattern="#,##0" /></td>
						<td class="out-col-collectAmount"><input class="collect-input" name="collect-${entry.stlVanDay}-${entry.vanId}" placeholder="실입금액" value="<c:if test="${entry.isSaved != 'TRUE'}">${entry.collectAmt}</c:if><c:if test="${entry.isSaved == 'TRUE'}">${entry.savedCollectAmount}</c:if>"></td>
						<td class="diffrenceAmount" data-amt=""></td>
						<td class="lowAmount" data-amt=""></td>
						<td class="overAmount" data-amt=""></td>
						<td><input class="summary" value="${entry.summary }"></td>
						<td>
							<c:if test="${entry.isSaved != 'TRUE'}">
								<a class="btn btn-sm purple-plum" href="javascript:collectDtlMake('${entry.stlVanDay}','${entry.mchtId}','${entry.vanId}')">저장</a>
							</c:if>
							<c:if test="${entry.isSaved == 'TRUE'}">
								<a class="btn btn-sm green-sharp" href="javascript:collectDtlMake('${entry.stlVanDay}','${entry.mchtId}','${entry.vanId}')">수정</a>
							</c:if>	
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
