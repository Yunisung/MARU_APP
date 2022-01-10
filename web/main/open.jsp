<%@page contentType="text/html; charset=UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%> 
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<c:import url="/include/head.jsp" />	
</head>
<!-- END HEAD -->
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
	<div class="page-wrapper">
		<c:import url="/include/header.jsp" />
		<!-- BEGIN CONTAINER -->
		<div class="page-container">
			<c:import url="/include/nav.jsp" />
			<!-- BEGIN CONTENT -->
			<div class="page-content-wrapper">
				<div class="page-content">
					<div class="mtouch-container">
						 <!-- BEGIN PAGE BAR -->
						<div class="page-bar">
							<ul class="page-breadcrumb">
									<li><a href="/">Home</a><i class="fa fa-circle"></i></li>
									<li><span>Dashboard</span><i class="fa fa-circle"></i></li>
									<li><span>예비 & 승인대기 터미널</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">TEXT</div>
										</div>
									</div>
							</div>
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult">
										<div class="table-scrollable">
											<!-- 리스트 본문 시작 -->
											<table class="pg-table table table-striped table-hover flip-content" id="sortTable">
												<!-- table-bordered -->
												<thead>
													<tr>
														<th>No</th>
														<th data-sort="string">가맹점ID</th>
														<th data-sort="string">가맹점명</th>
														<th data-sort="string">터미널ID</th>
														<th data-sort="string">상태</th>
														<th data-sort="string">Tax</th>
														<th data-sort="string">시작일자</th>
														<th data-sort="string">터미널 정보</th>
														<th data-sort="string">등록자</th>
														<th data-sort="string">등록일시</th>
													</tr>
												</thead>
												<tbody id="list">
													<c:forEach var="entry" items="${TMNMAP}" varStatus="status">
														<tr>
															<td>${status.count+1}</td>
															<td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.mchtId}</td>
															<td class="link" data-url="/mcht/view/${entry.mchtId}/tab_basic">${entry.mchtName}</td>
															<td>${entry.tmnId}</td>
															<td><c:if test="${entry.status eq '대기'}">승인</c:if>${entry.status}</td>
															<td>${entry.taxName}</td>
															<td class="date">${entry.activeDate}</td>
															<td class="link" data-url="/mcht/tmn/modify/${entry.tmnId}">
																<div class="btn btn-sm default">정보 수정</div>
															</td>
															<td class="date">${entry.regId}</td>
															<td class="date">${entry.regDate}</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- 내용 폼 종료 -->
									</div>
									<!-- END PAGE CONTENT INNER -->
								</div>
							</div>
					</div>
					<!-- BEGIN CONTAINER -->
			</div>
			<c:import url="/include/footer.jsp" />
		</div>
		<c:import url="/include/javascript.jsp" />

		<script type="text/javascript">
			$('#nav-board').addClass('active');
		</script>
		<!-- 모달 생성을 위한 베이스 -->
		<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>