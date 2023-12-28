package br.nom.soares.eduardo.bible2meps.application.format;

import br.nom.soares.eduardo.bible2meps.domain.Proxy;

public interface ProxyListServer {
    public Proxy getRandomProxy();

    public void removeProxy(Proxy proxy);
}
