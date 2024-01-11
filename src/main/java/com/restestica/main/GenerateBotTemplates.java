package com.restestica.main;

import com.botica.utils.templates.TemplateGenerator;

public class GenerateBotTemplates {

    private static final String BOTS_DEFINITION_PATH = "src/main/java/com/restestica/bots";
    private static final String PROPERTIES_PATH = "src/main/resources/BOTICAConfig";

    private static String templateType = null;
    private static String template = null;
    private static String outputPath = "";

    public static void main(String[] args) {

        if (args.length > 0) {
            templateType = args[0];
        }

        if (args.length > 1) {
            template = args[1];
        }

        if (args.length > 2) {
            outputPath = args[2];
        } else {
            if (templateType != null) {
                if (templateType.equals("bots-definition")) {
                    outputPath = BOTS_DEFINITION_PATH;
                } else if (templateType.equals("properties")) {
                    outputPath = PROPERTIES_PATH;
                }
            }
        }

        TemplateGenerator.generateTemplate(templateType, template, outputPath);

    }
}
