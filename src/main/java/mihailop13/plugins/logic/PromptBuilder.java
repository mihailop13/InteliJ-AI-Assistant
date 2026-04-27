package mihailop13.plugins.logic;

public class PromptBuilder {

    public String buildPrompt(String userMessage, String selectedCode, String fullCode, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert developer. You are analyzing the file: ").append(fileName).append("\n\n");

        if (selectedCode != null && !selectedCode.isEmpty()) {
            sb.append("### SELECTED CODE:\n```java\n").append(selectedCode).append("\n```\n");
        } else {
            sb.append("### FULL FILE CONTENT:\n```java\n").append(fullCode).append("\n```\n");
        }

        sb.append("\n### USER QUESTION:\n").append(userMessage);
        return sb.toString();
    }
}
