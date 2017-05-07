package uk.co.szmg.jsonbuildergenerator;

/*-
 * #%L
 * domain-generator-maven-plugin
 * %%
 * Copyright (C) 2017 Mate Gabor Szvoboda
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
import java.util.List;
import java.util.Map;

public class CodeGenerator {

    private static final String CLASS_TEMPLATE = "class.ftl";
    private static final String FACTORIES_TEMPLATE = "factories.ftl";
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

        Template temp = getTemplate(CLASS_TEMPLATE);
        String code = generateCode(model, temp);
        return formatCode(code);
    }

    public String generateFactories(List<DomainDescription> domains, String packageName) throws MojoFailureException {
        Map<String, Object> model = new HashMap<>();
        model.put("domains", domains);
        model.put("packageName", packageName);

        Template temp = getTemplate(FACTORIES_TEMPLATE);
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

    private Template getTemplate(String name) throws MojoFailureException {
        try {
            return templateCfg.getTemplate(name);
        } catch (IOException e) {
            throw new MojoFailureException("Could not load template", e);
        }
    }
}
