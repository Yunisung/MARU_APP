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
<link href="/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
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
										가맹점 조회 &nbsp;&nbsp; <small>* Home &gt; 가맹점 </small>
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
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/user/list" method="post">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">아이디</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm id" name="id" data-oper="lk" placeholder="아이디">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">등록일</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control" name="regDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control" name="regDay" value="${endDate}" data-oper="le">
																</div>
															</div>
														</div>
													</div>
													<div class="row search-opt">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">소속</label>
															<select class="selectpicker col-lg-8" name="grade" data-oper="eq">
																<option value="">소속</option>
																<option value="본사">본사</option>
																<option value="대행사">대행사</option>
																<option value="에이전시">에이전시</option>
																<option value="지사">지사</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">상태</label>
															<select class="selectpicker col-lg-8" name="status" data-oper="eq">
																<option value="">상태</option>
																<option value="본사">예비</option>
																<option value="사용">사용</option>
																<option value="중지">중지</option>
															</select>
														</div>

													</div>
												</div>
												<div class="form-actions nobg right">
													<div class="btn folding-search-btn folding-down"></div>
													<div class="">
														<button type="button" class="btn green" id="search_submit" onClick="searchForList()">
															<i class="fa fa-search" aria-hidden="true"></i> 검색
														</button>
														<button type="button" class="btn default" id="SearchClear">
															<i class="fa fa-trash" aria-hidden="true"></i> 초기화
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-title">
											<div class="caption font-red-sunglo">
												<i class="icon-share font-red-sunglo"></i>
												<span class="caption-subject bold uppercase"> Search Result </span>
												<span class="caption-helper"> 10 건 조회됨.</span>
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
										<div class="portlet-body form light" id="searchResult">
											<c:import url="./list.jsp" />
										</div>
									</div>
									<!-- 내용 폼 종료 -->
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
	<script src="/assets/global/plugins/moment.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.ko.min.js" type="text/javascript"></script>

	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>