<%@  page contentType= "text/html;charset=UTF-8" language="java" pageEncoding= "UTF-8" %><%
/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Name:        $Id: show-Default.jsp,v 1.73 2008/08/27 13:21:18 wfro Exp $
 * Description: Default.jsp
 * Revision:    $Revision: 1.73 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/08/27 13:21:18 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 *
 * Copyright (c) 2004-2008, OMEX AG, Switzerland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * * Neither the name of the openMDX team nor the names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * ------------------
 *
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 *
 * This product includes software developed by Mihai Bazon
 * (http://dynarch.com/mishoo/calendar.epl) published with an LGPL
 * license.
 */
%><%@ page session="true" import="
org.openmdx.compatibility.base.naming.*,
org.openmdx.portal.servlet.*,
org.openmdx.portal.servlet.control.*,
org.openmdx.portal.servlet.view.*,
org.openmdx.portal.servlet.texts.*
" %>
<%
	ApplicationContext app = (ApplicationContext)session.getValue(WebKeys.APPLICATION_KEY);
	ViewsCache viewsCache = (ViewsCache)session.getValue(WebKeys.VIEW_CACHE_KEY_SHOW);
	ShowObjectView view = (ShowObjectView)viewsCache.getView(request.getParameter(Action.PARAMETER_REQUEST_ID));
	PaintScope paintScope = PaintScope.valueOf(request.getParameter(Action.PARAMETER_SCOPE));
	Texts_1_0 texts = app.getTexts();
	ShowInspectorControl inspectorControl = view.getShowInspectorControl();
	HtmlPage p = HtmlPageFactory.openPage(
		view,
		request,
		out
	);

	// PaintScope.FULL
	if(paintScope == PaintScope.FULL) {
		// Set header
		response.setHeader(
			"Cache-Control",
			"max-age=" + Integer.MAX_VALUE
		);
		response.setHeader(
			"Pragma",
			""
		);

		// Prolog
		Control prolog = view.createControl(
			"PROLOG",
			PagePrologControl.class
		);

		// Epilog
		Control epilog = view.createControl(
			"EPILOG",
			PageEpilogControl.class
		);

		// Operation parameters
		PanelControl operationParams = (PanelControl)view.createControl(
			"PARAMS",
			PanelControl.class
		);
		operationParams.setLayout(PanelControl.LAYOUT_NONE);
		operationParams.addControl(inspectorControl.getOperationPaneControl(), OperationPaneControl.FRAME_PARAMETERS);
		operationParams.addControl(inspectorControl.getReportControl(), OperationPaneControl.FRAME_PARAMETERS);

		// North
		Control north = view.createControl(
			"north",
			SessionInfoControl.class
		);

		// Root menu
		MenuControl rootPanel = (MenuControl)view.createControl(
			"rootPanel",
			MenuControl.class
		);
		rootPanel.addControl(
			view.createControl("rootmenu", RootMenuControl.class)
		);
		rootPanel.setMenuClass("navv");
		rootPanel.setLayout(MenuControl.LAYOUT_VERTICAL);

		// West
		PanelControl west = (PanelControl)view.createControl(
			"layoutWest",
			PanelControl.class
		);
		west.setLayout(PanelControl.LAYOUT_VERTICAL);
		west.setTableStyle("cellspacing=\"0\" cellpadding=\"0\"");

		// Navigation
		Control navigation = view.createControl(
			"navigation",
			NavigationControl.class
		);

		// Operations Menu
		MenuControl menuOps = (MenuControl)view.createControl(
			"menuOps",
			MenuControl.class
		);
		menuOps.setLayout(MenuControl.LAYOUT_HORIZONTAL);
		menuOps.setMenuClass("nav");
		menuOps.setHasPrintOption(true);
		menuOps.addControl(inspectorControl.getOperationPaneControl());
		menuOps.addControl(inspectorControl.getWizardControl());
		menuOps.addControl(inspectorControl.getReportControl());

		// Search
		Control search = view.createControl(
			"search",
			ScriptControl.class
		);

		// Operation results
		PanelControl operationResults = (PanelControl)view.createControl(
			"PARAMS",
			PanelControl.class
		);
		operationResults.setLayout(PanelControl.LAYOUT_NONE);
		operationResults.addControl(inspectorControl.getOperationPaneControl(), OperationPaneControl.FRAME_RESULTS);

		// Errors
		Control errors =	view.createControl(null, ShowErrorsControl.class);

		// Attributes
		Control attributes = inspectorControl.getAttributePaneControl();

		// References
		PanelControl references = (PanelControl)view.createControl(
			"REFERENCES",
			PanelControl.class
		);
		references.setLayout(PanelControl.LAYOUT_NONE);
		ReferencePaneControl[] referencePaneControls = inspectorControl.getReferencePaneControl();
		for(int i = 0; i < referencePaneControls.length; i++) {
      %><%@ include file="../Set-MultiDelete-include.jsp" %><%
		}
		references.addControl(
			referencePaneControls,
			ReferencePaneControl.FRAME_VIEW
		);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="<%= texts.getDir() %>">
<head>
  <title><%= app.getApplicationName() + " - " + view.getObjectReference().getTitle() + (view.getObjectReference().getTitle().length() == 0 ? "" : " - ") + view.getObjectReference().getLabel() %></title>
<%
	prolog.paint(p, PagePrologControl.FRAME_PRE_PROLOG, false);
	p.flush();
%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="_style/colors.css" rel="stylesheet" type="text/css">
	<link href="_style/calendar-small.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="javascript/portal-all.js"></script>
	<!--[if lt IE 7]><script type="text/javascript" src="javascript/iehover-fix.js"></script><![endif]-->
	<script type="text/javascript" src="javascript/calendar/lang/calendar-<%= app.getCurrentLocaleAsString() %>.js"></script>
	<script language="javascript" type="text/javascript">
	  var OF = null;
	  try {
		OF = self.opener.OF;
	  }
	  catch(e) {
		OF = null;
	  }
	  if(!OF) {
		OF = new ObjectFinder();
	  }
	</script>
	<link rel="stylesheet" type="text/css" href="_style/ssf.css" >
	<link rel="stylesheet" type="text/css" href="_style/n2default.css" >
	<link rel="stylesheet" type="text/css" href="javascript/yui/build/assets/skins/sam/container.css" >
	<link rel='shortcut icon' href='images/favicon.ico' />
<%
	prolog.paint(p, PagePrologControl.FRAME_POST_PROLOG, false);
	p.flush();
%>
<script language="javascript" type="text/javascript">
		var rootMenu = null;

		function toggleRootMenu(e){
			try{if(e.ctrlKey && e.altKey){rootMenu.moveTo(e.clientX+1, e.clientY);if(rootMenu.cfg.config.visible.value){rootMenu.hide();}else{rootMenu.show();}YAHOO.util.Event.preventDefault(e);}}catch(e){}
		};

		YAHOO.util.Event.onDOMReady(function(){
			rootMenu = new YAHOO.widget.Panel("rootPanel",{context:['rootMenuAnchor','tl','tr'], close:true, visible:false,constraintoviewport:true});
			rootMenu.cfg.queueProperty(
				"keylisteners",
				new YAHOO.util.KeyListener(document, { keys:27 }, {fn:rootMenu.hide, scope:rootMenu, correctScope:true })
			);
			rootMenu.cfg.queueProperty(
				"keylisteners",
				new YAHOO.util.KeyListener(document, { alt:true, keys:88 }, {fn:rootMenu.hide, scope:rootMenu, correctScope:true })
			);
			kl = new YAHOO.util.KeyListener(document, { alt:true, keys:88 }, {fn:rootMenu.show, scope:rootMenu, correctScope:true });
			kl.enable();
			YAHOO.util.Event.addListener(document, "click", toggleRootMenu);
			rootMenu.render();
		});
</script>
</head>
<body class="yui-skin-sam" onload="initPage();">
<iframe class="popUpFrame" id="DivShim" src="blank.html" scrolling="no" frameborder="0" style="position:absolute; top:0px; left:0px; display:none;"></iframe>
<%
		EditObjectControl.paintEditPopups(p);
		operationParams.paint(p, false);
		p.flush();
%>
<div id="container">
	<div id="wrap">
		<div id="header">
			<div id="hider">
<%
				rootPanel.paint(p, false);
				p.flush();
%>
			</div> <!-- hider -->
<%
			north.paint(p, false);
			p.flush();
%>
			<div id="topnavi">
<%
				search.paint(p, false);
				p.flush();
%>
				<ul id="nav" class="nav" onmouseover="sfinit(this);" >
					<li><a href="#" onclick="javascript:return false;"><img id="rootMenuAnchor" src="./images/mlogo.png" border="0"/></a>
						<ul onclick="this.style.left='-999em';" onmouseout="this.style.left='';">
<%
							RootMenuControl.paintQuickAccessors(p);
							p.flush();
%>
						</ul>
					</li>
				</ul>
				<ul id="navigation" class="navigation" onmouseover="sfinit(this);">
<%
					RootMenuControl.paintTopNavigation(p);
					p.flush();
%>
				</ul>
			</div> <!-- topnavi -->
<%
			navigation.paint(p, false);
			menuOps.paint(p, false);
			p.flush();
%>
		</div> <!-- header -->
		<div id="content-wrap">
			<div id="<%= app.getPanelState("Header") == 0 ? "content" : "contentNH" %>">
<%@ include file="../../../../show-header.html" %>
<%
				errors.paint(p, false);
				operationResults.paint(p, false);
				p.flush();
%>
				<div id="aPanel">
<%
					attributes.paint(p, false);
					p.flush();
%>
				</div>
<%
				references.paint(p, false);
				p.flush();
%>
<%@ include file="../../../../show-footer.html" %>
			</div> <!-- content -->
		</div> <!-- content-wrap -->
<%@ include file="../../../../show-footer-noscroll.html" %>
	<div> <!-- wrap -->
</div> <!-- container -->

<%
	epilog.paint(p, false);
	p.close(false);
%>
</body>
</html>
<%
	}
	// PaintScope.ATTRIBUTE_PANE
	else if(paintScope == PaintScope.ATTRIBUTE_PANE) {
		view.getAttributePane().getAttributePaneControl().paint(
			p,
			false
		);
		p.flush();
		p.close(false);
%>
	<script language="javascript" type="text/javascript">
		//alert('postLoad show');
	</script>
<%
	}
%>
