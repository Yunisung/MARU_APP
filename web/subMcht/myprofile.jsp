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
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed">
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
										<div class="panel-body">
										</div>
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
													<li class="active"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 사용자 정보 </a></li>
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
																				<p class="form-control-static">${DATAMAP.tmnId}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">상호:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlName}</p>
																			</div>
																		</div>
																	</div>
																</div>
																<!--/row-->
																<div class="row">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대표자 이름</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlCeoName}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">은행:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlBankCd}</p>
																			</div>
																		</div>
																	</div>
																</div>
																<!--/row-->
																<div class="row">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">계좌:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlAccntHolder}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">계좌번호:</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlAccount}</p>
																			</div>
																		</div>
																	</div>
																</div>
																<div class="row">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">주소</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlAddr1} ${DATAMAP.dtlAddr2}</p>
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">우편번호</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.dtlZip}</p>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</form>
													</div>
													<!-- END FORM-->
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				</div>
				<!-- BEGIN CONTAINER -->
			</div>
		</div>
		<c:import url="/subMcht/include/footer.jsp" />
	</div>
	<c:import url="/subMcht/include/javascript.jsp" />
	<script type="text/javascript">
	$('#nav-my').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>