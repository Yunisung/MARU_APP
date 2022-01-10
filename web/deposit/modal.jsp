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
		<h4>${DATAMAP.name} 예수금 수기 입력</h4>
	</div>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<h4>${DATAMAP.trxDay}</h4>
		<br />
		<form class="form-horizontal" role="form" id="depositAddForm" action="#">
			<div class="form-group">
				<label for="amount" class="col-md-2 control-label">구분</label>
				<div class="col-md-4">
				<select name="depType" class="form-control">
					<option value="반환요청">반환(정산 예수금으로 반영)</option>
					<option value="수기반환">반환(정산 미반영)</option>
					<option value="보류">보류금액 추가</option>
					<option value="정산차감">정산차감(보류금액과 별도)</option>
				</select>
				</div>
			</div>
			<div class="form-group">
				<input type="hidden" name="mchtId" value="${DATAMAP.mchtId}">
				<label for="amount" class="col-md-2 control-label">금액</label>
				<div class="col-md-4">
					<input type="text" name="amount" class="form-control" value="0">
				</div>
			</div>
			<div class="form-group">
				<label for="regDay" class="col-md-2 control-label">일자 지정</label>
				<div class="col-md-4">
					<input class="datepicker form-control" name="regDay">
				</div>
			</div>
			<div class="form-group">
				<label for="summary" class="col-md-2 control-label">변경이력 </label>
				<div class="col-md-8">
					<textarea rows="4" name="summary" id="summary" placeholder="Write comment here ..." class="form-control input-md"></textarea>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-md-offset-2 col-md-10">
					<button class="btn purple" type="button" onClick="depositAdd()">내역 생성
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
	
	$('input[name="amount"]').keyup(function(){
		$('input[name="amount"]').val(addComma(String($('input[name="amount"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	});
	
	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	
	$(document).ready(function() {
		
		$('input[name="regDay"]').datepicker({
			format: 'yyyy-mm-dd',
		});
		$('.datepicker').datepicker('update', new Date());
	});
	
	
	function depositAdd() {
		
		var amount = String($('#depositAddForm').find('input[name="amount"]').val()).replace(/,/g, '');
		if(amount <= 0) {
			bootbox.alert('금액을 입력해야 합니다.');
			return;
		}

		var summary = $('#depositAddForm').find('textarea[name="summary"]').val();
		if(summary <= 0) {
			bootbox.alert('사유를 입력해야 합니다.');
			return;
		}
		
		var depType = $('#depositAddForm').find('select[name="depType"]').val();
		var msg = depType == '반환요청' ? '예수금 반환내역을 추가하시겠습니까?' : '보류내역을 추가하시겠습니까?';
		
		$('#depositAddForm').find('input[name="amount"]').val(String($('input[name="amount"]').val()).replace(/,/g, ''));
		
		bootbox.confirm(msg, function(result) {
			if(result) {
				depType, amount, summary
				
				$.ajax({
					url: '/deposit/insert',
					method:'post',
					data: $('#depositAddForm').serialize(),
					success: function(res) {
			    	if(res.result == 'OK') {
							bootbox.alert("예수금 내역 반영에 성공하였습니다.",function() {
								location.reload();
							});
			      } else {
			        bootbox.alert("예수금 내역 반영에 실패하였습니다.\n" + res.msg);
			      }
			    },
			    error : function(xhr, status, error) {
			    	bootbox.alert("예수금 내역 반영에 실패하였습니다.");
			    }
				});
			}
		});
		
	}
</script>
</body>
</html>