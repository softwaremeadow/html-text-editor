package com;


import com.listeners.FrameListener;
import com.listeners.TabbedPaneChangeListener;
import com.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.CENTER;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            ExceptionHandler.log(e);
        } catch (InstantiationException e) {
            ExceptionHandler.log(e);
        } catch (IllegalAccessException e) {
            ExceptionHandler.log(e);
        } catch (UnsupportedLookAndFeelException e) {
            ExceptionHandler.log(e);
        }
    }

    /*
    init() - This method initializes the GUI and adds an event listener to the window. The method also shows the window.
     */
    public void init() {
        initGui();
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        setVisible(true);
    }

    /*
    initMenuBar() - This method initializes the menu bar for the view. It initializes each part of the menu bar using
    the MenuHelper class and adds the menu bar to the top of frame's content pane.
     */
    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }

    /*
    initEditor() - Initializes the editor. This method adds a tab for html text and a tab for plain text. It sets the
    dimension for the editor and adds a change listener for the tabbed pane. The tabbed pane is added to the content
    pane layer.
     */
    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPaneForHtmlTextPane = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", jScrollPaneForHtmlTextPane);
        JScrollPane jScrollPaneForPlainTextPane = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Text", jScrollPaneForPlainTextPane);
        tabbedPane.setPreferredSize(new Dimension(1000, 500));
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        getContentPane().add(tabbedPane, CENTER);
    }

    /*
    initGui() - This method initializes the GUI. It first initializes the menu bar and next initializes the editor.
    The method also sets the frame size and packs the components within the frame.
     */
    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();

        switch (event) {
            case "New": controller.createNewDocument();
                        break;
            case "Open": controller.openDocument();
                         break;
            case "Save": controller.saveDocument();
                         break;
            case "Save as...": controller.saveDocumentAs();
                               break;
            case "Exit": controller.exit();
                         break;
            case "About": showAbout();
                          break;
        }
    }

    public void exit() {
        controller.exit();
    }

    public void selectedTabChanged() {
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        } else if (tabbedPane.getSelectedIndex() == 1) {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void undo() {
        try {
            undoManager.undo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(this, "This is an HTML Text Editor", "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
