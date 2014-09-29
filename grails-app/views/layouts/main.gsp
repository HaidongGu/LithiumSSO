<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
		<g:layoutHead/>
		<g:javascript library="application"/>		
		<r:layoutResources />
	</head>
	<body>
		<div id="apacHeader" role="banner">
			<table width="100%"> <tr><td>
					<a href="http://apac.stage.lithium.com"><img src="${resource(dir: 'images', file: 'lithiumapac_logo.gif')}" alt="Lithium APAC"/></a>
				</td>
				<td align="right">
						<sec:ifLoggedIn> ${sec.username()}  <br> <g:link controller='logout'>Logout</g:link> </sec:ifLoggedIn> 
								  <sec:ifNotLoggedIn> <g:link controller='login'>Login</g:link> </sec:ifNotLoggedIn> 
								  <sec:ifNotLoggedIn> <g:link controller='register'>Register</g:link> </sec:ifNotLoggedIn>  
								</td></tr>
			</table>
		</div>
		<g:layoutBody/>
		<div class="footer" role="contentinfo">By Lithium APAC Team</div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<r:layoutResources />
	</body>
</html>
