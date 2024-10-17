// 팝업창 관련
function openPassword(url) {
	window
			.open(
					url,
					"비밀번호 변경",
					"width=500, height=500, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}

// 알림 관련
function refreshAlertCount() {
	$('#alert-count').text($('#alert-menu').find('li').length > 0 ? $('#alert-menu').find('li').length : '');
}

// 임시비번 생성
/*
 * function getTempPassword() { return fillzero(Math.floor(Math.random() *
 * 100000) + 1, 5); }
 */

// 정산 확정 처리 - 통신
function settleStatusUpdate(status, stlId, isSub) {
	bootbox.confirm('선택된 항목들을 모두 ' + status + '처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/settle/status/' + (isSub ? 'sub/' : '') + status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

// 정산 지급완료 처리 - 통신
function settlePayStatusUpdate(status, stlId, isSub) {
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/settle/paystatus/' + (isSub ? 'sub/' : '') + status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

// 정산 지급 데이터 EXPORT
function settlePayOutExport(bankCd, stlId, isSub, grade) {
	console.log("GRADE ==== ", grade);

	if(isSub) {
		$('body').append('<form action="/settle/sub/export" method="POST" id="excel_export_form"></form>');
	} else {
		$('body').append('<form action="/settle/export" method="POST" id="excel_export_form"></form>');
	}
	$('#excel_export_form').append('<input type="hidden" name="stlId" value="'+stlId+'">');
	$('#excel_export_form').append('<input type="hidden" name="bankCd" value="'+bankCd+'">');
	$('#excel_export_form').append('<input type="hidden" name="grade" value="'+grade+'">');
	$('#excel_export_form').submit();

	$.ajax({
		url : '/settle' + (isSub ? '/sub/export' : '/export'),
		type : 'POST',
		data : $('#excel_export_form').serialize(),
		success : function(json, textStatus) {
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("정산 상태 변경에 실패헸습니다.");
		},
		complete : function(data) {
			$('#excel_export_form').remove();
			searchForList();
		}
	});
}

function settleDetailDownload(gradeId, stlId, isSub) {
	$.ajax({
		url : '/settle/detail' + (isSub ? '/sub' : ''),
		type : 'POST',
		data : { 'grade' : gradeId, 'stlId' : stlId },
		success : function(json, textStatus) {
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("정산 상태 변경에 실패헸습니다.");
		},
		complete : function(data) {
			searchForList();
		}
	});
}

//가상계좌 정산 확정 처리 - 통신
function vactSettleStatusUpdate(status, stlId, isMcht) {
	bootbox.confirm('선택된 항목들을 모두 ' + status + '처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/vactSettle/status/' + (isMcht ? 'mcht/' : '') + status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

// 가상계좌 정산 지급완료 처리 - 통신
function vactSettlePayStatusUpdate(status, stlId, isMcht) {
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/vactSettle/paystatus/' + (isMcht ? 'mcht/' : '') + status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

// 정산 지급 데이터 EXPORT
function vactSettlePayOutExport(bankCd, stlId, isMcht, grade) {
	console.log("GRADE ==== ", grade);
	$('body').append('<form action="/vactSettle/export" method="POST" id="excel_export_form"></form>');
	$('#excel_export_form').append('<input type="hidden" name="stlId" value="'+stlId+'">');
	$('#excel_export_form').append('<input type="hidden" name="bankCd" value="'+bankCd+'">');
	$('#excel_export_form').append('<input type="hidden" name="grade" value="'+grade+'">');
	//$('#excel_export_form').submit();
	
	$.ajax({
		url : '/vactSettle' + (isMcht ? '/mcht/export' : '/export'),
		type : 'POST',
		data : $('#excel_export_form').serialize(),
		success : function(json, textStatus) {
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("정산 상태 변경에 실패헸습니다.");
		},
		complete : function(data) {
			$('#excel_export_form').remove();
			searchForList();
		}
	});
}

function vactSettleDetailDownload(gradeId, stlId, isSub) {
	$.ajax({
		url : '/vactSettle/detail' + (isSub ? '/sub' : ''),
		type : 'POST',
		data : { 'grade' : gradeId, 'stlId' : stlId },
		success : function(json, textStatus) {
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("엑셀 다운로드에 실패헸습니다.");
		},
		complete : function(data) {
			searchForList();
		}
	});
}

// 월세 정산 확정 처리 - 통신
function rentSettleStatusUpdate(status, stlId, isSub) {
	bootbox.confirm('선택된 항목들을 모두 ' + status + '처리 하시겠습니까?', function (result) {
		if (result) {
			$.ajax({
				url: '/rent/distSettle/status/' + (isSub ? 'sub/' : '') + status,
				type: 'POST',
				dataType: "text",
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Content-type",
						"application/json;charset=utf-8");
				},
				data: stlId,
				success: function (json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error: function (xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete: function (data) {
					searchForList();
				}
			});
		}
	});
}

// 월세 정산 지급완료 처리 - 통신
function rentSettlePayStatusUpdate(status, stlId, isSub) {
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/rent/distSettle/paystatus/' + (isSub ? 'sub/' : '') + status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
						"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

// 월세 엑셀 다운
function rentSettleDetailDownload(gradeId, stlId, isSub) {
	$.ajax({
		url : '/rent/distSettle/detail' + (isSub ? '/sub' : ''),
		type : 'POST',
		data : { 'grade' : gradeId, 'stlId' : stlId },
		success : function(json, textStatus) {
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("엑셀 다운로드에 실패헸습니다.");
		},
		complete : function(data) {
			searchForList();
		}
	});
}

function redirectToMain() {
	window.top.location.href = '/'
}

// 거래 리스크 변경
function trxStlStatusChange(risk, stlId, summary) {
	$('body').append('<form action="/trx/cap/risk" method="POST" id="status_change_form"></form>');
	$('#status_change_form').append('<input type="hidden" name="capId" value="'+stlId+'">');
	$('#status_change_form').append('<input type="hidden" name="risk" value="'+risk+'">');
	$('#status_change_form').append('<input type="hidden" name="summary" value="'+summary+'">');
	
	//console.log(risk, stlId, summary);
	$.ajax({
		url : '/trx/cap/risk',
		type : 'POST',
		data : $('#status_change_form').serialize(),
		success : function(json, textStatus) {
			if(json.indexOf('NOK') > -1) {
				bootbox.alert(json.substring(4));
			} else {
				bootbox.alert(json.substring(3));
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("리스크 변경에 실패헸습니다.");
		},
		complete : function(data) {
			$('#status_change_form').remove();
			searchForList();
		}
	});
}


//가맹점정산 상태변경 처리 - 통신
function mchtSettleDecide(status, stlId) {
	console.log('mchtSettleDecide:',status, stlId);

	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/settle/mcht/make/'+ status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

//가맹점정산 지급보류 상태변경 처리 - 통신
function mchtSettleHoldStatus(status, stlId) {
	console.log('mchtSettleHoldStatus:',status, stlId);
	
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/settle/mcht/hold/'+ status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

//가맹점정산 지급 상태변경 처리 - 통신
function mchtSettlePayoutChange(status, stlId) {
	console.log('mchtSettlePayoutChange:',status, stlId);
	
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/settle/mcht/payout/'+ status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}


//대출정산 지급 상태변경 처리 - 통신
function loanSettlePayoutChange(status, loanStlId) {
	console.log('loanSettlePayoutChange:',status, loanStlId);
	
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/settle/loanSettle/payout/'+ status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				data : loanStlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}

function fnExcelReport(tableId, fileName) {
	$('.fill-input').each(function(i) {
		var sele = $(this).find('input').val();
		$(this).text(sele);
		$(this).find('input').hide();
	});
	$('.excel-hide').remove();
	$('.excel-show').show();

	var tab_text = "<table border='2px'><tr>";
	var textRange;
	var j = 0;
	tab = document.getElementById(tableId); // id of table
	for (j = 0; j < tab.rows.length; j++) {
		tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
	}

	tab_text = tab_text + "</table>";
	tab_text = tab_text.replace(/<a[^>]*>|<\/a>/g, "");//remove if u want links in your table
	tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
	tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params
	tab_text = tab_text.replace(/<br[^>]*>|<\/br>/gi, ""); // reomves br
	tab_text = tab_text.replace(/<td class="ck-td[^>]*>|<\/td>/gi, ""); // reomves
	tab_text = tab_text.replace(/<th class="ck-th[^>]*>|<\/th>/gi, ""); // reomves

	var ua = window.navigator.userAgent;
	var msie = ua.indexOf("MSIE ");

	if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) // If Internet Explorer
	{
		txtArea1.document.open("txt/html", "replace");
		txtArea1.document.write(tab_text);
		txtArea1.document.close();
		txtArea1.focus();
		sa = txtArea1.document.execCommand("SaveAs", true, fileName + ".xls");
	} else { //other browser not tested on IE 11
		var sa = document.getElementById("excel-click");
		$(sa).attr('download', fileName + '.xls')
		$(sa).attr('href','data:application/vnd.ms-excel,'+ encodeURIComponent(tab_text));
		$(sa).css('display', 'inline-block');

		//sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));  
	}
}




//거래 리스크 변경
function trxCapDelete(stlId, summary) {
	$('body').append('<form action="/trx/cap/risk" method="POST" id="status_change_form"></form>');
	$('#status_change_form').append('<input type="hidden" name="capId" value="'+stlId+'">');
	$('#status_change_form').append('<input type="hidden" name="summary" value="'+summary+'">');
	
	$.ajax({
		url : '/trx/capdel/action',
		type : 'POST',
		data : $('#status_change_form').serialize(),
		success : function(json, textStatus) {
			if(json.indexOf('NOK') > -1) {
				bootbox.alert(json.substring(4));
			} else {
				bootbox.alert(json.substring(3));
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("거래 삭제 변경에 실패헸습니다.");
		},
		complete : function(data) {
			$('#status_change_form').remove();
			searchForList();
		}
	});
}

function pad(n, width, z) {
	z = z || '0';
	n = n + '';
	return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
}

function trxNotiRetry(idx) {
	console.log('trxNotiRetry:', idx);

	bootbox.confirm('선택된 항목을 재전송 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/trx/noti/retry/'+ idx,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
						"application/json;charset=utf-8");
				},
				data : idx,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("재전송 완료했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("재전송 실패했습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}
// 가상계좌 노티 재전송
function vactNotiRetry(idx) {
	console.log('vactNotiRetry:', idx);

	bootbox.confirm('선택된 항목을 재전송 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				url : '/vact/noti/retry/'+ idx,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
						"application/json;charset=utf-8");
				},
				data : idx,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					if (json.result == 'OK') {
						bootbox.alert("재전송 완료했습니다.");
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("재전송 실패했습니다.");
				},
				complete : function(data) {
					searchForList();
				}
			});
		}
	});
}


