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
<link href="/assets/global/plugins/bootstrap-summernote/summernote.css" rel="stylesheet" type="text/css" />
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
									<li><span>시스템 관리</span><i class="fa fa-circle"></i></li>
                  <li><span>FAQ 등록</span></li>
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
												<i class="fa fa-reorder"></i>
												<span class="caption-title"> FAQ 등록 </span>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal" role="form" data-form="true" id="writeFrm" name="form" action="/system/faq/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group">
														<div class="col-sm-12">
															<input type="text" class="form-control input-sm name" maxlength="100" name="title" placeholder="제목을 입력하세요." value="테스트">
														</div>
													</div>
													<div class="form-group">
														<div class="col-sm-12">
															<div id="summernote" data-name=summary></div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">개시일자</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm datepicker now-date pubDay" maxlength="10" name="pubDay" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">만료일자</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm datepicker closeDay" maxlength="10" name="closeDay" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">개시여부</label>
														<select name="status" class="selectpicker col-sm-6">
															<option value="개시">개시</option>
															<option value="미개시">미개시</option>
															<!-- <option value="만료">사용</option> -->
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">분류</label>
														<select name="category" class="selectpicker col-sm-6">
															<option value="일반">일반</option>
															<option value="문의">문의</option>
														</select>
													</div>
												</div>
												<div class="alert alert-danger display-hide"></div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
															<i class="fa fa-search"></i>&nbsp;Submit
														</button>
													</div>
												</div>
											</form>
										</div>
										<!-- END ADD FORM TABLE-->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
		<c:import url="/include/footer.jsp" />
	</div>
	
	<c:import url="/include/javascript2.jsp" />
	<script src="/assets/global/plugins/bootstrap-summernote/summernote.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/bootstrap-summernote/lang/summernote-ko-KR.min.js" type="text/javascript"></script>
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		$(document).ready(function() {
    		$('#summernote').summernote({
    			height: 400,                 // set editor height
    			minHeight: null,             // set minimum height of editor
    			maxHeight: null,
    			toolbar: [
    		          // [groupName, [list of button]]
    		          ['style', ['bold', 'italic', 'underline', 'clear']],
    		          ['font', ['strikethrough', 'superscript', 'subscript']],
    		          ['fontsize', ['fontsize']],
    		          ['color', ['color']],
    		          ['para', ['ul', 'ol', 'paragraph']],
    		          ['height', ['height']]
    		        ]
    		});
    	});
			
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				title : {
					minlength : 5,
					required : true
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
                
				bootbox.confirm("FAQ를 등록하시겠습니까?", function(result) {
					if (result) {
						$(form).find('.form-body').append('<input type="hidden" name="summary" value="summary">');
						
						$("#summary-result").html($('#summernote').summernote('code'));
						console.log($("#summary-result"));
						ajaxFormSubmit(form, '/system/faq/form'); //PAGE 이동		
					}
				});
			}
		});
		
		
		$('#nav-system').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
	<div id="summary-result" class="hide"></div>
</body>

</html>