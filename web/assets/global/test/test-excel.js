
//csv 파일 업로드 시 리스트로 보여주기
$('#upload-excel').change(function(event){
	processFile(event.target.files[0]);
})

//csv 파일 to 리스트
function processFile(file){
	var reader = new FileReader();
	reader.readAsText(file,'euc-kr');
	
	reader.onload = function(){
		var allRow = reader.result;
		allRow = allRow.replaceAll(/[\=\"]/g,'');
		var lines = allRow.split('\n');
		
		for(var i=0; i<lines.length; i++){
			if(lines[i] != ''){
				var coll = lines[i].split(',');
				$('tbody').append($('<tr>'));
				for(var j=0; j<coll.length; j++){
					if(i == 0){
						$('thead').find('tr').append($('<th>').text(coll[j]));
					}else{
						$('tbody tr').last().append($('<td>').text(coll[j]));
					}
				}
			}
		}
	}
}

//선택한 파일 업로드
function uploadFile(){
	
	//사용자가 올린 파일 받음
	var data = $('#upload-excel')[0];
	var formData = new FormData();
	formData.append('file', data.files[0]);
	
	//지정된 경로에 대한 백단으로 파일 보내줌
	$.ajax({
		url: '/trxoper/uploadFile',
		data: formData,
		type: 'post',
		contentType: false,
		processData: false,
		success: function(result){
			alert(result);
		},
		 error: function(xhr, status, error) {
            console.log('실패');
        }
	})
}
