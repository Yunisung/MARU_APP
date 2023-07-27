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
		 		<span class="caption-detail"> 거래금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.amtSum}" pattern="#,##0" />원,</span>  
		 		<span class="caption-detail"> 가맹점 수수료 합계: <fmt:formatNumber type="number" value="${SUMMAP.vatSum}" pattern="#,##0" />원,</span>  
		 		<span class="caption-detail"> 출금 수수료 합계: <fmt:formatNumber type="number" value="${SUMMAP.payOutVatSum}" pattern="#,##0" />원,</span>  
		 		<span class="caption-detail"> 실출금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.payOutAmountSum}" pattern="#,##0" />원</span>  
		 </span>
	</div>
	
	<div class="actions">
		<div class="btn-group">
			<a class="btn btn-sm red selectRetry" href="javascript:selectRetry();"">
				선택 재전송
			</a>
		</div>&nbsp;&nbsp;

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
<form class="form-horizontal form-bordered" role="form" data-form="true" id="realTimeFrm" name="form">
	<div class="table-scrollable">
		<!-- 리스트 본문 시작 -->
		<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>
					<th>No</th>
					<th class="ck-th" rowspan="3"><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
					<th>거래번호</th>
					<th>가맹점<br>아이디</th>
					<th style="min-width: 150px;">가맹점</th>
					<th>터미널ID</th>
					<th>주문번호</th>
					<th style="min-width: 80px;">거래일시</th>
					<th>결제수단</th>
					<th>승인번호</th>
					<th>금액</th>
					<th>가맹점<br>정산 수수료</th>
					<th>가맹점<br>정산 수수료 VAT</th>
					<th>정산금액</th>
					<th>출금<br>수수료</th>
					<th>출금<br>수수료 VAT</th>
					<th>은행<br>수수료</th>
					<th>실출금액</th>
					<th>취소<br>입금액</th>
					<th style="min-width: 100px;">은행이름</th>
					<th style="min-width: 120px;">계좌번호</th>
					<th style="min-width: 100px;">예금주</th>
					<th style="min-width: 80px;">출금일시</th>
					<th>출금<br>결과메시지</th>
					<th>출금<br>전송횟수</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="25">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-trxId="${entry.trxId}" 
						<c:if test="${status.index ne 0}">
							class="bg-grey-cararra bg-font-grey-cararra"
						</c:if>>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td class="ck-td btn-td">
							<c:if test="${entry.sendCnt == '3'}">
								<c:if test="${entry.resultCd != '0000'}">
									<input type="checkbox" class="row-check" id="${entry.trxId}_check" class="checkbox-style" /><label for="${entry.trxId}_check"></label>
								</c:if>
							</c:if>
						</td>
						<td>${entry.trxId}</td>
						<td>${entry.mchtId}</td>
						<td>${entry.name}</td>
						<td>${entry.tmnId}</td>
						<td>${entry.trackId}</td>
						<td class="date">${entry.trxDay}${entry.trxTime}</td>
						<td>${entry.payType}</td>
						<td>${entry.authCd}</td>
						<td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.stlFee}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.stlFeeVat}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.stlAmount}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.payOutFee}" pattern="#,##0" /></td>
						<td><fmt:formatNumber type="number" value="${entry.payOutFeeVat}" pattern="#,##0" /></td>
						<c:if test="${entry.payOutAmount != '0'}">
							<td><fmt:formatNumber type="number" value="${entry.bankFee}" pattern="#,##0" /></td>
						</c:if>
						<c:if test="${entry.payOutAmount == '0'}">
							<td><fmt:formatNumber type="number" value="0" pattern="#,##0" /></td>
						</c:if>
						<td><fmt:formatNumber type="number" value="${entry.payOutAmount}" pattern="#,##0" /></td>
						<c:if test="${entry.trxType == '0'}">
							<td><fmt:formatNumber type="number" value="${entry.cancelAmount}" pattern="#,##0" /></td>
						</c:if>
						<c:if test="${entry.trxType == '1'}">
							<c:if test="${entry.cancelAmount != '0'}">
								<td> 
									<span onclick="modifiCancel('${entry.trxId}','${entry.cancelAmount}','${entry.payOutAmount}')" style="cursor:pointer">
									<fmt:formatNumber type="number" value="${entry.cancelAmount}" pattern="#,##0" /></span>
								</td>
							</c:if>
							<c:if test="${entry.cancelAmount == '0'}">
								<td>
									<div class="input-group input-group-sm">
										<div class="input-group-btn">
											<a class="btn btn-sm red retry-btn" href="javascript:inputCancel('${entry.trxId}','${entry.payOutAmount}');">입금</a>
										</div>
									</div>
								</td>
							</c:if>
						</c:if>
					
						<td>${entry.bankName}</td>
						<td>${entry.account}</td>
						<td>${entry.accntHolder}</td>
						<td class="date">${entry.payOutDay}${entry.payOutTime}</td>
						<td>
							<c:if test="${entry.resultCd == '0000'}">성공</c:if>
							<c:if test="${entry.resultCd != '0000'}"><span onclick="failPop('${entry.resultMsg}')" style="cursor:pointer">실패</span></c:if>
						</td>
						<c:if test="${entry.sendCnt != '3'}">
							<td>${entry.sendCnt}</td>
						</c:if>
						<c:if test="${entry.sendCnt == '3'}">
							<c:if test="${entry.resultCd == '0000'}">
								<td>${entry.sendCnt}</td>
							</c:if>
							<c:if test="${entry.resultCd != '0000'}">
								<td><a class="btn btn-sm red retry-btn" href="javascript:retryTrx('${entry.trxId}');">재전송</a></td>
							</c:if>
						</c:if>
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
</form>
</div>
<!-- 리스트 페이징 종료 -->
