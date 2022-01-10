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
									<li><span>영중소 가맹점 정보 수정</span></li>
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
												<i class="fa fa-reorder"></i> <span class="caption-title">
													${DATAMAP.mchtName} 영중소 가맹점 정보 수정 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/diff/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													
													<input type="hidden" name="mchtId" value="${DATAMAP.mchtId}" data-key="true"/>
													<input type="hidden" name="vanId" value="2006500004" />
													<input type="hidden" name="compNo" value="4198800046" />
													 
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="40" name="mchtName" placeholder="" value="${DATAMAP.mchtName }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">사업자번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="10" name="mchtCompNo" placeholder="" value="${DATAMAP.mchtCompNo }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">업종</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="20" name="bizType" placeholder="" value="${DATAMAP.bizType }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">웹사이트URL</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="80" name="mchtUrl" placeholder="" value="https://www.mtouch.com">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대표자명</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="40" name="mchtCeo" placeholder="" value="${DATAMAP.mchtCeo }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점전화번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="20" name="mchtPhone" placeholder="" value="${DATAMAP.mchtPhone }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이메일주소</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" maxlength="40" name="mchtEmail" placeholder="" value="${DATAMAP.mchtEmail }">
														</div>
													</div>
													<!-- 주소 영역           다음 플러그인 -->
		                                            <div class="form-group col-sm-6">
		                                                <label class="control-label col-sm-4 req-label">우편번호</label>
		                                                <div class="col-md-6">
		                                                    <div class="input-group input-group-sm ">
		                                                        <span class="input-group-btn">
																			<button class="btn green btn-addr" type="button">주소 찾기</button>
																		</span>
		                                                        <input type="text" class="form-control input-sm zip" maxlength="6" name="zip" placeholder="" readonly value="${DATAMAP.zip }">
		                                                    </div>
		                                                    <!-- /input-group -->
		                                                </div>
		                                            </div>
		                                            <div class="form-group col-sm-6">
		                                                <label class="control-label req-label col-sm-4">주소</label>
		                                                <div class="col-sm-8">
		                                                    <input type="text" class="form-control input-sm addr1" maxlength="50" name="addr1" placeholder="" readonly value="${DATAMAP.addr1}">
		                                                </div>
		                                            </div>
		                                            <div class="form-group col-sm-6">
		                                                <label class="control-label col-sm-4 req-label">상세주소</label>
		                                                <div class="col-sm-8">
		                                                    <input type="text" class="form-control input-sm addr2" maxlength="50" name="addr2" placeholder="" value="${DATAMAP.addr2 }">
		                                                </div>
		                                            </div>
		                                            <div class="form-group col-sm-6">
                                                		<label class="control-label input-sm col-sm-4 req-label">차액정산 VAN</label>
	                                                	<select name="vanName" class="selectpicker col-sm-6">
		                                                	<c:choose>
			                                                    <c:when test="${DATAMAP.vanName eq 'KSNET'}">
				                                                    <option value="KSNET" selected>KSNET</option>
																	<option value="KCP">KCP</option>
			                                                    </c:when>
			                                                    <c:otherwise>
				                                                    <option value="KSNET">KSNET</option>
																	<option value="KCP" selected>KCP</option>
			                                                    </c:otherwise>
		                                                    </c:choose>
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
	<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=525bdd42a1b50641247881ae3f07e6cb&libraries=services"></script>
    <script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
	<c:import url="/include/javascript.jsp" />
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				mchtName : {
					required:true
				},
				mchtCompNo : {
					required:true
				},
				bizType : {
					required:true
				},
				mchtUrl : {
					required:true
				},
				mchtCeo : {
					required:true
				},
				mchtPhone : {
					required:true
				},
				mchtEmail : {
					required:true,
					email:true
				},
				addr1 : {
					required:true
				},
				addr2 : {
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.mchtName} 영중소 가맹점 정보를 수정하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_diff'); //PAGE 이동		
					}
				});
			}
		});
		

		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>