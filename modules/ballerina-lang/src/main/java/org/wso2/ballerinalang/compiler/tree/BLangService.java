/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.wso2.ballerinalang.compiler.tree;

import org.ballerinalang.model.elements.Flag;
import org.ballerinalang.model.tree.AnnotationAttachmentNode;
import org.ballerinalang.model.tree.IdentifierNode;
import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.ResourceNode;
import org.ballerinalang.model.tree.ServiceNode;
import org.ballerinalang.model.tree.statements.VariableDefinitionNode;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BSymbol;
import org.wso2.ballerinalang.compiler.tree.statements.BLangVariableDef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 0.94
 */
public class BLangService extends BLangNode implements ServiceNode {
    
    public BLangIdentifier name;
    public BLangIdentifier protocolPkgIdentifier;
    public List<BLangVariableDef> vars;
    public List<BLangResource> resources;
    public Set<Flag> flags;
    public List<BLangAnnotationAttachment> annAttachments;

    public BSymbol symbol;

    public BLangService() {
        this.vars = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.flags = new HashSet<>();
        this.annAttachments = new ArrayList<>();
    }
    
    @Override
    public BLangIdentifier getName() {
        return name;
    }
    
    @Override
    public void setName(IdentifierNode name) {
        this.name = (BLangIdentifier) name;
    }

    @Override
    public BLangIdentifier getProtocolPackageIdentifier() {
        return protocolPkgIdentifier;
    }
    
    @Override
    public void setProtocolPackageIdentifier(IdentifierNode protocolPkgIdentifier) {
        this.protocolPkgIdentifier = (BLangIdentifier) protocolPkgIdentifier;
    }

    @Override
    public List<BLangVariableDef> getVariables() {
        return vars;
    }
    
    @Override
    public void addVariable(VariableDefinitionNode var) {
        this.getVariables().add((BLangVariableDef) var);
    }

    @Override
    public List<BLangResource> getResources() {
        return resources;
    }
    
    @Override
    public void addResource(ResourceNode resource) {
        this.resources.add((BLangResource) resource);
    }

    @Override
    public Set<Flag> getFlags() {
        return null;
    }
    
    @Override
    public void addFlag(Flag flag) {
        this.getFlags().add(flag);
    }

    @Override
    public List<BLangAnnotationAttachment> getAnnotationAttachments() {
        return annAttachments;
    }
    
    @Override
    public void addAnnotationAttachment(AnnotationAttachmentNode annAttachment) {
        this.getAnnotationAttachments().add((BLangAnnotationAttachment) annAttachment);
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public NodeKind getKind() {
        return NodeKind.SERVICE;
    }

    @Override
    public String toString() {
        return "BLangService: " + flags + " " + annAttachments + " " + getName() + "<" + protocolPkgIdentifier + "> "
                + vars + " " + resources;
    }
}
