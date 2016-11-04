package com.onlineinteract.core.processor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.util.ListTypeRetainer;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;

import java.util.List;

public class DeviceInputProcessor implements InputProcessor {
    private Workspace workspace;
    private List<Template> retainedTemplateList;
    private List<Template> templateInstances;

    public DeviceInputProcessor(Workspace workspace) {
        this.workspace = workspace;
        templateInstances = this.workspace.getTemplateInstances();
        List<WorkbenchItem> workbenchItems = this.workspace.getWorkbenchItems();
        retainedTemplateList = ListTypeRetainer.<WorkbenchItem, Template>retainedList(workbenchItems, Template.class);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return processTouchDown(button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // System.out.println("mouse up @ " + x + ", " + invertY(y));
            // Determinate if template has been clicked on
            // If so, set dragFlag to false and remove instance type of template.
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("Dragged getting called with - " + pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean processTouchDown(int button) {
        if (button == Input.Buttons.LEFT) {
            Vector3 coordinates = workspace.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (Template templateItem : retainedTemplateList) {
                if (templateItem.isClickWithinBoundary(coordinates))
                    createTemplateInstance(templateItem);
            }

            return true;
        }
        return false;
    }

    private void createTemplateInstance(Template templateItem) {

        WorkbenchOutline workbenchOutline = workspace.getWorkbenchOutline();

        float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
        float y = workbenchOutline.getBoxHeight() - Template.BOX_HEIGHT;

        if (templateItem.getLabel().equals("µicroservice"))
            templateInstances.add(new Template(workspace.getShapeRenderer(), workspace.getBatch(), workspace.getFont(), workspace.getCamera(), x, y, Color.FOREST, Color.FOREST, "µicroservice"));
        if (templateItem.getLabel().equals("Infrastructure"))
            templateInstances.add(new Template(workspace.getShapeRenderer(), workspace.getBatch(), workspace.getFont(), workspace.getCamera(), x, y, Color.CORAL, Color.CORAL, "Infrastructure"));
        if (templateItem.getLabel().equals("Scripts"))
            templateInstances.add(new Template(workspace.getShapeRenderer(), workspace.getBatch(), workspace.getFont(), workspace.getCamera(), x, y, Color.BLUE, Color.GRAY, "Scripts"));
    }
}
