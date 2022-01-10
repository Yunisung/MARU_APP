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
					<th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
					<th>No</th>
					<th style="min-width:140px;">거래일시</th>
					<th>거래번호</th>
					<th>거래상태</th>
					<th>가맹점ID</th>
					<th>가맹점명</th>
					<th>터미널ID</th>
					<th>주문번호</th>
					<th>금액</th>
					<th>상품유형</th>
					<th>주문자명</th>
					<th>VAN</th>
					<th>응답코드</th>
					<th>취소거래번호</th>
					<th>취소일자</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="11">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-capId="${entry.trxId}">
						<td class="btn-td">
							<input type="checkbox" class="row-check" id="${entry.trxId}_check" class="checkbox-style" /><label for="${entry.trxId}_check"></label>
						</td>		
						<!-- No --><td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<!-- 거래일시 --><td class="date">${entry.regDay}${entry.regTime}</td>
						<!-- 거래번호 --><td class="link_modal" data-url="/phone/view/${entry.trxId}">${entry.trxId}</td>
						<!-- 거래상태 --><td>${entry.status}</td>
						<!-- 가맹점ID --><td>${entry.mchtId}</td>
						<!-- 가맹점명 --><td title="${entry.mchtId }
									<c:choose>
									<c:when test="${CP_SESSION.grade == '본사'}">${entry.distName} &lt;${entry.agencyName}</c:when>
									<c:when test="${CP_SESSION.grade == '대행사'}"> &lt;${entry.agencyName}</c:when>
									<c:when test="${CP_SESSION.grade == '에이전시'}">${entry.salesName}</c:when>
									<c:otherwise>${entry.agencyName}</c:otherwise>
									</c:choose>">${entry.name}</td>
						<!-- 터미널ID --><td>${entry.tmnId}</td>			
						<!-- 주문번호 --><td>${entry.trackId}</td>
						<!-- 금액 --><td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<!-- 상품유형 --><td>${entry.prodType}</td>
						<!-- 상품유형 --><td>${entry.payerName}</td>
						<!-- VAN --><td>${entry.van}</td>
						<!-- 응답코드 --><td><c:if test="${entry.resultCd == 0000}">성공</c:if>
										  <c:if test="${entry.resultCd != '0000'}"><span onclick="failPop('${entry.resultMsg}')" style="cursor:pointer">실패</span></c:if>
									  </td>
						<!-- 취소거래번호 --><td class="link_modal" data-url="/phone/view/${entry.trxId}">${entry.rfdTrxId}</td>
						<!-- 취소일자 --><td class="date">${entry.rfdRegDay}${entry.rfdRegTime}</td>
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
