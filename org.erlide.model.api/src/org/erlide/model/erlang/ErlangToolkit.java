package org.erlide.model.erlang;

public interface ErlangToolkit {

    IErlParser createParser();

    IErlScanner createScanner(String scannerName, String initialText,
            String path, boolean logging);

}
