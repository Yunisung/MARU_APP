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

</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h4>차감정산 일정 개별 수정</h4>
	</div>
	<div class="modal-body form">
		<form role="form" class="form-horizontal form" id="form2">
			<div class="form-body row">
				<input type="hidden" name="ddctId" value="${DATAMAP.ddctId }"/>
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">일정아이디</label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm" name="scheId" value="${DATAMAP.scheId }" readonly="readonly">
					</div>
				</div>
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">차감정산예정일</label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm" maxlength="10" name="stlDay" value="${DATAMAP.stlDay }">
					</div>
				</div>
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">차감 금액</label>
					<div class="col-sm-6">
						<div class="input-group input-group-sm">
							<input type="text" class="form-control currency ddctAmt" maxlength="13" name="ddctAmt" placeholder="" value="${DATAMAP.ddctAmt }"> 
							<span class="input-group-addon"><i class="fa fa-krw"></i></span>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
		<c:if test="${DATAMAP.stlStatus eq '정산대기' }">
			<button id="ddctChange" class="btn btn-success" type="button">차감정산 일정수정</button>
		</c:if>
	</div>
	
	<script>
		$('.ddctAmt').val(addComma(String($('.ddctAmt').val()).replace(/[^0-9]/g,"")));
		
		$('.ddctAmt').keyup(function(){
			$('.ddctAmt').val(addComma(String($('.ddctAmt').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
			
		function addComma(data) {
	    	return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	
		$(".btn-success").click(function(){
			$(".ddctAmt").val($(".ddctAmt").val().replace(/,/g, ''));
		});
	</script>
</body>
</html>