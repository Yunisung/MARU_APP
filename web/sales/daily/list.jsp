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
					<th rowspan="2">No</th>
					<th rowspan="2">거래일시</th>
					<c:if test="${CP_SESSION.grade ne '가맹점' }">
					<th colspan="5">매입현황</th>
					<th colspan="5">매입취소현황</th>
					<c:if test="${SUMMAP.rfdedCount > 0}">
					<th colspan="5">정산취소현황</th>
					</c:if>
					</c:if>
					<c:if test="${CP_SESSION.grade eq '가맹점' }">
					<th colspan="4">매입현황</th>
					<th colspan="4">매입취소현황</th>
					<c:if test="${SUMMAP.rfdedCount > 0}">
					<th colspan="4">정산취소현황</th>
					</c:if>
					</c:if>
					<c:choose>
						<c:when test="${CP_SESSION.grade eq '본사' }">
							<th rowspan="2">본사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '대행사' }">
							<th rowspan="2">대행사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '에이전시' }">
							<th rowspan="2">에이전시 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '지사' }">
							<th rowspan="2">지사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '선정산' }">
							<th rowspan="2">선정산 수익</th>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<th>금액</th>
					<th>건수</th>
					<th>정산금액</th>
					<th>수수료</th>
					<c:choose>
						<c:when test="${CP_SESSION.grade eq '본사' }">
							<th>본사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '대행사' }">
							<th>대행사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '에이전시' }">
							<th>에이전시 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '지사' }">
							<th>지사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '선정산' }">
							<th>선정산 수익</th>
						</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					<th>금액</th>
					<th>건수</th>
					<th>정산금액</th>
					<th>수수료</th>
					<c:choose>
						<c:when test="${CP_SESSION.grade eq '본사' }">
							<th>본사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '대행사' }">
							<th>대행사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '에이전시' }">
							<th>에이전시 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '지사' }">
							<th>지사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '선정산' }">
							<th>선정산 수익</th>
						</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					<c:if test="${SUMMAP.rfdedCount > 0}">
					<th>금액</th>
					<th>건수</th>
					<th>정산금액</th>
					<th>수수료</th>
					<c:choose>
						<c:when test="${CP_SESSION.grade eq '본사' }">
							<th>본사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '대행사' }">
							<th>대행사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '에이전시' }">
							<th>에이전시 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '지사' }">
							<th>지사 수익</th>
						</c:when>
						<c:when test="${CP_SESSION.grade eq '선정산' }">
							<th>선정산 수익</th>
						</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					</c:if>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="18">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:if test="${CPR.result.code == 200}">
					<tr class="lightgray">
						<td colspan="2">합계</td>
						<td class="text-right"><fmt:formatNumber type="number" value="${SUMMAP.saleAmount}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${SUMMAP.saleCount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.saleStlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.saleStlFee}" pattern="#,##0" /></td>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${SUMMAP.saleBenefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${SUMMAP.saleStlDistFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${SUMMAP.saleStlAgencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${SUMMAP.saleStlSalesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${SUMMAP.saleStlLoanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdCount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdStlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdStlFee}" pattern="#,##0" /></td>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${SUMMAP.rfdBenefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${SUMMAP.rfdStlDistFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${SUMMAP.rfdStlAgencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${SUMMAP.rfdStlSalesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${SUMMAP.rfdStlLoanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						<c:if test="${SUMMAP.rfdedCount > 0}">
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdedAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdedCount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdedStlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${SUMMAP.rfdedStlFee}" pattern="#,##0" /></td>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${SUMMAP.rfdedBenefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${SUMMAP.rfdedStlDistFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${SUMMAP.rfdedStlAgencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${SUMMAP.rfdedStlSalesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${SUMMAP.rfdedStlLoanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						</c:if>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${SUMMAP.benefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${SUMMAP.distFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${SUMMAP.agencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${SUMMAP.salesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${SUMMAP.loanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="date">${entry.capDay}</td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.saleAmount}" pattern="#,##0" /></td>
						<td class="text-right"><fmt:formatNumber type="number" value="${entry.saleCount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.saleStlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.saleStlFee}" pattern="#,##0" /></td>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${entry.saleBenefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${entry.saleStlDistFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${entry.saleStlAgencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${entry.saleStlSalesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${entry.saleStlLoanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						<td><fmt:formatNumber type="number" value="${entry.rfdAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdCount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdStlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdStlFee}" pattern="#,##0" /></td>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${entry.rfdBenefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${entry.rfdStlDistFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${entry.rfdStlAgencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${entry.rfdStlSalesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${entry.rfdStlLoanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						<c:if test="${SUMMAP.rfdedCount > 0}">
						<td><fmt:formatNumber type="number" value="${entry.rfdedAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdedCount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdedStlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.rfdedStlFee}" pattern="#,##0" /></td>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${entry.rfdedBenefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${entry.rfdedStlDistFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${entry.rfdedStlAgencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${entry.rfdedStlSalesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${entry.rfdedStlLoanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						</c:if>
						<c:choose>
							<c:when test="${CP_SESSION.grade eq '본사' }">
								<td class="digits">${entry.benefit}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '대행사' }">
								<td class="digits">${entry.distFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '에이전시' }">
								<td class="digits">${entry.agencyFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '지사' }">
								<td class="digits">${entry.salesFee}</td>
							</c:when>
							<c:when test="${CP_SESSION.grade eq '선정산' }">
								<td class="digits">${entry.loanFee}</td>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
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
				$('#pageSize').find('option[value="${CPR.page.size}"]').attr(
						"selected", "selected");
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
