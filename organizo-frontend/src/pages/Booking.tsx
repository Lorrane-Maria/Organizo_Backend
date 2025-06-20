// src/pages/BookingPage.tsx
import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getSalonById, getServiceById, createBooking, BookingRequest } from '../api';
import { Card } from '../components/Card';
import { Button } from '../components/Button';
import { AuthContext } from '../contexts/AuthContext';

interface Salao {
  id: number;
  nome: string;
  endereco: string;
}

interface Servico {
  id: number;
  nome: string;
  descricao: string;
  duracaoMinutos: number;
  preco: number;
}

export default function BookingPage() {
  const { salonId, serviceId } = useParams<{ salonId: string; serviceId: string }>();
  const [salao, setSalao] = useState<Salao | null>(null);
  const [servico, setServico] = useState<Servico | null>(null);
  const [dataHora, setDataHora] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);

  useEffect(() => {
    async function fetchData() {
      try {
        if (!salonId || !serviceId) throw new Error('Parâmetros inválidos');
        const [s, sv] = await Promise.all([
          getSalonById(Number(salonId)),
          getServiceById(Number(serviceId)),
        ]);
        setSalao(s);
        setServico(sv);
      } catch (err: any) {
        console.error(err);
        setError('Não foi possível carregar os dados. Tente novamente.');
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, [salonId, serviceId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.id) {
      setError('Você precisa estar logado para agendar.');
      return;
    }
    try {
      const payload: BookingRequest = {
        clienteId: user.id,
        profissionalId: +serviceId!,        // supondo service traz id e profissional embutido? ajuste se precisar
        servicoId: +serviceId!,
        dataHoraAgendada: dataHora,
      };
      await createBooking(payload);
      alert('Agendamento realizado com sucesso!');
      navigate('/');
    } catch (err: any) {
      console.error(err);
      setError('Falha ao criar agendamento. Tente novamente.');
    }
  };

  if (loading) return <p>Carregando...</p>;
  if (error) return <p className="error">{error}</p>;
  if (!salao || !servico) return <p>Dados incompletos.</p>;

  return (
    <main className="container booking">
      <h1>Agendar em: {salao.nome}</h1>
      <Card title={servico.nome}>
        <p>{servico.descricao}</p>
        <p><strong>Duração:</strong> {servico.duracaoMinutos} minutos</p>
        <p><strong>Preço:</strong> R$ {servico.preco.toFixed(2)}</p>
      </Card>

      <form onSubmit={handleSubmit} className="booking__form">
        <label htmlFor="dataHora">Data e Hora</label>
        <input
          id="dataHora"
          type="datetime-local"
          value={dataHora}
          required
          onChange={(e) => setDataHora(e.target.value)}
        />

        <Button type="submit" variant="primary">
          Confirmar Agendamento
        </Button>
      </form>
    </main>
  );
}
