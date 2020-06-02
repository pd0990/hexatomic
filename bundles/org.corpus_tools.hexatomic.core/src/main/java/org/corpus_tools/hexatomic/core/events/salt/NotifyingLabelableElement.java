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
import org.corpus_tools.salt.graph.LabelableElement;

public interface NotifyingLabelableElement<T extends LabelableElement>
    extends NotifyingElement<T>, LabelableElement {

  /**
   * Sends the event for removing the label given by the qualified name.
   * 
   * @param qname The qualified name of the label to remove.
   * @return If, true, the label should be removed.
   */
  default boolean prepareRemoveLabel(String qname) {
    if (qname != null) {
      Label label = getLabel(qname);
      SaltNotificationFactory.sendEvent(Topics.ANNOTATION_REMOVED, label);
      return true;
    } else {
      return false;
    }
  }
}