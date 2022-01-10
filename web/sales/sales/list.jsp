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
					<th rowspan="2">No</th>
					<th rowspan="2">매입월</th>
					<th rowspan="2">지사</th>
					<th colspan="2">매입현황</th>
					<th colspan="2">매입취소현황</th>
					<th colspan="4">합계</th>
					<th rowspan="2">지사<br>수익</th>
				</tr>
				<tr>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>정산금액</th>
					<th>가맹점수수료</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code == 200}">
					<tr class="lightgray">
						<td colspan="3">합계</td>
						<td class="text-right"><fmt:formatNumber type="number" value="${SUMMAP.payAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${SUMMAP.payCnt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdCnt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.totalAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.totalCnt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.stlAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.stlFee}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.stlSalesFee}" pattern="#,##0" /></td>
					</tr>
				</c:if>
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="19">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="date">${entry.capMonth}</td>
						<td>${entry.salesName}(${entry.agencyName})</td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.payAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.payCnt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdCnt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.totalAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.totalCnt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.stlAmt}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.stlFee}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.stlSalesFee}" pattern="#,##0" /></td>
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
