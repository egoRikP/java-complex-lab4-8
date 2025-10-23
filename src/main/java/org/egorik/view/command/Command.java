package org.egorik.view.command;

public interface Command {
    void execute();

    String getName();

    String getDescription();
}