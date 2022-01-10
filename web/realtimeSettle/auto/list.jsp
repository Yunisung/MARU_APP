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
	 		<span class="caption-detail"> 결제금액 합계: <fmt:formatNumber type="number" value="${SUMMAP.amtSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 가맹점 수수료 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.vatSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 출금 수수료 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.payOutVatSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 차감금액 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.minusAmtSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 예수금 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.deductAmtSum}" pattern="#,##0" />원,</span>  
	 		<span class="caption-detail"> 실출금액 합계 금액: <fmt:formatNumber type="number" value="${SUMMAP.payOutAmountSum}" pattern="#,##0" />원</span>  
		 </span>
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForExcel();">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForPDF();">
			<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		</a>
		<%-- 
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '자동 정산내역');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>--%>
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
					<th>정산번호</th>
					<th>정산주기</th>
					<th>지급상태</th>
					<th style="min-width: 80px;">정산일</th>
					<th style="min-width: 80px;">출금일</th>
					<th>출금시간</th>
					<th style="min-width: 150px;">가맹점명</th>
					<th>가맹점ID</th>
					<th style="min-width: 100px;">대상거래 기간</th>
					<th>거래건수</th>
					<th>거래금액</th>
					<th>가맹점수수료</th>
					<th>출금수수료</th>
					<th>은행수수료</th>
					<th>정산예정금액</th>
					<th>차감금액</th>
					<th>예수금</th>
					<th>실지급액</th>
					<th>차액</th>
					<th style="min-width: 100px;">지급은행</th>
					<th style="min-width: 120px;">지급계좌</th>
					<th style="min-width: 100px;">지급계좌<br>예금주</th>
					<c:if test="${CP_SESSION.grade == '본사'}">
							<th>지급정산<br>수수료율</th>
							<th>출금<br>결과메시지</th>
							<th>출금<br>전송횟수</th>
					</c:if>
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
				<c:set var="var18" value="0"/>
				<c:set var="var19" value="0"/>
				<c:set var="var20" value="0"/>
				<c:set var="var21" value="0"/>
				<c:set var="var22" value="0"/>
				<c:if test="${CP_SESSION.grade == '본사'}">
					<c:set var="var23" value="0"/>
					<c:set var="var24" value="0"/>
					<c:set var="var25" value="0"/>
				</c:if>
				
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr data-stlId="${entry.stlId}" 
						<c:if test="${status.index ne 0 && entry.stlDay ne CPR.data[status.index-1].stlDay}">
							class="bg-grey-cararra bg-font-grey-cararra"
						</c:if>>
						<td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
						<td>${entry.stlId}</td>
						<td>${entry.stlType}</td>
						<td>${entry.status}</td>
						<td class="date">${entry.stlDay}</td>
						<td class="date">${entry.payOutDay}</td>
						<td class="time">${entry.payOutTime}</td>
						<td>${entry.name}</td>
						<td>${entry.mchtId}</td>
						<td><span class="date">${entry.startDay}</span> ~<br><span class="date">${entry.endDay}</span></td>
						<td class="text-right digits">${entry.payCnt + entry.rfdCnt}</td>
						<td class="text-right digits">${entry.payAmt + entry.rfdAmt}</td>
						<td class="text-right digits">${(entry.payFee + entry.payVat) + (entry.rfdFee + entry.rfdVat)}</td>
						<td class="text-right digits">
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
								0
							</c:if>
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
								${entry.payOutFee + entry.payOutFeeVat}
							</c:if>
						</td>
						<td class="text-right digits">
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
								0
							</c:if>
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
								${entry.bankFee}
							</c:if>
						</td>
						<td class="text-right digits">
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
								${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee)}
							</c:if>
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
								${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - (entry.payOutFee + entry.payOutFeeVat)}
							</c:if>
						</td>
						<td>
							<c:if test="${entry.resultCd == '0000'}">
								<c:if test="${entry.minusAmt == '0'}">0</c:if>
								<c:if test="${entry.minusAmt != '0'}">
									<span onclick="msgPop('${entry.minusAmtMemo}')" style="cursor:pointer"><fmt:formatNumber type="number" value="${entry.minusAmt}" pattern="#,##0" /></span>
								</c:if>
							</c:if>
							<c:if test="${entry.resultCd != '0000'}">
								<c:if test="${entry.minusAmt == 0}">
									<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
										<span onclick="inputMinusAmt('${entry.stlId}','0')" style="cursor:pointer">
										<fmt:formatNumber type="number" value="${entry.minusAmt}" pattern="#,##0" /></span>
									</c:if>
									<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
										<span onclick="inputMinusAmt('${entry.stlId}','${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - (entry.payOutFee + entry.payOutFeeVat) + entry.deductAmt}')" style="cursor:pointer">
										<fmt:formatNumber type="number" value="${entry.minusAmt}" pattern="#,##0" /></span>
									</c:if>
								</c:if>
								<c:if test="${entry.minusAmt != 0}">
									<span onclick="modifiMinusAmt('${entry.stlId}','${entry.minusAmt}','${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - (entry.payOutFee + entry.payOutFeeVat) + entry.deductAmt}')" style="cursor:pointer">
									<fmt:formatNumber type="number" value="${entry.minusAmt}" pattern="#,##0" /></span>
								</c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${entry.resultCd == '0000'}">
								<c:if test="${entry.deductAmt == '0'}">0</c:if>
								<c:if test="${entry.deductAmt != '0'}">
									<span onclick="msgPop('${entry.deductAmtMemo}')" style="cursor:pointer"><fmt:formatNumber type="number" value="${entry.deductAmt}" pattern="#,##0" /></span>
								</c:if>
							</c:if>
							<c:if test="${entry.resultCd != '0000'}">
								<c:if test="${entry.deductAmt == 0}">
									<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
										<span onclick="inputDeductAmt('${entry.stlId}','${entry.payOutFee + entry.payOutFeeVat}','0')" style="cursor:pointer">
										<fmt:formatNumber type="number" value="${entry.deductAmt}" pattern="#,##0" /></span>
									</c:if>
									<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
										<span onclick="inputDeductAmt('${entry.stlId}','${entry.payOutFee + entry.payOutFeeVat}','${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt}')" style="cursor:pointer">
										<fmt:formatNumber type="number" value="${entry.deductAmt}" pattern="#,##0" /></span>
									</c:if>
								</c:if>
								<c:if test="${entry.deductAmt != 0}">
									<span onclick="modifiDeductAmt('${entry.stlId}','${entry.payOutFee + entry.payOutFeeVat}','${entry.deductAmt}','${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt}')" style="cursor:pointer">
									<fmt:formatNumber type="number" value="${entry.deductAmt}" pattern="#,##0" /></span>
								</c:if>
							</c:if>
						</td>
						<td class="text-right digits">
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
								${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt}
							</c:if>
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
								${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - (entry.payOutFee + entry.payOutFeeVat) - entry.minusAmt + entry.deductAmt}
							</c:if>
						</td>
						<td class="text-right digits">
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt == 0}">
								0
							</c:if>
							<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - entry.minusAmt + entry.deductAmt != 0}">
								<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) == 0}">
									${entry.deductAmt - entry.minusAmt - (entry.payOutFee + entry.payOutFeeVat)}
								</c:if>
								<c:if test="${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) != 0}">
									${entry.deductAmt - entry.minusAmt}
								</c:if>
							</c:if>
							</td>
						<td style="min-width:60px;">${entry.bankName}</td>
						<td style="mso-number-format:'\@'">${entry.account}</td>
						<td style="min-width:100px;">${entry.accntHolder}</td>
						
						<c:if test="${CP_SESSION.grade == '본사'}">
							<td class="text-right"><fmt:formatNumber value="${entry.stlRate * 100}" pattern="0.000"/> %</td>
							<td>
								<c:if test="${entry.resultCd == ''}"></c:if>
								<c:if test="${entry.resultCd == '0000'}">성공</c:if>
								<c:if test="${entry.resultCd != '' && entry.resultCd != '0000'}"><span onclick="msgPop('${entry.resultMsg}')" style="cursor:pointer">실패</span></c:if>
							</td>
							<c:if test="${entry.sendCnt < '3'}">
								<c:if test="${entry.resultCd != '0000' && entry.nowTime > 130100}">
									<td><a class="btn btn-sm red retry-btn" href="javascript:retrySend('${entry.stlId}', '${entry.bankCd}', '${entry.account}', '${entry.mchtId}', '${entry.stlDay}', '${entry.stlType}', '${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - (entry.payOutFee + entry.payOutFeeVat) - entry.minusAmt + entry.deductAmt}');">재전송</a></td>
								</c:if>
								<c:if test="${entry.resultCd != '0000' && entry.nowTime < 130100}">
									<td>${entry.sendCnt}</td>
								</c:if>
								<c:if test="${entry.resultCd == '0000'}">
									<td>${entry.sendCnt}</td>
								</c:if>
							</c:if>
							<c:if test="${entry.sendCnt > '2'}">
								<c:if test="${entry.resultCd == '0000'}">
									<td>${entry.sendCnt}</td>
								</c:if>
								<c:if test="${entry.resultCd != '0000'}">
									<td><a class="btn btn-sm red retry-btn" href="javascript:retrySend('${entry.stlId}', '${entry.bankCd}', '${entry.account}', '${entry.mchtId}', '${entry.stlDay}', '${entry.stlType}', '${(entry.payAmt - entry.payFee - entry.payVat) + (entry.rfdAmt - entry.rfdVat - entry.rfdFee) - (entry.payOutFee + entry.payOutFeeVat) - entry.minusAmt + entry.deductAmt}');">재전송</a></td>
								</c:if>
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
