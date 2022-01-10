<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<style type="text/css">
		.form-subtitle {
			padding-top: 10px;
			padding-left: 38px;
			font-size: 14px;
		}
		.form-subtitle i {
			margin-right: 10px;
		}
	</style>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h4>발송 내역 상세정보 (${DATAMAP.name})</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">발송 내역</a>
			</li>
			<li>
				<a href="#tab2" data-toggle="tab">처리 결과</a>
			</li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab1">
				<!-- BEGIN FORM-->
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<div class="form-group col-sm-12 form-subtitle">
							<label>
								<i class="fa fa-reorder"></i>기본 정보</label>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>발송번호</label>
								<div class='col-md-8'>
									<p class='form-control-static digits'>${DATAMAP.idx}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>발송 영업사 아이디</label>
								<div class='col-md-8'>
	                                <p class='form-control-static digits'>${DATAMAP.id}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>발송 영업사</label>
								<div class='col-md-8'>
	                            	<p class='form-control-static digits'>${DATAMAP.name}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="form-group col-sm-12 form-subtitle">
							<label>
								<i class="fa fa-reorder"></i>전자계약서</label>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>전자계약 상태</label>
								<div class='col-md-8'>
									<p class='form-control-static'>
										<c:choose>
											<c:when test="${DATAMAP.doc_status eq 'WSI'}">휴대폰본인인증</c:when>
											<c:when test="${DATAMAP.doc_status eq 'WR'}">작성요청</c:when>
											<c:when test="${DATAMAP.doc_status eq 'WS'}">작성시작</c:when>
											<c:when test="${DATAMAP.doc_status eq 'WE'}">작성완료</c:when>
											<c:when test="${DATAMAP.doc_status eq 'WJ'}">작성거절</c:when>
											<c:when test="${DATAMAP.doc_status eq 'WX'}">작성기한만료</c:when>
											<c:when test="${DATAMAP.doc_status eq 'WC'}">전송취소</c:when>
										</c:choose>
									</p>
								</div>
							</div>
						</div>
						<!--/span-->
					</div>
				</form> 
			</div>
			<div class="tab-pane" id="tab2">
				<div class="portlet light portlet-form">
					<div class="portlet-body form light">
						<br/>
						<form class="form-horizontal" role="form" data-form="true" id="writeFrm" name="form" action="/eform/auth/" method="post">
							<input type="hidden" name="action_type" value="addResult" data-reg="false" />
							<input type="hidden" name="idx" data-key="true" value="${DATAMAP.idx }"/>
							<div class="form-group">
								<label for="doc_result" class="col-md-2 control-label">처리 결과<br>(대기, 처리중, 완료, 반려)</label>
								<div class="col-md-4">
									<input type="text" name="doc_result" class="form-control" id="doc_result" value="${DATAMAP.doc_result}" onchange="changeRes(this)">
								</div>
							</div>
							<div class="form-group">
								<label for="doc_ref" class="col-md-2 control-label">비고</label>
								<div class="col-md-4">
									<input type="text" name="doc_ref" class="form-control" id="doc_ref" value="${DATAMAP.doc_ref}" placeholder="비고/등록자">
								</div>
							</div>
							<div class="form-group">
								<label for="tmnId" class="col-md-2 control-label">TID 입력</label>
								<div class="col-md-4" id="addTmnId">
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-offset-2 col-md-10">
									<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
											<i class="fa fa-search"></i>&nbsp;저장
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>

	<script>
	var form1 = $('#writeFrm');
	var error1 = $('.alert-danger', form1);
	form1.validate({
		rules : {
			doc_result : {
				required: true
			},
			doc_ref : {
				required: true
			},
			tmnId : {
				required: true
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
			bootbox.confirm("입력하신 정보로 계약서 처리 결과를 수정 하시겠습니까?", function(result) {
				if (result) {
					ajaxFormSubmit(form, '/eform/form.jsp'); //PAGE 이동		
				}
			});
		}
	});
	
	var ckResult = $("input[name=doc_result]").val();
	var form = document.getElementById("addTmnId");
	var count = 0;
	
	if(ckResult != "완료"){
		$("#tmnId").remove();
	}else if(ckResult == "완료"){
		form.innerHTML+="<input type='text' name='tmnId' class='form-control' value='${DATAMAP.tmnId}' id='tmnId'>";
		count++;
	}
	
	function changeRes(obj){
		var chRes = $("input[name=doc_result]").val();
		
		if(chRes != "완료"){
			$("#tmnId").remove();
		}else if(chRes == "완료"){
			form.innerHTML+="<input type='text' name='tmnId' class='form-control' id='tmnId' placeholder='터미널 아이디'>";
			count++;
		}
	}
	
	if(count > 1){
		count = 0;
	}
	</script>
</body>

</html>