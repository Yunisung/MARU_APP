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
					<th data-sort="string">아이디</th>
					<th data-sort="string">이름</th>
					<th data-sort="string">사업자(주민)번호</th>
					<th data-sort="string">대표자이름</th>
					<th data-sort="string">상태</th>
					<c:if test="${CP_SESSION.grade eq '본사' || ((CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사') && CP_SESSION.loanSettleStatus == 'Y')}">
					<th data-sort="string">대출정산</th>
					</c:if>
					<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사'}"><th data-sort="string">대행사</th></c:if>
					<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '에이전시'}"><th data-sort="string">에이전시</th></c:if>
					<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '에이전시'|| CP_SESSION.grade eq '지사'}"><th data-sort="string">지사</th></c:if>
					<th data-sort="string">예수금</th>
					<th data-sort="string">정산주기</th>
					<th data-sort="string">수수료</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<%--<th data-sort="string">선정산 수수료</th> --%>
						<th data-sort="string">대표가맹점</th>
					</c:if>
					<th data-sort="string">가상계좌 인증서비스</th>
					<th data-sort="string">등록자</th>
					<th data-sort="string">시작일자</th>
					<th data-sort="string">등록일시</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="18">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.mchtId}</td>
						<td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.name}</td>
						<td>${entry.maskidentity}</td>
						<td>${entry.ceoName}</td>
						<td><c:if test="${entry.status eq '대기'}">승인</c:if>${entry.status}</td>
						<c:if test="${CP_SESSION.grade eq '본사' || ((CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사') && CP_SESSION.loanSettleStatus == 'Y')}">
						<td>${entry.loanSettleStatus}</td>
						</c:if>
						<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사'}"><td>${entry.distName}</td></c:if>
						<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '에이전시'}"><td>${entry.agencyName}</td></c:if>
						<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '에이전시'|| CP_SESSION.grade eq '지사'}"><td>${entry.salesName}</td></c:if>
						<td class="digits link" data-url="/deposit/form/${entry.mchtId}">${entry.deposit}</td>
						<td>${entry.settleType}</td>
						<c:if test="${CP_SESSION.grade == '본사'}">
						<td><fmt:formatNumber value="${entry.rate * 100}" pattern="0.000"/> %</td>
						<%--<td><fmt:formatNumber value="${entry.loanRate * 100}" pattern="0.000"/> %</td>--%>
						<td>${entry.aggregator}</td>
						</c:if>
						<c:if test="${CP_SESSION.grade != '본사'}">
							<td><fmt:formatNumber value="${entry.rate * 100}" pattern="0.000"/> %</td>
						</c:if>
						<td>${entry.authType}</td>
						<td>${entry.regId}</td>
						<td>${entry.mchtActiveDate}</td>
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
