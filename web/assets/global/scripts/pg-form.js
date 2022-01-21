var initForm = function() {
	$.validator.setDefaults({
		ignore: "",
		debug: true,
		highlight: function (element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },
        unhighlight: function (element) { // revert the change done by hightlight
            $(element).closest('.form-group').removeClass('has-error'); // set error class to the control group
        },
        success: function (label) {
        	label.closest('.form-group').removeClass('has-error'); 
            label.closest('.form-group').addClass('has-success'); 
        }
	});
	
	// Add validator Role
	jQuery.validator.addMethod("userId", function(value, element) {
	    var isValid =  /(^[0-9a-zA-Z]{5,30}$)/.test(value);
	    return this.optional(element) || isValid;
	},"영문, 숫자로된 5~30자를 입력할 수 있습니다.");
	
	jQuery.validator.addMethod("money", function(value, element) {
		    var isValidMoney =  /^[0-9,₩ ]*$/.test(value) ;
		    if(isValidMoney && value.replace(/[, ₩]'/g, '') < 0) isValidMoney = false;
		    return this.optional(element) || isValidMoney;
		},"금액만 입력할 수 있습니다.");
	
	jQuery.validator.addMethod("maxMoney", function(value, element, options) {
			value = replaceAll(value, ',', '');
			value = replaceAll(value, ' ₩', '');
			var isValidMoney = (value <= options) ? true : false;
			return this.optional(element) || isValidMoney;
		}, function(params, element) {
			return '최대 한도 : ' + params + ' ₩';
		});
	
	jQuery.validator.addMethod("minMoney", function(value, element, options) {
		value = replaceAll(value, ',', '');
		value = replaceAll(value, ' ₩', '');
		var isValidMoney = (value > options) ? true : false;
		return this.optional(element) || isValidMoney;
	}, function(params, element) {
		return '값이너무 작습니다. 연관된 하위 멤버의 한도를 다시 설정해야 합니다.';
	});
	
	jQuery.validator.addMethod("rate", function(value, element) {
		var isValid = /0.[0-9]{1,5}/g.test(value);
		if(isValid && value >= 1) isValid = false;
		
		return this.optional(element) || isValid;
		},"1 % 이하의 값을 입력하세요.");
	
	jQuery.validator.addMethod("settle", function(value, element) {
		var isValid = /0.[0-9]{1,5}/g.test(value);
		if(isValid && value >= 1) isValid = false;
		
		return this.optional(element) || isValid;
		},"1 % 이하의 값을 입력하세요.");
	
	jQuery.validator.addMethod("equals", function(value, element, param) { 
		  return this.optional(element) || value !== param; 
		}, "기존과 동일한 소속입니다.");
	
	jQuery.validator.addMethod(
	        "regex",
	        function(value, element, regexp) {
	            var re = new RegExp(regexp);
	            return this.optional(element) || re.test(value);
	        },"입력값을 확인해주세요.");
	
	// 필수항목 표시
	$('#writeFrm label.req-label').append('<span class="required">*</span>');
	
	// 토글다운 메뉴 변경사항 반영 (identity 등)
	$('#writeFrm .dropdown-menu>li>a').click(function() {
		var target = $(this).closest('ul').data('target');
		var buttonLabel = $(this).closest('.input-group-btn').find('.dropdown-toggle>span');
		var targetTxt = $(this).text();
		var inp = $('#writeFrm').find('input[name="'+target+'"]');
		inp.val(targetTxt);
		buttonLabel.text($(this).text());
	});
	
	// 은행 코드 변경시 은행 이름 자동기입
	$('#writeFrm .bankCd').bind('change',function(e) {
		var selected = $(this);
		var selectedId = $(this).val();
		var selectedText = $(this).find('option[value="'+selectedId+'"]').text();
		selectedText = selectedText == '은행 선택' ? '' : selectedText;
		$('#writeFrm').find('input[name="bankName"]').val(selectedText);
	});
	
	// 식별정보 변경 버튼 클릭 
	$('#writeFrm').on('click', '.change-identity', function() {
		var $modal = $('#pgmate-modal');
		if($modal.children().length < 1) {
			$modal.empty();
		}
		var groupElement = $(this).closest('.form-group');
		var typeTargetElement = groupElement.find('.change-target-idType');
		var identityElement = groupElement.find('.change-target-identity');
		
	    $modal.load('/common/changeIdentity.jsp', '', function(responseTxt, statusTxt, xhr){
	        if(statusTxt == "success"){
	        	$modal.modal();
	        	if(identityElement.hasClass('ceoIdentity')) {
	        		$modal.find('.dropdown-toggle>span').text("주민번호");
	        		$modal.find('input[name="idType"]').val("주민번호");
	        		$modal.find('.dropdown-toggle').attr('disabled',true);
	        	}
	        }
	    });
	    
	    $modal.on('hidden.bs.modal', function () {
	    	if($('#modalForm input[name="change-flag"]').val() == 'true') {
	    		typeTargetElement.val($('#modalForm input[name="idType"]').val());
	    		identityElement.val($('#modalForm input[name="identity"]').val());
	    	}
	    	$modal.empty();
	    });
	});
	
	
} 

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}

var addLimitRules = function(limitOnce, limitDay, limitMonth) {
	limitOnce > 0 && $.validator.addClassRules('limitOnce', { money : true, maxMoney : limitOnce });
	limitDay > 0 && $.validator.addClassRules('limitDay', { money : true, maxMoney : limitDay });
	limitMonth > 0 && $.validator.addClassRules('limitMonth', { money : true, maxMoney : limitMonth });
}

var addLimitLowRules = function(limitOnceMin, limitDayMin, limitMonthMin) {
	limitOnceMin > 0 && $.validator.addClassRules('limitOnce', { money : true, minMoney : limitOnceMin });
	limitDayMin > 0 && $.validator.addClassRules('limitDay', { money : true, minMoney : limitDayMin });
	limitMonthMin > 0 && $.validator.addClassRules('limitMonth', { money : true, minMoney : limitMonthMin });
}

var addLimitHighLowRules = function(limitOnce, limitDay, limitMonth, limitOnceMin, limitDayMin, limitMonthMin) {
	limitOnce > 0 && limitOnceMin > 0 && $.validator.addClassRules('limitOnce', { money : true, maxMoney : limitOnce, minMoney : limitOnceMin });
	limitDay > 0 && limitDayMin > 0 && $.validator.addClassRules('limitDay', { money : true, maxMoney : limitDay, minMoney : limitDayMin });
	limitMonth > 0 && limitMonthMin > 0 && $.validator.addClassRules('limitMonth', { money : true, maxMoney : limitMonth, minMoney : limitMonthMin });
}

function ajaxFormSubmit(form, redirectUrl){
	event.preventDefault();
	var action = "";
	var keyDataArr = new Array();
	var activeType = $(form).find("[name='action_type']").val();
	var searchKeyCnt = 0;
	$(form).find('input, select, textarea').each(function(i, e) {		
		var isSearchKey = $(e).data("key");
		var name = $(e).attr("name");
		var type = $(e).attr("type");
		var value = $(e).val();
		
		var checked = false;
		if(type == "checkbox" || type == "radio"){
			if($(e).prop("checked") == true) checked = true;
			else checked = false;
		}else{
			checked = true;
		}
		
		var keyData = new Object();
		keyData.name = name;
		keyData.order = "";
		var val = $(e).val();
		if($(e).attr('name') == 'summary' && $('#summary-result')){
			val = $('#summary-result').html();
		}else if($(e).parent().hasClass('input-daterange') || $(e).hasClass('datepicker')){
			val = $(e).val().replace(/-/gi, '');
		}else if($(e).hasClass('datepicker')){
			val = $(e).val().replace(/-/gi, '');
		}else if($(e).hasClass('timeInput')){
			val = $(e).val().replace(/:/gi, '');
		}
		
		keyData.val = val;
		if(isSearchKey == true){
			keyData.oper = "eq";
			keyData.key = true;
			keyDataArr.push(keyData);
			searchKeyCnt++;
		}else if($(e).data("reg") != false && name && value && checked) {
			keyData.oper = "";
			keyData.key = false;
			keyDataArr.push(keyData);
		}
	});
	
	if(activeType == 'update' && searchKeyCnt < 1) {
		bootbox.alert('업데이트에 대상이 올바르게 설정되지 않았습니다.');
		return;
	}
	
	var executeData = new Object();
	executeData.type = action;
	executeData.reason = activeType;
	executeData.redirect = redirectUrl;
	executeData.data = keyDataArr;
	
	var dataJson = JSON.stringify(executeData);
	// ========================================================================================
	// console.log("request : "+ dataJson);
	
	$.ajax({
		type : "post",
		url : $(form).attr('action') + activeType,
		dataType : "json",
		beforeSend : function(xhr){
			xhr.setRequestHeader("Content-type","application/json");
		},
		data : dataJson,
		success: function(json, textStatus){
			if(json.result.code == "200"){
				bootbox.alert(json.result.message, function() {
					if(json.redirect){
						location.href = json.redirect;
					}
				});
			}else{
				bootbox.dialog({
				    title: json.result.message,
				    message: '<p>'+json.result.error+'</p>'
				});
			}
		},
		error: function(xhr, textStatus, errorThrown){
			bootbox.alert('Error ' + errorThrown);
		}
	});
}

var gradeSelector = function(submitForm, userGrade, targetGrade, targetInput, debug) {
	if(debug) console.log("INIT : USER = " + userGrade + " / TARGET = " + targetGrade);
	//if(subText(userGrade) == targetGrade || userGrade == targetGrade){
	if(userGrade == targetGrade || userGrade == '선정산' || userGrade == '지사'  || userGrade == '터미널' || userGrade.indexOf('가맹점') > -1){	
		$('#gradeSelector').hide();
		return;
	}
	
	if(typeof submitForm === 'string'){
		submitForm = $('#' + submitForm);
		if(!submitForm) {
			submitForm = $('#writeFrm');
		} 
	}
	if(!submitForm) {
		if(debug) console.log("소속선택에 사용할 폼 정보가 없습니다.");
	}
	
	if($(submitForm).attr("id") == "searchForm") {
		targetInput = $('#grade_search');
	} else if(!targetInput) {
		targetInput = $(submitForm).find('input[name="parentId"]');
	} else if(typeof targetInput === 'string') {
		targetInput = $(submitForm).find('input[name="'+targetInput+'"]');
	}
	if(!targetInput) {
		if(debug) console.log("소속선택에 사용할 ID 를 입력할 INPUT이 지정되지 않았습니다.");
	}
	
	$('#gradeSelector .selectedTargetGrade').val(subText(userGrade));
	
	if(targetGrade) {
		$('#gradeSelector select.selectedTargetGrade').append('<option value="'+targetGrade+'">'+targetGrade+'</option>');
		$('#gradeSelector .selectedTargetGrade').closest('.selecter-wrapper').addClass('hide');
		$('#gradeSelector .selectGrade-label').text(targetGrade + ' 선택');
	}else {
		targetGrade = subText(userGrade);
		if(targetGrade == '대행사') {
			$('#gradeSelector select.selectedTargetGrade').append('<option value="대행사">대행사</option>');
			$('#gradeSelector select.selectedTargetGrade').append('<option value="에이전시">에이전시</option>');
			$('#gradeSelector select.selectedTargetGrade').append('<option value="지사">지사</option>');
		}else if(targetGrade == '에이전시'){
			$('#gradeSelector select.selectedTargetGrade').append('<option value="에이전시">에이전시</option>'); 
			$('#gradeSelector select.selectedTargetGrade').append('<option value="지사">지사</option>');
		}else if(targetGrade == '지사'){
			$('#gradeSelector select.selectedTargetGrade').append('<option value="지사">지사</option>'); 
		}
	}
	$('#gradeSelector select.depth1').attr('name', subText(userGrade));
	$('#gradeSelector select.depth1').prepend('<option value="">'+subText(userGrade)+' 선택</option>'); 
	$('#gradeSelector select.depth1').val(1);
	
	$('#gradeSelector .selecterGrade, .selectedTargetGrade').bind('change',function(e) {
		var selected = $(this); 																			// 선택된 객체
		var selectedGrade = $(this).attr("name"); 															// 선택된 소속
		var selectedId = $(this).val(); 																	// 선택된 아이디
		var targetGrade = $('#gradeSelector .selectedTargetGrade').selectpicker('val'); 					// 목표 소속
		if(debug) console.log("선택된 소속: " + selectedGrade+ " /선택된 아이디: " + selectedId + " /목표 소속: "+ targetGrade);
					
		if (selected.hasClass('selectedTargetGrade')) { // IF : 소속 선택이 바뀜
			if(debug) console.log("목표 소속 변경 => " + targetGrade);
			$('#gradeSelector select.depth1').val(1);
			$('#gradeSelector select.depth1').selectpicker('refresh');
			$('#gradeSelector select.depth1').closest('.selecter-wrapper').nextAll('.selecter-wrapper').addClass('hide');
			$('#gradeSelector').attr("data-last-selected", "false");
			$('#gradeSelector .confirm-btn').addClass('disabled');
		} else if (selectedId == "") { // IF : 현재 단계 초기화됨
			if(debug) console.log("단계 초기화");
			selected.closest('.selecter-wrapper').nextAll('.selecter-wrapper').addClass('hide');
			$('#gradeSelector').attr("data-last-selected", "false");
			$('#gradeSelector .confirm-btn').addClass('disabled');
		} else if (targetGrade == selectedGrade) { // IF : 최종 단계를 선택함
			var targetId = selected.selectpicker('val');
			var targetName = selected.find("option[value='"+ selected.selectpicker('val')+ "']").text();
			var targetGrade = targetGrade;
			if(debug) console.log("최종 단계(" + selectedGrade+ ") 선택 : " + targetId + " "+ targetName);
			$('#gradeSelector').attr("data-res-id", selected.selectpicker('val'));
			$('#gradeSelector').attr("data-res-name", selected.find("option[value='"+ selected.selectpicker('val')+ "']").text());
			$('#gradeSelector').attr("data-res-grade", targetGrade);
			$('#gradeSelector').attr("data-last-selected", "true");
			$('#gradeSelector .confirm-btn').removeClass('disabled');
		} else { // IF : 단계(최종이 아닌)를 선택함
			if(debug) console.log("단계(최종이 아닌)를 선택 =>" + selectedGrade);
			$.post("/member/user/childrenList",{"grade" : selectedGrade,"parentId" : selectedId},function(data) {
				selected.closest('.selecter-wrapper').next().removeClass('hide');
				var nextSelect = selected.closest('.selecter-wrapper').next().find("select");
				nextSelect.empty();
				nextSelect.prepend('<option value="">'+subText(selectedGrade)+' 선택</option>'); 
				$.each(data,function(i,e) {console.log('<option value="'+ e.id +'">'+ e.name+ '</option>');
					nextSelect.append('<option value="'+ e.id +'">'+ e.name+ '</option>');
				});
				nextSelect.attr("name",subText(selectedGrade));
				nextSelect.selectpicker('refresh');
			});
		}
	});
	
	// 선택 확정 버튼 클릭 (선택 변경 버튼으로 토클)
	$('#gradeSelector .confirm-btn').bind('click', function() {
		if($('#gradeSelector .confirm-btn').hasClass("change-btn")){
			$('#gradeSelector .selectpicker').attr('disabled', false).selectpicker('refresh');
			$('#gradeSelector .confirm-btn').addClass('green').removeClass('blue-dark').removeClass("change-btn").text("선택");
			
			$(targetInput).val('');
			$(submitForm).find('input[name="parentName"]').val('');
			$(submitForm).find('input[name="grade"]').val('');
		} else if($('#gradeSelector').attr("data-last-selected") == "true") {
			$('#gradeSelector .confirm-btn').text("변경").addClass("change-btn").removeClass('green').addClass('blue-dark');
			$('#gradeSelector .selectpicker').attr('disabled', true).selectpicker('refresh');
			
			$(targetInput).val($('#gradeSelector').attr("data-res-id"));
			
			if($(submitForm).attr("id") == 'searchForm') {
				$(targetInput).attr("name", toTableId($('#gradeSelector').attr("data-res-grade")));
			} else {
				$(submitForm).find('input[name="parentName"]').val($('#gradeSelector').attr("data-res-name"));
				$(submitForm).find('input[name="grade"]').val($('#gradeSelector').attr("data-res-grade"));
			}
		}
	});

	function subText(orgText) {
		if(orgText == "본사") {return "대행사";
		} else if (orgText == "대행사") {return "에이전시";
		} else if (orgText == "에이전시") {return "지사";
		} else {return "";}
	}
	
	function toTableId(orgText) {
		if (orgText == "대행사") {return "distId";
		} else if (orgText == "에이전시") {return "agencyId";
		} else if (orgText == "지사") {return "salesId";
		} else {return "";}
	}
}

function postCode($this, zip, addr1, addr2, lat, lng) {
	
//	var geocoder = new daum.maps.services.Geocoder();
	
	new daum.Postcode({
        oncomplete: function(data) {
        	// 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = ''; // 최종 주소 변수
            var extraAddr = ''; // 조합형 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                fullAddr = data.roadAddress;

            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                fullAddr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
            if(data.userSelectedType === 'R'){
                //법정동명이 있을 경우 추가한다.
                if(data.bname !== ''){
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if(data.buildingName !== ''){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            zip.val(data.zonecode);
            addr1.val(fullAddr);
            if(addr2) addr2.val(extraAddr);
            
//            geocoder.addressSearch(data.address, function(results, status) {
//                // 정상적으로 검색이 완료됐으면
//                if (status === daum.maps.services.Status.OK) {
//
//                    var result = results[0]; //첫번째 결과의 값을 활용
//                    if(lat) lat.val(result.y); // latitude 위도
//                	if(lng) lng.val(result.x); // longitude  경도
//                }
//            });
            
            // 커서를 상세주소 필드로 이동한다.
            addr2.focus();
        }
    }).open();
}

// 하위 사용자 비밀번호 초기화
function resetPassword(grade, id ) {
	bootbox.confirm('비밀번호를 재설정하고 임시 비밀번호를 발급하시겠습니까?', function(result) {
		if(result) {
			if(grade == 'user') {
				$.get("/member/user/resetPassword/" + id, function( data ) {
					bootbox.alert(data.msg);
				});
			}
		}
	});
}



(function() {
	initForm();

}());

