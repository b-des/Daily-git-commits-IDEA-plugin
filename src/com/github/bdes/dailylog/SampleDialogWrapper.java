package com.github.bdes.dailylog;

import com.intellij.ide.plugins.newui.TextHorizontalLayout;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class SampleDialogWrapper extends DialogWrapper {

    private String message;

    public SampleDialogWrapper(String message) {
        super(true); // use current window as parent
        this.message = message;
        init();
        setTitle("Commits for the day");
        setOKButtonText("Copy & Close");
        setCancelButtonText("Close");
        if (message == null) {
            setErrorText("Something went wrong");
            setOKActionEnabled(false);
        }

    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        StringSelection stringSelection = new StringSelection(this.message);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBorder(JBUI.Borders.empty());

        JTextArea jTextArea = new JTextArea(this.message);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jTextArea.setPreferredSize(new Dimension(400, 300));
        JBScrollPane jsp = new JBScrollPane(jTextArea);
        jPanel.add(jsp, BorderLayout.PAGE_START);

        return jPanel;
    }
}