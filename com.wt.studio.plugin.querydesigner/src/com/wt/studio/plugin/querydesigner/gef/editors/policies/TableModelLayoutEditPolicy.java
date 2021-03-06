package com.wt.studio.plugin.querydesigner.gef.editors.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.wt.studio.plugin.querydesigner.gef.commands.CreateAddChildColumnCommand;
import com.wt.studio.plugin.querydesigner.gef.commands.CreateColumnCommand;
import com.wt.studio.plugin.querydesigner.gef.commands.CreateColumnGroupCommand;
import com.wt.studio.plugin.querydesigner.gef.commands.MoveElementCommand;
import com.wt.studio.plugin.querydesigner.gef.model.AbstractBlockModel;
import com.wt.studio.plugin.querydesigner.gef.model.ColumnGroupModel;
import com.wt.studio.plugin.querydesigner.gef.model.ColumnModel2;
import com.wt.studio.plugin.querydesigner.gef.model.Element;

public class TableModelLayoutEditPolicy extends FlowLayoutEditPolicy
{

	@Override
	public Command getCommand(Request request)
	{
		if (REQ_RESIZE_CHILDREN.equals(request.getType())) {
			MoveElementCommand command = new MoveElementCommand();
			ChangeBoundsRequest boundsRequest = (ChangeBoundsRequest) request;
			Element element = (Element) ((EditPart) boundsRequest.getEditParts().get(0)).getModel();
			command.setElement(element);
			Rectangle rectangle = element.getRectangle();
			rectangle.setSize(rectangle.getSize().getExpanded(boundsRequest.getSizeDelta()));
			command.setRectangle(rectangle);
			return command;
		}
		return super.getCommand(request);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request)
	{
		Element parent = (Element) getHost().getModel();
		if (request.getNewObject() instanceof ColumnModel2) {
			CreateColumnCommand command = new CreateColumnCommand();
			command.setParentModel(parent);
			command.setColumnModel((ColumnModel2) request.getNewObject());
			return command;
		} else if (request.getNewObject() instanceof ColumnGroupModel) {
			CreateColumnGroupCommand command = new CreateColumnGroupCommand();
			command.setParentModel(parent);
			command.setColumnGroupModel((ColumnGroupModel) request.getNewObject());
			return command;
		}
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child)
	{
		return super.createChildEditPolicy(child);
	}

	@Override
	protected Command createAddCommand(EditPart child, EditPart after)
	{
		// TODO Auto-generated method stub
		if (child != null && after != null) {
			if (child.getParent().getModel() instanceof AbstractBlockModel
					&& after.getParent().getModel() instanceof AbstractBlockModel) {
				AbstractBlockModel childparentblock = (AbstractBlockModel) child.getParent()
						.getModel();
				AbstractBlockModel afterparentblock = (AbstractBlockModel) after.getParent()
						.getModel();
				Element childelement = (Element) child.getModel();
				Element afterelement = (Element) after.getModel();
				CreateAddChildColumnCommand command = new CreateAddChildColumnCommand();
				command.setChildParent(childparentblock);
				command.setAfterParent(afterparentblock);
				command.setChild(childelement);
				command.setAfter(afterelement);
				return command;
			}
		}
		return null;
	}

	@Override
	protected Command createMoveChildCommand(EditPart arg0, EditPart arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}