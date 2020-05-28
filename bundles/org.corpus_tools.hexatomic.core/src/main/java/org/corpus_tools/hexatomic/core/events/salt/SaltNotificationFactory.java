/*-
 * #%L
 * org.corpus_tools.hexatomic.core
 * %%
 * Copyright (C) 2018 - 2020 Stephan Druskat,
 *                                     Thomas Krause
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.corpus_tools.hexatomic.core.events.salt;

import org.corpus_tools.hexatomic.core.ProjectManager;
import org.corpus_tools.salt.ISaltFactory;
import org.corpus_tools.salt.SaltFactory;
import org.corpus_tools.salt.common.SCorpus;
import org.corpus_tools.salt.common.SCorpusDocumentRelation;
import org.corpus_tools.salt.common.SCorpusGraph;
import org.corpus_tools.salt.common.SCorpusRelation;
import org.corpus_tools.salt.common.SDocument;
import org.corpus_tools.salt.common.SDocumentGraph;
import org.corpus_tools.salt.common.SDominanceRelation;
import org.corpus_tools.salt.common.SMedialDS;
import org.corpus_tools.salt.common.SMedialRelation;
import org.corpus_tools.salt.common.SOrderRelation;
import org.corpus_tools.salt.common.SPointingRelation;
import org.corpus_tools.salt.common.SSpan;
import org.corpus_tools.salt.common.SSpanningRelation;
import org.corpus_tools.salt.common.SStructure;
import org.corpus_tools.salt.common.STextualDS;
import org.corpus_tools.salt.common.STextualRelation;
import org.corpus_tools.salt.common.STimeline;
import org.corpus_tools.salt.common.STimelineRelation;
import org.corpus_tools.salt.common.SToken;
import org.corpus_tools.salt.common.SaltProject;
import org.corpus_tools.salt.common.impl.SCorpusDocumentRelationImpl;
import org.corpus_tools.salt.common.impl.SCorpusGraphImpl;
import org.corpus_tools.salt.common.impl.SCorpusImpl;
import org.corpus_tools.salt.common.impl.SCorpusRelationImpl;
import org.corpus_tools.salt.common.impl.SDocumentGraphImpl;
import org.corpus_tools.salt.common.impl.SDocumentImpl;
import org.corpus_tools.salt.common.impl.SDominanceRelationImpl;
import org.corpus_tools.salt.common.impl.SMedialDSImpl;
import org.corpus_tools.salt.common.impl.SMedialRelationImpl;
import org.corpus_tools.salt.common.impl.SOrderRelationImpl;
import org.corpus_tools.salt.common.impl.SPointingRelationImpl;
import org.corpus_tools.salt.common.impl.SSpanImpl;
import org.corpus_tools.salt.common.impl.SSpanningRelationImpl;
import org.corpus_tools.salt.common.impl.SStructureImpl;
import org.corpus_tools.salt.common.impl.STextualDSImpl;
import org.corpus_tools.salt.common.impl.STextualRelationImpl;
import org.corpus_tools.salt.common.impl.STimelineImpl;
import org.corpus_tools.salt.common.impl.STimelineRelationImpl;
import org.corpus_tools.salt.common.impl.STokenImpl;
import org.corpus_tools.salt.common.impl.SaltProjectImpl;
import org.corpus_tools.salt.core.SAnnotation;
import org.corpus_tools.salt.core.SFeature;
import org.corpus_tools.salt.core.SGraph;
import org.corpus_tools.salt.core.SLayer;
import org.corpus_tools.salt.core.SMetaAnnotation;
import org.corpus_tools.salt.core.SNode;
import org.corpus_tools.salt.core.SProcessingAnnotation;
import org.corpus_tools.salt.core.SRelation;
import org.corpus_tools.salt.core.impl.SAnnotationImpl;
import org.corpus_tools.salt.core.impl.SFeatureImpl;
import org.corpus_tools.salt.core.impl.SGraphImpl;
import org.corpus_tools.salt.core.impl.SLayerImpl;
import org.corpus_tools.salt.core.impl.SMetaAnnotationImpl;
import org.corpus_tools.salt.core.impl.SNodeImpl;
import org.corpus_tools.salt.core.impl.SProcessingAnnotationImpl;
import org.corpus_tools.salt.core.impl.SRelationImpl;
import org.corpus_tools.salt.graph.Graph;
import org.corpus_tools.salt.graph.IdentifiableElement;
import org.corpus_tools.salt.graph.Identifier;
import org.corpus_tools.salt.graph.Label;
import org.corpus_tools.salt.graph.Layer;
import org.corpus_tools.salt.graph.Node;
import org.corpus_tools.salt.graph.Relation;
import org.corpus_tools.salt.graph.impl.IdentifierImpl;
import org.corpus_tools.salt.semantics.SCatAnnotation;
import org.corpus_tools.salt.semantics.SLemmaAnnotation;
import org.corpus_tools.salt.semantics.SPOSAnnotation;
import org.corpus_tools.salt.semantics.SSentenceAnnotation;
import org.corpus_tools.salt.semantics.STypeAnnotation;
import org.corpus_tools.salt.semantics.SWordAnnotation;
import org.corpus_tools.salt.semantics.impl.SCatAnnotationImpl;
import org.corpus_tools.salt.semantics.impl.SLemmaAnnotationImpl;
import org.corpus_tools.salt.semantics.impl.SPOSAnnotationImpl;
import org.corpus_tools.salt.semantics.impl.SSentenceAnnotationImpl;
import org.corpus_tools.salt.semantics.impl.STypeAnnotationImpl;
import org.corpus_tools.salt.semantics.impl.SWordAnnotationImpl;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;

/**
 * Implements at {@link SaltFactory} where the created objects will use the {@link IEventBroker} to
 * send events when the objects are updated.
 * 
 * <p>
 * The event will have the ID of the updated element as argument.
 * </p>
 * 
 * @author Thomas Krause {@literal <krauseto@hu-berlin.de>}
 *
 */
