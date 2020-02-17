<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Tasks</title>
<link href="${contextPath}/resources/css/welcome.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
	integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
	crossorigin="anonymous"></script>
	
<script src="/resources/js/jquery.cookie.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://unpkg.com/vuex"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/axios/0.15.3/axios.min.js"></script>
<script src="/resources/js/common.js"></script>
<script src="https://kit.fontawesome.com/a03df4b32a.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://kit-free.fontawesome.com/releases/latest/css/free.min.css" media="all">
<link rel="stylesheet" href="https://kit-free.fontawesome.com/releases/latest/css/free-v4-font-face.min.css" media="all">
<link rel="stylesheet" href="https://kit-free.fontawesome.com/releases/latest/css/free-v4-shims.min.css" media="all">
<script
	src='//cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js'></script>

<link
	href="${contextPath}/resources/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet">

<link href='${contextPath}/packages/core/main.css' rel='stylesheet' />
<link href='${contextPath}/packages/daygrid/main.css' rel='stylesheet' />
<script src='${contextPath}/packages/core/main.js'></script>
<script src='${contextPath}/packages/interaction/main.js'></script>
<script src='${contextPath}/packages/daygrid/main.js'></script>


<script type="text/javascript"
	src="${contextPath}/resources/js/bootstrap-datetimepicker.js"></script>

