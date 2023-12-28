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
			결과 금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.amount }" pattern="#,##0" /> 원
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
					<th style="min-width:140px;">가맹점</th>
					<th>가맹점아이디</th>
					<th>주문번호</th>
					<th>거래번호</th>
					<th>원거래번호</th>
					<th>참조번호</th>
					<th>거래구분</th>
					<th>결제유형</th>
					<th>납부구분</th>
					<th>입출금원금</th>
					<th>수수료</th>
					<th>수수료부가세</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<th>은행수수료</th>
					</c:if>
					<th>계정실출금액</th>
					<th style="min-width:140px;">거래일시</th>
					<th>지급예정일</th>
					<th>지급완료일</th>
					<th>지급상태</th>
					<th>계좌번호</th>
					<th>예금주</th>
					<th style="min-width:100px;">은행</th>
					<th>이체재시도</th>
					<th style="min-width:100px;">등록일시</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="24">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-trxId="${entry.trxId}">
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.name}</td>
						<td>${entry.mchtId}</td>
						<td>${entry.trackId}</td>
						<td>${entry.trxId}</td>
						<td>${entry.rootTrxId}</td>
						<td>${entry.refId}</td>
						<td>${entry.trxType}</td>
						<td>${entry.billingType}</td>
						<td>${entry.billingMethod}</td>
						<td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.fee}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.feeVat}" pattern="#,##0" /></td>
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td><fmt:formatNumber type="number" value="${entry.bankFee}" pattern="#,##0" /></td>
						</c:if>
						<td><fmt:formatNumber type="number" value="${entry.netAmount}" pattern="#,##0" /></td>
						<td class="date">${entry.payDay}${entry.payTime}</td>
						<td>${entry.pubDay}</td>
						<td>${entry.trxDay}</td>	<!-- == payOutDay -->
						<td>${entry.status}</td>
						<td>${entry.decAccount}</td>
						<td>${entry.decHolder}</td>
						<td>${entry.bankName}</td>
						<c:choose>
							<c:when test="${entry.status eq '실패' && entry.rootTrxId eq ''}">
								<td class="btn-td"><a class="btn green btn-sm" href="javascript:retryPayOut('${entry.trxId}')">이체</a></td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
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

	<script type="text/javascript">
		function retryPayOut(trxId, amount) {
			var $modal = $('#pgmate-modal');
			if ($modal.children().length < 1) {
				$modal.empty();
			}
			var url = '/rent/settle/retry/view/' + trxId;

			$modal.load(url, '', function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					$modal.modal();
					textMask();
				}
			});
		}
	</script>
</div>
<!-- 리스트 페이징 종료 -->
