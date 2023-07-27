<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<span id="page-total" style="display:none;">${CPR.page.total}</span>
	<div class="actions">
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForExcel();">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForPDF();">
			<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		</a>
		<%-- 
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '수납정산 내역');">
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
					<th rowspan="2">No</th>
					<th rowspan="2" style="min-width: 60px;">거래월</th>
					<th rowspan="2" style="min-width: 120px;">가맹점명</th>
					<th rowspan="2">가맹점ID</th>
					<th rowspan="2">거래금액</th>
					<th colspan="2">0회차(M+0)</th>
					<th colspan="2">1회차(M+1)</th>
					<th colspan="2">2회차(M+2)</th>
					<th colspan="2">3회차(M+3)</th>
					<th colspan="2">4회차(M+4)</th>
					<th colspan="2">5회차(M+5)</th>
					<th colspan="2">6회차(M+6)</th>
					<th colspan="2">7회차(M+7)</th>
					<th colspan="2">8회차(M+8)</th>
					<th colspan="2">9회차(M+9)</th>
					<th colspan="2">1회차(M+10)</th>
					<th rowspan="2">미납액</th>
					<th rowspan="2">누적 수납액</th>
					<th rowspan="2">최종 수납율</th>
				</tr>
				<tr>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
					<th>수납금액</th>
					<th style="min-width: 70px;">수납율</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="30">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td class="date">${entry.trxDay}</td>
						<td>${entry.name}</td>
						<td>${entry.mchtId}</td>
						<td class="text-right digits">${entry.amount}</td>
						<td class="text-right digits">${entry.m0_sunab}</td>
						<td class="text-right rate">${entry.m0_percent}</td>
						<td class="text-right digits">${entry.m1_sunab}</td>
						<td class="text-right rate">${entry.m1_percent}</td>
						<td class="text-right digits">${entry.m2_sunab}</td>
						<td class="text-right rate">${entry.m2_percent}</td>
						<td class="text-right digits">${entry.m3_sunab}</td>
						<td class="text-right rate">${entry.m3_percent}</td>
						<td class="text-right digits">${entry.m4_sunab}</td>
						<td class="text-right rate">${entry.m4_percent}</td>
						<td class="text-right digits">${entry.m5_sunab}</td>
						<td class="text-right rate">${entry.m5_percent}</td>
						<td class="text-right digits">${entry.m6_sunab}</td>
						<td class="text-right rate">${entry.m6_percent}</td>
						<td class="text-right digits">${entry.m7_sunab}</td>
						<td class="text-right rate">${entry.m7_percent}</td>
						<td class="text-right digits">${entry.m8_sunab}</td>
						<td class="text-right rate">${entry.m8_percent}</td>
						<td class="text-right digits">${entry.m9_sunab}</td>
						<td class="text-right rate">${entry.m9_percent}</td>
						<td class="text-right digits">${entry.m10_sunab}</td>
						<td class="text-right rate">${entry.m10_percent}</td>
						<td class="text-right digits">${entry.minabAmt}</td>
						<td class="text-right digits">${entry.SunabSumAmt}</td>
						<td class="text-right rate">${entry.tot_percent}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="row">
		<div class="col-sm-4 col-xs-12">
			<select name="pageSize" id="pageSize" class="input-sm pull-left col-lg-4 col-sm-5 col-xs-12">
				<option value="200">200개씩 보기</option>
				<option value="500">500개씩 보기</option>
				<option value="1000">1000개씩 보기</option>
				<option value="2000">2000개씩 보기</option>
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
