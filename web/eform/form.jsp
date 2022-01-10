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
								<li><span>전자계약서</span><i class="fa fa-circle"></i></li>
                 					<li><span>전자계약서 내역 조회</span></li>
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
						<div class="page-content-inner" id="search-container">
							<!-- 검색 폼 시작 -->
							<div class="portlet light portlet-form">
								<div class="portlet-body form light">
									<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/eform/list" method="post">
										<div class="form-body">
											<div class="row">
								<div class="form-group pg-form-group">
		                            <label class="control-label col-lg-4">업체명</label>
									<div class="col-lg-8">
										<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="이름">
									</div>
								</div>
								<div class="form-group pg-form-group">
									<label class="control-label col-lg-4">발송일시</label>
									<div class="col-lg-8">
										<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
											<input type="text" class="form-control now-date" name="regDay" value="" data-oper="ge">
											<span class="input-group-addon">~</span>
											<input type="text" class="form-control now-date" name="regDay" value="" data-oper="le">
										</div>
									</div>
								</div>
								<div class="form-group pg-form-group">
									<label class="control-label col-lg-4">계약서 상태</label>
									<select class="selectpicker col-lg-8 doc_status" name="doc_status" data-oper="eq">
										<option value="">-- 상태 구분 --</option>
										<option value="WSI">휴대폰본인인증</option>
										<option value="WR">작성요청</option>
										<option value="WS">작성시작</option>
										<option value="WE">작성완료</option>
										<option value="WJ">작성거절</option>
										<option value="WX">작성기한만료</option>
										<option value="WC">전송취소</option>
									</select>
								</div>
								<div class="form-group pg-form-group">
									<label class="control-label col-lg-4">계약서 제목</label>
									<div class="col-lg-8">
										<input type="text" class="form-control input-sm doc_name" name="doc_name" data-oper="lk" placeholder="제목" data-search="doc_name">
									</div>
								</div>
							</div>
						</div>
										<div class="form-actions nobg right">
											<div class="btn folding-search-btn icon-arrow-down"></div>
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
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-eform').addClass('active');
		
		function failPop(message) {
			bootbox.alert(message);
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>