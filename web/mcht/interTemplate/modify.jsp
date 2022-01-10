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
<style>
	.rate {width:100%} 
</style>
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
									<li><span>가맹점 관리</span><i class="fa fa-circle"></i></li>
									<li><span>상점 부담 무이자 템플릿 수정</span></li>
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
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-reorder"></i> <span class="caption-title">
													 상점부담 무이자 수정 </span>
											</div>
										</div>
										<div class="form-group col-sm-12">
											<label class="control-label col-sm-2 req-label">템플릿명</label> 
											<div class="col-sm-10">
												<input type="text" class="form-control input-sm name" id="name" name="name" maxlength="100" value="${DATAMAP.name }">
												<input type="hidden" id="templateId" name="templateId" value="${DATAMAP.templateId }">
											</div>
										</div>
										<div style="color:red">* % 단위로 입력하세요. (10% = 0.1)</div>
										<div class="table-scrollable">
										<!-- 스케쥴 시작 -->
											<table class="pg-table table table-bordered table-hover flip-content">
												<!-- table-bordered -->
												<thead>
													<tr>
														<th>매입사</th>
														<th>2개월</th>
														<th>3개월</th>
														<th>4개월</th>
														<th>5개월</th>
														<th>6개월</th>
														<th>7개월</th>
														<th>8개월</th>
														<th>9개월</th>
														<th>10개월</th>
														<th>11개월</th>
														<th>12개월</th>
														<th>13개월</th>
														<th>14개월</th>
														<th>15개월</th>
														<th>16개월</th>
														<th>17개월</th>
														<th>18개월</th>
														<th>19개월</th>
														<th>20개월</th>
														<th>21개월</th>
														<th>22개월</th>
														<th>23개월</th>
														<th>24개월</th>
													</tr>
												</thead>
												<tbody id="list">
												<c:forEach var="entry" items="${DATALIST}" varStatus="status">
													<form id="form${status.count }">
													<tr>
														<td>${entry.acqName }<input type="hidden" name="acquirer" value="${entry.acquirer }"></td>
														<td><input type="text" class="perm02${status.count }" value="" data-reg="false"/><input type="hidden" class="m02${status.count }" name="m02" value="${entry.m02 }"/></td>
														<td><input type="text" class="perm03${status.count }" value="" data-reg="false"/><input type="hidden" class="m03${status.count }" name="m03" value="${entry.m03 }"/></td>
														<td><input type="text" class="perm04${status.count }" value="" data-reg="false"/><input type="hidden" class="m04${status.count }" name="m04" value="${entry.m04 }"/></td>
														<td><input type="text" class="perm05${status.count }" value="" data-reg="false"/><input type="hidden" class="m05${status.count }" name="m05" value="${entry.m05 }"/></td>
														<td><input type="text" class="perm06${status.count }" value="" data-reg="false"/><input type="hidden" class="m06${status.count }" name="m06" value="${entry.m06 }"/></td>
														<td><input type="text" class="perm07${status.count }" value="" data-reg="false"/><input type="hidden" class="m07${status.count }" name="m07" value="${entry.m07 }"/></td>
														<td><input type="text" class="perm08${status.count }" value="" data-reg="false"/><input type="hidden" class="m08${status.count }" name="m08" value="${entry.m08 }"/></td>
														<td><input type="text" class="perm09${status.count }" value="" data-reg="false"/><input type="hidden" class="m09${status.count }" name="m09" value="${entry.m09 }"/></td>
														<td><input type="text" class="perm10${status.count }" value="" data-reg="false"/><input type="hidden" class="m10${status.count }" name="m10" value="${entry.m10 }"/></td>
														<td><input type="text" class="perm11${status.count }" value="" data-reg="false"/><input type="hidden" class="m11${status.count }" name="m11" value="${entry.m11 }"/></td>
														<td><input type="text" class="perm12${status.count }" value="" data-reg="false"/><input type="hidden" class="m12${status.count }" name="m12" value="${entry.m12 }"/></td>
														<td><input type="text" class="perm13${status.count }" value="" data-reg="false"/><input type="hidden" class="m13${status.count }" name="m13" value="${entry.m13 }"/></td>
														<td><input type="text" class="perm14${status.count }" value="" data-reg="false"/><input type="hidden" class="m14${status.count }" name="m14" value="${entry.m14 }"/></td>
														<td><input type="text" class="perm15${status.count }" value="" data-reg="false"/><input type="hidden" class="m15${status.count }" name="m15" value="${entry.m15 }"/></td>
														<td><input type="text" class="perm16${status.count }" value="" data-reg="false"/><input type="hidden" class="m16${status.count }" name="m16" value="${entry.m16 }"/></td>
														<td><input type="text" class="perm17${status.count }" value="" data-reg="false"/><input type="hidden" class="m17${status.count }" name="m17" value="${entry.m17 }"/></td>
														<td><input type="text" class="perm18${status.count }" value="" data-reg="false"/><input type="hidden" class="m18${status.count }" name="m18" value="${entry.m18 }"/></td>
														<td><input type="text" class="perm19${status.count }" value="" data-reg="false"/><input type="hidden" class="m19${status.count }" name="m19" value="${entry.m19 }"/></td>
														<td><input type="text" class="perm20${status.count }" value="" data-reg="false"/><input type="hidden" class="m20${status.count }" name="m20" value="${entry.m20 }"/></td>
														<td><input type="text" class="perm21${status.count }" value="" data-reg="false"/><input type="hidden" class="m21${status.count }" name="m21" value="${entry.m21 }"/></td>
														<td><input type="text" class="perm22${status.count }" value="" data-reg="false"/><input type="hidden" class="m22${status.count }" name="m22" value="${entry.m22 }"/></td>
														<td><input type="text" class="perm23${status.count }" value="" data-reg="false"/><input type="hidden" class="m23${status.count }" name="m23" value="${entry.m23 }"/></td>
														<td><input type="text" class="perm24${status.count }" value="" data-reg="false"/><input type="hidden" class="m24${status.count }" name="m24" value="${entry.m24 }"/></td>
													</tr>
													</form>
												</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- END ADD FORM TABLE-->
										<div class="alert alert-danger display-hide"></div>
											<div class="form-actions pull-right">
												<div class="">
													<button type="button" class="btn green btn-sm loading-btn" id="insert" onclick="fnInsert('${fn:length(DATALIST) }');">
														<i class="fa fa-edit"></i>&nbsp;수정
												</button>
												<button type="button" id="deleteTemplate" class="btn red btn-sm loading-btn"
															data-loading-text="Loading...">
															<i class="fa fa-remove"></i>&nbsp;삭제
												</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
			</div>
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		$.fn.serializeObject = function(){
		   var o = {};
		   var a = this.serializeArray();
		   $.each(a, function() {
		       if (o[this.name]) {
		           if (!o[this.name].push) {
		               o[this.name] = [o[this.name]];
		           }
		           o[this.name].push(this.value || '');
		       } else {
		           o[this.name] = this.value || '';
		       }
		   });
		   return o;
		};
	
		function fnInsert(size){
			bootbox.confirm("상점부담 무이자 템플릿 수정을 진행하시겠습니까?",function(result){
				if(result){
					var list = new Array();
					var json = new Object();
					json.templateId = $('#templateId').val();
					json.name = $('#name').val();
					
					for(var i=1;i<=size;i++){
						$('.m02'+i).val(($('.perm02'+i).val()/100).toFixed(5));
						$('.m03'+i).val(($('.perm03'+i).val()/100).toFixed(5));
						$('.m04'+i).val(($('.perm04'+i).val()/100).toFixed(5));
						$('.m05'+i).val(($('.perm05'+i).val()/100).toFixed(5));
						$('.m06'+i).val(($('.perm06'+i).val()/100).toFixed(5));
						$('.m07'+i).val(($('.perm07'+i).val()/100).toFixed(5));
						$('.m08'+i).val(($('.perm08'+i).val()/100).toFixed(5));
						$('.m09'+i).val(($('.perm09'+i).val()/100).toFixed(5));
						$('.m10'+i).val(($('.perm10'+i).val()/100).toFixed(5));
						$('.m11'+i).val(($('.perm11'+i).val()/100).toFixed(5));
						$('.m12'+i).val(($('.perm12'+i).val()/100).toFixed(5));
						$('.m13'+i).val(($('.perm13'+i).val()/100).toFixed(5));
						$('.m14'+i).val(($('.perm14'+i).val()/100).toFixed(5));
						$('.m15'+i).val(($('.perm15'+i).val()/100).toFixed(5));
						$('.m16'+i).val(($('.perm16'+i).val()/100).toFixed(5));
						$('.m17'+i).val(($('.perm17'+i).val()/100).toFixed(5));
						$('.m18'+i).val(($('.perm18'+i).val()/100).toFixed(5));
						$('.m19'+i).val(($('.perm19'+i).val()/100).toFixed(5));
						$('.m20'+i).val(($('.perm20'+i).val()/100).toFixed(5));
						$('.m21'+i).val(($('.perm21'+i).val()/100).toFixed(5));
						$('.m22'+i).val(($('.perm22'+i).val()/100).toFixed(5));
						$('.m23'+i).val(($('.perm23'+i).val()/100).toFixed(5));
						$('.m24'+i).val(($('.perm24'+i).val()/100).toFixed(5));
						
						list.push($('#form'+i).serializeObject());
					}
					
					json.list = list;
					$.ajax({
						url:'/mcht/interTemplate/update',
						type:'POST',
						data: JSON.stringify(json),
						dataType: 'json',
						contentType: 'application/json; charset=UTF-8',
						success: function(result){
							if(result.resultCd == '0000'){
								bootbox.alert("상점부담 무이자 템플릿 수정이 완료되었습니다.",function(){
									location.href = '/mcht/interTemplate/form.jsp';	
								});
							}else{
								bootbox.alert("상점부담 무이자 수정에 실패하였습니다.");
							}
						}
					});
				}
			});
		}
		
		$(document).on('click', '#deleteTemplate', function() {
			bootbox.confirm('해당 템플릿을 삭제하시겠습니까?', function(res) {
				if(res) {
					$.ajax({
						url: '/mcht/InterTemplate/delete/' + $('#templateId').val(),
						type:'get',
						success: function(res) {
							if(res.result == 'OK') {
								bootbox.alert('템플릿 삭제에 성공했습니다.', function() {
									location.replace('/mcht/interTemplate/form.jsp');
								});
							} else {
								bootbox.alert('템플릿 삭제에 실패했습니다.');	
							}
						}, 
						error: function(res, stats) {
							console.log(res, status);
							bootbox.alert('템플릿 삭제에 실패했습니다.'+ status);	
						}
					});
				}
			});
		});
		
		for(var i=1; i<=${fn:length(DATALIST) };i++){
			$('.perm02'+i).val(($('.m02'+i).val()*100).toFixed(3));
			$('.perm03'+i).val(($('.m03'+i).val()*100).toFixed(3));
			$('.perm04'+i).val(($('.m04'+i).val()*100).toFixed(3));
			$('.perm05'+i).val(($('.m05'+i).val()*100).toFixed(3));
			$('.perm06'+i).val(($('.m06'+i).val()*100).toFixed(3));
			$('.perm07'+i).val(($('.m07'+i).val()*100).toFixed(3));
			$('.perm08'+i).val(($('.m08'+i).val()*100).toFixed(3));
			$('.perm09'+i).val(($('.m09'+i).val()*100).toFixed(3));
			$('.perm10'+i).val(($('.m10'+i).val()*100).toFixed(3));
			$('.perm11'+i).val(($('.m11'+i).val()*100).toFixed(3));
			$('.perm12'+i).val(($('.m12'+i).val()*100).toFixed(3));
			$('.perm13'+i).val(($('.m13'+i).val()*100).toFixed(3));
			$('.perm14'+i).val(($('.m14'+i).val()*100).toFixed(3));
			$('.perm15'+i).val(($('.m15'+i).val()*100).toFixed(3));
			$('.perm16'+i).val(($('.m16'+i).val()*100).toFixed(3));
			$('.perm17'+i).val(($('.m17'+i).val()*100).toFixed(3));
			$('.perm18'+i).val(($('.m18'+i).val()*100).toFixed(3));
			$('.perm19'+i).val(($('.m19'+i).val()*100).toFixed(3));
			$('.perm20'+i).val(($('.m20'+i).val()*100).toFixed(3));
			$('.perm21'+i).val(($('.m21'+i).val()*100).toFixed(3));
			$('.perm22'+i).val(($('.m22'+i).val()*100).toFixed(3));
			$('.perm23'+i).val(($('.m23'+i).val()*100).toFixed(3));
			$('.perm24'+i).val(($('.m24'+i).val()*100).toFixed(3));
		}
		
		
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>