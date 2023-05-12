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
									<li><span>가상계좌 관리</span><i class="fa fa-circle"></i></li>
									<li><span>출금계좌 블랙리스트 등록</span><i class="fa fa-circle"></i></li>
									<li><span>출금계좌 블랙리스트 등록</li>
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
													출금계좌 블랙리스트 등록
												</span>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/vact/reg/blackList/" method="post">
												<input type="hidden" name="action_type" value="add" data-reg="false" />
												<div class="form-body row">
													<div class="form-body row">
														<div class="form-group pg-form-group">
															<label class="control-label input-sm col-sm-4 req-label">출금은행</label>
															<select class="selectpicker col-lg-8" name="bankCd" id="bankCd" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="004">국민은행</option>
																<option value="088">신한은행</option>
																<option value="081">KEB하나은행</option>
																<option value="003">IBK기업은행</option>
																<option value="020">우리은행</option>
																<option value="090">카카오뱅크</option>
																<option value="089">케이뱅크</option>
																<option value="011">농협(중앙회)</option>
																<option value="012">농협(지역)</option>
																<option value="048">신협</option>
																<option value="105">웰컴저축은행</option>
																<option value="007">수협은행</option>
																<option value="071">우체국</option>
																<option value="023">SC은행</option>
																<option value="027">한국씨티은행</option>
																<option value="045">새마을금고</option>
																<option value="039">경남은행</option>
																<option value="031">대구은행</option>
																<option value="032">부산은행</option>
																<option value="034">광주은행</option>
																<option value="035">제주은행</option>
																<option value="037">전북은행</option>
																<option value="002">한국산업은행</option>
																<option value="050">상호저축은행</option>
																<option value="051">기타외국은행</option>
																<option value="052">모건스탠리</option>
																<option value="054">홍콩상하이은행</option>
																<option value="055">도이치은행</option>
																<option value="056">에이비엔암로은행</option>
																<option value="058">미즈호코퍼레이트은행</option>
																<option value="059">도쿄미쓰비시은행</option>
																<option value="060">뱅크오브아메리카</option>
																<option value="064">산림조합</option>
																<option value="209">유안타증권</option>
																<option value="218">현대증권</option>
																<option value="230">미래에셋증권</option>
																<option value="238">대우증권</option>
																<option value="240">삼성증권</option>
																<option value="243">한국투자증권</option>
																<option value="247">우리투자증권</option>
																<option value="261">교보증권</option>
																<option value="262">하이투자증권</option>
																<option value="263">에이치엠씨투자증권</option>
																<option value="264">키움증권</option>
																<option value="265">이트레이드증권</option>
																<option value="266">에스케이증권</option>
																<option value="267">대신증권</option>
																<option value="268">솔로몬투자증권</option>
																<option value="269">한화증권</option>
																<option value="270">하나대투증권</option>
																<option value="278">굿모닝신한증권</option>
																<option value="279">동부증권</option>
																<option value="280">유진투자증권</option>
																<option value="287">메리츠증권</option>
																<option value="289">엔에이치투자증권</option>
																<option value="290">부국증권</option>
																<option value="291">신영증권</option>
																<option value="292">엘아이지투자증권</option>
																<option value="288">카카오페이증권</option>
															</select>
														</div>
														<div class="form-group col-sm-8">
															<label class="control-label input-sm col-sm-2 req-label">출금계좌번호
															</label>
															<div class="col-sm-8">
																<div class="col-sm-6">
																	<input type="text" class="form-control input-sm numberHypen" maxlength="14" id="account" name="account" value="">
																</div>
																<div class="col-sm-6">
																	<a class="btn btn-sm green link_modal" onclick="showModal('/vact/reg/blackList/searchAccountModal')">가상계좌번호로 조회</a>
																	<a class="btn btn-sm green link_modal" onclick="showModal('/vact/reg/blackList/searchAuthIdModal')">인증ID로 조회</a>
																</div>
															</div>
														</div>
													</div>
													<div class="form-body row">
														<div class="form-group col-sm-4">
															<label class="control-label input-sm col-sm-4 req-label">등록사유</label>
															<div class="col-sm-8">
																<textarea class="form-control" id="reason" name="reason" cols="100" rows="3" maxlength="100" placeholder="등록사유"></textarea>
															</div>
														</div>
													</div>
													<div class="alert alert-danger display-hide"></div>
													<div class="form-actions right">
														<div class="">
															<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
																<i class="fa fa-search"></i>&nbsp;등록
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
				account : {
					maxlength : 14,
					required : true
				},
				bankCd : {
					minlength : 1,
					required : true
				},
				reason : {
					maxlength : 100,
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
				bootbox.confirm("입력하신 출금정보계좌를 블랙리스트에 등록하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/vact/blackList/add.jsp'); //PAGE 이동	
					}
				});
			}
		});

		function showModal(url) {
			var $modal = $('#pgmate-modal');
			if ($modal.children().length < 1) {
				$modal.empty();
			}

			$modal.load(url, '', function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					$modal.modal();
					textMask();
				}
			});
		}
		
		$('#nav-trx').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>