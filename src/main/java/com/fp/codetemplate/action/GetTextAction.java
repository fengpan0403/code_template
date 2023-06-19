package com.fp.codetemplate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBTextArea;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

public class GetTextAction extends AnAction {

    private JTextArea textArea = new JTextArea();


    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        /*String text = Optional.ofNullable(e.getData(CommonDataKeys.EDITOR).getSelectionModel().getSelectedText())
               .orElse(null);
        if (text == null || text.length() == 0) {
           Messages.showMessageDialog("please select the SQL script!", "Tip", Messages.getWarningIcon());
           return;
        }
        Messages.showMessageDialog("Your SQL script is" + text, "Tip", Messages.getInformationIcon());*/

        // 获取当前项目对象
        final Project project = e.getProject();

        // 创建GUI对象
/*        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 20));
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(600,200)); // 输入框大小
        textArea.setLineWrap(Boolean.TRUE);
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        FileChooserDescriptor chooserDescriptor = new FileChooserDescriptor(false,true,false,false,false,false);
        TextBrowseFolderListener listener = new TextBrowseFolderListener(chooserDescriptor);
        textFieldWithBrowseButton.addBrowseFolderListener(listener);
        panel.add(textFieldWithBrowseButton, BorderLayout.NORTH);
        panel.add(textArea);
        // 构建对话框
        DialogBuilder dialogBuilder = new DialogBuilder(project);
        // 设置对话框显示内容
        dialogBuilder.setCenterPanel(panel);
        dialogBuilder.setTitle("导出代码模板");
        dialogBuilder.setOkOperation(() ->{
            String text = textArea.getText();
        });
        // 显示对话框
        dialogBuilder.show();*/

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 20));
        panel.add(createSqlTextPanel());
        // 构建对话框
        DialogBuilder dialogBuilder = new DialogBuilder(project);
        // 设置对话框显示内容
        dialogBuilder.setCenterPanel(panel);
        dialogBuilder.setTitle("导出代码模板");
        dialogBuilder.setOkOperation(() ->{
            String text = textArea.getText();
            if (text == null || text.length() == 0) {
                Messages.showMessageDialog("SQL not found!", "Tip", Messages.getWarningIcon());
            }
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            WriteCommandAction.runWriteCommandAction(project, () -> {
                int offset = editor.getCaretModel().getOffset();
                editor.getDocument().insertString(offset, "JPanel panel = new JPanel();\n" +
                        "        JLabel label = new JLabel(\"SQL\");\n" +
                        "        textArea.setPreferredSize(new Dimension(600,200)); // 输入框大小\n" +
                        "        textArea.setLineWrap(Boolean.TRUE);\n" +
                        "        panel.add(label);\n" +
                        "        panel.add(textArea);\n" +
                        "        return panel;");
            });
            dialogBuilder.getDialogWrapper().close(0);
        });
        // 显示对话框
        dialogBuilder.show();
    }

    private JPanel createSqlTextPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("SQL");
        textArea.setPreferredSize(new Dimension(600,200)); // 输入框大小
        textArea.setLineWrap(Boolean.TRUE);
        panel.add(label);
        panel.add(textArea);
        return panel;
    }
}
