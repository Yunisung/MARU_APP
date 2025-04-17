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
			결과 금액 합계: <fmt:formatNumber type="number" value="${AMOUNT_SUM }" pattern="#,##0" /> 원
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
		<table class="pg-table table table-striped table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>
					<th>No</th>
					<th>거래번호</th>
					<th>가맹점</th>
					<th>금액</th>
					<th>출금계좌은행</th>
					<th>출금계좌번호</th>
					<th>가상계좌발행은행</th>
					<th>가상계좌번호</th>
					<th>보낸사람</th>
					<th>거래타입</th>
					<th>입금일자</th>
					<th>입금시간</th>
					<th>리스크</th>
					<th>등록일시</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="17">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.vactId}</td>
						<td title="${entry.mchtId }">${entry.mchtName}</td>
						<td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td>${entry.withdrawBankName}</td>
						<td>${entry.decWithdrawAccount}</td>
						<td>${entry.codeName}</td>
						<td>${entry.account}</td>
						<td>${entry.holderName}</td>
						<td>${entry.trxType}</td>
						<td>${entry.trxDay}</td>
						<td class="time">${entry.trxTime}</td>
						<td>${entry.risk}</td>
						<td class="date">${entry.regDate}</td>
					</tr>
				</c:forEach>
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
