<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
		<style type="text/css">
			.caption-helper .caption-detail {
				padding-left:20px;
				color:#444d58;
			}
		</style>
		<span class="caption-helper">
		<span class="caption-helper"><span id="page-total">${CPR.page.total}</span> 건</span>
			<c:if test="${not empty SUMMAP}">
		 		<span class="caption-detail">최종정산일: <span class="date">${SUMMAP.stlDay}</span></span>  
		 		<span class="caption-detail">승인/취소 금액: <fmt:formatNumber type="number" value="${SUMMAP.payAmt}" pattern="#,##0" /> / 
		 		<fmt:formatNumber type="number" value="${SUMMAP.rfdAmt}" pattern="#,##0" /></span>  
		 		<span class="caption-detail">정산 금액: <fmt:formatNumber type="number" value="${SUMMAP.stlAmt}" pattern="#,##0" /></span>  
		 	</c:if>
		 </span>
		 
		
	</div>
	<div class="actions">
		<div class="btn-group">
			<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
				<i class="fa fa-bank"></i> 정산 기능 <i class="fa fa-angle-down"></i>
				
			</a>
			
			<ul class="dropdown-menu pull-right">
				<li><a href="javascript:;" class="settle-pay-out-check is-sub">
						<i class="fa fa-check-square-o"></i> 선택항목 확정 처리
					</a>
				</li>
				<li><a href="javascript:;" class="settle-pay-complete is-sub">
						<i class="fa fa-check-square-o"></i> 선택항목 지급완료 처리
					</a>
				</li>
			</ul>
		</div>
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
					<th>터미널</th>
					<th>상호</th>
					<th>확정</th>
					<th>지불</th>
					<th>대상거래 기간</th>
					<th>승인금액</th>
					<th>취소금액</th>
					<th>합계금액</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<th>실지급액</th>
					</c:if>
					<th>정산금액</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
						<th>비고</th>
					</c:if>
					<th>지급일</th>
					<th>대상거래</th>
					<c:choose>
						<c:when test="${ (CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
							<th style="min-width:170px;">확인<br><a class="btn btn-sm purple-plum" href="javascript:saveSettle('')">전체저장</a></th>
						</c:when>
						<c:otherwise>
							<th>확인</th>
						</c:otherwise>
					</c:choose>
</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="15">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.stlId}" 
					<c:if test="${status.index ne 0 && entry.stlDay ne CPR.data[status.index-1].stlDay}">
						class="bg-grey-cararra bg-font-grey-cararra is-sub"
					</c:if>>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="btn-td">
							<input type="checkbox" class="row-check is-sub" id="${entry.stlId}_check" class="checkbox-style" /><label for="${entry.stlId}_check"></label>
						</td>
						<td class="link_modal out-col-stlId" data-url="/settle/modal/sub/${entry.stlId}">${entry.stlId}</td>
						<td class="date">${entry.stlDay}</td>
						<td>${entry.tmnId}</td>
						<td>${entry.dtlName}</td>
						<td>${entry.status}</td>
						<td>${entry.payStatus}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right digits">${entry.payAmt}</td>
						<td class="text-right digits">${entry.rfdAmt}</td>
						<td class="text-right digits">${entry.payAmt + entry.rfdAmt}</td>
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td class="text-right"><input type="text" name="payOutAmt" value="${entry.payOutAmt }" class="collect-input payOutAmt${entry.stlId}"/></td>
						</c:if>
						<td class="text-right digits">${entry.stlAmt}</td>
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td class="text-right"><input type="text" name="summary" value="${entry.summary }" class="summary"/></td>
						</c:if>
						<td class="date">${entry.payOutDay}</td>
						<td><a class="btn green btn-sm settle-detail is-sub" data-grade="stlId">엑셀 다운로드</a></td>
						<td class="btn-td">
							<c:if test="${CP_SESSION.grade == '본사'}">
								<a class="btn btn-sm purple-plum" href="javascript:saveSettle('${entry.stlId}')">저장</a>
							</c:if>
							<c:if test="${CP_SESSION.grade == '가맹점'}">
								<c:if test="${entry.status == '대기'}">
									<a class="btn green btn-sm settle-pay-out is-sub">확정</a>
									<a class="btn red-haze btn-sm settle-pay-hold is-sub">보류</a>
								</c:if>
								<c:if test="${entry.status == '보류'}">
									<a class="btn default btn-sm settle-pay-cancel is-sub">대기</a>
								</c:if>
								<c:if test="${entry.status == '확정' && entry.payStatus == '대기'}">
									<a class="btn blue-soft btn-sm settle-pay-cancel is-sub">확정 취소</a>
								</c:if>
								<c:if test="${entry.payStatus == '지급완료'}">
									지급완료
								</c:if>
							</c:if>
						</td>
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
