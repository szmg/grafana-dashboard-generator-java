package com.szmg.jsonbuildergenerator;


import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.VirtualMachine;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

/**
 * In fact it doesn't build anything like Json. It's a bad name.
 *
 * @goal generate
 * @phase generate-sources
 * @description generate json builder sources
 */
public class JsonBuilderGenerator extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @required
     */
    protected MavenProject project;

    /**
     * @parameter expression=
     *            "${project.build.directory}/generated-sources/jsonbuilder"
     * @required
     */
    protected File outputJavaDirectory;

    /**
     * @parameter
     */
    protected String title;

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

    private void generateJavaCode() throws IOException {
        //System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        VirtualMachine vm = VirtualMachine.getVirtualMachine();
        CompilationUnit unit = vm.newCompilationUnit(this.outputJavaDirectory.getAbsolutePath());

        unit.setNamespace("com.szmg.some.stuff");
        PackageClass c = unit.newPublicClass("IAmGenerated");
        ClassMethod callMe = c.newMethod(vm.newType(Type.VOID), "callMe");
        callMe.setAccess(Access.PUBLIC);
        callMe.newStmt(vm.newInvoke("System.out", "println").addArg("Hello! I'm generated!"));

        unit.encode();
    }
}
