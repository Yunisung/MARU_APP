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
									<li><span>Tax 추가</span></li>
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
												<span class="caption-title"> ${DATAMAP.name} Tax 추가 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/tax/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<input type="hidden" name="mchtId" value="${DATAMAP.mchtId}"/> 
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">Tax 아이디</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm taxId" name="taxId" placeholder="" value="${DATAMAP.taxId}" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm name" maxlength="100" name="name" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">Tax 한도 (0은 무제한)</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm taxLimit" maxlength="10" name="taxLimit" placeholder="" value="0">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">회사 이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm compName" maxlength="100" name="compName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">대표자 이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoName" maxlength="100" name="ceoName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label idType">사업자(주민)번호</label>
														<div class="col-md-6">
															<div class="input-group input-group-sm">
																<div class="input-group-btn">
																	<button type="button" class="btn green dropdown-toggle" data-toggle="dropdown">
																		<span>사업자번호</span>
																		<i class="fa fa-angle-down"></i>
																	</button>
																	<ul class="dropdown-menu pull-right" data-target='idType'>
																		<li><a href="javascript:;">사업자번호</a></li>
																		<li><a href="javascript:;">주민번호</a></li>
																	</ul>
																</div>
																<input type="text" class="form-control input-sm numberOnly" maxlength="14" name="identity" placeholder="" value="">
																<input type="hidden" class="idType" name="idType" value="사업자번호" />
																<!-- /btn-group -->
															</div>
															<!-- /input-group -->
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이메일</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm email" maxlength="100" name="email" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">사용 상태</label>
														<select name="taxStatus" class="selectpicker col-sm-6 col-xs-12">
															<option value="예정">예정</option>
															<!-- <option value="사용">사용</option> -->
															<!-- <option value="만료">만료</option> -->
														</select>
													</div>
													<!-- 주소 영역           다음 플러그인 -->
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">우편번호</label>
														<div class="col-md-6">
															<div class="input-group input-group-sm">
																<span class="input-group-btn">
																	<button class="btn green btn-addr" type="button">주소 찾기</button>
																</span>
																<input type="text" class="form-control input-sm zip" maxlength="6" name="zip" placeholder="" readonly value="">
															</div>
															<!-- /input-group -->
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">주소</label>
														<div class="col-sm-8">
															<input type="text" class="form-control input-sm addr1" maxlength="50" name="addr1" placeholder="" readonly value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">상세주소</label>
														<div class="col-sm-8">
															<input type="text" class="form-control input-sm addr2" maxlength="50" name="addr2" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 은행 정보 입력</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">은행이름</label>
														<select name="bankCd" class="selectpicker col-sm-6 bankCd">
															<option value="">은행 선택</option>
															<c:forEach var="entryMap" items="${BANK_OPTION}">
																<option value="${entryMap['code']}">${entryMap['codeName']}</option>
															</c:forEach>
														</select>
														<input type="hidden" name="bankName" value="">
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">계좌번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoPhone account" maxlength="20" name="account" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">예금주</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoTel accntHolder" maxlength="30" name="accntHolder" placeholder="" value="">
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
							</div>
				</div>
			</div>
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=525bdd42a1b50641247881ae3f07e6cb&libraries=services"></script>
	<c:import url="/include/javascript.jsp" />
	<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		
		//은행 선택 셀렉트박스 크기 조절
		$(document).ready(function() {
			$('.dropdown-toggle').click(function() {
				$('.dropdown-menu').css('max-height', '400px');
			});
			
		});
		
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				name : {
					minlength : 2,
					required : true,
					remote : {
						url : "/mcht/tax/nameCheck", //make sure to return true or false with a 200 status code
						type : "post",
						data : {
							id : function() {
								return form1.find('input[name="name"]').val();
							}
						}
					}
				},
				taxLimit : {
					required : true,
					number: true
				},
				bankCd : {
					required : true
				},
				bankName : {
					required: true
				},
				account : {
					required:true
				},
				accntHolder: {
					required:true
				},
				email: {
					required:true,
					email:true
				},
				identity: {
					required:true
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 가맹점 Tax 정보를 생성하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_tax'); //PAGE 이동		
					}
				});
			}
		});
		
		$('#writeFrm .btn-addr').click(function() {
			var frm = 	$('#writeFrm');
            postCode($(this), frm.find('input[name="zip"]'), frm.find('input[name="addr1"]'), frm.find('input[name="addr2"]') );
	    });

   		$('.taxLimit').val(addComma(String($('.taxLimit').val()).replace(/[^0-9]/g,"")));
		
   		$('.taxLimit').keyup(function(){
			$('.taxLimit').val(addComma(String($('.taxLimit').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
   		
   		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		$(".loading-btn").click(function(){
			$(".taxLimit").val($(".taxLimit").val().replace(/,/g, ''));
		});

		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>