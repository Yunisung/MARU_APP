<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
    <div class="caption font-red-sunglo">
        <i class="icon-share font-red-sunglo"></i>
        <span class="caption-subject bold uppercase"> Result </span>
        <span class="caption-helper" style="display:none"><span id="page-total">${CPR.page.total}</span> 건</span>
        <span class="caption-helper amount_sum" style="color:#00a2ff;font-weight:600;">
			인증 금액 합계: <fmt:formatNumber type="number" value="${TOTAL_COUNT }" pattern="#,##0" /> 건(<fmt:formatNumber type="number" value="${TOTAL_SUM }" pattern="#,##0" />원),
            실명인증 : <fmt:formatNumber type="number" value="${OWNER_COUNT }" pattern="#,##0" /> 건(<fmt:formatNumber type="number" value="${OWNER_SUM }" pattern="#,##0" />원),
            1원인증 : <fmt:formatNumber type="number" value="${ACCOUNT_COUNT }" pattern="#,##0" /> 건(<fmt:formatNumber type="number" value="${ACCOUNT_SUM }" pattern="#,##0" />원),
            ARS인증 : <fmt:formatNumber type="number" value="${ARS_COUNT }" pattern="#,##0" /> 건(<fmt:formatNumber type="number" value="${ARS_SUM }" pattern="#,##0" />원)
		</span>
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
        <table class="pg-table table table-striped table-hover flip-content" id="sortTable">
            <!-- table-bordered -->
            <thead>
            <tr>
                <th>No</th>
                <th style="min-width: 140px">인증일시</th>
                <th>인증수수료ID</th>
                <th style="min-width: 130px">가맹점명</th>
                <th>가맹점ID</th>
                <th>가상계좌은행</th>
                <th>가상계좌</th>
                <th>인증수단</th>
                <th>인증결과</th>
                <th>인증수수료</th>
                <th>인중수수료VAT</th>
                <th>인증수수료정산결과</th>
                <th>정산번호</th>
                <th>정산예정일</th>
                <th>정산유형</th>
            </tr>
            </thead>
            <tbody id="list">
            <c:if test="${CPR.result.code != 200}">
                <tr>
                    <td colspan="15">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
                </tr>
            </c:if>
            <c:forEach var="entry" items="${CPR.data}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td class="date">${entry.regDate}</td>
                    <td>${entry.authId}</td>
                    <td>${entry.mchtName}</td>
                    <td>${entry.mchtId}</td>
                    <td>${entry.vactBank}</td>
                    <td>${entry.vactAccount}</td>
                    <td>${entry.summary}</td>
                    <td>${entry.resultMsg}</td>
                    <td>${entry.authFee}</td>
                    <td>${entry.authFeeVat}</td>
                    <td>${entry.stlStatus}</td>
                    <td>${entry.stlId}</td>
                    <td>${entry.stlDay}</td>
                    <td>${entry.stlType}</td>
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
</div>
<!-- 리스트 페이징 종료 -->
