package com.text.search.lucene;

import com.text.search.lucene.manager.StartupManager;
import com.text.search.lucene.configuration.ApplicationConfiguration;
import com.text.search.lucene.resource.DocumentResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.IOException;

public class MainClass extends Application<ApplicationConfiguration> {
    public static void main(String[] args) throws Exception {
        new MainClass().run(args);
    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws IOException {

        // You must register the endpoints that you want to expose
        StartupManager.getInstance().setAnalyzerConfiguration(applicationConfiguration);
        environment.jersey().register(new DocumentResource());
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    }
}