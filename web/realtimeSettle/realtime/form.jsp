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
									<li><span>즉시 정산내역 조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/realtime/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="실시간 정산내역">
												<input type="hidden" data-reg="false" name="thead" value="trxId:거래번호,mchtId:가맹점ID,name:가맹점,tmnId:터미널ID,trackId:주문번호,trxDay:거래일자,trxTime:거래시간,payType:결제수단,amount:금액,authCd:승인번호,trxType:거래승인구분,stlFee:가맹점 정산 수수료,stlFeeVat:가맹점 정산 수수료 VAT,stlAmount:정산금액,payOutFee:출금 수수료,payOutFeeVat:출금 수수료 VAT,bankFee:은행수수료,payOutAmount:실출금액,cancelAmount:취소입금액,bankCd:은행코드,bankName:은행이름,account:계좌번호,accntHolder:예금주,payOutDay:출금일자,payOutTime:출금시간,resultCd:출금 결과코드,resultMsg:출금 결과메시지,sendCnt:출금 전송횟수,cancelMemo:취소메모">
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
															<label class="control-label col-lg-4">터미널ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm tmnId" name="tmnId" data-oper="lk" placeholder="터미널 ID">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<div class="col-lg-4" style="padding:0;">
															<select class="selectpicker col-lg-12" name="" id="date-selector" data-reg="false">
																<option value="trxDay" selected>거래일</option>
																<option value="payOutDay">출금일</option>
															</select>
															</div>
															<div class="col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date date-selector-target" name="trxDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date date-selector-target" name="trxDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">출금여부</label>
															<select class="selectpicker col-lg-8" name="sendCheck" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="Y">성공</option>
																	<option value="N">실패</option>
																</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">결제수단</label>
															<select class="selectpicker col-lg-8" name="payType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="C">신용카드</option>
																<option value="V">가상계좌</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm trxId" name="trxId" data-oper="eq" placeholder="거래번호">
														</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm authCd" name="authCd" data-oper="eq" placeholder="승인번호">
														</div>
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
		
		function inputCancel(trxId,amt) {
			var $modal = $('#pgmate-modal');
			
			if($modal.children().length < 1) {
				$modal.empty();
			}
			
			$modal.load('/common/cancelInput.jsp', '', function(responseTxt, statusTxt, xhr){
				$('#modalForm input[name="cancelAmount"]').val(amt.replaceAll("-","").toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
				$('#modalForm input[name="payOutAmount"]').val(amt.replaceAll("-",""));
				
				if(statusTxt == "success"){
					$modal.modal();
				}
			});
		  
			$modal.on('hidden.bs.modal', function (e) {
				var flag = $('#modalForm input[name="input-flag"]').val();
				$('#modalForm input[name="cancelAmount"]').val($('#modalForm input[name="cancelAmount"]').val().replace(/,/g, ''));

				if(flag == "true") {
					$.ajax({
						url: "/realtime/cancelUpdate/" + trxId,
						type: "post",
						data: $("#modalForm").serialize(),
						success: function(result){
							if(result == 'true') {
								alert("취소데이터 정상처리 완료");	
							}else {
								alert("취소데이터 정상처리 실패");
							}
					    	
					    	searchForList();//검색 실행
						}});
				}
				
			  	$modal.empty();
			});
		};
		
		$('#date-selector').on('change', function() {
			$('.date-selector-target').attr('name', $(this).val());
		})
		
		function modifiCancel(trxId,amt,payOutAmount) {
			var $modal = $('#pgmate-modal');
			
			if($modal.children().length < 1) {
				$modal.empty();
			}
			
			var memo = selectMemo(trxId).replaceAll("&#34;","\"").replaceAll("&#39;","'");

			$modal.load('/common/cancelInput.jsp', '', function(responseTxt, statusTxt, xhr){
				if(statusTxt == "success"){
					$('#modalForm input[name="cancelAmount"]').val(amt.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
					$('#modalForm input[name="payOutAmount"]').val(payOutAmount.replaceAll("-",""));
					$('#modalForm textarea[name="cancelMemo"]').val(memo);
					$modal.modal();
				}
			});
		  
			$modal.on('hidden.bs.modal', function (e) {
				var flag = $('#modalForm input[name="input-flag"]').val();
				$('#modalForm input[name="cancelAmount"]').val($('#modalForm input[name="cancelAmount"]').val().replace(/,/g, ''));

				if(flag == "true") {
					$.ajax({
						url: "/realtime/cancelUpdate/" + trxId,
						type: "post",
						data: $("#modalForm").serialize(),
						success: function(result){
							if(result == 'true') {
								alert("취소데이터 정상처리 완료");	
							}else {
								alert("취소데이터 정상처리 실패");
							}
					    	
					    	searchForList();//검색 실행
						}});
				}
				
			  	$modal.empty();
			});
		};
		
		function selectMemo(trxId) {
			var memo = "";
			
			$.ajax({
				url: "/realtime/selectMemo/" + trxId,
				type: "post",
				async: false,
				success: function(result){
					memo = result;
				}});
			
			return memo;
		};
		
		function retryTrx(trxId) {
			bootbox.confirm({
			    message: "해당 거래를 재전송 처리 하시겠습니까?",
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
						$.ajax({url: "/realtime/cntUpdate/" + trxId,
						success: function(result){
							if(result == 'true') {
								alert("요청완료");	
							}else {
								alert("요청실패 확인요망.");
							}
					    	
					    	searchForList();//검색 실행
						}});
					}
			    }
			});
		};
		
		function selectRetry() {
			var trxId = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					trxId += $(e).attr('data-trxId') + ",";
				}
			});

			if (trxId.length < 1) {
				bootbox.alert("재전송할 대상을 체크하세요.");
				
				return;
			} else {
				trxId = trxId.substring(0, trxId.length - 1);
			}
			
			bootbox.confirm({
			    message: "선택한 거래 건을 모두 재전송 처리 하시겠습니까?",
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
						$.ajax({url: "/realtime/selectRetry/" + trxId,
						success: function(result){
							if(result == 'true') {
								alert("요청완료");	
							}else {
								alert("요청실패 확인요망.");
							}
					    	
					    	searchForList();//검색 실행
						}});
					}
			    }
			});
		};
		
		function failPop(message) {
			bootbox.alert(message);
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>