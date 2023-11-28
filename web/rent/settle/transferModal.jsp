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
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h4>분납이체</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">이체상세정보</a>
			</li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab1">
				<div class="portlet light portlet-form">
					<div class="portlet-body form light">
				<!-- BEGIN FORM-->
				<form class="form-horizontal" role="form" id="transForm" action="#">
						<!--/span-->
						<div class="form-group">
							<label class="col-md-2 control-label">예약구분</label>
							<div class="col-md-8">
								<select name="transferType" id="transferType" style="width:100px;height:30px;" onchange="changeType()">
									<option value="실시간">실시간</option>
									<option value="예약" selected>예약</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">이체일자</label>
							<div class="col-md-4">
								<input type="text" name="pubDay" class="form-control" id="pubDay" value="" placeholder="yyyyMMdd" maxlength="8"> </div>
						</div>
					<div class="form-group">
						<label class="col-md-2 control-label">이체시간</label>
						<div class="col-md-4">
							<input type="text" name="pubTime" class="form-control" id="pubTime" value="" placeholder="hhmmss" maxlength="6"> </div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">금액</label>
						<div class="col-md-4">
							<input type="text" name="amount" class="form-control" id="amount" value="${AMOUNT}" data-oper="comma" maxlength="10" value="0"> </div>
					</div>

					<div class="form-group">
						<div class="col-md-offset-2 col-md-10">
							<button class="btn purple" type="button" id="transferButton">실행</button>
						</div>
					</div>
				</form>
				<!-- END FORM-->
				</div>
			</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
	<script>
		function retryHook() {
			bootbox.confirm('해당 거래를 재전송 하시겠습니까?', function(result) {
				if (result) {
					$.ajax({
						type: "GET",
						url: "/chargeSettle/retry/${DATAMAP.trxId}",
						success: function (res) {
							if(res.result == 'OK') {
								bootbox.alert('재전송 요청에 성공했습니다.');
							} else {
								bootbox.alert(res.msg);
							}
						},
						error: function (res, status) {
							bootbox.alert('재전송 요청에 실패했습니다.' + status);
						}
					});
				}
			});
		};

		function changeType(){
			var selected = $('#transferType option:selected').val();

			if(selected == '실시간') {
				// disabled 처리
				$("#pubDay").attr("disabled",true);
				$("#pubTime").attr("disabled",true);
			} else {
				// disabled 삭제
				$("#pubDay").removeAttr("disabled");
				$("#pubTime").removeAttr("disabled");
			}
		}

		$('#transferButton').on(
				'click',
				function () {
					var flag = false;
					if($('#transferType option:selected').val() === '실시간'){
						if($("#amount").val() != ${AMOUNT}) {
							bootbox.alert('금액을 확인하여 주시기 바랍니다.');
						} else {
							flag = true;
						}
					} else {
						if ($('#pubDay').val().length != 8) {
							bootbox.alert('이체예정일자를 정확히 입력하여 주시기 바랍니다. ');
						} else if ($('#pubTime').val().length != 6) {
							bootbox.alert('이체예정시간을 정확히 입력하여 주시기 바랍니다.');
						} else if ($("#amount").val() != ${AMOUNT}) {
							bootbox.alert('금액을 확인하여 주시기 바랍니다.');
						} else {
							flag = true;
						}
					}

					if(flag) {
						$.ajax({
							type: "post",
							url: "/rent/settle/retry/${trxId}",
							data: $("#transForm").serialize(),
							success: function (json, textStatus) {
								bootbox.alert(json);

							},
							error: function (xhr, textStatus, errorThrown) {
								bootbox.alert('Error ' + errorThrown);
							}
						});
					}
				});
	</script>
</body>
</html>