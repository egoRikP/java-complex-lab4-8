package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;

public class GetMoreInfoAboutSaladCommand implements Command {
    private final SaladService saladService;

    public GetMoreInfoAboutSaladCommand(SaladService saladService) {
        this.saladService = saladService;
    }

    @Override
    public void execute() {
        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        new GetSaladsCommand(saladService).execute();
        Salad toFind = SelectUtility.selectOne(saladService.getAllSalads());

        if (toFind == null) {
            return;
        }

        System.out.println(toFind.detailedInfo());
    }

    @Override
    public String getName() {
        return "Get more salad info";
    }

    @Override
    public String getDescription() {
        return "Get more info about salad";
    }
}