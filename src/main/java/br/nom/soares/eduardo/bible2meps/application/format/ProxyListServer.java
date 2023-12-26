package br.nom.soares.eduardo.bible2meps.application.format;

public interface ProxyListServer {
    public Proxy getRandomProxy();

    public void readApi();

    public void removeProxy(Proxy proxy);
}
