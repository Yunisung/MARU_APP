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
									<li><span>고객센터</span><i class="fa fa-circle"></i></li>
                  <li><span>공지사항</span></li>
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
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-bell-o"></i>
												<span class="caption-title"> ${DATAMAP.title} </span>
											</div>
											<div class="col-md-6 pull-right">
												<div class="form-group">
													<label class="control-label col-md-12" style="text-align: right;">개시자 : ${DATAMAP.regId}</label> <label class="control-label col-md-12" style="text-align: right;">개시일 : <span class="date">${DATAMAP.pubDay}</span></label>
												</div>
											</div>
											<div class="portlet-body light">
												<!-- BEGIN FORM-->
												<form class="form-horizontal form" role="form">
													<div class="form-body row">
														<!--/span-->
														<div class="col-md-12">
															<div class="form-group">
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.summary}</p>
																</div>
															</div>
														</div>
													</div>
													<div class="form-actions right">
														<div class="">
															<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role ne '일반'}">
																<button type="button" class="btn btn-sm green" onclick="location.href='/system/notice/modify/${DATAMAP.idx}';">
																	<i class="fa fa-pencil"></i> 수정
																</button>
															</c:if>
														</div>
													</div>
												</form>
												<!-- END FORM-->
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
	<script type="text/javascript">
	/*$(document).ready(function() {
		$('#summary').html();
	});*/
	
	
	$('#nav-customer').addClass('active');
	<c:if test="${(CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사') && CP_SESSION.role ne '일반'}">
		$('#nav-customer').removeClass('active');
		$('#nav-system').addClass('active');
	</c:if>
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>