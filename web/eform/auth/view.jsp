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
									<li><span>전자계약서 권한 관리</span></li>
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
											<li class="active"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 기본 </a></li>
										</ul>

										<div class="tab-content">
											<!-- 기본 정보 탭 시작 -->
											<div class="tab-pane active" id="tab_basic">
												<!-- BEGIN FORM-->
												<form class="form-horizontal form" role="form" id="writeFrm" name="writeFrm">
													<div class="form-body row">
													
													<input type="hidden" id="authId" name="authId" value="">
													<input type="hidden" id="distId" name="distId" value="">
													<input type="hidden" id="distName" name="distName" value="">
													<input type="hidden" id="form_id" name="form_id" value="">
													<input type="hidden" id="doc_name" name="doc_name" value="">

														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">아이디</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.distId}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">이름</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.distName}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">계약서 목록</label>
																<div class="col-md-9">
																	<select class="selectpicker col-sm-6 " id='selectForm' onchange="listChange(this)">
																		<option selected>-- 계약서 선택 --</option>
																		<c:forEach var="entryMap" items="${DATAMAPFORM}">
																				<option value="${entryMap['form_id']}">${entryMap['doc_name']}</option>
																			</c:forEach>
																	</select>
																	<button type="button" class="btn btn-sm green" onclick="formDelete();">
																		<i class="fa fa-pencil"></i> 계약서 삭제
																	</button>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">계약서 선택</label>
																<div class="col-md-9">
																	<select class="selectpicker col-sm-6" id='selectForm' onchange="listChange(this)">
																		<option selected>-- 계약서 선택 --</option>
																	</select>
																	<button type="button" class="btn btn-sm green" onclick="formSave();">
																		<i class="fa fa-pencil"></i> 계약서 추가
																	</button>
																</div>
															</div>
														</div>
													</div>
												</form>
												<!-- END FORM-->
											</div>
											<!-- 기본 정보 탭 종료 -->
										</div>
									</div>
								</div>
							</div>
						</div><!-- END PAGE CONTENT INNER -->
					</div>
				</div>
			</div><!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<script type="text/javascript">
		$('.form-actions.def-action-mirror').html(
			$('.form-actions.def-action').html()
		);
		
		//전자계약서 목록 가져오기
		var resJson = '${resJson}';
		var token = '${token}';
		
		var resParse = JSON.parse(resJson);
		var title = resParse.result;
		
		for (var i=0; i<title.length; i++){
			var form = document.getElementById("selectForm");
				form.innerHTML += "<option value='" + title[i].id + "' data-val='" + title[i].title + "'>" + title[i].title + "</option>";
		}
		
		var formId = "";
		var formName = "";
		
		function listChange(obj){
			formId = $("#selectForm option:selected").val();
			formName = $("#selectForm option:selected").text();

			console.log("authId : " + $("#authId").val('${DATAMAPFORM.authId}'));
			
			$("#authId").val('${DATAMAPFORM.authId}');
			$("#form_id").val(formId);
			$("#doc_name").val(formName);
			$("#distId").val('${DATAMAP.distId}');
			$("#distName").val('${DATAMAP.distName}');
		}
		
		//formId, formName DB 저장
		function formSave(){
			$.ajax({
					type:"POST",
					url:"/eform/auth/add",
					dataType:'text',
					data: $("#writeFrm").serializeObject(),
					success:function(data){
						alert("계약서가 등록되었습니다.");
						console.log("formSave Success");
					},
					error:function(){
						console.log("formSave Failed");
					},
				});
		}
		
		function formDelete(){
			var deleteId = $('#authId').val();
			$.ajax({
				type:"DELETE",
				url:"/eform/auth/delete",
				dataType:'text',
				data: deleteId,
				success:function(data){
					alert("계약서가 삭제되었습니다.");
					console.log("formDelete Success");
				},
				error:function(){
					console.log("formDelete Failed");
				},
			});
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>