public class SaltNotificationFactory implements ISaltFactory {


  private final NotificationHelper notificationHelper;

  public SaltNotificationFactory(IEventBroker events, ProjectManager projectManager,
      UISynchronize sync) {
    this.notificationHelper = new NotificationHelper(events, projectManager, sync);
  }

  private GraphNotifierImpl createNotifierGraph() {
    return new GraphNotifierImpl(notificationHelper);
  }

  private LabelNotifierImpl createNotifierLabel() {
    return new LabelNotifierImpl(notificationHelper);
  }


  private LayerNotifierImpl<Node, Relation<Node, Node>> createNotifierLayer() {
    return new LayerNotifierImpl<>(notificationHelper);
  }

  private NodeNotifierImpl createNotifierNode() {
    return new NodeNotifierImpl(notificationHelper);
  }


  private RelationNotifierImpl<Node, Node> createNotifierRelation() {
    return new RelationNotifierImpl<>(notificationHelper);
  }


  @Override
  public Node createNode() {
    return createNotifierNode();
  }

  @Override
  public Graph<Node, Relation<Node, Node>, Layer<Node, Relation<Node, Node>>> createGraph() {
    return createNotifierGraph();
  }


  @Override
  public Relation<Node, Node> createRelation() {
    return createNotifierRelation();
  }

  @Override
  public Label createLabel() {
    return createNotifierLabel();
  }

  @Override
  public Identifier createIdentifier(IdentifiableElement container, String id) {
    return new IdentifierImpl(container, id);
  }

  @Override
  public Layer<Node, Relation<Node, Node>> createLayer() {
    return createNotifierLayer();
  }

