package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;

import java.util.List;

public class GetSaladsByCaloriesCommand implements Command {

    private final SaladService saladService;
    private final InputManager inputManager;


    public GetSaladsByCaloriesCommand(SaladService saladService, InputManager inputManager) {
        this.saladService = saladService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        System.out.println("Enter min calories: ");
        int min = inputManager.getPositiveInt();

        System.out.println("Enter max calories: ");
        int max = inputManager.getPositiveInt();

        List<Salad> result = saladService.getSaladsByTotalCalories(min, max);
        if (result.isEmpty()) {
            System.out.printf("Can't find any salads in calories range [%d;%d]!\n", min, max);
            return;
        }

        System.out.printf("Salads in calories range [%d;%d]:\n", min, max);
        result.forEach(System.out::println);
    }

    @Override
    public String getName() {
        return "By calories";
    }

    @Override
    public String getDescription() {
        return "Get salads by calories in range [min;max]";
    }
}