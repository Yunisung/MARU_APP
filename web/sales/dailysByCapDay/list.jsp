<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '일마감-승인거래기준');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
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
					<th rowspan="3">기준일</th>
					<th colspan="6">매출</th>
					<th colspan="2">입금예정액</th>
					<th colspan="2">지급예정액</th>
					<th rowspan="3">예상 매출이익</th>
					<th colspan="4">예상 영업수수료</th>
					<th rowspan="3">차액정산입금액</th>
					<th rowspan="3">예상 영업이익</th>
				</tr>
				<tr>
					<th colspan="2">승인</th>
					<th colspan="2">취소</th>
					<th colspan="2">합계</th>
					<th rowspan="2">입금수수료</th>
					<th rowspan="2">입금액</th>
					<th rowspan="2">가맹점수수료</th>
					<th rowspan="2">금액</th>
					<th rowspan="2">대행사</th>
					<th rowspan="2">에이전시</th>
					<th rowspan="2">지사</th>
					<th rowspan="2">총합계</th>
				</tr>
				<tr>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="18">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:set var="payCnt" value="0"/>
				<c:set var="payAmt" value="0"/>
				<c:set var="rfdCnt" value="0"/>
				<c:set var="rfdAmt" value="0"/>
				<c:set var="totalCnt" value="0"/>
				<c:set var="totalAmt" value="0"/>
				<c:set var="stlVanFee" value="0"/>
				<c:set var="stlVanAmt" value="0"/>
				<c:set var="stlFee" value="0"/>
				<c:set var="stlAmt" value="0"/>
				<c:set var="profit" value="0"/>
				<c:set var="stlDistFee" value="0"/>
				<c:set var="stlAgencyFee" value="0"/>
				<c:set var="stlSalesFee" value="0"/>
				<c:set var="totSalesFee" value="0"/>
				<c:set var="stlDiffAmt" value="0"/>
				<c:set var="benefit" value="0"/>
				
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td class="digits right">${status.count}</td>
						<td><a href="#" onclick="openpop('/sales/dailysByCapDay/dtl/${entry.capDay}');">${entry.capDay}</a></td>
						<td class="digits right">${entry.payCnt}</td>
						<td class="digits right">${entry.payAmt}</td>
						<td class="digits right">${entry.rfdCnt}</td>
						<td class="digits right">${entry.rfdAmt}</td>
						<td class="digits right">${entry.totalCnt}</td>
						<td class="digits right">${entry.totalAmt}</td>
						<td class="digits right">${entry.stlVanFee}</td>
						<td class="digits right">${entry.stlVanAmt}</td>
						<td class="digits right">${entry.stlFee}</td>
						<td class="digits right">${entry.stlAmt}</td>
						<td class="digits right">${entry.profit}</td>
						<td class="digits right">${entry.stlDistFee}</td>
						<td class="digits right">${entry.stlAgencyFee}</td>
						<td class="digits right">${entry.stlSalesFee}</td>
						<td class="digits right">${entry.totSalesFee}</td>
						<td class="digits right">${entry.stlDiffAmt}</td>
						<td class="digits right">${entry.benefit}</td>
						
						<c:set var="payCnt" value="${payCnt + entry.payCnt}"/>
						<c:set var="payAmt" value="${payAmt + entry.payAmt}"/>
						<c:set var="rfdCnt" value="${rfdCnt + entry.rfdCnt}"/>
						<c:set var="rfdAmt" value="${rfdAmt + entry.rfdAmt}"/>
						<c:set var="totalCnt" value="${totalCnt + entry.totalCnt}"/>
						<c:set var="totalAmt" value="${totalAmt + entry.totalAmt}"/>
						<c:set var="stlVanFee" value="${stlVanFee + entry.stlVanFee}"/>
						<c:set var="stlVanAmt" value="${stlVanAmt + entry.stlVanAmt}"/>
						<c:set var="stlFee" value="${stlFee + entry.stlFee}"/>
						<c:set var="stlAmt" value="${stlAmt + entry.stlAmt}"/>
						<c:set var="profit" value="${profit + entry.profit}"/>
						<c:set var="stlDistFee" value="${stlDistFee + entry.stlDistFee}"/>
						<c:set var="stlAgencyFee" value="${stlAgencyFee + entry.stlAgencyFee}"/>
						<c:set var="stlSalesFee" value="${stlSalesFee + entry.stlSalesFee}"/>
						<c:set var="totSalesFee" value="${totSalesFee + entry.totSalesFee}"/>
						<c:set var="stlDiffAmt" value="${stlDiffAmt + entry.stlDiffAmt}"/>
						<c:set var="benefit" value="${benefit + entry.benefit}"/>
					</tr>
					
				</c:forEach>
				<tr  class="warning">
					<td>*</td>
					<td>합계</td>
					<td class="digits right"><c:out value="${payCnt}"/></td>
					<td class="digits right"><c:out value="${payAmt}"/></td>
					<td class="digits right"><c:out value="${rfdCnt}"/></td>
					<td class="digits right"><c:out value="${rfdAmt}"/></td>
					<td class="digits right"><c:out value="${totalCnt}"/></td>
					<td class="digits right"><c:out value="${totalAmt}"/></td>
					<td class="digits right"><c:out value="${stlVanFee}"/></td>
					<td class="digits right"><c:out value="${stlVanAmt}"/></td>
					<td class="digits right"><c:out value="${stlFee}"/></td>
					<td class="digits right"><c:out value="${stlAmt}"/></td>
					<td class="digits right"><c:out value="${profit}"/></td>
					<td class="digits right"><c:out value="${stlDistFee}"/></td>
					<td class="digits right"><c:out value="${stlAgencyFee}"/></td>
					<td class="digits right"><c:out value="${stlSalesFee}"/></td>
					<td class="digits right"><c:out value="${totSalesFee}"/></td>
					<td class="digits right"><c:out value="${stlDiffAmt}"/></td>
					<td class="digits right"><c:out value="${benefit}"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="row">
		<div class="col-sm-4 col-xs-12">
			<select name="pageSize" id="pageSize" class="input-sm pull-left col-lg-4 col-sm-5 col-xs-12">
				<option value="200">200개씩 보기</option>
				<option value="500">500개씩 보기</option>
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
