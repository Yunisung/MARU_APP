<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
		<span class="caption-helper"><span id="page-total">${CPR.page.total}</span> 건</span>
		<span class="caption-helper amount_sum" style="color:#00a2ff;font-weight:600;">
		 		<span class="caption-detail"> 대출금액: <fmt:formatNumber type="number" value="${AMTSUMMAP.amountSum}" pattern="#,##0" />원,</span>  
		 		<span class="caption-detail"> 상환금액: <fmt:formatNumber type="number" value="${SUMMAP.paySum}" pattern="#,##0" />원</span>  
		</span>
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForExcel();">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForPDF();">
			<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
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
					<th>No</th>
					<th>구분</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<th>대출ID</th>
					</c:if>
					<c:if test="${CP_SESSION.grade ne '가맹점'}">
						<th>가맹점아이디</th>
						<th style="min-width:140px;">가맹점명</th>
					</c:if>
					<th style="min-width:100px;">일자</th>
					<th>대출금액</th>
					<th>상환금액</th>
					<th>중도상환금액</th>
					<th>잔액</th>
					<th>납입여부</th>
					<th>연체여부</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<th>대출정산번호</th>
					</c:if>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="15">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.loanType}</td>
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td>${entry.loanId}</td>
						</c:if>
						<c:if test="${CP_SESSION.grade ne '가맹점'}">
							<td>${entry.mchtId}</td>
							<td>${entry.name}</td>
						</c:if>
						<td class="date">${entry.trxDay}</td>
						<c:if test="${entry.loanType eq '대출실행'}">
							<td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
							<c:set var="amount" value="${amount + entry.amount}"/>
						</c:if>
						<c:if test="${entry.loanType ne '대출실행'}">
							<td></td>
						</c:if>
						<td><fmt:formatNumber type="number" value="${entry.payAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.prepayAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.balance}" pattern="#,##0" /></td>
						<td>${entry.payCk}</td>
						<td>${entry.delayCk}</td>
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td>${entry.loanStlId}</td>
						</c:if>
					</tr>
					
					
					<c:set var="payAmt" value="${payAmt + entry.payAmt}"/>
					<c:set var="prepayAmt" value="${prepayAmt + entry.prepayAmt}"/>
					
				</c:forEach>
				<tr  class="warning">
					<td>*</td>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<td colspan="5">합계</td>
					</c:if>
					<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '지사'}">
						<td colspan="4">합계</td>
					</c:if>
					<c:if test="${CP_SESSION.grade == '가맹점'}">
						<td colspan="2">합계</td>
					</c:if>
					<td><fmt:formatNumber type="number" value="${amount}" pattern="#,##0" /></td>
					<td><fmt:formatNumber type="number" value="${payAmt}" pattern="#,##0" /></td>
					<td><fmt:formatNumber type="number" value="${prepayAmt}" pattern="#,##0" /></td>
					<td>-</td>
					<td>-</td>
					<td>-</td>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<td>-</td>
					</c:if>
				</tr>
				
			</tbody>

		</table>
	</div>
	<div class="row">
		<div class="col-sm-4 col-xs-12">
			<select name="pageSize" id="pageSize" class="input-sm pull-left col-lg-4 col-sm-5 col-xs-12">
				<option value="20">20개씩 보기</option>
				<option value="50">50개씩 보기</option>
				<option value="100">100개씩 보기</option>
				<option value="200">200개씩 보기</option>
			</select>
			<script type="text/javascript">$('#pageSize').find('option[value="${CPR.page.size}"]').attr("selected", "selected");</script>
		</div>
		<div class="col-sm-4 col-xs-12">
			<div class="paging">
				<span>
					<a href="javascript:void(0);" class="mif-chevron-left mif-lg" id="pagePrev" onClick="pagePrev()">
						<i class="fa fa-angle-left" aria-hidden="true"></i>
					</a>
				</span>
				<span class="paging_num">
					Viewing
					<span>${CPR.page.current}</span>
					of ${CPR.page.totalPage}
				</span>
				<span>
					<a href="javascript:void(0);" class="mif-chevron-right mif-lg" id="pageNext" onClick="pageNext()">
						<i class="fa fa-angle-right" aria-hidden="true"></i>
					</a>
				</span>
				<input type="hidden" name="totalPage" id="totalPage" value="${CPR.page.totalPage}" />
			</div>
		</div>
		<div class="col-sm-4 col-xs-12">
			<a href="javascript:void(0);" id="pageMove" onClick="pageMove()" class="btn blue-dark btn-sm col-sm-4 col-xs-4 pull-right">페이지 이동</a>
			<span class="paging_num col-sm-4 col-xs-8 pull-right">
				<input type="text" class="form-control input-sm input_paging" name="paging" id="currentPage" value="${CPR.page.current}" />
			</span>
		</div>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
