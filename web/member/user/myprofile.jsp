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
                  
                  <li><span>내 정보</span></li>
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
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-body light">
											<div class="tabbable-line">
												<ul class="nav nav-tabs">
													<li class="active"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 유저 정보 </a></li>
													<c:if test="${DATAMAP.grade eq '가맹점'}">
														<li class=""><a href="#tab_mcht" data-toggle="tab" aria-expanded="true"> 가맹점 가입 정보 </a></li>
													</c:if>
													<li class=""><a href="#tab_access" data-toggle="tab" aria-expanded="true"> 접속 정보 </a></li>
												</ul>
												<div class="tab-content">
													<!-- 기본 정보 탭 시작 -->
													<div class="tab-pane active" id="tab_basic">
														<!-- BEGIN FORM-->
														<form class="form-horizontal" role="form">
															<div class="form-body">
																<!-- <h3 class="form-section">사용자 기본 정보</h3> -->
																<div class="row">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">아이디:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.id}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">이름:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.name}</p>
																			</div>
																		</div>
																	</div>

																</div>
																<!--/row-->
																<div class="row">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">전화번호:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.phone}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">소속:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.grade}</p>
																			</div>
																		</div>
																	</div>
																</div>
																<!--/row-->
																<div class="row">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">권한:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.role}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">비밀번호</label>
																			<div class="col-md-9">
																				<button type="button" class="btn btn-sm green" onclick="javascript:openPassword('/member/user/password/${DATAMAP.id}');">
																					<i class="fa fa-pencil"></i> 비밀번호 변경
																				</button>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</form>
													</div>
													<!-- END FORM-->
													<!-- 기본 정보 탭 종료 -->
													<c:if test="${DATAMAP.grade eq '가맹점'}">
														<!-- 가맹점 정보 탭 시작 -->
														<div class="tab-pane" id="tab_mcht">
															<!-- BEGIN FORM-->
															<form class="form-horizontal form" role="form">
																<div class="form-body row">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">아이디</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.mchtId}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">이름</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.name}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">${DATAMCHTMAP.idType}</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.identity}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">가맹점명</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.nick}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">업종</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.bizType}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">업태</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.bizCategory}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->

																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">상태</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.status}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">등록일시</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.regDate}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->

																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">사업장연락처</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.tel1}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">휴대폰번호</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.tel2}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">팩스</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.fax}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">우편번호</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.zip}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">주소</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.addr1}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">상세주소</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.addr2}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																</div>

															</form>
															<!-- END FORM-->
														</div>
														<!-- 기본 정보 탭 종료 -->
														<!-- 관리자 탭 시작 -->
														<div class="tab-pane" id="tab_additional">
															<!-- BEGIN FORM-->
															<form class="form-horizontal form" role="form">
																<div class="form-body row">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 이름</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoName}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 식별번호</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoIdentity}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 휴대폰</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoPhone}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 집전화</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoTel}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 우편번호</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoZip}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 주소</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoAddr1}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 상세주소</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.ceoAddr2}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">담당자 이름</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.managerName}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">담당자 전화번호</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMCHTMAP.managerPhone}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																</div>

															</form>
															<!-- END FORM-->
														</div>
														
													</c:if>
													<!-- 접속 정보 탭 시작 -->
													<div class="tab-pane" id="tab_access">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-share font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> Access Info </span>
																</div>
																<div class="actions">
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
																				<th data-sort="string">아이디</th>
																				<th data-sort="string">접속 아이피</th>
																				<th data-sort="string">접속일시</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATAACCESSMAP}" varStatus="status">
																				<tr>
																					<td>${status.count+1}</td>
																					<td>${entry.id}</td>
																					<td>${entry.ipAddr}</td>
																					<td>${entry.regDate}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
													<!-- 접속 정보 탭 종료 -->
												</div>
											</div>
										</div>
									</div>
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

	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>