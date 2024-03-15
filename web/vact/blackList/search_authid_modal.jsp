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
		<h4>인증ID로 조회</h4>
	</div>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<div class="portlet light portlet-form">
			<%--<div class="portlet-title"></div>--%>
			<div class="portlet-body form light">
				<div class="form-body row">
					<div class="form-group col-sm-offset-2">
						<label class="control-label input-sm col-sm-2 req-label">인증ID</label>
						<div class="col-sm-4">
							<input type="text" class="form-control input-sm authId" id="authId" value="" >
						</div>
						<div class="col-sm-2">
							<a class="btn btn-sm green" href="javascript:getWithdrawAccount()">출금계좌조회</a>
						</div>
					</div>
					<div class="form-group col-sm-offset-2">
						<div class="col-sm-10">
							<p class="form-control-static" id="resultMsg">&nbsp;</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END FORM-->
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
	<script type="text/javascript">
		function getWithdrawAccount() {
			var authId = document.getElementById('authId').value;
			if(authId === '') {
				bootbox.alert("인증ID를 입력해주세요.");
				return;
			}

			$.ajax({
				type: "GET",
				url: "/vact/withdrawAccountByAuthId/" + authId,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function (res) {
					//console.log(res);
					const msg = res.msg;
					const withdrawAccount = res.withdrawAccount;
					const withdrawBankCd = res.withdrawBankCd;
					const withdrawBankNm = res.withdrawBankNm;
					const holderName = res.holderName;
					if (res.result == 'OK') {
						document.getElementById('resultMsg').innerText = "계좌정보: " + msg;
						bootbox.confirm(msg + " 정보를 등록하시겠습니까?", function(result) {
							if(result) {
								document.getElementById('account').value = withdrawAccount;
								document.getElementById('holderName').value = holderName;
								document.getElementById('identity').value = identity;
								document.getElementById('bankName').value = withdrawBankNm;
								$('#bankCd').val(withdrawBankCd);
								$('.selectpicker').selectpicker('refresh');
								$('#pgmate-modal').modal('hide');
							}
						});
					} else {
						bootbox.alert(msg);
					}
				},
				error: function (xhr, textStatus, errorThrown) {
					bootbox.alert('Error ' + errorThrown);
				}
			});
		}
	</script>
</body>
</html>