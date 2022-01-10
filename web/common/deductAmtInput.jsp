<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
		<h4 class="modal-title">예수금 입력</h4>
	</div>
	<div class="modal-body">
		<form class="form-horizontal" role="form" id="deductAmtForm" action="#">
			<div class="form-group">
				<label for="lblDeductAmtAmount" class="col-md-2 control-label">예수금</label>
				<div class="col-md-6">
					<div class="input-group input-group-sm">
						<input type="text" name="deductAmt" class="form-control input-sm" id="deductAmt" value="" maxlength="19"> 
						<span class="input-group-addon"><i class="fa fa-krw"></i></span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="lsummary" class="col-md-2 control-label">예수금 메모</label>
				<div class="col-md-8">
					<textarea rows="4" name="deductAmtMemo" id="deductAmtMemo" placeholder="" maxlength="100" value="" class="form-control input-md"></textarea>
				</div>
				<input type="hidden" class="input-flag" name="input-flag" value="false" />
				<input type="hidden" class="payOutFee" name="payOutFee" value="" />
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
	$('input[name="deductAmt"]').keyup(function(){
		$('input[name="deductAmt"]').val(addComma(String($('input[name="deductAmt"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	});
	
	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	
	function input() {
		var deductAmt = $('#deductAmtForm input[name="deductAmt"]').val().replace(/,/g, '');
		var deductAmtMemo = $('#deductAmtForm textarea[name="deductAmtMemo"]').val();
		var payOutFee = $('#deductAmtForm input[name="payOutFee"]').val();
		var payOutAmount = $('#deductAmtForm input[name="payOutAmount"]').val();
		var flag = $('#deductAmtForm input[name="input-flag"]').val();
		var newAmount = Number(payOutAmount) - Number(payOutFee) + Number(deductAmt);
		
		if(!isEmpty(deductAmt)) {
			if(!isEmpty(deductAmtMemo)) {
				if(Number(newAmount) !=  Number(payOutFee*-1) && Number(newAmount) < 0) {
					bootbox.alert("실지급액이 '-' 금액이 됩니다.");
				}else {
					if(Number(newAmount) ==  Number(payOutFee*-1)) {
						newAmount = 0;
					}
					
					bootbox.confirm("예수금을 반영 하시겠습니까?", function(result) {
						if (result) {
							$('#deductAmtForm input[name="input-flag"]').val("true");
							$('#deductAmtForm input[name="payOutAmount"]').val(newAmount);
							$('#pgmate-modal').modal('hide');
						}
					});
				}	
			}else {
				bootbox.alert("예수금 메모 입력해야 합니다.");
			}
		} else {
			bootbox.alert("예수금을 입력해야 합니다.");
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