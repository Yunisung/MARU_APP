<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
	<c:import url="/include/head.jsp" />
	<style type="text/css">

	</style>
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-wrapper">
		<c:import url="/include/nav.jsp" />
		<div class="page-wrapper-row full-height">
			<div class="page-wrapper-middle">
				<!-- BEGIN CONTAINER -->
				<div class="page-container">
					<div class="page-content-wrapper">
						<div class="page-head">
							<div class="mtouch-container">
								<div class="page-title">
									<h1>
										가맹점 정보 &nbsp;&nbsp; <small>* Home &gt; 가맹점 정보</small>
									</h1>
								</div>
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
						</div>
						<div class="page-content">
							<div class="mtouch-container">
								<!-- BEGIN PAGE CONTENT - UNUSED - INNER -->
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-body light">
											<h3 style="font-size: 18px;">테스트 가맹점 페이지</h3>
											<div class="tabbable-line">
                                                <ul class="nav nav-tabs ">
                                                    <li class="active">
                                                        <a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 기본 정보 </a>
                                                    </li>
                                                    <li class="">
                                                        <a href="#tab_detail" data-toggle="tab" aria-expanded="true"> 결제 정보 </a>
                                                    </li>
                                                    <li class="">
                                                        <a href="#tab_other" data-toggle="tab" aria-expanded="false"> 기타 정보 </a>
                                                    </li>
                                                </ul>
                                                <div class="tab-content">
													<!-- 기본 정보 탭 시작 -->
                                                    <div class="tab-pane active" id="tab_basic">
														<c:import url="./view/basic.jsp" />
                                                    </div>
													<!-- 기본 정보 탭 종료 -->
													<!-- 결제 정보 탭 시작 -->
                                                    <div class="tab-pane" id="tab_detail">
                                                        <c:import url="./view/pay.jsp" />
                                                    </div>
													<!-- 결제 정보 탭 종료 -->
													<!-- 기타 정보 탭 시작 -->
                                                    <div class="tab-pane" id="tab_other">
                                                        <c:import url="./view/other.jsp" />
                                                    </div>
													<!-- 기타 정보 탭 종료 -->
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
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>