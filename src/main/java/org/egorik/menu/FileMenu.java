package org.egorik.menu;

import org.egorik.AppContext;
import org.egorik.command.FileLoadCommand;
import org.egorik.command.FileSaveCommand;

public class FileMenu extends AbstractMenu {

    public FileMenu(AppContext appContext) {
        super(appContext);
    }

    @Override
    protected void init() {
        addCommand(1, new FileSaveCommand(appContext));
        addCommand(2, new FileLoadCommand(appContext));
    }

    @Override
    public String getName() {
        return "File menu";
    }

    @Override
    public String getDescription() {
        return "Load, save - products and salads";
    }
}