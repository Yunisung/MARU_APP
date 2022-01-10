<%@ page contentType="text/html; charset=UTF-8"%>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script type="text/javascript">
	var id = '${mchtId}';
	
	if(confirm("이메일 수신거부 하시겠습니까?") == true){
		$.ajax({
			type : "POST",
			url : "/mcht/email/update",
			data: ({ id : id }),
			success : function(data) {
				alert("이메일 수신거부 처리 되었습니다.");
				window.close();
			},
			error : function(request,status,error) {
				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				alert("이메일 수신거부 처리 실패되었습니다. 관리자에 문의바랍니다.");
			}
		});
	}
</script>
