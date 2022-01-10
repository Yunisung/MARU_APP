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

function handleFile(e) {
  var files = e.target.files;
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
			if(i == 0) {
              $(e).prepend('<th><input type="checkbox" class="all-check" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>');
              $(e).find('td').contents().unwrap().wrap('<th></th>');
              $('#sortTable>thead>tr').html($(e).html());
            } else {
              $(e).prepend('<td class="btn-td"><input type="checkbox" class="row-check" name="ck_'+i+'" id="ck_'+i+'" class="checkbox-style" /><label for="ck_'+ i +'"></label></td>');
              
              $('#sortTable tbody').append($(e));
            }
          });
        }
      }); //end. forEach
    }; //end onload

    if (rABS) reader.readAsBinaryString(f);
    else reader.readAsArrayBuffer(f);

  } //end. for
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

		$(trElem).children('td').each(function(i, e) {
			if(i == 1) {
	          reqRow.bankNm = $(e).text();
	        } else if(i == 2) {
	          reqRow.account = $(e).text().replace(/-/gi, "");
	        } else if(i == 3) {
	          reqRow.amount = $(e).text().replace(/,/gi, "");
	        } else if(i == 4) {
	          reqRow.holder = $(e).text();
	        } else if(i == 5) {
	          reqRow.receiverDisplay = $(e).text();
	        } else if(i == 6) {
	          reqRow.senderDisplay = $(e).text();
	        } 
		});

		if(reqRow.bankNm == '') {
			bootbox.alert((i+1) + ' 번째 필드에 은행명이 없습니다.');
	        valid = false;
	        return;
		}else if(reqRow.account == '') {
			bootbox.alert((i+1) + ' 번째 필드에 계좌번호가 없습니다.');
	        valid = false;
	        return;
		}else if(reqRow.amount == '') {
			bootbox.alert((i+1) + ' 번째 필드에 이체금액이 없습니다.');
	        valid = false;
	        return;
		}else if(reqRow.holder == '') {
			bootbox.alert((i+1) + ' 번째 필드에 예금주가 없습니다.');
	        valid = false;
	        return;
		}else if(reqRow.receiverDisplay == '') {
			bootbox.alert((i+1) + ' 번째 필드에 받는분통장표시내용이 없습니다.');
	        valid = false;
	        return;
		}else if(reqRow.senderDisplay == '') {
			bootbox.alert((i+1) + ' 번째 필드에 본인통장표시내용이 없습니다.');
	        valid = false;
	        return;
		} else if(reqRow.account && reqRow.amount) {
	        if($.isNumeric(reqRow.account) && reqRow.amount > 0) {
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
	bootbox.confirm('선택된 항목들을 정산처리 하시겠습니까?', function(result) {
		if (result) {
	        $.ajax({
	          url: '/settle/excel/save',
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
	              bootbox.alert("지급대행 정산처리에 성공하였습니다.", function() {
	            	  document.location.href = '/settle/excel/form';
	              });
	            } else {
	              bootbox.alert("지급대행 정산처리에 실패하였습니다.");
	            }
	          },
	          error : function(xhr, status, error) {
	            bootbox.alert("지급대행 정산처리에 실패하였습니다.");
	          }
	        });
		}
	});
  }
}

function importExcelSettleData() {
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
        if($.isNumeric(reqRow.collectDay) && $.isNumeric(reqRow.collectTime) && $.isNumeric(reqRow.collectAmount) && reqRow.collectAmount > 0) {
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
}

//http://sheetjs.com/
//https://github.com/SheetJS/js-xls