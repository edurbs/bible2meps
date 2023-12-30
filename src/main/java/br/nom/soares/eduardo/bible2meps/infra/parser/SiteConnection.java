package br.nom.soares.eduardo.bible2meps.infra.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import br.nom.soares.eduardo.bible2meps.application.format.ProxyListServer;
import br.nom.soares.eduardo.bible2meps.domain.Proxy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SiteConnection {

    @NonNull
    private ProxyListServer proxyListServer;

    public Document getDocument(String url) {
        Document document = null;
        var proxy = new Proxy("", 0);
        while (document == null) {
            document = tryToGetDocument(url, proxy.host(), proxy.port());
            if (document == null) {
                proxyListServer.removeProxy(proxy);
                proxy = proxyListServer.getRandomProxy();
                System.out.println("Trying proxy: " + proxy.host()+":"+proxy.port());
            }
        }
        return document;
    }

    private Document tryToGetDocument(String url, String host, int port) {
        Connection connection;
        if (port == 0 || host.isEmpty()) {
            connection = Jsoup.connect(url);
        } else {
            connection = Jsoup.connect(url).proxy(host, port).timeout(5 * 1000);
        }
        try {
            return connection.get();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
