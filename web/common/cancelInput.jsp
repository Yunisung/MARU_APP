<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
		<h4 class="modal-title">취소정보 입력</h4>
	</div>
	<div class="modal-body">
		<form class="form-horizontal" role="form" id="modalForm" action="#">
			<div class="form-group">
				<label for="lblCancelAmount" class="col-md-2 control-label">취소 입금금액</label>
				<div class="col-md-6">
					<div class="input-group input-group-sm">
						<input type="text" name="cancelAmount" class="form-control input-sm" id="cancelAmount" value="" maxlength="15"> 
						<span class="input-group-addon"><i class="fa fa-krw"></i></span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="lsummary" class="col-md-2 control-label">취소 메모</label>
				<div class="col-md-8">
					<textarea rows="4" name="cancelMemo" id="cancelMemo" placeholder="" maxlength="100" value="" class="form-control input-md"></textarea>
				</div>
				<input type="hidden" class="input-flag" name="input-flag" value="false" />
				<input type="hidden" class="payOutAmount" name="payOutAmount" value="" />
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-sm gray" data-dismiss="modal"><i class="fa fa-close"></i> 취소</button>
		<button type="button" class="btn btn-sm blue-dark" onClick="input()"><i class="fa fa-pencil"></i> 입력</button>
	</div>
</div>

<script>
	$('input[name="cancelAmount"]').keyup(function(){
		$('input[name="cancelAmount"]').val(addComma(String($('input[name="cancelAmount"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	});
	
	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	
	function input() {
		var cancelAmount = $('#modalForm input[name="cancelAmount"]').val().replace(/,/g, '');
		var cancelMemo = $('#modalForm textarea[name="cancelMemo"]').val();
		var payOutAmount = $('#modalForm input[name="payOutAmount"]').val();
		var flag = $('#modalForm input[name="input-flag"]').val();
		
		if(!isEmpty(cancelAmount)) {
			if(!isEmpty(cancelMemo)) {
				if(Number(payOutAmount) < Number(cancelAmount)) {
					bootbox.alert("결제금액보다 취소입금액이 큽니다.");
				}else {
					bootbox.confirm("취소내역을 반영 하시겠습니까?", function(result) {
						if (result) {
							$('#modalForm input[name="input-flag"]').val("true");
							$('#pgmate-modal').modal('hide');
						}
					});
				}
			}else {
				bootbox.alert("취소 메모 입력해야 합니다.");
			}
		} else {
			bootbox.alert("취소 입금금액을 입력해야 합니다.");
		}
	}
	
	function isEmpty(str){
	    if(typeof str == "undefined" || str == null || str.trim() == "")
	        return true;
	    else
	        return false ;
	}
</script>
<!-- /.modal-content -->
<!-- /.modal-dialog -->