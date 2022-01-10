<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
		<h4 class="modal-title">차감금액 입력</h4>
	</div>
	<div class="modal-body">
		<form class="form-horizontal" role="form" id="minusAmtForm" action="#">
			<div class="form-group">
				<label for="lblMinusAmount" class="col-md-2 control-label">차감금액</label>
				<div class="col-md-6">
					<div class="input-group input-group-sm">
						<input type="text" name="minusAmt" class="form-control input-sm" id="minusAmt" value="" maxlength="19"> 
						<span class="input-group-addon"><i class="fa fa-krw"></i></span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="lsummary" class="col-md-2 control-label">차감금액 메모</label>
				<div class="col-md-8">
					<textarea rows="4" name="minusAmtMemo" id="minusAmtMemo" placeholder="" maxlength="100" value="" class="form-control input-md"></textarea>
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
	$('input[name="minusAmt"]').keyup(function(){
		$('input[name="minusAmt"]').val(addComma(String($('input[name="minusAmt"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	});

	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	function input() {
		
		var minusAmt = $('#minusAmtForm input[name="minusAmt"]').val().replace(/,/g, '');
		var minusAmtMemo = $('#minusAmtForm textarea[name="minusAmtMemo"]').val();
		var payOutAmount = $('#minusAmtForm input[name="payOutAmount"]').val();
		var flag = $('#minusAmtForm input[name="input-flag"]').val();
		var newAmount = Number(payOutAmount) - Number(minusAmt);
		
		if(!isEmpty(minusAmt)) {
			if(!isEmpty(minusAmtMemo)) {
				if(Number(newAmount) < 0) {
					bootbox.alert("출금예정금액보다 차감입금액이 큽니다.");
				}else {
					bootbox.confirm("차감금액을 반영 하시겠습니까?", function(result) {
						if (result) {
							$('#minusAmtForm input[name="input-flag"]').val("true");
							$('#minusAmtForm input[name="payOutAmount"]').val(newAmount);
							$('#pgmate-modal').modal('hide');
						}
					});
				}
			}else {
				bootbox.alert("차감금액 메모 입력해야 합니다.");
			}
		} else {
			bootbox.alert("차감금액을 입력해야 합니다.");
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