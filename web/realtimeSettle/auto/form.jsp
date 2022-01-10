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
									<li><span>실시간 정산</span><i class="fa fa-circle"></i></li>
									<li><span>자동 정산내역 조회</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
										</div>
									</div>
							</div>
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/auto/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="자동 정산내역">
												<input type="hidden" data-reg="false" name="thead" value="stlType:정산주기,stlId:정산번호,status:지급상태,stlDay:정산일,payOutDay:출금일,payOutTime:출금시간,name:가맹점명,mchtId:가맹점ID,payTerm:대상거래기간,totCnt:거래건수,totAmt:거래금액,totalFee:가맹점수수료,totPayOutFee:출금수수료,bankFee:은행수수료,stlAmount:정산예정금액,minusAmt:차감금액,deductAmt:예수금,payOutAmount:실지급액,diffAmt:차액,bankName:지급은행,account:지급계좌,accntHolder:지급계좌예금주,stlRate:지급정산수수료율,sendCnt:전송횟수,sendCheck:출금여부">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점 ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="가맹점명">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm stlId" name="stlId" data-oper="eq" placeholder="정산번호">
															</div>
														</div>
													</div>
													<div class="row">
													<div class="form-group pg-form-group">
														<div class="col-lg-4" style="padding:0;">
															<select class="selectpicker col-lg-12" name="" id="date-selector" data-reg="false">
																<option value="stlDay" selected>정산예정일</option>
																<option value="payOutDay">출금일</option>
															</select>
															</div>
															<div class="col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date date-selector-target" name="stlDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date date-selector-target" name="stlDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산주기</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="stlType" data-oper="eq">
																<option value="">전체</option>
																<option value="A+1">A+1</option>
																<option value="A+0">A+0</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">지급 상태</label>
															<select class="selectpicker col-lg-8 status" name="status" data-oper="eq">
																<option value="">-- 전체 --</option>
																<option value="지급대기">지급대기</option>
																<option value="지급완료">지급완료</option>
																<option value="지급실패">지급실패</option>
															</select>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">출금여부</label>
															<select class="selectpicker col-lg-8" name="sendCheck" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="Y">성공</option>
																	<option value="N">실패</option>
																</select>
														</div>
													</div>
												</div>
												<div class="form-actions nobg right">
													<div class="btn folding-search-btn icon-arrow-down"></div>
													<div class="">
														<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
															<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
														</button>
														<button type="button" class="btn btn-sm green" id="searchSubmit" onClick="searchForList()">
															<i class="fa fa-search" aria-hidden="true"></i> SEARCH
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult">
										
									</div>
									<!-- 내용 폼 종료 -->
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				<!-- BEGIN CONTAINER -->
		</div>
		<iframe id="txtArea1" style="display:none"></iframe>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-realtime').addClass('active');
		
		function inputMinusAmt(stlId, payOutAmount) {
			var $modal = $('#pgmate-modal');
			
			if($modal.children().length < 1) {
				$modal.empty();
			}
			
			$modal.load('/common/minusAmtInput.jsp', '', function(responseTxt, statusTxt, xhr){
				
				$('#minusAmtForm input[name="minusAmt"]').val(payOutAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
				$('#minusAmtForm input[name="payOutAmount"]').val(payOutAmount);
				
				if(statusTxt == "success"){
					$modal.modal();
				}
			});
		  
			$modal.on('hidden.bs.modal', function (e) {
				var flag = $('#minusAmtForm input[name="input-flag"]').val();
				$('#minusAmtForm input[name="minusAmt"]').val($('#minusAmtForm input[name="minusAmt"]').val().replace(/,/g, ''));
				if(flag == "true") {
					$.ajax({
						url: "/auto/minusAmtUpdate/" + stlId,
						type: "post",
						data: $("#minusAmtForm").serialize(),
						success: function(result){
							if(result == 'true') {
								alert("차감금액 데이터 정상처리 완료");	
							}else {
								alert("차감금액 데이터 정상저리 실패");
							}
					    	
					    	searchForList();//검색 실행
						}});
				}
				
			  	$modal.empty();
			});
		};
		
		
		function modifiMinusAmt(stlId,minusAmt,payOutAmount) {
			var $modal = $('#pgmate-modal');
			
			if($modal.children().length < 1) {
				$modal.empty();
			}
			
			var memo = selectMinusAmtMemo(stlId).replaceAll("&#34;","\"").replaceAll("&#39;","'");

			$modal.load('/common/minusAmtInput.jsp', '', function(responseTxt, statusTxt, xhr){
				if(statusTxt == "success"){
					$('#minusAmtForm input[name="minusAmt"]').val(minusAmt.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
					$('#minusAmtForm input[name="payOutAmount"]').val(payOutAmount);
					$('#minusAmtForm textarea[name="minusAmtMemo"]').val(memo);
					$modal.modal();
				}
			});
		  
			$modal.on('hidden.bs.modal', function (e) {
				var flag = $('#minusAmtForm input[name="input-flag"]').val();
				$('#minusAmtForm input[name="minusAmt"]').val($('#minusAmtForm input[name="minusAmt"]').val().replace(/,/g, ''));
				if(flag == "true") {
					$.ajax({
						url: "/auto/minusAmtUpdate/" + stlId,
						type: "post",
						data: $("#minusAmtForm").serialize(),
						success: function(result){
							if(result == 'true') {
								alert("차감금액 데이터 정상처리 완료");	
							}else {
								alert("차감금액 데이터 정상저리 실패");
							}
					    	
					    	searchForList();//검색 실행
						}});
				}
				
			  	$modal.empty();
			});
		};
		
		function selectMinusAmtMemo(stlId) {
			var memo = "";
			
			$.ajax({
				url: "/auto/selectMinusAmtMemo/" + stlId,
				type: "post",
				async: false,
				success: function(result){
					memo = result;
				}});
			
			return memo;
		};
		
		function inputDeductAmt(stlId,payOutFee,payOutAmount) {
			var $modal = $('#pgmate-modal');
			
			if($modal.children().length < 1) {
				$modal.empty();
			}
			
			$modal.load('/common/deductAmtInput.jsp', '', function(responseTxt, statusTxt, xhr){
				if(statusTxt == "success"){
					$('#deductAmtForm input[name="payOutFee"]').val(payOutFee);
					$('#deductAmtForm input[name="payOutAmount"]').val(payOutAmount);
					
					$modal.modal();
				}
			});
		  
			$modal.on('hidden.bs.modal', function (e) {
				var flag = $('#deductAmtForm input[name="input-flag"]').val();
				$('#deductAmtForm input[name="deductAmt"]').val($('#deductAmtForm input[name="deductAmt"]').val().replace(/,/g, ''));
				if(flag == "true") {
					$.ajax({
						url: "/auto/deductAmtUpdate/" + stlId,
						type: "post",
						data: $("#deductAmtForm").serialize(),
						success: function(result){
							if(result == 'true') {
								alert("예수금 데이터 입력 완료");	
							}else {
								alert("예수금 데이터 입력 실패");
							}
					    	
					    	searchForList();//검색 실행
						}});
				}
				
			  	$modal.empty();
			});
		};
		
		
		function modifiDeductAmt(stlId,payOutFee,deductAmt,payOutAmount) {
			var $modal = $('#pgmate-modal');
			
			if($modal.children().length < 1) {
				$modal.empty();
			}
			
			var memo = selectDeductAmtMemo(stlId).replaceAll("&#34;","\"").replaceAll("&#39;","'");

			$modal.load('/common/deductAmtInput.jsp', '', function(responseTxt, statusTxt, xhr){
				if(statusTxt == "success"){
					$('#deductAmtForm input[name="payOutFee"]').val(payOutFee);
					$('#deductAmtForm input[name="deductAmt"]').val(deductAmt.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
					$('#deductAmtForm input[name="payOutAmount"]').val(payOutAmount);
					$('#deductAmtForm textarea[name="deductAmtMemo"]').val(memo);
					$modal.modal();
				}
			});
		  
			$modal.on('hidden.bs.modal', function (e) {
				var flag = $('#deductAmtForm input[name="input-flag"]').val();
				$('#deductAmtForm input[name="deductAmt"]').val($('#deductAmtForm input[name="deductAmt"]').val().replace(/,/g, ''));
				if(flag == "true") {
					$.ajax({
						url: "/auto/deductAmtUpdate/" + stlId,
						type: "post",
						data: $("#deductAmtForm").serialize(),
						success: function(result){
							if(result == 'true') {
								alert("예수금 데이터 수정 완료");	
							}else {
								alert("예수금 데이터 수정 실패");
							}
					    	
					    	searchForList();//검색 실행
						}});
				}
				
			  	$modal.empty();
			});
		};
		
		function selectDeductAmtMemo(stlId) {
			var memo = "";
			
			$.ajax({
				url: "/auto/selectDeductAmtMemo/" + stlId,
				type: "post",
				async: false,
				success: function(result){
					memo = result;
				}});
			
			return memo;
		};
		
		function msgPop(message) {
			bootbox.alert(message);
		}
		
		$('#date-selector').on('change', function() {
			$('.date-selector-target').attr('name', $(this).val());
		})
		
		function retrySend(stlId, bankCd, account, mchtId, stlDay, stlType, payOutAmt) {
			if(payOutAmt > 0) {
				bootbox.confirm({
				    message: "해당 건을 재전송 처리 하시겠습니까?",
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
							var json = new Object();
							json.bankCd = bankCd;
							json.account = account;
							json.amount = payOutAmt;
							json.stlId = stlId;
							json.mchtId = mchtId;
							json.stlDay = stlDay;
							json.stlType = stlType;
							
							$.ajax({
								url: "/auto/retrySend",
						        type: "POST",
						        data: JSON.stringify(json),
						        contentType: "application/json",
						        dataType: "json",
						        success: function (data) {
						    	  if(data.resultCd == '0000'){
						    		  bootbox.alert("이체가 완료되었습니다.");  
						    	  }else{
						    		  bootbox.alert(data.resultMsg);
						    	  }
						       },
						       error: function () {
						           bootbox.alert("잠시 후 다시 시도해주세요.");
						       }					     				       
						    });
							
							searchForList();//검색 실행
						}
				    }
				});
			} else if(payOutAmt == 0) {
				bootbox.confirm({
				    message: "해당 건을 지급완료 처리 하시겠습니까?",
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
							var json = new Object();
							json.bankCd = bankCd;
							json.account = account;
							json.amount = payOutAmt;
							json.stlId = stlId;
							json.mchtId = mchtId;
							json.stlDay = stlDay;
							json.stlType = stlType;
							
							$.ajax({
								url: "/auto/statusUpdate",
						        type: "POST",
						        data: JSON.stringify(json),
						        contentType: "application/json",
						        dataType: "json",
						        success: function (data) {
						    	  if(data.resultCd == '0000'){
						    		  bootbox.alert("상태 업데이트가 완료되었습니다.");  
						    	  }else{
						    		  bootbox.alert(data.resultMsg);
						    	  }
						       },
						       error: function () {
						           bootbox.alert("잠시 후 다시 시도해주세요.");
						       }					     				       
						    });
							
							searchForList();//검색 실행
						}
				    }
				});
			} 
		};
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>