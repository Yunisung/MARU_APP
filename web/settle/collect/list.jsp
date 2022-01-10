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
		<div class="btn-group">
			<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
				<i class="fa fa-bank"></i> 지급 데이터 생성 <i class="fa fa-angle-down"></i>
			</a>
			<ul class="dropdown-menu pull-right">
				<li><a href="javascript:;" class="settle-pay-out-make" data-bank="020">
						<i class="fa fa-check-square-o"></i> 우리은행
					</a>
				</li>
				<li><a href="javascript:;" class="settle-pay-out-make" data-bank="004">
						<i class="fa fa-check-square-o"></i> 국민은행
					</a>
				</li>
				<li><a href="javascript:;" class="settle-pay-out-make" data-bank="081">
						<i class="fa fa-check-square-o"></i> 하나은행
					</a>
				</li>
			</ul>
		</div>
		<a href="javascript:;" class="btn btn-circle  btn-default settle-pay-complete">
			<i class="fa fa-check-square-o"></i> 선택항목 지급완료 처리
		</a>
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
					<th>정산번호</th>
					<th>정산일</th>
					<th>소속</th>
					<th>멤버ID</th>
					<th>가맹점</th>
					<th>확정 상태</th>
					<th>지불 상태</th>
					<th>대상 거래 기간</th>
					<th>정산금액</th>
					<th>출금 은행</th>
					<th>계좌번호</th>
					<th>예금주</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="13">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.stlId}">
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="btn-td">
							<input type="checkbox" class="row-check" id="${entry.stlId}_check" class="checkbox-style" /><label for="${entry.stlId}_check"></label>
						</td>
						<td class="link_modal" data-url="/settle/modal/${entry.stlId}">${entry.stlId}</td>
						<td class="date">${entry.stlDay}</td>
						<td>${entry.grade}</td>
						<td>${entry.memberId}</td>
						<td>${entry.memberName}</td>
						<td>${entry.status}</td>
						<td>${entry.payStatus}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right">
							<fmt:formatNumber type="number" value="${entry.stlAmt}" pattern="#,##0" />
						</td>
						<td>${entry.bankName}</td>
						<td>${entry.account}</td>
						<td>${entry.accntHolder}</td>
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
