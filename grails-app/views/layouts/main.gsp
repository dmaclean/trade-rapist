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
		<!--<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">-->
        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
        </style>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-responsive.min.css')}" type="text/css">
        <g:javascript src="jquery.min.js"/>
        <g:javascript src="bootstrap.js"/>
		<g:layoutHead/>
		<r:layoutResources />
	</head>
	<body>
        <g:set var="user" value="${sec.username()}" />
        <g:set var="isAdmin" value="${sec.username() == 'admin'}" />
        <span id="context_root" style="display: none">${ applicationContext.servletContext.contextPath }</span>

		<!--<div id="grailsLogo" role="banner"><a href="http://grails.org"><img src="${resource(dir: 'images', file: 'grails_logo.png')}" alt="Grails"/></a></div>-->
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <g:link controller="home" class="brand">trade rapist</g:link>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li><a href="${ application.contextPath }/index.html">Draft Central</a></li>
                            <g:if test="${ user && !isAdmin }">
                                <li><g:link controller="fantasyTeam" action="list">My Fantasy Teams</g:link></li>
                            </g:if>
                            <g:if test="${ isAdmin }">
                                <li><g:link controller="fantasyPoints" action="editProjections">Edit Projections</g:link></li>
                            </g:if>
                            <!--<li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li class="nav-header">Nav header</li>
                                    <li><a href="#">Separated link</a></li>
                                    <li><a href="#">One more separated link</a></li>
                                </ul>
                            </li>-->
                        </ul>
                        <g:if test="${ !user }">
                            <form class="navbar-form pull-right" method="post" action="${request.contextPath}/j_spring_security_check">
                                <input class="span2" type="text" name="j_username" placeholder="Email">
                                <input class="span2" type="password" name="j_password" placeholder="Password">
                                <button type="submit" class="btn">Sign in</button>
                            </form>
                        </g:if>
                        <g:else>
                            <ul class="nav pull-right">
                                <li><g:link controller="logout">Logout</g:link></li>
                            </ul>
                        </g:else>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>
        <div class="container">
            <!-- Space for flash messages -->
            <g:if test="${ flash.error }">
                <div class="span12">
                    <div class="alert alert-error">
                        <button type="button" class="close" data-dismiss="alert">×</button>
                        ${ flash.error }
                    </div>
                </div>
            </g:if>
            <g:elseif test="${ flash.warning }">
                <div class="span12">
                    <div class="alert">
                        <button type="button" class="close" data-dismiss="alert">×</button>
                        ${ flash.warning }
                    </div>
                </div>
            </g:elseif>
            <g:elseif test="${ flash.info }">
                <div class="span12">
                    <div class="alert alert-info">
                        <button type="button" class="close" data-dismiss="alert">×</button>
                        ${ flash.info }
                    </div>
                </div>
            </g:elseif>
            <g:layoutBody/>
            <div class="footer" role="contentinfo"></div>
            <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
        </div>
		<g:javascript library="application"/>
		<r:layoutResources />
	</body>
</html>
