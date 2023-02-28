/*
 * ==================================================================== 
 * Project: openMDX, http://www.openmdx.org
 * Description: Graphviz Mapper 
 * Owner: the original authors. 
 * ====================================================================
 * 
 * This software is published under the BSD license as listed below.
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
 * This product includes or is based on software developed by other 
 * organizations as listed in the NOTICE file.
 */
package org.openmdx.application.mof.mapping.pimdoc.image;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

import org.openmdx.application.mof.mapping.pimdoc.MagicFile;
import org.openmdx.application.mof.mapping.pimdoc.MagicFile.Type;
import org.openmdx.application.mof.mapping.pimdoc.PIMDocConfiguration;
import org.openmdx.application.mof.mapping.pimdoc.spi.AbstractMapper;
import org.openmdx.application.mof.mapping.pimdoc.text.AlbumMapper;
import org.openmdx.base.Version;
import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.io.Sink;
import org.openmdx.base.mof.cci.ModelElement_1_0;
import org.openmdx.base.mof.cci.Model_1_0;
import org.openmdx.base.mof.spi.PIMDocFileType;

/**
 * Graphviz Mapper
 */
abstract class GraphvizMapper extends AbstractMapper {

	protected GraphvizMapper(
		Sink sink, 
		Model_1_0 model, 
		boolean markdown, 
		PIMDocConfiguration configuration
	){
		super(sink, model, markdown, configuration);
	}

    protected String getMapperId() {
        return getClass().getSimpleName() + " " + Version.getImplementationVersion();
    }
	
    public void createArchiveEntry() {
    	fileHeader();
        directedGraph();
        fileFooter();
    }

    protected void directedGraph() {
		printLine("digraph {");
        attributeStatements();
        graphBody();
        printLine("}");
    }
    
	protected void attributeStatements() {
    	attributeStatement("graph", getGraphStyle());
    	attributeStatement("node", getNodeStyle());
    	attributeStatement("edge", getEdgeStyle());
    }

    private void attributeStatement(String elementType, Map<String, String> style) {
		printLine("\t", elementType, " [");
    	mapStyle("\t\t", style);
		printLine("\t]");
	}

	protected void mapStyle(String indent, Map<String, String> style) {
		for(Map.Entry<String,String> e : style.entrySet()) {
    		printLine(indent, e.getKey(), "=\"", e.getValue(), "\"");
    	}
	}

	protected Map<String, String> getGraphStyle(){
    	return configuration.getGraphvizStyleSheet().getElementStyle("graph");
    }

    protected Map<String, String> getNodeStyle(){
    	return configuration.getGraphvizStyleSheet().getElementStyle("node");
    }
    
    protected Map<String, String> getEdgeStyle(){
    	return configuration.getGraphvizStyleSheet().getElementStyle("edge");
    }

    protected Map<String, String> getClassStyle(String className){
    	return configuration.getGraphvizStyleSheet().getElementStyle(className);
    }
    
    protected void fileHeader(
    ) {
        fileGenerated();
    }
    
	protected void fileGenerated() {
		printLine("/*");
        printLine(" * Generated by ", getMapperId());
        printLine(" * Generated at ", Instant.now().toString());
        printLine(" *");
        printLine(" * GENERATED - DO NOT CHANGE MANUALLY");
        printLine(" */");
	}

    protected void fileFooter() {
    }

	/**
	 * Provide the entry name (using HTML entries)
	 * 
	 * @param element the model element used to derive the entry name
	 * @param type The package cluster file type
	 * 
	 * @return the entry name
	 */
    static String getClusterPath(ModelElement_1_0 element, Type type){
		final String baseName = MagicFile.PACKAGE_CLUSTER.getFileName(type);
		if(element == null) {
			return baseName;
		} else try {
			return element.getModel().toJavaPackageName(element, null).replace('.', '/') + '/' + baseName;
		} catch (ServiceException exception) {
			throw new RuntimeServiceException(exception);
    	}
    }
    
	protected String getBaseURL() {
    	return "";
    }

    protected String getHref(
    	ModelElement_1_0 element
    ){
    	if(element.isPackageType()){
        	return getBaseURL() + GraphvizMapper.getElementPath(element) + '#' + AlbumMapper.COMPARTMENT_ID;
    	} else if(element.isClassType() || element.isStructureType()){
        	return getBaseURL() + GraphvizMapper.getElementPath(element);
    	} else try {
    		final ModelElement_1_0 container = this.model.getElement(element.getContainer());
        	return getBaseURL() + GraphvizMapper.getElementPath(container) + "#" + element.getName();
    	} catch (ServiceException e) {
    		throw new RuntimeServiceException(e);
    	}
    }

    protected static String getDisplayName(
    	ModelElement_1_0 element
    ) {
    	String qualifiedName = element.getQualifiedName();
    	return (element.isPackageType() ? qualifiedName.substring(0, qualifiedName.lastIndexOf(':')) : qualifiedName).replace(":", "::");
    }
    
	protected Stream<ModelElement_1_0> streamElements(){
		return this.model.getContent().stream();
	}

    protected abstract String getTitle();
    
    protected abstract void graphBody();

	/**
	 * Provide the entry name (using HTML entries)
	 * 
	 * @param element the model element used to derive the entry name
	 * 
	 * @return the entry name
	 */
	public static String getElementPath(ModelElement_1_0 element){
		try {
	    	final StringBuilder entryName = new StringBuilder(
	    		element.getModel().toJavaPackageName(element, null).replace('.', '/')
	    	);
			entryName.append('/').append(element.getName()).append(PIMDocFileType.TEXT.extension());
	    	return entryName.toString();
		} catch (ServiceException exception) {
			throw new RuntimeServiceException(exception);
		}
	}
    
}
