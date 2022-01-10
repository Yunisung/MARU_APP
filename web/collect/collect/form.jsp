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
									<li><span>입금정산</span><i class="fa fa-circle"></i></li>
									<li><span>입금정산</span></li>
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
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/collect/collect/list" method="post">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm collectId" name="collectId" data-oper="lk" placeholder="입금 ID">
															</div>
														</div>

														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금 대상</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm categoryId" name="categoryId" data-oper="lk" placeholder="입금대상">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange">
																	<input type="text" class="form-control now-date" name="collectDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="collectDay" value="" data-oper="le">
																</div>
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
		$('#nav-collect').addClass('active');
		
		$(document).on('click', '.btn-delete-collect', function() {
			var id = $(this).attr('data-id');
			bootbox.confirm('해당 내역을 삭제하시겠습니까?', function(res) {
				if(res) {
					$.ajax({
						url: '/collect/delete/' + id,
						type:'get',
						success: function(res) {
							if(res.result == 'OK') {
								bootbox.alert('입금정산 삭제에 성공했습니다.', function() {
									window.location.reload();
								});
							} else {
								bootbox.alert('입금정산 삭제에 실패했습니다.'+ res.msg);	
							}
						}, 
						error: function(res, stats) {
							console.log(res, status);
							bootbox.alert('입금정산 삭제에 실패했습니다.'+ status);	
						}
					});
				}
			});
		});
		
		function deleteCollect(collectIds, summary) {
			bootbox.confirm('선택된 입금정산을 삭제하시겠습니까?', function(res) {
				if(res) {
					console.log(collectIds, summary);
					$.ajax({
						url: '/collect/delete/array',
						type : "post",
						dataType : "text",
						beforeSend : function(xhr) {
							xhr.setRequestHeader("Content-type",
									"application/json;charset=utf-8");
						},
						data: JSON.stringify({collectIds: collectIds, summary: summary}),
						success: function(res) {
							res = JSON.parse(res);
							console.log(res);
							if(res.result == 'OK') {
								bootbox.alert('입금정산 삭제에 성공했습니다.', function() {
									window.location.reload();
								});
							} else {
								bootbox.alert('입금정산 삭제에 실패했습니다.'+ res.msg);	
							}
						}, 
						error: function(res, stats) {
							console.log(res, status);
							bootbox.alert('입금정산 삭제에 실패했습니다.'+ status);	
						}
					});
				}
			});
					
				
		}
		
		// 일괄 삭제
		function deleteCollectSelected() {
			var collectIds = [];
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					collectIds.push($(e).attr('data-collectId'));
				}
			});

			if (collectIds.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				bootbox.prompt({
				    title: "변경 사유를 입력하세요.",
				    inputType: 'textarea',
				    callback: function (summary) {
				    	if(summary === null) {
				    	} else if(summary.length < 1) {
				    		bootbox.alert("변경 사유를 반드시 입력해야 합니다.");
				    	} else {
				    		console.log(collectIds);
				    		deleteCollect(collectIds, summary);
				    	}
				    }
				});
			}
		};
		
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>