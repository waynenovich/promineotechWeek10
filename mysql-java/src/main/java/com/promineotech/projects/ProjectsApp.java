package com.promineotech.projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.promineotech.projects.entity.Project;
import com.promineotech.projects.service.ProjectService;

public class ProjectsApp {
    // @formatter:off
    private List<String> operations = List.of(
        "1) Add a project",
        "2) List projects",
        "3) Select a project"
    );
    // @formatter:on
    private Scanner scanner = new Scanner(System.in);
    private ProjectService projectService = new ProjectService();
    private Project curProject;

    public static void main(String[] args) {
        new ProjectsApp().processUserSelections();
    }

    private void processUserSelections() {
        boolean done = false;

        while (!done) {
            try {
                int selection = getUserSelection();
                switch (selection) {
                    case -1:
                        done = exitMenu();
                        break;
                    case 1:
                        createProject();
                        break;
                    case 2:
                        listProjects();
                        break;
                    case 3:
                        selectProject();
                        break;
                    default:
                        System.out.println("\n" + selection + " is not a valid selection.");
                }

            } catch (Exception e) {
                System.out.println("\nError: " + e.toString());
            }
        }
    }

    private void selectProject() {
        listProjects();
        Integer projectId = getIntInput("Enter a project ID to select a project");
        curProject = null;
        curProject = projectService.fetchProjectById(projectId);
    }

    private void listProjects() {
        List<Project> projects = projectService.fetchAllProjects();
        System.out.println("\nProjects:");
        projects.forEach(project -> System.out
                .println("   " + project.getProjectId() + ": " + project.getProjectName()));
    }

    private void createProject() {
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
        String notes = getStringInput("Enter the project notes");

        Project project = new Project();
        project.setProjectName(projectName);
        project.setEstimatedHours(estimatedHours);
        project.setActualHours(actualHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);

        var dbProject = projectService.addProject(project);
        System.out.println("You have successfully created project: " + dbProject);
    }

    private int getUserSelection() {
        printOperations();
        Integer input = getIntInput("Enter a menu selection");
        return input == null ? -1 : input;
    }

    private void printOperations() {
        System.out.println("\nThese are the available selections:");
        operations.forEach(op -> System.out.println("  " + op));

        if(Objects.isNull(curProject)){
            System.out.println("\nYou are not working with a project.");
        } else {
            System.out.println("\nYou are working with project: " + curProject);
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();
        return input.isBlank() ? null : input.trim();
    }

    private Integer getIntInput(String prompt) {
        String input = getStringInput(prompt);
        if (input == null) {
            return null;
        }
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new RuntimeException(input + " is not a valid number. Try again.");
        }
    }

    private BigDecimal getDecimalInput(String prompt) {
        String input = getStringInput(prompt);
        if (input == null) {
            return null;
        }
        try {
            return new BigDecimal(input).setScale(2);
        } catch (NumberFormatException e) {
            throw new RuntimeException(input + " is not a valid decimal. Try again.");
        }
    }

    private boolean exitMenu() {
        System.out.println("\nExiting the menu.");
        return true;
    }
}
