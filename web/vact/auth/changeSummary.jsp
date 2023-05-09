<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<head>
    <c:import url="/include/head.jsp" />
</style>
</head>

<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h4>가상계좌 인증 내역 비고 일괄변경</h4>
</div>
<div class="modal-body">

    <div class="tab-content">
        <div class="tab-pane active" id="tab1">
            <div class="portlet light portlet-form">
                <div class="portlet-body form light">
                    <form class="form-horizontal" role="form" id="dayForm" action="#">
                        <div class="form-group">
                            <label for="lsummary" class="col-md-2 control-label">비고 </label>
                            <div class="col-md-8">
                                <textarea rows="4" name="summary" id="summary" placeholder="Write comment here ..." class="form-control input-md" maxlength="200"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-offset-2 col-md-10">
                                <button class="btn purple" type="button" id="dayButton">비고변경 </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
</div>
<script>
    $('#dayButton').on('click', function () {

        bootbox.confirm("비고를 일괄변경하시겠습니까?",function(result){
            if(result){
                var json = new Object();
                json.trxId = trxArr;
                json.summary = $('#summary').val();

                $.ajax({
                    type: "post",
                    url: "/vact/auth/changeSummary",
                    data: JSON.stringify(json),
                    contentType: "application/json",
                    success: function (json) {
                        if(json.resultCd == '0000'){
                            bootbox.alert(json.resultMsg,function(){
                                pageMove();
                                $('#pgmate-modal').modal('hide');
                            });
                        }else{
                            bootbox.alert(json.resultMsg);
                        }

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        bootbox.alert('Error ' + errorThrown);
                    }
                });
            }

        });

    });
</script>
</body>

</html>