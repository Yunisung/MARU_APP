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
                  <li><span>멤버관리</span><i class="fa fa-circle"></i></li>
                  <li><span>사용자 등록</span><i class="fa fa-circle"></i></li>
									<li><span>
                    <c:if test="${not empty isAdmin}">임직원 등록</c:if>
										<c:if test="${empty isAdmin}">하위사용자 등록</c:if>
										<c:if test="${empty isMcht}">가맹점사용자 등록</c:if>
                  </span></li>
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
												<span class="caption-title">
													<c:if test="${not empty isAdmin}">임직원 등록</c:if>
													<c:if test="${empty isAdmin}">하위사용자 등록</c:if>
													<c:if test="${empty isMcht}">가맹점사용자 등록</c:if>
												</span>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/user/" method="post">
												<div class="form-body row">
													<input type="hidden" name="action_type" value="insert" data-reg="false" />
													<div class="form-body row">
														<c:if test="${empty isAdmin && empty isMcht}">
															<c:import url="/common/selectGrade.jsp" />
														</c:if>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">아이디
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="50" name="id" placeholder="아이디를 입력해주세요." value="<c:if test="${not empty isMcht}">${DATAMAP.mchtId }</c:if>">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4 req-label">이름
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="50" name="name" value="<c:if test="${not empty isMcht}">${DATAMAP.name }</c:if>">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4 req-label">상태</label>
															<select name="status" class="selectpicker col-sm-6">
																<option value="예비">예비</option>
																<option value="사용" selected>사용</option>
																<option value="중지">중지</option>
															</select>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">연락처</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm phone" maxlength="13" name="phone" placeholder="ex) 010-1234-0000" value="<c:if test="${not empty isMcht}">${DATAMAP.tel2 }</c:if>">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">권한</label>
															<select name="role" class="selectpicker col-sm-6">
																<option value="일반" selected>일반</option>
																	<option value="관리자">관리자</option>
															</select>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">기타거래 조회 권한</label>
															<select name="showOthTrns" class="selectpicker col-sm-6">
																<option value="N" selected>미사용</option>
																	<option value="Y">사용</option>
															</select>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">전자계약서 사용 권한</label>
															<select name="" class="selectpicker col-sm-6">
																<option value="N" selected>미사용</option>
																<option value="Y">사용</option>
															</select>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대출정산 조회 권한</label>
															<select name="loanSettleStatus" class="selectpicker col-sm-6">
																<option value="N" selected>미사용</option>
																	<option value="Y">사용</option>
															</select>
														</div>
														<input type="hidden" name="grade" value="<c:if test="${not empty isAdmin}">본사</c:if><c:if test="${not empty isMcht}">가맹점</c:if>" />
														<c:if test="${empty isAdmin}">
															<input type="hidden" class="ptid" name="parentId" value="<c:if test="${not empty isMcht}">${DATAMAP.mchtId }</c:if>" />
															<input type="hidden" name="parentName" data-reg="false" value="<c:if test="${not empty isMcht}">${DATAMAP.name }</c:if>" />
														</c:if>
													</div>
													<div class="alert alert-danger display-hide"></div>
													<div class="form-actions right">
														<div class="">
															<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
																<i class="fa fa-search"></i>&nbsp;Submit
															</button>
														</div>
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
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				id : {
					minlength : 5,
					userId : true,
					required : true,
					remote : {
						url : "/member/user/idCheck", //make sure to return true or false with a 200 status code
						type : "post",
						data : {
							id : function() {
								return form1.find('input[name="id"]').val();
							}
						}
					}
				},
				phone : {
					required : true,
					minlength : 9
				},
				name : {
					minlength : 3,
					required : true
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>';
                if($('#gradeSelector').attr("data-last-selected") != "true") {
                	error1Str += "소속을 설정하지 않았습니다.";
                }else {
                	error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
                }
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
				var gradeText = "소속 : " + form1.find('input[name="grade"]').val() + (form1.find('input[name="grade"]').val() == '본사' ? "" : " [ "+ form1.find('input[name="parentName"]').val()+ "]");
				$('.confirm_grade').text(gradeText);
				$('.confirm_id').text("아이디 : " + form1.find('input[name="id"]').val());

				bootbox.confirm($('#bootbox_confirm').html(), function(result) {
					if (result) {
						ajaxFormSubmit(form, '/member/user/view/'+ form1.find('input[name="id"]').val()); //PAGE 이동		
					}
				});
			}
		});

		<c:if test="${empty isAdmin && empty isMcht}">
			gradeSelector('writeFrm', '${CP_SESSION.grade}');
			$.validator.addClassRules({ ptid : { required : true } });
		</c:if>
		$('#nav-member').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
	<!-- 확인창 생성을 위한 베이스 -->
	<div style="display: none;">
		<div class="panel panel-warning" id="bootbox_confirm">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<h3 class="panel-title">입력정보 확인</h3>
			</div>
			<div class="panel-body">
				<p>다음과 같은 정보로 사용자를 생성하려고 합니다.</p>
				<p>계속 진행하시겠습니까?</p>
			</div>
			<!-- List group -->
			<ul class="list-group">
				<li class="list-group-item confirm_id"></li>
				<li class="list-group-item confirm_grade"></li>
			</ul>
		</div>
	</div>
</body>

</html>