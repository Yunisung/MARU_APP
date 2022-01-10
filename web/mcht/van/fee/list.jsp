<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> RESULT </span>
		<span class="caption-helper"><span id="page-total">${CPR.page.total}</span> 건 조회됨.</span>
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
					<th data-sort="string">VAN</th>
					<th data-sort="string">정산유형</th>
					<th data-sort="string">휴대폰 결제 수수료</th>
					<th data-sort="string">Credit Card</th>
					<th data-sort="string">Check Card</th>
					<th data-sort="string">Credit Card(5억 미만)</th>
					<th data-sort="string">Check Card(5억 미만)</th>
					<th data-sort="string">영중소차액(영세/신용)</th>
					<th data-sort="string">영중소차액(중소1/신용)</th>
					<th data-sort="string">영중소차액(중소2/신용)</th>
					<th data-sort="string">영중소차액(중소3/신용)</th>
					<th data-sort="string">영중소차액(영세/체크)</th>
					<th data-sort="string">영중소차액(중소1/체크)</th>
					<th data-sort="string">영중소차액(중소2/체크)</th>
					<th data-sort="string">영중소차액(중소3/체크)</th>
					<th data-sort="string">등록자</th>
					<th data-sort="string" style="min-width:200px;">등록일시</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="10">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.van}</td>
						<td>${entry.settleType}</td>
						<td><fmt:formatNumber value="${entry.phoneRate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.creditRate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.checkRate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.credit5Rate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.check5Rate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff1Rate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff2Rate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff3Rate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff4Rate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff1CheckRate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff2CheckRate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff3CheckRate * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.diff4CheckRate * 100}" pattern="0.000"/> %</td>
						<td>${entry.regId}</td>
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
