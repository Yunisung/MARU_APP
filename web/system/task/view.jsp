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
                  <li><span>작업 관리</span></li>
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
									<!------------ 본문 시작 ------------>
									<div class="portlet light tasks-widget bordered">
										<div class="portlet-title">
											<div class="chat-form">
												<div class="input-target">
													<input class="form-control" id="task-target" type="text" placeholder="대상유저 ID">
												</div>
												<div class="btn-cont">
													<span class="arrow"> </span>
													<a href="javascript:;" class="btn blue icn-only taks-add">
														<i class="fa fa-check icon-white"></i>
													</a>
												</div>
												<div class="input-cont">
													<input class="form-control" id="task-comment" type="text" placeholder="작업내역을 입력하세요.">
												</div>
												
												
											</div>
											<!-- <div class="actions">
												<div class="btn-group">
													<a class="btn blue-oleo btn-circle btn-sm" href="javascript:;" data-toggle="dropdown" data-hover="dropdown" data-close-others="true" aria-expanded="false">
														More <i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu pull-right">
														<li><a href="javascript:;"> All Project </a></li>
														<li class="divider"></li>
														<li><a href="javascript:;"> AirAsia </a></li>
														<li><a href="javascript:;"> Cruise </a></li>
														<li><a href="javascript:;"> HSBC </a></li>
														<li class="divider"></li>
														<li><a href="javascript:;">
																Pending
																<span class="badge badge-danger"> 4 </span>
															</a></li>
														<li><a href="javascript:;">
																Completed
																<span class="badge badge-success"> 12 </span>
															</a></li>
														<li><a href="javascript:;">
																Overdue
																<span class="badge badge-warning"> 9 </span>
															</a></li>
													</ul>
												</div>
											</div> -->
										</div>
										<div class="portlet-body">
											<div class="task-content">
												<div class="slimScrollDiv">
													<div class="scroller">
														<!-- START TASK LIST -->
														<ul class="task-list">
															<c:forEach var="entry" items="${DATAMAP}" varStatus="status">
																<li>
																	<!-- <div class="task-checkbox">
																		<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"> <input type="checkbox" class="checkboxes" value="1"> <span></span>
																		</label>
																	</div> -->
																	<div class="task-user">
																		<span <c:if test="${CP_SESSION.userId eq entry.regId}">style="color:#32c5d2;"</c:if> class="task-title-sp" data-id='${entry.regId }'> ${entry.userName } (${entry.gradeName})</span>
																		<!-- <span class="label label-sm label-danger">Marketing</span> -->
																	</div>
																	<div class="task-title">
																		<span class="task-title-sp"> ${entry.task } </span>
																		<!-- <span class="label label-sm label-danger">Marketing</span> -->
																	</div>
																	<div class="date">
																		${entry.date }
																		<c:if test="${CP_SESSION.userId eq entry.regId}">
																			<a href="javascript:;" class="btn btn-sm task-remove" data-idx="${entry.idx}">X</a>
																		</c:if>
																	</div> 
																	<!-- <div class="task-config">
																		<div class="task-config-btn btn-group">
																			<a class="btn btn-sm default" href="javascript:;" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
																				<i class="fa fa-cog"></i> <i class="fa fa-angle-down"></i>
																			</a>
																			<ul class="dropdown-menu pull-right">
																				<li><a href="javascript:;">
																						<i class="fa fa-check"></i> Complete
																					</a></li>
																				<li><a href="javascript:;">
																						<i class="fa fa-pencil"></i> Edit
																					</a></li>
																				<li><a href="javascript:;">
																						<i class="fa fa-trash-o"></i> Cancel
																					</a></li>
																			</ul>
																		</div>
																	</div> -->
																</li>
															</c:forEach>
														</ul>
														<!-- END START TASK LIST -->
													</div>
													<div class="slimScrollBar" style="background: rgb(187, 187, 187); width: 7px; position: absolute; top: 41px; opacity: 0.4; display: block; border-radius: 7px; z-index: 99; right: 1px; height: 271.153px;"></div>
													<div class="slimScrollRail" style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(234, 234, 234); opacity: 0.2; z-index: 90; right: 1px;"></div>
												</div>
											</div>
											<div class="task-footer"></div>
										</div>
									</div>

								</div>
							</div>
					</div>
					<!-- BEGIN CONTAINER -->
			</div>
			<c:import url="/include/footer.jsp" />
		</div>
		<c:import url="/include/javascript.jsp" />
		<script type="text/javascript">
			$('#nav-customer').removeClass('active');

			$('.taks-add').click(function() {
				var sendData = {};
				sendData.task = $('#task-comment').val();
				sendData.targetId = $('#task-target').val();
				sendData.grade = '';
				
				if($('#task-comment').val().length < 1) {
					return;
				}
				
				$.ajax({
					url : "/system/task/insert",
					type : 'POST',
					data : sendData,
					success : function(resDate, textStatus) {
						if (resDate.result === 'OK') {
							window.location.href = "/system/task/view";
						} else {
							bootbox.alert("작업내역 등록에 실패했습니다.");
						}
					},
					error : function(xhr, status, error) {
						bootbox.alert("작업내역 등록에 실패했습니다.");
					}
				});
			});
			
			$('.task-remove').click(function() {
				$.ajax({
					url : "/system/task/remove",
					type : 'POST',
					data : 'idx='+$(this).attr('data-idx'),
					success : function(resDate, textStatus) {
						if (resDate.result === 'OK') {
							window.location.href = "/system/task/view";
						} else {
							bootbox.alert("작업내역 삭제에 실패했습니다.");
						}
					},
					error : function(xhr, status, error) {
						bootbox.alert("작업내역 삭제에 실패했습니다.");
					}
				});
				
			});
			
			$('.task-title-sp').click(function() {
				$('#task-target').val($(this).attr('data-id'));
			});
			
			$('#nav-customer').addClass('active');
		</script>
		<!-- 모달 생성을 위한 베이스 -->
		<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>