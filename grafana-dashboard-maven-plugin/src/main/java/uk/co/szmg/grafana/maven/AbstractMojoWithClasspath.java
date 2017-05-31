package uk.co.szmg.grafana.maven;

/*-
 * #%L
 * grafana-dashboard-maven-plugin
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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the {@link #getClassLoader()} method that creates a new classloader
 * with runtime dependencies, build output directory and resources on classpath.
 */
public abstract class AbstractMojoWithClasspath extends AbstractMojo {

    @Parameter( defaultValue = "${project}", readonly = true )
    protected MavenProject project;

    /**
     * Creates and new class loader that has runtime dependencies, build output
     * and resources on class path.
     * @return new class loader
     * @throws MojoFailureException when one of the class path elements is misconfigured, should never happen
     */
    protected ClassLoader getClassLoader() throws MojoFailureException {
        List<URL> classpath = null;
        try {
            classpath = getClasspathUrls();
        } catch (MalformedURLException e) {
            throw new MojoFailureException("Cannot load classpath", e);
        }

        // org.springframework.beans.factory.annotation.Autowire is loaded by this classloader already.
        // If the parent classloader is null, which would be preferred, then it throws ClassCastException
        // as is cannot case the two different class instance between each other.
        return new URLClassLoader(classpath.toArray(new URL[classpath.size()]), getClass().getClassLoader());
    }

    private List<URL> getClasspathUrls() throws MalformedURLException {
        List<URL> classpath = new ArrayList<>();

        for (Resource resource : project.getBuild().getResources()) {
            classpath.add(new File(resource.getDirectory()).toURI().toURL());
        }

        classpath.add(new File(project.getBuild().getOutputDirectory()).toURI().toURL());

        for (Object o : project.getRuntimeArtifacts()) {
            Artifact artifact = (Artifact) o;
            classpath.add(artifact.getFile().toURI().toURL());
        }

        getLog().debug("classpath: " + classpath);

        return classpath;
    }
}
