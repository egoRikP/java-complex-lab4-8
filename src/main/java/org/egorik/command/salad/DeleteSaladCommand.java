package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;

public class DeleteSaladCommand implements Command {

    private final SaladService saladService;

    public DeleteSaladCommand(SaladService saladService) {
        this.saladService = saladService;
    }

    @Override
    public void execute() {
        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        int ind = SelectUtility.selectInd(saladService.getAllSalads());

        if (ind == -1) {
            return;
        }

        if (InputManager.isContinue(String.format("Delete %s ?", saladService.getAllSalads().get(ind)))) {
            saladService.deleteSalad(ind);
        }
    }

    @Override
    public String getName() {
        return "Delete";
    }

    @Override
    public String getDescription() {
        return "Delete salad by ind";
    }
}