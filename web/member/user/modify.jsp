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
                  <li><span>사용자 정보 수정</span></li>
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
												<i class="fa fa-reorder"></i> 사용자 정보 변경
											</div>
											<div class="actions">
												<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사' }">
													<a href="/member/user/change/${DATAMAP.id}" class="btn btn-circle btn-default">
														<i class="fa fa-pencil"></i> 소속 변경
													</a>
												</c:if>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/user/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">아이디
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="50" name="id" data-key="true" value="${DATAMAP.id}" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">연락처</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="13" name="phone" value="${DATAMAP.phone}" placeholder="ex) 010-1234-0000">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">상태</label>
														<select name="status" class="selectpicker col-sm-6">
															<option value="예비">예비</option>
															<option value="사용" selected>사용</option>
															<option value="중지">중지</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.status.value = '${DATAMAP.status}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">이름
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="50" name="name" value="${DATAMAP.name }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">권한</label>
														<select name="role" class="selectpicker col-sm-6">
															<option value="일반" selected>일반</option>
															<option value="관리자">관리자</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.role.value = '${DATAMAP.role}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">기타거래 조회 권한</label>
														<select name="showOthTrns" class="selectpicker col-sm-6">
															<option value="N" selected>미사용</option>
															<option value="Y">사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.showOthTrns.value = '${DATAMAP.showOthTrns}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">전자계약서 사용 권한</label>
														<select name="eformStatus" class="selectpicker col-sm-6">
															<option disabled>${DATAMAP.eformStatus}</option>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대출정산 보기 권한</label>
														<select name="loanSettleStatus" class="selectpicker col-sm-6">
															<option value="N" selected>미사용</option>
															<option value="Y">사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.loanSettleStatus.value = '${DATAMAP.loanSettleStatus}' </script>
													</div>
													<div class="form-group col-sm-12 pg-form-group-12">
														<label class="control-label col-sm-2 req-label">코멘트</label>
														<div class="col-sm-9">
															<textarea rows="4" cols="50" class="form-control input-sm" maxlength="500" name="summary"></textarea>
														</div>
													</div>
												</div>
												<div class="alert alert-danger display-hide">
													<button class="close" data-close="alert"></button>
													등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.
												</div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn btn-sm green loading-btn" data-loading-text="Loading...">
															<i class="fa fa-search"></i>&nbsp;Submit
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- END ADD FORM TABLE-->

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
		form1.validate({
			rules : {
				phone : {
					minlength : 9,
					required : true
				},
				name : {
					minlength : 3,
					required : true
				}
			},
			submitHandler : function(form) {
				ajaxFormSubmit(form1, '/member/user/view/' + form1.find('input[name="id"]').val()); //PAGE 이동		
			}
		});
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>