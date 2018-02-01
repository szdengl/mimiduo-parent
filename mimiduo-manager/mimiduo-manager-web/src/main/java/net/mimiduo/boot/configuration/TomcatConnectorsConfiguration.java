package net.mimiduo.boot.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.mimiduo.boot.common.service.AppConfig;
import org.apache.catalina.connector.Connector;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;


@Configuration
public class TomcatConnectorsConfiguration {

    private static Logger LOG = LoggerFactory.getLogger(TomcatConnectorsConfiguration.class);
    @Autowired
    private AppConfig appConfig;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

        if (appConfig.getSSLEnabled()) {
            tomcat.addAdditionalTomcatConnectors(createSslConnector());
            LOG.info("add Tomcat Ssl Connector");
        }

        return tomcat;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            String keystorePath = appConfig.getProperty("server.keystore", "classpath:conf/https/keystore");
            if (LOG.isDebugEnabled()) {
                LOG.debug("server.keystore:{}", keystorePath);
            }
            File keystore = getFile(keystorePath, "keystore");
            File truststore = getFile(appConfig.getProperty("server.truststore", keystorePath), "truststore");
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(appConfig.getSSLPort());
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(appConfig.getProperty("server.keystorePass", "NConverge2014"));
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststorePass(appConfig.getProperty("server.truststorePass", protocol.getKeystorePass()));
            protocol.setKeyAlias(appConfig.getProperty("server.keyAlias", "tomcat"));
            return connector;
        } catch (IOException ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore" + "] or truststore: [" + "keystore"
                    + "]", ex);
        }
    }

    //NOTICE: jar file -> File
    private File getFile(String resourceLocation, String key) throws IOException {
        final URL url = ResourceUtils.getURL(resourceLocation);
        if (ResourceUtils.isJarURL(url)) {
            String dirPath = appConfig.getProperty("data", "data") + "/.keys";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File target = new File(dirPath, key);
            FileUtils.copyURLToFile(url, target);
            return target;
        } else {
            return ResourceUtils.getFile(resourceLocation);
        }
    }
}
