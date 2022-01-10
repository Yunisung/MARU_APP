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
									<li><span>거래관리</span><i class="fa fa-circle"></i></li>
                  <li><span>카드번호 수기입력</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">
											</div>
										</div>
									</div>
							</div>
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-title">
											<div class="caption">
												<i class="icon-speech"></i>
												<span class="caption-subject bold uppercase"> 카드번호 수기입력</span>
												<span class="caption-helper"></span>
											</div>
										</div>
										<div class="portlet-body">
											<div class="row">
												<div class="col-md-6">
													<div class="row">
														<div>
															<a class="btn btn-sm green" href="javascript:scan();">스캔하기</a>
														</div>
														<textarea rows="50" cols="80" id="contents"></textarea>
													</div>
												</div>
												<div class="col-md-6">
														<div>
															<a id="update" style="display: none;" class="btn btn-sm green pull-right" href="javascript:update();">반영하기</a>
														</div>
														<div class="table-scrollable">
															<!-- 리스트 본문 시작 -->
															<table class="pg-table table table-striped table-hover flip-content">
																<!-- table-bordered -->
																<thead>
																	<tr>
																		<th>No</th>
																		<th data-sort="string">거래번호</th>
																		<th data-sort="string">TID</th>
																		<th data-sort="string">AMOUNT</th>
																		<th data-sort="string">주문번호</th>
																		<th data-sort="string">Last4</th>
																	</tr>
																</thead>
																<tbody id="trx-list">
																	
																</tbody>
															</table>
														</div>
													</div>
											</div>
										</div>
									</div>
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				<!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<script type="text/javascript">
		var cardUpdateList = [];
		function scan() {
			var contents = $('#contents').val();
			contents = contents.split('\n');
		
			var contentList = [];
			for (i in contents) {
				var content = contents[i];
				if (!content || content.split('\t').length != 4) {
					console.log('ROW SKIP! ', i, content);
					break;
				}
				content = content.split('\t');
				var contentMap = {};
				contentMap.tid = content[0];
				contentMap.last4 = content[3].substring(content[3].length - 4, content[3].length);
				contentList.push(contentMap);
			}
			contentList = JSON.stringify(contentList);
			
			$.ajax({
				method:'post',
				url:'/trx/card/scan',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
						"application/json;charset=utf-8");
				},
				data: contentList,
				success: function(rs, stat) {
					if(rs == '[]') {
						alert('변경대상이 없습니다.');
					} else {
						rs = JSON.parse(rs);
						for (i in rs) {
							var each = rs[i];
							var cardMap = {};
							cardMap.trxId = each.trxId;
							cardMap.last4 = each.last4;
							cardUpdateList.push(cardMap);
							$('#trx-list').append('<tr><td>' + (++i) + '</td><td>' + each.trxId + '</td><td>' + each.vanTrxId + 
																		'</td><td>' + each.amount + '</td><td>' + each.trackId + 
																		'</td><td>' + each.last4 + '</td></tr>');
						}
						cardUpdateList = JSON.stringify(cardUpdateList);
						console.log(cardUpdateList);
						$('#update').show();
					}
				}, error: function(err) {
					console.log(err);
				}
			});
		}

		function update() {
			$.ajax({
				method:'post',
				url:'/trx/card/update',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
						"application/json;charset=utf-8");
				},
				data: cardUpdateList,
				success: function(rs, stat) {
					console.log(rs);
					if(rs.indexOf('NOK') > -1) {
						alert('실패했습니다.');
					} else {
						alert('성공했습니다.');
					}
					document.location.reload();
				}, error: function(err) {
					console.log(err);
					alert('실패했습니다.');
				}
			});
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>