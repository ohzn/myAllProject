<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<!-- Required meta tags -->
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>

<meta charset="UTF-8">
 <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgb(169, 216, 0);">
    <a class="navbar-brand" href="main.jsp">RabbitTail</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
  
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <a class="nav-link" id="Notice" data-value="Notice" href="#">Notice<span class="sr-only">(current)</span></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href='list.do'>FreeBoard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="databox" data-value="databox" href="#">DataBox</a>
        </li>
      </ul>
      <form id ="search" class="form-inline my-2 my-lg-0">
      	<select id="selectsearch" class="form-select" aria-label="Default select example">
				<option value="bTitle">제목</option>
				<option value="bMem">작성자</option>
		</select>
        <input id="keyword" class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="search_ajax()">Search</button>&nbsp;&nbsp;
      </form>
      
   
    </div>
</nav>
<title>Login Page</title>
<style>
	.test1{  }
    .test2{ padding-top: 80px; }
</style>
<script src="http://code.jquery.com/jquery.js"></script>
<script>
function search_ajax(){
	var menu = $('#selectsearch').val();
	var keyword = $('#keyword').val();
	var re = 'search_view.do?s_menu='+menu+'&s_keyword='+keyword;
	if(keyword==""){
		alert('검색어를 입력해주세요.');
	} else {
		window.location.replace(re);
	}

}

function submit_ajax(){
	var queryString = $("#reg_frm").serialize();
	$.ajax({
		url: 'login.do',
		type: 'POST',
		data: queryString,
		dataType: 'text',
		success: function(json){
			console.log(json);
			var result = JSON.parse(json);
			if(result.code == "success"){
				alert(result.desc);
				window.location.replace("main.jsp");
			} else {
				alert(result.desc);
			}
		}
	});
}
</script>
</head>
<body>
<div class="container-fluid">
<div class="row">
	<div class="col test1"></div>
    <div class="col-4 test2">
		<form id="reg_frm">
  		<!-- Id input -->
  		<div class="form-outline mb-3">
  			<label class="form-label" for="form2Example1">ID</label>
	    	<input type="text" name="mId" id="form2Example1" class="form-control"
	    		   value="<% if(session.getAttribute("id") != null)
								out.println(session.getAttribute("id"));
						  %>"/>
	    </div>

		<!-- Password input -->
 		<div class="form-outline mb-3">
 			<label class="form-label" for="form2Example2">Password</label>
    		<input type="password" name="mPw" id="form2Example2" class="form-control" />
		</div>

		<!-- 2 column grid layout for inline styling -->
		<div class="row mb-3">
    		
  		</div>

  		<!-- Login button -->
  		<button type="button" class="btn btn-success btn-block mb-4" onclick="submit_ajax()">Login</button>

  		<!-- Register buttons -->
 		<div class="text-center">
    		<p><a href="javascript:window.location='join.jsp'">회원가입</a></p>
    		<p></p>	
		</div>
		</form>
	</div>
    <div class="col test1"></div>
</div>  
</div>


<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>		
</body>
</html>