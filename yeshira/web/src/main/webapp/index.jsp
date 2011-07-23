<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
						assertion : assertion
					}).success(function() {
						alert("Login succesful");
					}).error(function() {
						alert("Hack attempt");
					});

				} else {
					// something went wrong!  the user isn't logged in.
					alert("Login Failed, please try again")
				}
			});
		});
	});
</script>
<title>בחירה ישירה</title>
</head>
<body>
	<img id="signin" src="https://browserid.org/i/sign_in_red.png"
		alt="Sign In using Mozilla's BrowserID" />
</body>
</html>