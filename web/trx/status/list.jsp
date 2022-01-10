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
			금액 합계: <fmt:formatNumber type="number" value="${AMOUNT_SUM }" pattern="#,##0" /> 원
		</span>
	</div>
	<div class="actions">
		<c:if test="${CP_SESSION.grade == '본사' }">
		<div class="btn-group">
			<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
				&nbsp;<i class="fa fa-exclamation"></i>&nbsp; 선택항목 리스크 변경&nbsp; <i class="fa fa-angle-down"></i>
			</a>
			<ul class="dropdown-menu pull-right">
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="">
						<i class="fa fa-check-square-o"></i> 리스크 해제
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="건한도">
						<i class="fa fa-check-square-o"></i> 건한도
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="중복">
						<i class="fa fa-check-square-o"></i> 중복
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="고액">
						<i class="fa fa-check-square-o"></i> 고액
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="최소금액">
						<i class="fa fa-check-square-o"></i> 최소금액
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="야간할부">
						<i class="fa fa-check-square-o"></i> 야간할부
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="1일중복">
						<i class="fa fa-check-square-o"></i> 1일중복
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="주간할부">
						<i class="fa fa-check-square-o"></i> 주간할부
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="야간건한도">
						<i class="fa fa-check-square-o"></i> 야간건한도
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="위험">
						<i class="fa fa-check-square-o"></i> 위험
					</a>
				</li>
				<li><a href="javascript:;" class="trx-stl-status-change" data-status="관리자 설정">
						<i class="fa fa-check-square-o"></i> 관리자 설정
					</a>
				</li>
			</ul>
		</div>
		</c:if>
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
		<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>
					<th>No</th>
					<th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
					<th>거래일시</th>
					<th>매입번호</th>
					<th>가맹점</th>
					<th>가맹점대표</th>
					<th>가맹점ID</th>
					<th>터미널</th>
					<th>매입구분</th>
					<th>매입금액</th>
					<th>정산금액</th>
					<th>승인번호</th>
					<th>BIN</th>
					<th>Last4</th>
					<th>정산상태</th>
					<th>할부</th>
					<th>리스크</th>
					<!-- <th>처리내역</th> -->
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="13">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-capId="${entry.capId}">
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="btn-td">
							<input type="checkbox" class="row-check" id="${entry.capId}_check" class="checkbox-style" /><label for="${entry.capId}_check"></label>
						</td>
						<td class="date">${entry.trxDay}${entry.regTime }</td>
						<td class="link_modal" data-url="/trx/cap/view/${entry.capId}">${entry.capId}</td>
						<td>${entry.name}</td>
						<td>${entry.ceoName}</td>
						<td>${entry.mchtId}</td>
						<td>${entry.tmnId}</td>
						<td>${entry.capType}</td>
						<td class="text-right digits">${entry.amount}</td>
						<td class="text-right digits">${entry.stlAmount}</td>
						<td>${entry.authCd}</td>
						<td>${entry.bin}</td>
						<td>${entry.last4}</td>
						<td>${entry.stlStatus}</td>
						<td>${entry.installment}</td>
						<td>${entry.risk}</td>
						<%-- <td><a class="btn btn-sm blue" href="javascript:riskHistory('${entry.idx}', 'INACTIVE')">이력 확인</a></td> --%>
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
				<option value="1000">1000개씩 보기</option>
			</select>
			<script type="text/javascript">
				$('#pageSize').find('option[value="${CPR.page.size}"]').attr("selected", "selected");
			</script>
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
