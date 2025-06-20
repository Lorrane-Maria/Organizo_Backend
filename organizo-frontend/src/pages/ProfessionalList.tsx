import React, { useState, useEffect } from 'react';
import { ProfessionalCard, Professional } from '../components/ProfessionalCard';
import { useNavigate, useParams } from 'react-router-dom';
// import api from '../api/axiosConfig'; // Caminho incorreto
import api from '../services/api'; // CORRIGIDO: Importa a instância configurada do Axios de services/api.ts
import { toast } from 'react-hot-toast';
import { Spinner } from '../components/ui/Spinner';

/**
 * Interface para dados paginados retornados pela API.
 * @template T O tipo dos itens na lista de conteúdo.
 */
interface Paginated<T> {
  content: T[];      // Array com os itens da página atual
  number: number;      // Número da página atual (base 0)
  totalPages: number;  // Número total de páginas
}

/**
 * Página para listar profissionais disponíveis para um serviço específico em um salão.
 * Obtém dados da API e permite a seleção de um profissional para agendamento.
 */
export default function ProfessionalList() {
  // Obtém IDs do salão e serviço dos parâmetros da URL
  const { salonId, serviceId } = useParams<{ salonId: string; serviceId: string }>();
  const navigate = useNavigate(); // Hook para navegação programática

  // Estados do componente
  const [data, setData] = useState<Paginated<Professional> | null>(null); // Armazena os dados paginados dos profissionais
  const [page, setPage] = useState(0); // Controla a página atual da paginação (base 0)
  const [loading, setLoading] = useState(true); // Indica se os dados estão sendo carregados

  // Efeito para buscar os dados da API quando o componente monta ou os IDs/página mudam
  useEffect(() => {
    setLoading(true); // Inicia o carregamento
    api.get<Paginated<Professional>>(
      // Endpoint da API para buscar profissionais por salão e serviço
      `/saloes/${salonId}/servicos/${serviceId}/profissionais`,
      { params: { page, size: 6 } } // Parâmetros de paginação (página atual, tamanho da página)
    )
    .then(resp => setData(resp.data)) // Atualiza o estado com os dados recebidos
    .catch(err => {
      toast.error('Erro ao carregar profissionais'); // Exibe notificação de erro
      console.error("Erro buscando profissionais:", err); // Loga o erro no console
    })
    .finally(() => setLoading(false)); // Finaliza o carregamento, mesmo em caso de erro
  }, [salonId, serviceId, page]); // Dependências do efeito: re-executa se algum mudar

  // Exibe um spinner enquanto os dados estão carregando
  if (loading) {
    return (
      <div className="flex justify-center py-xl"> {/* Centraliza o spinner com padding */}
        <Spinner />
      </div>
    );
  }

  // Renderização principal da página
  return (
    <main className="container py-lg"> {/* Container com padding vertical */}
      <h1 className="text-2xl font-bold mb-lg">Escolha o Profissional</h1> {/* Título da página */}

      {/* Grid responsivo para exibir os cards dos profissionais */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-lg">
        {/* Mapeia os profissionais da página atual para componentes ProfessionalCard */}
        {data?.content.map(p => (
          <ProfessionalCard
            key={p.id} // Chave única para cada card
            professional={p} // Passa os dados do profissional para o card
            onSelect={id =>
              // Navega para a página do calendário ao selecionar um profissional
              navigate(`/book/${salonId}/${serviceId}/${id}/calendar`)
            }
          />
        ))}
      </div>

      {/* --- Controles de Paginação --- */}
      {/* Só exibe paginação se houver mais de uma página */}
      {(data?.totalPages ?? 0) > 1 && (
        <div className="mt-xl flex justify-center items-center gap-sm"> {/* Container da paginação */}
          {/* Botão Anterior */}
          <button
            className="btn btn--secondary btn--sm" // Estilo de botão secundário pequeno
            disabled={page === 0} // Desabilitado na primeira página
            onClick={() => setPage(p => p - 1)} // Decrementa a página
          >
            Anterior
          </button>

          {/* Indicador de Página Atual (Opcional, pode ser complexo) */}
          <span className="text-muted text-sm">
            Página {page + 1} de {data?.totalPages}
          </span>

          {/* Botão Próxima */}
          <button
            className="btn btn--secondary btn--sm"
            disabled={page + 1 >= (data?.totalPages || 0)} // Desabilitado na última página
            onClick={() => setPage(p => p + 1)} // Incrementa a página
          >
            Próxima
          </button>
        </div>
      )}
    </main>
  );
}
