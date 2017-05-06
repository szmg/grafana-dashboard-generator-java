package com.szmg.jsonbuildergenerator;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * In fact it doesn't build anything like Json. It's a bad name.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JsonBuilderGenerator extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true)
    protected MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/jsonbuilder", required = true)
    protected File outputJavaDirectory;

    @Parameter(required = true)
    protected List<FileSet> descriptors;

    @Parameter
    protected String packageName;

    private CodeGenerator codeGenerator = new CodeGenerator();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.project != null) {
            this.project.addCompileSourceRoot(this.outputJavaDirectory.getAbsolutePath());
        }
        if (!this.outputJavaDirectory.mkdirs()) {
            getLog().error("Could not create source directory! (it might already exist)");
        } else {
            try {
                generateJavaCode();
            } catch (IOException e) {
                throw new MojoExecutionException("Could not generate Java source code!", e);
            }
        }
    }

    private void generateJavaCode() throws IOException, MojoExecutionException, MojoFailureException {

        for (FileSet descriptor : descriptors) {
            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(descriptor.getDirectory());
            scanner.setIncludes(toArrayOrNull(descriptor.getIncludes()));
            scanner.setExcludes(toArrayOrNull(descriptor.getExcludes()));
            scanner.scan();
            for (String yamlPath : scanner.getIncludedFiles()) {
                generateJavaCodeFor(new File(scanner.getBasedir(), yamlPath));
            }
        }
    }

    private void generateJavaCodeFor(File yamlPath) throws MojoExecutionException, MojoFailureException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            MappingIterator<DomainDescription> iterator = mapper.readerFor(DomainDescription.class).readValues(yamlPath);
            while (iterator.hasNextValue()) {
                generateJavaCodeFor(iterator.next());
            }
        } catch (IOException e) {
            getLog().error(String.format("Cannot parse descriptor [%s]", yamlPath), e);
            throw new MojoExecutionException(String.format("Cannot parse descriptor [%s]", yamlPath), e);
        }
    }

    private void generateJavaCodeFor(DomainDescription domain) throws MojoFailureException {
        getLog().info(String.format("Generating %s", domain.getName()));

        String code = codeGenerator.generateJavaCodeFor(domain, packageName);
        File javaFile = createDirsAndGetSourceFile(domain);
        writeToFile(javaFile, code);
    }

    private void writeToFile(File file, String content) throws MojoFailureException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new MojoFailureException("Could not write source file", e);
        }
    }

    private File createDirsAndGetSourceFile(DomainDescription domain) {
        File dir = new File(outputJavaDirectory.getAbsolutePath());
        if (StringUtils.isNotBlank(packageName)) {
            dir = new File(dir, packageName.replaceAll("\\.", File.separator));
        }
        dir.mkdirs();

        return new File(dir, domain.getName() + ".java");
    }

    private static String[] toArrayOrNull(List<String> list) {
        String[] result = null;
        if (list != null && !list.isEmpty()) {
            result = list.toArray(new String[list.size()]);
        }
        return result;
    }
}
