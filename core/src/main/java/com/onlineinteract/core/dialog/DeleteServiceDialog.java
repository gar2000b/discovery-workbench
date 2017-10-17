package com.onlineinteract.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;

public class DeleteServiceDialog extends Dialog {

    private Workspace workspace;
    private Template template;

    public DeleteServiceDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public DeleteServiceDialog(String title, Skin skin, Workspace workspace, Template template) {
        super(title, skin);
        this.workspace = workspace;
        this.template = template;
    }

    {
        button("Yes", true).padBottom(10);
        button("No", false).padBottom(10);
    }

    @Override
    protected void result(Object object) {
        if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
            /*
             * template.setLabel(labelTextField.getText()); template.setStartupCommand(startupCommandTextField.getText()); template.setRunningClause(runningClauseTextField.getText());
             * template.setServicePortNo(servicePortNoTextField.getText()); workspace.getServiceListComponent().updateServiceList(template);
             */
            System.out.println("Deleting MS from workspace");
            workspace.getServiceListComponent().removeTemplateInstance(template);

        }

        // Gdx.input.setInputProcessor(workspace.getDeviceInputProcessor());
    }

}
