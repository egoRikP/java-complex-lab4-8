package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetSaladsByTagSetCommand implements Command {

    private final SaladService saladService;

    public GetSaladsByTagSetCommand(SaladService saladService) {
        this.saladService = saladService;
    }

    @Override
    public void execute() {
        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        System.out.print("Enter tags with spaces: ");
        Set<String> tags = Arrays.stream(InputManager.getString().toLowerCase().split(" "))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(HashSet::new));

        List<Salad> result = saladService.getSaladsByTags(tags);

        if (result.isEmpty()) {
            System.out.println("Empty!");
            return;
        }

        System.out.printf("Salads with tags %s:\n", tags);
        result.forEach(System.out::println);
    }

    @Override
    public String getName() {
        return "By tags";
    }

    @Override
    public String getDescription() {
        return "Get salads by all tags with or";
    }
}