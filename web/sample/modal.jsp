<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head></head>
 <body>
    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3>Modal header</h3>
    </div>
    <div class="modal-body">
	<ul class="nav nav-tabs">
		<li class="active"><a href="#tab1" data-toggle="tab">Tab 1</a></li>
		<li><a href="#tab2" data-toggle="tab">Tab 2</a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active" id="tab1"><p>This modal was loaded in via ajax</p></div>
		<div class="tab-pane" id="tab2"><p>This is some other tab content</p></div>
	</div>
	<button class="btn update">Update</button>
</div>
<div class="modal-footer">
	<button type="button" data-dismiss="modal" class="btn">Close</button>
	<button type="button" class="btn btn-primary">Ok</button>
</div>
</body>
</html>