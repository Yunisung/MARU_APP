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
									<li><span>TMS</span><i class="fa fa-circle"></i></li>
                  <li><span>APP 등록</span></li>
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
													<li class="active"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> </a></li>
													<!-- <li class=""><a href="#tab_additional" data-toggle="tab" aria-expanded="false"> 관리자 </a></li> -->
												</ul>
												<div class="tab-content">
													<!-- 기본 정보 탭 시작 -->
													<div class="tab-pane active" id="tab_basic">
														<h2>Upload File</h2>
														<div class="button-center" style="margin: 10px 0px;">
															<form id="fileAttribute">
																<input type="hidden" name="version" value="${version}" />
																<input type="hidden" name="redirectUrl" value="/tms/app/form.jsp" />
															</form>
															<span class="btn btn-sm btn-file mif-plus fileinput-button" style="position: relative; overflow: hidden; display: inline-block; color: #fff; background-color: #5CB85C; float: left; margin-right: 3px;">
																<i class="glyphicon glyphicon-plus"></i>
																<span>Add files...</span>
																<input id="fileupload" type="file" name="files[]" data-url="/uploadFile/apk" multiple style="position: absolute; top: 0; right: 0; margin: 0; opacity: 0; -ms-filter: 'alpha(opacity=0)'; font-size: 200px; direction: ltr; cursor: pointer; display: block;">
															</span>
															<button type="button" id="uploadStart" class="btn btn-sm btn-file mif-file-upload" style="color: #fff; background-color: #428BCA;">
																<i class="glyphicon glyphicon-upload"></i>
																<span>Start upload</span>
															</button>
															<button type="button" id="clearFile" class="btn btn-sm btn-file mif-bin" style="color: #fff; background-color: #D9534F;">
																<i class="glyphicon glyphicon-trash"></i>
																<span>Delete</span>
															</button>
															<span id="progress_wrap">
																<div id="progressbar" style="width: 300px;"></div>
															</span>
														</div>
														<table class="pg-table table table-striped table-hover flip-content" summary="data table" id="uploaded-files">
															<colgroup>
																<col style="width: 70%;" />
																<col style="width: *;" />
															</colgroup>
															<thead>
																<tr>
																	<th>File Name</th>
																	<th>File Size</th>
																</tr>
															</thead>
															<tbody>
															</tbody>
														</table>
														<div id="dropzone" style="border: 1px dashed #8f9498; border-radius: 5px; background: white; overflow: hidden; margin: 10px 0px;">
															<h2 style="position: relative; text-align: center; margin: 20px 0px;font-size: 25px;">업로드할 파일을 끌어다 놓으세요.</h2>
														</div>
													</div>
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
	<script src="/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/jquery-file-upload/js/jquery.fileupload.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/jquery-file-upload/js/jquery.fileupload-ui.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/jquery-file-upload/js/jquery.fileupload-jquery-ui.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
	<script type="text/javascript">
	// 파일 업로드
		var dataArray = [];
		var maxFileSize = 51101880;
		
		$('#fileupload').fileupload({
	        dataType: 'json',
	        type: "POST",
	        add: function (e, data) {
	        	data.formData = $('#fileAttribute').serializeArray();
	        	$.each(data.files, function (index, file) {
	        		if(file.size < maxFileSize){
	        			dataArray[dataArray.length] = data;
	        			$('#uploaded-files > tbody:last').append('<tr><td>'+file.name+'</td><td>' + Math.round(file.size/1024) + ' KB</td></tr>');
	        		}else{
	        			alert("파일 사이즈가 50MB를 초과하였습니다.");
	        			return;
	        		}
	        	});
	        },
	        done: function (e, data) {
	        	$("#progressbar").hide();
	        	$("#uploaded-files > tbody").find("tr:has(td)").remove();
	        	$(".active > .tab-element").trigger("click");
	        },
	        progressall: function (e, data) {
	        	$("#progressbar").show();
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            if(progress >= 100){
	            	 $( "#progressbar" ).progressbar({
	            		 value: false
	 	            });
	            }else{
	            	 $( "#progressbar" ).progressbar({
	 	                value: progress
	 	            });
	            }
	        },
	        submit: function (e, data) {
	        	bootbox.alert("파일 업로드에 성공했습니다.", function() {
	        		window.location.href = $('#fileAttribute').find('input[name="redirectUrl"]').val();
	        	});
	        },
	        fail: function (e, data) {
	        	bootbox.alert("파일 업로드에 실패했습니다.");
	        },
	        dropZone: $('#dropzone')
	    });
		
		// 임시 등록된 파일을 업로드 한다.
		$("#tab_basic").on("click", "#uploadStart", function(){
			for (i = 0; i < dataArray.length; i++) {
		    	var data = dataArray[i];
		    	console.log(data);
		    	data.submit();
			}
			dataArray = [];
		});
		// 임시 등록된 파일을 초기화 한다.
		$("#tab_basic").on("click", "#clearFile", function(){
			dataArray = []; 
			$("#progressbar").hide();
        	$("#uploaded-files > tbody").find("tr:has(td)").remove();
		});
		$('#nav-tms').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>