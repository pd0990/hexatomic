/*-
 * #%L
 * org.corpus_tools.hexatomic.grid
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

package org.corpus_tools.hexatomic.grid.configuration;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.coordinate.PositionCoordinate;
import org.eclipse.nebula.widgets.nattable.edit.action.DeleteSelectionAction;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.NatEventData;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemState;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Configures the context menu for the body region of the NatTable.
 * 
 * @author Stephan Druskat (mail@sdruskat.net)
 */
public class CustomBodyMenuConfiguration extends AbstractUiBindingConfiguration {

  private static final String DELETE_CELL_ITEM = "DELETE_CELL_ITEM"; //$NON-NLS-1$
  private Menu menu;
  private final NatTable table;
  private final SelectionLayer selectionLayer;

  /**
   * Constructor setting the table and selection layer fields, and creating the menu via
   * {@link #createMenu()}.
   * 
   * @param table The NatTable upon whose body region the menu should be constructed
   * @param selectionLayer The SelectionLayer to be used for configuring menu item states based on
   *        selection
   */
  public CustomBodyMenuConfiguration(NatTable table, SelectionLayer selectionLayer) {
    this.table = table;
    this.selectionLayer = selectionLayer;
    this.menu = createMenu();
  }

  private Menu createMenu() {
    PopupMenuBuilder builder = new PopupMenuBuilder(this.table);
    builder.withMenuItemProvider(DELETE_CELL_ITEM, new DeleteItemProvider());
    builder.withVisibleState(DELETE_CELL_ITEM, new ValidSelectionState());
    Menu popUpMenu = builder.build();
    return popUpMenu;
  }

  @Override
  public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
    uiBindingRegistry.registerMouseDownBinding(
        new MouseEventMatcher(SWT.NONE, GridRegion.BODY, MouseEventMatcher.RIGHT_BUTTON),
        new PopupMenuAction(this.menu));

  }

  /**
   * Provides a menu item for deleting cells.
   * 
   * @author Stephan Druskat (mail@sdruskat.net)
   */
  public class DeleteItemProvider implements IMenuItemProvider {


    @Override
    public void addMenuItem(NatTable natTable, Menu popupMenu) {
      MenuItem item = new MenuItem(popupMenu, SWT.PUSH);
      item.setText("Delete cell(s)");
      item.setEnabled(true);
      item.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
          new DeleteSelectionAction().run(natTable, null);
        }
      });
    }

  }

  /**
   * A menu item state based on valid selection of cells.
   * 
   * <p>
   * {@link #isActive(NatEventData)} returns <code>true</code> only when cells are selected, and no
   * cells containing token text are selected.
   * </p>
   * 
   * @author Stephan Druskat (mail@sdruskat.net)
   */
  private class ValidSelectionState implements IMenuItemState {

    @Override
    public boolean isActive(NatEventData natEventData) {
      if (selectionLayer.getSelectedCells().isEmpty()) {
        return false;
      } else {
        PositionCoordinate[] selectedCellCoordinates = selectionLayer.getSelectedCellPositions();

        for (PositionCoordinate coord : selectedCellCoordinates) {
          // The first column is reserved for token text
          if (coord.getColumnPosition() == 0) {
            return false;
          }
        }
        return true;
      }
    }
  }

}
