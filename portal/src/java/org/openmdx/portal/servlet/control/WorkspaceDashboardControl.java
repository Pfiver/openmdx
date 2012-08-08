/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Name:        $Id: WorkspaceDashboardControl.java,v 1.5 2011/03/23 14:03:31 wfro Exp $
 * Description: DashboardControl 
 * Revision:    $Revision: 1.5 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2011/03/23 14:03:31 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2009-2011, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 *   contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
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
 * This product includes software developed by other organizations as
 * listed in the NOTICE file.
 */
package org.openmdx.portal.servlet.control;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.openmdx.portal.servlet.ViewPort;

public class WorkspaceDashboardControl
	extends AbstractDashboardControl 
	implements Serializable {
	
	//-----------------------------------------------------------------------
	public WorkspaceDashboardControl(
        String id,
        String locale,
        int localeAsIndex
	) {
		super(
			id,
			locale,
			localeAsIndex
		);
	}
	
	//-----------------------------------------------------------------------
	/* (non-Javadoc)
     * @see org.openmdx.portal.servlet.control.AbstractDashboardControl#getDashboardStyle()
     */
    @Override
    protected String getDashboardStyle(
    ) {
	    return "class=\"workspaceDashboard\"";
    }

	//-----------------------------------------------------------------------
	/* (non-Javadoc)
     * @see org.openmdx.portal.servlet.control.AbstractDashboardControl#getDashboardIdSuffix(org.openmdx.portal.servlet.ViewPort)
     */
    @Override
    protected String getDashboardIdSuffix(
    	ViewPort p
    ) {
    	return p.getApplicationContext().getCurrentWorkspace();   	
    }

	//-----------------------------------------------------------------------
	@Override
    protected int getMaxHorizontalOrder(
    ) {
		return 1;
    }

	//-----------------------------------------------------------------------
	@Override
    protected void getDashboard(    	
    	ViewPort p,
        String dashboardId,
        Properties settings,
        String dashletFilter,
        Map<String, Map<String, DashletDescr>> dashboard,
        boolean forEditing
    ) {
	    super.getDashboard(
	    	p,
	    	dashboardId, 
	    	settings, 
	    	dashletFilter, 
	    	dashboard,
	    	forEditing
	    );
	    // Add WorkspacesDashlet
	    String dashlet = "WorkspacesDashlet";
		String orderX = "0";
		String orderY = "9999";
		String width = "1";
		String name = "WorkspacesDashlet";
		String label = "*" + p.getApplicationContext().getTexts().getWorkspacesTitle();
		if(!forEditing && (dashletFilter == null || label.startsWith(dashletFilter))) {		
			if(dashboard.get(orderY) == null) {
				dashboard.put(
					orderY, 
					new TreeMap<String,DashletDescr>()
				);
			}						
			Map<String,DashletDescr> row = dashboard.get(orderY);
			row.put(
				orderX + "." + dashlet + (dashletFilter == null ? "" : "." + dashletFilter), 
				new DashletDescr(dashlet, name, width, label, orderX, orderY)
			);
		}
    }
    
}
