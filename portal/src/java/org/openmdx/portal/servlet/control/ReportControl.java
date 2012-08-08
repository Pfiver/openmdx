/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Name:        $Id: ReportControl.java,v 1.30 2008/08/12 16:38:06 wfro Exp $
 * Description: UiBasedOperationPaneControl class
 * Revision:    $Revision: 1.30 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/08/12 16:38:06 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2007, OMEX AG, Switzerland
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
 * This product includes yui, the Yahoo! UI Library
 * (License - based on BSD).
 *
 */
package org.openmdx.portal.servlet.control;

import java.io.Serializable;

import org.openmdx.application.log.AppLog;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.portal.servlet.HtmlPage;
import org.openmdx.portal.servlet.reports.ReportDefinition;

//-----------------------------------------------------------------------------
public class ReportControl
    extends Control
    implements Serializable {
  
    //-------------------------------------------------------------------------
    public ReportControl(
        String id,
        String locale,
        int localeAsIndex,
        ControlFactory controlFactory,
        ReportDefinition[] reportDefinitions
    ) {
        super(
            id,
            locale,
            localeAsIndex,
            controlFactory
        );
        this.reportTabs = new ReportTabControl[reportDefinitions.length];
        for(int i = 0; i < reportDefinitions.length; i++) {
            this.reportTabs[i] = controlFactory.createReportTabControl(
                null,
                locale,
                localeAsIndex,
                reportDefinitions[i],
                REPORT_PANE_INDEX,
                i                
            );
        }        
    }
  
    //-------------------------------------------------------------------------
    @Override
    public void paint(
        HtmlPage p,
        String frame,
        boolean forEditing        
    ) throws ServiceException {
        AppLog.detail("> paint");
       
        // Report menu entries
        if(frame == null) {
            if(this.reportTabs.length > 0) {
                p.write("<li><a href=\"#\">Reports&nbsp;&nbsp;&nbsp;</a>");
                p.write("  <ul onclick=\"this.style.left='-999em';\" onmouseout=\"this.style.left='';\">");
                for(int i = 0; i < this.reportTabs.length; i++) {
                    this.reportTabs[i].paint(
                        p, 
                        frame, 
                        forEditing
                    );
                }
                p.write("  </ul>");
                p.write("</li>");
            }
        }        
        // Report input fields
        else if(FRAME_PARAMETERS.equals(frame)) {
            for(int i = 0; i < this.reportTabs.length; i++) {
                this.reportTabs[i].paint(
                    p, 
                    frame, 
                    forEditing
                );
            }
        }   
        AppLog.detail("< paint");
        
    }

    //-------------------------------------------------------------------------
    private static final long serialVersionUID = -5868748717990932512L;

    public static final String FRAME_PARAMETERS = "Parameters";
    private static final int REPORT_PANE_INDEX = 2000;
    
    private final ReportTabControl[] reportTabs;
    
}

//--- End of File -----------------------------------------------------------
