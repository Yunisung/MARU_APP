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
	 		<span class="caption-detail"> 결제금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.amountSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 가맹점 수수료 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.feeSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 정산액 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.stlAmountSum}" pattern="#,##0" />원</span>  
		 </span>
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForExcel();">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForPDF();">
			<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		</a>
		<%-- 
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '자동 정산내역');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>--%>
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
					<th>정산번호</th>
					<th>거래유형</th>
					<th>정산주기</th>
					<th>지급상태</th>
					<th style="min-width: 80px;">정산일</th>
					<th style="min-width: 150px;">가맹점명</th>
					<th>가맹점ID</th>
					<th style="min-width: 100px;">대상거래 기간</th>
					<th>매입/입금건수</th>
					<th>매입/입금금액</th>
					<th>매입취소/입금취소건수</th>
					<th>매입취소/입금취소금액</th>
					<th>합계건수</th>
					<th>합계금액</th>
					<th>가맹점수수료</th>
					<th>정산액</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="24">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr> 
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.stlId}</td>
						<td>${entry.payType}</td>
						<td>${entry.stlType}</td>
						<td>${entry.status}</td>
						<td class="date">${entry.stlDay}</td>
						<td>${entry.name}</td>
						<td>${entry.mchtId}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right digits">${entry.payCnt}</td>
						<td class="text-right digits">${entry.payAmt}</td>
						<td class="text-right digits">${entry.rfdCnt}</td>
						<td class="text-right digits">${entry.rfdAmt}</td>
						<td class="text-right digits">${entry.totCnt}</td>
						<td class="text-right digits">${entry.totAmt}</td>
						<td class="text-right digits">${entry.stlFee}</td>
						<td class="text-right digits">${entry.stlAmount}</td>
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
			<script type="text/javascript">
				$('#pageSize').find('option[value="${CPR.page.size}"]').attr("selected", "selected");
			</script>
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
