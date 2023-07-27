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
	</div>
	<div class="actions">
		<c:if test="${CP_SESSION.grade == '본사'}">
			<div class="btn-group">
				<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
					<i class="fa fa-bank"></i> 정산 기능 <i class="fa fa-angle-down"></i>
				</a>
				<ul class="dropdown-menu pull-right">
					<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role != '일반'}">
					<li><a href="javascript:;" class="settle-pay-out-check">
							<i class="fa fa-check-square-o"></i> 선택항목 확정 처리
						</a>
					</li>
					</c:if>
					<li><a href="javascript:;" class="settle-pay-complete">
							<i class="fa fa-check-square-o"></i> 선택항목 지급완료 처리
						</a>
					</li>
				</ul>
			</div>
			<div class="btn-group">
				<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
					<i class="fa fa-bank"></i> 은행 지급 데이터 <i class="fa fa-angle-down"></i>
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
					<th>정산번호</th>
					<th>정산일</th>
					<th style="min-width:150px;">지사</th>
					<th>확정 상태</th>
					<th>지불 상태</th>
					<th>대상 거래 기간</th>
					<th>매입건수</th>
					<th>매입금액</th>
					<th>매입취소건수</th>
					<th>매입취소금액</th>
					<th>합계건수</th>
					<th>합계금액</th>
					<th>수수료액<br>(VAT포함)</th>
					<th>차액정산수수료액<br>(VAT포함)</th>
					<th>정산금액<br>(VAT포함)</th>
					<th>실지급액<br>(VAT포함)</th>
					<th style="min-width:80px;">지급일</th>
					<th>비고</th>
					<c:if test="${ (CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
					<th style="min-width:170px;">확인<br><a class="btn btn-sm purple-plum" href="javascript:saveSettle('')">전체저장</a></th>
					</c:if>
					<th>은행명</th>
					<th>계좌번호</th>
					<th>예금주</th>
					<th>대상거래</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="23">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.stlId}"<c:if test="${status.index ne 0 && entry.stlDay ne CPR.data[status.index-1].stlDay}">
						class="bg-grey-cararra bg-font-grey-cararra"
					</c:if>>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="btn-td">
							<input type="checkbox" class="row-check" id="${entry.stlId}_check" class="checkbox-style" /><label for="${entry.stlId}_check"></label>
						</td>
						<%--<td class="link_modal" data-url="/settle/modal/${entry.stlId}">${entry.stlId}</td> --%>
						<td class="out-col-stlId">${entry.stlId}</td>
						<td>${entry.stlDay}</td>
						<td>${entry.memberName}</td>
						<td>${entry.status}</td>
						<td>${entry.payStatus}</td>
						<td>${entry.startDay} ~ ${entry.endDay}</td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.payCnt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.payAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.rfdCnt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.rfdAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.totalCnt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.totalAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.stlFee}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.stlDiffFee}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.stlAmt}" pattern="#,##0" /></td>
					<c:choose>
						<c:when test="${ (CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
							<td class="text-right"><input type="text" name="payOutAmt" value="${entry.payOutAmt }" class="collect-input payOutAmt${entry.stlId}"/></td>
						</c:when>
						<c:otherwise>
							<td class="text-right"><fmt:formatNumber type="number" value="${entry.payOutAmt}" pattern="#,##0" /></td>
						</c:otherwise>
					</c:choose>
						<td>${entry.payOutDay}</td>
					<c:choose>
						<c:when test="${ (CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
							<td class="text-right"><input type="text" name="summary" value="${entry.summary }" class="summary"/></td>
						</c:when>
						<c:otherwise>
							<td>${entry.summary}</td>
						</c:otherwise>
					</c:choose>
					<c:if test="${(CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
							<td class="btn-td">
								<a class="btn btn-sm purple-plum" href="javascript:saveSettle('${entry.stlId}')">저장</a>
								<c:if test="${entry.status == '대기'}">
									<a class="btn green btn-sm settle-pay-out">확정</a>
									<a class="btn red-haze btn-sm settle-pay-hold">보류</a>
								</c:if>
								<c:if test="${entry.status == '보류'}">
									<a class="btn default btn-sm settle-pay-cancel">대기</a>
								</c:if>
								<c:if test="${entry.status == '확정' && entry.payStatus == '대기'}">
									<a class="btn blue-soft btn-sm settle-pay-cancel">확정 취소</a>
								</c:if>
							</td>
					</c:if>
						<td>${entry.bankName}</td>
						<td>${entry.account}</td>
						<td>${entry.accntHolder}</td>
						<td><a class="btn green btn-sm settle-detail" data-grade="stlSalesId">엑셀 다운로드</a></td>
					</tr>
				</c:forEach>
				<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.payCnt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.payAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.rfdCnt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.rfdAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.totalCnt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.totalAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.stlFee}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.stlDiffFee}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.stlAmt}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${CPR.sum.payOutAmt}" pattern="#,##0" /></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<c:if test="${(CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
						<td></td>
						</c:if>
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
<script type="text/javascript">
	<c:forEach var="entry" items="${CPR.data}" varStatus="status">
		$('.payOutAmt${entry.stlId}').val(addComma(String($('.payOutAmt${entry.stlId}').val())));
		
		$('.payOutAmt${entry.stlId}').keyup(function(){
			$('.payOutAmt${entry.stlId}').val(addComma(String($('.payOutAmt${entry.stlId}').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	</c:forEach>
		
</script>