<script>
	var calendar;
	
  document.addEventListener('DOMContentLoaded', function() {
	  $("#modal-btn-yes").on("click", function(){
		    removeEventActual(parseInt($('body').find('[id*=removeID]').html()));
		    $("#confirm-modal").modal('hide');
		  });
		  
		  $("#modal-btn-no").on("click", function(){
		    $("#confirm-modal").modal('hide');
		  });
		  
	   $("#modalDis-btn-yes").on("click", function(){
			    disapproveEventActual(parseInt($('body').find('[id*=disapproveID]').html()));
			    $("#disapprove-modal").modal('hide');
			  });
			  
			  $("#modalDis-btn-no").on("click", function(){
			    $("#disapprove-modal").modal('hide');
			  });
	  
	  
    var calendarEl = document.getElementById('calendar');
    var currentEvents;
    
     $.ajax({
		    type: "GET",
		    contentType: "application/json",
		    url: "/tasks",
		    dataType: 'json',
		    async: false,
		    success: function (data) { 
		    	var events = [];
		    	for (var i = 0; i < data.length; i++){
		    		var canAdd = getRoles().includes('admin') || getRoles().includes('manager');
		    		for (var j = 0; j < data[i].users.length; j++){
		    			if (data[i].users[j].username == getUsername() ){
		    				canAdd = true;
		    				break;
		    			}
		    		}
		    		if (canAdd){
		    			data[i].title = 'Title: '+data[i].title + '\n Description: ' + data[i].description + '\n Comments:\n ' + data[i].comments.join('\n ');
		    			events.push(data[i]);
		    		}
		    			
		    	}
		    	currentEvents = events;
		    },
		     error: function (e) {
		     	alert(e.responseText);
		     }
	});

    calendar = new FullCalendar.Calendar(calendarEl, {
      plugins: [ 'interaction', 'dayGrid' ],
      defaultDate: new Date,
      disableDragging: true,
      editable: false,
      eventLimit: true, // allow "more" link when too many events
      header: {
      	center:  (getRoles().includes('admin') || getRoles().includes('manager')) ? 'addEventButton' : ''
      },
      customButtons: {
        addEventButton: {
          text: 'add task...',
          click: function() {
          	$("#eventModal").find("#eventDate").val("");
          	$("#eventModal").find("#eventTitle").val("");
          	$("#eventModal").find("[id*=projectSelect]").val("");
          	$("#eventModal").find("[id*=groupSelect]").val("");
          	$("#eventModal").find("[id*=eventDescription]").val("");
          	
          	$("#eventModal").find("[id*=projectSelect]").removeClass("is-invalid");
          	$("#eventModal").find("[id*=groupSelect]").removeClass("is-invalid");
          	
          	$("#datetimepicker1").find('input').removeClass("is-invalid");
          	$("#eventModal").find("#eventTitle").removeClass("is-invalid");
          
          	$("#eventModal").modal();
          }
        }
      },
      eventRender: function(info) {   
    	 var $el = $(info.el);
	     
	     if (info.event.extendedProps.type != 0){
	    	 $el.css('background-color', 'red');
	     }
	     
	     $.ajax({
			    type: "GET",
			    contentType: "application/json",
			    url: "/tasks/"+info.event.id,
			    dataType: 'json',
			    timeout: 600000,
			    async: false,
			    success: function (data) { 
			    	var actualUsr = [];
			    	for (var i = 0; i < data.users.length;i++){
			    		actualUsr.push(data.users[i].username);
			    	}
			    	if (!getRoles().includes('manager') && actualUsr.includes(getUsername())){
				    	$el.append('<a style="color: white" href="javascript:approveEvent('+info.event.id+')">Finish task</a><br />');
					}
			    }
				});
	     
		
	    $el.append('<a style="color: yellow" href="javascript:disapproveEvent('+info.event.id+')">Comment task</a>');
	     
	     
	     $el.attr('event-id', (typeof info.event.id == 'undefined' ? '' : info.event.id));

	  },
			
      events: currentEvents
    });

    calendar.render();
    
  });
  
  	
  	function disapproveEvent(eventId){
  		$('[id*=disapproveID]').remove();
  		$('body').append("<div id='#disapproveID'>"+ eventId +"</div>");
  		$("#disapprovalReason").val('');
  		$("#disapprove-modal").modal('show');
  	}
  	
  	function disapproveEventActual(eventId){
  		if ($('#taskComm').val() == ''){
  			return;
  		}
  		$.ajax({
		    type: "GET",
		    contentType: "application/json",
		    url: "/tasks/"+eventId,
		    dataType: 'json',
		    timeout: 600000,
		    success: function (data) { 
		    	var taskData = {};
		    	taskData.data = {};
		    	taskData.users = {};
		    	var actualUsr = [];
		    	for (var i = 0; i < data.users.length;i++){
		    		actualUsr.push(data.users[i].username);
		    	}
		    	taskData.users.users = actualUsr;
		    	taskData.data.date = data.date;
		    	taskData.data.type = data.type;
		    	taskData.data.title = data.title;
		    	taskData.data.description = data.description;
		    	data.comments.push($('#taskComm').val());
		    	taskData.data.comments = data.comments;
		    	$.ajax({
				    type: "PUT",
				    contentType: "application/json",
				    url: "/tasks/"+eventId,
				    data: JSON.stringify(taskData),
				    timeout: 600000,
				    success: function (data) {
				    	window.location.reload()
				    },
				     error: function (e) {
				    	 console.log(e.responseText);
				    	 if (e.responseCode == '200'){
				    		 window.location.reload();
				    	 }else{
				     		 alert(e.responseText);
				    	 }
				     }
					});
		    }
			});
  	}
  	
  	
  	function approveEvent(eventId){

  		$.ajax({
			    type: "GET",
			    contentType: "application/json",
			    url: "/tasks/"+eventId,
			    dataType: 'json',
			    timeout: 600000,
			    success: function (data) { 
			    	var taskData = {};
			    	taskData.data = {};
			    	taskData.users = {};
			    	
			    	var actualUsr = [];
			    	for (var i = 0; i < data.users.length;i++){
			    		actualUsr.push(data.users[i].username);
    		    	}
			    	
			    	taskData.users.users = actualUsr.filter(function(value, index, arr){
			    	    return value != getUsername();
			    	});
			    	taskData.data.date = data.date;
			    	taskData.data.type = data.type;
			    	taskData.data.title = data.title;
			    	taskData.data.description = data.description;
			    	taskData.data.comments = data.comments;
			    	$.ajax({
					    type: "PUT",
					    contentType: "application/json",
					    url: "/tasks/"+eventId,
					    data: JSON.stringify(taskData),
					    timeout: 600000,
					    success: function (data) {
					    	if (taskData.users.users.length == 0){
					    		$.ajax({
								    type: "DELETE",
								    contentType: "application/json",
								    url: "/tasks/"+eventId,
								    data: JSON.stringify(taskData),
								    timeout: 600000,
								    success: function (data) {
								    	window.location.reload();
								    },
								     error: function (e) {
								    	 console.log(e.responseText);
								    	 if (e.responseCode == '200'){
								    		 window.location.reload();
								    	 }else{
								     		 alert(e.responseText);
								    	 }
								     }
									});
					    	}else{
					    		window.location.reload();
					    	}
					    },
					     error: function (e) {
					    	 console.log(e.responseText);
					    	 if (e.responseCode == '200'){
					    		 window.location.reload();
					    	 }else{
					     		 alert(e.responseText);
					    	 }
					     }
						});
			    }
				});
  	}
  
    function doAddEvent(){
		var dateStr = $("#eventModal").find("#eventDate").val();
        var title = $("#eventModal").find("#eventTitle").val();
        var project = $("[id*=projectSelect]").val();
        
        var description = $("[id*=eventDescription]").val();
        var type = $("[id*=eventType]").val();
          	
        var isError = false;
          	
        if (dateStr == ""){
        	$("#datetimepicker1").find('input').addClass("is-invalid");
        	isError = true;
        }
        if (title == ""){
        	$("#eventModal").find("#eventTitle").addClass("is-invalid");
        	isError = true;
        }
        if (project == null){
        	$("[id*=groupSelect]").addClass("is-invalid");
        	$("[id*=projectSelect]").addClass("is-invalid");
        	
        	isError = true;
        }
        
       if (!isError){
       	$("#eventModal").modal('toggle');
       	 
       	 var date = new Date(dateStr); // will be in local time
       	 var task = {};
       	 var users = [];
       	
       	if (project != null){
          	 $.ajax({
    		    type: "GET",
    		    contentType: "application/json",
    		    url: "/projects/"+project,
    		    async: false,
    		    success: function (data) {
    		    	for (var i = 0; i < JSON.parse(data).users.length;i++){
    		    		users.push(JSON.parse(data).users[i].username);
    		    	}
    		    	
    		    	task.data = {"title":title, "date": dateStr, "description":description, "type":type};
    		       	 task.users = {"users": users};
    				
    		         $.ajax({
    				    type: "POST",
    				    contentType: "application/json",
    				    url: "/tasks",
    				    data: JSON.stringify(task),
    				    timeout: 600000,
    				    success: function (data) {
    				    	window.location.reload()
    				    },
    				     error: function (e) {
    				    	 console.log(e.responseText);
    				    	 if (e.responseCode == '200'){
    				    		 window.location.reload();
    				    	 }else{
    				     		 alert(e.responseText);
    				    	 }
    				     }
    					});
    		    }
          	 });
          	 }
       	 
       	 
       }
	}

