package br.com.alura.literAlura.services;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
