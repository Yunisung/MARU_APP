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
									<li><span>터미널 추가 정보 등록</span></li>
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
												<span class="caption-title"> ${DATAMAP.name} 터미널 추가 정보 등록 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/tmnDtl/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
														<a href="javascript:dufilicationMchtData();" class="btn btn-sm pull-right"><i class="fa fa-check"></i> 가맹점 기본정보와 동일</a>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">터미널 아이디</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm tmnId" name="tmnId" placeholder="터미널 ID를 입력하세요." value="${DATAMAP.tmnId}" readonly >
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">상호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm name" maxlength="50" name="name" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">수수료</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm rate2" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																<input type="hidden" class="form-control rate" maxlength="7" name="rate" placeholder="" value="0.00000">
																<span class="input-group-addon"> % (VAT 별도)</span>
                                                    		</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">대표자명</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoName" maxlength="50" name="ceoName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">사업자/주민번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm identity numberOnly" maxlength="13" name="identity" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">휴대폰번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoPhone" maxlength="20" name="ceoPhone" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">사업장연락처</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm tel" maxlength="20" name="tel" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이메일주소</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm email" maxlength="128" name="email" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 은행 정보 입력</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">은행이름</label>
														<select name="bankCd" class="selectpicker col-sm-6 bankCd" >
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
															<input type="text" class="form-control input-sm account" maxlength="20" name="account" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">예금주</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm accntHolder" maxlength="30" name="accntHolder" placeholder="" value="">
														</div>
													</div>
													
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 주소 정보 입력</label>
													</div>
													<!-- 주소 영역           다음 플러그인 -->
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">우편번호</label>
														<div class="col-md-6">
															<div class="input-group input-group-sm">
																<span class="input-group-btn">
																	<button class="btn green btn-addr" type="button">주소 찾기</button>
																</span>
																<input type="text" class="form-control input-sm " maxlength="6" name="zip" placeholder="" readonly value="">
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
													<!-- 영수증 정보 입력 -->
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 영수증 정보 입력</label>
														<a href="javascript:dufilicationRctData();" class="btn btn-sm pull-right"><i class="fa fa-check"></i> 기본정보와 동일</a>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">사업자번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm rctIdentity" maxlength="20" name="rctIdentity" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대표자명</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm rctCeoName" maxlength="50" name="rctCeoName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">상호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm rctName" maxlength="50" name="rctName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">연락처</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm rctTelNo"  maxlength="20" name="rctTelNo" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">주소</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm telNo" name="rctAddr" placeholder="" value="">
														</div>
													</div>
													
													<!-- 서비스 사 정보 입력 -->
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 서비스 사 정보 입력</label>  
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">타입</label>
														<select name="svcType" class="selectpicker col-sm-6 svcType">
															<option value="개인">개인</option>
															<option value="사업자">사업자</option>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">서비스 사</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm svcName" maxlength="50" name="svcName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">사업자번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm svcIdentity" maxlength="12" name="svcIdentity" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">대표자명</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm svcCeo"  maxlength="50" name="svcCeo" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">주소</label> 
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm svcAddr" name="svcAddr" placeholder="" value="">
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
	<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script><!-- BEGIN FORM JAVASCRIPT -->
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
					required : true
				},
				ceoName : {
                    required : true,
                    minlength : 2
                },
                identity : {
                	required : true,
                	minlength : 10,
                	maxlength: 13
                },
                rate2 : {
					required: true
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
				tel: {
					required:true,
					minlength : 8
				},				
				svcType: {
					required:true
				},
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 가맹점 터미널을 생성하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/tmn/form.jsp'); //PAGE 이동		
					}
				});
			}
		});
		function dufilicationMchtData() {
			$('input[name="name"]').val('${MCHTMAP.name}');
			$('input[name="ceoName"]').val('${MCHTMAP.ceoName}');
			$('input[name="identity"]').val('${MCHTMAP.decIdentity}');
			$('input[name="ceoPhone"]').val('${MCHTMAP.tel2}');
			$('input[name="tel"]').val('${MCHTMAP.tel1}');
			$('input[name="email"]').val('${TAXMAP.email}');
			$('select[name="bankCd"]').val('${TAXMAP.bankCd}');
			$('.selectpicker').selectpicker('refresh');
			
			var bankId = $('select[name="bankCd"]').val();
			var bankName = $('select[name="bankCd"]').find('option[value="'+bankId+'"]').text();
			bankName = bankName == '은행 선택' ? '' : bankName;
			$('input[name="bankName"]').val(bankName);
			
			$('input[name="account"]').val('${TAXMAP.account}');
			$('input[name="accntHolder"]').val('${TAXMAP.accntHolder}');
			$('input[name="zip"]').val('${MCHTMAP.zip}');
			$('input[name="addr1"]').val('${MCHTMAP.addr1}');
			$('input[name="addr2"]').val('${MCHTMAP.addr2}');
        }
		function dufilicationRctData() {
            var nameVal = $('input[name="name"]').val();
            var ceoName = $('input[name="ceoName"]').val();
            var addr1 = $('input[name="addr1"]').val();
            var addr2 = $('input[name="addr2"]').val();
            var tel = $('input[name="tel"]').val();

            if (nameVal) $('input[name="rctName"]').val(nameVal);
            if (ceoName) $('input[name="rctCeoName"]').val(ceoName);
            if (addr1) $('input[name="rctAddr"]').val(addr1+" "+addr2);
            if (tel) $('input[name="rctTelNo"]').val(tel);
        }
		$('#writeFrm .btn-addr').click(function() {
			var frm = 	$('#writeFrm');
            postCode($(this), frm.find('input[name="zip"]'), frm.find('input[name="addr1"]'), frm.find('input[name="addr2"]'));
	    });
		
		$(".loading-btn").click(function(){
			$('.rate').val(($('.rate2').val()/100).toFixed(5));
		});
		
		$('#nav-mcht').addClass('active');
		
		
		$('input[name=rctIdentity]').on("propertychange change paste input", function() {
			const regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g;
		    if(regExp.test($(this).val())){
				alert('특수문자 입력이 불가능합니다.');
			    var str = $(this).val().replace(regExp,"").trim();
			    $(this).val(str);
			}
		});

		
		 
		
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>