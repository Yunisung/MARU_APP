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
		<c:if test="${CP_SESSION.grade eq '본사'}">
		<div class="btn-group">
			<a class="btn btn-circle btn-default " href="javascript:;" data-toggle="dropdown" aria-expanded="false">
				<i class="fa fa-bank"></i> 정산 기능 <i class="fa fa-angle-down"></i>
			</a>
			<ul class="dropdown-menu pull-right">
				<li><a href="javascript:;" class="settle-mcht-decide">
						<i class="fa fa-check-square-o"></i> 선택항목 확정 처리
					</a>
				</li>
			</ul>
		</div>
		</c:if>
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '가맹점 정산 예정 내역');">
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
					<th class="ck-th" rowspan="3"><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
					<th rowspan="3">No</th>
					<!-- <th rowspan="3">정산일</th> -->
					<th rowspan="3">정산구분</th>
					<th rowspan="3">가맹점명</th>
					<th rowspan="3">가맹점ID</th>
					<th rowspan="3">가맹점대표자명</th>
					<th style="min-width: 100px;" rowspan="3">대상거래 기간</th>
					<th colspan="13">매출정보</th>
					<th rowspan="3">대출정산차감금액</th>
					<th rowspan="3">차감 금액</th>
					<th rowspan="3">실지급액</th>
					<th colspan="2" rowspan="2">지급정보</th>
				</tr>
				<tr>
					<th colspan="2">매입</th>
					<th colspan="2">매입취소</th>
					<th colspan="2">지급보류</th>
					<th rowspan="2">합계</th>
					<th colspan="5">합계</th>
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
					<th>예수금<br>(미출금)</th>
					<th style="min-width: 140px;">계좌정보</th>
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
<c:set var="var17" value="0"/>
				
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.idx}" 
					<c:if test="${status.index ne 0 && entry.stlDay ne CPR.data[status.index-1].stlDay}">
						class="bg-grey-cararra bg-font-grey-cararra"
					</c:if>>
						<td class="ck-td btn-td">
							<input type="checkbox" class="row-check" id="${entry.idx}_check" class="checkbox-style" /><label for="${entry.idx}_check"></label>
						</td>
						<td>${status.count}</td>
						<%-- <td class="date">${entry.stlDay}</td> --%>
						<td>
							<c:set var="stlType" value="${entry.stlType }"/>
							<c:choose>
								<c:when test="${fn:contains(stlType,'C+') }">충전정산</c:when>
								<c:otherwise>일반</c:otherwise>
							</c:choose>
						</td>
						<td>${entry.mchtName}</td>
						<td>${entry.mchtId}</td>
						<td>${entry.ceoName}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right digits">${entry.payCnt}</td>
						<td class="text-right digits">${entry.payAmt}</td>
						<td class="text-right digits">${entry.rfdCnt}</td>
						<td class="text-right digits">${entry.rfdAmt}</td>
						<td class="text-right digits">${entry.holdCnt}</td>
						<td class="text-right digits">${entry.holdAmt}</td>
						<td class="text-right digits">${entry.payAmt + entry.rfdAmt}</td>
						<td class="text-right digits">${(entry.payFee + entry.payVat) + (entry.rfdFee + entry.rfdVat) + (entry.relsFee + entry.relsVat) + (entry.payInterFee + entry.payInterVat) + (entry.rfdInterFee + entry.rfdInterVat)}</td>
						<td class="text-right digits">${entry.vanFee + entry.vanInterFee}</td>
						<%--<td class="text-right digits">${entry.diffAmt}</td> --%>
						<td class="text-right digits">${entry.agencyFee + entry.distFee}</td>
						<td class="text-right digits">${entry.benefit}</td>
						<td class="text-right digits">${entry.stlAmount}</td>
						<td class="text-right digits">
						<c:choose>
							<c:when test="${entry.ddctType eq 'Y'}">${(entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt - entry.ddctAmt}</c:when>
							<c:otherwise>${(entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt}</c:otherwise>
						</c:choose>
						</td>
						<td class="text-right digits">${entry.loanDeductAmt}</td>
						<td class="text-right digits">${entry.deductAmt + entry.manualDeductAmt}</td>
						<td class="text-right digits">
						<c:choose>
							<c:when test="${entry.ddctType eq 'Y'}">${entry.stlAmount + ((entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt) + (entry.deductAmt  + entry.manualDeductAmt) - entry.ddctAmt}</c:when>
							<c:otherwise>${entry.stlAmount + ((entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt) + (entry.deductAmt  + entry.manualDeductAmt)}</c:otherwise>
						</c:choose>
						</td>
						<td>
						<c:choose>
							<c:when test="${fn:contains(stlType,'C+') }">가맹점충전잔액지급</c:when>
							<c:otherwise>${entry.account}<br>(${entry.bankName})</c:otherwise>
						</c:choose>
						</td>
						<td class="text-right"><fmt:formatNumber value="${entry.stlRate * 100}" pattern="0.000"/> %</td>
						
						
						<c:set var="var1"  value="${var1 +  entry.payCnt}"/>
						<c:set var="var2"  value="${var2 +  entry.payAmt}"/>
						<c:set var="var3"  value="${var3 +  entry.rfdCnt}"/>
						<c:set var="var4"  value="${var4 +  entry.rfdAmt}"/>
						<c:set var="var5"  value="${var5 +  entry.holdCnt}"/>
						<c:set var="var6"  value="${var6 +  entry.holdAmt}"/>
						<c:set var="var7"  value="${var7 +  entry.payAmt + entry.rfdAmt}"/>
						<c:set var="var8"  value="${var8 +  (entry.payFee + entry.payVat) + (entry.rfdFee + entry.rfdVat) + (entry.relsFee + entry.relsVat) + (entry.payInterFee + entry.payInterVat) + (entry.rfdInterFee + entry.rfdInterVat)}"/>
						<c:set var="var9"  value="${var9 +  entry.vanFee + entry.vanInterFee}"/>
						<%--<c:set var="var10"  value="${var10 +  entry.diffAmt}"/> --%>
						<c:set var="var11" value="${var11 + entry.agencyFee + entry.distFee}"/>
						<c:set var="var12" value="${var12 + entry.benefit}"/>
						<c:set var="var13" value="${var13 + entry.stlAmount}"/>
						<c:choose>
							<c:when test="${entry.ddctType eq 'Y'}"><c:set var="var14" value="${var14 + (entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt - entry.ddctAmt}"/></c:when>
							<c:otherwise><c:set var="var14" value="${var14 + (entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt}"/></c:otherwise>
						</c:choose>
						<c:set var="var17" value="${var17 + entry.loanDeductAmt}"/>
						<c:set var="var15" value="${var15 + entry.deductAmt + entry.manualDeductAmt}"/>
						<c:choose>
							<c:when test="${entry.ddctType eq 'Y'}"><c:set var="var16" value="${var16 + entry.stlAmount + ((entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt) + (entry.deductAmt  + entry.manualDeductAmt) - entry.ddctAmt}"/></c:when>
							<c:otherwise><c:set var="var16" value="${var16 + entry.stlAmount + ((entry.relsAmt - entry.relsFee - entry.relsVat) + entry.manualRelsAmt) + (entry.deductAmt  + entry.manualDeductAmt)}"/></c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
				<tr>
						<td class="ck-td"></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td>합계</td>
						<td class="text-right digits"><c:out value="${var1}" /></td> <%-- ${entry.payCnt} --%>
						<td class="text-right digits"><c:out value="${var2}" /></td> <%-- ${entry.payAmt} --%>
						<td class="text-right digits"><c:out value="${var3}" /></td> <%-- ${entry.rfdCnt} --%>
						<td class="text-right digits"><c:out value="${var4}" /></td> <%-- ${entry.rfdAmt} --%>
						<td class="text-right digits"><c:out value="${var5}" /></td> <%-- ${entry.holdCnt} --%>
						<td class="text-right digits"><c:out value="${var6}" /></td> <%-- ${entry.holdAmt} --%>
						<td class="text-right digits"><c:out value="${var7}" /></td> <%-- ${entry.payAmt + entry.rfdAmt - entry.holdAmt} --%>
						<td class="text-right digits"><c:out value="${var8}" /></td> <%-- ${(entry.payFee + entry.payVat) + (entry.rfdFee + entry.rfdVat) + (entry.relsFee + entry.relsVat)} --%>
						<td class="text-right digits"><c:out value="${var9}" /></td> <%-- ${entry.vanFee} --%>
						<%--<td class="text-right digits"><c:out value="${var10}" /></td> --%> <%-- ${entry.diffAmt} --%>
						<td class="text-right digits"><c:out value="${var11}"/></td> <%-- ${entry.agencyFee + entry.distFee} --%>
						<td class="text-right digits"><c:out value="${var12}"/></td> <%-- ${entry.benefit} --%>
						<td class="text-right digits"><c:out value="${var13}"/></td> <%-- ${entry.stlAmount} --%>
						<td class="text-right digits"><c:out value="${var14}"/></td> <%-- ${entry.relsAmt} --%>
						<td class="text-right digits"><c:out value="${var17}"/></td><%-- ${entry.loanDeductAmt} --%>
						<td class="text-right digits"><c:out value="${var15}"/></td> <%-- ${entry.deductAmt} --%>
						<td class="text-right digits"><c:out value="${var16}"/></td> <%-- ${entry.stlAmount + entry.relsAmt - entry.deductAmt} --%>
						<td></td>
						<td></td><%-- <td class="text-right rate"></td> 	${entry.stlRate} --%>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
