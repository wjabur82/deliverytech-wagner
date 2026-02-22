package com.deliverytech.delivery_api.health;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Gauge;

import java.util.concurrent.TimeUnit;

@Component
public class DeliveryMetrics {

    private final MeterRegistry registry;
    private final Counter pedidosSucesso;
    private final Counter pedidosErro;
    private double usuariosAtivos = 0;

    public DeliveryMetrics(MeterRegistry registry) {
        this.registry = registry;

        // Contador para pedidos com sucesso
        this.pedidosSucesso = Counter.builder("delivery_pedidos_total")
                .description("Total de pedidos processados")
                .tag("status", "sucesso")
                .register(registry);

        // Contador para pedidos que falharam
        this.pedidosErro = Counter.builder("delivery_pedidos_total")
                .tag("status", "erro")
                .register(registry);

        // Gauge: Mede um valor que sobe e desce (ex: usuários logados agora)
        Gauge.builder("delivery_usuarios_ativos_total", () -> usuariosAtivos)
                .description("Número de usuários com sessão ativa")
                .register(registry);
    }

    public void registrarPedido(boolean sucesso) {
        if (sucesso) pedidosSucesso.increment();
        else pedidosErro.increment();
    }

    public void setUsuariosAtivos(double qtd) {
        this.usuariosAtivos = qtd;
    }

    public void registrarTempoProcessamento(long ms) {
        Timer.builder("delivery_processamento_pedidos_seconds")
                .register(registry)
                .record(ms, TimeUnit.MILLISECONDS);
    }

}
