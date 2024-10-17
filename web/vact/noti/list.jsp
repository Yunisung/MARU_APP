<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
    <div class="actions">
        <div class="btn-group">
            <a href="javascript:;" class="btn btn-circle btn-default vact-noti-retry">
                <i class="fa fa-mail-reply"></i> 재전송
            </a>
        </div>
    </div>
</div>
<div class="portlet-body form light">
    <div class="table-scrollable">
        <!-- 리스트 본문 시작 -->
        <table class="pg-table table table-striped table-hover flip-content" id="sortTable">
            <!-- table-bordered -->
            <thead>
            <tr>
                <th class="ck-th" rowspan="3"><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
                <th>거래번호</th>
                <th>가맹점ID</th>
                <th>가맹점명</th>
                <th>금액</th>
                <th>거래유형</th>
                <th>거래일</th>
                <th>보낸사람</th>
                <th>가상계좌발행은행</th>
                <th>계좌번호</th>
                <th>전송URL</th>
                <th>전송횟수</th>
                <th>상태</th>
                <th>보낸시간</th>
                <th>등록일시</th>
            </tr>
            </thead>
            <tbody id="list">
            <c:if test="${CPR.result.code != 200}">
                <tr>
                    <td colspan="17">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
                </tr>
            </c:if>
            <c:forEach var="entry" items="${CPR.data}" varStatus="status">
                <tr data-notiIndex="${entry.vactId}">
                    <td class="ck-td btn-td">
                        <input type="checkbox" class="row-check" id="${entry.vactId}_check" class="checkbox-style" /><label for="${entry.vactId}_check"></label>
                    </td>
                    <td>${entry.vactId}</td>
                    <td>${entry.mchtId}</td>
                    <td title="${entry.mchtId }">${entry.mchtName}</td>
                    <td><fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" /></td>
                    <td>${entry.trxType}</td>
                    <td>${entry.trxDay}</td>
                    <td>${entry.sender}</td>
                    <c:choose>
                        <c:when test="${entry.bankCd eq '089'}"><td>케이뱅크</td></c:when>
                        <c:when test="${entry.bankCd eq '039'}"><td>경남은행</td></c:when>
                        <c:when test="${entry.bankCd eq '034'}"><td>광주은행</td></c:when>
                        <c:when test="${entry.bankCd eq '007'}"><td>수협은행</td></c:when>
                        <c:when test="${entry.bankCd eq '048'}"><td>신협은행</td></c:when>
                    </c:choose>
                    <td>${entry.account}</td>
                    <td>${entry.hookAddr}</td>
                    <td>${entry.hookRetry}</td>
                    <td>${entry.hookStatus}</td>
                    <td class="date">${entry.hookSentDate}</td>
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
