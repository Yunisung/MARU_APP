<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
		<span class="caption-helper"><span id="page-total">
		</span> ${fn:length(CPR.data)} 건 </span>
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
					<th>No</th>
					<th class="excel-hide">거래내역</th>
					<th>입금예정일</th>
					<th>VAN</th>
					<th>VAN ID</th>
					<th>VANID 이름</th>
					<th>터미널ID</th>
					<th>가맹점ID</th>
					<th>가맹점</th>
					<th>가맹점대표</th>
					<th>매입금액</th>
					<th>입금 수수료</th>
					<th>예정 금액<br><span class="digits" id="collect-sum"></span></th>
					<th>정산 금액<br><span id="MARU-sum"></span></th>
					<th>차액</th>
					<th class="excel-hide">Action</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="16">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:set var="var1" value="0"/>
				<c:set var="var2" value="0"/>
				<c:set var="var3" value="0"/>
				<c:set var="var4" value="0"/>
				<c:set var="var5" value="0"/>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td class="excel-hide">
							<a class="btn btn-sm blue-dark" href="javascript:trxCapCollectPop('/trx/cap/collect/temp/${entry.stlVanDay}/${entry.mchtId}/${entry.vanId}')">거래내역</a>
						</td>
						<td class="date">${entry.stlVanDay}</td>
						<td style="mso-number-format:'\@'">${entry.van}</td>
						<td style="mso-number-format:'\@'">${entry.vanId}</td>
						<td style="mso-number-format:'\@'">${entry.vanName}</td>
						<td style="mso-number-format:'\@'">${entry.tmnId}</td>
						<td class="out-col-mchtId" style="mso-number-format:'\@'">${entry.mchtId}</td>
						<td style="mso-number-format:'\@'">${entry.name}</td>
						<td style="mso-number-format:'\@'">${entry.ceoName}</td>
						<td class="text-right out-col-amount"><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td class="text-right out-col-stlVanFee"><fmt:formatNumber type="number" value="${entry.stlVanFee}" pattern="#,##0" /></td>
						<td class="out-col-collectAmount" 	data-amt="${entry.collectAmount}"><fmt:formatNumber 	type="number" value="${entry.collectAmount}" 		pattern="#,##0" /></td>
						<td class="out-col-MARUAmount" data-amt="${entry.MARUAmount}"><fmt:formatNumber type="number" value="${entry.MARUAmount}" 	pattern="#,##0" /></td>
						<td class="differenceAmount" data-amt=""></td>
						<td class="excel-hide">
							<c:if test="${entry.amount <= 0}">
								<a class="btn btn-sm green-dark" onClick="makeCollect('${entry.stlVanDay}','${entry.vanId}','vanId','${entry.collectAmount}')" style="margin-bottom:3px;"> VAN ID로 반영 </a>
								<a class="btn btn-sm blue-dark" onClick="makeCollect('${entry.stlVanDay}','${entry.mchtId}','mcht','${entry.collectAmount}')">가맹점 ID로 반영</a>
							</c:if>
						</td>
					</tr>
					<c:set var="var1"  value="${var1 + entry.amount}"/>
					<c:set var="var2"  value="${var2 + entry.stlVanFee}"/>
					<c:set var="var3"  value="${var3 + entry.collectAmount}"/>
					<c:set var="var4"  value="${var4 + entry.MARUAmount}"/>
					<c:set var="var5"  value="${var5 + (entry.collectAmount - entry.MARUAmount)}"/>
				</c:forEach>
				<tr>
						<td></td>
						<td class="excel-hide"></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td>합계</td>
						<td class="text-right digits"><c:out value="${var1}" /></td>
						<td class="text-right digits"><c:out value="${var2}" /></td>
						<td class="text-right digits"><c:out value="${var3}" /></td>
						<td class="text-right digits"><c:out value="${var4}" /></td>
						<td class="text-right digits"><c:out value="${var5}" /></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