  @Override
  public SGraph createSGraph() {
    GraphNotifierImpl delegate = createNotifierGraph();
    SGraphImpl result = new SGraphImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SNode createSNode() {
    NodeNotifierImpl delegate = createNotifierNode();
    SNode result = new SNodeImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SRelation<SNode, SNode> createSRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SRelation<SNode, SNode> result = new SRelationImpl<>(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SAnnotation createSAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SAnnotation result = new SAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SMetaAnnotation createSMetaAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SMetaAnnotation result = new SMetaAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SProcessingAnnotation createSProcessingAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SProcessingAnnotation result = new SProcessingAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SFeature createSFeature() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SFeature result = new SFeatureImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SLayer createSLayer() {
    LayerNotifierImpl<Node, Relation<Node, Node>> delegate = createNotifierLayer();
    SLayer result = new SLayerImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SaltProject createSaltProject() {
    return new SaltProjectImpl();
  }

  @Override
  public SCorpus createSCorpus() {
    NodeNotifierImpl delegate = createNotifierNode();
    SCorpus result = new SCorpusImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SDocument createSDocument() {
    NodeNotifierImpl delegate = createNotifierNode();
    SDocument result = new SDocumentImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SCorpusGraph createSCorpusGraph() {
    GraphNotifierImpl delegate = createNotifierGraph();
    SCorpusGraphImpl result = new SCorpusGraphImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SDocumentGraph createSDocumentGraph() {
    GraphNotifierImpl delegate = createNotifierGraph();
    SDocumentGraphImpl result = new SDocumentGraphImpl(delegate);
    delegate.setOwner(result);
    return result;
  }



  @Override
  public SCorpusRelation createSCorpusRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SCorpusRelation result = new SCorpusRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SCorpusDocumentRelation createSCorpusDocumentRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SCorpusDocumentRelation result = new SCorpusDocumentRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SSpanningRelation createSSpanningRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SSpanningRelation result = new SSpanningRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SDominanceRelation createSDominanceRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SDominanceRelation result = new SDominanceRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SPointingRelation createSPointingRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SPointingRelation result = new SPointingRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SOrderRelation createSOrderRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SOrderRelation result = new SOrderRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public STextualRelation createSTextualRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    STextualRelation result = new STextualRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public STimelineRelation createSTimelineRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    STimelineRelation result = new STimelineRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SMedialRelation createSMedialRelation() {
    RelationNotifierImpl<Node, Node> delegate = createNotifierRelation();
    SMedialRelation result = new SMedialRelationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SSpan createSSpan() {
    NodeNotifierImpl delegate = createNotifierNode();
    SSpan result = new SSpanImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SStructure createSStructure() {
    NodeNotifierImpl delegate = createNotifierNode();
    SStructure result = new SStructureImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public STextualDS createSTextualDS() {
    NodeNotifierImpl delegate = createNotifierNode();
    STextualDS result = new STextualDSImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SMedialDS createSMedialDS() {
    NodeNotifierImpl delegate = createNotifierNode();
    SMedialDS result = new SMedialDSImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public STimeline createSTimeline() {
    NodeNotifierImpl delegate = createNotifierNode();
    STimeline result = new STimelineImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SToken createSToken() {
    NodeNotifierImpl delegate = createNotifierNode();
    SToken result = new STokenImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SCatAnnotation createSCatAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SCatAnnotation result = new SCatAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SPOSAnnotation createSPOSAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SPOSAnnotation result = new SPOSAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SLemmaAnnotation createSLemmaAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SLemmaAnnotation result = new SLemmaAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public STypeAnnotation createSTypeAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    STypeAnnotation result = new STypeAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SWordAnnotation createSWordAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SWordAnnotation result = new SWordAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

  @Override
  public SSentenceAnnotation createSSentenceAnnotation() {
    LabelNotifierImpl delegate = createNotifierLabel();
    SSentenceAnnotation result = new SSentenceAnnotationImpl(delegate);
    delegate.setOwner(result);
    return result;
  }

}
