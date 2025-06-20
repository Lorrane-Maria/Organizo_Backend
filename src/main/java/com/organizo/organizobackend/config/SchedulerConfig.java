package com.organizo.organizobackend.config;

import com.organizo.organizobackend.enums.StatusAgendamento;
import com.organizo.organizobackend.model.Agendamento;
import com.organizo.organizobackend.repository.AgendamentoRepository;
import com.organizo.organizobackend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Componente responsável por enviar lembretes de agendamentos
 * confirmados que ocorrerão nas próximas 2 horas.
 */
@Component
@EnableScheduling
public class SchedulerConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    private final AgendamentoRepository agRepo;
    private final EmailService emailService;

    @Autowired
    public SchedulerConfig(AgendamentoRepository agRepo,
                           EmailService emailService) {
        this.agRepo       = agRepo;
        this.emailService = emailService;
    }

    /**
     * Executa a cada 15 minutos.
     * – Busca só os agendamentos CONFIRMADOS entre 'agora' e 'agora + 2h'
     * – Para cada um, tenta enviar o e-mail e, em caso de falha, só loga o erro
     */
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void enviarLembretes() {
        LocalDateTime agora  = LocalDateTime.now();
        LocalDateTime limite = agora.plusHours(2);

        // 1) Consulta dedicada ao banco
        List<Agendamento> proximos = agRepo
                .findAllByStatusAndDataHoraAgendadaBetween(
                        StatusAgendamento.CONFIRMADO, agora, limite
                );

        // 2) Itera e envia e-mail de lembrete
        proximos.forEach(ag -> {
            try {
                String texto = String.format(
                        "Olá %s,%n%n" +
                                "Este é um lembrete do seu agendamento de %s " +
                                "hoje às %s.%n%nAtenciosamente.",
                        ag.getCliente().getNome(),
                        ag.getServico().getNome(),
                        ag.getDataHoraAgendada().toLocalTime()
                );
                emailService.sendSimpleMessage(
                        ag.getCliente().getEmail(),
                        "Lembrete de Agendamento",
                        texto
                );
                log.info("Lembrete enviado p/ Agendamento[id={}]", ag.getId());
            } catch (Exception ex) {
                log.error(
                        "Falha ao enviar lembrete p/ Agendamento[id={}]: {}",
                        ag.getId(), ex.getMessage(), ex
                );
            }
        });
    }
}
