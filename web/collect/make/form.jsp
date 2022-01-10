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
									<li><span>입금정산</span><i class="fa fa-circle"></i></li>
									<li><span>입금 상세 내역</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/collect/make/list" method="post">
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
												<div class="form-actions nobg right">
													<div class="btn folding-search-btn icon-arrow-down"></div>
													<div class="">
														<c:if test="${CP_SESSION.role == '마스터' }">
														<button type="button" class="btn btn-sm red" onClick="collectDtlFinish();">
															<i class="fa fa-check" aria-hidden="true"></i> 입금 정산 마감
														</button>
														</c:if>
														
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
		$('#nav-collect').addClass('active');
		
		function calcAmt(){
			$('#real-def').text('<fmt:formatNumber type="number" value="${COLLECT_MAP.collectAmount}" pattern="#,##0" />');
			
			$('.diffrenceAmount').each(function() {
				var amt1 = $(this).parent().find('.out-col-calcAmount').attr('data-amt');
				var amt2 = $(this).parent().find('.collect-input').val();
				var res = Number(amt2) - amt1;
				
				$(this).text(toNum(res));
				$(this).attr('data-amt', res);

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

			
			$('#collect-sum').text('0');
			$('.out-col-calcAmount').each(function() {
				$('#collect-sum').text(Number($('#collect-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#collect-sum').text(toNum($('#collect-sum').text()));
			
			$('#real-sum').text('0');
			$('#real-sum').removeClass('font-red');
			$('.collect-input').each(function() {
				$('#real-sum').text(Number($('#real-sum').text()) + Number($(this).val()));
			});
			
			$('#real-sum').text(toNum($('#real-sum').text()));
			if($('#collect-sum').text() != $('#real-sum').text()) {
				$('#real-sum').addClass('font-red');
			}
			
			$('#diffrence-sum').text('0');
			$('.diffrenceAmount').each(function() {
				$('#diffrence-sum').text(Number($('#diffrence-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#diffrence-sum').text(toNum($('#diffrence-sum').text()));
			
			$('#low-sum').text('0');
			$('.lowAmount').each(function() {
				$('#low-sum').text(Number($('#low-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#low-sum').text(toNum($('#low-sum').text()));

			$('#over-sum').text('0');
			$('.overAmount').each(function() {
				$('#over-sum').text(Number($('#over-sum').text()) + Number($(this).attr('data-amt')));
			});
			$('#over-sum').text(toNum($('#over-sum').text()));
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

		function collectDtlMake(stlVanDay, mchtId, vanId) {
			var outColList = [];
			
			$('#searchResult tr').each(function() {
				if($(this).find('.out-col-mchtId').text() == '') {
					// EMPTY ROW
				} else if(mchtId == '' || ($(this).find('.out-col-mchtId').text() == mchtId && $(this).find('.out-col-vanId').text() == vanId)) {
					var outColObj = {};
					outColObj.mchtId = $(this).find('.out-col-mchtId').text();
					outColObj.vanId = $(this).find('.out-col-vanId').text();
					outColObj.collectId = $('input[name="collectId"]').val();
					outColObj.amount = $(this).find('.out-col-amount').text().replace(/,/gi, "");
					outColObj.stlVanFee = $(this).find('.out-col-stlVanFee').text().replace(/,/gi, "");
					outColObj.calcAmount = $(this).find('.out-col-calcAmount').text().replace(/,/gi, "");
					outColObj.collectAmount = $(this).find('.collect-input').val();
					outColObj.deductAmount = $(this).find('.diffrenceAmount').text().replace(/,/gi, "");
					outColObj.summary = $(this).find('.summary').val();
					outColList.push(outColObj);
					return;
				}
			});
			console.log(outColList);
			
			var msg = (mchtId == '' ? '전체 항목을 저장 하시겠습니까?' : '해당 항목을 저장 하시겠습니까?');
			
			bootbox.confirm(msg, function(result) {
      if (result) {
        $.ajax({
          url: '/collect/decide/${COLLECT_MAP.collectId }',
          dataType : "text",
          beforeSend : function(xhr) {
            xhr.setRequestHeader("Content-type",
                "application/json;charset=utf-8");
          },
          method:'post',
          data: JSON.stringify(outColList),
          success: function(res, stat) {
            res = JSON.parse(res);
            if(res.result == 'OK') {
							bootbox.alert("입금정산 상세 내역 반영에 성공하였습니다.");
							location.reload();
            } else {
              bootbox.alert("입금정산 상세 내역 반영에 실패하였습니다.\n" + res.msg);
            }
          },
          error : function(xhr, status, error) {
            bootbox.alert("입금정산 상세 내역 반영에 실패하였습니다.");
          }
        });
      }
    });
		}
		
		function collectDtlFinish() {
			bootbox.confirm('입금정산을 마감하시겠습니까?', function(result) {
				if (result) {
			    $.ajax({
			      url: '/collect/finish/${COLLECT_MAP.collectId }',
			      dataType : "text",
			      method:'get',
			      success: function(res, stat) {
			        res = JSON.parse(res);
			        if(res.result == 'OK') {
			          bootbox.alert("입금정산을 마감 하였습니다.", function () {
									document.location.href = '/collect/collect/form';
								});
								
			        } else {
			          bootbox.alert("입금정산 마감에 실패하였습니다.\n" + res.msg);
			        }
			      },
			      error : function(xhr, status, error) {
			        bootbox.alert("입금정산 마감에 실패하였습니다. (시스템 에러)");
			      }
			    });
				}
			});
		}

	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>