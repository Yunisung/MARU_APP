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
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '일마감');">
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
					<th colspan="6">매출(매입일 기준)</th>
					<th colspan="8">입금(입금일 기준)</th>
					<th colspan="7">지급(정산일 기준)</th>
				</tr>
				<tr>
					<th colspan="2">건수</th>
					<th colspan="2">금액</th>
					<th colspan="2">합계</th>
					<th colspan="2">건수</th>
					<th colspan="2">매출금액</th>
					<th colspan="3">수수료</th>
					<th rowspan="2">입금액</th>
					<th colspan="2">건수</th>
					<th colspan="2">수수료</th>
					<th rowspan="2">수수료합계</th>
					<th rowspan="2">지급액</th>
					<th rowspan="2">수익</th>
				</tr>
				<tr>
					<th>승인</th>
					<th>취소</th>
					<th>승인</th>
					<th>취소</th>
					<th>건수</th>
					<th>금액</th>
					<th>승인</th>
					<th>취소</th>
					<th>승인</th>
					<th>취소</th>
					<th>승인</th>
					<th>취소</th>
					<th>합계</th>
					<th>승인</th>
					<th>취소</th>
					<th>승인</th>
					<th>취소</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="23">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:set var="trxCapAmt" value="0"/>
				<c:set var="trxCapCnt" value="0"/>
				<c:set var="trxRfdAmt" value="0"/>
				<c:set var="trxRfdCnt" value="0"/>
				<c:set var="inCapAmt" value="0"/>
				<c:set var="inCapCnt" value="0"/>
				<c:set var="inRfdAmt" value="0"/>
				<c:set var="inRfdCnt" value="0"/>
				<c:set var="inVanCapFee" value="0"/>
				<c:set var="inVanRfdFee" value="0"/>
				<c:set var="inVanFee" value="0"/>
				<c:set var="inVanInAmt" value="0"/>
				<c:set var="outPayAmt" value="0"/>
				<c:set var="outPayCnt" value="0"/>
				<c:set var="outPayFee" value="0"/>
				<c:set var="outRfdAmt" value="0"/>
				<c:set var="outRfdCnt" value="0"/>
				<c:set var="outRfdFee" value="0"/>
				<c:set var="outAllFee" value="0"/>
				<c:set var="outVanFee" value="0"/>
				<c:set var="outDistFee" value="0"/>
				<c:set var="outAgencyFee" value="0"/>
				<c:set var="outPayOutAmt" value="0"/>
				<c:set var="outBenefit" value="0"/>
				
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td class="digits">${status.count}</td>
						<td><a href="#" onclick="openpop('/sales/dailys/dtl/${entry.days}');">${entry.days}</a></td>
						<td class="digits right">${entry.trxCapCnt}</td>
						<td class="digits right">${entry.trxRfdCnt}</td>

						<td class="digits right">${entry.trxCapAmt}</td>
						<td class="digits right">${entry.trxRfdAmt}</td>

						<td class="digits right">${entry.trxCapCnt + entry.trxRfdCnt}</td>
						<td class="digits right">${entry.trxCapAmt + entry.trxRfdAmt}</td>
						
						<td class="digits right">${entry.inCapCnt}</td>
						<td class="digits right">${entry.inRfdCnt}</td>

						<td class="digits right">${entry.inCapAmt}</td>
						<td class="digits right">${entry.inRfdAmt}</td>
						
						<td class="digits right">${entry.inVanCapFee}</td>
						<td class="digits right">${entry.inVanRfdFee}</td>
						<td class="digits right">${entry.inVanFee}</td>
						<td class="digits right">${entry.inVanInAmt}</td>

						<td class="digits right">${entry.outPayCnt}</td>
						<td class="digits right">${entry.outRfdCnt}</td>
						
						<%-- <td class="digits">${entry.outPayAmt}</td>
						<td class="digits">${entry.outRfdAmt}</td> --%>
						
						<td class="digits right">${entry.outPayFee}</td>
						<td class="digits right">${entry.outRfdFee}</td>
						<td class="digits right">${entry.outAllFee}</td>
						<%-- <td class="digits">${entry.outVanFee}</td>
						<td class="digits">${entry.outDistFee}</td>
						<td class="digits">${entry.outAgencyFee}</td> --%>
						<td class="digits right">${entry.outPayOutAmt}</td>
						<td class="digits right">${entry.outBenefit}</td>

						<c:set var="trxCapAmt" value="${trxCapAmt + entry.trxCapAmt}"/>
						<c:set var="trxCapCnt" value="${trxCapCnt + entry.trxCapCnt}"/>
						<c:set var="trxRfdAmt" value="${trxRfdAmt + entry.trxRfdAmt}"/>
						<c:set var="trxRfdCnt" value="${trxRfdCnt + entry.trxRfdCnt}"/>
						<c:set var="inCapAmt" value="${inCapAmt + entry.inCapAmt}"/>
						<c:set var="inCapCnt" value="${inCapCnt + entry.inCapCnt}"/>
						<c:set var="inRfdAmt" value="${inRfdAmt + entry.inRfdAmt}"/>
						<c:set var="inRfdCnt" value="${inRfdCnt + entry.inRfdCnt}"/>
						<c:set var="inVanCapFee" value="${inVanCapFee + entry.inVanCapFee}"/>
						<c:set var="inVanRfdFee" value="${inVanRfdFee + entry.inVanRfdFee}"/>
						<c:set var="inVanFee" value="${inVanFee + entry.inVanFee}"/>
						<c:set var="inVanInAmt" value="${inVanInAmt + entry.inVanInAmt}"/>
						<c:set var="outPayAmt" value="${outPayAmt + entry.outPayAmt}"/>
						<c:set var="outPayCnt" value="${outPayCnt + entry.outPayCnt}"/>
						<c:set var="outPayFee" value="${outPayFee + entry.outPayFee}"/>
						<c:set var="outRfdAmt" value="${outRfdAmt + entry.outRfdAmt}"/>
						<c:set var="outRfdCnt" value="${outRfdCnt + entry.outRfdCnt}"/>
						<c:set var="outRfdFee" value="${outRfdFee + entry.outRfdFee}"/>
						<c:set var="outAllFee" value="${outAllFee + entry.outAllFee}"/>
						<c:set var="outVanFee" value="${outVanFee + entry.outVanFee}"/>
						<c:set var="outDistFee" value="${outDistFee + entry.outDistFee}"/>
						<c:set var="outAgencyFee" value="${outAgencyFee + entry.outAgencyFee}"/>
						<c:set var="outPayOutAmt" value="${outPayOutAmt + entry.outPayOutAmt}"/>
						<c:set var="outBenefit" value="${outBenefit + entry.outBenefit}"/>
					</tr>
				</c:forEach>
				<tr  class="warning">
					<td>*</td>
					<td>합계</td>
					<td class="digits right"><c:out value="${trxCapCnt}"/></td>
					<td class="digits right"><c:out value="${trxRfdCnt}"/></td>
					<td class="digits right"><c:out value="${trxCapAmt}"/></td>
					<td class="digits right"><c:out value="${trxRfdAmt}"/></td>
					<td class="digits right"><c:out value="${trxCapCnt + trxRfdCnt}"/></td>
					<td class="digits right"><c:out value="${trxCapAmt + trxRfdAmt}"/></td>
					
					<td class="digits right"><c:out value="${inCapCnt}"/></td>
					<td class="digits right"><c:out value="${inRfdCnt}"/></td>
					<td class="digits right"><c:out value="${inCapAmt}"/></td>
					<td class="digits right"><c:out value="${inRfdAmt}"/></td>
					<td class="digits right"><c:out value="${inVanCapFee}"/></td>
					<td class="digits right"><c:out value="${inVanRfdFee}"/></td>
					<td class="digits right"><c:out value="${inVanFee}"/></td>
					<td class="digits right"><c:out value="${inVanInAmt}"/></td>
					<td class="digits right"><c:out value="${outPayCnt}"/></td>
					<td class="digits right"><c:out value="${outRfdCnt}"/></td>
					<td class="digits right"><c:out value="${outPayFee}"/></td>
					<td class="digits right"><c:out value="${outRfdFee}"/></td>
					<td class="digits right"><c:out value="${outAllFee}"/></td>
					<td class="digits right"><c:out value="${outPayOutAmt}"/></td>
					<td class="digits right"><c:out value="${outBenefit}"/></td>
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
