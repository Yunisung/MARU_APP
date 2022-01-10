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
<head></head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h4>차감금액 관리자 설정</h4>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<h4>${DATAMAP.trxDay}</h4>
		<br />
		<form class="form-horizontal" role="form" id="changeDeduct" action="#">
			<div class="form-group">
				<label for="amount" class="col-md-2 control-label">차감 금액</label>
				<div class="col-md-4">
					<input type="text" name="amount" class="form-control" id="modal-deduct-deductAmt">
				</div>
			</div>
			<div class="form-group">
				<label for="regDay" class="col-md-2 control-label">정산번호</label>
				<div class="col-md-4">
					<input class="datepicker form-control" name="stlId" id="modal-deduct-stlId">
				</div>
			</div>
			<div class="form-group">
				<label for="lsummary" class="col-md-2 control-label">변경이력<br>(최대 150자)</label>
				<div class="col-md-8">
					<textarea rows="4" name="summary" placeholder="최대 150자 까지 입력 가능합니다" class="form-control input-md"></textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-offset-2 col-md-10">
					<button class="btn purple" type="button" onClick="changeDeduct()">변경
					</button>
				</div>
			</div>
		</form>
		<!-- END FORM-->
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
	
	<script type="text/javascript">
	
	$(document).ready(function() {
		
	});

	$("input[name='amount']").keyup(function(){
		$("input[name='amount']").val(addComma(String($("input[name='amount']").val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});

	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	
	function changeDeduct() {
    	
		var amount = $('#changeDeduct').find('input[name="amount"]').val().replace(/,/g, '');
    	
		if(amount > 0) {
			bootbox.alert('0 미만의 금액을 입력해야 합니다.');
			return;
		}
		
    	$('input[name="amount"]').val(amount.replace(/,/g, ''));
    	
		var stlId = $('#changeDeduct').find('input[name="stlId"]').val();
		if(!stlId) {
			bootbox.alert('정산번호 오류');
			return;
		}
		
		var summary = $('#changeDeduct').find('textarea[name="summary"]').val();
		if(!summary) {
			bootbox.alert('사유를 입력해야 합니다.');
			return;
		}
		
		bootbox.confirm("차감금액을 변경하시겠습니까?", function(result) {
			if(result) {
				$.ajax({
					url: '/settle/mcht/change/deduct',
					method:'post',
					data: $('#changeDeduct').serialize(),
					success: function(res) {
			    	if(res.result == 'OK') {
							bootbox.alert("차감 금액 설정에 성공하였습니다.",function() {
								location.reload();
							});
			      } else {
			        bootbox.alert("차감 금액 설정에 실패하였습니다.\n" + res.msg);
			      }
			    },
			    error : function(xhr, status, error) {
			    	bootbox.alert("차감 금액 설정에 실패하였습니다.");
			    }
				});
			}
		});
		
	}
	</script>
</body>
</html>