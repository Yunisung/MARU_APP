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
									<li><span>가맹점 관리</span><i class="fa fa-circle"></i></li>
									<li><span>가맹점 수수료 템플릿 조회</span></li>
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
								<div class="page-content-inner" id="search-container" >
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form" style="display:none;">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/mcht/feeTemplate/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="가맹점 수수료 템플릿">
												<input type="hidden" data-reg="false" name="thead" value="name:템플릿명,rate:가맹점수수료,distRate:대행사수수료,agencyRate:에이전시수수료,salesRate:지사수수료,diff0DistRate:차액정산대행사영세(신용),diff1DistRate:차액정산대행사중소1(신용),diff2DistRate:차액정산대행사중소2(신용),diff3DistRate:차액정산대행사중소3(신용),diff0CheckDistRate:차액정산대행사영세(체크),diff1CheckDistRate:차액정산대행사중소1(체크),diff2CheckDistRate:차액정산대행사중소2(체크),diff3CheckDistRate:차액정산대행사중소3(체크),diff0AgencyRate:차액정산에이전시영세(신용),diff1AgencyRate:차액정산에이전시중소1(신용),diff2AgencyRate:차액정산에이전시중소2(신용),diff3AgencyRate:차액정산에이전시중소3(신용),diff0CheckAgencyRate:차액정산에이전시영세(체크),diff1CheckAgencyRate:차액정산에이전시중소1(체크),diff2CheckAgencyRate:차액정산에이전시중소2(체크),diff3CheckAgencyRate:차액정산에이전시중소3(체크),diff0SalesRate:차액정산지사영세(신용),diff1SalesRate:차액정산지사중소1(신용),diff2SalesRate:차액정산지사중소2(신용),diff3SalesRate:차액정산지사중소3(신용),diff0CheckSalesRate:차액정산지사영세(체크),diff1CheckSalesRate:차액정산지사중소1(체크),diff2CheckSalesRate:차액정산지사중소2(체크),diff3CheckSalesRate:차액정산지사중소3(체크)">
												<div class="form-body">
												</div>
												<div class="form-actions nobg right">
													<div class="">
														<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
															<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
														</button>
														<button type="button" class="btn btn-sm green" id="search_submit" onClick="searchForList()">
															<i class="fa fa-search" aria-hidden="true"></i> SEARCH
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult">
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
		searchForList();//검색 실행
		$('#nav-mcht').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>