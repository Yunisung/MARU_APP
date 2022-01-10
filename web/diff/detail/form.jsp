<%@page contentType="text/html; charset=UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%> 
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<c:import url="/include/head.jsp" />	
</head>
<!-- END HEAD -->
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
	<div class="page-wrapper">
		<c:import url="/include/header.jsp" />
		<!-- BEGIN CONTAINER -->
		<div class="page-container">
			<c:import url="/include/nav.jsp" />
			<!-- BEGIN CONTENT -->
			<div class="page-content-wrapper">
				<div class="page-content">
					<div class="mtouch-container">
						 <!-- BEGIN PAGE BAR -->
						<div class="page-bar">
							<ul class="page-breadcrumb">
									<li><a href="/">Home</a><i class="fa fa-circle"></i></li>
									<li><span>영중소 차액정산</span><i class="fa fa-circle"></i></li>
									<li><span>차액입금 상세 내역</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">TEXT</div>
										</div>
									</div>
							</div>
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/diff/detail/list" method="post">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금정산 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm " name="collectId" data-oper="eq" value="${COLLECT_MAP.collectId }" readonly>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금대상 분류</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm " name="category" data-oper="eq" value="${COLLECT_MAP.category }" readonly>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금대상</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm " name="categoryId" data-oper="eq" value="${COLLECT_MAP.categoryId}" readonly>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금일</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="collectDay" data-oper="eq" value="${COLLECT_MAP.collectDay }" readonly>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금금액</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="collectAmount" data-oper="eq" value="<fmt:formatNumber type="number" value="${COLLECT_MAP.collectAmount}" pattern="#,##0" />" readonly>
															</div>
														</div>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult">
										
									</div>
									<!-- 내용 폼 종료 -->
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				<!-- BEGIN CONTAINER -->
		</div>
		<iframe id="txtArea1" style="display:none"></iframe>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ searchForList(calcAmt); }, 100); //검색 실행
		$('#nav-diff').addClass('active');
		
		function calcAmt(){
			$('#real-def').text('<fmt:formatNumber type="number" value="${COLLECT_MAP.collectAmount}" pattern="#,##0" />');
			
			$('.deductAmount').each(function() {
				var res = $(this).attr('data-amt');
				if(res == 0) {
					$(this).parent().find('.overAmount').text('');
					$(this).parent().find('.overAmount').attr('data-amt', 0);
					$(this).parent().find('.lowAmount').text('');
					$(this).parent().find('.lowAmount').attr('data-amt', 0);
				} else if(res < 0) {
					$(this).parent().find('.lowAmount').text(toNum(res));
					$(this).parent().find('.lowAmount').attr('data-amt', res);

					$(this).parent().find('.overAmount').text('');
					$(this).parent().find('.overAmount').attr('data-amt', 0);
				} else {
					$(this).parent().find('.overAmount').text(toNum(res));
					$(this).parent().find('.overAmount').attr('data-amt', res);

					$(this).parent().find('.lowAmount').text('');
					$(this).parent().find('.lowAmount').attr('data-amt', 0);
				}
			});

			$('.result-amt').each(function() {
				var amt2 = $(this).parent().find('.collect-input').val();
				var amt3 = $(this).parent().find('.deduct-input').val();
				var res = Number(amt2) + Number(amt3);
				
				$(this).text(toNum(res));
				$(this).attr('data-amt', res);
			});
			
			
			$('#amount-sum').text('0');
			$('.amount').each(function() {
				$('#amount-sum').text(Number($('#amount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#amount-sum').text(toNum($('#amount-sum').text()));
			
			$('#diffAmount-sum').text('0');
			$('.diffAmount').each(function() {
				$('#diffAmount-sum').text(Number($('#diffAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#diffAmount-sum').text(toNum($('#diffAmount-sum').text()));
			
			$('#diffVanAmount-sum').text('0');
			$('.diffVanAmount').each(function() {
				$('#diffVanAmount-sum').text(Number($('#diffVanAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#diffVanAmount-sum').text(toNum($('#diffVanAmount-sum').text()));
			
			$('#diffSettleAmount-sum').text('0');
			$('.diffSettleAmount').each(function() {
				$('#diffSettleAmount-sum').text(Number($('#diffSettleAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#diffSettleAmount-sum').text(toNum($('#diffSettleAmount-sum').text()));
			
			
			$('#collectAmount-sum').text('0');
			$('.collectAmount').each(function() {
				$('#collectAmount-sum').text(Number($('#collectAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#collectAmount-sum').text(toNum($('#collectAmount-sum').text()));

			$('#deductAmount-sum').text('0');
			$('.deductAmount').each(function() {
				$('#deductAmount-sum').text(Number($('#deductAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#deductAmount-sum').text(toNum($('#deductAmount-sum').text()));

			$('#lowAmount-sum').text('0');
			$('.lowAmount').each(function() {
				$('#lowAmount-sum').text(Number($('#lowAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#lowAmount-sum').text(toNum($('#lowAmount-sum').text()));

			$('#overAmount-sum').text('0');
			$('.overAmount').each(function() {
				$('#overAmount-sum').text(Number($('#overAmount-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#overAmount-sum').text(toNum($('#overAmount-sum').text()));

			$('#real-sum').text(toNum($('#real-sum').text()));
			if($('#collect-sum').text() != $('#real-sum').text()) {
				$('#real-sum').addClass('font-red');
			}
		}
			
		$('#searchResult').on('keyup', '.collect-input, .deduct-input', function(){
			$(this).val($(this).val().replace(/[^0-9,-]/g,""));
		}); 
		
		$('#searchResult').on('change', '.collect-input, .deduct-input', function() {
			calcAmt();
		});
		
		function toNum(x) {
		    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		function trxCapCollectPop(url) {
			var w = '1500';
			var h = '950';
			window.open(url, "", "width="+w+", height="+h+", scrollbars=1");
		}

	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>