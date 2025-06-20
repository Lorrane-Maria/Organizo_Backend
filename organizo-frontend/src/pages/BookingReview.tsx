import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Button } from '../components/Button';
import { createBooking, BookingRequest } from '../api';

interface BookingState {
  salonId: number;
  serviceId: number;
  professionalId: number;
  date: string;
  clientId: number;
}

export default function BookingReview() {
  // 1) informa o tipo genérico à useLocation
  const { state } = useLocation<BookingState>();
  // 2) destrutura o state
  const { salonId, serviceId, professionalId, date, clientId } = state;
  const navigate = useNavigate();

  const handleConfirm = async () => {
    const payload: BookingRequest = {
      clienteId: clientId,
      profissionalId: professionalId,
      servicoId: serviceId,
      dataHoraAgendada: date,
    };
    await createBooking(payload);
    navigate('/dashboard', { replace: true });
  };

  return (
    <main className="container">
      <h1>Confirmar Agendamento</h1>
      <p><strong>Salão:</strong> {salonId}</p>
      <p><strong>Serviço:</strong> {serviceId}</p>
      <p><strong>Profissional:</strong> {professionalId}</p>
      <p>
        <strong>Data:</strong>{' '}
        {new Date(date).toLocaleString()}
      </p>
      <Button variant="primary" onClick={handleConfirm}>
        Confirmar
      </Button>
    </main>
  );
}
