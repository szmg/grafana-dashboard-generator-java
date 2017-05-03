package com.szmg.jsonbuildergenerator;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ClassField;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.FormalParameter;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.StringLiteral;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.Variable;
import net.sourceforge.jenesis4java.VirtualMachine;
import org.apache.maven.model.FileSet;
import org.apache.maven.model.Resource;
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

        //System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());
        VirtualMachine vm = VirtualMachine.getVirtualMachine();
        CompilationUnit unit = vm.newCompilationUnit(this.outputJavaDirectory.getAbsolutePath());

        if (StringUtils.isNotBlank(packageName)) {
            unit.setNamespace(packageName);
        }

        PackageClass c = unit.newPublicClass(domain.getName());
        if (domain.isAbstract()) {
            c.isAbstract(true);
        }

        if (StringUtils.isNotBlank(domain.getExtendedClass())) {
            c.setExtends(domain.getExtendedClass());
        } else {
            // TODO provide this with generation or make it configurable
            c.setExtends("com.szmg.grafana.domain.BaseJsonObject");
        }

        for (FieldDescription field : domain.getFields()) {
            addField(vm, c, field);
        }

        try {
            unit.encode();
        } catch (IOException e) {
            getLog().error(String.format("Error during generating [%s]", domain.getName()), e);
            throw new MojoFailureException(String.format("Error during generating [%s]", domain.getName()), e);
        }
    }

    private void addField(VirtualMachine vm, PackageClass c, FieldDescription field) {
        String name = field.getName();
        String fieldName = "FIELD_" + ccToUnderscoreUppercase(name);
        Variable fieldNameVar = vm.newVar(fieldName);

        ClassField staticField = c.newField(String.class, fieldName);
        staticField.setAccess(Access.PROTECTED);
        staticField.isStatic(true);
        staticField.isFinal(true);
        staticField.setExpression(vm.newString(name));

        String capName = StringUtils.capitalise(name);
        Type fieldType = vm.newType(field.getType());
        ClassMethod setter = c.newMethod(vm.newType(Type.VOID), "set" + capName);
        setter.setAccess(Access.PUBLIC);
        setter.addParameter(fieldType, name);
        setter.newStmt(vm.newInvoke("this", "addValue").addArg(fieldNameVar).addArg(vm.newVar(name)));

        ClassMethod getter = c.newMethod(fieldType, "get" + capName);
        getter.setAccess(Access.PUBLIC);
        getter.newReturn().setExpression(vm.newInvoke("this", "getValue").addArg(fieldNameVar));
    }

    private static String[] toArrayOrNull(List<String> list) {
        String[] result = null;
        if (list != null && !list.isEmpty()) {
            result = list.toArray(new String[list.size()]);
        }
        return result;
    }

    private static String ccToUnderscoreUppercase(String cc) {
        return cc.replaceAll("[A-Z]", "_$0").toUpperCase();
    }

}
