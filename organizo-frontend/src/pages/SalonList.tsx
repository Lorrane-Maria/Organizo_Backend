import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getSalons } from '../api';
import { Card } from '../components/Card';
import { Button } from '../components/Button'; // certifique-se de que esse caminho está correto

// Tipagem do salão (você pode mover isso para um arquivo types.ts se quiser)
interface Servico {
  id: number;
  nome: string;
  preco: number;
}

interface Salon {
  id: number;
  nome: string;
  endereco: string;
  telefone: string;
  servicos: Servico[];
}

/**
 * Página que lista os salões disponíveis.
 */
export default function SalonList() {
  const [salons, setSalons] = useState<Salon[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchSalons() {
      try {
        const data = await getSalons();
        setSalons(data);
      } catch (err) {
        console.error('Erro ao carregar salões:', err);
        setError('Erro ao carregar salões. Tente novamente mais tarde.');
      } finally {
        setLoading(false);
      }
    }

    fetchSalons();
  }, []);

  if (loading) return <p>Carregando salões...</p>;
  if (error) return <p>{error}</p>;
  if (salons.length === 0) return <p>Nenhum salão encontrado.</p>;

  return (
    <main className="container salons">
      <h1>Salões Próximos</h1>

      {salons.map((s) => (
        <Card key={s.id} title={s.nome}>
          <p><strong>Endereço:</strong> {s.endereco}</p>
          <p><strong>Telefone:</strong> {s.telefone}</p>

          <Button
            variant="primary"
            onClick={() => navigate(`/book/${s.id}/${s.servicos[0]?.id}`)}
          >
            Ver Serviços
          </Button>
        </Card>
      ))}
    </main>
  );
}
