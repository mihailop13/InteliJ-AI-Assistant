package mihailop13.plugins.logic;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

public class EditorContextManager {
    private final Project project;

    public EditorContextManager(Project project) {
        this.project = project;
    }

    public String getSelectedCode() {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            return editor.getSelectionModel().getSelectedText();
        }
        return null;
    }

    public String getFullFileContent() {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            return editor.getDocument().getText();
        }
        return "";
    }

    public String getCurrentFileName() {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            return FileDocumentManager.getInstance().getFile(editor.getDocument()).getName();
        }
        return "unknown file";
    }
}
