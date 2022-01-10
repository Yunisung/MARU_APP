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
	.rate{width:100%}
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
									<li><span>상점 부담 무이자 등록</span></li>
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
													${DATAMAP.name} 상점부담 무이자 등록 </span>
											</div>
										</div>
										<c:if test="${CP_SESSION.grade eq '본사' }">
											<div class="form-group col-sm-12">
												<label class="control-label input-sm col-sm-2 req-label">수수료템플릿</label> 
													<select name="feeTemplate" class="selectpicker col-sm-10 feeTemplate">
														<option value="">선택</option>
														<c:forEach items="${DATATEMPLATE }" var="list">
															<option value="${list.templateId }">${list.name }</option>
														</c:forEach>
													</select>
											</div>
										</c:if>
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
												<c:forEach var="entry" items="${DATAACQLIST}" varStatus="status">
													<form id="form${status.count }" name="c${entry.code }">
													<tr>
														<td>${entry.codeName }<input type="hidden" name="acquirer" value="${entry.code }"></td>
														<td><input type="text" class="perm02${status.count }" name="perm02" value="0" data-reg="false"/><input type="hidden" class="m02${status.count }" name="m02" value="0.00000"/></td>
														<td><input type="text" class="perm03${status.count }" name="perm03" value="0" data-reg="false"/><input type="hidden" class="m03${status.count }" name="m03" value="0.00000"/></td>
														<td><input type="text" class="perm04${status.count }" name="perm04" value="0" data-reg="false"/><input type="hidden" class="m04${status.count }" name="m04" value="0.00000"/></td>
														<td><input type="text" class="perm05${status.count }" name="perm05" value="0" data-reg="false"/><input type="hidden" class="m05${status.count }" name="m05" value="0.00000"/></td>
														<td><input type="text" class="perm06${status.count }" name="perm06" value="0" data-reg="false"/><input type="hidden" class="m06${status.count }" name="m06" value="0.00000"/></td>
														<td><input type="text" class="perm07${status.count }" name="perm07" value="0" data-reg="false"/><input type="hidden" class="m07${status.count }" name="m07" value="0.00000"/></td>
														<td><input type="text" class="perm08${status.count }" name="perm08" value="0" data-reg="false"/><input type="hidden" class="m08${status.count }" name="m08" value="0.00000"/></td>
														<td><input type="text" class="perm09${status.count }" name="perm09" value="0" data-reg="false"/><input type="hidden" class="m09${status.count }" name="m09" value="0.00000"/></td>
														<td><input type="text" class="perm10${status.count }" name="perm10" value="0" data-reg="false"/><input type="hidden" class="m10${status.count }" name="m10" value="0.00000"/></td>
														<td><input type="text" class="perm11${status.count }" name="perm11" value="0" data-reg="false"/><input type="hidden" class="m11${status.count }" name="m11" value="0.00000"/></td>
														<td><input type="text" class="perm12${status.count }" name="perm12" value="0" data-reg="false"/><input type="hidden" class="m12${status.count }" name="m12" value="0.00000"/></td>
														<td><input type="text" class="perm13${status.count }" name="perm13" value="0" data-reg="false"/><input type="hidden" class="m13${status.count }" name="m13" value="0.00000"/></td>
														<td><input type="text" class="perm14${status.count }" name="perm14" value="0" data-reg="false"/><input type="hidden" class="m14${status.count }" name="m14" value="0.00000"/></td>
														<td><input type="text" class="perm15${status.count }" name="perm15" value="0" data-reg="false"/><input type="hidden" class="m15${status.count }" name="m15" value="0.00000"/></td>
														<td><input type="text" class="perm16${status.count }" name="perm16" value="0" data-reg="false"/><input type="hidden" class="m16${status.count }" name="m16" value="0.00000"/></td>
														<td><input type="text" class="perm17${status.count }" name="perm17" value="0" data-reg="false"/><input type="hidden" class="m17${status.count }" name="m17" value="0.00000"/></td>
														<td><input type="text" class="perm18${status.count }" name="perm18" value="0" data-reg="false"/><input type="hidden" class="m18${status.count }" name="m18" value="0.00000"/></td>
														<td><input type="text" class="perm19${status.count }" name="perm19" value="0" data-reg="false"/><input type="hidden" class="m19${status.count }" name="m19" value="0.00000"/></td>
														<td><input type="text" class="perm20${status.count }" name="perm20" value="0" data-reg="false"/><input type="hidden" class="m20${status.count }" name="m20" value="0.00000"/></td>
														<td><input type="text" class="perm21${status.count }" name="perm21" value="0" data-reg="false"/><input type="hidden" class="m21${status.count }" name="m21" value="0.00000"/></td>
														<td><input type="text" class="perm22${status.count }" name="perm22" value="0" data-reg="false"/><input type="hidden" class="m22${status.count }" name="m22" value="0.00000"/></td>
														<td><input type="text" class="perm23${status.count }" name="perm23" value="0" data-reg="false"/><input type="hidden" class="m23${status.count }" name="m23" value="0.00000"/></td>
														<td><input type="text" class="perm24${status.count }" name="perm24" value="0" data-reg="false"/><input type="hidden" class="m24${status.count }" name="m24" value="0.00000"/></td>
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
													<button type="button" class="btn green btn-sm loading-btn" id="insert" onclick="fnInsert('${DATAMAP.mchtId}','${fn:length(DATAACQLIST) }');">
														<i class="fa fa-edit"></i>&nbsp;등록
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
	
		$('.selectpicker.feeTemplate').on('change', function() {
			var selected = $(this).find("option:selected").val();
			if(selected == '') return false;
			$.ajax({
		  		url:'/mcht/interTemplate/get/'+selected,
		  		method: 'GET',
		  		dataType: 'json',
		  		success: function(data){
		  			var list = new Array();
		  			list = data;
		  			for(var i=0; i<list.length;i++){
		  				var code = 'c'+list[i].acquirer;
		  				document.forms[code].m02.value = list[i].m02;
		  				document.forms[code].m03.value = list[i].m03;
		  				document.forms[code].m04.value = list[i].m04;
		  				document.forms[code].m05.value = list[i].m05;
		  				document.forms[code].m06.value = list[i].m06;
		  				document.forms[code].m07.value = list[i].m07;
		  				document.forms[code].m08.value = list[i].m08;
		  				document.forms[code].m09.value = list[i].m09;
		  				document.forms[code].m10.value = list[i].m10;
		  				document.forms[code].m11.value = list[i].m11;
		  				document.forms[code].m12.value = list[i].m12;
		  				document.forms[code].m13.value = list[i].m13;
		  				document.forms[code].m14.value = list[i].m14;
		  				document.forms[code].m15.value = list[i].m15;
		  				document.forms[code].m16.value = list[i].m16;
		  				document.forms[code].m17.value = list[i].m17;
		  				document.forms[code].m18.value = list[i].m18;
		  				document.forms[code].m19.value = list[i].m19;
		  				document.forms[code].m20.value = list[i].m20;
		  				document.forms[code].m21.value = list[i].m21;
		  				document.forms[code].m22.value = list[i].m22;
		  				document.forms[code].m23.value = list[i].m23;
		  				document.forms[code].m24.value = list[i].m24;
		  				
		  				document.forms[code].perm02.value = ((list[i].m02)*100).toFixed(3);
		  				document.forms[code].perm03.value = ((list[i].m03)*100).toFixed(3);
		  				document.forms[code].perm04.value = ((list[i].m04)*100).toFixed(3);
		  				document.forms[code].perm05.value = ((list[i].m05)*100).toFixed(3);
		  				document.forms[code].perm06.value = ((list[i].m06)*100).toFixed(3);
		  				document.forms[code].perm07.value = ((list[i].m07)*100).toFixed(3);
		  				document.forms[code].perm08.value = ((list[i].m08)*100).toFixed(3);
		  				document.forms[code].perm09.value = ((list[i].m09)*100).toFixed(3);
		  				document.forms[code].perm10.value = ((list[i].m10)*100).toFixed(3);
		  				document.forms[code].perm11.value = ((list[i].m11)*100).toFixed(3);
		  				document.forms[code].perm12.value = ((list[i].m12)*100).toFixed(3);
		  				document.forms[code].perm13.value = ((list[i].m13)*100).toFixed(3);
		  				document.forms[code].perm14.value = ((list[i].m14)*100).toFixed(3);
		  				document.forms[code].perm15.value = ((list[i].m15)*100).toFixed(3);
		  				document.forms[code].perm16.value = ((list[i].m16)*100).toFixed(3);
		  				document.forms[code].perm17.value = ((list[i].m17)*100).toFixed(3);
		  				document.forms[code].perm18.value = ((list[i].m18)*100).toFixed(3);
		  				document.forms[code].perm19.value = ((list[i].m19)*100).toFixed(3);
		  				document.forms[code].perm20.value = ((list[i].m20)*100).toFixed(3);
		  				document.forms[code].perm21.value = ((list[i].m21)*100).toFixed(3);
		  				document.forms[code].perm22.value = ((list[i].m22)*100).toFixed(3);
		  				document.forms[code].perm23.value = ((list[i].m23)*100).toFixed(3);
		  				document.forms[code].perm24.value = ((list[i].m24)*100).toFixed(3);
		  			}
		  		}
		  	});
		});
		
		
		
		function fnInsert(mchtId,size){
			bootbox.confirm("상점부담 무이자 등록을 진행하시겠습니까?",function(result){
				var list = new Array();
				var json = new Object();
				json.mchtId = mchtId;
				
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
					url:'/mcht/inter/insert',
					type:'POST',
					data: JSON.stringify(json),
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					success: function(result){
						if(result.resultCd == '0000'){
							bootbox.alert("상점부담 무이자 등록이 완료되었습니다.",function(){
								location.href = '/mcht/view/${DATAMAP.mchtId}/tab_interest';	
							});
						}else{
							bootbox.alert("상점부담 무이자 등록에 실패하였습니다.");
						}
					}
				});
			});
		}
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>