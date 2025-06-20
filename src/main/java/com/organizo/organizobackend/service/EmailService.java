package com.organizo.organizobackend.service;

public interface EmailService {
    /**
     * Envia um e-mail simples de texto.
     * @param to      destinatário
     * @param subject assunto
     * @param text    corpo da mensagem
     */
    void sendSimpleMessage(String to, String subject, String text);
}