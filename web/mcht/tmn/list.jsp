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
					<th data-sort="string">가맹점ID</th>
					<th data-sort="string">가맹점명</th>
					<th data-sort="string">가맹점 대표자</th>
					<th data-sort="string">터미널ID</th>
					<th data-sort="string">상호</th>
					<th data-sort="string">Tax</th>
					<c:if test="${CP_SESSION.grade eq '본사'}">
					<th data-sort="string">VAN</th>
					<th data-sort="string">VAN정보</th>
					<th data-sort="string">웹결제창</th>
					<th data-sort="string">수기유형</th>
					<th data-sort="string">최대할부</th>
					<th data-sort="string">정산후취소</th>
					</c:if>
					<th data-sort="string">시작일자</th>
					<th data-sort="string">취급품목</th>
					<c:if test="${CP_SESSION.grade ne '가맹점' || CP_SESSION.aggregator == 'Y'}">
						<th data-sort="string">기본정보</th>
					</c:if>
					<c:if test="${CP_SESSION.aggregator == 'Y' || CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시'}">
						<th data-sort="string">추가정보</th>
					</c:if>
					<th data-sort="string">등록자</th>
					<th data-sort="string">등록일시</th>
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
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.mchtId}</td>
						<td>${entry.mchtName}</td>
						<td>${entry.ceoName}</td>
						<td>${entry.tmnId}</td>
						<td>${entry.dtlName}</td>
						<td>${entry.taxName}</td>
						<c:if test="${CP_SESSION.grade eq '본사'}">
							<c:if test="${not empty entry.van}">
							<td>${entry.van}</td>
							<td>${entry.vanName}(${entry.vanId})</td>
							</c:if>
							<c:if test="${empty entry.van}">
								<td>${entry.van}</td>
								<td colspan="" class="font-red">지정되지 않음</td>
							</c:if>
							<td>${entry.webPay}</td>
							<td>
								<c:if test="${entry.semiAuth == 'Y'}">비생</c:if>
								<c:if test="${entry.semiAuth == 'N'}">일반</c:if>
							</td>
							<td>${entry.apiMaxInstall}개월</td>
							<td>${entry.refundType}</td>
						</c:if>
						<td class="date">${entry.activeDate}</td>
						<td class="date">${entry.description}</td>
						<c:if test="${CP_SESSION.grade ne '가맹점' || CP_SESSION.aggregator == 'Y'}">
						<td class="link" data-url="/mcht/tmn/modify/${entry.tmnId}">
							<div class="btn btn-sm default">기본정보</div>
						</td>
						</c:if>
						<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.aggregator == 'Y' || CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시'}">
							<c:if test="${not empty entry.dtlName }">
								<td class="link" data-url="/mcht/tmnDtl/modify/${entry.tmnId}">
									<div class="btn btn-sm default">추가정보</div>
								</td>
							</c:if>
							<c:if test="${empty entry.dtlName }">
								<td class="link" data-url="/mcht/tmnDtl/add/${entry.tmnId}">
									<div class="btn btn-sm blue">추가정보 등록</div>
								</td>
							</c:if>
						</c:if>
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
