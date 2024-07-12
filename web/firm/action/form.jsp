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
									<li><span>개시,잔액조회</span></li>
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
															<label class="control-label col-lg-3">처리일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">  
																<input type="text" class="form-control now-date" name="recvDate" value="" data-oper="ge">
																<span class="input-group-addon">~</span>
																<input type="text" class="form-control now-date" name="recvDate" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-3">은행선택</label>
															<select id="select" class="selectpicker col-lg-8" name="mAccnt" data-oper="eq">
																<option value="">-----&nbsp;</option>  
																<option value="08970022000000008" selected="selected">케이뱅크(70022000000008)</option>
																<%--<option value="0392070158564301">경남은행(2070158564301)</option>--%>
<%--																<option value="0392070158563707">경남은행(2070158563707)</option>--%>
																<option value="0398003344291839">경남은행(8003344291839)</option>
                                                                <option value="0341107021617114">광주은행(1107021617114)</option>
																<option value="007101024656079">수협은행(101024656079)</option>
																<option value="048131022424175">신협은행(131022424175)</option>
															</select>
														</div>  
 														<div class="form-group pg-form-group">
<%--																<button type="button" class="btn blue btn-outline " value="0800100" onClick="handleClick(this)">      --%>
<%--																	<i class="fa fa-mouse-pointer" aria-hidden="true"></i> 업무개시     --%>
<%--																</button>  --%>
																<button type="button" class="btn green btn-outline " value="0600300" onClick="handleClick(this)">      
																	<i class="fa fa-calculator" aria-hidden="true"></i> 잔액조회     
																</button>
<%--																<button type="button" class="btn purple btn-outline " value="0700100" onClick="handleClick(this)">    --%>
<%--																	<i class="fa fa-bar-chart" aria-hidden="true"></i> 거래집계     --%>
<%--																</button>    --%>
<%--																<button type="button" class="btn red btn-outline " value="0800800" onClick="handleClick(this)">                --%>
<%--																	<i class="fa fa-wrench" aria-hidden="true"></i> 테스트콜           --%>
<%--																</button>   --%>
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
		
		 function handleClick(res) {
			var action = res.value;
			
			var accnt = $("select[name=mAccnt]").val(); 
			var bankCd = "";
			var mAccnt = "";
			
		   	if(accnt == "" ){       
		   		bootbox.alert("은행을 선택해주세요");   
			}else{
	 			bankCd = accnt.substring(0,3);
	 			mAccnt = accnt.substring(3);
	 			
	 			console.log('action:', action, 'bankCd:', bankCd, 'mAccnt:', mAccnt);
	 			
	 			if(bankCd == "089" && action != "0600300") {
	 				bootbox.alert("케이뱅크는 해당기능을 지원하지않습니다.");   
	 			}else if(bankCd == "007" && action != "0600300") {
	 				bootbox.alert("수협은행은 해당기능을 지원하지않습니다.");   
	 			}else if(bankCd == "048" && action != "0600300") {
					bootbox.alert("신협은행은 해당기능을 지원하지않습니다.");
				}else {
	 				$("#viewBox").hide();  
					$.ajax({
						type: "post",
						url: "/firm/action",
						data: JSON.stringify({action: action, bankCd: bankCd, mAccnt: mAccnt}),
						beforeSend : function(xhr) {
					    xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
					  },
						success: function (response) {
							//console.log('res: ', response);
							var msg = "응답코드: &nbsp;"+response.resultCd+"</br>응답메세지: &nbsp;"+response.resultMsg+"</br>";
							if(response.resultCd == '0000'){
								if(action == '0600300'){
									msg+="</br>잔액: &nbsp;"+response.data.amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>출금가능금액: &nbsp;"+response.data.payable_amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								}else if(action == '0700100'){
									msg+="</br>당행요청건수 : &nbsp;"+response.data.reqCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>당행요청금액 : &nbsp;"+response.data.reqAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>당행성공건수 : &nbsp;"+response.data.sucCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>당행성공금액 : &nbsp;"+response.data.sucAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>당행실패건수 : &nbsp;"+response.data.failCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>당행실패금액 : &nbsp;"+response.data.failAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행요청건수 : &nbsp;"+response.data.oReqCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행요청금액 : &nbsp;"+response.data.oReqAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행성공건수 : &nbsp;"+response.data.oSucCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행성공금액 : &nbsp;"+response.data.oSucAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행실패건수 : &nbsp;"+response.data.oFailCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행실패금액 : &nbsp;"+response.data.oFailAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br></br>당행수수료합 : &nbsp;"+response.data.fee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타행수수료합 : &nbsp;"+response.data.oFee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
									msg+="</br>타임아웃건수 : &nbsp;"+response.data.timeOutCount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								}
							}
							bootbox.alert(msg);
							$("#messageBox").html(msg);  
							$("#viewBox").show();  
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