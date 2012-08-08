/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: RoseExporterMain.java,v 1.10 2008/09/10 08:55:29 hburger Exp $
 * Description: RoseExporterMain command-line tool
 * Revision:    $Revision: 1.10 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/10 08:55:29 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004, OMEX AG, Switzerland
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
 */
package org.openmdx.model1.exporter.rose;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmdx.uses.gnu.getopt.Getopt;
import org.openmdx.uses.gnu.getopt.LongOpt;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.model1.exporter.spi.Model_1Accessor;
import org.openmdx.model1.importer.rose.RoseImporter_1;

//---------------------------------------------------------------------------  
@SuppressWarnings("unchecked")
public class RoseExporterMain {

    //-------------------------------------------------------------------------  
    static class Runner {

        //-----------------------------------------------------------------------  
        public void run(
            String args[]
        ) throws ServiceException, Exception {

            // get options
            Getopt g = new Getopt(
                RoseExporterMain.class.getName(),
                args,
                "",
                new LongOpt[]{
                    new LongOpt("pathMapSymbol", LongOpt.REQUIRED_ARGUMENT, null, 's'),
                    new LongOpt("pathMapPath", LongOpt.REQUIRED_ARGUMENT, null, 'p'),
                    new LongOpt("mdlDir", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
                    new LongOpt("mdlFile", LongOpt.REQUIRED_ARGUMENT, null, 'f'),
                    new LongOpt("out", LongOpt.OPTIONAL_ARGUMENT, null, 'o'),
                    new LongOpt("format", LongOpt.OPTIONAL_ARGUMENT, null, 't'),
                    new LongOpt("openmdxjdoDir", LongOpt.OPTIONAL_ARGUMENT, null, 'j')
                }
            );

            List formats = new ArrayList();
            List modelNames = new ArrayList();
            String mdlDir = null;
            String mdlFile = null;
            String openmdxjdoDir = null;
            String outFileName = "./out.jar";
            List pathMapSymbols = new ArrayList();
            List pathMapPaths = new ArrayList();

            int c;
            while ((c = g.getopt()) != -1) {
                switch(c) {
                    case 's':
                        pathMapSymbols.add(
                            g.getOptarg()
                        );
                        break;
                    case 't':
                        formats.add(
                            g.getOptarg()
                        );
                        break;
                    case 'p':
                        pathMapPaths.add(
                            g.getOptarg()
                        );
                        break;
                    case 'd':
                        mdlDir = g.getOptarg();
                        break;
                    case 'j':
                        openmdxjdoDir = g.getOptarg();
                        break;
                    case 'f':
                        mdlFile = g.getOptarg();
                        break;
                    case 'o':
                        outFileName = g.getOptarg();
                        break;
                }
            }

            // modelNames      
            for(
                    int i = g.getOptind(); 
                    i < args.length ; 
                    i++
            ) {
                modelNames.add(args[i]);
            }
            if(modelNames.size() != 1) {
                throw new ServiceException(
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.INVALID_CONFIGURATION,
                    "number of model names must be 1",
                    new BasicException.Parameter("model names", modelNames)
                );
            }

            // pathMap
            Map pathMap = new HashMap();
            if(pathMapSymbols.size() != pathMapPaths.size()) {
                throw new ServiceException(
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.INVALID_CONFIGURATION,
                    "number of symbols and paths must be equal",
                    new BasicException.Parameter("pathMapSymbol", pathMapSymbols),
                    new BasicException.Parameter("pathMapPath", pathMapPaths)
                );
            }

            pathMap = new HashMap();
            for(
                    int i = 0; 
                    i < pathMapSymbols.size();
                    i++
            ) {
                pathMap.put(
                    pathMapSymbols.get(i),
                    pathMapPaths.get(i)
                );
            }

            Model_1Accessor modelExternalizer = new Model_1Accessor(
                "Mof", 
                openmdxjdoDir
            );

            modelExternalizer.importModel(
                new RoseImporter_1(
                    mdlDir,
                    mdlFile,
                    pathMap,
                    System.out
                )
            );      
            FileOutputStream fos = new FileOutputStream(
                new File(outFileName)
            );
            fos.write(
                modelExternalizer.externalizePackageAsJar(
                    (String)modelNames.get(0),
                    formats
                )
            );  
            fos.close();
        }
    }

    //---------------------------------------------------------------------------  
    public static void main(
        String[] args
    ) {
        try {
            new Runner().run(args);
        }
        catch(ServiceException e) {
            System.err.println(e.toString());
        }
        catch(Exception e) {
            System.err.println(
                new ServiceException(e).toString()
            );
        }
    }

    //---------------------------------------------------------------------------
    // Variables  
    //---------------------------------------------------------------------------  

}

//--- End of File -----------------------------------------------------------
