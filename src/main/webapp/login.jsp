<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="utf-8">
      <title>Log in with your account</title>
<link href="${contextPath}/resources/css/welcome.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
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
      <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
      <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
      <script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"
	type="text/javascript"></script>
      <script src="/resources/js/jquery.cookie.js"></script>
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
				<a class="nav-link" href="/login" id="loginLink">Login</a>
				<a class="nav-link" href="/logout" id="logoutLink">Logout</a>
				<a class="nav-link" href="/ui/registration" id="registerLink">Register</a>
				
			</form>
		</div>
	</nav>
    <div class="container page-content">
      <form method="POST" action="${contextPath}/login" class="form-signin">
        <h2 class="form-heading">Log in</h2>

        <div class="form-group ${error != null ? 'has-error' : ''}">
            <span>${message}</span>
            <input name="username" type="text" class="form-control" placeholder="Username"
                   autofocus="true"/>
            <input name="password" type="password" class="form-control" placeholder="Password"/>
            <span>${error}</span>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
           <%--  <h4 class="text-center"><a href="${contextPath}/registration">Create an account</a></h4> --%>
        </div>
      </form>
    </div>
	<script>
	if (window.location.href.includes("?logout")){
		$.removeCookie('JWT', {path: '/ui'});
	}
	</script>
	<footer class="page-footer font-small blue pt-4">
  <!-- Copyright -->
  <div class="footer-copyright text-center py-3">� 2019: Jonas Ribikauskas IFF-6/4
  </div>
  <!-- Copyright -->
</footer>
  </body>
</html>