</script>
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
	<div class="page-content">
	<div class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="mySmallModalLabel" aria-hidden="true"
		id="confirm-modal">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Remove event?</h4>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="modal-btn-yes">Yes</button>
					<button type="button" class="btn btn-primary" id="modal-btn-no">No</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog"
		id="disapprove-modal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					
					<h4 class="modal-title" id="myModalLabel">Comment task</h4>
				</div>
				<div class="modal-body">
					<div class="container">
						<input id="taskComm" type='text' class="form-control" />
					</div>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="modalDis-btn-yes">OK</button>
					<button type="button" class="btn btn-primary" id="modalDis-btn-no">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog" id="eventModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">New task</h5>
				</div>

				<div class="modal-body">
					<div class="container">
						<div class="row">
							<div class='col-sm-6'>
								<div class="form-group">
									<div class='input-group date' id='datetimepicker1'>
										<input id="eventDate" type='text' class="form-control" /> <span
											class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
							<script type="text/javascript">
								$(function() {
									$('#datetimepicker1').datetimepicker({
										format : 'yyyy-mm-dd',
										minView: 2
									});
								});
							</script>
						</div>
						Title:
						<div class="row">
							<div class='col-sm-6'>
								<input id="eventTitle" type='text' class="form-control" />
							</div>
						</div>
						Type:
						<div class="row">
							<div class='col-sm-6'>
								<select name="cars" id="eventType" class="form-control">
									<option value="0">Regular</option>
									<option value="1">Special</option>
								</select>
							</div>
						</div>
						Description:
						<div class="row">
							<div class='col-sm-6'>
								<input id="eventDescription" type='text' class="form-control" />
							</div>
						</div>
					Project assigned:
						<div id="projectsInputContainer">
							<select class="form-control" id="projectSelect">
								<option value="" disabled selected>Project assigned</option>
								<option v-for="project in projects" v-bind:value="project.id">{{project.name}}</option>
							</select>
						</div>
					<script>
					new Vue({
					    el: '#projectSelect',
					    created() {
					        this.fetchData();	
					    },
					    data: {
					    	projects: []
					    },
					    methods: {
					        fetchData() {
					        axios.get('/projects').then(response => {
					        	this.projects = response.data;
					            });
					        }
					    }
					});
					</script>
					
					</div>

				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						onclick="javascript:doAddEvent()">Create</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id='calendar'></div>
	</div>
	<footer class="page-footer font-small blue pt-4">
  <!-- Copyright -->
  <div class="footer-copyright text-center py-3">© 2019: Jonas Ribikauskas IFF-6/4
  </div>
  <!-- Copyright -->
</footer>
</body>
</html>
