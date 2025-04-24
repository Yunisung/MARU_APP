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
                                <li><span>영중소 차액정산</span><i class="fa fa-circle"></i></li>
                                <li><span>차액입금정산 내역 업로드</span></li>
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
                                            <p>* <code>우리은행</code> 입금 포맷 사용</p>
                                            <p>* 상단 Row 유지</p>
                                            <p>* <code>NO</code>, <code>거래일시</code> 항목은 수정 하지 않고 업로드(중복 검사항목)</p>
                                            <p>* <code>CPID</code> 사용시 <code>기재내용</code>이 <code>Van ID</code>와 일치하면 자동 매핑</p>
                                            <p>* <code>예비</code> 상태인 <code>Van ID</code>는 매핑에서 제외 </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- END PAGE BAR -->
                        <!-- BEGIN PAGE CONTENT INNER -->
                        <div class="page-content-inner" id="search-container">
                            <!-- 검색 폼 시작 -->
                            <div class="portlet light portlet-form">
                                <div class="portlet-body form light">
                                    <div class="row">
                                        <div class="col-md-6" style="max-width: 480px;">
                                            <div class="fileinput fileinput-new" data-provides="fileinput">
                                                <div class="input-group">
                                                    <div class="form-control uneditable-input input-fixed input-large" data-trigger="fileinput">
                                                        <i class="fa fa-file fileinput-exists"></i>&nbsp;
                                                        <span class="fileinput-filename">
                                                        </span>
                                                    </div>
                                                    <span class="input-group-addon btn blue btn-file">
                                                        <span class="fileinput-new"> Excel 파일 업로드 </span>
                                                        <span class="fileinput-exists"> 변경 </span>
                                                        <input type="hidden">
                                                        <input type="file" name="upload-excel" id="upload-excel">
                                                    </span>
                                                    <a href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput">	제거 </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="pull-right">
