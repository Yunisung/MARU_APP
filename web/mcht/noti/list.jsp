<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
    <div class="caption font-red-sunglo">
        <i class="icon-share font-red-sunglo"></i>
        <span class="caption-subject bold uppercase"> RESULT </span>
        <span class="caption-helper"><span id="page-total">${CPR.page.total}</span> 건 조회됨.</span>
    </div>
    <div class="actions">
        <c:if test="${CP_SESSION.grade eq '본사'}">
            <a href="/mcht/noti/add" class="btn btn-circle btn-default">
                <i class="fa fa-pencil"></i> 등록
            </a>
        </c:if>
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
                <th data-sort="string">종류</th>
                <th data-sort="string">ID</th>
                <th data-sort="string">거래구분</th>
                <th data-sort="string">사용여부</th>
                <th data-sort="string">전달주소</th>
                <th data-sort="string">수정</th>
                <th data-sort="string">등록자아이디</th>
                <th data-sort="string">등록일시</th>
            </tr>
            </thead>
            <tbody id="list">
            <c:if test="${CPR.result.code != 200}">
                <tr>
                    <td colspan="10">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
                </tr>
            </c:if>
            <c:forEach var="entry" items="${CPR.data}" varStatus="status">
                <tr>
                    <td>${CPR.page.total-((CPR.page.current-1)*CPR.page.size)-status.count+1}</td>
                    <c:if test="${entry.idType == 'distId'}">
                        <td>대행사</td>
                    </c:if>
                    <c:if test="${entry.idType == 'agencyId'}">
                        <td>에이전시</td>
                    </c:if>
                    <c:if test="${entry.idType == 'salesId'}">
                        <td>지사</td>
                    </c:if>
                    <c:if test="${entry.idType == 'mchtId'}">
                        <td>가맹점</td>
                    </c:if>
                    <c:if test="${entry.idType == 'tmnId'}">
                        <td>터미널</td>
                    </c:if>
<%--                    <td>${entry.idType}</td>--%>
                    <td>${entry.id}</td>
<%--                    <td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.mchtId}</td>--%>
<%--                    <td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.name}</td>--%>
                    <td>${entry.trxType}</td>
                    <td>${entry.status}</td>
                    <td>${entry.hookUrl}</td>
                    <td class="link" data-url="/mcht/noti/modify/${entry.idx}">
                        <div class="btn btn-sm default">수정</div>
                    </td>
                    <td>${entry.regId}</td>
                    <td class="date">${entry.regDate}</td>
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
