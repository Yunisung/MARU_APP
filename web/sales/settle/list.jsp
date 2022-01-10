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
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '지급정산내역');">
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
					<th rowspan="2">No</th>
					<th rowspan="2">지급정산일</th>
					<th colspan="2">매입</th>
					<th colspan="2">매입취소</th>
					<th colspan="2">지급보류</th>
					<th rowspan="2">매출합계</th>
					<th colspan="5">합계</th>
					<th rowspan="2">과입</th>
					<th rowspan="2">차감</th>
					<th rowspan="2">실지급액</th>
				</tr>
				<tr>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>건수</th>
					<th>금액</th>
					<th>수수료</th>
					<th>입금수수료</th>
					<th>대행사수수료</th>
					<th>수익</th>
					<th>지급예정</th>
				</tr>
				
			</thead>
			<tbody id="list">
				
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
				<c:forEach var="entry" items="${DATAMAP}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td class="date">${entry.days}</td>
						<td class="text-right digits">${entry.payCnt}</td>
						<td class="text-right digits">${entry.payAmt}</td>
						<td class="text-right digits">${entry.rfdCnt}</td>
						<td class="text-right digits">${entry.rfdAmt}</td>
						<td class="text-right digits">${entry.holdCnt}</td>
						<td class="text-right digits">${entry.holdAmt}</td>
						<td class="text-right digits">${entry.salesAmt}</td>
						<td class="text-right digits">${entry.fee}</td>
						<td class="text-right digits">${entry.vanFee}</td>
						<td class="text-right digits">${entry.agencyFee + entry.distFee}</td>
						<td class="text-right digits">${entry.benefit}</td>
						<td class="text-right digits">${entry.stlAmount}</td>
						<td class="text-right digits">${entry.overAmt}</td>
						<td class="text-right digits">${entry.deductAmt}</td>
						<td class="text-right digits">${entry.payoutAmt}</td>
						<c:set var="var1"  value="${var1 +  entry.payCnt}"/>
						<c:set var="var2"  value="${var2 +  entry.payAmt}"/>
						<c:set var="var3"  value="${var3 +  entry.rfdCnt}"/>
						<c:set var="var4"  value="${var4 +  entry.rfdAmt}"/>
						<c:set var="var5"  value="${var5 +  entry.holdCnt}"/>
						<c:set var="var6"  value="${var6 +  entry.holdAmt}"/>
						<c:set var="var7"  value="${var7 +  entry.salesAmt}"/>
						<c:set var="var8"  value="${var8 +  entry.fee}"/>
						<c:set var="var9"  value="${var9 +  entry.vanFee}"/>
						<c:set var="var10" value="${var10 + entry.agencyFee + entry.distFee}"/>
						<c:set var="var11" value="${var11 + entry.benefit}"/>
						<c:set var="var12" value="${var12 + entry.stlAmount}"/>
						<c:set var="var13" value="${var13 + entry.overAmt}"/>
						<c:set var="var14" value="${var14 + entry.deductAmt}"/>
						<c:set var="var15" value="${var15 + entry.payoutAmt}"/>
					</tr>
				</c:forEach>
				<tr class="warning">
						<td colspan="2">합계</td>
						<td class="text-right digits"><c:out value="${var1}" /></td> <%-- ${entry.payCnt} --%>
						<td class="text-right digits"><c:out value="${var2}" /></td> <%-- ${entry.payAmt} --%>
						<td class="text-right digits"><c:out value="${var3}" /></td> <%-- ${entry.rfdCnt} --%>
						<td class="text-right digits"><c:out value="${var4}" /></td> <%-- ${entry.rfdAmt} --%>
						<td class="text-right digits"><c:out value="${var5}" /></td> <%-- ${entry.holdCnt} --%>
						<td class="text-right digits"><c:out value="${var6}" /></td> <%-- ${entry.holdAmt} --%>
						<td class="text-right digits"><c:out value="${var7}" /></td> <%-- ${entry.payAmt + entry.rfdAmt - entry.holdAmt} --%>
						<td class="text-right digits"><c:out value="${var8}" /></td> <%-- ${(entry.payFee + entry.payVat) + (entry.rfdFee + entry.rfdVat) + (entry.relsFee + entry.relsVat)} --%>
						<td class="text-right digits"><c:out value="${var9}" /></td> <%-- ${entry.vanFee} --%>
						<td class="text-right digits"><c:out value="${var10}"/></td> <%-- ${entry.agencyFee + entry.distFee} --%>
						<td class="text-right digits"><c:out value="${var11}"/></td> <%-- ${entry.benefit} --%>
						<td class="text-right digits"><c:out value="${var12}"/></td> <%-- ${entry.stlAmount} --%>
						<td class="text-right digits"><c:out value="${var13}"/></td> <%-- ${entry.relsAmt} --%>
						<td class="text-right digits"><c:out value="${var14}"/></td> <%-- ${entry.deductAmt} --%>
						<td class="text-right digits"><c:out value="${var15}"/></td> <%-- ${entry.stlAmount + entry.relsAmt - entry.deductAmt} --%>
				</tr>
			</tbody>
		</table>
	</div>
	
</div>
<!-- 리스트 페이징 종료 -->
