function parseJwt (token) {
	var base64Url = token.split('.')[1];
	var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
	var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
		return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
	}).join(''));

	return JSON.parse(jsonPayload);
};

function getRoles () {
	if (!!$.cookie('JWT')) {
		var roles = [];
		var jwt = parseJwt($.cookie('JWT')).Role;
		for (var i = 0; i < jwt.length; i++){
			roles.push(jwt[i].name);
		}
		return roles
	}
	return [];
};

function getUsername () {
	if (!!$.cookie('JWT')) {
		var roles = [];
		var jwt = parseJwt($.cookie('JWT'));
		return jwt.sub;
	}
	return [];
};

$(document).ready(function() {
	if (!!$.cookie('JWT')) {
		$('[id$=loginLink]').css('display', 'none');
		$('[id$=registerLink]').css('display', 'none');
		$('[id$=logoutLink]').css('display', '');
		$('[id$=commonLinks]').css('display', '');
	} else {
		$('[id$=loginLink]').css('display', '');
		$('[id$=registerLink]').css('display', '');
		$('[id$=logoutLink]').css('display', 'none');
		$('[id$=commonLinks]').css('display', 'none');
	}
})

function deleteObject(id, type){
	$.ajax({
		url: "/"+type+"/"+id,
		type: "DELETE",
		contentType: "application/json"
	}).done(function(data){
		if (data != 'success'){
			alert('Error: '+data);
		}else{
			window.location = "/ui/"+type;
		}
	}).fail(function(data) {
        alert("Failed to delete! "+ data.responseText);
    });
}