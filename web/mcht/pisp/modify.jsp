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
								<li><span>지급이체대행 정보 수정</span></li>
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
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-reorder"></i>
										<span class="caption-title"> ${MCHT_MAP.name} 지급이체대행 정보 수정 </span>
									</div>
								</div>  
								<div class="portlet-body form">
									<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/pisp/"
									 method="post">
										<input type="hidden" name="action_type" value="update" data-reg="false" />
										<div class="form-body row">
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">가맹점아이디</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" name="mchtId" data-key="true" value="${MCHT_MAP.mchtId}"
													 readonly>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">상태</label>
												<select name="status" class="selectpicker col-sm-6">
													<option value="중지" selected>중지</option>
													<option value="사용">사용</option>
												</select>
												<script type="text/javascript"> 
													document.forms.writeFrm.status.value = '${DATAMAP.status}'
												</script> 
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">서비스개시일자</label>
												<div class="col-sm-6">
													<input type="number" class="form-control input-sm" name="startDay" value="${DATAMAP.startDay}" placeholder="20190624" readonly>
												</div>
											</div>
											<div class="form-group col-sm-6"> 
												<label class="control-label col-sm-4 req-label">정산주기</label>
												<select name="settleType" class="selectpicker col-sm-6">
													<option value="M+1">M+1</option>
													<option value="M+2">M+2</option>
													<option value="M+3">M+3</option>
													<option value="M+4">M+4</option>
													<option value="M+5">M+5</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">이체수수료</label>
												<div class="col-sm-6">
													<input type="number" class="form-control input-sm netFee" maxlength="20" name="netFee" placeholder="" value="${DATAMAP.netFee}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">입금수수료</label>
												<div class="col-sm-6">
													<input type="number" class="form-control input-sm vactFee" maxlength="100" name="vactFee" value="${DATAMAP.vactFee}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">계좌인증수수료</label>
												<div class="col-sm-6">
													<input type="number" class="form-control input-sm fcsFee" maxlength="100" name="fcsFee" value="${DATAMAP.fcsFee}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">지급이체접속KEY</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm authKey" name="authKey"  value="${DATAMAP.authKey}" readonly>
												</div>
											</div> 
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">입금은행</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" name="bankCd"  value="020" readonly>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">입금가상계좌</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm account" maxlength="100" name="account" value="${DATAMAP.account}" readonly="readonly">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">입금계좌발급번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm issueId" maxlength="100" name="issueId" value="${DATAMAP.issueId}" readonly="readonly">
												</div>
											</div> 
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">거래전달 프로토콜</label>
												<select name="hookType" class="selectpicker col-sm-6">
													<option value="HTTPS" selected>HTTPS</option>
													<option value="HTTP">HTTP</option>
													<option value="TCP">TCP</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.hookType.value = '${DATAMAP.hookType}'
												</script>
											</div> 
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">거래전달 주소(URL)</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" maxlength="100" name="hookAddr" placeholder="api.example.com"
													 value="${DATAMAP.hookAddr}">
												</div>
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
						<!-- END PAGE CONTENT INNER -->
					</div>
				</div>
			</div>
			<!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules: {
				netFee :{
					required : true
				},
				vactFee : {
					required : true
				},
				fcsFee : {
					required : true
				},
				authKey : {
					required : true,
					maxlength : 16
				},
				bankCd : { 
					required : true,
					maxlength : 3
				},
				account : {
					required : true,
					maxlength : 15
				},
				issueId : {
					required : true,
					maxlength : 14
				},
				hookType : {
					required : true,
					maxlength : 10
				},
				hookAddr : {
					maxlength : 100
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
				bootbox.confirm("입력하신 정보로 ${MCHT_MAP.name} 지급이체대행 정보를 수정 하시겠습니까?", function (result) {
					if (result) { 
						ajaxFormSubmit(form, '/mcht/view/${MCHT_MAP.mchtId}/tab_pisp'); //PAGE 이동		
					}
				});
			}
		});

		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>