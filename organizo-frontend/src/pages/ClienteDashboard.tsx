import React, { useEffect, useState, useContext } from 'react'; // Adicionado useContext
import { Link } from 'react-router-dom';
// import { getSalons } from '../api'; // Remover ou ajustar se a chamada API for centralizada
import api from '../services/api'; // Usar a instância configurada do Axios
import { Card } from '../components/Card';
import { Button } from '../components/Button'; // Importar Button
import { Spinner } from '../components/ui/Spinner'; // Importar Spinner
import { toast } from 'react-hot-toast';
import { AuthContext } from '../contexts/AuthContext'; // Importar AuthContext para obter dados do usuário se necessário
// import '../styles/Dashboard.css'; // Removido: Estilos agora são globais ou utilitários de index.css

// Interface para representar a estrutura de um Salão (ajustar conforme a API)
interface Salon {
  id: number;
  nome: string;
  endereco: string;
  telefone: string;
  // Adicionar outros campos relevantes, como imagem, descrição, etc.
  // servicos?: any[]; // Remover ou ajustar, a lógica de navegação mudou
}

/**
 * Dashboard do Cliente:
 * - Exibe uma lista de salões disponíveis para agendamento.
 * - Busca dados da API.
 * - Mostra estado de carregamento e trata erros.
 * - Cada card de salão leva à lista de serviços daquele salão.
 */
export default function ClienteDashboard() {
  const { role } = useContext(AuthContext); // Obter role para verificar se é cliente
  const [saloes, setSaloes] = useState<Salon[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Efeito para buscar os salões da API quando o componente monta
  useEffect(() => {
    setLoading(true);
    setError(null);
    // Endpoint para buscar salões (ajustar conforme a API real)
    api.get<Salon[]>('/saloes') // Assumindo que a API retorna um array de Saloes
      .then(response => {
        setSaloes(response.data);
      })
      .catch(err => {
        console.error("Erro ao buscar salões:", err);
        setError('Não foi possível carregar os salões. Tente novamente mais tarde.');
        toast.error('Erro ao carregar salões.');
      })
      .finally(() => {
        setLoading(false);
      });
  }, []); // Roda apenas na montagem

  // Exibe o spinner durante o carregamento
  if (loading) {
    return (
      <div className="flex justify-center items-center py-xl">
        <Spinner />
      </div>
    );
  }

  // Exibe mensagem de erro se a busca falhar
  if (error) {
    return (
      <div className="container py-lg text-center text-red-600">
        <p>{error}</p>
      </div>
    );
  }

  return (
    // Container principal com padding
    <main className="container py-lg md:py-xl">
      {/* Cabeçalho do Dashboard */}
      <div className="dashboard__header mb-lg">
        {/* TODO: Usar o nome real do cliente se disponível no AuthContext */}
        <h1 className="text-2xl font-bold mb-xs">Bem-vindo(a)!</h1>
        <p className="text-muted">Escolha um salão abaixo para ver os serviços e agendar.</p>
      </div>

      {/* Grid responsivo para os cards dos salões */}
      {saloes.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-lg">
          {saloes.map(s => (
            // Usando o componente Card genérico
            <Card key={s.id} title={s.nome} className="flex flex-col"> {/* Flex col para alinhar botão no final */}
              {/* Corpo do Card */}
              <div className="flex-grow mb-md"> {/* Faz o conteúdo crescer e empurra o botão para baixo */}
                {/* TODO: Adicionar imagem do salão se disponível */}
                <p className="text-sm text-muted mb-xs">{s.endereco}</p>
                <p className="text-sm text-muted">{s.telefone}</p>
                {/* TODO: Adicionar descrição curta do salão */}
              </div>
              {/* Botão de Ação */}
              <Link to={`/salons/${s.id}/services`} className="mt-auto"> {/* Link envolvendo o botão */}
                <Button variant="primary" className="w-full"> {/* Botão ocupando largura total */}
                  Ver Serviços
                </Button>
              </Link>
            </Card>
          ))}
        </div>
      ) : (
        // Mensagem caso não haja salões
        <div className="text-center py-xl text-muted">
          <p>Nenhum salão encontrado no momento.</p>
        </div>
      )}
    </main>
  );
}

