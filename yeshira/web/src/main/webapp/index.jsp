<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/screen.css" type="text/css" />
<script src="https://browserid.org/include.js" type="text/javascript"></script>
<script
	src="https://www.google.com/jsapi?key=ABQIAAAAkNgA0ghQr37pbck7FctnSxRjl4Ew3o2j6o8DHUBUN9KxOf6o7hTKI2IW3Pwb00J9oJYwd8wZSVKGpQ"
	type="text/javascript"></script>
<script type="text/javascript">
	google.load("jquery", "1.6.2");
	google.load("jqueryui", "1.8.14");
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#signin").click(function() {
			navigator.id.getVerifiedEmail(function(assertion) {
				if (assertion) {
					// This code will be invoked once the user has successfully
					// selected an email address they control to sign in with.
					$.post("user/login", {
						assertion : assertion,
						realm : document.location.host
					}).success(function(data) {
						// Login succesfull
						$("body").addClass("loggedIn")
						alert("Welcome " + $.parseJSON(data).email);
					}).error(function(data, textStatus, httpStatus) {
						alert("Login failed (" + httpStatus + ")");
					});
				} else {
					// something went wrong!  the user isn't logged in.
					alert("Login Failed, please try again")
				}
			});
		});
		$("#signout").click(function() {
			$.post("user/logout").success(function() {
				$("body").removeClass("loggedIn")
			}).error(function(data, textStatus, httpStatus) {
				alert("Error logging out (" + httpStatus + ")");
			});
		});
	});
</script>
<title>בחירה ישירה</title>
</head>
<body>
	<img id="signin" src="https://browserid.org/i/sign_in_red.png"
		alt="Sign In using Mozilla's BrowserID" />
	<span id="signout">Sign Out</span>
</body>
</html>