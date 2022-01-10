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
<c:import url="/include/head.jsp" />
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-wrapper">
		<div class="page-wrapper-row full-height">
			<div class="page-wrapper-middle">
				<!-- BEGIN CONTAINER -->
				<div class="page-container">
					<div class="page-content-wrapper">
						<div class="page-content">
							<div class="mtouch-container">
								<!-- BEGIN PAGE CONTENT INNER -->
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-bell-o"></i>
												<span class="caption-title"> SMS 메시지 전송</span>
											</div>
											<div class="portlet-body light">
												<!-- BEGIN FORM-->
												<form class="form-horizontal" role="form" data-form="true" name="form" action="http://www.trustmate.net/sms.jsp" method="post" style="margin: 10px;">
													<div class="form-body row">
														<div class="form-group col-sm-6">
															<div class="col-sm-12">
																<textarea style="font-size: 14px;" rows="3" cols="40" class="form-control input-sm" name="message" maxlength="80" placeholder="메시지 내용을 입력하세요." id="smstext"></textarea>
															</div>
															<div class="col-sm-12" style="text-align: right;">
																<span id="limit-text">0</span> / 80
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4">받는 사람</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm " maxlength="13" name="telno" value="010">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4">보내는 사람</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm " maxlength="13" name="callback" value="0269599632">
															</div>
														</div>
													</div>
													<div class="alert alert-danger display-hide"></div>
													<div class="form-actions right">
														<div class="">
															<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
																<i class="fa fa-search"></i>&nbsp;전송
															</button>
														</div>
													</div>
												</form>
											</div>
											<!-- END FORM-->
										</div>
									</div>
								</div>
							</div>
							<!-- END PAGE CONTENT INNER -->
						</div>
					</div>
				</div>
			</div>
			<!-- BEGIN CONTAINER -->
		</div>
	</div>
	<c:import url="/include/javascript.jsp" />
	<script>
	$('#smstext').keyup(function(e) {
		var limit = byteCheck($(this));
		$('#limit-text').text(limit);
		var val = $(this).val();
		if(limit > 80) {
			if(e.keyCode != 8 && e.keyCode != 9) {
				$(this).val(val.substring(0, val.length -2));
			}
		}
	})
	
	function byteCheck(el){
	    var codeByte = 0;
	    for (var idx = 0; idx < el.val().length; idx++) {
	        var oneChar = escape(el.val().charAt(idx));
	        if ( oneChar.length == 1 ) {
	            codeByte ++;
	        } else if (oneChar.indexOf("%u") != -1) {
	            codeByte += 2;
	        } else if (oneChar.indexOf("%") != -1) {
	            codeByte ++;
	        }
	    }
	    return codeByte;
	}
	</script>
</body>

</html>