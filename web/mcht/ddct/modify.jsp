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
									<li><span>차감정산 수정</span></li>
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
													차감정산 수정 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/ddct/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												
												<div class="form-body row">
													<input type="hidden" name="ddctId" value="${DATAMAP.ddctId }"/>
													<input type="hidden" name="mchtId" value="${DATAMAP.mchtId }"/>
													<input type="hidden" name="type" value="${DATAMAP.type }"/>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 항목</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" name="ddctName" readonly="readonly" value="${DATAMAP.ddctName }"> 
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 금액</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency monthlyAmt" maxlength="11" name="monthlyAmt" placeholder="" value="${DATAMAP.monthlyAmt }"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 시작월</label>
														<div class="col-sm-6">
															<div class="input-icon">
																	<i class="fa fa-calendar font-blue"></i><input class="datepicker form-control ddct" name="startMonth" id="startMonth">
															</div>
														</div>
													</div>
													<script type="text/javascript"> document.forms.writeFrm.startMonth.value = '${DATAMAP.startMonth}' </script>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 종료월</label>
														<div class="col-sm-6">
															<div class="input-icon">
																	<i class="fa fa-calendar font-blue"></i><input class="datepicker form-control" name="endMonth" id="endMonth">
															</div>
														</div>
													</div>
													<script type="text/javascript"> document.forms.writeFrm.endMonth.value = '${DATAMAP.endMonth}' </script>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 적용일자</label>
														<select name="settleType" class="selectpicker col-sm-6 settleType">
															<option value="M+1" selected>1일 정산</option>
															<c:forEach var="day" begin="2" end="31" step="1">
																<option value="M+${day }">${day }일 정산</option>
															</c:forEach>
														</select>
													</div>
													<script type="text/javascript"> document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}' </script>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">상태</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" name="status" readonly="readonly" value="${DATAMAP.status }"> 
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group col-sm-12 text-center">
														<h5>* 차감현황  총 금액 : <fmt:setLocale value="ko_KR"/><fmt:formatNumber type="currency" value="${DATAMAP.totalAmt }"/>
														<c:set var="var1" value="0"/>
														<c:forEach items="${SCHELIST }" var="entry">
															<c:if test="${entry.stlStatus eq '정산완료'}">
																<c:set var="var1"  value="${var1 +  entry.ddctAmt}"/>
															</c:if>
														</c:forEach>
														차감금액 : <fmt:formatNumber type="currency" value="${var1 }"/>
														잔여금액 : <fmt:formatNumber type="currency" value="${DATAMAP.totalAmt - var1 }"/>
														</h5>
													</div>
												</div>
												<div class="alert alert-danger display-hide"></div>
												<div class="form-actions right">
													<div class="">
														<button type="button" class="btn green btn-sm loading-btn" id="modify">
															<i class="fa fa-edit"></i>&nbsp;수정
														</button>
														<button type="button" class="btn red btn-sm loading-btn" id="delete" style="display:none;">
															<i class="fa fa-trash"></i>&nbsp;삭제
														</button>
														<button type="button" class="btn red btn-sm loading-btn" id="end" style="display:none;">
															<i class="fa fa-ban"></i>&nbsp;종료
														</button>
													</div>
												</div>
											</form>
										</div>
										<!-- END ADD FORM TABLE-->
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-reorder"></i> <span class="caption-title">
													차감정산 일정 </span>
											</div>
										</div>
										<div class="table-scrollable">
										<!-- 스케쥴 시작 -->
											<table class="pg-table table table-bordered table-hover flip-content">
												<!-- table-bordered -->
												<thead>
													<tr>
														<th>No.</th>
														<th>아이디</th>
														<th>차감정산예정일</th>
														<th>차감금액</th>
														<th>정산여부</th>
														<th>정산번호</th>
														<th>비고</th>
													</tr>
												</thead>
												<tbody id="list">
													<c:forEach var="entry" items="${SCHELIST}" varStatus="status">
													<tr>
														<td>${status.count}</td>
														<td class="link_modal" data-url="/mcht/ddct/change/${entry.scheId}">${entry.scheId}</td>
														<td class="date">${entry.stlDay}</td>
														<td class="text-right"><fmt:formatNumber type="number" value="${entry.ddctAmt}" pattern="#,##0" /></td>
														<td class="text-right">${entry.stlStatus}</td>
														<td class="text-right">${entry.stlId}</td>
														<td class="text-right">${entry.summary}</td>
													</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
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
	var setStartDate = '${SETDATE.startDate}';
		$(document).ready(function() {
			
			if(parseInt(${var1}) > 0){
				$('#type').prop('disabled',true);
				$('#type').attr('readonly',true);
				$('.selectpicker[data-id="type"]').addClass('disabled');
				$('#startMonth').attr("readonly",true);
				$('#startMonth').datepicker("remove");
				$('#endMonth').datepicker("setStartDate",new Date(parseInt(setStartDate.substring(0,4)),parseInt(setStartDate.substring(4,6))-1));
				$('#end').show();
			}else{
				$('#delete').show();
			}
			
		});
	
		var startMonth = '${DATAMAP.startMonth}';
		var endMonth = '${DATAMAP.endMonth}';
		$(document).on('click', '#modify',function() {
			var form1 = $('#writeFrm');
			var error1 = $('.alert-danger', form1);
			if($('#writeFrm [name="monthlyAmt"]').val() == '' || $('#writeFrm [name="startMonth"]').val() == '' || $('#writeFrm [name="endMonth"]').val() == ''){
				var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
                return false;
			}
	        bootbox.confirm("차감정산 내용을 수정하시겠습니까?",function(result){
	        	if(result){
	        		error1.hide();
	   				$.ajax({
	   					url:'/mcht/ddct/update',
	   					type:'POST',
	   					data: $('#writeFrm').serialize(),
	   					contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
	   					dataType: 'json',
	   					success: function(result){
	   					if(result.resultCd == '0000'){
	   						bootbox.alert('차감정산 내용 수정이 완료되었습니다.',function(){
	   							location.reload(true);
	   						});
	   					}else{
	   						bootbox.alert('차감정산 수정에 실패하였습니다. 관리자에게 문의 바랍니다.');
	   					}
	   				}
	   			});
	   		}
	   	  });
	   });
	
		$('#delete').click(function(){
			bootbox.confirm("해당 차감정산을 삭제하시겠습니까?",function(result){
				if(result){
					$.ajax({
						url:'/mcht/ddct/delete',
						type:'POST',
						data: $('#writeFrm').serialize(),
						contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
						dataType: 'json',
						success: function(result){
							if(result.resultCd == '0000'){
								bootbox.alert('차감정산이 삭제되었습니다.',function(){
									location.href = '/mcht/view/${DATAMAP.mchtId}/tab_ddct';
								});
								
							}else{
								bootbox.alert('차감정산 삭제에 실패하였습니다. 관리자에게 문의 바랍니다.');
							}
						}
					});
				}
			});
		});
		$('#end').click(function(){
			var $modal = $('#pgmate-modal');
			$modal.addClass('modal-sm');
			if ($modal.children().length < 1) {
				$modal.empty();
			}
			 
			$modal.load('/mcht/ddct/end.jsp', '', function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					$('.selectpicker').selectpicker();
					$('#endDate').datepicker({
						format: 'yyyy-mm-dd'
					});
					$('#endDate').datepicker('setDate', new Date);
					$modal.modal();
					textMask();
				}
			});
		});
		
		$(document).on('click', '#ddctClose',function() {
			bootbox.confirm("차감정산을 종료 하시겠습니까?",function(result){
				if(result){
					$('#form1 [name="ddctId"]').val('${DATAMAP.ddctId }');
					$('#form1 [name="mchtId"]').val('${DATAMAP.mchtId }');
					$('#form1 [name="type"]').val('${DATAMAP.type }');
					$.ajax({
						url:'/mcht/ddct/close',
						type:'POST',
						data: $('#form1').serialize(),
						contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
						dataType: 'json',
						success: function(result){
							if(result.resultCd == '0000'){
								bootbox.alert('차감정산 종료가 완료되었습니다.',function (){
									location.reload(true);
								});
								
							}else{
								bootbox.alert('차감정산 종료에 실패하였습니다. 관리자에게 문의 바랍니다.');
							}
						}
					});
				}
			});
		});
		
		$(document).on('click', '#ddctChange',function() {
			bootbox.confirm("차감정산 일정 수정을 진행 하시겠습니까?",function(result){
				if(result){
					$.ajax({
						url:'/mcht/ddct/scheUpdate',
						type:'POST',
						data: $('#form2').serialize(),
						contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
						dataType: 'json',
						success: function(result){
							if(result.resultCd == '0000'){
								bootbox.alert('차감정산 일정 개별수정이 완료되었습니다.',function (){
									location.reload(true);
								});
								
							}else{
								bootbox.alert('차감정산 일정 수정에 실패하였습니다.');
							}
						}
					});
				}
			});
		});
		
		$(".datepicker").datepicker({
			format : "yyyymm",
			viewMode : "months",
			minViewMode : "months",
			locale : 'ko',
			autoclose : true
		});	
		
		$('#startMonth').datepicker('setDate',new Date(parseInt(startMonth.substring(0,4)),parseInt(startMonth.substring(4,6))-1));
		//$('#endMonth').datepicker('setDate',new Date(parseInt(endMonth.substring(0,4)),parseInt(endMonth.substring(4,6))-1));
		
		//정기 차감 종료월 수정
		$('#endMonth').datepicker('setStartDate',new Date(parseInt(startMonth.substring(0,4)),parseInt(startMonth.substring(4,6))-1));
		
		$('#endMonth').datepicker().on('show', function(e){
			$('#endMonth').datepicker('setDate',new Date(parseInt(endMonth.substring(0,4)),parseInt(endMonth.substring(4,6))-1));
		});
	
		$('#startMonth').datepicker().on('changeDate', function (selected){
			var startDate = new Date(selected.date.valueOf());
			$('#endMonth').datepicker('setStartDate', startDate);
		});
		
   		$('.monthlyAmt').val(addComma(String($('.monthlyAmt').val()).replace(/[^0-9]/g,"")));
		
   		$('.monthlyAmt').keyup(function(){
			$('.monthlyAmt').val(addComma(String($('.monthlyAmt').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
   		
   		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		$(".loading-btn").click(function(){
			$(".monthlyAmt").val($(".monthlyAmt").val().replace(/,/g, ''));
		});
		
		$('#nav-mcht').addClass('active');
		
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>