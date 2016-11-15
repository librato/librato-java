package com.librato.metrics.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Encapsulates logic about versions found in POM files
 */
public class Versions {
    private static final Logger LOG = LoggerFactory.getLogger(Versions.class);

    private Versions() {
        // do not construct.
    }

    /**
     * Attempts to get a version property from a specified resource
     *
     * @param path  the path of the properties file resource
     * @param klass the Class whose classloader will be used to load the resource
     * @return the found version, "unknown" if it could not be found / determined
     */
    public static String getVersion(String path, Class<?> klass) {
        try {
            final InputStream in = klass.getClassLoader().getResourceAsStream(path);
            if (in != null) {
                try {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = reader.readLine();
                    while (line != null) {
                        if (line.startsWith("version")) {
                            return line.split("=")[1];
                        }
                        line = reader.readLine();
                    }
                } finally {
                    in.close();
                }
            }
        } catch (IOException e) {
            LOG.error("Could not read package version using path " + path + ":", e);
        }
        return "unknown";
    }

}
