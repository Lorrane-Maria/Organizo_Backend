// src/pages/Dashboard.tsx
import React, { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { Card } from '../components/Card';
import { getAgendamentosPorUsuario } from '../api';
import { Button } from '../components/Button';

/**
 * Dashboard principal: mostra agendamentos recentes
 * e possui botão para marcar novo.
 */
export default function Dashboard() {
  const { user } = useContext(AuthContext);
  const [bookings, setBookings] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAgendamentosPorUsuario(user.id)
      .then(setBookings)
      .finally(() => setLoading(false));
  }, [user.id]);

  return (
    <main className="container dashboard">
      <h1>Olá, {user.nome}</h1>
      <Button variant="accent" onClick={() => window.location.href = '/salons'}>
        Novo Agendamento
      </Button>

      {loading
        ? <p>Carregando agendamentos...</p>
        : bookings.length === 0
          ? <p>Você não tem agendamentos.</p>
          : bookings.map(b => (
              <Card key={b.id} title={`Agendamento #${b.id}`}>
                <p><strong>Data:</strong> {new Date(b.dataHoraAgendada).toLocaleString()}</p>
                <p><strong>Serviço:</strong> {b.servico.nome}</p>
                <p><strong>Status:</strong> {b.status}</p>
              </Card>
            ))
      }
    </main>
  );
}
