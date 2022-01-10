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
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '일마감-정산거래기준');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
	</div>
</div>

<div class="portlet-body form light">
<!-- 리스트 본문 -->
<div class="table-scrollable">
	<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
	<thead>
		<tr>
			<th rowspan="3">No</th>
			<th rowspan="3">기준일</th>
			<th colspan="6">매출</th>
			<th colspan="2">입금</th>
			<th colspan="2">지급</th>
			<th rowspan="3">매출이익</th>
			<th colspan="4">영업수수료</th>
			<th rowspan="3">차액정산입금액</th>
			<th rowspan="3">영업이익</th>
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
				<td colspan="23">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
			</tr>
		</c:if>
		<c:set var="payCnt" value="0"/> <!-- 승인건수 -->
		<c:set var="payAmt" value="0"/> <!-- 승인금액 -->
		<c:set var="rfdCnt" value="0"/> <!-- 취소건수 -->
		<c:set var="rfdAmt" value="0"/> <!-- 취소금액 -->
		<c:set var="totalCnt" value="0"/> <!-- 합계건수 -->
		<c:set var="totalAmt" value="0"/> <!-- 합계금액 -->
		
		<c:set var="stlVanFee" value="0"/> <!-- 입금수수료 -->
		<c:set var="stlVanAmt" value="0"/> <!-- 입금액 -->
		<c:set var="stlFee" value="0"/> <!-- 가맹점 수수료 -->
		<c:set var="stlAmt" value="0"/> <!-- 가맹점 지급금액 -->
		
		<c:set var="profit" value="0"/> <!-- 매출이익 -->
		
		<c:set var="stlDistFee" value="0"/> <!-- 대행사 수수료 -->
		<c:set var="stlAgencyFee" value="0"/> <!-- 에이전시 수수료 -->
		<c:set var="stlSalesFee" value="0"/> <!-- 지사 수수료 -->
		<c:set var="totSalesFee" value="0"/> <!-- 영업 수수료 총합계 -->

		<c:set var="stlDiffAmt" value="0"/> <!-- 차액정산입금액 -->
		
		<c:set var="benefit" value="0"/> <!-- 영업이익 -->
		
		<c:forEach var="entry" items="${CPR.data }" varStatus="status">
			<tr>
				<td class="digits">${status.count}</td>
				<td><a href="#" onclick="openpop('/sales/dailysByStlDay/dtl/form/${entry.stlDay}');">${entry.stlDay }</a></td>
				<td class="digits right">${entry.payCnt}</td>
				<td class="digits right">${entry.payAmt}</td>
				<td class="digits right">${entry.rfdCnt}</td>
				<td class="digits right">${entry.rfdAmt}</td>
				<td class="digits right">${entry.totalCnt}</td>
				<td class="digits right">${entry.totalAmt}</td>
				
				<td class="digits right">${entry.stlVanFee}</td> <!-- 입금수수료 -->
				<td class="digits right">${entry.stlVanAmt}</td> <!-- 입금액 -->
				<td class="digits right">${entry.stlFee}</td> <!-- 가맹점 수수료 -->
				<td class="digits right">${entry.stlAmt}</td> <!-- 가맹점 지급금액 -->
				<td class="digits right">${entry.profit}</td> <!-- 매출이익 -->
				<td class="digits right">${entry.stlDistFee}</td>
				<td class="digits right">${entry.stlAgencyFee}</td>
				<td class="digits right">${entry.stlSalesFee}</td>
				<td class="digits right">${entry.totSalesFee}</td>
				<td class="digits right">${entry.stlDiffAmt}</td>
				<td class="digits right">${entry.benefit}</td>
				
				<c:set var="payCnt" value="${entry.payCnt + payCnt}"/>
				<c:set var="payAmt" value="${entry.payAmt + payAmt}"/>
				<c:set var="rfdCnt" value="${entry.rfdCnt + rfdCnt}"/>
				<c:set var="rfdAmt" value="${entry.rfdAmt + rfdAmt}"/>
				<c:set var="totalCnt" value="${entry.totalCnt + totalCnt}"/>
				<c:set var="totalAmt" value="${entry.totalAmt + totalAmt}"/>
				
				<c:set var="stlVanFee" value="${entry.stlVanFee + stlVanFee}"/> <!-- 입금수수료 -->
				<c:set var="stlVanAmt" value="${entry.stlVanAmt + stlVanAmt}"/> <!-- 입금액 -->
				
				<c:set var="stlFee" value="${entry.stlFee + stlFee}"/> <!-- 가맹점 수수료 -->
				<c:set var="stlAmt" value="${entry.stlAmt + stlAmt}"/> <!-- 가맹점 지급금액 -->
				<c:set var="profit" value="${entry.profit + profit}"/>
				
				<c:set var="stlDistFee" value="${entry.stlDistFee + stlDistFee}"/>
				<c:set var="stlAgencyFee" value="${entry.stlAgencyFee + stlAgencyFee}"/>
				<c:set var="stlSalesFee" value="${entry.stlSalesFee + stlSalesFee}"/>
				<c:set var="totSalesFee" value="${entry.totSalesFee + totSalesFee}"/>
				<c:set var="stlDiffAmt" value="${entry.stlDiffAmt + stlDiffAmt}"/>		
				<c:set var="benefit" value="${entry.benefit + benefit}"/>		
			</tr>
		</c:forEach>
		<tr class="warning">
			<td>*</td>
			<td>합계</td>
			<td class="digits right"><c:out value="${payCnt}" /></td>
			<td class="digits right"><c:out value="${payAmt}" /></td>
			<td class="digits right"><c:out value="${rfdCnt}" /></td>
			<td class="digits right"><c:out value="${rfdAmt}" /></td>
			<td class="digits right"><c:out value="${totalCnt}" /></td>
			<td class="digits right"><c:out value="${totalAmt}" /></td>
			
			<td class="digits right"><c:out value="${stlVanFee}" /></td>
			<td class="digits right"><c:out value="${stlVanAmt}" /></td>
			
			<td class="digits right"><c:out value="${stlFee}" /></td>
			<td class="digits right"><c:out value="${stlAmt}" /></td>
			
			<td class="digits right"><c:out value="${profit}" /></td>
			
			<td class="digits right"><c:out value="${stlDistFee}" /></td>
			<td class="digits right"><c:out value="${stlAgencyFee}" /></td>
			<td class="digits right"><c:out value="${stlSalesFee}" /></td>
			<td class="digits right"><c:out value="${totSalesFee}" /></td>
			<td class="digits right"><c:out value="${stlDiffAmt}" /></td>
			<td class="digits right"><c:out value="${benefit}" /></td>
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
		<script>
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