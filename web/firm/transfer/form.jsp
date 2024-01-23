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
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed">
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
									<li><span>펌뱅킹 관리</span><i class="fa fa-circle"></i></li>
									<li><span>잔액이체</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
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
								<!-- BEGIN PAGE CONTENT INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/firm/action" method="post">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-3">송신계좌</label>
															<select id="select" class="selectpicker col-lg-8" name="sendAccnt" data-oper="eq">
																<option value="08970022000000008" selected="selected">케이뱅크(70022000000008)</option>
																<%--<option value="0392070158564301">경남은행(2070158564301)</option>--%>
<%--																<option value="0392070158563707">경남은행(2070158563707)</option>--%>
																<option value="0398003344291839">경남은행(8003344291839)</option>
																<option value="0341107021617089">광주은행(1107021617089)</option>
															</select>
														</div>  
														
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">잔액</label>
															<div class="col-lg-8">
																<input type="text" id="balance" class="form-control input-sm" name="balance" placeholder="계좌잔액" onkeyup="commas(this)" value="0" readonly>
															</div>
														</div>
 														<div class="form-group pg-form-group">
															<button type="button" class="btn green btn-outline " value="0600300" onClick="handleClick(this)">      
																<i class="fa fa-calculator" aria-hidden="true"></i> 잔액조회     
															</button>
														</div>   
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-3">수신계좌</label>
															<select id="select" class="selectpicker col-lg-8" name="recvAccnt" data-oper="eq">
																<option value="088100035873256" selected="selected">신한은행(100-035-873256)(매출)</option>
																<option value="088100035873605">신한은행(100-035-873605)(운영)</option>
<%--																<option value="0201005103757834">우리은행(1005103757834)</option>--%>
<%--																<option value="0201005004107798">우리은행(1005004107798)</option>--%>
<%--																<option value="0392070131351802">경남은행(2070131351802)</option>--%>
															</select>
														</div>  
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">이체금액</label>
															<div class="col-lg-6">
																<input type="text" id="amount" class="form-control input-sm" name="amount" placeholder="모계좌 이체금액" onkeyup="commas(this)" value="0">
															</div>
															<label class="mt-checkbox mt-checkbox-outline">
																전액 <input type="checkbox" value="1" name="allCheck" onClick="check(this)"><span>
											            	</label>
														</div>
														<div class="form-group pg-form-group">
															<button type="button" class="btn purple btn-outline " value="0100100" onClick="handleClick(this)">    
																<i class="fa fa-mouse-pointer" aria-hidden="true"></i> 이체하기     
															</button>    
														</div>   
													</div>
												</div>      
											</form>
										</div>  
									</div>    
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult" style="height : 400px" >          
   										<div class="portlet light portlet-form"> 
											<div id="viewBox" class="portlet-body form light" style="display:none;">        
												<div class="form-group pg-form-group" id="messageBox">
					  
												</div>
											</div>
										</div>
									</div>
									<!-- 내용 폼 종료 -->
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

	<script type="text/javascript">
		$('#nav-firm').addClass('active');
		
		function check(box) {
			if(box.checked == true) {
				 $('#amount').val($('#balance').val());
			}else {
				 $('#amount').val('0');
			}
		}
		
		function commas(t) {
			// 콤마 빼고 
			var x = t.value;			
			x = x.replace(/,/gi, '');

		    // 숫자 정규식 확인
			var regexp = /^[0-9]*$/;

			if(!regexp.test(x)){ 
				$(t).val(""); 
				alert("숫자만 입력 가능합니다.");
			} else {
				x = x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");			

				$(t).val(x);			
			}
		}
		
		function handleClick(res) {
			var action = res.value;

			var sendBankCd = $("select[name=sendAccnt]").val().substring(0,3); 
			var recvBankCd = $("select[name=recvAccnt]").val().substring(0,3); 
			
			var sendAccnt = $("select[name=sendAccnt]").val().substring(3); 
			var recvAccnt = $("select[name=recvAccnt]").val().substring(3); 
			
			var amount = $('#amount').val().replaceAll(",","");
			var msg = "";
			
			if(action == "0100100" && amount == "0"){
				bootbox.alert("이체금액을 입력해주세요");   
			}else {
				if(sendAccnt == ""){       
			   		bootbox.alert("송신계좌를 선택해주세요");   
				}else{
		 			console.log('action:', action, 'sendBankCd:', sendBankCd, 'sendAccnt:', sendAccnt, 'recvBankCd:', recvBankCd, 'recvAccnt:', recvAccnt, 'amount:', amount);
		 			
		 			if(action == "0600300") {
	 					msg = "송신계좌 잔액조회를 하시겠습니까?";
					}else {
						msg = recvAccnt + " 계좌로 " + amount + "원을 이체하시겠습니까?";
					}
		 			
	 				$("#viewBox").hide();  
	 				
	 				bootbox.confirm({
	 					message: msg,
	 				    buttons: {
	 				        confirm: {
	 				            label: 'Yes',
	 				            className: 'btn-success'
	 				        },
	 				        cancel: {
	 				            label: 'No',
	 				            className: 'btn-danger'
	 				        }
	 				    },
	 				    callback: function (result) {
	 						if(result) {
	 							$.ajax({
	 								type: "post",
	 								url: "/firm/balanceTransfer",
	 								data: JSON.stringify({action: action, sendBankCd: sendBankCd, sendAccnt: sendAccnt, recvBankCd: recvBankCd, recvAccnt: recvAccnt, amount: amount}),
	 								beforeSend : function(xhr) {
	 							    xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
	 							  },
	 								success: function (response) {
	 									var msg = "응답코드: &nbsp;"+response.resultCd+"</br>응답메세지: &nbsp;"+response.resultMsg+"</br>";
	 									if(response.resultCd == '0000'){
	 										if(action == '0600300'){
	 											msg+="</br>잔액: &nbsp;"+response.data.amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	 											$('#balance').val(response.data.amount.toString());
	 										}else {
	 											msg = "이체성공";
	 											$('#amount').val('0');
	 										}
	 									}
	 									bootbox.alert(msg);
	 								}
	 							});
	 						}
	 				    }
	 				});	
				}
			}
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>