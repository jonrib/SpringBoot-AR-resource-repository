<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>User</title>
<link href="${contextPath}/resources/css/welcome.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
	integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
	integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
	integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
	crossorigin="anonymous"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"
	type="text/javascript"></script>
<script src="/resources/js/jquery.cookie.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://unpkg.com/vuex"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/axios/0.15.3/axios.min.js"></script>
<script src="/resources/js/common.js"></script>
<script src="https://kit.fontawesome.com/a03df4b32a.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://kit-free.fontawesome.com/releases/latest/css/free.min.css" media="all">
<link rel="stylesheet" href="https://kit-free.fontawesome.com/releases/latest/css/free-v4-font-face.min.css" media="all">
<link rel="stylesheet" href="https://kit-free.fontawesome.com/releases/latest/css/free-v4-shims.min.css" media="all">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a class="navbar-brand" href="/ui/welcome"><i class="fas fa-home"></i>&nbsp;Main page</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto" id="commonLinks">
				<li class="nav-item"><a class="nav-link" href="/ui/tasks">Tasks</a></li>
				<li class="nav-item"><a class="nav-link" href="/ui/groups">Groups</a></li>
				<li class="nav-item"><a class="nav-link" href="/ui/users">Users</a></li>
				<li class="nav-item"><a class="nav-link" href="/ui/projects">Projects</a></li>
			</ul>
			<form class="form-inline my-2 my-lg-0">
				<a class="nav-link" href="/login" id="loginLink">Login</a> <a
					class="nav-link" href="/logout" id="logoutLink">Logout</a> <a
					class="nav-link" href="/ui/registration" id="registerLink">Register</a>

			</form>
		</div>
	</nav>

	<div class="container page-content">
		<form class="form-signin">
			<h2 class="form-signin-heading">User</h2>

			<div class="form-group">
				<span  class="form-control" id="name"></span>
					
				
				<div id="rolesInputContainer">
					<select class="form-control" id="rolesSelect" multiple>
						<option value="" disabled selected>User roles</option>
						<option v-for="role in roles" v-bind:value="role.name">{{role.name}}</option>
					</select>
				</div>
				<script>
	new Vue({
	    el: '#rolesSelect',
	    created() {
	        this.fetchData();	
	    },
	    data: {
	        roles: []
	    },
	    methods: {
	        fetchData() {
	        axios.get('/roles').then(response => {
	        	this.roles = response.data;
	            });
	        }
	    }
	});
	
	$(document).ready(function(){
		var id = window.location.href.substring(window.location.href.lastIndexOf('/')+1);
		if (id != 'userform'){
			$.ajax({
				url: "/users/"+id,
				type: "GET",
				contentType: "application/json"
			}).done(function(data){
				var jsonData = JSON.parse(data);
				$('#name').html(jsonData.username);
				var roles = [];
				for (var i = 0; i < jsonData.roles.length; i++){
					roles.push(jsonData.roles[i].name);
				}
				$('#rolesSelect').val(roles);
			});
		}
	});
	
	function doSubmit(){
		var user = {};
		user.users = {};
		user.users.roles = $('#rolesSelect').val();
		
		
		$.ajax({
			url: "/users"+(window.location.href.substring(window.location.href.lastIndexOf('/')+1) == 'userform' ? '' : '/'+window.location.href.substring(window.location.href.lastIndexOf('/')+1)),
			type: window.location.href.substring(window.location.href.lastIndexOf('/')+1) == 'userform' ? "POST" : "PUT",
			data: JSON.stringify(user),
			contentType: "application/json"
		}).done(function(data){
			if (data != 'success'){
				alert('Error: '+data);
			}else{
				window.location = "/ui/users";
			}
		});
	}
	function makeManager(){
		$.ajax({
			url: "/managers/create/"+$('#name').html(),
			type: "POST",
			contentType: "application/json"
		}).done(function(data){
			if (data != 'success'){
				alert('Error: '+data);
			}else{
				window.location = "/ui/users";
			}
		});
	}
	</script>

				<a class="btn btn-lg btn-primary btn-block" href="javascript:doSubmit()">Submit</a>
				<a class="btn btn-lg btn-primary btn-block" href="javascript:makeManager()">Make a manager</a>
			</div>
		</form>
	</div>
<footer class="page-footer font-small blue pt-4">
  <!-- Copyright -->
  <div class="footer-copyright text-center py-3">© 2019: Jonas Ribikauskas IFF-6/4
  </div>
  <!-- Copyright -->
</footer>
</body>
</html>