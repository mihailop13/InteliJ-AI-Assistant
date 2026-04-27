package mihailop13.plugins.view;

import com.intellij.openapi.project.Project;
import mihailop13.plugins.logic.EditorContextManager;
import mihailop13.plugins.logic.PromptBuilder;
import mihailop13.plugins.service.AIService;

import javax.swing.*;
import java.awt.*;

public class ChatPanel {

    private final JPanel mainPanel;
    private final JTextPane chatTextPane;
    private final JButton sendButton;
    private final JTextField inputField;
    private final EditorContextManager editor;
    private boolean isSending = false;

    private void sendButtonListener() {
        //sending action
        Runnable sendAction = () -> {
            String userText = inputField.getText().trim();

            if (!userText.isEmpty() && !isSending) {
                isSending = true;
                sendButton.setEnabled(false);

                //Document aware
                String selectedCode = editor.getSelectedCode();
                String fullCode = "";
                String fileName = editor.getCurrentFileName();

                if (selectedCode == null || selectedCode.isEmpty()) {
                    fullCode = editor.getFullFileContent();
                }

                PromptBuilder promptBuilder = new PromptBuilder();
                String promptToSend = promptBuilder.buildPrompt(userText, selectedCode, fullCode, fileName);

                inputField.setText("");
                updateChat("User: " + userText + "\n");

                AIService aiService = new AIService();

                aiService.sendMessage(promptToSend).thenAccept(response -> {
                    SwingUtilities.invokeLater(() -> {

                        updateChat("AI: " + response + "\n\n");

                        isSending = false;
                        sendButton.setEnabled(true);
                    });
                }).exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        updateChat("Error: " + ex.getMessage() + "\n");
                        isSending = false;
                        sendButton.setEnabled(true);
                    });
                    return null;
                });
            }
        };

        //button click
        sendButton.addActionListener(e -> sendAction.run());

        //enter pressed
        inputField.addActionListener(e -> sendAction.run());
    }

    private void updateChat(String text) {
        String current = chatTextPane.getText();
        chatTextPane.setText(current + text);
    }

    public ChatPanel(Project project) {
        // main Layout
        mainPanel = new JPanel(new BorderLayout(5, 5));
        editor = new EditorContextManager(project);

        chatTextPane = new JTextPane();
        chatTextPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextPane);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        // bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        sendButtonListener();
    }

    public JComponent getPanel() { return mainPanel; }
}
