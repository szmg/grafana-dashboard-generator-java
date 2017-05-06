package com.szmg.jsonbuildergenerator;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.maven.plugin.MojoFailureException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {

    private static final String CLASS_TEMPLATE = "class.ftlh";
    private static final String TEMPLATE_PATH = "templates";

    private final Configuration templateCfg;
    private final Formatter codeFormatter;

    public CodeGenerator() {
        templateCfg = new Configuration(Configuration.VERSION_2_3_23);
        templateCfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), TEMPLATE_PATH);
        templateCfg.setDefaultEncoding("UTF-8");
        templateCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        templateCfg.setLogTemplateExceptions(false);

        codeFormatter = new Formatter();
    }

    public String generateJavaCodeFor(DomainDescription domain, String packageName) throws MojoFailureException {
        Map<String, Object> model = new HashMap<>();
        model.put("domain", domain);
        model.put("packageName", packageName);

        Template temp = getTemplate();
        String code = generateCode(model, temp);
        return formatCode(code);
    }

    private String formatCode(String code) throws MojoFailureException {
        try {
            return codeFormatter.formatSource(code);
        } catch (FormatterException e) {
            throw new MojoFailureException("Cannot format source code", e);
        }
    }

    private String generateCode(Map<String, Object> model, Template temp) throws MojoFailureException {
        StringWriter stringWriter = new StringWriter();
        try {
            temp.process(model, stringWriter);
        } catch (TemplateException e) {
            throw new MojoFailureException("Error during source generation", e);
        } catch (IOException e) {
            throw new MojoFailureException("This just cannot happen... can it?", e);
        }
        return stringWriter.toString();
    }

    private Template getTemplate() throws MojoFailureException {
        try {
            return templateCfg.getTemplate(CLASS_TEMPLATE);
        } catch (IOException e) {
            throw new MojoFailureException("Could not load class template", e);
        }
    }

}
