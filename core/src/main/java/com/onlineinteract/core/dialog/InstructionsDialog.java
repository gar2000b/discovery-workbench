package com.onlineinteract.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.onlineinteract.core.Workspace;

public class InstructionsDialog extends Dialog {

    private Workspace workspace;
    TextArea instructionsTextArea;
    Label instructionsLabel;

    public InstructionsDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public InstructionsDialog(String title, Skin skin, Workspace workspace) {
        super(title, skin);
        this.workspace = workspace;
    }

    {
        button("Update", true).padBottom(10);
        button("Cancel", false).padBottom(10);
        instructionsTextArea = new TextArea("", getSkin());
        instructionsTextArea.setPrefRows(20);
        getContentTable().add(instructionsTextArea).padTop(20).width(400);
        getContentTable().row();
    }

    @Override
    protected void result(Object object) {
        if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
            workspace.setInstructions(instructionsTextArea.getText());
        }
        workspace.setDialogToggleFlag(false);
    }

    public TextArea getInstructionsTextArea() {
        return instructionsTextArea;
    }

    public void setInstructionsTextArea(TextArea instructionsTextArea) {
        this.instructionsTextArea = instructionsTextArea;
    }

}
