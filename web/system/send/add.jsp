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
<style type="text/css">
form {
	margin: 0 auto; 
	width : 780px;
}
textarea {
	width : 810px;
	height : 400px;
	resize:none;
}
.selectOpt{
	top: -3px;
	border-radius: 3px;
	line-height: 1.44;
	border-color: #c2cad8;
	padding-right: 25px;
	z-index: 1;
	position: relative;
	float: left;
	outline: 0!important;
	color: #333;
	background-color: #fff;
	display: inline-block;
	margin-bottom: 0;
	font-weight: 400;
	text-align: center;
	vertical-align: middle;
	touch-action: manipulation;
	cursor: pointer;
	white-space: nowrap;
	padding: 6px 12px;
	font-size: 12px;
	height: 30px;
	font-family: 'Nanum Gothic', sans-serif;
	overflow: visible;
}
</style>
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
                 				<li><span>공지사항 전송</span></li>
							</ul>
							<div class="page-toolbar">
								<div class="btn-group btn-theme-panel">
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
								<span class="caption-title"> 공지사항 전송 </span>
							</div>
						</div>
						<div class="portlet-body form">
							<form class="form-horizontal" role="form" data-form="true" id="writeFrm" name="form" action="/system/send/" method="post" onsubmit="return false;">
								<input type="hidden" name="action_type" value="sendForm" data-reg="false" />
								<div class="form-body row">
								<div class="form-group col-sm-12">
									<input type="radio" name="selectSend" value="partSend" checked>
										<label for="partSend">개별전송</label>
									<input type="radio" name="selectSend" value="allSend">
										<label for="allSend">전체전송</label>
									<input type="radio" name="selectSend" value="inputSend">
										<label for="inputSend">입력전송</label>
								</div>
								<div id="option"></div>
								<div id="partKind">
                                    <c:import url="/common/selectGrade.jsp" />
								</div>
								<div id="allKind">
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">전체 발송</label>
										<select name="all" id="all" class="col-sm-6 selectOpt">
											<option>-- 선택 --</option>
											<option value="distAll">대행사</option>
											<option value="agencyAll">에이전시</option>
											<option value="salesAll">지사</option>
											<option value="mchtAll">가맹점</option>
										</select>
									</div>
								</div>
								<div id="inputKind">
									<div class="form-group col-sm-6">
					                    <label class="control-label input-sm col-sm-4 req-label">휴대폰 번호</label>
					                    <div class="col-sm-6">
					                        <input type="text" class="form-control input-sm" id="phone" name="phone" maxlength="20">
					                    </div>
						            </div>
						            <div class="form-group col-sm-6">
					                    <label class="control-label input-sm col-sm-4 req-label">이메일 주소</label>
					                    <div class="col-sm-6">
					                        <input type="text" class="form-control input-sm" id="email" name="email" maxlength="30">
					                    </div>
						            </div>
								</div>
								<div class="form-group">
									<div class="col-sm-12">
										<input type="text" name="title" class="form-control input-sm" value="[(주)건흥페이먼츠]공지사항 안내" readOnly>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-12">
										<textarea id="summary" name="summary" placeholder="내용을 입력해주세요." required></textarea>
									</div>
								</div>
						            <input type="hidden" name="grade" value="" />
									<input type="hidden" class="ptid" name="parentId" value="" />
									<input type="hidden" name="parentName" data-reg="false" value="" />
								</div>
								<div class="alert alert-danger display-hide"></div>
								<div class="form-actions right">
									<div class="">
										<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
											<i class="fa fa-search"></i>&nbsp;전송
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
	
	<c:import url="/include/javascript.jsp" />
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
	var copyPart;
	var copyAll;
	var copyInput;
	
	var part = $("div#partKind");
	var all = $("div#allKind");
	var input = $("div#inputKind");
	
	copyPart = part.detach();
	copyAll = all.detach();
	copyInput = input.detach();
	$("#option").append(copyPart);
	
	var radioValue = "";
	$("input[name=selectSend]:radio").change(function(){
		radioValue = this.value;
		if(radioValue == "partSend"){
			$("#option").append(copyPart);
			if(part.length){
				copyAll = all.detach();
				copyInput = input.detach();
			}
			
		}else if(radioValue == "allSend"){
			$("#option").append(copyAll);
			if(all.length){
				copyPart = part.detach();
				copyInput = input.detach();
			}
			
		}else if(radioValue == "inputSend"){
			$("#option").append(copyInput);
			if(input.length){
				copyPart = part.detach();
				copyAll = all.detach();
			}
		}
	});
		
	var form1 = $('#writeFrm');
	var error1 = $('.alert-danger', form1);
	form1.validate({
		rules : {
			selectSend : {
				required : true
			},
			all : {
				required : true
			},
			summary : {
                   required : true
               },
            partKind : {
            	required : true
            },
            phone : {
            	 required: function(){
                     if($('#email').val() == ''){
                         return true;
                     }
                     return false;
                 }
            },
            email : {
           	 required: function(){
                    if($('#phone').val() == ''){
                        return true;
                    }
                    return false;
             	}
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
			bootbox.confirm("공지사항을 전송하시겠습니까?", function(result) {
				if (result) {
					$(form).find('.form-body').append('<input type="hidden" name="summary" value="summary">');
					
					$("#summary-result").html($('#summary').val());
					console.log($("#summary-result"));
					ajaxFormSubmit(form, '/system/send/add'); //PAGE 이동
				}
			});
		}
	});
	
	gradeSelector('writeFrm', '${CP_SESSION.grade}');
	$('#nav-system').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
	<div id="summary-result" class="hide"></div>
</body>
</html>