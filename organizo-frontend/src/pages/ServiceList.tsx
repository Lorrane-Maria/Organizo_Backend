import React, { useState, useEffect } from 'react';
import { ServiceCard, Service } from '../components/ServiceCard';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

interface Paginated<T> {
  content: T[];
  number: number;
  size: number;
  totalPages: number;
  totalElements: number;
}

export default function ServiceList() {
  const { salonId } = useParams<{ salonId: string }>();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [data, setData] = useState<Paginated<Service> | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchServices() {
      setLoading(true);
      try {
        const resp = await axios.get<Paginated<Service>>(
          `${import.meta.env.VITE_API_URL}/saloes/${salonId}/servicos`,
          { params: { page, size: 6 } }
        );
        setData(resp.data);
      } catch (e: any) {
        setError('Não foi possível carregar serviços.');
      } finally {
        setLoading(false);
      }
    }
    fetchServices();
  }, [salonId, page]);

  if (loading) return <p>Carregando serviços…</p>;
  if (error)   return <p className="text-red-500">{error}</p>;

  return (
    <main className="container py-6">
      <h1 className="text-2xl font-bold mb-4">Serviços disponíveis</h1>

      {/* GRID RESPONSIVA */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {data?.content.map(s => (
          <ServiceCard
            key={s.id}
            service={s}
            onSelect={serviceId =>
              navigate(`/salons/${salonId}/services/${serviceId}/professionals`)
            }
          />
        ))}
      </div>

      {/* PAGINAÇÃO */}
      <div className="mt-6 flex justify-center space-x-2">
        <button
          disabled={page === 0}
          onClick={() => setPage(p => p - 1)}
          className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Anterior
        </button>
        {[...Array(data?.totalPages || 0)].map((_, i) => (
          <button
            key={i}
            onClick={() => setPage(i)}
            className={`px-3 py-1 rounded ${
              i === page ? 'bg-primary text-white' : 'bg-gray-200'
            }`}
          >
            {i + 1}
          </button>
        ))}
        <button
          disabled={page + 1 >= (data?.totalPages || 0)}
          onClick={() => setPage(p => p + 1)}
          className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Próxima
        </button>
      </div>
    </main>
  );
}
