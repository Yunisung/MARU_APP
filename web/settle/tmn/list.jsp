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
			<c:if test="${not empty SUMMAP} ">
		 		<span class="caption-detail">최종정산일: <span class="date">${SUMMAP.stlDay}</span></span>  
		 		<span class="caption-detail">승인/취소 금액: <fmt:formatNumber type="number" value="${SUMMAP.payAmt}" pattern="#,##0" /> / 
		 		<fmt:formatNumber type="number" value="${SUMMAP.rfdAmt}" pattern="#,##0" /></span>  
		 		<span class="caption-detail">정산 금액: <fmt:formatNumber type="number" value="${SUMMAP.stlAmt}" pattern="#,##0" /></span>  
		 	</c:if>
		 </span>
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '터미널상점 정산 내역');">
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
					<th rowspan="3">가맹점명</th>
					<th rowspan="3">터미널상점명</th>
					<th style="min-width: 100px;" rowspan="3">대상거래 기간</th>
					<th colspan="15">매출정보</th>
					<th colspan="5" rowspan="2">지급정보</th>
				</tr>
				<tr>
					<th colspan="2">매입</th>
					<th colspan="2">매입취소</th>
					<th colspan="2">지급보류</th>
					<th rowspan="2">합계</th>
					<th colspan="8">합계</th>
				</tr>
				<tr>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					
					<th>입금수수료</th>
					<th>대행사수수료</th>
					<th>에이전시수수료</th>
					<th>지사수수료</th>
					<th>가맹점수수료</th>
					<th>수익</th>
					<th>총수수료</th>
					<th>지급예정</th>
					<th>실지급액</th>
					<th>은행명</th>
					<th style="min-width: 140px;">계좌번호</th>
					<th>예금주명</th>
					<th>정산수수료율</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="24">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:set var="var1" value="0"/>
				<c:set var="var2" value="0"/>
				<c:set var="var3" value="0"/>
				<c:set var="var4" value="0"/>
				<c:set var="var5" value="0"/>
				<c:set var="var6" value="0"/>
				<c:set var="var7" value="0"/>
				<c:set var="var8" value="0"/>
				<c:set var="var9" value="0"/>
				<c:set var="var10" value="0"/>
				<c:set var="var11" value="0"/>
				<c:set var="var12" value="0"/>
				<c:set var="var13" value="0"/>
				<c:set var="var14" value="0"/>
				<c:set var="var15" value="0"/>
				<c:set var="var16" value="0"/>
				
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.idx}" 
					<c:if test="${status.index ne 0 && entry.stlDay ne CPR.data[status.index-1].stlDay}">
						class="bg-grey-cararra bg-font-grey-cararra"
					</c:if>>
						<td>${status.count}</td>
						<td>${entry.mchtName}</td>
						<td>${entry.tmnName}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right digits">${entry.payCnt}</td>
						<td class="text-right digits">${entry.payAmount}</td>
						<td class="text-right digits">${entry.rfdCnt}</td>
						<td class="text-right digits">${entry.rfdAmount}</td>
						<td class="text-right digits">${entry.holdCnt}</td>
						<td class="text-right digits">${entry.holdAmount}</td>
						<td class="text-right digits">${entry.payAmount + entry.rfdAmount}</td>
						<td class="text-right digits">${entry.stlVanFee}</td>
						<td class="text-right digits">${entry.stlDistFee}</td>
						<td class="text-right digits">${entry.stlAgencyFee}</td>
						<td class="text-right digits">${entry.stlSalesFee}</td>
						<td class="text-right digits">${entry.stlMchtFee}</td>
						<td class="text-right digits">${entry.benefit}</td>
						<td class="text-right digits">${entry.stlVanFee + entry.stlDistFee + entry.stlAgencyFee + entry.stlSalesFee + entry.stlMchtFee + entry.benefit}</td>
						<td class="text-right digits">${entry.stlTmnAmount}</td>
						<td class="text-right digits">${entry.stlTmnAmount}</td>
						<td>${entry.tmnBankName}</td>
						<td>${entry.tmnAccount}&nbsp;</td>
						<td>${entry.tmnAccntHolder}</td>
						<td class="text-right"><fmt:formatNumber value="${entry.stlTmnRate * 100}" pattern="0.000"/> %</td>
						
						
						<c:set var="var1"  value="${var1 +  entry.payCnt}"/>
						<c:set var="var2"  value="${var2 +  entry.payAmount}"/>
						<c:set var="var3"  value="${var3 +  entry.rfdCnt}"/>
						<c:set var="var4"  value="${var4 +  entry.rfdAmount}"/>
						<c:set var="var5"  value="${var5 +  entry.holdCnt}"/>
						<c:set var="var6"  value="${var6 +  entry.holdAmount}"/>
						<c:set var="var7"  value="${var7 +  entry.payAmount + entry.rfdAmount}"/>
						<c:set var="var8"  value="${var8 +  entry.stlVanFee}"/>
						<c:set var="var9"  value="${var9 +  entry.stlDistFee}"/>
						<c:set var="var10" value="${var10 + entry.stlAgencyFee}"/>
						<c:set var="var11" value="${var11 + entry.stlSalesFee}"/>
						<c:set var="var12" value="${var12 + entry.stlMchtFee}"/>
						<c:set var="var13" value="${var13 + entry.benefit}"/>
						<c:set var="var14" value="${var14 + (entry.stlVanFee + entry.stlDistFee + entry.stlAgencyFee + entry.stlSalesFee + entry.stlMchtFee + entry.benefit)}"/>
						<c:set var="var15" value="${var15 + entry.stlTmnAmount}"/>
						<c:set var="var16" value="${var16 + entry.stlTmnAmount}"/>
					</tr>
				</c:forEach>
				<tr>
						<td></td>
						<td></td>
						<td></td>
						<td>합계</td>
						<td class="text-right digits"><c:out value="${var1}" /></td> <%-- ${entry.payCnt} --%>
						<td class="text-right digits"><c:out value="${var2}" /></td> <%-- ${entry.payAmount} --%>
						<td class="text-right digits"><c:out value="${var3}" /></td> <%-- ${entry.rfdCnt} --%>
						<td class="text-right digits"><c:out value="${var4}" /></td> <%-- ${entry.rfdAmount} --%>
						<td class="text-right digits"><c:out value="${var5}" /></td> <%-- ${entry.holdCnt} --%>
						<td class="text-right digits"><c:out value="${var6}" /></td> <%-- ${entry.holdAmount} --%>
						<td class="text-right digits"><c:out value="${var7}" /></td> <%-- ${entry.payAmount + entry.rfdAmount} --%>
						<td class="text-right digits"><c:out value="${var8}" /></td> <%-- ${entry.stlVanFee} --%>
						<td class="text-right digits"><c:out value="${var9}" /></td> <%-- ${entry.stlDistFee} --%>
						<td class="text-right digits"><c:out value="${var10}"/></td> <%-- ${entry.stlAgencyFee} --%>
						<td class="text-right digits"><c:out value="${var11}"/></td> <%-- ${entry.stlSalesFee} --%>
						<td class="text-right digits"><c:out value="${var12}"/></td> <%-- ${entry.stlMchtFee} --%>
						<td class="text-right digits"><c:out value="${var13}"/></td> <%-- ${entry.benefit} --%>
						<td class="text-right digits"><c:out value="${var14}"/></td> <%-- ${entry.(entry.stlVanFee + entry.stlDistFee + entry.stlAgencyFee + entry.stlSalesFee + entry.stlMchtFee + entry.benefit)} --%>
						<td class="text-right digits"><c:out value="${var15}"/></td> <%-- ${entry.stlTmnAmount} --%>
						<td class="text-right digits"><c:out value="${var16}"/></td> <%-- ${entry.stlTmnAmount} --%>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
