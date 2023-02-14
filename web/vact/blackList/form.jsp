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

<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed">
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
								<li><span>가상계좌 관리</span><i class="fa fa-circle"></i></li>
								<li><span>출금계좌 블랙리스트 조회</span></li>
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
						<div class="page-content-inner" id="search-container">
							<!-- 검색 폼 시작 -->
							<div class="portlet light portlet-form">
								<div class="portlet-body form light">
									<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/vact/reg/blackList/list"
									 method="post">
										<input type="hidden" data-reg="false" name="reason" value="출금계좌 블랙리스트">
										<input type="hidden" data-reg="false" name="thead" value="bankCd:출금은행코드,bankNm:출금은행명,account:츌금계좌번호,regDate:등록일시">
										<div class="form-body">
											<div class="row">
												<div class="form-group pg-form-group">
													<label class="control-label input-sm col-sm-4 req-label">출금은행</label>
													<select class="selectpicker col-lg-8" name="bankCd" data-oper="eq">
														<option value="">-- 전체 -- </option>
														<option value="002">산업</option>
														<option value="003">기업</option>
														<option value="004">국민</option>
														<option value="011">농협(중앙회)</option>
														<option value="012">농협(지억)</option>
														<option value="020">우리</option>
														<option value="088">신한</option>
														<option value="090">카카오뱅크</option>
														<option value="089">케이뱅크</option>
														<option value="092">토스뱅크</option>
														<option value="081">KEB하나</option>
														<option value="105">웰컴저축</option>
														<option value="045">새마을</option>
														<option value="048">신협</option>
														<option value="071">우체국</option>
														<option value="023">SC</option>
														<option value="027">씨티</option>
														<option value="031">대구</option>
														<option value="032">부산</option>
														<option value="034">광주</option>
														<option value="035">제주</option>
														<option value="037">전북</option>
														<option value="039">경남</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">츌금계좌번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="account" data-oper="eq" placeholder="계좌번호">
													</div>
												</div>
											</div>
										</div>
										<div class="form-actions nobg right">
											<div class="btn folding-search-btn icon-arrow-down"></div>
											<div class="">
												<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
													<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
												</button>
												<button type="button" class="btn btn-sm green" id="search_submit" onClick="searchForList()">
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
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function () {
			searchForList();
		}, 100); //검색 실행
		$('#nav-trx').addClass('active');

		var idxArr = '';
		$(document).on('click', '.changeReason', function() {
			var idx = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					idx += $(e).attr('data-idx') + ",";
				}
			});

			if (idx.length < 1) {
				bootbox.alert("대상을 체크하세요.");
			} else {
				idxArr = idx.substring(0, idx.length - 1);
				var $modal = $('#pgmate-modal');
				if ($modal.children().length < 1) {
					$modal.empty();
				}
				var url = '/vact/blackList/changeReason.jsp';

				$modal.load(url, '', function(responseTxt, statusTxt, xhr) {
					if (statusTxt == "success") {
						$modal.modal();
						textMask();
					}
				});
			}
		});

		function selectDelete() {
			var idx = '';
			$('table.pg-table>tbody>tr').each(function(i, e) {
				if ($(e).find('input[type="checkbox"]').is(':checked')) {
					idx += $(e).attr('data-idx') + ",";
				}
			});

			if (idx.length < 1) {
				bootbox.alert("재전송할 대상을 체크하세요.");
				
				return;
			} else {
				idx = idx.substring(0, idx.length - 1);
			}
			
			bootbox.confirm({
			    message: "선택한 블랙리스트 모두 삭제 처리 하시겠습니까?",
			    buttons: {
			        confirm: {
			            label: 'Yes',
			            className: 'btn-success'
			        },
			        cancel: {
			            label: 'No',
			            className: 'btn-danger'
			        }
			    },
			    callback: function (result) {
					if(result) {
						$.ajax({url: "/vact/reg/blackList/delete/" + idx,
						success: function(result){
							if(result == 'true') {
								alert("요청완료");	
							}else {
								alert("요청실패 확인요망.");
							}
					    	
					    	searchForList();//검색 실행
						}});
					}
			    }
			});
		};
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>