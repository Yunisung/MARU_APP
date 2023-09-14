<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
		<span class="caption-helper"><span id="page-total">총 ${CPR.page.total}</span> 건</span>
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
					<th data-sort="string">송금일시</th>
					<th data-sort="string">출금은행</th>
						<th data-sort="string">전문번호</th>
						<th data-sort="string">송금금액</th>
						<th data-sort="string">입금정보</th>
						<th data-sort="string">고객적요</th>
						<th data-sort="string">통장적요</th>
						<th data-sort="string">거래번호</th>
						<c:if test= "${CP_SESSION.role ne '일반'}">
						<th data-sort="string">잔액</th>
						</c:if>
						<th data-sort="string">응답시간</th>
						<th data-sort="string">결과메세지</th>
						<th data-sort="string">처리상태</th>
						<th data-sort="string">프로세스</th>
						<th data-sort="string">프로세스ID</th>
						
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="16">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
							<td class="date">${entry.sendDate}${entry.sendTime}</td>
							<td>
								<c:choose>
									<c:when test="${entry.bankCd=='089'}">
										케이뱅크
									</c:when>
									<c:when test="${entry.bankCd=='039'}">
										경남은행
									</c:when>
									<c:when test="${entry.bankCd=='034'}">
										광주은행
									</c:when>
								</c:choose>

							</td>
							<td>${entry.seqNo}</td>
							<td class="pull-right"><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
							<td>${entry.recvBankName},${entry.recvAccount}</td>
							<td>${entry.recvHolder}</td>
							<td>${entry.recordInfo}</td>
							<td>${entry.filler}</td>
							<c:if test= "${CP_SESSION.role ne '일반'}">
								<td><fmt:formatNumber type="number" value="${entry.balance}" pattern="#,##0" /></td>
							</c:if>
							<td class="date">${entry.recvDate}${entry.recvTime}</td>
							<td title="${entry.resultCd}">${entry.resultMsg}</td>
							<td>
							<c:choose>
								<c:when test="${entry.procGb == 'Y'}">
									<font color="blue">지급완료</font>
								</c:when>
								<c:when test="${entry.procGb == 'X' }">
									<font color="red">통신장애</font>
								</c:when>
								<c:when test="${entry.procGb == 'I' }">
									<font color="red">지급처리중</font>
								</c:when>
								<c:when test="${entry.procGb == 'N'}">
									<font color="green">지급실패</font>
								</c:when>
								<c:when test="${entry.procGb == 'R'}">
									<font>지급대기</font>
								</c:when>
								<c:otherwise>
									<font>${entry.procGb }</font>
								</c:otherwise>
							</c:choose>
						</td>
						<td> ${entry.procType}	</td>
						<td> ${entry.procId}	</td>
						
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


