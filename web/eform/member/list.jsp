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
					<th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
					<th>No</th>
					<th style="min-width:140px;">발송일시</th>
					<th>발송 번호</th>
					<th>발송 영업사</th>
					<th>계약서 제목</th>
					<th>수신자</th>
					<th style="min-width:140px;">수신일시</th>
					<th>현재상태</th>
					<th>처리결과</th>
					<th>비고</th>
					<th>만료기간</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="12">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-idx="${entry.idx}">
						<td class="btn-td">
							<input type="checkbox" class="row-check" id="${entry.idx}_check" class="checkbox-style" /><label for="${entry.idx}_check"></label>
						</td>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="date">${entry.regDay}${entry.regTime}</td>
						<td>${entry.idx}</td>
						<td>${entry.name}</td>
						<td>${entry.doc_name}</td>
						<td>${entry.receiver_name}</td>
						<td class="date">${entry.doc_date}</td>
						<td>
							<c:choose>
								<c:when test="${entry.doc_status eq 'WSI'}">휴대폰본인인증</c:when>
								<c:when test="${entry.doc_status eq 'WR'}">작성요청</c:when>
								<c:when test="${entry.doc_status eq 'WS'}">작성시작</c:when>
								<c:when test="${entry.doc_status eq 'WE'}">작성완료</c:when>
								<c:when test="${entry.doc_status eq 'WJ'}">작성거절</c:when>
								<c:when test="${entry.doc_status eq 'WX'}">작성기한만료</c:when>
								<c:when test="${entry.doc_status eq 'WC'}">전송취소</c:when>
							</c:choose>
						</td>
						<td>${entry.doc_result}</td>
						<td>${entry.doc_ref}</td>
						<td>${entry.expiration_date}</td>
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
