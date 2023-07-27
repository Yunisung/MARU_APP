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
					<th data-sort="string">매입사</th>
					<th data-sort="string">2개월</th>
					<th data-sort="string">3개월</th>
					<th data-sort="string">4개월</th>
					<th data-sort="string">5개월</th>
					<th data-sort="string">6개월</th>
					<th data-sort="string">7개월</th>
					<th data-sort="string">8개월</th>
					<th data-sort="string">9개월</th>
					<th data-sort="string">10개월</th>
					<th data-sort="string">11개월</th>
					<th data-sort="string">12개월</th>
					<th data-sort="string">13개월</th>
					<th data-sort="string">14개월</th>
					<th data-sort="string">15개월</th>
					<th data-sort="string">16개월</th>
					<th data-sort="string">17개월</th>
					<th data-sort="string">18개월</th>
					<th data-sort="string">19개월</th>
					<th data-sort="string">20개월</th>
					<th data-sort="string">21개월</th>
					<th data-sort="string">22개월</th>
					<th data-sort="string">23개월</th>
					<th data-sort="string">24개월</th>
					<th data-sort="string">등록자</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="27">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.van}</td>
						<td>${entry.acquirer}</td>
						<td><fmt:formatNumber value="${entry.m02 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m03 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m04 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m05 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m06 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m07 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m08 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m09 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m10 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m11 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m12 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m13 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m14 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m15 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m16 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m17 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m18 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m19 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m20 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m21 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m22 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m23 * 100}" pattern="0.000"/> %</td>
						<td><fmt:formatNumber value="${entry.m24 * 100}" pattern="0.000"/> %</td>
						<td>${entry.regId}</td>
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
