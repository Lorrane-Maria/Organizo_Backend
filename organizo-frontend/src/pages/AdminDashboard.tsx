import React, { useState, useEffect } from 'react';
import api from '../services/api'; // Importa a instância configurada do Axios
import { toast } from 'react-hot-toast';
import { Spinner } from '../components/ui/Spinner'; // Componente Spinner para feedback visual
import { Card } from '../components/Card'; // Reutilizar o Card
import { Button } from '../components/Button'; // Reutilizar o Button

// --- Interfaces para os dados da API ---

// Interface para as estatísticas gerais
interface AdminStats {
  totalUsuarios: number;
  totalSaloes: number;
  totalProfissionais: number;
  agendamentosHoje: number;
  // Adicionar outras estatísticas se a API retornar
}

// Interface para um Usuário (simplificada para a lista)
interface Usuario {
  id: number;
  nome: string;
  email: string;
  role: 'CLIENTE' | 'PROFISSIONAL' | 'DONO_SALAO' | 'ADMIN';
  status?: string; // Status pode ser opcional ou vir de outro lugar
  criadoEm?: string; // Data de criação, se disponível
}

// Interface para a resposta paginada da API de usuários (exemplo)
interface PaginatedUsers {
  content: Usuario[];
  totalPages: number;
  number: number; // Página atual
  // Outros campos de paginação se houver (totalElements, size, etc.)
}

/**
 * Dashboard do Administrador:
 * - Busca e exibe estatísticas gerais e lista de usuários do sistema.
 * - Gerencia estados de carregamento e erro.
 * - TODO: Implementar busca de salões, agendamentos, paginação e ações.
 */
