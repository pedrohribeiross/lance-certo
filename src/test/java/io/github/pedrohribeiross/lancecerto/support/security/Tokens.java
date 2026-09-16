package io.github.pedrohribeiross.lancecerto.support.security;

public final class Tokens {

    private static final TestTokenFactory APP = TestTokenFactory.app();
    private static final TestTokenFactory ROGUE = TestTokenFactory.rogue();

    private Tokens() {}

    // Token assinado pela chave da aplicação: deve ser aceito.
    public static TokenBuilder signedByAppKey(){
        return new TokenBuilder(APP);
    }

    // Token assinado por chave desconhecida: deve ser rejeitado.
    public static TokenBuilder signedByAnotherKey() {
        return new TokenBuilder(ROGUE);
    }
}
