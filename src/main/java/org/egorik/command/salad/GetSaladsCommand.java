package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;

import java.util.List;

public class GetSaladsCommand implements Command {

    private final SaladService saladService;

    public GetSaladsCommand(SaladService saladService) {
        this.saladService = saladService;
    }

    @Override
    public void execute() {
        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        System.out.println("All salads:");
        List<Salad> productList = saladService.getAllSalads();
        for (int i = 0; i < productList.size(); i++) {
            System.out.printf("%d). %s\n", i, productList.get(i));
        }
    }

    @Override
    public String getName() {
        return "Get all";
    }

    @Override
    public String getDescription() {
        return "Shows all salads";
    }
}