export default function AdminDashboard() {
  // --- Estados do Componente ---
  const [stats, setStats] = useState<AdminStats | null>(null);
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loadingStats, setLoadingStats] = useState(true);
  const [loadingUsuarios, setLoadingUsuarios] = useState(true);
  const [errorStats, setErrorStats] = useState<string | null>(null);
  const [errorUsuarios, setErrorUsuarios] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0); // Para paginação futura
  const [totalPages, setTotalPages] = useState(0); // Para paginação futura

  // --- Efeitos para buscar dados da API ---

  // Busca estatísticas gerais
  useEffect(() => {
    setLoadingStats(true);
    setErrorStats(null);
    // TODO: Confirmar o endpoint correto da API para estatísticas do admin
    api.get<AdminStats>('/admin/stats')
      .then(response => {
        setStats(response.data);
      })
      .catch(err => {
        console.error("Erro ao buscar estatísticas:", err);
        setErrorStats('Falha ao carregar estatísticas.');
        toast.error('Erro ao buscar estatísticas.');
      })
      .finally(() => {
        setLoadingStats(false);
      });
  }, []); // Roda apenas na montagem

  // Busca lista de usuários (com paginação básica)
  useEffect(() => {
    setLoadingUsuarios(true);
    setErrorUsuarios(null);
    // TODO: Confirmar o endpoint correto da API para listar usuários (com paginação)
    api.get<PaginatedUsers>('/usuarios', {
      params: { page: currentPage, size: 10 } // Exemplo de paginação
    })
      .then(response => {
        setUsuarios(response.data.content);
        setTotalPages(response.data.totalPages);
      })
      .catch(err => {
        console.error("Erro ao buscar usuários:", err);
        setErrorUsuarios('Falha ao carregar lista de usuários.');
        toast.error('Erro ao buscar usuários.');
      })
      .finally(() => {
        setLoadingUsuarios(false);
      });
  }, [currentPage]); // Re-busca quando a página mudar

  // --- Renderização ---

  return (
    <main className="container py-lg md:py-xl">
      {/* Cabeçalho */}
      <div className="dashboard__header mb-xl">
        <h1 className="text-2xl font-bold mb-xs">Dashboard do Administrador</h1>
        <p className="text-muted">Visão geral e gerenciamento do sistema Organizo.</p>
      </div>

      {/* Seção 1: Visão Geral (Cards) */}
      <section className="mb-xl">
        <h2 className="text-xl font-semibold mb-lg">Visão Geral</h2>
        {loadingStats && <Spinner />} {/* Mostra spinner enquanto carrega stats */}
        {errorStats && <p className="text-red-600">{errorStats}</p>} {/* Mostra erro de stats */}
        {!loadingStats && !errorStats && stats && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-lg">
            {/* Card Total Usuários */}
            <Card>
              <div className="card__body flex flex-col items-start">
                <h3 className="text-muted text-sm font-medium mb-sm">Total de Usuários</h3>
                <p className="text-3xl font-bold text-text">{stats.totalUsuarios ?? 'N/A'}</p>
              </div>
            </Card>
            {/* Card Total Salões */}
            <Card>
              <div className="card__body flex flex-col items-start">
                <h3 className="text-muted text-sm font-medium mb-sm">Salões Ativos</h3>
                <p className="text-3xl font-bold text-text">{stats.totalSaloes ?? 'N/A'}</p>
              </div>
            </Card>
            {/* Card Total Profissionais */}
            <Card>
              <div className="card__body flex flex-col items-start">
                <h3 className="text-muted text-sm font-medium mb-sm">Profissionais</h3>
                <p className="text-3xl font-bold text-text">{stats.totalProfissionais ?? 'N/A'}</p>
              </div>
            </Card>
            {/* Card Agendamentos Hoje */}
            <Card>
              <div className="card__body flex flex-col items-start">
                <h3 className="text-muted text-sm font-medium mb-sm">Agendamentos Hoje</h3>
                <p className="text-3xl font-bold text-accent">{stats.agendamentosHoje ?? 'N/A'}</p>
              </div>
            </Card>
          </div>
        )}
      </section>

      {/* Seção 2: Gerenciamento de Usuários */}
      <section className="mb-xl">
        <div className="flex justify-between items-center mb-lg">
          <h2 className="text-xl font-semibold">Gerenciamento de Usuários</h2>
          {/* TODO: Implementar modal/página para novo usuário */}
          <Button variant="primary" size="sm">Novo Usuário</Button>
        </div>
        <Card className="overflow-x-auto"> {/* Card para conter a tabela */}
          {loadingUsuarios && <div className="p-md"><Spinner /></div>} {/* Spinner dentro do card */}
          {errorUsuarios && <p className="p-md text-red-600">{errorUsuarios}</p>} {/* Erro dentro do card */}
          {!loadingUsuarios && !errorUsuarios && (
            <table className="w-full text-left">
              <thead>
                <tr className="border-b border-border">
                  <th className="p-md font-semibold text-muted text-sm">Nome</th>
                  <th className="p-md font-semibold text-muted text-sm">Email</th>
                  <th className="p-md font-semibold text-muted text-sm">Perfil</th>
                  {/* <th className="p-md font-semibold text-muted text-sm">Status</th> */} {/* Status pode não vir da API principal */}
                  <th className="p-md font-semibold text-muted text-sm">Ações</th>
                </tr>
              </thead>
              <tbody>
                {usuarios.length > 0 ? (
                  usuarios.map((u) => (
                    <tr key={u.id} className="border-b border-border hover:bg-gray-50">
                      <td className="p-md">{u.nome}</td>
                      <td className="p-md">{u.email}</td>
                      <td className="p-md">
                        {/* Badge simples para o perfil */}
                        <span className={`px-2 py-1 text-xs rounded font-medium ${u.role === 'ADMIN' ? 'bg-red-100 text-red-700' : u.role === 'DONO_SALAO' ? 'bg-purple-100 text-purple-700' : u.role === 'PROFISSIONAL' ? 'bg-yellow-100 text-yellow-700' : 'bg-blue-100 text-blue-700'}`}>
                          {u.role}
                        </span>
                      </td>
                      {/* <td className="p-md"><span className={`px-2 py-1 text-xs rounded font-medium ${u.status === 'Ativo' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'}`}>{u.status ?? 'N/A'}</span></td> */}
                      <td className="p-md">
                        {/* TODO: Implementar ações (Ver Detalhes, Editar, etc.) */}
                        <button className="text-accent hover:text-accent-hover text-sm mr-sm">Ver</button>
                        <button className="text-blue-600 hover:text-blue-800 text-sm">Editar</button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={4} className="p-md text-center text-muted">Nenhum usuário encontrado.</td>
                  </tr>
                )}
              </tbody>
            </table>
          )}
          {/* TODO: Implementar componente de Paginação aqui, usando currentPage e totalPages */}
        </Card>
      </section>

      {/* TODO: Implementar Seção 3 (Salões) e Seção 4 (Agendamentos) com busca de dados reais */}
      {/* ... estrutura similar à seção de usuários ... */}

    </main>
  );
}

