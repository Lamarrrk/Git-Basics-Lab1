import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownToHtml {

    public static void main(String[] args) {

        String inputFile = args[0];
        String outputFile = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputFile)) {

            StringBuilder htmlContent = new StringBuilder();
            String line;
            boolean inPreformatted = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("```")) {
                    if (inPreformatted) {
                        htmlContent.append("</pre>");
                        inPreformatted = false;
                    } else {
                        htmlContent.append("<pre>");
                        inPreformatted = true;
                    }
                } else if (line.trim().isEmpty() && !inPreformatted) {
                    htmlContent.append("</p><p>");
                } else {
                    String convertedLine = convertMarkdownToHtml(line, inPreformatted);
                    if (inPreformatted) {
                        htmlContent.append(convertedLine).append("\n");
                    } else {
                        if (htmlContent.length() == 0 || htmlContent.toString().endsWith("</p><p>")) {
                            htmlContent.append("<p>").append(convertedLine);
                        } else {
                            htmlContent.append(convertedLine);
                        }
                    }
                }
            }
            if (inPreformatted) {
                htmlContent.append("</pre>");
            } else {
                htmlContent.append("</p>");
            }

            writer.write(htmlContent.toString().replaceAll("\n", ""));
            System.out.println("Конвертація пройшла успішно!");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String convertMarkdownToHtml(String markdown, boolean inPreformatted) {
        if (inPreformatted) {
            return markdown;
        }
        markdown = convertBoldAndItalic(markdown);
        markdown = convertMonospaced(markdown);
        return markdown;
    }

    private static String convertBoldAndItalic(String markdown) {
        markdown = markdown.replaceAll("\\*\\*(.+?)\\*\\*", "<b>$1</b>");
        markdown = markdown.replaceAll("__(.+?)__", "<b>$1</b>");
        markdown = markdown.replaceAll("\\*(.+?)\\*", "<i>$1</i>");
        markdown = markdown.replaceAll("_(.+?)_", "<i>$1</i>");
        return markdown;
    }

    private static String convertMonospaced(String markdown) {
        return markdown.replaceAll("`(.+?)`", "<tt>$1</tt>");

    }
}
