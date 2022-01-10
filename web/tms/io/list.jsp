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
		<!-- <a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForExcel();">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForPDF();">
			<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		</a> -->
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
					<th>주문번호</th>
					<th>가맹점 ID</th>
					<th>터미널 ID</th>
					<th>구분</th>
					<th>메시지</th>
					<th>금액</th>
					<th>할부</th>
					<th>VAN TID</th>
					<th>거래번호</th>
					<th>등록일시</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="11">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.trackId}</td>
						<!-- <td class="link_modal" data-url="/tms/io/view/${entry.trackId}">${entry.trackId}</td> -->
						<!-- <td class="text-left">
							<c:choose>
								<c:when test="${fn:indexOf(entry.uri,'summary') > -1 }">
									Dashboard
								</c:when>
								<c:when test="${fn:indexOf(entry.uri,'list') > -1 }">
									거래내역
								</c:when>
								<c:when test="${fn:indexOf(entry.uri,'statistics') > -1 }">
									집계
								</c:when>
								<c:when test="${fn:indexOf(entry.uri,'push') > -1 }">
									거래등록 
									<c:if test="${fn:indexOf(entry.regData,'trxId') > 0 }">
										(취소)
									</c:if>
									<c:if test="${fn:indexOf(entry.regData,'trxId') < 0 }">
										(승인)
									</c:if>
									
								</c:when>
								<c:when test="${fn:indexOf(entry.uri,'key') > -1 }">
									<c:if test="${entry.uri eq '/v0/key' }">
										앱 등록
									</c:if>
									<c:if test="${entry.uri ne '/v0/key' }">
										 <c:set var="version" value="${fn:replace(entry.uri,'/v0/key/', '')}" />
										앱 시작 (${version})
									</c:if>
									
								</c:when>
								<c:when test="${fn:indexOf(entry.uri,'crule') > -1 }">
									취소요청
								</c:when>
								<c:when test="${fn:indexOf(entry.uri,'rule') > -1 }">
									승인요청
								</c:when>
								<c:otherwise>${entry.uri }</c:otherwise>
							</c:choose>
						</td> -->
						<td>${entry.mchtId}</td>
						<td>${entry.tmnId}</td>
						<td>${entry.status}</td>
						<td>${entry.message}</td>
						<td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td>${entry.installment}</td>
						<td>${entry.secondKey}</td>
						<td>${entry.trxId}</td>
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
