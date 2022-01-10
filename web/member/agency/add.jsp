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
									<li><span>에이전시 등록</span></li>
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
												<span class="caption-title"> 에이전시 등록 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/agency/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<c:import url="/common/selectGrade.jsp" />
													<input class="distId" type="hidden" name="distId" value="<c:if test="${CP_SESSION.grade eq '대행사'}">${CP_SESSION.parentId}</c:if>"/>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">사업자 이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm name" maxlength="100" name="name" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">상태</label>
														<select name="status" class="selectpicker col-sm-6">
															<option value="예비" selected>예비</option>
															<option value="사용">사용</option>
															<option value="중지">중지</option>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">업태</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm bizCategory" maxlength="30" name="bizCategory" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">업종</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm bizType" maxlength="30" name="bizType" placeholder="" value="">
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
																<input type="text" class="form-control input-sm numberOnly" maxlength="13" name="identity" placeholder="" value="">
																<input type="hidden" class="idType" name="idType" value="사업자번호" />
																<!-- /btn-group -->
															</div>
															<!-- /input-group -->
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">대표이메일</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="100" name="email" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">전화번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm numberHypen" maxlength="20" name="tel1" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">전화번호2</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm numberHypen" maxlength="20" name="tel2" placeholder="" value="">
														</div>
													</div>

													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">사업장팩스번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm numberHypen" maxlength="20" name="fax" placeholder="" value="">
														</div>
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
													<input type="hidden" name="lat" value="" />
													<input type="hidden" name="lng" value="" />

													<!-- 대표자 영역 -->
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 대표자 정보 입력</label>
														<a href="javascript:dufilicationData();" class="btn btn-sm pull-right"><i class="fa fa-check"></i> 기본정보와 동일</a>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoName" maxlength="50" name="ceoName" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대표자 주민번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoIdentity numberOnly" maxlength="13" name="ceoIdentity" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">휴대폰</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoPhone numberHypen" maxlength="20" name="ceoPhone" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">전화번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoTel numberHypen" maxlength="20" name="ceoTel" placeholder="" value="">
														</div>
													</div>

													<!-- 주소 영역 (대표자)          다음 플러그인 -->
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">우편번호</label>
														<div class="col-md-6">
															<div class="input-group input-group-sm">
																<span class="input-group-btn">
																	<button class="btn green btn-addr2" type="button">주소 찾기</button>
																</span>
																<input type="text" class="form-control input-sm ceoZip" maxlength="6" name="ceoZip" placeholder="" readonly>
															</div>
															<!-- /input-group -->
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">주소</label>
														<div class="col-sm-8">
															<input type="text" class="form-control input-sm ceoAddr1" maxlength="50" name="ceoAddr1" placeholder="" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">상세주소</label>
														<div class="col-sm-8">
															<input type="text" class="form-control input-sm ceoAddr2" maxlength="50" name="ceoAddr2" placeholder="">
														</div>
													</div>
													<!-- 주소 영역 (대표자) 종료 -->

													<!-- 담당자 영역 -->
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 담당자 정보 입력</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm managerName" maxlength="50" name="managerName" placeholder="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">전화번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm managerPhone numberHypen" maxlength="20" name="managerPhone" placeholder="">
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
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				name : {
					minlength : 2,
					required : true
				},
				phone : {
					required : true,
					minlength : 8
				},
                idType : {
                    required : true
                },
                tel1 : {
                    required : true,
                    minlength : 8
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
                ceoIdentity : {
                	required : true,
                	minlength : 13,
                	maxlength: 13
                }
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
               	error1.html(error1Str);
				error1.show();
				console.log(event);
				console.log(validator);
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
				bootbox.confirm("입력하신 정보로 에이전시을 생성하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/member/agency/form'); //PAGE 이동		
					}
				});
			}
		});
		
		function dufilicationData() {
			var nameVal = $('input[name="name"]').val();
			var zip = $('input[name="zip"]').val();
			var addr1 = $('input[name="addr1"]').val();
			var addr2 = $('input[name="addr2"]').val();
			var identity = $('input[name="identity"]').val();
			var tel1 = $('input[name="tel1"]').val();
			
			if(nameVal) $('input[name="ceoName"]').val(nameVal);
			if($('input[name="idType"]').val() == '주민번호') {
				if(identity) $('input[name="ceoIdentity"]').val(identity);
			}
			if(addr1) $('input[name="ceoAddr1"]').val(addr1);
			if(addr2) $('input[name="ceoAddr2"]').val(addr2);
			if(zip) $('input[name="ceoZip"]').val(zip);
			if(tel1) $('input[name="ceoPhone"]').val(tel1);
		}
		
		$('#writeFrm .btn-addr').click(function() {
			var frm = 	$('#writeFrm');
            postCode($(this), frm.find('input[name="zip"]'), frm.find('input[name="addr1"]'), frm.find('input[name="addr2"]'), frm.find('input[name="lat"]'), frm.find('input[name="lng"]'));
	    });
		
		$('#writeFrm .btn-addr2').click(function() {
			var frm = 	$('#writeFrm'); 
            postCode($(this), frm.find('input[name="ceoZip"]'), frm.find('input[name="ceoAddr1"]'), frm.find('input[name="ceoAddr2"]'));
	    });
		
		gradeSelector('writeFrm', '${CP_SESSION.grade}', '대행사', 'distId');
		$.validator.addClassRules({ distId : { required : true } });
		
		$('#nav-member').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>