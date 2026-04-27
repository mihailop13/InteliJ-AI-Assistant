package mihailop13.plugins.view;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;

public class AIToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ChatPanel panel = new ChatPanel(project);

        Content content = ContentFactory.getInstance()
                .createContent(panel.getPanel(), "", false);

        toolWindow.getContentManager().addContent(content);
    }
}