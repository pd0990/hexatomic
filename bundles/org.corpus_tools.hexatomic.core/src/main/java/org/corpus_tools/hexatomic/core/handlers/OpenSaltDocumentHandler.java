/*-
 * #%L
 * org.corpus_tools.hexatomic.core
 * %%
 * Copyright (C) 2018 - 2019 Stephan Druskat, Thomas Krause
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

package org.corpus_tools.hexatomic.core.handlers;

import javax.inject.Named;
import org.corpus_tools.hexatomic.core.ProjectManager;
import org.corpus_tools.salt.common.SDocument;
import org.corpus_tools.salt.exceptions.SaltResourceException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

/**
 * Handler to open a document with a specific editor.
 * 
 * @author Thomas Krause
 * @author Stephan Druskat
 *
 */
public class OpenSaltDocumentHandler {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(OpenSaltDocumentHandler.class);

  public static final String COMMAND_PARAM_EDITOR_ID =
      "org.corpus_tools.hexatomic.core.commandparameter.editor-id";
  public static final String DOCUMENT_ID = "org.corpus_tools.hexatomic.document-id";
  public static final String COMMAND_OPEN_DOCUMENT_ID =
      "org.corpus_tools.hexatomic.core.command.open_salt_document";
  public static final String EDITOR_TAG = "org.corpus_tools.hexatomic.tag.editor";

  /**
   * Opens the currently selected document with the given editor.
   * 
   * @param projectManager An instance of the project manager that holds the document
   * @param partService An instance of the part service which is used to create a new editor
   * @param selectionService An instance of the selection service used to get the currently selected
   *        document
   * @param editorID The model ID of the editor PartDescription to use a template
   */
  @Execute
  public static void execute(ProjectManager projectManager, EPartService partService,
      ESelectionService selectionService, @Named(COMMAND_PARAM_EDITOR_ID) String editorID) {

    // get currently selected document
    Object selection = selectionService.getSelection();
    if (selection instanceof SDocument) {

      SDocument document = (SDocument) selection;

      if (document.getDocumentGraph() == null) {
        try {
          if (document.getDocumentGraphLocation() == null) {
            // create a new document graph, because no one exists yet
            document.createDocumentGraph();
          } else {
            // TODO: show progress indicator
            document.loadDocumentGraph();
          }

        } catch (SaltResourceException ex) {
          // TODO: display error to the user in a dialog
          log.error("Could not load document graph (the actual annotations for document {}).",
              document.getId(), ex);
        }
      }

      // Create a new part from an editor part descriptor
      MPart editorPart = partService.createPart(editorID);
      String title = document.getName();
      if (editorPart.getLabel() != null || !editorPart.getLabel().isEmpty()) {
        title = title + " (" + editorPart.getLabel() + ")";
      }
      editorPart.setLabel(title);
      editorPart.getPersistedState().put(OpenSaltDocumentHandler.DOCUMENT_ID, document.getId());

      partService.showPart(editorPart, PartState.ACTIVATE);

    }
  }

  /**
   * Checks whether the currently selected object is a document.
   * 
   * @param selectionService An instance of the selection service
   * @return
   */
  @CanExecute
  public static boolean canExecute(ESelectionService selectionService) {
    return selectionService.getSelection() instanceof SDocument;
  }

}