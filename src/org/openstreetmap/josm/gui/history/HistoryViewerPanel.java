// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.history;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.openstreetmap.josm.gui.util.AdjustmentSynchronizer;

/**
 * Base class of {@link TagInfoViewer} and {@link RelationMemberListViewer}.
 * @since 6207
 */
public abstract class HistoryViewerPanel extends JPanel {

    protected transient HistoryBrowserModel model;
    protected VersionInfoPanel referenceInfoPanel;
    protected VersionInfoPanel currentInfoPanel;
    protected transient AdjustmentSynchronizer adjustmentSynchronizer;
    protected transient SelectionSynchronizer selectionSynchronizer;

    protected HistoryViewerPanel(HistoryBrowserModel model) {
        setModel(model);
        build();
    }

    private JScrollPane embedInScrollPane(JTable table) {
        JScrollPane pane = new JScrollPane(table);
        adjustmentSynchronizer.participateInSynchronizedScrolling(pane.getVerticalScrollBar());
        return pane;
    }

    /**
     * Sets the history browsing model.
     * @param model The history browsing model
     */
    public final void setModel(HistoryBrowserModel model) {
        if (this.model != null) {
            unregisterAsChangeListener(model);
        }
        this.model = model;
        if (this.model != null) {
            registerAsChangeListener(model);
        }
    }

    protected final void unregisterAsChangeListener(HistoryBrowserModel model) {
        if (currentInfoPanel != null) {
            model.removeChangeListener(currentInfoPanel);
        }
        if (referenceInfoPanel != null) {
            model.removeChangeListener(referenceInfoPanel);
        }
    }

    protected final void registerAsChangeListener(HistoryBrowserModel model) {
        if (currentInfoPanel != null) {
            model.addChangeListener(currentInfoPanel);
        }
        if (referenceInfoPanel != null) {
            model.addChangeListener(referenceInfoPanel);
        }
    }

    protected abstract JTable buildReferenceTable();

    protected abstract JTable buildCurrentTable();

    private void build() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // ---------------------------
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 0.5;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        referenceInfoPanel = new VersionInfoPanel(model, PointInTimeType.REFERENCE_POINT_IN_TIME);
        add(referenceInfoPanel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.5;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        currentInfoPanel = new VersionInfoPanel(model, PointInTimeType.CURRENT_POINT_IN_TIME);
        add(currentInfoPanel, gc);

        adjustmentSynchronizer = new AdjustmentSynchronizer();
        selectionSynchronizer = new SelectionSynchronizer();

        // ---------------------------
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 0.5;
        gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.NORTHWEST;
        add(embedInScrollPane(buildReferenceTable()), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 0.5;
        gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.NORTHWEST;
        add(embedInScrollPane(buildCurrentTable()), gc);
    }
}
