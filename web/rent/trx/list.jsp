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
			출금 금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.depositAmt }" pattern="#,##0" /> 원 /
			입금 금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.withdrawAmt }" pattern="#,##0" /> 원
		</span>
	</div>
	<div class="actions">
<%--		<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">--%>
<%--			&nbsp;<i class="fa fa-exclamation"></i>&nbsp; 분납 이체&nbsp; <i class="fa fa-angle-down"></i>--%>
<%--		</a>--%>
<%--		<a class="btn btn-circle btn-default charge-settle-transfer" href="javascript:;" aria-expanded="false">--%>
<%--			<i class="fa fa-check-square-o"></i> 분납 이체--%>
<%--		</a>--%>
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
<%--					<th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>--%>
					<th>No</th>
					<th>거래번호</th>
					<th>납부구분</th>
					<th>거래종류</th>
					<th>거래유형</th>
					<th style="min-width:140px;">가맹점</th>
					<th>가맹점아이디</th>
					<th>주문번호</th>
					<th>참조번호</th>
					<th>입출금원금</th>
					<th>수수료</th>
					<th>수수료부가세</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<th>은행수수료</th>
					</c:if>
					<th>계정실출금액</th>
					<th>거래후잔액</th>
					<th>등록자</th>
					<th style="min-width:140px;">거래일시</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="17">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-trxId="${entry.trxId}">
<%--						<td class="btn-td">--%>
<%--							<c:choose>--%>
<%--								<c:when test="${entry.billingMethod eq '분납' && entry.firmTrxId eq ''}">--%>
<%--									<input type="checkbox" class="row-check" id="${entry.trxId}_check" class="checkbox-style" /><label for="${entry.trxId}_check"></label>--%>
<%--								</c:when>--%>
<%--								<c:when test="${entry.billingMethod eq '분납' && entry.firmTrxId ne ''}">--%>
<%--									<input type="checkbox" class="row-check" id="${entry.trxId}_check" class="checkbox-style" disabled /><label for="${entry.trxId}_check"></label>--%>
<%--								</c:when>--%>
<%--								<c:otherwise></c:otherwise>--%>
<%--							</c:choose>--%>
<%--						</td>--%>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="link_modal" data-url="/rent/trx/view/${entry.trxId}">${entry.trxId}</td>
						<td>${entry.billingMethod}</td>
						<td>${entry.trxType}</td>
						<td>${entry.trxUnit}</td>
						<td>${entry.name}</td>
						<td>${entry.mchtId}</td>
						<td>${entry.trackId}</td>
						<td>${entry.refId}</td>
						<td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.fee}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.feeVat}" pattern="#,##0" /></td>
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td><fmt:formatNumber type="number" value="${entry.bankFee}" pattern="#,##0" /></td>
						</c:if>
						<td><fmt:formatNumber type="number" value="${entry.netAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.balance}" pattern="#,##0" /></td>
						<td>${entry.regId}</td>
						<td class="date">${entry.trxDay}${entry.trxTime}</td>
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
		// 분납건 이체 처리
		$(document).on('click', '.charge-settle-transfer', function() {
			var stlStatus = $(this).attr("data-status");
			var trxId = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					trxId += "'" + $(e).attr('data-trxId') + "',";
				}
			});

			if (trxId.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				var $modal = $('#pgmate-modal');
				if ($modal.children().length < 1) {
					$modal.empty();
				}
				var url = '/rent/trx/transfer/view/' + trxId;

				$modal.load(url, '', function(responseTxt, statusTxt, xhr) {
					if (statusTxt == "success") {
						$modal.modal();
						textMask();
					}
				});

			}
		});
	</script>
</div>
<!-- 리스트 페이징 종료 -->
