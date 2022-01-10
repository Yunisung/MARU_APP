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
									<li><span>정산관리</span><i class="fa fa-circle"></i></li>
                  <li><span>가맹점 정산 조회</span></li>
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
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/settle/mcht/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="가맹점 정산 리스트">
												<c:if test="${CP_SESSION.grade eq '본사' }">
												<input type="hidden" data-reg="false" name="thead" value="stlId:정산번호,status:지급상태,stlDay2:정산일,mchtName:가맹점명,mchtId:가맹점ID,ceoName:가맹점대표자명,payTerm:대상거래기간,payCnt:매입건수,payAmt:매입금액,rfdCnt:취소건수,rfdAmt:취소금액,holdCnt:보류건수,holdAmt:보류금액,totalAmt:매출합계,totalFee:수수료,vanFee:입금수수료,totalDistFee:대행사수수료,benefit:수익,stlAmount:지급예정,minusAmt:차감금액,deductAmt:예수금(미출금),payOutAmt:실지급액,bankName:지급은행,account:지급계좌,accntHolder:지급계좌예금주,stlRate:지급정산수수료율">
												</c:if>
												<c:if test="${CP_SESSION.grade ne '본사' }">
												<input type="hidden" data-reg="false" name="thead" value="stlId:정산번호,status:지급상태,stlDay2:정산일,mchtName:가맹점명,mchtId:가맹점ID,ceoName:가맹점대표자명,payTerm:대상거래기간,payCnt:매입건수,payAmt:매입금액,rfdCnt:취소건수,rfdAmt:취소금액,holdCnt:보류건수,holdAmt:보류금액,totalAmt:매출합계,totalFee:수수료,stlAmount:지급예정,minusAmt:차감금액,deductAmt:예수금(미출금),payOutAmt:실지급액,bankName:지급은행,account:지급계좌,accntHolder:지급계좌예금주,stlRate:지급정산수수료율">
												</c:if>
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="아이디">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtName typeahead" name="mchtName" data-oper="lk" data-search="mchtName" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 대표자</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="가맹점 대표자">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산예정일</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date" name="stlDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="stlDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm stlId" name="stlId" data-oper="eq" placeholder="정산번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">지급 상태</label>
															<select class="selectpicker col-lg-8 status" name="status" data-oper="eq">
																<option value="">-- 전체 --</option>
																<option value="지급대기">지급대기</option>
																<option value="지급완료">지급완료</option>
																<option value="지급보류">지급보류</option>
																<option value="지급실패">지급실패</option>
															</select>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산주기</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="stlType" data-oper="eq">
																<option value="">전체</option>
																<option value="D+1">D+1</option>
																<option value="D+2">D+2</option>
																<option value="D+3">D+3</option>
																<option value="D+4">D+4</option>
																<option value="D+5">D+5</option>
																<option value="D+6">D+6</option>
																<option value="D+7">D+7</option>
																<option value="C+1">C+1</option>
																<option value="C+2">C+2</option>
																<option value="C+3">C+3</option>
																<option value="C+4">C+4</option>
																<option value="C+5">C+5</option>
																<option value="C+6">C+6</option>
																<option value="C+7">C+7</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산구분</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="settleSvc" data-oper="eq">
																<option value="">전체</option>
																<option value="일반">일반</option>
																<option value="충전정산">충전정산</option>
															</select>
														</div>
														<!-- 
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">차감여부</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="deductAmtSelect" data-oper="eq" data-reg="false">
																<option value="">전체</option>
																<option value="00">차감 없음</option>
																<option value="11">차감 있음</option>
															</select>
															<input id="deductAmtSelect" type="hidden" name="deductAmt" data-oper="eq" data-reg="false" value="00">
														</div>
														//-->
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4"></label>
														</div>
														<c:import url="/common/selectGrade.jsp" />
														<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value=""/>
													</div>													
												</div>
												<div class="form-actions nobg right">
													<div class="">
														<c:if test="${CP_SESSION.grade eq '본사' }">
															<button type="button" class="btn btn-sm grey-salsa" id="tax-bill">
																<i class="fa fa-balance-scale" aria-hidden="true"></i> 세금계산서 발행&nbsp;
															</button>
														</c:if>
														<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
															<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
														</button>
														<button type="button" class="btn btn-sm green" id="search_submit" onClick="searchForList()">
															<i class="fa fa-search" aria-hidden="true"></i> SEARCH
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult"></div>
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
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ 
			searchForList(); 
		}, 100); //검색 실행
		$('#SearchForWaiting').click(function() {
			//$('#pageSize').val(100); 200개로 Controller 에서 설정됨.
			//$('#SearchClear').trigger('click');
			$('.selectpicker.status').selectpicker('refresh');
			searchForList();
		});
		<c:if test="${CP_SESSION.grade eq '본사'}">
		$('#nav-settle-mcht').addClass('active');
		</c:if>
		<c:if test="${CP_SESSION.grade ne '본사'}">
		$('#nav-sales').addClass('active');
		</c:if>
		
		$('#tax-bill').click(function() {
			var $modal = $('#pgmate-modal');
			$modal.addClass('modal-sm');
			if ($modal.children().length < 1) {
				$modal.empty();
			}
			 
			$modal.load('/settle/mcht/tax.jsp', '', function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					$modal.modal();
					textMask();
				}
			});
			
		});
		
		$(document).on('click', '#tax-download',function() {
			var sendObj = {
					trxDay: $('input[name="trxDay"]').val().replace(/-/gi, ''),
					identity: $('input[name="identity"]').val().replace(/-/gi, ''),
					compName: $('input[name="compName"]').val(),
					ceoName: $('input[name="ceoName1"]').val(),
					addr1: $('input[name="addr1"]').val(),
					addr2: $('input[name="addr2"]').val(),
					bizCategory: $('input[name="bizCategory"]').val(),
					bizType: $('input[name="bizType"]').val(),
					email: $('input[name="email"]').val(),
					distType: $('input[name="distType"]').is(':checked')
			}
			console.log('sendObj', sendObj);
			sendObj.ceoName == '' ? '권유현' : '';
			$.ajax({
	    		url : "/settle/tax/export",
	    		beforeSend : function(xhr) {
	    			xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
	    		},
	    		type : 'POST',
	    		data: JSON.stringify(sendObj),
	    		success : function(json, textStatus) {
	    			console.log(json);
	    			if(!json.file) {
	    				bootbox.alert("세금계산서 출력할 내용이 없습니다.");	
	    			} else {
	    				location.href = json.file.link;
	    			}
	    		},
	    		error : function(xhr, status, error) {
	    			bootbox.alert("세금계산서 출력에 실패했습니다.");
	    		}
	    	});
		});
		
		function changeDeductAmt(stlId, deductAmt, status) {
			//alert('changeDecuct AMT ' + stlId + ' - ' + deductAmt + ' - ' + status);
			
			if(status != '지급대기' && status != '지급보류') {
				bootbox.alert('이미 지급된 정산은 변경할 수 없습니다.');
				return;
			}
			
			var $modal = $('#pgmate-modal');
			$modal.addClass('modal-sm');
			if ($modal.children().length < 1) {
				$modal.empty();
			}
			 
			$modal.load('/settle/mcht/modal.jsp', '', function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					$modal.modal();
					$('#modal-deduct-stlId').val(stlId);
					$('#modal-deduct-deductAmt').val(addComma(String(deductAmt)));
					textMask();
				}
			});
		}

        $('select[name="deductAmtSelect"]').on('change', function() {
            var hidden = $('#deductAmtSelect');
            hidden.data('reg', 'true');
            hidden.attr('data-reg', 'true');
            hidden.val('0');
            if($(this).val() == '00') {
                hidden.data('oper', 'eq');
            } else if($(this).val() == '11') {
                hidden.data('oper', 'ne');
            } else {
                hidden.val('');
                hidden.data('reg', 'false');
                hidden.attr('data-reg', 'false');
            }
        });
		
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>