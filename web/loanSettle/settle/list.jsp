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
		 		<span class="caption-detail"> 실 지급액 합계: <fmt:formatNumber type="number" value="${totAmt}" pattern="#,##0" />원</span>  
		</span>
	</div>
	<div class="actions">
		<c:if test="${CP_SESSION.grade eq '본사'}">
		<div class="btn-group">
			<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
				<i class="fa fa-bank"></i>정산 기능 <i class="fa fa-angle-down"></i>
			</a>
			<ul class="dropdown-menu pull-right">
				<li><a href="javascript:;" class="settle-loan-payout" data-status="지급완료">
						<i class="fa fa-check-square-o"></i> 선택항목 지급 완료 처리
					</a>
				</li>
				<li><a href="javascript:;" class="settle-loan-payout" data-status="지급보류">
						<i class="fa fa-check-square-o"></i> 선택항목 지급 보류 처리
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
					<th rowspan="3">No</th>
					<c:if test="${CP_SESSION.grade eq '본사' }">
						<th class="ck-th" rowspan="3"><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
						<th>대출정산번호</th>
					</c:if>
					<th>지급상태</th>
					<th>지급일</th>
					<th>소속</th>
					<th>소속ID</th>
					<th>소속명</th>
					<th>대표자명</th>
					<th style="min-width: 100px;">대상거래 기간</th>
					<th>실지급액</th>
					<th style="min-width: 200px;" colspan="3">계좌정보</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
				 		<c:choose> 
						<c:when test="${CP_SESSION.grade eq '본사'}">
										<td colspan="24">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
						</c:when>
						<c:when test="${CP_SESSION.grade ne '본사'}">
										<td colspan="21">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
						</c:when>
						</c:choose>
					</tr>
				</c:if>
	<c:set var="var1" value="0"/>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-loanStlId="${entry.loanStlId}" 
					<c:if test="${status.index ne 0 && entry.payOutDay ne CPR.data[status.index-1].payOutDay}">
						class="bg-grey-cararra bg-font-grey-cararra"
					</c:if>>
						<td>${status.count}</td>
						<c:if test="${CP_SESSION.grade eq '본사' }">
						<td class="ck-td btn-td">
							<input type="checkbox" class="row-check" id="${entry.loanStlId}_check" class="checkbox-style" /><label for="${entry.loanStlId}_check"></label>
						</td>
						
						<td>${entry.loanStlId}</td>
						</c:if>
						<td>${entry.payStatus}</td>
						<td class="date">${entry.payOutDay}</td>
						<td>${entry.grade}</td>
						<c:if test="${entry.grade eq '대행사' }">
							<td>${entry.distId}</td>
						</c:if>
						<c:if test="${entry.grade eq '에이전시' }">
							<td>${entry.agencyId}</td>
						</c:if>
						<c:if test="${entry.grade eq '지사' }">
							<td>${entry.salesId}</td>
						</c:if>
						<td>${entry.name}</td>
						<td>${entry.ceoName}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right digits">${entry.payAmt}</td>
						<td style="min-width:60px;">${entry.bankName}</td>
						<td style="mso-number-format:'\@'">${entry.account}</td>
						<td style="min-width:100px;">${entry.accntHolder}</td>
						
						<c:set var="var1"  value="${var1 +  entry.payAmt}"/>
					</tr>
				</c:forEach>
				<tr style="background-color:#f9e491;">
					<c:if test="${CP_SESSION.grade eq '본사' }">
						<td class="ck-td" ></td>
						<td ></td>
					</c:if>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td>합계</td>
					<td class="text-right digits"><c:out value="${var1}" /></td> <%-- ${entry.payAmt} --%>
					<td ></td>
					<td ></td>
					<td></td>
				</tr>	
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
