package org.egorik.command;

public interface Command {
    void execute();

    String getName();

    String getDescription();
}