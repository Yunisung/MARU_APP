<%@page contentType="text/html; charset=UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%> 
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*"%>
<%
	SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
	SimpleDateFormat sf2 = new SimpleDateFormat("yyyy년 MM월");
	
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, -2);
	java.util.Date result_date = cal.getTime();
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
    <c:import url="/include/head.jsp" />
    <link href="/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet">
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
                                <li><span>휴대폰 결제</span><i class="fa fa-circle"></i></li>
                                <li><span>수납정산 내역 업로드</span></li>
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
                                            <p>* 휴대폰 수납파일 등록은 월1회 가능합니다.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- END PAGE BAR -->
                        <div class="portlet-body form">
                        	<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="" method="post">	
	                            <div class="form-body row">
		                            <div class="form-group col-sm-12 form-subtitle">
		                                <label><i class="fa fa-reorder"></i>수납파일 등록</label>
		                            </div>
		                            
		                            <div class="form-group col-sm-12">
	                                    <label class="control-label col-sm-2">수납월 </label>
	                                    <div class="col-sm-8"><%= sf2.format(result_date) %></div>
	                                </div>
	                                <div class="form-group col-sm-12">
	                                    <label class="control-label col-sm-2">수납파일</label>
	                                    <div class="col-sm-8">
	                                    	<div class="fileinput fileinput-new" data-provides="fileinput">
		                                        <div class="input-group">
		                                            <div class="form-control uneditable-input input-fixed input-large" data-trigger="fileinput">
		                                                <i class="fa fa-file fileinput-exists"></i>&nbsp;
		                                                <span class="fileinput-filename">
		                                                </span>
		                                            </div>
		                                            <span class="input-group-addon btn blue btn-file">
		                                                <span class="fileinput-new"> Excel 수납파일 업로드 </span>
		                                                <span class="fileinput-exists"> 변경 </span>
		                                                <input type="hidden">
		                                                <input type="file" name="upload-excel" id="upload-excel">
		                                            </span>
		                                            <a href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput">	제거 </a>
		                                            
		                                            <div class="pull-right">
                                                        <button type="button" class="btn btn-sm green right" id="search_submit" onClick="importExcelCollectData();">
                                                            <i class="fa fa-check" aria-hidden="true"></i> 수납정산 반영
                                                        </button>
                                                    </div>
		                                        </div>
		                                    </div>
	                                    </div>
	                                </div>
	                            </div>
	                        </form>
                        </div>
                        <div class="page-content-inner" id="search-container">
	                        <div class="portlet light portlet-form">
	                            <div class="portlet-body form light">
	                            	<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/settle/phone/sunab/excel/list" method="post">
	                            		<div class="form-body">
	                            			<div class="form-group pg-form-group"> 
	                            				<input type="hidden" name="stlDay" value=<%= sf1.format(result_date) %> data-oper="eq">
	                            			</div>
	                            		</div>
	                            	</form>
	                            </div>
	                        </div>
	                        <div class="portlet light portlet-form" id="searchResult"></div>
	                    </div>
                    </div>
                </div>
            </div>
        <!-- BEGIN CONTAINER -->
    </div>
    <c:import url="/include/footer.jsp" />
    </div>
    <c:import url="/include/javascript.jsp" />
    <script src="/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/xlsx/xlsx.full.min.js"></script>
    <script type="text/javascript">
    	setTimeout(function(){ searchForList(); }, 100); //검색 실행
    	
		$('#nav-phone-settle').addClass('active');
		var rABS = true; // T : 바이너리, F : 어레이 버퍼
		var input_dom_element;
		var resultArray = [];
		var reqRow = {};
		var flag = false;
		
    	$(function () {
    		input_dom_element = document.getElementById('upload-excel');
    		
    		if (input_dom_element.addEventListener) {
    			input_dom_element.addEventListener('change', handleFile, false);
    		}
    	});
    	
		// 어레이 버퍼를 처리한다 ( 오직 readAsArrayBuffer 데이터만 가능하다 )
		function fixdata(data) {
			var o = "", l = 0, w = 10240;
			for (; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)));
			o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
			return o;
		}

    	function handleFile(e) {
    		var files = e.target.files;
    		var i, f;
    		
    		resultArray = [];
    		reqRow = {};
    		flag = false;
    		endFlag = false;
    		
    		for (i = 0; i != files.length; ++i) {
    			f = files[i];
    			var reader = new FileReader();
    			var name = f.name;

    			reader.onload = function (e) {
    				var data = e.target.result;
    				var workbook;

    				if (rABS) {
    					workbook = XLSX.read(data, {
    						type: 'binary'
    					});
    				} else {
	    		         var arr = fixdata(data);
	    		         workbook = XLSX.read(btoa(arr), {
	    		        	 type: 'base64'
	    		         });
    				} //end. if

    				/* 워크북 처리 */
    				workbook.SheetNames.forEach(function (item, index, array) {
    					if(index == 0) {
    						var html = XLSX.utils.sheet_to_html(workbook.Sheets[item], {});

    						$(html).find('tr').each(function (i , e) {
    							if(i == 0) {
    								$(e).find('td').each(function (x , y) {
    									if(x == 3) {
    										if($(y).html() == "주문번호") {
    											flag = true;
    										}
    									}
    	    						});
    							} else if(i > 0) {
    								$(e).find('td').each(function (x , y) {
    									if(x == 3) {
    										if(flag) {
    											reqRow.trxId = $(y).html();
    										}
    									}else if(x == 7) {
    										if(flag) {
    											reqRow.payAmt = $(y).html();
    										}
    									}else if(x == 15) {
    										if(flag) {
    											reqRow.sunabAmt = $(y).html();
    											endFlag = true;
    										}
    									}
    									
    									if(endFlag) {
    										endFlag = false;
        									resultArray.push(reqRow);
        									reqRow = {};
    									}
    	    						});
    							} 
    						});
    					}
    		        }); //end. forEach
    		     }; //end onload

    		     if (rABS) reader.readAsBinaryString(f);
    		     else reader.readAsArrayBuffer(f);
    		} //end. for
    	}

    	function importExcelCollectData() {
    		if(flag) {
    			bootbox.confirm('수납파일을 업로드 하시겠습니까?', function(result) {
    				if (result) {
    					$.ajax({
    						url: '/settle/phone/sunab/excel/save',
    						dataType : "text",
    						beforeSend : function(xhr) {
    						xhr.setRequestHeader("Content-type",
    								"application/json;charset=utf-8");
    				 	 	},
    						method:'post',
    						data: JSON.stringify(resultArray),
    						success: function(res, stat) {
    				 	 		res = JSON.parse(res);
    				 	 		if(res.result == 'OK') {
    				 	 			bootbox.alert("수납파일 업로드에 성공하였습니다.", function() {
    				 	 				searchForList();
    				 	 			});
    				 	 		} else {
    				 	 			bootbox.alert("수납파일 업로드에 실패하였습니다.");
    				 	 		}
    				 	 	},
    						error : function(xhr, status, error) {
    				 	 		bootbox.alert("수납파일 업로드에 실패하였습니다.");
    				 	 	}
    					});
    				}
    			});
    		}else {
    			bootbox.alert("정상적인 수납파일을 등록해 주세요.");
    		}
    	}
    </script>
    <!-- 모달 생성을 위한 베이스 -->
    <div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>