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

import org.corpus_tools.hexatomic.core.Topics;
import org.corpus_tools.salt.graph.Label;
import org.corpus_tools.salt.graph.Node;
import org.corpus_tools.salt.graph.impl.NodeImpl;
import org.eclipse.e4.core.services.events.IEventBroker;

/**
 * Implements a Salt {@link Node} which use the {@link IEventBroker} to send events when the node
 * was updated.
 * 
 * <p>
 * The event will have the ID of the node as argument.
 * </p>
 * 
 * @author Thomas Krause {@literal <krauseto@hu-berlin.de>}
 *
 */
public class NodeNotifierImpl extends NodeImpl implements Node, NotifyingElement<Node> {

  private static final long serialVersionUID = -7940440063671378198L;

  private Node owner;

  @Override
  public Node getOwner() {
    return owner;
  }

  @Override
  public void setOwner(Node owner) {
    this.owner = owner;
  }

  @Override
  public void addLabel(Label label) {
    super.addLabel(label);
    SaltNotificationFactory.sendEvent(Topics.ANNOTATION_ADDED, label);
  }

  @Override
  public void removeLabel(String qname) {
    if (qname != null) {
      Label label = getLabel(qname);
      SaltNotificationFactory.sendEvent(Topics.ANNOTATION_REMOVED, label);
      super.removeLabel(qname);
    }
  }

  @Override
  public void removeAll() {
    SaltNotificationFactory.sendEvent(Topics.ANNOTATION_BEFORE_MODIFICATION, this);
    super.removeAll();
    SaltNotificationFactory.sendEvent(Topics.ANNOTATION_AFTER_MODIFICATION, this);
  }
}