import React from 'react';
import { Link } from 'react-router-dom';
// import '../styles/Dashboard.css'; // Removido: Estilos agora são globais ou utilitários de index.css

/**
 * Dashboard do Profissional:
 * - Visão geral da agenda e acesso rápido às funcionalidades.
 * - Utiliza classes globais de estilo definidas em `index.css`.
 * - TODO: Implementar busca de dados reais (agenda, perfil).
 */
export default function ProfissionalDashboard() {
  // TODO: Buscar dados do profissional (nome, salão vinculado, próximos agendamentos)

  return (
    // Container principal da página, aplicando espaçamento padrão do dashboard
    <main className="container dashboard py-lg md:py-xl"> {/* Adicionado padding vertical */}

      {/* Cabeçalho da seção do dashboard */}
      <div className="dashboard__header mb-xl">
        {/* TODO: Usar o nome real do profissional */}
        <h1 className="dashboard__title text-2xl font-bold mb-xs">Olá, Profissional!</h1>
        <p className="dashboard__subtitle text-muted">Gerencie sua agenda e veja seus próximos compromissos.</p>
      </div>

      {/* Seção de ações rápidas */}
      <div className="dashboard__actions mb-xl">
        {/* Link estilizado como botão primário */}
        <Link to="/profissional/agenda" className="btn btn--primary">
          Minha Agenda Detalhada
        </Link>
        {/* Adicionar mais links/ações conforme necessário (ex: Editar Perfil, Gerenciar Disponibilidade) */}
        {/* <Link to="/profissional/disponibilidade" className="btn btn--secondary">Gerenciar Disponibilidade</Link> */}
      </div>

      {/* Seção de Próximos Agendamentos (Exemplo) */}
      <section>
        <h2 className="text-xl font-semibold mb-lg">Próximos Agendamentos</h2>
        <div className="card">
          <div className="card__body">
            {/* TODO: Implementar lista/calendário resumido dos próximos agendamentos */}
            <p className="text-muted">Lista de próximos agendamentos aqui...</p>
            {/* Exemplo: */}
            <ul>
              <li>02/06/2025 14:00 - Corte - Cliente X</li>
              <li>02/06/2025 15:00 - Barba - Cliente Y</li>
            </ul>
          </div>
        </div>
      </section>

    </main>
  );
}

