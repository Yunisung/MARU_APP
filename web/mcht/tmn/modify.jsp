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
									<li><span>터미널 정보 수정</span></li>
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
												<span class="caption-title"> ${DATAMAP.name} 터미널 정보 수정 </span>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/tmn/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<input type="hidden" name="blockCard" id="blockCard" value=""/>
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<input type="hidden" class="form-control input-sm tmnId" name="tmnId" data-key="true" value="${DATAMAP.tmnId}">
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">터미널 아이디</label>
														<div class="col-sm-6">
															<c:if test="${HasTransaction eq 'false'}">
															<input type="text" class="form-control input-sm tmnId" name="tmnId" placeholder="" value="${DATAMAP.tmnId}" >
															</c:if>
															<c:if test="${HasTransaction eq 'true'}">
															<input type="text" class="form-control input-sm tmnId" name="tmnId" placeholder="" value="${DATAMAP.tmnId}" readonly data-reg="false">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4">Tax 등록</label>
														<select name="taxId" class="selectpicker col-sm-6">
															<option value="">선택하세요.</option>
															<c:forEach var="entry" items="${DATATAXMAP}" varStatus="status" >
																<option value="${entry.id}">${entry.name }</option>
															</c:forEach>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.taxId.value = '${DATAMAP.taxId}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">상태</label>
														<select name="status" class="selectpicker col-sm-6 status">
															<option value="예비">예비</option>
															<option value="대기">승인대기</option>
															<option value="중지">중지</option>
															<c:if test="${CP_SESSION.grade eq '본사' || DATAMAP.status eq '사용'}">
																<option value="사용" >사용</option>
															</c:if>
															<c:if test="${CP_SESSION.grade eq '본사'}">
																<option value="폐기">폐기</option>
															</c:if>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.status.value = '${DATAMAP.status}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">일련번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm serial" maxlength="100" name="serial" placeholder="" value="${DATAMAP.serial}">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">온라인 결제 Key</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm payKey" maxlength="100" name="payKey" placeholder="" value="${DATAMAP.payKey}" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">웹결제창</label>
														<select name="webPay" class="selectpicker col-sm-6">
															<option value="미사용" selected="selected">미사용</option>
															<option value="사용">사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.webPay.value = '${DATAMAP.webPay}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">앱을통한 수기결제</label>
														<select name="appDirect" class="selectpicker col-sm-6">
															<option value="N" selected="selected">미사용</option>
															<option value="Y">사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.appDirect.value = '${DATAMAP.appDirect}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">최대할부기간</label>
														<select name="apiMaxInstall" class="selectpicker col-sm-6">
															<option value="0">일시불</option>
															<option value="2">2개월</option>
															<option value="3">3개월</option>
															<option value="4">4개월</option>
															<option value="5">5개월</option>
															<option value="6">6개월</option>
															<option value="7">7개월</option>
															<option value="8">8개월</option>
															<option value="9">9개월</option>
															<option value="10">10개월</option>
															<option value="11">11개월</option>
															<option value="12">12개월</option>
															<option value="13">13개월</option>
															<option value="14">14개월</option>
															<option value="15">15개월</option>
															<option value="16">16개월</option>
															<option value="17">17개월</option>
															<option value="18">18개월</option>
															<option value="19">19개월</option>
															<option value="20">20개월</option>
															<option value="21">21개월</option>
															<option value="22">22개월</option>
															<option value="23">23개월</option>
															<option value="24">24개월</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.apiMaxInstall.value = '${DATAMAP.apiMaxInstall}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">웹결제인증유형</label>
														<select name="semiAuth" class="selectpicker col-sm-6">
															<option value="N" selected="selected">일반</option>
															<option value="Y">일반(비생인증)</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.semiAuth.value = '${DATAMAP.semiAuth}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label  col-sm-4 req-label">활성화 시작일자</label>
														<div class="col-md-4 col-sm-6">
															<c:choose>
																<c:when test="${DATAMAP.activeDate ne ''}">
																	<fmt:parseDate value="${DATAMAP.activeDate}" var="dateStr" pattern="yyyyMMdd"/>
																	<fmt:formatDate value="${dateStr }" pattern="yyyy-MM-dd" var="activeDate"/>
																	<input type="text" class="form-control input-sm datepicker activeDate" maxlength="10" name="activeDate" placeholder="" value="${activeDate}">
																</c:when>
																<c:otherwise>
																	<input type="text" class="form-control input-sm datepicker activeDate now-date" maxlength="10" name="activeDate" placeholder="" value="">
																</c:otherwise>
															</c:choose>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">신용/체크 분리여부</label>
														<select name="ccType" class="selectpicker col-sm-6">
															<option value="동일">동일</option>
															<option value="분리">분리</option>
														</select>
													</div>
													<script type="text/javascript"> document.forms.writeFrm.ccType.value = '${DATAMAP.ccType}' </script>
													<c:if test="${CP_SESSION.grade == '본사'}">
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4 req-label">정산후취소여부</label>
															<select name="refundType" class="selectpicker col-sm-6">
																<option value="불가">불가</option>
																<option value="가능">가능</option>
															</select>
														</div>
														<script type="text/javascript"> document.forms.writeFrm.refundType.value = '${DATAMAP.refundType}' </script>

														<div class="form-group col-sm-12">
															<label class="control-label input-sm col-sm-2">최소금액 제한</label>
															<div class="col-sm-8">
																<input id="minAmount" type="text" class="form-control input-sm minAmount" maxlength="100" name="minAmount" placeholder="" value="${DATAMAP.minAmount}">
															</div>
														</div>

														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">영업시간/금액 제한여부</label>
															<select name="payLimit" class="selectpicker col-sm-6">
																<option value="N" selected="selected">미사용</option>
																<option value="Y">사용</option>
															</select>
														</div>
														<script type="text/javascript"> document.forms.writeFrm.payLimit.value = '${DATAMAP.payLimit}' </script>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">결제 상한금액</label>
															<div class="col-sm-6">
																<input id="limitAmount" type="text" class="form-control input-sm limitAmount" maxlength="100" name="limitAmount" placeholder="" value="${DATAMAP.limitAmount}">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">영업제한 시작시각</label>
															<div class="col-md-4 col-sm-6">
																<fmt:parseDate value="${DATAMAP.limitStartTime}" var="timeStartStr" pattern="HHmmss"/>
																<fmt:formatDate value="${timeStartStr }" pattern="HH:mm:ss" var="limitStartTime"/>
																<input id="limitStartTime" type="text" class="form-control input-sm timeInput limitStartTime" maxlength="8" name="limitStartTime" placeholder="" value="${limitStartTime}">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">영업제한 종료시각</label>
															<div class="col-md-4 col-sm-6">
																<fmt:parseDate value="${DATAMAP.limitEndTime}" var="timeEndStr" pattern="HHmmss"/>
																<fmt:formatDate value="${timeEndStr }" pattern="HH:mm:ss" var="limitEndTime"/>
																<input id="limitEndTime" type="text" class="form-control input-sm timeInput limitEndTime" maxlength="8" name="limitEndTime" placeholder="" value="${limitEndTime}">
															</div>
														</div>
													</c:if>
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2 req-label">취급 품목</label>
														<div class="col-sm-8">
															<input type="text" class="form-control input-sm description" name="description" placeholder="취급 품목을 입력하세요." value="${DATAMAP.description}">
														</div>
													</div>
													<c:if test="${CP_SESSION.grade eq '본사'}">
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2">수기결제 제한 카드</label>
														<div class="col-sm-8">
															<table>
																<tr>
																	<td>
																		<input type="checkbox" id="card_km" value="국민"/><label for="card_km" value="국민"></label>
																	</td>
																	<td>
																		국민
																	</td>
																	<td>
																		<input type="checkbox" id="card_bc" value="비씨"/><label for="card_bc"></label>
																	</td>
																	<td>
																		비씨
																	</td>
																	<td>
																		<input type="checkbox" id="card_lt" value="롯데"/><label for="card_lt"></label>
																	</td>
																	<td>
																		롯데
																	</td>
																	<td>
																		<input type="checkbox" id="card_ss" value="삼성"/><label for="card_ss"></label>
																	</td>
																	<td>
																		삼성
																	</td>
																	<td>
																		<input type="checkbox" id="card_sh" value="신한"/><label for="card_sh"></label>
																	</td>
																	<td>
																		신한
																	</td>
																	<td>
																		<input type="checkbox" id="card_wr" value="우리"/><label for="card_wr"></label>
																	</td>
																	<td>
																		우리
																	</td>
																	<td>
																		<input type="checkbox" id="card_hn" value="하나"/><label for="card_hn"></label>
																	</td>
																	<td>
																		하나
																	</td>
																	<td>
																		<input type="checkbox" id="card_hd" value="현대"/><label for="card_hd"></label>
																	</td>
																	<td>
																		현대
																	</td>
																</tr>
																<tr>
																	<td>
																		<input type="checkbox" id="bank_gj" value="광주"/><label for="bank_gj"></label>
																	</td>
																	<td>
																		광주
																	</td>
																	<td>
																		<input type="checkbox" id="bank_nh" value="농협"/><label for="bank_nh"></label>
																	</td>
																	<td>
																		농협
																	</td>
																	<td>
																		<input type="checkbox" id="bank_sh" value="수협"/><label for="bank_sh"></label>
																	</td>
																	<td>
																		수협
																	</td>
																	<td>
																		<input type="checkbox" id="bank_jb" value="전북"/><label for="bank_jb"></label>
																	</td>
																	<td>
																		전북
																	</td>
																	<td>
																		<input type="checkbox" id="bank_jj" value="제주"/><label for="bank_jj"></label>
																	</td>
																	<td>
																		제주
																	</td>
																</tr>
															</table>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">VAN</label>
														<select name="van" class="selectpicker col-sm-6 van">
															<option value="">VAN 선택</option>
															<c:forEach var="entryMap" items="${VANMAP}">
																<option value="${entryMap['van']}">${entryMap['van']}</option>
															</c:forEach>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.van.value = '${DATAMAP.van}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">VAN ID</label>
														<select name="vanIdx" class="selectpicker col-sm-6 vanIdx">
															<option value="">VAN ID 선택</option>
															<c:forEach var="entryMap" items="${VANIDMAP}">
																<option value="${entryMap['idx']}">${entryMap['name']}</option>
															</c:forEach>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.vanIdx.value = '${DATAMAP.vanIdx}' </script>
													</div>
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

		var selectBank = "";
		const hiddenInput = document.getElementById('blockCard');

		function handleCheckboxChange(event) {
			const checkbox = event.target; // 이벤트가 발생한 체크박스
			const value = checkbox.value;

			if (checkbox.checked) {
				// 체크박스를 선택했을 때의 동작
				selectBank += checkbox.value;
			} else {
				// 체크박스를 해제했을 때의 동작
				selectBank = selectBank.replace(value, '');
			}

			// console.log(selectBank);
			hiddenInput.value = selectBank;
			console.log('Updated Value : ', hiddenInput.value);
		}

		window.onload = function() {
			const blockCard = '${DATAMAP.blockCard}';
			const blockCardList = splitIntoChunks(blockCard, 2);
			const checkboxes = document.querySelectorAll('input[type="checkbox"]');
			checkboxes.forEach(checkbox => {
				if(blockCardList.includes(checkbox.value)) {
					checkbox.checked = true;
					selectBank += checkbox.value;
				}

				checkbox.addEventListener('change', handleCheckboxChange);
			});
		};

		function splitIntoChunks(inputString, chunkSize) {
			const result = [];
			for (let i = 0; i < inputString.length; i += chunkSize) {
				result.push(inputString.substring(i, i + chunkSize));
			}
			return result;
		}


		$(function(){
			$("#limitStartTime").timepicker({
				step: 30,            //시간간격 : 5분
				timeFormat: "H:i:s"    //시간:분 으로표시
			});
			$("#limitEndTime").timepicker({
				step: 30,            //시간간격 : 5분
				timeFormat: "H:i:s"    //시간:분 으로표시
			});
		});



		var statusReq = function() {
			if($('.status.selectpicker').val() == '사용') {
				return true;
			} else {
				return false;
			}
		}
	
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				payKey : {
					required : true
				},
				activeDate : { 
					required :true 
				},
				van: {
					required : statusReq
				},
				vanIdx : {
					required : statusReq
				},
				taxId: {
					required : statusReq
				},
				description: {
					required: true
				},
				limitStartTime:{
					minlength : 8
				},
				limitEndTime:{
					minlength:8
				},
				limitAmount:{
					number : true
				},
				minAmount:{
					number : true
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 가맹점 터미널 정보를 수정하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_tmn'); //PAGE 이동		
					}
				});
			}
		});
		

		$('.selectpicker.van').on('change', function(){
		    var selected = $(this).find("option:selected").val();
		    if(selected) {
		    	$.get("/mcht/van/select/" + selected, function(data, status){
		    		data = jQuery.parseJSON(data);
		    		$(".selectpicker.vanIdx").html('<option value="">VAN ID 선택</option>').selectpicker('refresh');
		    		if(data.length > 0) {
		    			$.each(data, function(index, val) {
		    				$(".selectpicker.vanIdx").append('<option value="'+val.idx+'">('+val.vanId + ") "+ val.name+'</option>');
							});
							$(".selectpicker.vanIdx").selectpicker('refresh');
			      } else {
			        	bootbox.alert('사용할 수 있는 VAN ID가 없습니다.');
			      }
			    }); 
		    }
		});
		if($('.selectpicker.van').find("option:selected").val()) {
			$(".selectpicker.vanIdx").append('<option value="${DATAMAP.vanIdx}" selected>${DATAMAP.vanName} (${DATAMAP.vanId})</option>').selectpicker('refresh');
		}
		
   		$('.minAmount').val(addComma(String($('.minAmount').val()).replace(/[^0-9]/g,"")));
   		$('.limitAmount').val(addComma(String($('.limitAmount').val()).replace(/[^0-9]/g,"")));

		$('.minAmount').keyup(function(){
			$('.minAmount').val(addComma(String($('.minAmount').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.limitAmount').keyup(function(){
			$('.limitAmount').val(addComma(String($('.limitAmount').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
       	
		$(".loading-btn").click(function(){
			$(".minAmount").val($(".minAmount").val().replace(/,/g, ''));
			$(".limitAmount").val($(".limitAmount").val().replace(/,/g, ''));
		});
	
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>