<%-- 																<button type="button" class="btn btn-sm blue-dark right" id="SearchClear">
                                                    <i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
                                                </button> --%>
                                                <button type="button" class="btn btn-sm green right" id="search_submit" onClick="importExcelCollectData();">
                                                    <i class="fa fa-check" aria-hidden="true"></i> 선택항목 반영
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 검색 폼 종료 -->
                            <!-- 내용 폼 시작 -->
                            <div class="portlet light portlet-form" id="searchResult">
                                <div class="portlet-title">
                                    <div class="caption font-red-sunglo">
                                        <i class="icon-share font-red-sunglo"></i>
                                        <span class="caption-subject bold uppercase"> Result </span>
                                    </div>
                                </div>
                                <div class="portlet-body form light">
                                    <div class="table-scrollable">
                                        <!-- 리스트 본문 시작 -->
                                        <table class="pg-table table table-hover flip-content" id="sortTable">
                                        <thead><tr></tr></thead>
                                        <tbody></tbody>
                                        </table>
                                    </div>
                                </div>
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
    <script src="/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/xlsx/xlsx.full.min.js"></script>
    <script type="text/javascript">
    $('#nav-diff').addClass('active');
    var rABS = true; // T : 바이너리, F : 어레이 버퍼

 // 어레이 버퍼를 처리한다 ( 오직 readAsArrayBuffer 데이터만 가능하다 )
 function fixdata(data) {
   var o = "",
     l = 0,
     w = 10240;
   for (; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w +
     w)));
   o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
   return o;
 }

 // 데이터를 바이너리 스트링으로 얻는다.
 function getConvertDataToBin($data) {
   var arraybuffer = $data;
   var data = new Uint8Array(arraybuffer);
   var arr = new Array();
   for (var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
   var bstr = arr.join("");

   return bstr;
 }


 function pad(n, width, z) {
 	z = z || '0';
 	n = n + '';
 	return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
 }

 function getCollectId(date, no) {
 	var collectId = 'CL' + date.replace(/ |\:|\(|\)|\./gi, "") + '' + pad(no, 3);
 	return collectId;
 }

 function handleFile(e) {
   var files = e.target.files;

     // 허용할 엑셀 확장자 및 MIME 타입
     const allowedExtensions = ['xls', 'xlsx'];
     const allowedMimeTypes = [
         'application/vnd.ms-excel',                        // .xls
         'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' // .xlsx
     ];

     if(files.length > 1) {
         bootbox.alert("1개의 파일만 업로드할 수 있습니다.");
         return;
     }

     const file = files[0];
     if(!file) return;

     const fileName = file.name.toLowerCase();
     const fileExtension = fileName.split('.').pop();
     const fileType = file.type;

     if (!allowedExtensions.includes(fileExtension) || !allowedMimeTypes.includes(fileType)) {
         bootbox.alert("엑셀 파일(.xls, .xlsx)만 업로드할 수 있습니다.");
         return;
     }

   var i, f;
   for (i = 0; i != files.length; ++i) {
     f = files[i];
     var reader = new FileReader();
     var name = f.name;

     reader.onload = function (e) {
       var data = e.target.result;
       var workbook;

       if (rABS) {
         /* if binary string, read with type 'binary' */
         workbook = XLSX.read(data, {
           type: 'binary'
         });
       } else {
         /* if array buffer, convert to base64 */
         var arr = fixdata(data);
         workbook = XLSX.read(btoa(arr), {
           type: 'base64'
         });
       } //end. if

       /* 워크북 처리 */
       workbook.SheetNames.forEach(function (item, index, array) {
         if(index == 0) {
           var html = XLSX.utils.sheet_to_html(workbook.Sheets[item], {
             header: '<div class="portlet-body form light"><div class="table-scrollable">'
           });
           var senders = {};
           var datetimes = {};
           $(html).find('tr').each(function (i , e) {
             if(i < 3) {
               $(e).remove();
             } else if(i == 3) {
               $(e).prepend('<th>지급 대상</th>');
               $(e).prepend('<th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>');
               $(e).find('td').contents().unwrap().wrap('<th></th>');
               $('#sortTable>thead>tr').html($(e).html());
             } else {
               $(e).prepend('<td class="select-td"><select class="collect-group-category" name="category-'+i+'" data-idx="'+i+'"><option value="van">VAN</option><option value="vanId">VAN ID</option><option value="mcht">가맹점</option><option value="mchtId">가맹점아이디</option></select><select name="'+i+'"><option value="">선택 안됨</option></select></td>');
               $(e).prepend('<td class="btn-td"><input type="checkbox" class="row-check" name="ck_'+i+'" id="ck_'+i+'" class="checkbox-style" /><label for="ck_'+ i +'"></label></td>');
               
               $('#sortTable tbody').append($(e));
               senders[i] = { sender: $(e).children().eq(5).text(), collectId: getCollectId($(e).children().eq(3).text(), $(e).children().eq(2).text())};
             }
           });

           //console.log('기재내용 검색 OBJ:',senders);
           $.ajax({
             url: '/diff/group/find',
             dataType : "text",
             beforeSend : function(xhr) {
               xhr.setRequestHeader("Content-type",
                   "application/json;charset=utf-8");
             },
             method:'post',
             data: JSON.stringify(senders),
             success: function(res, stat) {
               res = JSON.parse(res);
               for(each in res) {
             	  appendSelectGroup(each, res);
               }
             },
             error : function(xhr, status, error) {
               bootbox.alert("실패하였습니다.");
             }
           });

         }
       }); //end. forEach
     }; //end onload

     if (rABS) reader.readAsBinaryString(f);
     else reader.readAsArrayBuffer(f);

   } //end. for
 }

 function appendSelectGroup(each, res) {
     var categoryElem = $('#sortTable').find('select[name="category-'+each+'"]');
     var elem = $('#sortTable').find('select[name="'+each+'"]');
     elem.empty();
     var eachList = res[each];
       if(eachList == 'DUPLICATION') {
         categoryElem.hide();
         elem.hide();
         elem.closest('td').addClass('font-red').text('기입력');
         $('#sortTable').find('input[name="ck_'+each+'"]').attr("disabled", true);
       } else {
     	var selectedCnt = 0;
     	for(i in eachList) {
     		if(eachList[i].selected) {
             	selectedCnt++;
             }
     		categoryElem.val(eachList[i].category);
             elem.append('<option value="'+eachList[i].colgId+'"' + (eachList[i].selected && selectedCnt === 1 ? 'selected' : '') + '>'+eachList[i].name+'</option>');
     	}
     	selectedCnt === 1 && $('#sortTable').find('input[name="ck_'+each+'"]').attr("checked", true);
     }
     //categoryElem.selectpicker('refresh');
     //elem.selectpicker('refresh');
 }

 var input_dom_element;
 $(function () {
   input_dom_element = document.getElementById('upload-excel');
   if (input_dom_element.addEventListener) {
     input_dom_element.addEventListener('change', handleFile, false);
   }
 });

 function importExcelCollectData() {
   var resultArray = [];
   var valid = false;
   $('.row-check').each(function(i, e) {
     var index = $(e).attr('name').replace('ck_', '');
     var reqRow = {};
     if($(e).prop('checked') && !$(e).prop('disabled')) {
       var trElem = $(e).closest('tr');
       var category = $('select[name="category-' + index + '"]').val();
       var colgId = $('select[name="' + index + '"]').val();
       reqRow.category = category;
       reqRow.colgId = colgId;
       var tempDate = '';
       var tempNo = '';
       $(trElem).children('td').each(function(i, e) {
         if(i == 2) {
           tempNo = $(e).text();
         } else if(i == 3) {
           var datetime = $(e).text().replace(/ |\:|\(|\)|\./gi, "");
           tempDate = $(e).text();
           reqRow.collectDay = datetime.substring(0,8);
           reqRow.collectTime = datetime.substring(8,14);
         } else if(i == 7) {
           reqRow.collectAmount = $(e).text().replace(/,/gi, "");
         } else if(i == 9) {
           reqRow.summary = $(e).text();
         }
       });
       reqRow.collectId = getCollectId(tempDate, tempNo);
       
       if(reqRow.colgId == '') {
         bootbox.alert((i+1) + ' 번째 필드에 지급 대상이 선택되지 않았습니다.');
         valid = false;
         return;
       } else if(reqRow.collectId && reqRow.collectDay && reqRow.collectTime && reqRow.collectAmount) {
         // if($.isNumeric(reqRow.collectDay) && $.isNumeric(reqRow.collectTime) && $.isNumeric(reqRow.collectAmount) && reqRow.collectAmount >= 0) {
         if($.isNumeric(reqRow.collectDay) && $.isNumeric(reqRow.collectTime) && $.isNumeric(reqRow.collectAmount)) {
           valid = true;
           resultArray.push(reqRow);
         } else {
           bootbox.alert((i+1) + ' 번째 필드에 필수값 이상');
           valid = false;
           return;
         }
       } else {
         bootbox.alert((i+1) + ' 번째 필드에 필수값 없음');
         valid = false;
         return;
       }
     }
   });
   if(valid) {
     console.log('resultArray:',resultArray);
     
     bootbox.confirm('선택된 항목들을 업로드 하시겠습니까?', function(result) {
       if (result) {
         $.ajax({
           url: '/diff/add',
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
               bootbox.alert("입금정산 업로드에 성공하였습니다.", function() {
             	  document.location.href = '/diff/collect/form';
               });
             } else {
               bootbox.alert("입금정산 업로드에 실패하였습니다.");
             }
           },
           error : function(xhr, status, error) {
             bootbox.alert("입금정산 업로드에 실패하였습니다.");
           }
         });
       }
     });
   }
 }

 $(document).on('change', 'select.collect-group-category', function() {
 	var idx = $(this).attr('data-idx');
 	$.ajax({
 		type:'get',
 		url:'/collect/group/find/'+idx+'/' + $(this).val(),
 		success: function(res) {
 			appendSelectGroup(idx, res);
 		}, error: function(res) {
 			bootbox.alert('해당 항목이 없습니다.');
 		}
 	});
 });
    </script>
    <!-- 모달 생성을 위한 베이스 -->
    <div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>