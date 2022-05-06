// pg-common.js => ajax 통신 기능 정의

// 팝업창 관련
/*
	KJM : 내 정보 > 비밀번호 변경
	url : /member/user/password/${DATAMAP.id}
*/
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
//KJM : 은행 지급 데이터 (은행용 지급 데이터 다운로드)
function settlePayOutExport(bankCd, stlId, isSub, grade) {
	console.log("GRADE ==== ", grade);
	//KJM : 보내줄 데이터 세팅위한 폼 생성
	$('body').append('<form action="/settle/export" method="POST" id="excel_export_form"></form>');
	$('#excel_export_form').append('<input type="hidden" name="stlId" value="'+stlId+'">');
	$('#excel_export_form').append('<input type="hidden" name="bankCd" value="'+bankCd+'">');
	$('#excel_export_form').append('<input type="hidden" name="grade" value="'+grade+'">');
	$('#excel_export_form').submit();
	
	$.ajax({
		url : '/settle' + (isSub ? '/sub/export' : '/export'),
		type : 'POST',
		data : $('#excel_export_form').serialize(),
		success : function(json, textStatus) {
			//KJM : 정상적 수행 후 데이터 받았다면 json안에 message 값이 없음 = undefined
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("정산 상태 변경에 실패헸습니다.");
		},
		//KJM : ajax 통신 완료 후 생성한 폼 삭제 후 리스트 새로 가져옴
		complete : function(data) {
			$('#excel_export_form').remove();
			searchForList();
		}
	});
}

//KJM : 정산 대상거래 엑셀파일 다운로드 ajax 통신
function settleDetailDownload(gradeId, stlId, isSub) {
	$.ajax({
		url : '/settle/detail' + (isSub ? '/sub' : ''),
		type : 'POST',
		data : { 'grade' : gradeId, 'stlId' : stlId },
		success : function(json, textStatus) {
			//KJM : 정상적으로 기능이 수행 되었다면 json.message에 값이 없음
			if(typeof json.message == "undefined"){
				location.href = json.file.link;
			//KJM : 정상적으로 수행 안됐을 경우 메시지 알림창으로 띄워줌
			}else{
				bootbox.alert(json.message);
			}
		},
		error : function(xhr, status, error) {
			bootbox.alert("정산 상태 변경에 실패헸습니다.");
		},
		//KJM : ajax 기능이 수행된 뒤 리스트 다시 보내줌
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

function redirectToMain() {
	window.top.location.href = '/'
}

// 거래 리스크 변경
function trxStlStatusChange(risk, stlId, summary) {
	//KJM : 리스크 변경하는 매입건에 대한 데이터 넣기위한 form 생성
	$('body').append('<form action="/trx/cap/risk" method="POST" id="status_change_form"></form>');
	$('#status_change_form').append('<input type="hidden" name="capId" value="'+stlId+'">');	//KJM : 매입번호
	$('#status_change_form').append('<input type="hidden" name="risk" value="'+risk+'">');		// 리스크 종류
	$('#status_change_form').append('<input type="hidden" name="summary" value="'+summary+'">');//변경 사유
	
	//console.log(risk, stlId, summary);
	$.ajax({
		//KJM : controller에서 받을 url
		url : '/trx/cap/risk',
		type : 'POST',
		data : $('#status_change_form').serialize(),
		//KJM : string형식의 반환값으로 성공/실패 구분
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
		//KJM : 데이터 주기위해 만들었던 form 제거 후 매입건 리스트 들고옴
		complete : function(data) {
			$('#status_change_form').remove();
			searchForList();
		}
	});
}

//가맹점정산 상태변경 처리 - 통신
function mchtSettleDecide(status, stlId) {
	//KJM : 가맹점 정산 생성 > 정산 확정 시 ("확정", 인덱스)
	console.log('mchtSettleDecide:',status, stlId);
	
	//KJM : 확인창 띄워준 뒤 상태변경 수행
	bootbox.confirm('선택된 항목들을 모두 ' + status + ' 처리 하시겠습니까?', function(result) {
		if (result) {
			$.ajax({
				//KJM : controller url
				url : '/settle/mcht/make/'+ status,
				type : 'POST',
				dataType : "text",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Content-type",
							"application/json;charset=utf-8");
				},
				//KJM : 선택한 리스트 인덱스
				data : stlId,
				success : function(json, textStatus) {
					json = JSON.parse(json);
					//KJM : 정상 수행 시
					if (json.result == 'OK') {
						bootbox.alert("정산 상태를 변경했습니다.");
					//KJM : 수행 실패 시 실패 메시지 띄워줌
					} else {
						bootbox.alert(json.msg);
					}
				},
				error : function(xhr, status, error) {
					bootbox.alert("정산 상태 변경에 실패헸습니다.");
				},
				//KJM : ajax 끝난 뒤 리스트 조회해옴
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
	
	//KJM : 변경 확인 창 띄워줌
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
//KJM : 정산기능(지급완료, 보류, 삭제), 정산번호 
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
					//KJM : 수행 결과에 따라서 확인창 띄워줌
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
// KBR ('sortTable', '입금정산내역')
function fnExcelReport(tableId, fileName) {
	
	console.log(fileName)
	console.log(tableId)
	
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
		// KBR 엑셀 icon 클릭 시 
		console.log('excel click ! ')
		var sa = document.getElementById("excel-click");
		// 상단에서 받아온 파일 이름 셋팅 
		$(sa).attr('download', fileName + '.xls')
		// 엑셀 위치??
		$(sa).attr('href','data:application/vnd.ms-excel,'+ encodeURIComponent(tab_text));
		// 버튼 보여주기 
		$(sa).css('display', 'inline-block');

		//sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));  
	}
}

//거래 리스크 변경
//KJM : 매입 삭제
function trxCapDelete(stlId, summary) {
	//KJM : ajax에 데이터 보내기용 form 만들어줌
	$('body').append('<form action="/trx/cap/risk" method="POST" id="status_change_form"></form>');
	$('#status_change_form').append('<input type="hidden" name="capId" value="'+stlId+'">');
	$('#status_change_form').append('<input type="hidden" name="summary" value="'+summary+'">');
	
	$.ajax({
		url : '/trx/capdel/action',
		type : 'POST',
		//KJM : serialize() 메소드를 사용하여 폼의 객체들을 한번에 보냄
		//{capId = ...}, {summary = ...}
		data : $('#status_change_form').serialize(),
		success : function(json, textStatus) {
			//KJM : 반환된 데이터 중 'NOK'라는 단어가 있으면 데이터 삭제 안된 경우
			//KJM : 반환값 "OK..." or "NOK..."
			if(json.indexOf('NOK') > -1) {
				//KJM : 알림창 띄워줌
				//KJM : "데이터가 삭제되지 않았습니다. 관리자에게 문의 바랍니다."
				bootbox.alert(json.substring(4));
			} else {
				//KJM : "데이터가 삭제되었습니다."
				bootbox.alert(json.substring(3));
			}
		},
		//KJM : 에러 발생 시
		error : function(xhr, status, error) {
			bootbox.alert("거래 삭제 변경에 실패헸습니다.");
		},
		//KJM : 위의 메소드 실행 후 데이터 보내기 용으로 만들었던 form 삭제
		complete : function(data) {
			$('#status_change_form').remove();
			//KJM : pg-search.js > searchForList() 메소드 실행
			//KJM : 매입건 리스트 가져옴
			searchForList();
		}
	});
}

function pad(n, width, z) {
	z = z || '0';
	n = n + '';
	return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
}
