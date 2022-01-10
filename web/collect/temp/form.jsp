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
									<li><span>입금 예정내역 조회</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">한 가맹점에 (같은 VAN 소속의)다수의 터미널 ID 등록되어 있을 경우, 값이 정확하지 않을 수 있다.</div>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/collect/temp/list" method="post">
												<div class="form-body">
													<div class="row">
														<!-- <div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금일</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm now-date datepicker" name="stlVanDay" data-oper="eq" value="">
															</div>
														</div> -->
														<!-- 
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="A.mchtId" data-oper="lk" placeholder="가맹점 ID">
															</div>
														</div>
														 -->
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm typeahead mchtName" name="D.name" data-oper="lk" placeholder="이름" data-search="mchtName">
															</div>
														</div>
														<!-- 
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 대표자</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="A.ceoName" data-oper="lk" placeholder="가맹점 대표자">
															</div>
														</div>
														 -->
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금일</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date" name="stlVanDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="stlVanDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">터미널ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm tmnId" name="A.tmnId" data-oper="lk" placeholder="터미널 ID">
															</div>
														</div>
														<c:import url="/common/selectGrade.jsp" />
														<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value=""/>
													</div>
												</div>
												<div class="form-actions nobg right">
													<div class="btn folding-search-btn icon-arrow-down"></div>
													<div class="">
														<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
															<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
														</button>
														<button type="button" class="btn btn-sm green" id="searchSubmit" onClick="searchThisPage()">
															<i class="fa fa-search" aria-hidden="true"></i> SEARCH
														</button>
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
		function searchThisPage() {
			searchForList(calcAmt);
		}
		
		$('#nav-collect').addClass('active');
		
		function calcAmt(){
			$('.differenceAmount').each(function() {
				var amt2 = $(this).closest('tr').find('.out-col-collectAmount').attr('data-amt');
				var amt3 = $(this).closest('tr').find('.out-col-MARUAmount').attr('data-amt');
				var res = Number(amt2) - Number(amt3);
				//console.log(amt2, amt3, res);
				$(this).text(toNum(res));
				$(this).attr('data-amt', res);
			});
		}
		function toNum(x) {
		    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		function trxCapCollectPop(url) {
			var w = '1500';
			var h = '950';
			window.open(url, "", "width="+w+", height="+h+", scrollbars=1");
		}
		
		function makeCollect(stlVanDay, categoryId, category, collectAmount) {
			
			bootbox.confirm('선택된 내역을 입금정산으로 생성하시겠습니까?', function(res) {
				if(res) {
					bootbox.prompt({
					    title: "생성 사유를 입력하세요.",
					    inputType: 'textarea',
					    callback: function (summary) {
					    	if(summary === null) {
					    	} else if(summary.length < 1) {
					    		bootbox.alert("생성 사유를 반드시 입력해야 합니다.");
					    	} else {
					    		makeCollectSend(stlVanDay, categoryId, category, collectAmount, summary);
					    	}
					    }
					});
				}
			});
		}
		
		function makeCollectSend(stlVanDay, categoryId, category, collectAmount, summary) {
			console.log(stlVanDay, categoryId, category, collectAmount, summary);
			
			function pad2(n) { return n < 10 ? '0' + n : n }
			var date = new Date();
			var collectDay = date.getFullYear().toString() + pad2(date.getMonth() + 1) + pad2( date.getDate());
			var collectTime = pad2( date.getHours() ) + '' + pad2( date.getMinutes() ) + pad2( date.getSeconds() );
			var collectId = 'CL' + collectDay + collectTime + '0001';
			
			$.ajax({
				url: '/collect/minus/add',
				type : "post",
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data: JSON.stringify({
					stlVanDay: stlVanDay,
					categoryId: categoryId,
					category: category,
					collectId: collectId,
					collectDay: stlVanDay,
					collectTime: collectTime,
					collectAmount: collectAmount,
					summary: summary}),
				success: function(res) {
					res = JSON.parse(res);
					console.log(res);
					if(res.result == 'OK') {
						bootbox.alert('입금정산 생성에 성공했습니다.', function() {
							window.location.href = '/collect/collect/form';
						}); 
					} else {
						bootbox.alert('입금정산 생성에 실패했습니다.'+ res.msg);	
					}
				}, 
				error: function(res, stats) {
					console.log(res, status);
					bootbox.alert('입금정산 생성에 실패했습니다.'+ status);	
				}
			});
		}
		
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>