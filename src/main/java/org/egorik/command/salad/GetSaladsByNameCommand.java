package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;

import java.util.List;

public class GetSaladsByNameCommand implements Command {
    private final SaladService saladService;
    private final InputManager inputManager;

    public GetSaladsByNameCommand(SaladService saladService, InputManager inputManager) {
        this.saladService = saladService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        System.out.print("Enter salad name: ");
        String name = inputManager.getValidString();
        List<Salad> result = saladService.getSaladsByName(name);

        if (result.isEmpty()) {
            System.out.printf("Can't find any salads with name %s!\n", name);
            return;
        }

        System.out.println("Salads: ");
        result.forEach(System.out::println);
    }

    @Override
    public String getName() {
        return "By name";
    }

    @Override
    public String getDescription() {
        return "Get salads by name";
    }
}