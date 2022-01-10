<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head></head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h4>${DATAMAP.name}리스크 내역조회</h4>
	</div>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<div class="portlet light portlet-form">
			<div class="portlet-title">
				<div class="actions">
					<a class="btn btn-sm green"
						href="/mcht/doc/upload/${DATAMAP.mchtId }">파일 등록</a>
					<c:if test="${CP_SESSION.grade eq '본사'}">
						<a class="btn btn-sm green"
							href="javascript:statusChangeAll('본사')">본사뷰 권한으로 설정</a>
					</c:if>
				</div>
			</div>
			<div class="portlet-body form light">

				<div class="table-scrollable">
					<!-- 리스트 본문 시작 -->
					<table
						class="pg-table table table-striped table-hover flip-content">
						<!-- table-bordered -->
						<thead>
							<tr>
								<th>No</th>
								<th>파일명</th>
								<th>용량</th>
								<th>등록자</th>
								<th>등록일</th>
								<th>다운로드</th>
								<c:if test="${CP_SESSION.grade eq '본사'}">
									<th>뷰 권한</th>
									<th>Action</th>
								</c:if>
							</tr>
						</thead>
						<tbody id="list">
							<c:if test="${fn:length(DATADOCMAP) < 1}">
								<tr>
									<td colspan="9">조회 가능한 서류목록이 없습니다.</td>
								</tr>
							</c:if>
							<c:if test="${fn:length(DATADOCMAP) > 0}">
								<c:forEach var="entry" items="${DATADOCMAP}" varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td>${entry.fileNm}</td>
										<td>${entry.fileSize}</td>
										<td>${entry.regId}</td>
										<td class="date">${entry.regDay}</td>
										<td class="link download-link"
											data-url="/uploadFile/get/${entry.idx}">DOWNLOAD</td>
										<c:if test="${CP_SESSION.grade eq '본사'}">
											<td>${entry.status}</td>
											<td><c:if test="${entry.status eq '본사'}">
													<a class="btn btn-sm btn-info"
														href="javascript:statusChange('${entry.idx}','사용')">전체
														뷰</a>
												</c:if> <c:if test="${entry.status eq '사용'}">
													<a class="btn btn-sm btn-success"
														href="javascript:statusChange('${entry.idx}','본사')">본사
														뷰</a>
												</c:if> <a class="btn btn-sm btn-danger"
												href="javascript:statusChange('${entry.idx}','폐기')">폐기</a></td>
										</c:if>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- END FORM-->
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>
</html>