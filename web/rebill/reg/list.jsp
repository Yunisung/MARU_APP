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
                <c:if test="${ (CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
                    <th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
                </c:if>
                <th style="min-width: 140px">정기결제ID</th>
                <th>가맹점ID</th>
                <th>가맹점이름</th>
                <th>터미널ID</th>
                <th>금액</th>
                <th>상태</th>
                <th>성명</th>
                <th>전화번호</th>
                <th>상품명</th>
                <th>결제주기</th>
                <th>결제횟수</th>
                <th>SMS사용</th>
                <th>다음결제일자</th>
                <th>만료일자</th>
                <th>등록일자</th>
            </tr>
            </thead>
            <tbody id="list">
            <c:if test="${CPR.result.code != 200}">
                <tr>
                    <td colspan="16">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
                </tr>
            </c:if>
            <c:forEach var="entry" items="${CPR.data}" varStatus="status">
                <tr data-rebillId="${entry.rebillId}">
                    <td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
                    <c:if test="${ (CP_SESSION.grade eq '본사') && CP_SESSION.role != '일반'}">
                        <td class="btn-td">
                            <input type="checkbox" class="row-check" id="${entry.authId}_check" class="checkbox-style" /><label for="${entry.rebillId}_check"></label>
                        </td>
                    </c:if>
                    <td>${entry.rebillId}</td>
                    <td>${entry.mchtId}</td>
                    <td>${entry.nick}</td>
                    <td>${entry.tmnId}</td>
                    <td>${entry.amount}</td>
                    <td>${entry.status}</td>
                    <td>${entry.payerName}</td>
                    <td>${entry.payerTel}</td>
                    <td>${entry.productName}</td>
                    <td>${entry.rebillCycleType}</td>
                    <td>${entry.rebillCount}</td>
                    <td>${entry.rebillSmsUse}</td>
                    <td class="date">${entry.nextPayDay}</td>
                    <td class="date">${entry.expireDay}</td>
                    <td class="date">${entry.regDay}</td>
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
